package com.clnine.kimpd.src.Web.inquiry;


import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.inquiry.models.*;
import com.clnine.kimpd.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryProvider inquiryProvider;
    private final JwtService jwtService;
    private final InquiryService inquiryService;

    @ResponseBody
    @GetMapping("/inquiry-categories")
    @Operation(summary="1:1문의 유형 카테고리 조회 API",description = "토큰이 필요합니다.")
    public BaseResponse<List<GetInquiryCategoryRes>> getInquiryCategory(){
        try{
            Integer userIdx = jwtService.getUserIdx();
            List<GetInquiryCategoryRes> getInquiryCategoryResList = inquiryProvider.getInquiryCategoryList();
            return new BaseResponse<>(SUCCESS,getInquiryCategoryResList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/inquiries")
    @Operation(summary="1:1 문의 접수 API",description = "토큰이 필요합니다.")
    public BaseResponse<String> postInquiry(@RequestBody PostInquiryReq postInquiryReq){
        if(postInquiryReq.getInquiryCategoryIdx()==null){
            return new BaseResponse<>(EMPTY_INQUIRY_CATEGORY_SELECTED);
        }
        if(postInquiryReq.getInquiryTitle()==null || postInquiryReq.getInquiryTitle().length()==0){
            return new BaseResponse<>(EMPTY_INQUIRY_TITLE);
        }
        if(postInquiryReq.getInquiryDescription()==null || postInquiryReq.getInquiryDescription().length()==0){
            return new BaseResponse<>(EMPTY_INQUIRY_DESCRIPTION);
        }
        try{
            Integer userIdx = jwtService.getUserIdx();
            inquiryService.postInquiry(userIdx,postInquiryReq);
            return new BaseResponse<>(SUCCESS);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/inquiries")
    @Operation(summary="1:1문의 내역 리스트 조회 API")
    public BaseResponse<GetInquiriesRes> getInquiryList(@RequestParam(required = true,value = "page")Integer page){
        if(page==null){
            return new BaseResponse<>(EMPTY_PAGE);
        }
        try{
            GetInquiriesRes getInquiriesRes = inquiryProvider.getInquiryListRes(page,10);
            return new BaseResponse<>(SUCCESS,getInquiriesRes);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/inquiries/{inquiryIdx}")
    @Operation(summary = "1:1문의 내역 상세 조회 API",description = "토큰이 필요합니다.(본인의 1:1문의만 상세 조회가 가능합니다.")
    public BaseResponse<GetInquiryRes> getInquiry(@PathVariable(required = true)int inquiryIdx){
        try{
            Integer userIdx = jwtService.getUserIdx();
            GetInquiryRes getInquiryRes = inquiryProvider.getInquiryRes(userIdx,inquiryIdx);
            return new BaseResponse<>(SUCCESS,getInquiryRes);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
