package com.clnine.kimpd.src.Web.basket;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.basket.models.GetBasketListRes;
import com.clnine.kimpd.src.Web.basket.models.PostBasketReq;
import com.clnine.kimpd.src.Web.casting.models.CastingCountRes;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class BasketController {
    private final JwtService jwtService;
    private final BasketService basketService;
    private final BasketProvider basketProvider;

    /**
     * 장바구니 담기 API
     * @param postBasketReq
     * @return
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
        //todo requestbody validation 추가해야함
        try{
            basketService.postBasket(userIdx,postBasketReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 내 장바구니 조회 API
     * @param projectIdx
     * @return
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


}
