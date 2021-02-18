package com.clnine.kimpd.src.WebAdmin.faq;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminFaq;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminPatchFaqsReq;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminPostFaqsReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


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
     * FAQ 등록 API
     * @param postNoticesReq
     * @return AdminPostFaqsReq
     * @throws BaseException
     */
    public void createFaqs(AdminPostFaqsReq postNoticesReq) throws BaseException {

        String faqQuestion = postNoticesReq.getFaqQuestion();
        String faqAnswer = postNoticesReq.getFaqAnswer();
        AdminFaq faqInfo = new AdminFaq(faqQuestion, faqAnswer);

        try {
            faqInfo = adminFaqRepository.save(faqInfo);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_FAQS);
        }
        return;
    }

    /**
     * FAQ 수정 (POST uri 가 겹쳤을때의 예시 용도)
     * @param adminPatchFaqsReq
     * @return void
     * @throws BaseException
     */
    public void updateFaq(AdminPatchFaqsReq adminPatchFaqsReq) throws BaseException {
        AdminFaq adminFaq = null;

        try {
            adminFaq = adminFaqProvider.retrieveFaqByFaqIdx(adminPatchFaqsReq.getFaqIdx());
            adminFaq.setFaqQuestion(adminPatchFaqsReq.getFaqQuestion());
            adminFaq.setFaqAnswer(adminPatchFaqsReq.getFaqAnswer());
            adminFaq.setStatus(adminPatchFaqsReq.getStatus());
            adminFaqRepository.save(adminFaq);
            return ;
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_FAQS);
        }
    }

}