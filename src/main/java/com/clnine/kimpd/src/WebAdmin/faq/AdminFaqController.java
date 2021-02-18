package com.clnine.kimpd.src.WebAdmin.faq;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminGetFaqsListRes;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminGetFaqsRes;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminPatchFaqsReq;
import com.clnine.kimpd.src.WebAdmin.faq.models.AdminPostFaqsReq;
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

    /**
     * 공지사항 전체 조회 API
     * [GET] /notices
     * @return BaseResponse<AdminGetNoticesListRes>
     */
    @ResponseBody
    @GetMapping("/notices")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetFaqsListRes> getBanners() throws BaseException{
        List<AdminGetFaqsRes> getNoticesResList;

        try{
            getNoticesResList = adminFaqProvider.getNoticeList();
            AdminGetFaqsListRes bannerList = new AdminGetFaqsListRes(getNoticesResList);
            return new BaseResponse<>(SUCCESS_READ_NOTICES, bannerList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 공지사항 상세 조회 API
     * [GET] /notices/:noticeIdx
     * @PathVariable noticeIdx
     * @return BaseResponse<AdminGetNoticesRes>
     */
    @ResponseBody
    @GetMapping("/notices/{noticeIdx}")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetFaqsRes> getUser(@PathVariable Integer noticeIdx) {
        if (noticeIdx== null || noticeIdx <= 0) {
            return new BaseResponse<>(EMPTY_NOTICE_IDX);
        }

        try {
            AdminGetFaqsRes adminGetFaqsRes = adminFaqProvider.retrieveNoticeInfo(noticeIdx);
            return new BaseResponse<>(SUCCESS_READ_BANNERS, adminGetFaqsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 공지사항 등록 API
     * [POST] /notices
     * @RequestBody AdminPostNoticesReq
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @PostMapping("/notices")
    @CrossOrigin(origins = "*")
    public BaseResponse<Void> postNotices(@RequestBody AdminPostFaqsReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getNoticeTitle() == null || parameters.getNoticeTitle().length() <= 0) {
            return new BaseResponse<>(EMPTY_BANNER_END_DATE);
        }

        if (parameters.getNoticeDescription() == null || parameters.getNoticeDescription().length() <= 0) {
            return new BaseResponse<>(EMPTY_BANNER_IMAGE);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 2. Post Notice
        try {
            adminFaqService.createNotices(parameters);
            return new BaseResponse<>(SUCCESS_POST_NOTICES);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 광고 수정 API
     * [PATCH] /notices
     * @RequestBody AdminPatchNoticesReq
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @PatchMapping("/notices")
    @CrossOrigin(origins = "*")
    public BaseResponse<Void> patchNotices(@RequestBody AdminPatchFaqsReq parameters) {
        if (parameters.getNoticeIdx() <= 0) {
            return new BaseResponse<>(EMPTY_NOTICE_IDX);
        }

        if (parameters.getNoticeTitle() == null || parameters.getNoticeTitle().length() <= 0) {
            return new BaseResponse<>(EMPTY_NOTICE_TITLE);
        }

        if (parameters.getNoticeDescription() == null || parameters.getNoticeDescription().length() <= 0) {
            return new BaseResponse<>(EMPTY_NOTICE_DESCRIPTION);
        }

        try {
            adminFaqService.updateNotice(parameters);
            return new BaseResponse<>(SUCCESS_PATCH_BANNERS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
