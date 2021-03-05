package com.clnine.kimpd.src.WebAdmin.casting;

import com.clnine.kimpd.src.WebAdmin.casting.models.AdminCasting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminCastingRepository extends CrudRepository<AdminCasting,Integer> {
    List<AdminCasting> findAll();

}
