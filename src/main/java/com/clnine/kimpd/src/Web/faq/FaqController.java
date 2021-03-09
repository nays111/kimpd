package com.clnine.kimpd.src.Web.faq;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.faq.models.GetFaqsDTO;
import com.clnine.kimpd.src.Web.faq.models.GetFaqsRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.EMPTY_PAGE;
import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin
public class FaqController {

    private final FaqProvider faqProvider;

    @ResponseBody @GetMapping("/faqs")
    public BaseResponse<GetFaqsRes> getNoticesRes(@RequestParam(required = true) Integer page){
        if(page==null){
            return new BaseResponse<>(EMPTY_PAGE);
        }
        try{
            GetFaqsRes getFaqsRes = faqProvider.getFaqList(page,5);
            return new BaseResponse<>(SUCCESS, getFaqsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
