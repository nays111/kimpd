package com.clnine.kimpd.src.Web.basket;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
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
}
