package com.clnine.kimpd.src.Web.notice;

import com.clnine.kimpd.src.Web.notice.models.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface NoticeRepository extends CrudRepository<Notice,Integer> {
    List<Notice> findAllByStatus(String status, Pageable pageable);
}
