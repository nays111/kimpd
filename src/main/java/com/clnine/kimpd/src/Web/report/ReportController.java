package com.clnine.kimpd.src.Web.report;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.report.models.GetReportCategoryRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ReportController {
    private final ReportProvider reportProvider;

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

}
