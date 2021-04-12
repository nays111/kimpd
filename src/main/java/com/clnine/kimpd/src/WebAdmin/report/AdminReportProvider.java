package com.clnine.kimpd.src.WebAdmin.report;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.casting.AdminCastingRepository;
import com.clnine.kimpd.src.WebAdmin.inquiry.models.AdminGetInquiriesRes;
import com.clnine.kimpd.src.WebAdmin.inquiry.models.AdminInquiry;
import com.clnine.kimpd.src.WebAdmin.report.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
public class AdminReportProvider {

    @Autowired
    private final AdminReportRepository adminReportRepository;
    private final AdminCastingRepository adminCastingRepository;

    AdminReportProvider(AdminReportRepository adminReportRepository, AdminCastingRepository adminCastingRepository){
        this.adminReportRepository = adminReportRepository;
        this.adminCastingRepository = adminCastingRepository;
    }

    /**
     * 신고 전체 조회
     * @return List<AdminGetReportsRes>
     * @throws BaseException
     */
    public List<AdminGetReportsRes> getReportList() throws BaseException{
        List<AdminReport> reportList;
        try{
            reportList = adminReportRepository.findAll();
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_REPORTS);
        }

        SimpleDateFormat sDate = new SimpleDateFormat("yyyy.MM.dd");
        return reportList.stream().map(Report ->{
            int reportIdx = Report.getReportIdx();
            String reportUserNickname = Report.getReportUserInfo().getNickname();
            String reportedUserNickname = Report.getReportedUserInfo().getNickname();
            String reportCategoryName = Report.getReportCategory().getReportCategoryName();
            String createdAt = sDate.format(Report.getCreatedAt());
            String status = Report.getStatus();
            return new AdminGetReportsRes(reportIdx, reportUserNickname, reportedUserNickname, reportCategoryName, createdAt, status);
        }).collect(Collectors.toList());
    }

    /**
     * 신고 상세 조회
     * @param reportIdx
     * @return AdminGetReportRes
     * @throws BaseException
     */
    public AdminGetReportRes retrieveReportInfo(int reportIdx) throws BaseException {
        // 1. DB에서 reportIdx AdminReport 조회
        AdminReport adminReport = retrieveReportByReportIdx(reportIdx);
        if(adminReport == null){
            throw new BaseException(FAILED_TO_GET_REPORTS);
        }
        // 2. AdminGetReviewRes 변환하여 return
        SimpleDateFormat sDate = new SimpleDateFormat("yyyy.MM.dd");

        String reportUserName = adminReport.getReportUserInfo().getName();
        String reportUserNickname = adminReport.getReportUserInfo().getNickname();
        String reportUserPhoneNumber = adminReport.getReportUserInfo().getPhoneNum();
        String reportUserEmail = adminReport.getReportUserInfo().getEmail();

        String reportedUserName = adminReport.getReportedUserInfo().getName();
        String reportedUserNickname = adminReport.getReportedUserInfo().getNickname();
        String reportedUserPhoneNumber = adminReport.getReportedUserInfo().getPhoneNum();
        String reportedUserEmail = adminReport.getReportedUserInfo().getEmail();

        String reportCategoryName = adminReport.getReportCategory().getReportCategoryName();
        String reportDescription = adminReport.getReportDescription();
        String createdAt = sDate.format(adminReport.getCreatedAt());
        String status = adminReport.getStatus();

        return new AdminGetReportRes(reportUserName, reportUserNickname, reportUserPhoneNumber, reportUserEmail,
                reportedUserName, reportedUserNickname, reportedUserPhoneNumber, reportedUserEmail,
                reportCategoryName, reportDescription, createdAt, status);
    }

    /**
     * 신고 조회
     * @param reportIdx
     * @return AdminReview
     * @throws BaseException
     */
    public AdminReport retrieveReportByReportIdx(int reportIdx) throws BaseException {
        // 1. DB에서 AdminReport 조회
        AdminReport adminReport;
        try {
            adminReport = adminReportRepository.findById(reportIdx).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_REPORTS);
        }

        // 2. AdminReport return
        return adminReport;
    }
}
