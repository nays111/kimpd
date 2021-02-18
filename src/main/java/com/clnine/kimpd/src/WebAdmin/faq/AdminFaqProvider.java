package com.clnine.kimpd.src.WebAdmin.faq;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminGetFaqsRes;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminFaq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_NOTICES;

@Service
public class AdminFaqProvider {

    @Autowired
    private final AdminFaqRepository adminFaqRepository;

    AdminFaqProvider(AdminFaqRepository adminFaqRepository){
        this.adminFaqRepository = adminFaqRepository;

    }

    /**
     * 공지사항 전체 조회
     * @return List<AdminGetNoticesRes>
     * @throws BaseException
     */
    public List<AdminGetFaqsRes> getNoticeList() throws BaseException{
        List<AdminFaq> noticesList;
        try{
            noticesList = adminFaqRepository.findAll();
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_NOTICES);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return noticesList.stream().map(Notice ->{
            int noticeIdx = Notice.getNoticeIdx();
            String noticeTitle = Notice.getNoticeTitle();
            String noticeDescription = Notice.getNoticeDescription();
            String status = Notice.getStatus();
            return new AdminGetFaqsRes(noticeIdx, noticeTitle, noticeDescription, status);
        }).collect(Collectors.toList());
    }

    /**
     * 공지사항 상세 조회
     * @param noticeIdx
     * @return AdminGetNoticesRes
     * @throws BaseException
     */
    public AdminGetFaqsRes retrieveNoticeInfo(int noticeIdx) throws BaseException {
        // 1. DB에서 noticeIdx AdminNotice 조회
        AdminFaq adminFaq = retrieveNoticeByNoticeIdx(noticeIdx);
        if(adminFaq == null){
            throw new BaseException(FAILED_TO_GET_NOTICES);
        }
        // 2. AdminGetNoticesRes 변환하여 return
        String noticeTitle = adminFaq.getNoticeTitle();
        String noticeDescription = adminFaq.getNoticeDescription();
        String status = adminFaq.getStatus();

        return new AdminGetFaqsRes(noticeIdx, noticeTitle, noticeDescription, status);
    }

    /**
     * 공지사항 조회
     * @param noticeIdx
     * @return AdminNotice
     * @throws BaseException
     */
    public AdminFaq retrieveNoticeByNoticeIdx(int noticeIdx) throws BaseException {
        // 1. DB에서 AdminNotice 조회
        AdminFaq adminFaq;
        try {
            adminFaq = adminFaqRepository.findById(noticeIdx).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_NOTICES);
        }

        // 2. AdminNotice return
        return adminFaq;
    }

}
