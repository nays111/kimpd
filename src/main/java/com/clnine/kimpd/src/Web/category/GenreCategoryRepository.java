package com.clnine.kimpd.src.Web.category;

import com.clnine.kimpd.src.Web.category.models.GenreCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GenreCategoryRepository extends CrudRepository<GenreCategory,Integer> {
    GenreCategory findAllByGenreCategoryIdx(int genreCategoryIdx);
    List<GenreCategory> findAll();
    GenreCategory findTopByOrderByGenreCategoryIdxDesc();
}
