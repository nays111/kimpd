package com.clnine.kimpd.src.WebAdmin.report;

import com.clnine.kimpd.src.Web.user.models.UserInfo;
import com.clnine.kimpd.src.WebAdmin.inquiry.models.AdminInquiry;
import com.clnine.kimpd.src.WebAdmin.report.models.AdminReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminReportRepository extends CrudRepository<AdminReport,Integer> {
    List<AdminReport> findAll();
}
