package com.clnine.kimpd.src.user.models;

import com.clnine.kimpd.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Data // from lombok
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "UserInfo") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class UserInfo extends BaseEntity {
    /**
     * 유저 인덱스
     */
    @Id // PK를 의미하는 어노테이션
    @Column(name = "userIdx", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userIdx;

    /**
     * 유저 타입
     */
    @Column(name="userType",nullable = false)
    private int userType;

    /**
     * 유저 ID
     */
    private String id;

    /**
     * 유저 비밀번호
     */
    private String password;

    /**
     * 유저 이메일
     */
    private String email;

    /**
     * 유저 휴대폰 번호
     */
    private String phoneNum;

    /**
     * 유저 주소
     */
    private String address;

    /**
     * 광고 수신 동의 여부
     */
    private int agreeAdvertisement;

    /**
     * 개인 사업자명
     */
    private String businessNumber;

    /**
     * 사업자 등록 이미지
     */
    private String businessImageURL;

    /**
     * 법인 사업자명
     */
    private String corporationBusinessName;

    /**
     * 법인 사업자 번호
     */
    private String corporationBusinessNumber;

    /**
     * 닉네임
     */
    private String nickname;

    /**
     * 프로필 이미지 URL
     */
    private String profileImageURL;

    /**
     * 상태값 (ACTIVE)
     */
    private String status;




//    /**
//     * 유저 ID
//     */
//    @Id // PK를 의미하는 어노테이션
//    @Column(name = "id", nullable = false, updatable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    /**
//     * 이메일
//     */
//    @Column(name = "email", nullable = false, length = 100)
//    private String email;
//
//    /**
//     * 비밀번호
//     */
//    @Column(name = "password", nullable = false)
//    private String password;
//
//    /**
//     * 닉네임
//     */
//    @Column(name = "nickname", nullable = false, length = 30)
//    private String nickname;
//
//    /**
//     * 전화번호
//     */
//    @Column(name = "phoneNumber", length = 30)
//    private String phoneNumber;
//
//    /**
//     * 상태
//     */
//    @Column(name = "status", nullable = false, length = 10)
//    private String status = "ACTIVE";
//
//    public UserInfo(String email, String password, String nickname, String phoneNumber) {
//        this.email = email;
//        this.password = password;
//        this.nickname = nickname;
//        this.phoneNumber = phoneNumber;
//    }
}
