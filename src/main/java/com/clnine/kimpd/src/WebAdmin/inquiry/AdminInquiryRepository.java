package com.clnine.kimpd.src.WebAdmin.inquiry;

import com.clnine.kimpd.src.WebAdmin.inquiry.models.AdminInquiry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminInquiryRepository extends CrudRepository<AdminInquiry,Integer> {
    List<AdminInquiry> findAll();

}
