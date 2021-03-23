package com.clnine.kimpd.src.WebAdmin.contract;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.WebAdmin.contract.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.clnine.kimpd.config.BaseResponseStatus.*;


@Service
public class AdminContractService {
    private final AdminContractRepository adminContractRepository;

    @Autowired
    public AdminContractService(AdminContractRepository adminContractRepository) {
        this.adminContractRepository = adminContractRepository;
    }

    /**
     * 계약서 수정 API
     *
     * @param adminPatchContractReq
     * @return AdminPostNoticesReq
     * @throws BaseException
     */
    public void updateContract(AdminPatchContractReq adminPatchContractReq) throws BaseException {

        String contractContent = adminPatchContractReq.getContractContent();
        AdminContract adminContract = new AdminContract(contractContent);

        try {
            adminContract = adminContractRepository.save(adminContract);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_PATCH_CONTRACT);
        }
        return;
    }
}
