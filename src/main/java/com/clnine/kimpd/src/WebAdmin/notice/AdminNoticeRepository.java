package com.clnine.kimpd.src.WebAdmin.notice;

import com.clnine.kimpd.src.WebAdmin.notice.models.AdminNotice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminNoticeRepository extends CrudRepository<AdminNotice,Integer> {
    List<AdminNotice> findAll();

}
