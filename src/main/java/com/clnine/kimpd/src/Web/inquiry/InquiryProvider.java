package com.clnine.kimpd.src.Web.inquiry;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.inquiry.models.GetInquiryCategoryRes;
import com.clnine.kimpd.src.Web.inquiry.models.InquiryCategory;
import com.clnine.kimpd.src.Web.report.models.GetReportCategoryRes;
import com.clnine.kimpd.src.Web.report.models.ReportCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_INQUIRY_CATEGORIES;


@Service
@RequiredArgsConstructor
public class InquiryProvider {
    private final InquiryCategoryRepository inquiryCategoryRepository;

    /**
     * 1:1문의 카테고리 조회 API
     * @return
     * @throws BaseException
     */
    public List<GetInquiryCategoryRes> getInquiryCategoryList() throws BaseException{
        List<InquiryCategory> inquiryCategoryList;
        try{
            inquiryCategoryList = inquiryCategoryRepository.findAll();
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_INQUIRY_CATEGORIES);
        }
        return inquiryCategoryList.stream().map(inquiryCategory -> {
            int inquiryCategoryIdx = inquiryCategory.getInquiryCategoryIdx();
            String inquiryCategoryName = inquiryCategory.getInquiryCategoryName();
            return new GetInquiryCategoryRes(inquiryCategoryIdx,inquiryCategoryName);
        }).collect(Collectors.toList());
    }
}
