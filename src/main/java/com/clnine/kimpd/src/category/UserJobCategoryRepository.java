package com.clnine.kimpd.src.category;

import com.clnine.kimpd.src.category.models.JobCategoryChild;
import com.clnine.kimpd.src.category.models.JobCategoryParent;
import com.clnine.kimpd.src.category.models.UserJobCategory;
import com.clnine.kimpd.src.user.models.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJobCategoryRepository extends CrudRepository<UserJobCategory,Integer> {
    List<UserInfo> findByJobCategoryChild(JobCategoryChild jobCategoryChild);
    List<UserInfo> findByJobCategoryParent(JobCategoryParent jobCategoryParent);
    List<UserJobCategory> findByUserInfo(UserInfo userInfo);
    List<UserJobCategory> findByUserJobCategoryIdx(int userJobCategoryIdx);
}
