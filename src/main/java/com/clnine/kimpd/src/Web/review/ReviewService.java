package com.clnine.kimpd.src.Web.review;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.review.models.PostReviewReq;
import com.clnine.kimpd.src.Web.review.models.Review;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserInfoRepository userInfoRepository;
    private final CastingRepository castingRepository;
    private final ReviewRepository reviewRepository;
    private final UserInfoProvider userInfoProvider;

    /**
     * 전문가 평가 API
     * @param userIdx
     * @param castingIdx
     * @param postReviewReq
     * @throws BaseException
     */
    public void postReview(int userIdx,int castingIdx, PostReviewReq postReviewReq) throws BaseException{
        UserInfo userInfo;
                //= userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        try {
            userInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx,"ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        System.out.println(userInfo.getUserIdx());
        Casting casting;
        try{
            casting = castingRepository.findAllByCastingIdxAndStatus(castingIdx,"ACTIVE");
        }catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_CASTING);
        }
        UserInfo expertInfo = casting.getExpert();
        float star = postReviewReq.getStar();
        String description = postReviewReq.getDescription();
        Review review = new Review(star,description,userInfo,expertInfo);

        try{
            reviewRepository.save(review);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_POST_REVIEW);
        }
    }
}
