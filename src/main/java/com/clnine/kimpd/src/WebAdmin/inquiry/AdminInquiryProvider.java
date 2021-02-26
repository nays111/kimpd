package com.clnine.kimpd.src.WebAdmin.inquiry;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.inquiry.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
public class AdminInquiryProvider {

    @Autowired
    private final AdminInquiryRepository adminInquiryRepository;
    private final AdminInquiryFileRepository adminInquiryFileRepository;

    AdminInquiryProvider(AdminInquiryRepository adminInquiryRepository, AdminInquiryFileRepository adminInquiryFileRepository){
        this.adminInquiryRepository = adminInquiryRepository;
        this.adminInquiryFileRepository = adminInquiryFileRepository;

    }

    /**
     * Inquiry 전체 조회
     * @return List<AdminGetInquiriesRes>
     * @throws BaseException
     */
    public List<AdminGetInquiriesRes> getInquiryList() throws BaseException{
        List<AdminInquiry> inquiryList;
        try{
            inquiryList = adminInquiryRepository.findAll();
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_FAQS);
        }

        SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd");
        return inquiryList.stream().map(Inquiry ->{
            int inquiryIdx = Inquiry.getInquiryIdx();
            String inquiryAnswer = Inquiry.getInquiryAnswer();
            String answerState = null;
            if(inquiryAnswer == null || inquiryAnswer.length() == 0){
                answerState = "답변대기";
            }
            else{
                answerState = "답변완료";
            }
            String inquiryTitle = Inquiry.getInquiryTitle();
            String nickname = Inquiry.getAdminUserInfo().getNickname();
            String createdAt = sDate.format(Inquiry.getCreatedAt());
            String status = Inquiry.getStatus();
            return new AdminGetInquiriesRes(inquiryIdx, answerState, inquiryTitle, nickname, createdAt, status);
        }).collect(Collectors.toList());
    }

    /**
     * 1:1문의 상세 조회
     * @param inquiryIdx
     * @return AdminGetInquiryRes
     * @throws BaseException
     */
    public AdminGetInquiryRes retrieveInquiryInfo(int inquiryIdx) throws BaseException {
        // 1. DB에서 inquiryIdx AdminInquiry 조회
        AdminInquiry adminInquiry = retrieveInquiryByInquiryIdx(inquiryIdx);

        if(adminInquiry == null){
            throw new BaseException(FAILED_TO_GET_INQUIRIES);
        }

        ArrayList<AdminInquiryFile> adminInquiryFileList = adminInquiryFileRepository.findAllByAdminInquiry(adminInquiry);

        if(adminInquiryFileList == null){
            throw new BaseException(FAILED_TO_GET_INQUIRIES);
        }

        ArrayList<String> fileList = new ArrayList<>();
        for(int i = 0; i < adminInquiryFileList.size(); i++){
            fileList.add(i, adminInquiryFileList.get(i).getInquiryFileName());
        }

        // 2. AdminGetInquiryRes 변환하여 return
        return new AdminGetInquiryRes(adminInquiry.getAdminInquiryCategory().getInquiryCategoryName(),
                adminInquiry.getInquiryTitle(), adminInquiry.getAdminUserInfo().getNickname(), adminInquiry.getInquiryDescription(),
                adminInquiry.getInquiryAnswer(), fileList, adminInquiry.getStatus());
    }

    /**
     * Inquiry 조회
     * @param inquiryIdx
     * @return AdminInquiry
     * @throws BaseException
     */
    public AdminInquiry retrieveInquiryByInquiryIdx(int inquiryIdx) throws BaseException {
        // 1. DB에서 AdminInquiry 조회
        AdminInquiry adminInquiry;
        try {
            adminInquiry = adminInquiryRepository.findById(inquiryIdx).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_INQUIRIES);
        }

        // 2. AdminInquiry return
        return adminInquiry;
    }
}
