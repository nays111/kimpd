package com.clnine.kimpd.src.WebAdmin.contract;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.WebAdmin.contract.models.*;
import com.clnine.kimpd.src.WebAdmin.user.AdminUserInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/web-admin")
@RequiredArgsConstructor
public class AdminContractController {
    private final AdminContractProvider adminContractProvider;
    private final AdminContractService adminContractService;
    private final AdminUserInfoProvider adminUserInfoProvider;

    /**
     * 계약서 상세 조회 API
     * [GET] /contracts
     * @return BaseResponse<AdminGetNoticeRes>
     */
    @ResponseBody
    @GetMapping("/contracts")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetContractRes> getContract() {

        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            AdminGetContractRes adminGetContractRes = adminContractProvider.retrieveContractInfo();
            return new BaseResponse<>(SUCCESS_READ_CONTRACTS, adminGetContractRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 계약서 수정 API
     * [PATCH] /contracts
     * @RequestBody AdminPatchContractReq
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @PatchMapping("/contracts")
    @CrossOrigin(origins = "*")
    public BaseResponse<Void> patchContract(@RequestBody AdminPatchContractReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getContractContent() == null || parameters.getContractContent().length() <= 0) {
            return new BaseResponse<>(EMPTY_CONTRACT_CONTENT);
        }

        // 2. Patch Contract
        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            adminContractService.updateContract(parameters);
            return new BaseResponse<>(SUCCESS_PATCH_CONTRACTS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
