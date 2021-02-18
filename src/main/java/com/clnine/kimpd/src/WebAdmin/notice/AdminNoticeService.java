package com.clnine.kimpd.src.WebAdmin.notice;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.notice.models.AdminNotice;
import com.clnine.kimpd.src.WebAdmin.notice.models.AdminPatchNoticesReq;
import com.clnine.kimpd.src.WebAdmin.notice.models.AdminPostNoticesReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@Service
public class AdminNoticeService {
    private final AdminNoticeRepository adminNoticeRepository;
    private final AdminNoticeProvider adminNoticeProvider;

    @Autowired
    public AdminNoticeService(AdminNoticeRepository adminNoticeRepository, AdminNoticeProvider adminNoticeProvider) {
        this.adminNoticeRepository = adminNoticeRepository;
        this.adminNoticeProvider = adminNoticeProvider;
    }

    /**
     * 공지사항 등록 API
     * @param postNoticesReq
     * @return AdminPostNoticesReq
     * @throws BaseException
     */
    public void createNotices(AdminPostNoticesReq postNoticesReq) throws BaseException {

        String noticeTitle = postNoticesReq.getNoticeTitle();
        String noticeDescription = postNoticesReq.getNoticeDescription();
        AdminNotice noticeInfo = new AdminNotice(noticeTitle, noticeDescription);

        try {
            noticeInfo = adminNoticeRepository.save(noticeInfo);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_NOTICES);
        }
        return;
    }

    /**
     * 공지사항 수정 (POST uri 가 겹쳤을때의 예시 용도)
     * @param adminPatchNoticesReq
     * @return void
     * @throws BaseException
     */
    public void updateNotice(AdminPatchNoticesReq adminPatchNoticesReq) throws BaseException {
        AdminNotice adminNotice = null;

        try {
            adminNotice = adminNoticeProvider.retrieveNoticeByNoticeIdx(adminPatchNoticesReq.getNoticeIdx());
            adminNotice.setNoticeTitle(adminPatchNoticesReq.getNoticeTitle());
            adminNotice.setNoticeDescription(adminPatchNoticesReq.getNoticeDescription());
            adminNotice.setStatus(adminPatchNoticesReq.getStatus());
            adminNoticeRepository.save(adminNotice);
            return ;
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_NOTICES);
        }
    }

}