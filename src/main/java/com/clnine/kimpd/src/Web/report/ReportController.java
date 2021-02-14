package com.clnine.kimpd.src.Web.report;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.report.models.GetReportCategoryRes;
import com.clnine.kimpd.src.Web.report.models.PostReportReq;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.EMPTY_REPORT_DESCRIPTION;
import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ReportController {
    private final ReportProvider reportProvider;
    private final JwtService jwtService;
    private final ReportService reportService;

    /**
     * 신고유형 카테고리 조회 API
     * @return
     * @throws BaseException
     */
    @ResponseBody
    @GetMapping("/report-categories")
    public BaseResponse<List<GetReportCategoryRes>> getReportCategories()throws BaseException {
        List<GetReportCategoryRes> getReportCategoryResList;
        try{
            getReportCategoryResList = reportProvider.getReportCategory();
            return new BaseResponse<>(SUCCESS,getReportCategoryResList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 신고하기 API
     * @param reportedUserIdx
     * @param postReportReq
     * @return
     */
    @ResponseBody
    @PostMapping("/castings/{userIdx}/reports")
    public BaseResponse<String> postReport(@PathVariable(required = true,value="userIdx")int reportedUserIdx,
                                           @RequestBody PostReportReq postReportReq){
        int reporterUserIdx;
        try{
            reporterUserIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

        if(postReportReq.getReportDescription()==null || postReportReq.getReportDescription().length()==0){
            return new BaseResponse<>(EMPTY_REPORT_DESCRIPTION);
        }

        try{
            reportService.postReport(reporterUserIdx,reportedUserIdx,postReportReq);



            return new BaseResponse<>(SUCCESS);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
