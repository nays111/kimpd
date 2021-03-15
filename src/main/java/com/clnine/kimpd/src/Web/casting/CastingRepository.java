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
    int countAllByUserInfoAndStatusAndCastingStatusNotOrderByCastingIdxDesc(UserInfo userInfo, String status,int castingStatus);
    //전체 기간 섭외 상태에 따른 조회
    List<Casting> findAllByUserInfoAndCastingStatusAndStatusOrderByCastingIdxDesc(UserInfo userInfo,int castingStatus,String status,Pageable pageable);
    int countAllByUserInfoAndCastingStatusAndStatusOrderByCastingIdxDesc(UserInfo userInfo,int castingStatus,String status);

    //기간 내 전체 조회
    List<Casting> findAllByUserInfoAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, String status, int castingStatus,Date now, Date end, Pageable pageable);
    int countAllByUserInfoAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, String status, int castingStatus,Date now, Date end);
    //기간 내 섭외 상태에 따른 조회
    List<Casting> findAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, int castingStatus, String status, Date now, Date end, Pageable pageable);
    int countAllByUserInfoAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, int castingStatus, String status, Date now, Date end);


    //섭외 요청을 받은 경우
    //전체 기간 전체 조회 (수정)
    List<Casting> findAllByExpertAndStatusAndCastingStatusNotOrderByCastingIdxDesc(UserInfo userInfo, String status, int castingStatus,Pageable pageable);
    int countAllByExpertAndStatusAndCastingStatusNotOrderByCastingIdxDesc(UserInfo userInfo, String status, int castingStatus);
    //전체 기간 섭외 상태에 따른 조회
    List<Casting> findAllByExpertAndCastingStatusAndStatusOrderByCastingIdxDesc(UserInfo userInfo,int castingStatus,String status,Pageable pageable);
    int countAllByExpertAndCastingStatusAndStatusOrderByCastingIdxDesc(UserInfo userInfo,int castingStatus,String status);

    //기간 내 전체 조회 (수정)
    List<Casting> findAllByExpertAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, String status, int castingStatus,Date now, Date end, Pageable pageable);
    int countAllByExpertAndStatusAndCastingStatusNotAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, String status, int castingStatus,Date now, Date end);
    //기간 내 섭외 상태에 따른 조회
    List<Casting> findAllByExpertAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, int castingStatus, String status, Date now, Date end, Pageable pageable);
    int countAllByExpertAndCastingStatusAndStatusAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, int castingStatus, String status, Date now, Date end);

    //castingIdx로 섭외 상세 내역 조회
    Casting findAllByCastingIdxAndStatus(int castingIdx,String status);

    Casting findByCastingIdxAndUserInfoAndStatus(int castingIdx,UserInfo userInfo,String status);




    List<Casting> findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNullOrderByCastingIdxDesc(UserInfo userInfo,int castingStatus,String status,Pageable pageable);
    int countAllByUserInfoAndCastingStatusAndStatusAndReviewIsNullOrderByCastingIdxDesc(UserInfo userInfo,int castingStatus,String status);


    List<Casting> findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNotNullOrderByCastingIdxDesc(UserInfo userInfo,int castingStatus,String status,Pageable pageable);
    int countAllByUserInfoAndCastingStatusAndStatusAndReviewIsNotNullOrderByCastingIdxDesc(UserInfo userInfo,int castingStatus,String status);

    List<Casting> findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNullAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, int castingStatus, String status, Date now, Date end, Pageable pageable);
    int countAllByUserInfoAndCastingStatusAndStatusAndReviewIsNullAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, int castingStatus, String status, Date now, Date end);

    List<Casting> findAllByUserInfoAndCastingStatusAndStatusAndReviewIsNotNullAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, int castingStatus, String status, Date now, Date end, Pageable pageable);
    int countAllByUserInfoAndCastingStatusAndStatusAndReviewIsNotNullAndCreatedAtBetweenOrderByCastingIdxDesc(UserInfo userInfo, int castingStatus, String status, Date now, Date end);

    //전문가 캐스팅 정보 달력 조회
    List<Casting> findAllByExpertAndStatusAndCastingStartDateLessThanEqualAndCastingEndDateGreaterThanEqual(UserInfo userInfo,String status,String startDate,String endDate);


    //전문가 캐스팅 정보 달력으로 상세 조회
    List<Casting> findAllByExpertAndStatusAndCastingStartDateGreaterThanEqualAndCastingEndDateLessThanEqual(UserInfo userInfo,String status,String searchDate,String searchDate2);
}
