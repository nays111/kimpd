package com.clnine.kimpd.src.Web.inquiry;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.inquiry.models.Inquiry;
import com.clnine.kimpd.src.Web.inquiry.models.InquiryCategory;
import com.clnine.kimpd.src.Web.inquiry.models.InquiryFile;
import com.clnine.kimpd.src.Web.inquiry.models.PostInquiryReq;
import com.clnine.kimpd.src.Web.project.models.GetProjectsRes;
import com.clnine.kimpd.src.Web.report.models.PostReportReq;
import com.clnine.kimpd.src.Web.report.models.Report;
import com.clnine.kimpd.src.Web.report.models.ReportCategory;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final UserInfoRepository userInfoRepository;
    private final InquiryCategoryRepository inquiryCategoryRepository;
    private final InquiryRepository inquiryRepository;
    private final InquiryFileRepository inquiryFileRepository;

    @Transactional
    public void postInquiry(int userIdx, PostInquiryReq postInquiryReq) throws BaseException {
        UserInfo userInfo;
        try {
            userInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx,"ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }


        InquiryCategory inquiryCategory;
        try{
            inquiryCategory = inquiryCategoryRepository.findAllByInquiryCategoryIdx(postInquiryReq.getInquiryCategoryIdx());
        }catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_INQUIRY_CATEGORIES);
        }
        String title = postInquiryReq.getInquiryTitle();
        String description = postInquiryReq.getInquiryDescription();
        Inquiry inquiry = new Inquiry(title,description,inquiryCategory,userInfo);

        if(postInquiryReq.getInquiryFileList()!=null){
            List<String> inquiryFileList = postInquiryReq.getInquiryFileList();

            for(int i=0;i<inquiryFileList.size();i++){
                String inquiryFileName = inquiryFileList.get(i);
                InquiryFile inquiryFile = new InquiryFile(inquiry,inquiryFileName);
                inquiryFileRepository.save(inquiryFile);
            }
        }


        try{
            inquiryRepository.save(inquiry);
        }catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_POST_INQUIRY);
        }
    }
}
