package com.clnine.kimpd.src.Web.user;

import com.clnine.kimpd.src.Web.expert.models.GetUsersRes;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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


    /**
     * 검색 결과에 대한 정보 섭외순 정렬
     * @param jobCategoryParentIdx
     * @param jobCategoryChildIdx
     * @param genreCategoryIdx
     * @param city
     * @param nickname
     * @param introduce
     * @param minimumCastingPrice
     * @param page
     * @return
     */
    @Query(value="select UI.userIdx,\n" +
            "       ifnull(UI.profileImageURL,\"프로필 사진이 없습니다\") as profileImageURL,\n" +
            "       UI.nickname,\n" +
            "       (select JobCategoryChildName\n" +
            "        from JobCategoryChild\n" +
            "        where UJC.jobCategoryChildIdx = JobCategoryChild.JobCategoryChildIdx\n" +
            "        limit 1) as jobCategoryChildName,\n" +
            "       UI.introduce,\n" +
            "       ifnull((select round(avg(r.star), 1)\n" +
            "               from Review r\n" +
            "               where r.evaluatedUserIdx = UI.userIdx\n" +
            "                 and r.status = \"ACTIVE\"\n" +
            "               group by UI.userIdx), 0) as reviewAverage,\n" +
            "       ifnull((select count(*)\n" +
            "               from Review r\n" +
            "               where r.evaluatedUserIdx = UI.userIdx\n" +
            "                 and r.status = \"ACTIVE\"\n" +
            "               group by UI.userIdx), 0) as reviewCount,\n" +
            "       ifnull((select count(*)\n" +
            "               from Casting C\n" +
            "               where C.expertIdx = UI.userIdx and C.status = \"ACTIVE\"\n" +
            "               group by UI.userIdx), 0) as castingCount\n" +
            "from UserInfo as UI\n" +
            "         left join UserJobCategory UJC on UI.userIdx = UJC.userIdx\n" +
            "         left join JobCategoryParent JCP on UJC.jobCategoryParentIdx = JCP.jobCategoryParentIdx\n" +
            "         left join JobCategoryChild JCC on JCP.jobCategoryParentIdx = JCC.jobCategoryParentIdx\n" +
            "         left join UserGenreCategory UGC on UI.userIdx = UGC.userIdx\n" +
            "         left join GenreCategory GC on UGC.genreCategoryIdx = GC.genreCategoryIdx\n" +
            "where UI.status = \"ACTIVE\"\n" +
            "  and (UI.userType = 4 or UI.userType = 5 or UI.userType = 6)\n" +
            "  and UJC.jobCategoryParentIdx = ifnull(?, UJC.jobCategoryParentIdx)\n" +
            "  and UJC.jobCategoryChildIdx = ifnull(?, UJC.jobCategoryChildIdx)\n" +
            "  and UGC.genreCategoryIdx = ifnull(?, UGC.genreCategoryIdx)\n" +
            "  and UI.city = ifnull(?, UI.city)\n" +
            "  and (UI.nickname like concat('%', ifnull(?, UI.nickname), '%') or\n" +
            "       UI.introduce like concat('%', ifnull(?, UI.introduce), '%'))\n" +
            "and UI.minimumCastingPrice <= ifnull(?, UI.minimumCastingPrice)\n" +
            "group by UI.userIdx\n" +
            "order by castingCount desc\n" +
            "limit ?,5;",nativeQuery = true,name="findExpertOrderByCasting")
    List<Object[]> findExpertOrderByCasting(@Param("jobCategoryParentIdx")Integer jobCategoryParentIdx,
                                            @Param("jobCategoryChildIdx") Integer jobCategoryChildIdx,
                                            @Param("genreCategoryIdx")Integer genreCategoryIdx,
                                            @Param("city")String city,
                                            @Param("nickname")String nickname,
                                            @Param("introduce") String introduce,
                                            @Param("minimumCastingPrice")String minimumCastingPrice,
                                            @Param("page")int page);


    /**
     * 검색 결과 개수 얻기위한 쿼리
     * @param jobCategoryParentIdx
     * @param jobCategoryChildIdx
     * @param genreCategoryIdx
     * @param city
     * @param nickname
     * @param introduce
     * @param minimumCastingPrice
     * @return
     */
    @Query(value="select UI.userIdx,\n" +
            "       ifnull(UI.profileImageURL,\"프로필 사진이 없습니다\") as profileImageURL,\n" +
            "       UI.nickname,\n" +
            "       ifnull(UI.minimumCastingPrice,0) as minimumCastingPrice,\n" +
            "       (select JobCategoryChildName\n" +
            "        from JobCategoryChild\n" +
            "        where UJC.jobCategoryChildIdx = JobCategoryChild.JobCategoryChildIdx\n" +
            "        limit 1) as jobCategoryChildName,\n" +
            "       UI.introduce,\n" +
            "       ifnull((select round(avg(r.star), 1)\n" +
            "               from Review r\n" +
            "               where r.evaluatedUserIdx = UI.userIdx\n" +
            "                 and r.status = \"ACTIVE\"\n" +
            "               group by UI.userIdx), 0) as reviewAverage,\n" +
            "       ifnull((select count(*)\n" +
            "               from Review r\n" +
            "               where r.evaluatedUserIdx = UI.userIdx\n" +
            "                 and r.status = \"ACTIVE\"\n" +
            "               group by UI.userIdx), 0) as reviewCount,\n" +
            "       ifnull((select count(*)\n" +
            "               from Casting C\n" +
            "               where C.expertIdx = UI.userIdx and C.status = \"ACTIVE\"\n" +
            "               group by UI.userIdx), 0) as castingCount\n" +
            "from UserInfo as UI\n" +
            "         left join UserJobCategory UJC on UI.userIdx = UJC.userIdx\n" +
            "         left join JobCategoryParent JCP on UJC.jobCategoryParentIdx = JCP.jobCategoryParentIdx\n" +
            "         left join JobCategoryChild JCC on JCP.jobCategoryParentIdx = JCC.jobCategoryParentIdx\n" +
            "         left join UserGenreCategory UGC on UI.userIdx = UGC.userIdx\n" +
            "         left join GenreCategory GC on UGC.genreCategoryIdx = GC.genreCategoryIdx\n" +
            "where UI.status = \"ACTIVE\"\n" +
            "  and (UI.userType = 4 or UI.userType = 5 or UI.userType = 6)\n" +
            "  and UJC.jobCategoryParentIdx = ifnull(?, UJC.jobCategoryParentIdx)\n" +
            "  and UJC.jobCategoryChildIdx = ifnull(?, UJC.jobCategoryChildIdx)\n" +
            "  and UGC.genreCategoryIdx = ifnull(?, UGC.genreCategoryIdx)\n" +
            "  and UI.city = ifnull(?, UI.city)\n" +
            "  and (UI.nickname like concat('%', ifnull(?, UI.nickname), '%') or\n" +
            "       UI.introduce like concat('%', ifnull(?, UI.introduce), '%'))\n" +
            "and UI.minimumCastingPrice <= ifnull(?, UI.minimumCastingPrice)\n" +
            "group by UI.userIdx\n" +
            "order by castingCount desc;",nativeQuery = true,name="findExpertOrderByCastingCount")
    List<Object[]> findExpertCount(@Param("jobCategoryParentIdx")Integer jobCategoryParentIdx,
                                                   @Param("jobCategoryChildIdx") Integer jobCategoryChildIdx,
                                                   @Param("genreCategoryIdx")Integer genreCategoryIdx,
                                                   @Param("city")String city,
                                                   @Param("nickname")String nickname,
                                                   @Param("introduce") String introduce,
                                                   @Param("minimumCastingPrice")String minimumCastingPrice);


    /**
     * 검색결과에 대한 정보 - 평점순 정렬
     * @param jobCategoryParentIdx
     * @param jobCategoryChildIdx
     * @param genreCategoryIdx
     * @param city
     * @param nickname
     * @param introduce
     * @param minimumCastingPrice
     * @param page
     * @return
     */

    @Query(value="select UI.userIdx,\n" +
            "       ifnull(UI.profileImageURL,\"프로필 사진이 없습니다\") as profileImageURL,\n" +
            "       UI.nickname,\n" +
            "       (select JobCategoryChildName\n" +
            "        from JobCategoryChild\n" +
            "        where UJC.jobCategoryChildIdx = JobCategoryChild.JobCategoryChildIdx\n" +
            "        limit 1) as jobCategoryChildName,\n" +
            "       UI.introduce,\n" +
            "       ifnull((select round(avg(r.star), 1)\n" +
            "               from Review r\n" +
            "               where r.evaluatedUserIdx = UI.userIdx\n" +
            "                 and r.status = \"ACTIVE\"\n" +
            "               group by UI.userIdx), 0) as reviewAverage,\n" +
            "       ifnull((select count(*)\n" +
            "               from Review r\n" +
            "               where r.evaluatedUserIdx = UI.userIdx\n" +
            "                 and r.status = \"ACTIVE\"\n" +
            "               group by UI.userIdx), 0) as reviewCount,\n" +
            "       ifnull((select count(*)\n" +
            "               from Casting C\n" +
            "               where C.expertIdx = UI.userIdx and C.status = \"ACTIVE\"\n" +
            "               group by UI.userIdx), 0) as castingCount\n" +
            "from UserInfo as UI\n" +
            "         left join UserJobCategory UJC on UI.userIdx = UJC.userIdx\n" +
            "         left join JobCategoryParent JCP on UJC.jobCategoryParentIdx = JCP.jobCategoryParentIdx\n" +
            "         left join JobCategoryChild JCC on JCP.jobCategoryParentIdx = JCC.jobCategoryParentIdx\n" +
            "         left join UserGenreCategory UGC on UI.userIdx = UGC.userIdx\n" +
            "         left join GenreCategory GC on UGC.genreCategoryIdx = GC.genreCategoryIdx\n" +
            "where UI.status = \"ACTIVE\"\n" +
            "  and (UI.userType = 4 or UI.userType = 5 or UI.userType = 6)\n" +
            "  and UJC.jobCategoryParentIdx = ifnull(?, UJC.jobCategoryParentIdx)\n" +
            "  and UJC.jobCategoryChildIdx = ifnull(?, UJC.jobCategoryChildIdx)\n" +
            "  and UGC.genreCategoryIdx = ifnull(?, UGC.genreCategoryIdx)\n" +
            "  and UI.city = ifnull(?, UI.city)\n" +
            "  and (UI.nickname like concat('%', ifnull(?, UI.nickname), '%') or\n" +
            "       UI.introduce like concat('%', ifnull(?, UI.introduce), '%'))\n" +
            "and UI.minimumCastingPrice <= ifnull(?, UI.minimumCastingPrice)\n" +
            "group by UI.userIdx\n" +
            "order by reviewAverage desc\n" +
            "limit ?,5;",nativeQuery = true,name="findExpertOrderByCasting")
    List<Object[]> findExpertOrderByReview(@Param("jobCategoryParentIdx")Integer jobCategoryParentIdx,
                                            @Param("jobCategoryChildIdx") Integer jobCategoryChildIdx,
                                            @Param("genreCategoryIdx")Integer genreCategoryIdx,
                                            @Param("city")String city,
                                            @Param("nickname")String nickname,
                                            @Param("introduce") String introduce,
                                            @Param("minimumCastingPrice")String minimumCastingPrice,
                                            @Param("page")int page);



}