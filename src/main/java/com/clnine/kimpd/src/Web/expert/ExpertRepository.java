package com.clnine.kimpd.src.Web.expert;

import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpertRepository extends CrudRepository<UserInfo, Integer> {


    @Query(value="select UI.userIdx,\n" +
            "       UI.profileImageURL,\n" +
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
            "               group by UI.userIdx), 0) as castingCount,\n" +
            "       group_concat(ECD.castingPossibleDate separator '') as GC\n" +
            "from UserInfo as UI\n" +
            "         left join UserJobCategory UJC on UI.userIdx = UJC.userIdx\n" +
            "         left join JobCategoryParent JCP on UJC.jobCategoryParentIdx = JCP.jobCategoryParentIdx\n" +
            "         left join JobCategoryChild JCC on JCP.jobCategoryParentIdx = JCC.jobCategoryParentIdx\n" +
            "         left join UserGenreCategory UGC on UI.userIdx = UGC.userIdx\n" +
            "         left join GenreCategory GC on UGC.genreCategoryIdx = GC.genreCategoryIdx\n" +
            "         left join ExpertCastingDate ECD on UI.userIdx = ECD.userIdx\n" +
            "where UI.status = \"ACTIVE\"\n" +
            "  and (UI.userType = 4 or UI.userType = 5 or UI.userType = 6)\n" +
            "  and UI.agreeShowDB=1\n" +
            "  and UJC.jobCategoryParentIdx  IN (:jobCategoryParentIdx)\n" +
            "  and UJC.jobCategoryChildIdx  IN (:jobCategoryChildIdx)\n" +
            "  and UGC.genreCategoryIdx  IN (:genreCategoryIdx)\n" +
            "  and UI.city  IN (:city)\n" +
            "  and (UI.nickname like concat('%', ifnull(:nickname, UI.nickname), '%') or\n" +
            "       UI.introduce like concat('%', ifnull(:introduce, UI.introduce), '%'))\n" +
            "  and UI.minimumCastingPrice <= ifnull(:minimumCastingPrice, UI.minimumCastingPrice)\n" +
            "group by UI.userIdx\n" +
            "having GC like concat('%',:castingDate,'%')" +
            "order by castingCount desc\n" +
            "limit :page,5;",nativeQuery = true,name="findExpertOrderByCasting")
    List<Object[]> findExpertListOrderByCasting(@Param("jobCategoryParentIdx")List<Long> jobCategoryParentIdx,
                                                @Param("jobCategoryChildIdx") List<Long> jobCategoryChildIdx,
                                                @Param("genreCategoryIdx")List<Long> genreCategoryIdx,
                                                @Param("city")List<String> city,
                                                @Param("nickname")String nickname,
                                                @Param("introduce") String introduce,
                                                @Param("minimumCastingPrice")int minimumCastingPrice,
                                                @Param("castingDate")String castingDate,
                                                @Param("page")int page);

    @Query(value="select UI.userIdx,\n" +
            "       UI.profileImageURL,\n" +
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
            "               group by UI.userIdx), 0) as castingCount,\n" +
            "       group_concat(ECD.castingPossibleDate separator '') as GC\n" +
            "from UserInfo as UI\n" +
            "         left join UserJobCategory UJC on UI.userIdx = UJC.userIdx\n" +
            "         left join JobCategoryParent JCP on UJC.jobCategoryParentIdx = JCP.jobCategoryParentIdx\n" +
            "         left join JobCategoryChild JCC on JCP.jobCategoryParentIdx = JCC.jobCategoryParentIdx\n" +
            "         left join UserGenreCategory UGC on UI.userIdx = UGC.userIdx\n" +
            "         left join GenreCategory GC on UGC.genreCategoryIdx = GC.genreCategoryIdx\n" +
            "         left join ExpertCastingDate ECD on UI.userIdx = ECD.userIdx\n" +
            "where UI.status = \"ACTIVE\"\n" +
            "  and (UI.userType = 4 or UI.userType = 5 or UI.userType = 6)\n" +
            "  and UI.agreeShowDB=1\n" +
            "  and UJC.jobCategoryParentIdx  IN (:jobCategoryParentIdx)\n" +
            "  and UJC.jobCategoryChildIdx  IN (:jobCategoryChildIdx)\n" +
            "  and UGC.genreCategoryIdx  IN (:genreCategoryIdx)\n" +
            "  and UI.city  IN (:city)\n" +
            "  and (UI.nickname like concat('%', ifnull(:nickname, UI.nickname), '%') or\n" +
            "       UI.introduce like concat('%', ifnull(:introduce, UI.introduce), '%'))\n" +
            "  and UI.minimumCastingPrice <= ifnull(:minimumCastingPrice, UI.minimumCastingPrice)\n" +
            "group by UI.userIdx\n" +
            "having GC like concat('%',:castingDate,'%')" +
            "order by reviewAverage desc\n" +
            "limit :page,5;",nativeQuery = true,name="findExpertOrderByCasting")
    List<Object[]> findExpertListOrderByReview(@Param("jobCategoryParentIdx")List<Long> jobCategoryParentIdx,
                                                @Param("jobCategoryChildIdx") List<Long> jobCategoryChildIdx,
                                                @Param("genreCategoryIdx")List<Long> genreCategoryIdx,
                                                @Param("city")List<String> city,
                                                @Param("nickname")String nickname,
                                                @Param("introduce") String introduce,
                                               @Param("minimumCastingPrice")int minimumCastingPrice,
                                               @Param("castingDate")String castingDate,
                                                @Param("page")int page);





    @Query(value="select UI.userIdx,\n" +
            "       UI.profileImageURL,\n" +
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
            "               group by UI.userIdx), 0) as castingCount,\n" +
            "       group_concat(ECD.castingPossibleDate separator '') as GC\n" +
            "from UserInfo as UI\n" +
            "         left join UserJobCategory UJC on UI.userIdx = UJC.userIdx\n" +
            "         left join JobCategoryParent JCP on UJC.jobCategoryParentIdx = JCP.jobCategoryParentIdx\n" +
            "         left join JobCategoryChild JCC on JCP.jobCategoryParentIdx = JCC.jobCategoryParentIdx\n" +
            "         left join UserGenreCategory UGC on UI.userIdx = UGC.userIdx\n" +
            "         left join GenreCategory GC on UGC.genreCategoryIdx = GC.genreCategoryIdx\n" +
            "         left join ExpertCastingDate ECD on UI.userIdx = ECD.userIdx\n" +
            "where UI.status = \"ACTIVE\"\n" +
            "  and (UI.userType = 4 or UI.userType = 5 or UI.userType = 6)\n" +
            "  and UI.agreeShowDB=1\n" +
            "  and UJC.jobCategoryParentIdx  IN (:jobCategoryParentIdx)\n" +
            "  and UJC.jobCategoryChildIdx  IN (:jobCategoryChildIdx)\n" +
            "  and UGC.genreCategoryIdx  IN (:genreCategoryIdx)\n" +
            "  and UI.city  IN (:city)\n" +
            "  and (UI.nickname like concat('%', ifnull(:nickname, UI.nickname), '%') or\n" +
            "       UI.introduce like concat('%', ifnull(:introduce, UI.introduce), '%'))\n" +
            "  and UI.minimumCastingPrice <= ifnull(:minimumCastingPrice, UI.minimumCastingPrice)\n" +
            "group by UI.userIdx\n" +
            "having GC like concat('%',:castingDate,'%')" +
            "order by castingCount desc;",nativeQuery = true,name="findExpertOrderByCasting")
    List<Object[]> findExpertListCountOrderByCasting(@Param("jobCategoryParentIdx")List<Long> jobCategoryParentIdx,
                                                     @Param("jobCategoryChildIdx") List<Long> jobCategoryChildIdx,
                                                     @Param("genreCategoryIdx")List<Long> genreCategoryIdx,
                                                     @Param("city")List<String> city,
                                                     @Param("nickname")String nickname,
                                                     @Param("introduce") String introduce,
                                                     @Param("minimumCastingPrice")int minimumCastingPrice,
                                                     @Param("castingDate")String castingDate);


    @Query(value="select UI.userIdx,\n" +
            "       UI.profileImageURL,\n" +
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
            "               group by UI.userIdx), 0) as castingCount,\n" +
            "       group_concat(ECD.castingPossibleDate separator '') as GC\n" +
            "from UserInfo as UI\n" +
            "         left join UserJobCategory UJC on UI.userIdx = UJC.userIdx\n" +
            "         left join JobCategoryParent JCP on UJC.jobCategoryParentIdx = JCP.jobCategoryParentIdx\n" +
            "         left join JobCategoryChild JCC on JCP.jobCategoryParentIdx = JCC.jobCategoryParentIdx\n" +
            "         left join UserGenreCategory UGC on UI.userIdx = UGC.userIdx\n" +
            "         left join GenreCategory GC on UGC.genreCategoryIdx = GC.genreCategoryIdx\n" +
            "         left join ExpertCastingDate ECD on UI.userIdx = ECD.userIdx\n" +
            "where UI.status = \"ACTIVE\"\n" +
            "  and (UI.userType = 4 or UI.userType = 5 or UI.userType = 6)\n" +
            "  and UI.agreeShowDB=1\n" +
            "  and UJC.jobCategoryParentIdx  IN (:jobCategoryParentIdx)\n" +
            "  and UJC.jobCategoryChildIdx  IN (:jobCategoryChildIdx)\n" +
            "  and UGC.genreCategoryIdx  IN (:genreCategoryIdx)\n" +
            "  and UI.city  IN (:city)\n" +
            "  and (UI.nickname like concat('%', ifnull(:nickname, UI.nickname), '%') or\n" +
            "       UI.introduce like concat('%', ifnull(:introduce, UI.introduce), '%'))\n" +
            "  and UI.minimumCastingPrice <= ifnull(:minimumCastingPrice, UI.minimumCastingPrice)\n" +
            "  and ECD.castingPossibleDate between ifnull(:castingStartDate,ECD.castingPossibleDate) and ifnull(:castingEndDate,ECD.castingPossibleDate)\n"+
            "group by UI.userIdx\n" +
            "having GC like concat('%',:castingDate,'%')" +
            "order by reviewAverage desc;",nativeQuery = true,name="findExpertOrderByCasting")
    List<Object[]> findExpertListCountOrderByReview(@Param("jobCategoryParentIdx")List<Long> jobCategoryParentIdx,
                                                    @Param("jobCategoryChildIdx") List<Long> jobCategoryChildIdx,
                                                    @Param("genreCategoryIdx")List<Long> genreCategoryIdx,
                                                    @Param("city")List<String> city,
                                                    @Param("nickname")String nickname,
                                                    @Param("introduce") String introduce,
                                                    @Param("minimumCastingPrice")int minimumCastingPrice,
                                                    @Param("castingDate")String castingDate);




}
