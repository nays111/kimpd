package com.clnine.kimpd.src.Web.inquiry;


import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.inquiry.models.GetInquiryCategoryRes;
import com.clnine.kimpd.src.Web.inquiry.models.GetInquiryListRes;
import com.clnine.kimpd.src.Web.inquiry.models.PostInquiryReq;
import com.clnine.kimpd.src.Web.report.models.GetReportCategoryRes;
import com.clnine.kimpd.src.Web.report.models.PostReportReq;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.SUCCESS;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryProvider inquiryProvider;
    private final JwtService jwtService;
    private final InquiryService inquiryService;

    /**
     * 1:1문의 유형 카테고리 조회 API
     * @return
     * @throws BaseException
     */
    @ResponseBody
    @GetMapping("/inquiry-categories")
    public BaseResponse<List<GetInquiryCategoryRes>> getInquiryCategory() throws BaseException{
        List<GetInquiryCategoryRes> getInquiryCategoryResList;
        try{
            getInquiryCategoryResList = inquiryProvider.getInquiryCategoryList();
            return new BaseResponse<>(SUCCESS,getInquiryCategoryResList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 1:1 문의 접수 API
     * @param postInquiryReq
     * @return
     * @throws BaseException
     */
    @ResponseBody
    @PostMapping("/inquiries")
    public BaseResponse<String> postInquiry(@RequestBody PostInquiryReq postInquiryReq) throws BaseException{
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            inquiryService.postInquiry(userIdx,postInquiryReq);
            return new BaseResponse<>(SUCCESS);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 1:1문의 내역 리스트 조회 API
     * @param page
     * @return
     */
    public BaseResponse<List<GetInquiryListRes>> getInquiryList(@RequestParam(required = true,value = "page")int page){
        try{
            inquiryProvider.getInquiryListRes(page,10);
            return new BaseResponse<>(SUCCESS);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
