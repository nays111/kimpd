package com.clnine.kimpd.src.Web.inquiry;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.inquiry.models.*;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
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

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@Service
@RequiredArgsConstructor
public class InquiryProvider {
    private final InquiryCategoryRepository inquiryCategoryRepository;
    private final InquiryRepository inquiryRepository;
    private final UserInfoProvider userInfoProvider;
    private final InquiryFileRepository inquiryFileRepository;

    /**
     * 1:1 문의 카테고리 조회
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


    /**
     * 1:1문의 리스트 조회
     */
    public GetInquiriesRes getInquiryListRes(int page, int size) throws BaseException{
        Pageable pageable = PageRequest.of(page-1,size, Sort.by(Sort.Direction.DESC,"inquiryIdx"));
        List<Inquiry> inquiryList;
        int totalCount = 0;
        try{
            inquiryList = inquiryRepository.findAllByStatus("ACTIVE",pageable);
            totalCount = inquiryRepository.countAllByStatus("ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_INQUIRIES);
        }

        List<GetInquiriesDTO> getInquiryListResList = new ArrayList<>();
        for(Inquiry inquiry : inquiryList){
            int inquiryIdx = inquiry.getInquiryIdx();

            String answerStatus=null;
            if(inquiry.getInquiryAnswer()==null){
                answerStatus = "답변대기";
            }else{
                answerStatus = "답변완료";
            }

            String inquiryTitle = inquiry.getInquiryTitle();
            String userNickname = inquiry.getUserInfo().getNickname();
            Date createdAt = inquiry.getCreatedAt();
            SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd");
            String createdDate = sDate.format(createdAt);//4
            GetInquiriesDTO getInquiriesDTO = new GetInquiriesDTO(inquiryIdx,answerStatus,inquiryTitle,userNickname,createdDate);
            getInquiryListResList.add(getInquiriesDTO);

        }
        GetInquiriesRes getInquiriesRes = new GetInquiriesRes(totalCount,getInquiryListResList);
        return getInquiriesRes;
    }

    /**
     * 1:1 문의 글 상세 조회
     */
    public GetInquiryRes getInquiryRes(int userIdx, int inquiryIdx) throws BaseException{
        Inquiry inquiry = inquiryRepository.findAllByInquiryIdxAndStatus(inquiryIdx,"ACTIVE");
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        if(inquiry==null){
            throw new BaseException(NO_INQUIRY);
        }
        /**
         * 1:1 문의 글은 글 작성자만 볼 수 있음
         */
        if(inquiry.getUserInfo()!=userInfo){
            throw new BaseException(NOT_USER_INQUIRY);
        }
        List<InquiryFile> inquiryFiles = inquiryFileRepository.findAllByInquiryAndStatus(inquiry,"ACTIVE");
        List<String> inquiryFileUrlList  = new ArrayList<>();
        for(InquiryFile inquiryFile : inquiryFiles){
            String name = inquiryFile.getInquiryFileName();
            inquiryFileUrlList.add(name);
        }
        String inquiryTitle = inquiry.getInquiryTitle();
        String inquiryDescription = inquiry.getInquiryDescription();
        String userNickname = userInfo.getNickname();
        String inquiryAnswer = inquiry.getInquiryAnswer();
        Date createdAt = inquiry.getCreatedAt();
        Date updatedAt = inquiry.getUpdatedAt();
        SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd");
        String createdDate = sDate.format(createdAt);
        String answerDate;
        /**
         * 답변 여부
         */
        if(inquiryAnswer==null){
            answerDate=null;
        }else{
            answerDate = sDate.format(updatedAt);
        }
        GetInquiryRes getInquiryRes = new GetInquiryRes(inquiryTitle,inquiryDescription,inquiryFileUrlList,userNickname,createdDate,inquiryAnswer,answerDate);
        return getInquiryRes;
    }
}
