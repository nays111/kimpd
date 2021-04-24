package com.clnine.kimpd.src.Web.review;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.review.models.GetMyReviewsListDTO;
import com.clnine.kimpd.src.Web.review.models.GetMyReviewsRes;
import com.clnine.kimpd.src.Web.review.models.PostReviewReq;
import com.clnine.kimpd.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtService jwtService;
    private final ReviewProvider reviewProvider;

    @ResponseBody
    @PostMapping("/castings/{castingIdx}/reviews")
    @Operation(summary = "평가하기 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> postReview(@PathVariable(required = true,value="castingIdx")int castingIdx,
                                            @RequestBody PostReviewReq postReviewReq){
        if(postReviewReq.getDescription()==null || postReviewReq.getDescription().length()==0){
            return new BaseResponse<>(EMPTY_REVIEW_DESCRIPTION);
        }
        if(postReviewReq.getStar()>5 || postReviewReq.getStar()<0){
            return new BaseResponse<>(WRONG_REVIEW_STAR);
        }
        try{
            int evaluateUserIdx = jwtService.getUserIdx();
            reviewService.postReview(evaluateUserIdx,castingIdx,postReviewReq);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 내가 평가한 것들 대상으로 조회
     */
    @ResponseBody
    @GetMapping("/castings/reviews")
    @Operation(summary = "평가 내역 리스트 조회 API",description = "토큰이 필요합니다.")
    public BaseResponse<GetMyReviewsRes>getReviews(@RequestParam(value="reviewStatus",required = true)Integer reviewStatus,
                                                   @RequestParam(value = "duration",required = true)Integer duration,
                                                   @RequestParam(value="page",required = true)Integer page){
        if(reviewStatus==null){
            return new BaseResponse<>(EMPTY_REVIEW_STATUS);
        }
        if(reviewStatus!=1 && reviewStatus!=2 && reviewStatus!=0){
            return new BaseResponse<>(WRONG_REVIEW_STATUS);
        }
        if(duration==null){
            return new BaseResponse<>(EMPTY_DURATION);
        }
        if(duration!=1 && duration!=2 && duration!=0){
            return new BaseResponse<>(WRONG_DURATION);
        }
        if(page==null){
            return new BaseResponse<>(EMPTY_PAGE);
        }
        try{
            int userIdx = jwtService.getUserIdx();
            GetMyReviewsRes getMyReviewsRes = reviewProvider.getMyReviewsRes(userIdx,duration,reviewStatus,page,6);
            return new BaseResponse<>(SUCCESS, getMyReviewsRes);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
