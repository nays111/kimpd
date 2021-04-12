package com.clnine.kimpd.src.Web.report;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.category.models.GenreCategory;
import com.clnine.kimpd.src.Web.category.models.GetGenreCategoryRes;
import com.clnine.kimpd.src.Web.report.models.GetReportCategoryRes;
import com.clnine.kimpd.src.Web.report.models.ReportCategory;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_GENRE_CATEGORIES;
import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_REPORT_CATEGORIES;

@Service
@RequiredArgsConstructor
public class ReportProvider {

    private final ReportCategoryRepository reportCategoryRepository;

    public List<GetReportCategoryRes> getReportCategory() throws BaseException {
        List<ReportCategory> reportCategoryList;
        try{
            reportCategoryList = reportCategoryRepository.findAll();
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_REPORT_CATEGORIES);
        }
        return reportCategoryList.stream().map(reportCategory -> {
            int reportCategoryIdx = reportCategory.getReportCategoryIdx();
            String reportCategoryName = reportCategory.getReportCategoryName();
            return new GetReportCategoryRes(reportCategoryIdx,reportCategoryName);
        }).collect(Collectors.toList());
    }
}
