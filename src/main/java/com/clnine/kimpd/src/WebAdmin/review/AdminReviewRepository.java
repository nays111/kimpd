package com.clnine.kimpd.src.WebAdmin.review;

import com.clnine.kimpd.src.Web.user.models.UserInfo;
import com.clnine.kimpd.src.WebAdmin.review.models.AdminReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminReviewRepository extends CrudRepository<AdminReview,Integer> {

    int countAllByEvaluatedUserInfoAndStatus(UserInfo userInfo,String status);
    List<AdminReview> findAllByEvaluatedUserInfoAndStatus(UserInfo userInfo, String status);

}
