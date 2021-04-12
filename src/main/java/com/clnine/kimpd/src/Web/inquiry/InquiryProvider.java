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
        int temp = totalCount;
        List<GetInquiriesDTO> getInquiryListResList = new ArrayList<>();
        for(int i=0;i<inquiryList.size();i++){
            int no = temp;
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
            GetInquiriesDTO getInquiriesDTO = new GetInquiriesDTO(no,inquiryIdx,answerStatus,inquiryTitle,userNickname,createdDate);
            getInquiryListResList.add(getInquiriesDTO);
            temp--;
        }
        GetInquiriesRes getInquiriesRes = new GetInquiriesRes(totalCount,getInquiryListResList);
        return getInquiriesRes;
    }

    public GetInquiryRes getInquiryRes(int userIdx, int inquiryIdx) throws BaseException{
        Inquiry inquiry = inquiryRepository.findAllByInquiryIdxAndStatus(inquiryIdx,"ACTIVE");
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        if(inquiry==null){
            throw new BaseException(NO_INQUIRY);
        }
        if(inquiry.getUserInfo()!=userInfo){
            throw new BaseException(NOT_USER_INQUIRY);
        }
        List<InquiryFile> inquiryFiles = inquiryFileRepository.findAllByInquiryAndStatus(inquiry,"ACTIVE");
        List<String> inquiryFileUrlList  = new ArrayList<>();
        for(int i=0;i<inquiryFiles.size();i++){
            String name = inquiryFiles.get(i).getInquiryFileName();
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
        if(inquiryAnswer==null){
            answerDate=null;
        }else{
            answerDate = sDate.format(updatedAt);
        }
        GetInquiryRes getInquiryRes = new GetInquiryRes(inquiryTitle,inquiryDescription,inquiryFileUrlList,userNickname,createdDate,inquiryAnswer,answerDate);
        return getInquiryRes;
    }
}
