package com.clnine.kimpd.src.casting;

import com.clnine.kimpd.src.casting.models.Casting;
import com.clnine.kimpd.src.project.models.Project;
import com.clnine.kimpd.src.user.models.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CastingRepository extends CrudRepository<Casting,Integer> {
    List<Casting> findByUserInfoAndExpertAndProjectAndStatus(UserInfo user, UserInfo expert, Project project,String status);
}
