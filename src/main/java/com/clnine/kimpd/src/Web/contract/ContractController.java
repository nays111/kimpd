package com.clnine.kimpd.src.Web.contract;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.contract.models.GetContractRes;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ContractController {
    private final JwtService jwtService;
    private final ContractProvider contractProvider;

    /**
     * 계약서 불러오기 API
     * @param castingIdx
     * @return
     */
    @ResponseBody
    @GetMapping("/castings/{castingIdx}/contracts")
    public BaseResponse<GetContractRes> getContract(@PathVariable(required = true,value="castingIdx") int castingIdx){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            GetContractRes getContractRes = contractProvider.getContractRes(castingIdx);
            return new BaseResponse<>(SUCCESS,getContractRes);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
