package com.clnine.kimpd.src.WebAdmin.faq;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminFaq;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminPatchFaqsReq;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminPostFaqsReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_PATCH_NOTICES;
import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_POST_NOTICES;


@Service
public class AdminFaqService {
    private final AdminFaqRepository adminFaqRepository;
    private final AdminFaqProvider adminFaqProvider;

    @Autowired
    public AdminFaqService(AdminFaqRepository adminFaqRepository, AdminFaqProvider adminFaqProvider) {
        this.adminFaqRepository = adminFaqRepository;
        this.adminFaqProvider = adminFaqProvider;
    }

    /**
     * 공지사항 등록 API
     * @param postNoticesReq
     * @return AdminPostNoticesReq
     * @throws BaseException
     */
    public void createNotices(AdminPostFaqsReq postNoticesReq) throws BaseException {

        String noticeTitle = postNoticesReq.getNoticeTitle();
        String noticeDescription = postNoticesReq.getNoticeDescription();
        AdminFaq noticeInfo = new AdminFaq(noticeTitle, noticeDescription);

        try {
            noticeInfo = adminFaqRepository.save(noticeInfo);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_NOTICES);
        }
        return;
    }

    /**
     * 공지사항 수정 (POST uri 가 겹쳤을때의 예시 용도)
     * @param adminPatchFaqsReq
     * @return void
     * @throws BaseException
     */
    public void updateNotice(AdminPatchFaqsReq adminPatchFaqsReq) throws BaseException {
        AdminFaq adminFaq = null;

        try {
            adminFaq = adminFaqProvider.retrieveNoticeByNoticeIdx(adminPatchFaqsReq.getNoticeIdx());
            adminFaq.setNoticeTitle(adminPatchFaqsReq.getNoticeTitle());
            adminFaq.setNoticeDescription(adminPatchFaqsReq.getNoticeDescription());
            adminFaq.setStatus(adminPatchFaqsReq.getStatus());
            adminFaqRepository.save(adminFaq);
            return ;
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_NOTICES);
        }
    }

}