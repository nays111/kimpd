package com.clnine.kimpd.src.Web.category;

import com.clnine.kimpd.src.Web.category.models.GenreCategory;
import com.clnine.kimpd.src.Web.category.models.UserGenreCategory;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGenreCategoryRepository extends CrudRepository<UserGenreCategory,Integer> {

    List<UserInfo> findByGenreCategory(GenreCategory genreCategory);

}
