package com.clnine.kimpd.src.Web.user;

import com.clnine.kimpd.src.Web.user.models.GetExpertsRes;
import com.clnine.kimpd.src.Web.user.models.GetUsersRes;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Stream;

@Repository // => JPA => Hibernate => ORM => Database 객체지향으로 접근하게 해주는 도구이다
public interface UserInfoRepository extends CrudRepository<UserInfo, Integer> {
    List<UserInfo> findByStatus(String status);
    List<UserInfo> findByEmailAndStatus(String email, String status);
    List<UserInfo> findByStatusAndNicknameIsContaining(String status, String word);
    List<UserInfo> findByIdAndStatus(String id,String status);
    boolean existsByIdAndStatus(String id,String status);
    boolean existsByNicknameAndStatus(String nickname,String status);
    UserInfo findUserInfoByUserIdxAndStatus(int userIdx,String status);

    List<UserInfo> findByStatusAndUserType(String status,int userType);
    List<UserInfo> findByStatusAndUserTypeAndNicknameIsContaining(String status,int userType,String word);
    List<UserInfo> findByPhoneNumAndStatus(String phoneNum,String status);

    // 닉네임과 소개에 word가 포함된 전문가이면서 ACTIVE인 유저리스트
    List<UserInfo> findByUserTypeAndStatusAndNicknameIsContainingOrIntroduceIsContaining(int userType,String status,String word,String word2);

    @Query(value="select u.userIdx as userIdx," +
            "       ifnull(u.profileImageURL, \"프로필 사진 없음\") as profileImageURL," +
            "       ifnull(u.nickname, \"닉네임 없음\")           as nickname," +
            "       if(u.introduce=\"\",\"소개가 아직 없습니다.\",u.introduce)          as introduce," +
            "       ifnull((select round(avg(r.star), 1)" +
            "               from Review r" +
            "               where r.evaluatedUserIdx = u.userIdx" +
            "                 and r.status = \"ACTIVE\"" +
            "               group by u.userIdx), 0)        as reviewAverage," +
            "       ifnull((select ifnull(count(*), 0)" +
            "               from Review r" +
            "               where r.evaluatedUserIdx = u.userIdx" +
            "                 and r.status = \"ACTIVE\"" +
            "               group by u.userIdx), 0)        as reviewCount" +
            "from UserInfo as u" +
            "         left join UserJobCategory UJC on u.userIdx = UJC.userIdx" +
            "         left join JobCategoryParent JCP on UJC.jobCategoryParentIdx = JCP.jobCategoryParentIdx" +
            "         left join JobCategoryChild JCC on JCP.jobCategoryParentIdx = JCC.jobCategoryParentIdx" +
            "         left join UserGenreCategory UGC on u.userIdx = UGC.userIdx" +
            "         left join GenreCategory GC on UGC.genreCategoryIdx = GC.genreCategoryIdx" +
            "where u.status = \"ACTIVE\"" +
            "  and (u.userType = 4" +
            "    or u.userType = 5" +
            "    or u.userType = 6)" +
            "  and JCP.jobCategoryParentIdx = ifnull(:jobCategoryParentIdx, JCP.jobCategoryParentIdx)" +
            "  and JCC.jobCategoryChildIdx = ifnull(:jobCategoryChildIdx, JCC.jobCategoryChildIdx)" +
            "  and GC.genreCategoryIdx = ifnull(:genreCategoryIdx, GC.genreCategoryIdx)" +
            "  and u.city = ifnull(:city, u.city)" +
            "     and (u.nickname like concat('%', ifnull(:nickname, u.nickname), '%')" +
            "    and u.introduce like concat('%', ifnull(:introduce, u.introduce), '%'))" +
            "group by u.userIdx" +
            "order by reviewAverage desc",nativeQuery = true)
    List<Object[]> findExpert(@Param("jobCategoryParentIdx")Integer jobCategoryParentIdx,
                                   @Param("jobCategoryChildIdx") Integer jobCategoryChildIdx,
                                   @Param("genreCategoryIdx")Integer genreCategoryIdx,
                                   @Param("city")String city,
                                   @Param("nickname")String nickname,
                                   @Param("introduce") String introduce);







}