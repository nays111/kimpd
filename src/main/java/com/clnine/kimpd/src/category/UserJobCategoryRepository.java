package com.clnine.kimpd.src.category;

import com.clnine.kimpd.src.category.models.UserJobCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJobCategoryRepository extends CrudRepository<UserJobCategory,Integer> {
}
