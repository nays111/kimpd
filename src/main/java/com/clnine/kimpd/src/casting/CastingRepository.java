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
    int countAllByUserInfoAndAndCastingStatusAndStatus(UserInfo user,int castingStatus,String status);

    //전체 기간 조회
    List<Casting> findAllByUserInfoAndStatus(UserInfo userInfo,String status);
    List<Casting> findAllByUserInfoAndCastingStatusAndStatus(UserInfo userInfo,int castingStatus,String status);


    //기간 조회
//    List<Casting> findAllByUserInfoAndStatusAndCreatedAt(UserInfo userInfo);
//    List<Casting> findAllByUserInfoAndCastingStatusAndStatus(UserInfo userInfo,int castingStatus,String status);

}
