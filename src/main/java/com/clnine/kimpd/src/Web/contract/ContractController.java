package com.clnine.kimpd.src.Web.contract;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.contract.models.GetContractRes;
import com.clnine.kimpd.src.Web.contract.models.HtmlToPdfReq;
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


    @ResponseBody
    @PostMapping("/to-pdf/{contractIdx}")
    public BaseResponse<String> getContract(@PathVariable(required = true,value="contractIdx") int contractIdx,
                                                    @RequestBody HtmlToPdfReq htmlToPdfReq) throws IOException {

           String s = contractProvider.makepdf(null,contractIdx);
            return new BaseResponse<>(SUCCESS,s);

    }


    @PostMapping("/profile/pic")
    public BaseResponse<String> upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        //logger.info("HIT -/upload | File Name : {}", multipartFile.getOriginalFilename());
        String s = contractProvider.upload(multipartFile);
        return new BaseResponse<>(SUCCESS,s);
    }
}
