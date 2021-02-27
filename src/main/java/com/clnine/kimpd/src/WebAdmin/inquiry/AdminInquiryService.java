package com.clnine.kimpd.src.WebAdmin.inquiry;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.inquiry.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@Service
public class AdminInquiryService {
    private final AdminInquiryRepository adminInquiryRepository;
    private final AdminInquiryProvider adminInquiryProvider;

    @Autowired
    public AdminInquiryService(AdminInquiryRepository adminInquiryRepository, AdminInquiryProvider adminInquiryProvider) {
        this.adminInquiryRepository = adminInquiryRepository;
        this.adminInquiryProvider = adminInquiryProvider;
    }

    /**
     * 1:1문의 답글 (POST uri 가 겹쳤을때의 예시 용도)
     * @param adminPatchInquiriesReq
     * @return void
     * @throws BaseException
     */
    public void updateInquiry(AdminPatchInquiriesReq adminPatchInquiriesReq) throws BaseException {
        AdminInquiry adminInquiry = null;

        try {
            adminInquiry = adminInquiryProvider.retrieveInquiryByInquiryIdx(adminPatchInquiriesReq.getInquiryIdx());
            if(adminInquiry == null){
                throw new BaseException(FAILED_TO_PATCH_INQUIRIES);
            }
            adminInquiry.setInquiryAnswer(adminPatchInquiriesReq.getInquiryAnswer());
            adminInquiry.setStatus(adminPatchInquiriesReq.getStatus());
            adminInquiryRepository.save(adminInquiry);
            return ;
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_INQUIRIES);
        }
    }

}