package com.clnine.kimpd.src.Web.contract;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.casting.CastingProvider;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.contract.models.GetContractRes;
import com.clnine.kimpd.src.Web.contract.models.HtmlToPdfReq;
import com.clnine.kimpd.src.Web.message.models.GetMessagesRes;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ContractController {
    private final JwtService jwtService;
    private final ContractProvider contractProvider;


    private final CastingProvider castingProvider;


    @ResponseBody
    @PostMapping("/to-pdf/{castingIdx}")
    public BaseResponse<String> getContracts(@PathVariable(required = true,value="castingIdx") int castingIdx,
                                                    @RequestBody HtmlToPdfReq htmlToPdfReq) throws IOException,BaseException {

        Casting casting = castingProvider.retrieveCastingByCastingIdx(castingIdx);
        String s = contractProvider.makepdf(casting,12);
        return new BaseResponse<>(SUCCESS,s);

    }


//    @PostMapping("/profile/pic")
//    public BaseResponse<String> upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
//
//        String s = contractProvider.upload(multipartFile);
//        return new BaseResponse<>(SUCCESS,s);
//    }

    @GetMapping("/castings/{castingIdx}/contracts")
    public BaseResponse<GetContractRes> getContract(@PathVariable(required = true,value = "castingIdx")int castingIdx){
        try{
            Integer userIdx = jwtService.getUserIdx();
            GetContractRes getContractRes = contractProvider.getContract(userIdx,castingIdx);
            return new BaseResponse<>(SUCCESS, getContractRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
