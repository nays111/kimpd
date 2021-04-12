package com.clnine.kimpd.src.Web.basket;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.basket.models.GetBasketListRes;
import com.clnine.kimpd.src.Web.basket.models.PostBasketCastingReq;
import com.clnine.kimpd.src.Web.basket.models.PostBasketReq;
import com.clnine.kimpd.src.Web.casting.models.CastingCountRes;
import com.clnine.kimpd.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class BasketController {
    private final JwtService jwtService;
    private final BasketService basketService;
    private final BasketProvider basketProvider;

    @ResponseBody
    @PostMapping("/baskets")
    @Operation(summary = "장바구니 담기 API",description = "여러명의 유저를 한 프로젝트 안에 담는 방식입니다, 토큰이 필요합니다.")
    public BaseResponse<String> postBasket(@RequestBody PostBasketReq postBasketReq){
        if(postBasketReq.getUserIdx().size()==0 || postBasketReq.getUserIdx()==null){
            return new BaseResponse<>(EMPTY_EXPERT_TO_POST_BASKET);
        }
        if(postBasketReq.getProjectIdx()==null){
            return new BaseResponse<>(EMPTY_PROJECT_INDEX);
        }
        try{
            int userIdx = jwtService.getUserIdx();
            List<Integer> expertIdxList = postBasketReq.getUserIdx();
            for(int i=0;i<expertIdxList.size();i++){
                if(expertIdxList.get(i)==userIdx){
                    return new BaseResponse<>(CANNOT_PUT_BASKET_YOURSELF);
                }
            }
            basketService.postBasket(userIdx,postBasketReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @ResponseBody
    @GetMapping("/baskets")
    @Operation(summary = "내 장바구니 조회 API",description = "토큰이 필요합니다.")
    public BaseResponse<GetBasketListRes> getMyBasket(@RequestParam(required = false,value = "projectIdx")Integer projectIdx){
        try{
            int userIdx = jwtService.getUserIdx();
            GetBasketListRes getBasketListRes = basketProvider.getBasketList(userIdx,projectIdx);
            return new BaseResponse<>(SUCCESS,getBasketListRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/baskets/castings")
    @Operation(summary = "장바구니에서 섭외요청하기 API",description = "섭외 조건이 다 입력되어야 섭외 요청을 할 수 있습니다.")
    public BaseResponse<String> postBasketCasting(@RequestBody PostBasketCastingReq postBasketCastingReq){
        if(postBasketCastingReq.getCastingIdx().size()==0 || postBasketCastingReq.getCastingIdx()==null){
            return new BaseResponse<>(EMPTY_CASTING_INDEX);
        }
        try{
            int userIdx = jwtService.getUserIdx();
            basketService.postBasketCasting(postBasketCastingReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
