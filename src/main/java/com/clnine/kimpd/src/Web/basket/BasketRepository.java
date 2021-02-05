package com.clnine.kimpd.src.Web.basket;

import com.clnine.kimpd.src.Web.basket.models.Basket;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketRepository extends CrudRepository<Basket,Integer> {
    List<Project> findByUserInfoAndStatus(UserInfo userInfo, String status);
}
