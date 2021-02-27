package com.clnine.kimpd.src.WebAdmin.casting;

import com.clnine.kimpd.config.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminCastingService {
    private final AdminCastingRepository adminCastingRepository;
    private final AdminCastingProvider adminCastingProvider;

    @Autowired
    public AdminCastingService(AdminCastingRepository adminCastingRepository, AdminCastingProvider adminCastingProvider) {
        this.adminCastingRepository = adminCastingRepository;
        this.adminCastingProvider = adminCastingProvider;
    }

    /**
     * 1:1문의 답글 (POST uri 가 겹쳤을때의 예시 용도)
     * @param adminPatchInquiriesReq
     * @return void
     * @throws BaseException
     */
//    public void updateInquiry(AdminPatchInquiriesReq adminPatchInquiriesReq) throws BaseException {
//        AdminCasting adminCasting = null;
//
//        try {
//            adminCasting = adminInquiryProvider.retrieveInquiryByInquiryIdx(adminPatchInquiriesReq.getInquiryIdx());
//            if(adminCasting == null){
//                throw new BaseException(FAILED_TO_PATCH_INQUIRIES);
//            }
//            adminCasting.setInquiryAnswer(adminPatchInquiriesReq.getInquiryAnswer());
//            adminCasting.setStatus(adminPatchInquiriesReq.getStatus());
//            adminInquiryRepository.save(adminCasting);
//            return ;
//        } catch (Exception ignored) {
//            throw new BaseException(FAILED_TO_PATCH_INQUIRIES);
//        }
//    }

}