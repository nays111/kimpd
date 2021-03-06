package com.clnine.kimpd.src.Web.report;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.report.models.GetReportCategoryRes;
import com.clnine.kimpd.src.Web.report.models.PostReportReq;
import com.clnine.kimpd.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ReportController {
    private final ReportProvider reportProvider;
    private final JwtService jwtService;
    private final ReportService reportService;

    @ResponseBody
    @GetMapping("/report-categories")
    @Operation(summary = "신고유형 카테고리 조회 API")
    public BaseResponse<List<GetReportCategoryRes>> getReportCategories()throws BaseException {
        List<GetReportCategoryRes> getReportCategoryResList;
        try{
            int userIdx = jwtService.getUserIdx();
            getReportCategoryResList = reportProvider.getReportCategory();
            return new BaseResponse<>(SUCCESS,getReportCategoryResList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/castings/{userIdx}/reports")
    @Operation(summary = "신고하기 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> postReport(@PathVariable(required = true,value="userIdx")int reportedUserIdx,
                                           @RequestBody PostReportReq postReportReq){
        if(postReportReq.getReportDescription()==null || postReportReq.getReportDescription().length()==0){
            return new BaseResponse<>(EMPTY_REPORT_DESCRIPTION);
        }
        if(postReportReq.getReportCategoryIdx()==null){
            return new BaseResponse<>(EMPTY_REPORT_CATEGORY);
        }
        try{
            int reporterUserIdx = jwtService.getUserIdx();
            reportService.postReport(reporterUserIdx,reportedUserIdx,postReportReq);
            reportService.deleteUserByReport(reportedUserIdx);
            return new BaseResponse<>(SUCCESS);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
