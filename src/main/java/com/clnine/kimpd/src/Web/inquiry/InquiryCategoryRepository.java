package com.clnine.kimpd.src.Web.inquiry;

import com.clnine.kimpd.src.Web.inquiry.models.InquiryCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryCategoryRepository extends CrudRepository<InquiryCategory,Integer> {
    List<InquiryCategory> findAll();
    InquiryCategory findAllByInquiryCategoryIdx(int inquiryCategoryIdx);
}
