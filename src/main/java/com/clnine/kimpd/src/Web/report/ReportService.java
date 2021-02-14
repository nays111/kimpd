package com.clnine.kimpd.src.Web.report;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.report.models.PostReportReq;
import com.clnine.kimpd.src.Web.report.models.Report;
import com.clnine.kimpd.src.Web.report.models.ReportCategory;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final UserInfoRepository userInfoRepository;
    private final ReportCategoryRepository reportCategoryRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public void postReport(int reportUserIdx, int reportedUserIdx, PostReportReq postReportReq) throws BaseException {
        UserInfo reportUserInfo;
        try {
            reportUserInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(reportUserIdx,"ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        UserInfo reportedUserInfo;
        try {
            reportedUserInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(reportedUserIdx,"ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        int reportCount;
        try{
            reportCount = reportRepository.countReportByReportedUserInfoAndStatus(reportedUserInfo,"ACTIVE");
        }catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_COUNT_REPORT);
        }
        //신고 5회 이상이면
        if(reportCount >=5){
            reportedUserInfo.setStatus("INACTIVE");
            userInfoRepository.save(reportedUserInfo);
        }


        ReportCategory reportCategory;
        try{
            reportCategory = reportCategoryRepository.findByReportCategoryIdx(postReportReq.getReportCategoryIdx());
        }catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_REPORT_CATEGORIES);
        }
        String description = postReportReq.getReportDescription();
        Report report = new Report(reportUserInfo,reportedUserInfo,reportCategory,description);

        try{
            reportRepository.save(report);
        }catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_POST_REPORT);
        }
    }

}
