package com.clnine.kimpd.src.category;

import com.clnine.kimpd.src.category.models.GenreCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GenreCategoryRepository extends CrudRepository<GenreCategory,Integer> {
    GenreCategory findAllByGenreCategoryIdx(int genreCategoryIdx);
    List<GenreCategory> findAll();
}
