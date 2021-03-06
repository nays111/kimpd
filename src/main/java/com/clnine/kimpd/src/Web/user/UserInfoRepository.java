package com.clnine.kimpd.src.Web.user;

import com.clnine.kimpd.src.Web.expert.models.GetUsersRes;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo, Integer> {
    List<UserInfo> findByStatus(String status);
    List<UserInfo> findByEmailAndStatus(String email, String status);
    List<UserInfo> findByStatusAndNicknameIsContaining(String status, String word);
    List<UserInfo> findByIdAndStatus(String id,String status);
    UserInfo findByPhoneNumAndNameAndStatus(String email,String name,String status);
    boolean existsByIdAndStatus(String id,String status);
    boolean existsByNicknameAndStatus(String nickname,String status);
    boolean existsByPhoneNumAndStatus(String phoneNum,String status);
    boolean existsByEmailAndStatus(String email,String status);
    UserInfo findUserInfoByUserIdxAndStatus(int userIdx,String status);

    List<UserInfo> findByStatusAndUserType(String status,int userType);
    List<UserInfo> findByStatusAndUserTypeAndNicknameIsContaining(String status,int userType,String word);
    List<UserInfo> findByPhoneNumAndStatus(String phoneNum,String status);

    // 닉네임과 소개에 word가 포함된 전문가이면서 ACTIVE인 유저리스트
    List<UserInfo> findByUserTypeAndStatusAndNicknameIsContainingOrIntroduceIsContaining(int userType,String status,String word,String word2);

}