package com.clnine.kimpd.src.WebAdmin.inquiry;

import com.clnine.kimpd.src.Web.user.models.UserInfo;
import com.clnine.kimpd.src.WebAdmin.inquiry.models.AdminInquiry;
import com.clnine.kimpd.src.WebAdmin.inquiry.models.AdminInquiryFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface AdminInquiryFileRepository extends CrudRepository<AdminInquiryFile,Integer> {
    List<AdminInquiryFile> findAll();
    ArrayList<AdminInquiryFile> findAllByAdminInquiry(AdminInquiry adminInquiry);


}
