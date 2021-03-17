package com.clnine.kimpd.src.WebAdmin.contract;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.contract.models.AdminGetContractRes;
import com.clnine.kimpd.src.WebAdmin.contract.models.AdminContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_NOTICES;

@Service
public class AdminContractProvider {

    @Autowired
    private final AdminContractRepository adminContractRepository;

    AdminContractProvider(AdminContractRepository adminContractRepository){
        this.adminContractRepository = adminContractRepository;

    }

    /**
     * 계약서 상세 조회
     * @return AdminGetContractRes
     * @throws BaseException
     */
    public AdminGetContractRes retrieveContractInfo() throws BaseException {
        // 1. DB에서 noticeIdx AdminNotice 조회
        AdminContract adminContract = adminContractRepository.findFirstByOrderByCreatedAtDesc();
        if(adminContract == null){
            throw new BaseException(FAILED_TO_GET_NOTICES);
        }
        // 2. AdminGetNoticesRes 변환하여 return
        String contractContent = adminContract.getContractContent();

        return new AdminGetContractRes(contractContent);
    }

    /**
     * 공지사항 조회
     * @param noticeIdx
     * @return AdminNotice
     * @throws BaseException
     */
//    public AdminContract retrieveNoticeByNoticeIdx(int noticeIdx) throws BaseException {
//        // 1. DB에서 AdminNotice 조회
//        AdminContract adminContract;
//        try {
//            adminContract = adminContractRepository.findById(noticeIdx).orElse(null);
//        } catch (Exception ignored) {
//            throw new BaseException(FAILED_TO_GET_NOTICES);
//        }
//
//        // 2. AdminNotice return
//        return adminContract;
//    }

}
