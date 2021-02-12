package com.clnine.kimpd.src.Web.report;

import com.clnine.kimpd.src.Web.report.models.ReportCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportCategoryRepository extends CrudRepository<ReportCategory,Integer> {
    List<ReportCategory> findAll();
}
