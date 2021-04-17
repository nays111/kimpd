package com.clnine.kimpd.src.Web.basket;

import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketRepository extends CrudRepository<Casting,Integer> {

    //장바구니 관련 JPA - castingStatus가 0이면 장바구니에 담긴 것이다.
    List<Casting> findAllByUserInfoAndCastingStatusAndStatus(UserInfo userInfo, int castingStatus,String status);
    List<Casting> findAllByUserInfoAndCastingStatusAndProjectAndStatus(UserInfo userInfo,int castingStatus,Project project,String status);

    Casting findAllByUserInfoAndExpertAndProjectAndStatusAndCastingStatus(UserInfo userInfo,UserInfo expert,Project project,String status,int castingStatus);
}
