package com.clnine.kimpd.src.WebAdmin.user.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Data // from lombok
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "AdminUserInfo") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class AdminUserInfo extends BaseEntity {
    /**
     * 유저 ID
     */
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 이메일
     */
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    /**
     * 비밀번호
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 닉네임
     */
    @Column(name = "nickname", nullable = false, length = 30)
    private String nickname;

    /**
     * 전화번호
     */
    @Column(name = "phoneNumber", length = 30)
    private String phoneNumber;

    /**
     * 상태
     */
    @Column(name = "status", nullable = false, length = 10)
    private String status = "ACTIVE";

    public AdminUserInfo(String email, String password, String nickname, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }
}
