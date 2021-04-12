package com.clnine.kimpd.src.WebAdmin.contract;

import com.clnine.kimpd.src.WebAdmin.contract.models.AdminContract;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminContractRepository extends CrudRepository<AdminContract,Integer> {
    AdminContract findFirstByOrderByCreatedAtDesc();

}
