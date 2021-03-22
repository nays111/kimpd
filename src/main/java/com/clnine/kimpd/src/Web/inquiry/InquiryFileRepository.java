package com.clnine.kimpd.src.Web.inquiry;

import com.clnine.kimpd.src.Web.inquiry.models.Inquiry;
import com.clnine.kimpd.src.Web.inquiry.models.InquiryCategory;
import com.clnine.kimpd.src.Web.inquiry.models.InquiryFile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InquiryFileRepository extends CrudRepository<InquiryFile,Integer> {
    List<InquiryFile> findAllByInquiry(Inquiry inquiry);
}
