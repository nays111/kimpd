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
    List<Casting> findByUserInfoAndExpertAndProjectAndCastingStatusNotAndStatus(UserInfo user, UserInfo expert, Project project,int castingStatus,String status);
    //섭외 신청을 보낸 경우
    int countAllByUserInfoAndCastingStatusAndStatus(UserInfo user,int castingStatus,String status);
    //섭외 요청을 받은 경우
    int countAllByExpertAndCastingStatusAndStatus(UserInfo user,int castingStatus,String status);


    // 섭외 요청을 보낸 경우
    //전체 기간 전체 조회
    List<Casting> findAllByUserInfoAndStatusAndCastingStatusNotOrderByCastingIdxDesc(UserInfo userInfo, String status,int castingStatus ,Pageable pageable);
    //전체 기간 섭외 상태에 따른 조회
    List<Casting> findAllByUserInfoAndCastingStatusAndStatusOrderByCastingIdxDesc(UserInfo userInfo,int castingStatus,String status,Pageable pageable);
    //기간 내 전체 조회
    List<Casting> findAllByUserInfoAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, String status, int castingStatus,Date now, Date end, Pageable pageable);
    //기간 내 섭외 상태에 따른 조회
    List<Casting> findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, int castingStatus, String status, Date now, Date end, Pageable pageable);

    //섭외 요청을 받은 경우
    //전체 기간 전체 조회 (수정)
    List<Casting> findAllByExpertAndStatusAndCastingStatusNotOrderByCastingIdxDesc(UserInfo userInfo, String status, int castingStatus,Pageable pageable);
    //전체 기간 섭외 상태에 따른 조회
    List<Casting> findAllByExpertAndCastingStatusAndStatusOrderByCastingIdxDesc(UserInfo userInfo,int castingStatus,String status,Pageable pageable);
    //기간 내 전체 조회 (수정)
    List<Casting> findAllByExpertAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, String status, int castingStatus,Date now, Date end, Pageable pageable);
    //기간 내 섭외 상태에 따른 조회
    List<Casting> findAllByExpertAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, int castingStatus, String status, Date now, Date end, Pageable pageable);


    //castingIdx로 섭외 상세 내역 조회
    Casting findAllByCastingIdxAndStatus(int castingIdx,String status);




    List<Casting> findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNullOrderByCastingIdxDesc(UserInfo userInfo,int castingStatus,String status,Pageable pageable);
    List<Casting> findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNotNullOrderByCastingIdxDesc(UserInfo userInfo,int castingStatus,String status,Pageable pageable);


    List<Casting> findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNullAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, int castingStatus, String status, Date now, Date end, Pageable pageable);
    List<Casting> findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNotNullAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, int castingStatus, String status, Date now, Date end, Pageable pageable);
}
