package com.clnine.kimpd.src.WebAdmin.faq;

import com.clnine.kimpd.src.WebAdmin.faq.models.AdminFaq;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminFaqRepository extends CrudRepository<AdminFaq,Integer> {
    List<AdminFaq> findAll();

}
