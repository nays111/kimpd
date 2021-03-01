package com.clnine.kimpd.src.WebAdmin.casting;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.WebAdmin.casting.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/web-admin")
@RequiredArgsConstructor
public class AdminCastingController {
    private final AdminCastingProvider adminCastingProvider;
    private final AdminCastingService adminCastingService;

    /**
     * 섭외 전체 조회 API
     * [GET] /castings
     * @return BaseResponse<AdminGetCastingsListRes>
     */
    @ResponseBody
    @GetMapping("/castings")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetCastingsListRes> getCastings() throws BaseException {
        List<AdminGetCastingsRes> getInquiriesResList;

        try{
            getInquiriesResList = adminCastingProvider.getCastingList();
            AdminGetCastingsListRes castingList = new AdminGetCastingsListRes(getInquiriesResList);
            return new BaseResponse<>(SUCCESS_READ_INQUIRIES, castingList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 섭외 상세 조회 API
     * [GET] /castings/:castingIdx
     * @PathVariable castingIdx
     * @return BaseResponse<AdminGetCastingRes>
     */
    @ResponseBody
    @GetMapping("/castings/{castingIdx}")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetCastingRes> getCasting(@PathVariable Integer castingIdx) {
        if (castingIdx == null || castingIdx <= 0) {
            return new BaseResponse<>(EMPTY_INQUIRY_IDX);
        }

        try {
            AdminGetCastingRes adminGetCastingRes = adminCastingProvider.retrieveCastingInfo(castingIdx);
            return new BaseResponse<>(SUCCESS_READ_INQUIRIES, adminGetCastingRes);
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
//    @ResponseBody
//    @PatchMapping("/inquiries")
//    @CrossOrigin(origins = "*")
//    public BaseResponse<Void> patchInquiries(@RequestBody AdminPatchInquiriesReq parameters) {
//        if (parameters.getInquiryIdx() <= 0) {
//            return new BaseResponse<>(EMPTY_INQUIRY_IDX);
//        }
//
//        if (parameters.getInquiryAnswer() == null || parameters.getInquiryAnswer().length() <= 0) {
//            return new BaseResponse<>(EMPTY_INQUIRY_ANSWER);
//        }
//
//        try {
//            adminInquiryService.updateInquiry(parameters);
//            return new BaseResponse<>(SUCCESS_PATCH_FAQS);
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }
}
