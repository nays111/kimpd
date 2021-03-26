package com.clnine.kimpd.src.WebAdmin.faq;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminGetFaqsListRes;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminGetFaqsRes;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminPatchFaqsReq;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminPostFaqsReq;
import com.clnine.kimpd.src.WebAdmin.user.AdminUserInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/web-admin")
@RequiredArgsConstructor
public class AdminFaqController {
    private final AdminFaqProvider adminFaqProvider;
    private final AdminFaqService adminFaqService;
    private final AdminUserInfoProvider adminUserInfoProvider;

    /**
     * FAQ 전체 조회 API
     * [GET] /faqs
     * @return BaseResponse<AdminGetFaqsListRes>
     */
    @ResponseBody
    @GetMapping("/faqs")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetFaqsListRes> getFaqs() throws BaseException{
        List<AdminGetFaqsRes> getFaqsResList;

        try{
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            getFaqsResList = adminFaqProvider.getFaqList();
            AdminGetFaqsListRes faqList = new AdminGetFaqsListRes(getFaqsResList);
            return new BaseResponse<>(SUCCESS, faqList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * FAQ 상세 조회 API
     * [GET] /faqs/:faqIdx
     * @PathVariable faqIdx
     * @return BaseResponse<AdminGetNoticesRes>
     */
    @ResponseBody
    @GetMapping("/faqs/{faqIdx}")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetFaqsRes> getFaq(@PathVariable Integer faqIdx) {
        if (faqIdx == null || faqIdx <= 0) {
            return new BaseResponse<>(EMPTY_FAQ_IDX);
        }

        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            AdminGetFaqsRes adminGetFaqsRes = adminFaqProvider.retrieveFaqInfo(faqIdx);
            return new BaseResponse<>(SUCCESS, adminGetFaqsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * FAQ 등록 API
     * [POST] /faqs
     * @RequestBody AdminPostFaqsReq
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @PostMapping("/faqs")
    @CrossOrigin(origins = "*")
    public BaseResponse<Void> postFaqs(@RequestBody AdminPostFaqsReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getFaqQuestion() == null || parameters.getFaqQuestion().length() <= 0) {
            return new BaseResponse<>(EMPTY_FAQ_QUESTION);
        }

        if (parameters.getFaqAnswer() == null || parameters.getFaqAnswer().length() <= 0) {
            return new BaseResponse<>(EMPTY_FAQ_ANSWER);
        }

        // 2. Post Faq
        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            adminFaqService.createFaqs(parameters);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * FAQ 수정 API
     * [PATCH] /faqs
     * @RequestBody AdminPatchFaqsReq
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @PatchMapping("/faqs")
    @CrossOrigin(origins = "*")
    public BaseResponse<Void> patchFaqs(@RequestBody AdminPatchFaqsReq parameters) {
        if (parameters.getFaqIdx() <= 0) {
            return new BaseResponse<>(EMPTY_FAQ_IDX);
        }

        if (parameters.getFaqQuestion() == null || parameters.getFaqQuestion().length() <= 0) {
            return new BaseResponse<>(EMPTY_FAQ_QUESTION);
        }

        if (parameters.getFaqAnswer() == null || parameters.getFaqAnswer().length() <= 0) {
            return new BaseResponse<>(EMPTY_FAQ_ANSWER);
        }

        try {
            if(adminUserInfoProvider.checkJWT() == false){
                return new BaseResponse<>(INVALID_JWT);
            }

            adminFaqService.updateFaq(parameters);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
