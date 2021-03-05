package com.clnine.kimpd.src.Web.user;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.secret.Secret;
import com.clnine.kimpd.src.Web.category.*;
import com.clnine.kimpd.src.Web.category.models.*;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.models.*;
import com.clnine.kimpd.utils.AES128;
import com.clnine.kimpd.utils.JwtService;
import com.clnine.kimpd.utils.MailService;
import com.clnine.kimpd.utils.SmsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;
import static com.clnine.kimpd.utils.SmsService.sendMessage;


@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserInfoProvider userInfoProvider;
    private final JwtService jwtService;
    private final MailService mailService;
    private final CertificateRepository certificateRepository;
    private final GenreCategoryRepository genreCategoryRepository;
    private final UserGenreCategoryRepository userGenreCategoryRepository;
    private final JobCategoryChildRepository jobCategoryChildRepository;
    private final JobCategoryParentRepository jobCategoryParentRepository;
    private final UserJobCategoryRepository userJobCategoryRepository;

    /**
     * 회원가입
     */
    public PostUserRes createUserInfo(PostUserReq postUserReq) throws BaseException {
        UserInfo existsUserInfo = null;
        try {
            // 1-1. 이미 존재하는 회원이 있는지 조회
            existsUserInfo = userInfoProvider.retrieveUserInfoByEmail(postUserReq.getEmail());
        } catch (BaseException exception) {
            // 1-2. 이미 존재하는 회원이 없다면 그대로 진행
            if (exception.getStatus() != NOT_FOUND_USER) {
                throw exception;
            }
        }
        // 1-3. 이미 존재하는 회원이 있다면 return DUPLICATED_USER
        if (existsUserInfo != null) {
            throw new BaseException(DUPLICATED_USER);
        }

        // 2. 유저 정보 생성
        int userType = postUserReq.getUserType();
        String id = postUserReq.getId();
        String email = postUserReq.getEmail();
        String phoneNumber = postUserReq.getPhoneNum();
        String password;
        String city = postUserReq.getCity();
        String address = postUserReq.getAddress();
        int agreeAdvertisement = postUserReq.getAgreeAdvertisement();;
        String privateBusinessName = postUserReq.getPrivateBusinessName();
        String businessNumber = postUserReq.getBusinessNumber();
        String businessImageURL = postUserReq.getBusinessImageURL();
        String corporationBusinessName = postUserReq.getCorporationBusinessName();
        String corporationBusinessNumber = postUserReq.getCorporationBusinessNumber();
        String nickname = postUserReq.getNickname();
        int agreeShowDB = postUserReq.getAgreeShowDB();
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_USER);
        }
        UserInfo userInfo = new UserInfo(userType,id,password,email,
                phoneNumber,city,address,agreeAdvertisement,privateBusinessName,businessNumber,businessImageURL,
                corporationBusinessName,corporationBusinessNumber,nickname,agreeShowDB);

        // 3. 유저 정보 저장
        try {
            userInfo = userInfoRepository.save(userInfo);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_USER);
        }
        // 어차피 리스트 길이는 같아야함
        ArrayList<Integer> jobParentCategoryIdxList = postUserReq.getJobParentCategoryIdx();
        ArrayList<Integer> jobChildCategoryIdxList = postUserReq.getJobChildCategoryIdx();
        try{
            if(jobChildCategoryIdxList!=null && jobParentCategoryIdxList!=null){
                for(int i=0;i<jobParentCategoryIdxList.size();i++){
                    JobCategoryParent jobCategoryParent = jobCategoryParentRepository.findAllByJobCategoryParentIdx(jobParentCategoryIdxList.get(i));
                    JobCategoryChild jobCategoryChild = jobCategoryChildRepository.findAllByJobCategoryChildIdx(jobChildCategoryIdxList.get(i));
                    UserJobCategory userJobCategory = new UserJobCategory(userInfo,jobCategoryParent,jobCategoryChild);
                    userJobCategoryRepository.save(userJobCategory);
                }
            }
        }catch(Exception exception){
            throw new BaseException(FAILED_TO_POST_USER_JOB_CATEGORY);
        }

        ArrayList<Integer> genreCategoryIdxList = postUserReq.getGenreCategoryIdx();
        try{
            if(genreCategoryIdxList!=null){
                for(int i=0;i<genreCategoryIdxList.size();i++){
                    GenreCategory genreCategory = genreCategoryRepository.findAllByGenreCategoryIdx(genreCategoryIdxList.get(i));
                    UserGenreCategory userGenreCategory = new UserGenreCategory(userInfo,genreCategory);
                    userGenreCategoryRepository.save(userGenreCategory);
                }
            }
        }catch (Exception exception){
            throw new BaseException(FAILED_TO_POST_USER_GENRE_CATEGORY);
        }


        // 4. JWT 생성
        String jwt = jwtService.createJwt(userInfo.getUserIdx());

        // 5. UserInfoLoginRes로 변환하여 return
        int userIdx = userInfo.getUserIdx();
        return new PostUserRes(userIdx, jwt);
    }

    /**
     * 회원 탈퇴
     */
    public void deleteUserInfo(int userIdx) throws BaseException {
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        userInfo.setStatus("INACTIVE");
        try {
            userInfoRepository.save(userInfo);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_DELETE_USER);
        }
    }

    /**
     * 이메일로 새로운 랜덤 비밀번호 전송후 유저 비밀번호 업데이트
     */
    public GetNewPasswordRes patchUserPassword(String email) throws BaseException{
        UserInfo existsUserInfo = null;
        try{
            existsUserInfo = userInfoProvider.retrieveUserInfoByEmail(email);
        }catch(BaseException exception){
            throw new BaseException(NOT_FOUND_USER);
        }
        String newPassword = mailService.sendPwFindMail(email);
        String password=newPassword;
        try {
            newPassword = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(newPassword);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_USER);
        }
        existsUserInfo.setPassword(newPassword);
        userInfoRepository.save(existsUserInfo);
        return new GetNewPasswordRes(password);
    }

    /**
     * 휴대폰 인증번호를 데이터베이스에 저장
     */
    public void PostSecureCode(int rand,String phoneNum) throws BaseException{
        String message="김피디입니다.화면의 인증번호란에 ["+rand+"]를 입력해주세요.";
        try{
            sendMessage(message,phoneNum);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_SEND_MESSAGE);
        }
        Certification certification=new Certification(phoneNum,rand);
        try{
            certificateRepository.save(certification);
        }catch(Exception ignored){
            throw new BaseException(FAILED_TO_POST_SECURE_CODE);
        }
    }

    /**
     * 전문가 전환
     */
    public void changeUserTypeToExpert(int userIdx, PatchUserTypeReq patchUserTypeReq)throws BaseException{
        UserInfo userInfo;
        try {
            userInfo = userInfoRepository.findById(userIdx).orElse(null);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        List<Integer> jobParentCategoryIdxList = patchUserTypeReq.getJobParentCategoryIdx();
        List<Integer> jobChildCategoryIdxList = patchUserTypeReq.getJobChildCategoryIdx();
        try{
            if(jobChildCategoryIdxList!=null && jobParentCategoryIdxList!=null){
                for(int i=0;i<jobParentCategoryIdxList.size();i++){
                    JobCategoryParent jobCategoryParent = jobCategoryParentRepository.findAllByJobCategoryParentIdx(jobParentCategoryIdxList.get(i));
                    JobCategoryChild jobCategoryChild = jobCategoryChildRepository.findAllByJobCategoryChildIdx(jobChildCategoryIdxList.get(i));
                    UserJobCategory userJobCategory = new UserJobCategory(userInfo,jobCategoryParent,jobCategoryChild);
                    userJobCategoryRepository.save(userJobCategory);
                }
            }
        }catch(Exception exception){
            throw new BaseException(FAILED_TO_POST_USER_JOB_CATEGORY);
        }
        List<Integer> genreCategoryIdxList = patchUserTypeReq.getGenreCategoryIdx();
        try{
            if(genreCategoryIdxList!=null){
                for(int i=0;i<genreCategoryIdxList.size();i++){
                    GenreCategory genreCategory = genreCategoryRepository.findAllByGenreCategoryIdx(genreCategoryIdxList.get(i));
                    UserGenreCategory userGenreCategory = new UserGenreCategory(userInfo,genreCategory);
                    userGenreCategoryRepository.save(userGenreCategory);
                }
            }
        }catch (Exception exception){
            throw new BaseException(FAILED_TO_POST_USER_GENRE_CATEGORY);
        }
        userInfo.setAgreeShowDB(patchUserTypeReq.getAgreeShowDB());
        if(userInfo.getUserType()==1){
            userInfo.setUserType(4);
        }else if(userInfo.getUserType()==2){
            userInfo.setUserType(5);
        }else if(userInfo.getUserType()==3){
            userInfo.setUserType(6);
        }
        userInfoRepository.save(userInfo);
    }

    /**
     * 비밀번호 수정
     */
    public void patchMyPassword(int userIdx, PatchUserPasswordReq patchUserPasswordReq) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(userInfo.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_LOGIN);
        }
        if (!patchUserPasswordReq.getCurrentPassword().equals(password)) {
            throw new BaseException(WRONG_CURRENT_PASSWORD);
        }
        String newPassword = patchUserPasswordReq.getNewPassword();
        try {
            newPassword = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(newPassword);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_USER);
        }
        try{
            userInfo.setPassword(newPassword);
            userInfoRepository.save(userInfo);
        }catch (Exception ignored) {
            throw new BaseException(FAILED_SAVE_NEW_PASSWORD);
        }
    }

    /**
     * 회원 정보 수정
     */
    public void patchMyUserInfo(int userIdx,PatchMyUserInfoReq patchMyUserInfoReq) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);

        String profileImageURL = patchMyUserInfoReq.getProfileImageURL();
        String phoneNum = patchMyUserInfoReq.getPhoneNum();
        String email = patchMyUserInfoReq.getEmail();
        String privateBusinessName = patchMyUserInfoReq.getPrivateBusinessName();
        String businessNumber = patchMyUserInfoReq.getBusinessNumber();
        String businessImageURL = patchMyUserInfoReq.getBusinessImageURL();
        String corpBusinessName = patchMyUserInfoReq.getCorpBusinessName();
        String corpBusinessNumber = patchMyUserInfoReq.getCorpBusinessNumber();

        userInfo.setProfileImageURL(profileImageURL);
        userInfo.setPhoneNum(phoneNum);
        userInfo.setEmail(email);
        userInfo.setPrivateBusinessName(privateBusinessName);
        userInfo.setBusinessNumber(businessNumber);
        userInfo.setBusinessImageURL(businessImageURL);
        userInfo.setCorporationBusinessName(corpBusinessName);
        userInfo.setCorporationBusinessNumber(corpBusinessNumber);
        userInfoRepository.save(userInfo);
    }
}