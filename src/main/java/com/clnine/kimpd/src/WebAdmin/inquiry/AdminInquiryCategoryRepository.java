package com.clnine.kimpd.src.WebAdmin.inquiry;

import com.clnine.kimpd.src.WebAdmin.inquiry.models.AdminInquiryCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminInquiryCategoryRepository extends CrudRepository<AdminInquiryCategory,Integer> {
    List<AdminInquiryCategory> findAll();

}
