package com.clnine.kimpd.src.Web.faq;

import com.clnine.kimpd.src.Web.faq.models.Faq;
import com.clnine.kimpd.src.Web.notice.models.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends CrudRepository<Faq,Integer> {
    List<Faq> findAllByStatus(String status, Pageable pageable);
}
