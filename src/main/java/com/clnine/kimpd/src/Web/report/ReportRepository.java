package com.clnine.kimpd.src.Web.report;

import com.clnine.kimpd.src.Web.report.models.Report;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends CrudRepository<Report,Integer> {
    int countReportByReportedUserInfoAndStatus(UserInfo userInfo,String status);
}
