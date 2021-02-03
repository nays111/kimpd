package com.clnine.kimpd.src.category;

import com.clnine.kimpd.src.category.models.UserGenreCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGenreCategoryRepository extends CrudRepository<UserGenreCategory,Integer> {

}
