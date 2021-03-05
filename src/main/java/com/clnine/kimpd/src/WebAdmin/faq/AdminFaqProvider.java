package com.clnine.kimpd.src.WebAdmin.faq;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminGetFaqsRes;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminFaq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
public class AdminFaqProvider {

    @Autowired
    private final AdminFaqRepository adminFaqRepository;

    AdminFaqProvider(AdminFaqRepository adminFaqRepository){
        this.adminFaqRepository = adminFaqRepository;

    }

    /**
     * FAQ 전체 조회
     * @return List<AdminGetFaqsRes>
     * @throws BaseException
     */
    public List<AdminGetFaqsRes> getFaqList() throws BaseException{
        List<AdminFaq> faqList;
        try{
            faqList = adminFaqRepository.findAll();
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_FAQS);
        }

        return faqList.stream().map(Faq ->{
            int faqIdx = Faq.getFaqIdx();
            String faqQuestion = Faq.getFaqQuestion();
            String faqAnswer = Faq.getFaqAnswer();
            String status = Faq.getStatus();
            return new AdminGetFaqsRes(faqIdx, faqQuestion, faqAnswer, status);
        }).collect(Collectors.toList());
    }

    /**
     * FAQ 상세 조회
     * @param faqIdx
     * @return AdminGetFaqsRes
     * @throws BaseException
     */
    public AdminGetFaqsRes retrieveFaqInfo(int faqIdx) throws BaseException {
        // 1. DB에서 noticeIdx AdminNotice 조회
        AdminFaq adminFaq = retrieveFaqByFaqIdx(faqIdx);
        if(adminFaq == null){
            throw new BaseException(FAILED_TO_GET_FAQS);
        }
        // 2. AdminGetNoticesRes 변환하여 return
        String faqQuestion = adminFaq.getFaqQuestion();
        String faqAnswer = adminFaq.getFaqAnswer();
        String status = adminFaq.getStatus();

        return new AdminGetFaqsRes(faqIdx, faqQuestion, faqAnswer, status);
    }

    /**
     * FAQ 조회
     * @param faqIdx
     * @return AdminFaq
     * @throws BaseException
     */
    public AdminFaq retrieveFaqByFaqIdx(int faqIdx) throws BaseException {
        // 1. DB에서 AdminFaq 조회
        AdminFaq adminFaq;
        try {
            adminFaq = adminFaqRepository.findById(faqIdx).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_FAQS);
        }

        // 2. AdminFaq return
        return adminFaq;
    }

}
