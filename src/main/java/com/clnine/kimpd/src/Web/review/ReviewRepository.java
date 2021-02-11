package com.clnine.kimpd.src.Web.review;

import com.clnine.kimpd.src.Web.review.models.Review;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review,Integer> {

    int countAllByEvaluatedUserInfoAndStatus(UserInfo userInfo,String status);
    List<Review> findAllByEvaluatedUserInfoAndStatus(UserInfo userInfo, String status);

}
