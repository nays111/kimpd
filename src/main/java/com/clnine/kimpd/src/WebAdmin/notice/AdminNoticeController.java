package com.clnine.kimpd.src.WebAdmin.notice;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.WebAdmin.notice.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/web-admin")
@RequiredArgsConstructor
public class AdminNoticeController {
    private final AdminNoticeProvider adminNoticeProvider;
    private final AdminNoticeService adminNoticeService;

    /**
     * 공지사항 전체 조회 API
     * [GET] /notices
     * @return BaseResponse<AdminGetNoticesListRes>
     */
    @ResponseBody
    @GetMapping("/notices")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetNoticesListRes> getNotices() throws BaseException{
        List<AdminGetNoticesRes> getNoticesResList;

        try{
            getNoticesResList = adminNoticeProvider.getNoticeList();
            AdminGetNoticesListRes noticeList = new AdminGetNoticesListRes(getNoticesResList);
            return new BaseResponse<>(SUCCESS_READ_NOTICES, noticeList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 공지사항 상세 조회 API
     * [GET] /notices/:noticeIdx
     * @PathVariable noticeIdx
     * @return BaseResponse<AdminGetNoticeRes>
     */
    @ResponseBody
    @GetMapping("/notices/{noticeIdx}")
    @CrossOrigin(origins = "*")
    public BaseResponse<AdminGetNoticeRes> getNotice(@PathVariable Integer noticeIdx) {
        if (noticeIdx== null || noticeIdx <= 0) {
            return new BaseResponse<>(EMPTY_NOTICE_IDX);
        }

        try {
            AdminGetNoticeRes adminGetNoticeRes = adminNoticeProvider.retrieveNoticeInfo(noticeIdx);
            return new BaseResponse<>(SUCCESS_READ_BANNERS, adminGetNoticeRes);
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
    public BaseResponse<Void> postNotices(@RequestBody AdminPostNoticesReq parameters) {
        // 1. Body Parameter Validation
        if (parameters.getNoticeTitle() == null || parameters.getNoticeTitle().length() <= 0) {
            return new BaseResponse<>(EMPTY_NOTICE_TITLE);
        }

        if (parameters.getNoticeDescription() == null || parameters.getNoticeDescription().length() <= 0) {
            return new BaseResponse<>(EMPTY_NOTICE_DESCRIPTION);
        }

        // 2. Post Notice
        try {
            adminNoticeService.createNotices(parameters);
            return new BaseResponse<>(SUCCESS_POST_NOTICES);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 공지사항 수정 API
     * [PATCH] /notices
     * @RequestBody AdminPatchNoticesReq
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @PatchMapping("/notices")
    @CrossOrigin(origins = "*")
    public BaseResponse<Void> patchNotices(@RequestBody AdminPatchNoticesReq parameters) {
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
            adminNoticeService.updateNotice(parameters);
            return new BaseResponse<>(SUCCESS_PATCH_BANNERS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
