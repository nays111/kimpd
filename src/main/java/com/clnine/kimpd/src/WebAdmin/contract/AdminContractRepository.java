package com.clnine.kimpd.src.WebAdmin.contract;

import com.clnine.kimpd.src.WebAdmin.contract.models.AdminContract;
import com.clnine.kimpd.src.WebAdmin.report.models.AdminReportCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminContractRepository extends CrudRepository<AdminContract,Integer> {
    List<AdminContract> findAll();
    AdminContract findFirstByOrderByCreatedAtDesc();

}
