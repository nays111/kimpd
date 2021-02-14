package com.clnine.kimpd.src.Web.inquiry;

import com.clnine.kimpd.src.Web.inquiry.models.Inquiry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends CrudRepository<Inquiry,Integer> {
}
