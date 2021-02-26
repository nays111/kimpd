package com.clnine.kimpd.src.WebAdmin.inquiry;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.WebAdmin.inquiry.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/web-admin")
@RequiredArgsConstructor
public class AdminInquiryController {
    private final AdminInquiryProvider adminInquiryProvider;
    private final AdminInquiryService adminInquiryService;

    /**
     * 1:1문의 전체 조회 API
     * [GET] /inquiries
     * @return BaseResponse<AdminGetInquiriesListRes>
     */
    @ResponseBody
    @GetMapping("/inquiries")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetInquiriesListRes> getInquiries() throws BaseException{
        List<AdminGetInquiriesRes> getInquiriesResList;

        try{
            getInquiriesResList = adminInquiryProvider.getInquiryList();
            AdminGetInquiriesListRes InquiryList = new AdminGetInquiriesListRes(getInquiriesResList);
            return new BaseResponse<>(SUCCESS_READ_INQUIRIES, InquiryList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 1:1문의 상세 조회 API
     * [GET] /inquiries/:inquiryIdx
     * @PathVariable inquiryIdx
     * @return BaseResponse<AdminGetNoticesRes>
     */
    @ResponseBody
    @GetMapping("/inquiries/{inquiryIdx}")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetInquiryRes> getInquiry(@PathVariable Integer inquiryIdx) {
        if (inquiryIdx == null || inquiryIdx <= 0) {
            return new BaseResponse<>(EMPTY_INQUIRY_IDX);
        }

        try {
            AdminGetInquiryRes adminGetInquiryRes = adminInquiryProvider.retrieveInquiryInfo(inquiryIdx);
            return new BaseResponse<>(SUCCESS_READ_INQUIRIES, adminGetInquiryRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 1:1문의 답글 API
     * [PATCH] /faqs
     * @RequestBody AdminPatchFaqsReq
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @PatchMapping("/inquiries")
    @CrossOrigin(origins = "*")
    public BaseResponse<Void> patchInquiries(@RequestBody AdminPatchInquiriesReq parameters) {
        if (parameters.getInquiryIdx() <= 0) {
            return new BaseResponse<>(EMPTY_INQUIRY_IDX);
        }

        if (parameters.getInquiryAnswer() == null || parameters.getInquiryAnswer().length() <= 0) {
            return new BaseResponse<>(EMPTY_INQUIRY_ANSWER);
        }

        try {
            adminInquiryService.updateInquiry(parameters);
            return new BaseResponse<>(SUCCESS_PATCH_FAQS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
