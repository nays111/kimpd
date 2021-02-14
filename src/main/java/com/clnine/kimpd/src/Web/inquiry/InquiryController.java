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

import static com.clnine.kimpd.config.BaseResponseStatus.*;

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
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
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
        if(postInquiryReq.getInquiryCategoryIdx()==null){
            return new BaseResponse<>(EMPTY_INQUIRY_CATEGORY_SELECTED);
        }
        if(postInquiryReq.getInquiryTitle()==null || postInquiryReq.getInquiryTitle().length()==0){
            return new BaseResponse<>(EMPTY_INQUIRY_TITLE);
        }
        if(postInquiryReq.getInquiryDescription()==null || postInquiryReq.getInquiryDescription().length()==0){
            return new BaseResponse<>(EMPTY_INQUIRY_DESCRIPTION);
        }
        //todo 첨부파일 validation
//        if(postInquiryReq.getInquiryFileList()!=null) {
//            if (postInquiryReq.getInquiryFileList().size() == 0) {
//                return new BaseResponse<>(EMPTY_INQUIRY_DESCRIPTION);
//            }
//        }


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
    @ResponseBody
    @GetMapping("/inquiries")
    public BaseResponse<List<GetInquiryListRes>> getInquiryList(@RequestParam(required = true,value = "page")int page){
        try{
            List<GetInquiryListRes> getInquiryListResList = inquiryProvider.getInquiryListRes(page,10);
            return new BaseResponse<>(SUCCESS,getInquiryListResList);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
