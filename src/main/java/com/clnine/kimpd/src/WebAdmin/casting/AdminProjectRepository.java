package com.clnine.kimpd.src.WebAdmin.casting;

import com.clnine.kimpd.src.WebAdmin.casting.models.AdminProject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminProjectRepository extends CrudRepository<AdminProject, Integer> {
    List<AdminProject> findAll();

}
