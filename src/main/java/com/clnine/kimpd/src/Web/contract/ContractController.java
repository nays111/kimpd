package com.clnine.kimpd.src.Web.contract;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.contract.models.GetContractRes;
import com.clnine.kimpd.src.Web.contract.models.HtmlToPdfReq;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ContractController {
    private final JwtService jwtService;
    private final ContractProvider contractProvider;


    @ResponseBody
    @PostMapping("/to-pdf/{contractIdx}")
    public BaseResponse<GetContractRes> getContract(@PathVariable(required = true,value="contractIdx") int contractIdx,
                                                    @RequestBody HtmlToPdfReq htmlToPdfReq) throws IOException {
//        int userIdx;
//        try{
//            userIdx = jwtService.getUserIdx();
//        }catch(BaseException exception){
//            return new BaseResponse<>(exception.getStatus());
//        }

           contractProvider.makepdf(contractIdx,htmlToPdfReq.getDest());
            return new BaseResponse<>(SUCCESS);

    }
}
