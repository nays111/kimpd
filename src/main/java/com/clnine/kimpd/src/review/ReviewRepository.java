package com.clnine.kimpd.src.review;

import com.clnine.kimpd.src.review.models.review;
import com.clnine.kimpd.src.user.models.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<review,Integer> {

    int countAllByEvaluatedUserInfoAndStatus(UserInfo userInfo,String status);
}
