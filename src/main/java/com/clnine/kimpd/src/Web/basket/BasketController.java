package com.clnine.kimpd.src.Web.basket;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.basket.models.GetBasketListRes;
import com.clnine.kimpd.src.Web.basket.models.PostBasketCastingReq;
import com.clnine.kimpd.src.Web.basket.models.PostBasketReq;
import com.clnine.kimpd.src.Web.casting.models.CastingCountRes;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class BasketController {
    private final JwtService jwtService;
    private final BasketService basketService;
    private final BasketProvider basketProvider;

    /**
     * 장바구니 담기 API
     */
    @ResponseBody
    @PostMapping("/baskets")
    public BaseResponse<String> postBasket(@RequestBody PostBasketReq postBasketReq){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(postBasketReq.getUserIdx().size()==0 || postBasketReq.getUserIdx()==null){
            return new BaseResponse<>(EMPTY_EXPERT_TO_POST_BASKET);
        }
        if(postBasketReq.getProjectIdx()==null){
            return new BaseResponse<>(EMPTY_PROJECT_INDEX);
        }
        try{
            basketService.postBasket(userIdx,postBasketReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 내 장바구니 조회 API
     */
    @ResponseBody
    @GetMapping("/baskets")
    public BaseResponse<GetBasketListRes> getMyBasket(@RequestParam(required = false,value = "projectIdx")Integer projectIdx){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            GetBasketListRes getBasketListRes = basketProvider.getBasketList(userIdx,projectIdx);
            return new BaseResponse<>(SUCCESS,getBasketListRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 장바구니에 담긴걸 섭외 요청 보내기
     */
    @ResponseBody
    @PostMapping("/baskets/castings")
    public BaseResponse<String> postBasketCasting(@RequestBody PostBasketCastingReq postBasketCastingReq){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(postBasketCastingReq.getCastingIdx().size()==0 || postBasketCastingReq.getCastingIdx()==null){
            return new BaseResponse<>(EMPTY_CASTING_INDEX);
        }
        try{
            basketService.postBasketCasting(postBasketCastingReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
