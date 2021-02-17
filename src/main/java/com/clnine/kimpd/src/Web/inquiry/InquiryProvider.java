package com.clnine.kimpd.src.Web.inquiry;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.inquiry.models.GetInquiryCategoryRes;
import com.clnine.kimpd.src.Web.inquiry.models.GetInquiryListRes;
import com.clnine.kimpd.src.Web.inquiry.models.Inquiry;
import com.clnine.kimpd.src.Web.inquiry.models.InquiryCategory;
import com.clnine.kimpd.src.Web.report.models.GetReportCategoryRes;
import com.clnine.kimpd.src.Web.report.models.ReportCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_INQUIRIES;
import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_INQUIRY_CATEGORIES;


@Service
@RequiredArgsConstructor
public class InquiryProvider {
    private final InquiryCategoryRepository inquiryCategoryRepository;
    private final InquiryRepository inquiryRepository;

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

    public List<GetInquiryListRes> getInquiryListRes(int page,int size) throws BaseException{
        Pageable pageable = PageRequest.of(page-1,size, Sort.by(Sort.Direction.DESC,"inquiryIdx"));
        List<Inquiry> inquiryList;
        try{
            inquiryList = inquiryRepository.findAllByStatus("ACTIVE",pageable);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_INQUIRIES);
        }
        List<GetInquiryListRes> getInquiryListResList = new ArrayList<>();
        for(int i=0;i<inquiryList.size();i++){
            int no = inquiryList.size()+1-i;
            int inquiryIdx = inquiryList.get(i).getInquiryIdx();
            String answerStatus=null;
            if(inquiryList.get(i).getInquiryAnswer()==null){
                answerStatus = "답변대기";
            }else{
                answerStatus = "답변완료";
            }
            String inquiryTitle = inquiryList.get(i).getInquiryTitle();
            String userNickname = inquiryList.get(i).getUserInfo().getNickname();
            Date createdAt = inquiryList.get(i).getCreatedAt();
            SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd");
            String createdDate = sDate.format(createdAt);//4
            GetInquiryListRes getInquiryListRes = new GetInquiryListRes(no,inquiryIdx,answerStatus,inquiryTitle,userNickname,createdDate);
            getInquiryListResList.add(getInquiryListRes);
        }

        return getInquiryListResList;
    }
}
