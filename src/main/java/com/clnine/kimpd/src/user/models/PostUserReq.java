package com.clnine.kimpd.src.user.models;

import com.sun.istack.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostUserReq {
    //필수 컬럼
    private int userType;
    private String id;
    private String password;
    private String confirmPassword;
    private String email;
    private int agreeAdvertisement;
    private String phoneNum;
    private String address;
    //todo 인증번호 (certificateCode 회원가입할 떄 처리할지?)
    //private int certifiacteCode;
    @Nullable
    private String privateBusinessName;//개인사업자명
    @Nullable
    private String businessNumber; //사업자 등록번호
    @Nullable
    private String businessImageURL; //사업자 등록증
    @Nullable
    private String corporationBusinessName; //법인 사업자명
    @Nullable
    private String corporationBusinessNumber;//법인 등록번호
    @Nullable
    private String nickname;
    @Nullable
    private ArrayList<Integer> genreCategoryIdx; //장르 카테고리 인덱스
    @Nullable
    private ArrayList<Integer> jobParentCategoryIdx;
    @Nullable
    private ArrayList<Integer> jobChildCategoryIdx;
}
