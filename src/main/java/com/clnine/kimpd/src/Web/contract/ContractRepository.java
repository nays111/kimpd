package com.clnine.kimpd.src.Web.contract;

import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.contract.models.Contract;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends CrudRepository<Contract,Integer> {
    Contract findByCastingAndStatus(Casting casting,String status);
    Contract findByContractIdx(int contractIdx);


}
