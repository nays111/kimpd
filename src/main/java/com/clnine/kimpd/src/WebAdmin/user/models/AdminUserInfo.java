package com.clnine.kimpd.src.WebAdmin.user.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Data // from lombok
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "UserInfo") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class AdminUserInfo extends BaseEntity {
    /**
     * 유저 ID
     */
    @Id // PK를 의미하는 어노테이션
    @Column(name = "userIdx", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userIdx;

    /**
     * 유저 타입
     */
    @Column(name = "userType", nullable = false)
    private int userType;

    /**
     * 아이디
     */
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 이메일
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * 전화번호
     */
    @Column(name = "phoneNum", nullable = false)
    private String phoneNum;

    /**
     * 주소
     */
    @Column(name = "address")
    private String address;

    /**
     * 상태
     */
    @Column(name = "status", nullable = false, length = 10)
    private String status = "ACTIVE";

    public AdminUserInfo(int userIdx, int userType, String id, String email, String phoneNum, String address) {
        this.userIdx = userIdx;
        this.userType = userType;
        this.id = id;
        this.email = email;
        this.phoneNum = phoneNum;
        this.address = address;
    }
}
