package com.clnine.kimpd.src.Web.faq;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.faq.models.GetFaqsRes;
import com.clnine.kimpd.src.Web.notice.models.GetNoticesRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin
public class FaqController {

    private final FaqProvider faqProvider;

    @ResponseBody
    @GetMapping("/faqs")
    public BaseResponse<List<GetFaqsRes>> getNoticesRes(@RequestParam(required = true) int page){
        try{
            List<GetFaqsRes> getFaqsResList = faqProvider.getFaqList(page,5);
            return new BaseResponse<>(SUCCESS,getFaqsResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
