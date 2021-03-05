package com.clnine.kimpd.src.WebAdmin.user;

import com.clnine.kimpd.src.Web.user.models.UserInfo;
import com.clnine.kimpd.src.WebAdmin.user.models.AdminUserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // => JPA => Hibernate => ORM => Database 객체지향으로 접근하게 해주는 도구이다
public interface AdminUserInfoRepository extends CrudRepository<AdminUserInfo, Integer> {
    List<AdminUserInfo> findAll();
    List<AdminUserInfo> findByEmailAndStatus(String email, String status);
    boolean existsByIdAndStatus(String id, String status);
    boolean existsByEmailAndStatus(String email, String status);
    boolean existsByPhoneNumAndStatus(String phoneNum, String status);
    boolean existsByNicknameAndStatus(String nickname, String status);
//    List<AdminUserInfo> findById(int userIdx);
//    List<AdminUserInfo> findByStatus(String status);
//    List<AdminUserInfo> findByEmailAndStatus(String email, String status);
//    List<AdminUserInfo> findByStatusAndNicknameIsContaining(String status, String word);
}