package com.clnine.kimpd.src.Web.project;

import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Integer> {
    List<Project> findByUserInfoAndStatus(UserInfo user, String status, Pageable pageable);

    List<Project> findByUserInfoAndStatus(UserInfo user,String status);
    Project findByProjectIdxAndUserInfoAndStatus(int projectIdx,UserInfo userInfo,String status);
    Project findByProjectIdxAndStatus(int projectIdx,String status);

    int countAllByUserInfoAndStatus(UserInfo userinfo,String status);

    List<Project> findAllByUserInfoAndStatusAndCreatedAtBetweenOrderByProjectIdxDesc(UserInfo userInfo, String status, Date now, Date end, Pageable pageable);
    int countAllByUserInfoAndStatusAndCreatedAtBetweenOrderByProjectIdxDesc(UserInfo userInfo, String status, Date now, Date end);

}
