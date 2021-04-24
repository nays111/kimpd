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
    List<Project> findByUserInfoAndProjectStatusAndStatus(UserInfo user, int projectStatus,String status, Pageable pageable);
    List<Project> findByUserInfoAndStatus(UserInfo user,String status);

    List<Project> findByUserInfoAndProjectStatusAndStatus(UserInfo userInfo,int projectStatus,String status);

    Project findByProjectIdxAndStatus(int projectIdx,String status);
    int countAllByUserInfoAndStatus(UserInfo userInfo,String status);
    int countAllByUserInfoAndProjectStatusAndStatus(UserInfo userinfo,int projectStatus,String status);
    //List<Project> findAllByUserInfoAndProjectStatusAndStatusAndCreatedAtBetweenOrderByProjectIdxDesc(UserInfo userInfo, int projectStatus, String status, Date now, Date end, Pageable pageable);
    //int countAllByUserInfoAndProjectStatusAndStatusAndCreatedAtBetweenOrderByProjectIdxDesc(UserInfo userInfo, int projectStatus,String status, Date now, Date end);
    List<Project> findAllByUserInfoAndProjectStatusAndStatusAndProjectStartDateGreaterThanOrderByProjectIdxDesc(UserInfo userInfo, int projectStatus, String status, String beforeDay, Pageable pageable);
    int countAllByUserInfoAndProjectStatusAndStatusAndProjectStartDateGreaterThanOrderByProjectIdxDesc(UserInfo userInfo, int projectStatus, String status, String beforeDay);
}
