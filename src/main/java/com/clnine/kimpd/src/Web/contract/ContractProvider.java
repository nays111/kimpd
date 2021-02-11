package com.clnine.kimpd.src.Web.contract;


import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.contract.models.Contract;
import com.clnine.kimpd.src.Web.contract.models.GetContractRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_CASTING;

@Service
@RequiredArgsConstructor
public class ContractProvider {
    private final CastingRepository castingRepository;
    private final ContractRepository contractRepository;
    public GetContractRes getContractRes(int castingIdx) throws BaseException{
        Contract contract;
        Casting casting;
        try{
            casting = castingRepository.findAllByCastingIdxAndStatus(castingIdx,"ACTIVE");
        }catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_CASTING);
        }
        try{
            contract = contractRepository.findByCastingAndStatus(casting,"ACTIVE");
        }catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_CASTING);
        }
        GetContractRes getContractRes = new GetContractRes(contract.getContractIdx(), contract.getContractFileURL());
        return getContractRes;

    }
}
