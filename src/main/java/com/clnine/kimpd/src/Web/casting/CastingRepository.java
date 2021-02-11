package com.clnine.kimpd.src.Web.casting;

import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface CastingRepository extends CrudRepository<Casting,Integer> {
    List<Casting> findByUserInfoAndExpertAndProjectAndStatus(UserInfo user, UserInfo expert, Project project,String status);
    int countAllByUserInfoAndAndCastingStatusAndStatus(UserInfo user,int castingStatus,String status);

    //전체 기간 전체 조회
    List<Casting> findAllByUserInfoAndStatusOrderByCastingIdxDesc(UserInfo userInfo, String status, Pageable pageable);
    //전체 기간 섭외 상태에 따른 조회
    List<Casting> findAllByUserInfoAndCastingStatusAndStatusOrderByCastingIdxDesc(UserInfo userInfo,int castingStatus,String status,Pageable pageable);
    //기간 내 전체 조회
    List<Casting> findAllByUserInfoAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, String status, Date now, Date end, Pageable pageable);
    //기간 내 섭외 상태에 따른 조회
    List<Casting> findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, int castingStatus, String status, Date now, Date end, Pageable pageable);

    //기간 조회
//    List<Casting> findAllByUserInfoAndStatusAndCreatedAt(UserInfo userInfo);
//    List<Casting> findAllByUserInfoAndCastingStatusAndStatus(UserInfo userInfo,int castingStatus,String status);


    //castingIdx로 섭외 상세 내역 조회
    Casting findAllByCastingIdxAndStatus(int castingIdx,String status);

}
