package com.clnine.kimpd.src.Web.notice;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.notice.models.GetNoticesDTO;
import com.clnine.kimpd.src.Web.notice.models.GetNoticesRes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.EMPTY_PAGE;
import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin
public class NoticeController {
    private final NoticeProvider noticeProvider;

    @ResponseBody
    @GetMapping("/notices")
    @Operation(summary = "공지사항 조회 API")
    public BaseResponse<GetNoticesRes> getNoticesRes(@RequestParam(required = true) Integer page){
        if(page==null){
            return new BaseResponse<>(EMPTY_PAGE);
        }
        try{
            GetNoticesRes getNoticesRes = noticeProvider.getNoticeList(page,8);
            return new BaseResponse<>(SUCCESS, getNoticesRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
