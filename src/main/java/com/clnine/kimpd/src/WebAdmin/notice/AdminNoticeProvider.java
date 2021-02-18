package com.clnine.kimpd.src.WebAdmin.notice;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.notice.models.AdminNotice;
import com.clnine.kimpd.src.WebAdmin.notice.models.AdminGetNoticesRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
public class AdminNoticeProvider {

    @Autowired
    private final AdminNoticeRepository adminNoticeRepository;

    AdminNoticeProvider(AdminNoticeRepository adminNoticeRepository){
        this.adminNoticeRepository = adminNoticeRepository;

    }

    /**
     * 공지사항 전체 조회
     * @return List<AdminGetNoticesRes>
     * @throws BaseException
     */
    public List<AdminGetNoticesRes> getNoticeList() throws BaseException{
        List<AdminNotice> noticesList;
        try{
            noticesList = adminNoticeRepository.findAll();
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_GET_NOTICES);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return noticesList.stream().map(Notice ->{
            int noticeIdx = Notice.getNoticeIdx();
            String noticeTitle = Notice.getNoticeTitle();
            String noticeDescription = Notice.getNoticeDescription();
            String status = Notice.getStatus();
            return new AdminGetNoticesRes(noticeIdx, noticeTitle, noticeDescription, status);
        }).collect(Collectors.toList());
    }

    /**
     * 공지사항 상세 조회
     * @param noticeIdx
     * @return AdminGetNoticesRes
     * @throws BaseException
     */
    public AdminGetNoticesRes retrieveNoticeInfo(int noticeIdx) throws BaseException {
        // 1. DB에서 noticeIdx AdminNotice 조회
        AdminNotice adminNotice = retrieveNoticeByNoticeIdx(noticeIdx);
        if(adminNotice == null){
            throw new BaseException(FAILED_TO_GET_NOTICES);
        }
        // 2. AdminGetNoticesRes 변환하여 return
        String noticeTitle = adminNotice.getNoticeTitle();
        String noticeDescription = adminNotice.getNoticeDescription();
        String status = adminNotice.getStatus();

        return new AdminGetNoticesRes(noticeIdx, noticeTitle, noticeDescription, status);
    }

    /**
     * 공지사항 조회
     * @param noticeIdx
     * @return AdminNotice
     * @throws BaseException
     */
    public AdminNotice retrieveNoticeByNoticeIdx(int noticeIdx) throws BaseException {
        // 1. DB에서 AdminNotice 조회
        AdminNotice adminNotice;
        try {
            adminNotice = adminNoticeRepository.findById(noticeIdx).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_NOTICES);
        }

        // 2. AdminNotice return
        return adminNotice;
    }

}
