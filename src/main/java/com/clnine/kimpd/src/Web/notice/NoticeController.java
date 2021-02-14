package com.clnine.kimpd.src.Web.notice;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.notice.models.GetNoticesRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin
public class NoticeController {
    private final NoticeProvider noticeProvider;

    /**
     * 공지사항 조회 API
     * @param page
     * @return
     */
    public BaseResponse<List<GetNoticesRes>> getNoticesRes(@RequestParam(required = true) int page){
        try{
            List<GetNoticesRes> getNoticesResList = noticeProvider.getNoticeList(page,8);
            return new BaseResponse<>(SUCCESS,getNoticesResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
