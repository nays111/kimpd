package com.clnine.kimpd.src.Web.review;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.review.models.PostReviewReq;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtService jwtService;

    /**
     * 평가하기 API
     * @param castingIdx
     * @param postReviewReq
     * @return
     */
    @ResponseBody
    @PostMapping("/castings/{castingIdx}/reviews")
    public BaseResponse<String> postReview(@PathVariable(required = true,value="castingIdx")int castingIdx,
                                            @RequestBody PostReviewReq postReviewReq){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(postReviewReq.getDescription()==null || postReviewReq.getDescription().length()==0){
            return new BaseResponse<>(EMPTY_REVIEW_DESCRIPTION);
        }
        if(postReviewReq.getStar()>5 || postReviewReq.getStar()<0){
            return new BaseResponse<>(WRONG_REVIEW_STAR);
        }
        try{
            reviewService.postReview(userIdx,castingIdx,postReviewReq);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
