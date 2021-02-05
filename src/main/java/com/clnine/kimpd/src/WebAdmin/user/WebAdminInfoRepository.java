package com.clnine.kimpd.src.WebAdmin.user;

import com.clnine.kimpd.src.WebAdmin.user.models.WebAdmin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // => JPA => Hibernate => ORM => Database 객체지향으로 접근하게 해주는 도구이다
public interface WebAdminInfoRepository extends CrudRepository<WebAdmin, String> {
//    List<WebAdmin> findByStatus(String status);
//    List<WebAdmin> findByEmailAndStatus(String email, String status);
//    List<WebAdmin> findByStatusAndNicknameIsContaining(String status, String word);
}