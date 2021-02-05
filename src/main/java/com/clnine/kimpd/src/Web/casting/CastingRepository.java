package com.clnine.kimpd.src.Web.casting;

import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CastingRepository extends CrudRepository<Casting,Integer> {
    List<Casting> findByUserInfoAndExpertAndProjectAndStatus(UserInfo user, UserInfo expert, Project project,String status);
    int countAllByUserInfoAndAndCastingStatusAndStatus(UserInfo user,int castingStatus,String status);

    //전체 기간 조회
    List<Casting> findAllByUserInfoAndStatus(UserInfo userInfo, String status, Pageable pageable);
    List<Casting> findAllByUserInfoAndCastingStatusAndStatus(UserInfo userInfo,int castingStatus,String status,Pageable pageable);
    List<Casting> findAllByUserInfoAndStatusAndCreatedAtBetween(UserInfo userInfo, String status, Timestamp start, Timestamp end, Pageable pageable);
    List<Casting> findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetween(UserInfo userInfo, int castingStatus, String status, Timestamp start, Timestamp end, Pageable pageable);

    //기간 조회
//    List<Casting> findAllByUserInfoAndStatusAndCreatedAt(UserInfo userInfo);
//    List<Casting> findAllByUserInfoAndCastingStatusAndStatus(UserInfo userInfo,int castingStatus,String status);

}
