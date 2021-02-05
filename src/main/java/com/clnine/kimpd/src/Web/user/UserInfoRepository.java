package com.clnine.kimpd.src.Web.user;

import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // => JPA => Hibernate => ORM => Database 객체지향으로 접근하게 해주는 도구이다
public interface UserInfoRepository extends CrudRepository<UserInfo, Integer> {
    List<UserInfo> findByStatus(String status);
    List<UserInfo> findByEmailAndStatus(String email, String status);
    List<UserInfo> findByStatusAndNicknameIsContaining(String status, String word);
    List<UserInfo> findByIdAndStatus(String id,String status);
    boolean existsByIdAndStatus(String id,String status);
    boolean existsByNicknameAndStatus(String nickname,String status);
    UserInfo findUserInfoByUserIdxAndStatus(int userIdx,String status);

    List<UserInfo> findByStatusAndUserType(String status,int userType);
    List<UserInfo> findByStatusAndUserTypeAndNicknameIsContaining(String status,int userType,String word);
    List<UserInfo> findByPhoneNumAndStatus(String phoneNum,String status);

    // 닉네임과 소개에 word가 포함된 전문가이면서 ACTIVE인 유저리스트
    List<UserInfo> findByUserTypeAndStatusAndNicknameIsContainingOrIntroduceIsContaining(int userType,String status,String word,String word2);




}