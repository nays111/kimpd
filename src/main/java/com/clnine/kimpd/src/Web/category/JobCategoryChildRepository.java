package com.clnine.kimpd.src.Web.category;

import com.clnine.kimpd.src.Web.category.models.JobCategoryChild;
import com.clnine.kimpd.src.Web.category.models.JobCategoryParent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobCategoryChildRepository extends CrudRepository<JobCategoryChild,Integer> {
    //JobCategoryChild findAllByJobCategoryChildIdxAndAndJobCategoryParent(int jobCategoryChildIdx,JobCategoryParent jobCategoryParent);
    JobCategoryChild findAllByJobCategoryChildIdx(int jobCategoryChildIdx);
    List<JobCategoryChild> findAllByJobCategoryParent(JobCategoryParent jobCategoryParent);
}
