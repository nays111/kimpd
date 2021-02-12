package com.clnine.kimpd.src.Web.review;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.review.models.GetMyReviewsRes;
import com.clnine.kimpd.src.Web.review.models.PostReviewReq;
import com.clnine.kimpd.utils.JwtService;
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

    /**
     * 평가리스트 조회 API
     */
    @ResponseBody
    @GetMapping("/castings/reviews")
    public BaseResponse<List<GetMyReviewsRes>>getReviews(@RequestParam(value="reviewStatus",required = false)Integer reviewStatus,
                                                         @RequestParam(value = "duration",required = false)Integer duration,
                                                         @RequestParam(value="page",required = true)int page){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        List<GetMyReviewsRes> getMyReviewsResList;
        try{
            getMyReviewsResList = reviewProvider.getMyReviewsRes(userIdx,duration,reviewStatus,page,6);
            return new BaseResponse<>(SUCCESS,getMyReviewsResList);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
