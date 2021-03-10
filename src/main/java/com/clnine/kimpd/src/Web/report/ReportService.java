package com.clnine.kimpd.src.Web.report;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.report.models.PostReportReq;
import com.clnine.kimpd.src.Web.report.models.Report;
import com.clnine.kimpd.src.Web.report.models.ReportCategory;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
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
    private final UserInfoProvider userInfoProvider;

    @Transactional
    public void postReport(int reportUserIdx, int reportedUserIdx, PostReportReq postReportReq) throws BaseException {
        UserInfo reportUserInfo = userInfoProvider.retrieveUserInfoByUserIdx(reportUserIdx);
        UserInfo reportedUserInfo = userInfoProvider.retrieveUserInfoByUserIdx(reportedUserIdx);

        ReportCategory reportCategory = reportCategoryRepository.findByReportCategoryIdx(postReportReq.getReportCategoryIdx());;
        if(reportCategory==null){
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

    public boolean deleteUserByReport(int reportedUserIdx) throws BaseException{
        UserInfo reportedUserInfo = userInfoProvider.retrieveUserInfoByUserIdx(reportedUserIdx);
        int reportCount = reportRepository.countReportByReportedUserInfoAndStatus(reportedUserInfo,"ACTIVE");
        if(reportCount >=5){
            reportedUserInfo.setStatus("INACTIVE");
            userInfoRepository.save(reportedUserInfo);
            return true;
        }else{
            return false;
        }
    }
}
