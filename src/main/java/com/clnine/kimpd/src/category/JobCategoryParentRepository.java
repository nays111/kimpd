package com.clnine.kimpd.src.category;

import com.clnine.kimpd.src.category.models.JobCategoryParent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobCategoryParentRepository extends CrudRepository<JobCategoryParent,Integer> {
    JobCategoryParent findAllByJobCategoryParentIdx(int jobCategoryParentIdx);
    List<JobCategoryParent> findAll();
}

