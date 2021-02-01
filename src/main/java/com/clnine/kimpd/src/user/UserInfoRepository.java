package com.clnine.kimpd.src.user;

import com.clnine.kimpd.src.user.models.UserInfo;
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



}