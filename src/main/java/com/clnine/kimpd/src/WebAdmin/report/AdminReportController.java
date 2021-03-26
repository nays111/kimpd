package com.clnine.kimpd.src.WebAdmin.report;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.WebAdmin.report.models.*;
import com.clnine.kimpd.src.WebAdmin.user.AdminUserInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/web-admin")
@RequiredArgsConstructor
public class AdminReportController {
    private final AdminReportProvider adminReportProvider;
    private final AdminUserInfoProvider adminUserInfoProvider;

    /**
     * 신고 전체 조회 API
     * [GET] /reports
     * @return BaseResponse<AdminGetReportsListRes>
     */
    @ResponseBody
    @GetMapping("/reports")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetReportsListRes> getReports() throws BaseException{
        List<AdminGetReportsRes> getReportsResList;

        try{
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            getReportsResList = adminReportProvider.getReportList();
            AdminGetReportsListRes reportList = new AdminGetReportsListRes(getReportsResList);
            return new BaseResponse<>(SUCCESS, reportList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 신고 상세 조회 API
     * [GET] /reports/:reportIdx
     * @PathVariable reportIdx
     * @return BaseResponse<AdminGetReportRes>
     */
    @ResponseBody
    @GetMapping("/reports/{reportIdx}")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetReportRes> getReport(@PathVariable Integer reportIdx) {
        if (reportIdx == null || reportIdx <= 0) {
            return new BaseResponse<>(EMPTY_REVIEW_IDX);
        }

        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            AdminGetReportRes adminGetReportRes = adminReportProvider.retrieveReportInfo(reportIdx);
            return new BaseResponse<>(SUCCESS, adminGetReportRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
