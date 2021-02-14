package com.clnine.kimpd.src.Web.inquiry;

import com.clnine.kimpd.src.Web.inquiry.models.Inquiry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends CrudRepository<Inquiry,Integer> {

    List<Inquiry> findAllByStatus(String status, Pageable pageable);
}
