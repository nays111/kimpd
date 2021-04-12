package com.clnine.kimpd.src.Web.review;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.casting.CastingProvider;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.review.models.PostReviewReq;
import com.clnine.kimpd.src.Web.review.models.Review;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserInfoProvider userInfoProvider;
    private final CastingProvider castingProvider;
    private final CastingRepository castingRepository;

    @Transactional
    public void postReview(int userIdx,int castingIdx, PostReviewReq postReviewReq) throws BaseException{
        UserInfo evaluateUserInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        UserInfo evaluatedUserInfo = userInfoProvider.retrieveUserInfoByUserIdx(postReviewReq.getEvaluatedUserIdx());
        Casting casting = castingProvider.retrieveCastingByCastingIdx(castingIdx);

        //이미 리뷰를 담긴 상태면 막아야함
        Review existReview = reviewRepository.findByEvaluatedUserInfoAndEvaluateUserInfoAndCastingAndStatus(evaluatedUserInfo,evaluateUserInfo,casting,"ACTIVE");
        if(existReview!=null){//만약 리뷰가 존재하면
            throw new BaseException(ALREADY_POST_REVIEW);
        }
        float star = postReviewReq.getStar();
        String description = postReviewReq.getDescription();

        Review review = new Review(star,description,evaluateUserInfo,evaluatedUserInfo,casting);
        try{
            reviewRepository.save(review);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_POST_REVIEW);
        }
    }
}
