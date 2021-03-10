package com.clnine.kimpd.src.Web.user;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.secret.Secret;
import com.clnine.kimpd.src.Web.category.*;
import com.clnine.kimpd.src.Web.category.models.*;
import com.clnine.kimpd.src.Web.project.ProjectRepository;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.models.*;
import com.clnine.kimpd.utils.AES128;
import com.clnine.kimpd.utils.JwtService;
import com.clnine.kimpd.utils.MailService;
import com.clnine.kimpd.utils.SmsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ProjectRepository projectRepository;

    @Transactional
    public PostUserRes createUserInfo(PostUserReq postUserReq) throws BaseException {
        int userType = postUserReq.getUserType();
        String id = postUserReq.getId();
        String email = postUserReq.getEmail();
        String phoneNumber = postUserReq.getPhoneNum();
        String password;
        String city = postUserReq.getCity();
        String name = postUserReq.getName();
        String address = postUserReq.getAddress();
        Integer agreeAdvertisement = postUserReq.getAgreeAdvertisement();;
        String privateBusinessName = postUserReq.getPrivateBusinessName();
        String businessNumber = postUserReq.getBusinessNumber();
        String businessImageURL = postUserReq.getBusinessImageURL();
        String corporationBusinessName = postUserReq.getCorporationBusinessName();
        String corporationBusinessNumber = postUserReq.getCorporationBusinessNumber();
        String nickname = postUserReq.getNickname();
        Integer agreeShowDB = postUserReq.getAgreeShowDB();
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_USER);
        }
        UserInfo userInfo = new UserInfo(userType,id,password,email,name,
                phoneNumber,city,address,agreeAdvertisement,privateBusinessName,businessNumber,businessImageURL,
                corporationBusinessName,corporationBusinessNumber,nickname,agreeShowDB);
        try {
            userInfo = userInfoRepository.save(userInfo);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_USER);
        }
        ArrayList<ArrayList<Integer>>jobCategoryIdxList = postUserReq.getJobCategoryIdx();
        try{
            if(jobCategoryIdxList!=null){
                for(int i=0;i<jobCategoryIdxList.size();i++){
                    JobCategoryParent jobCategoryParent = jobCategoryParentRepository.findAllByJobCategoryParentIdx(jobCategoryIdxList.get(i).get(0));
                    JobCategoryChild jobCategoryChild = jobCategoryChildRepository.findAllByJobCategoryChildIdx(jobCategoryIdxList.get(i).get(1));
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
//        Project project = new Project(userInfo,"기본프로젝트","기본프로젝트");
//        try{
//            projectRepository.save(project);
//        }catch (Exception exception){
//            throw new BaseException(FAILED_TO_POST_PROJECT);
//        }
        String jwt = jwtService.createJwt(userInfo.getUserIdx());
        int userIdx = userInfo.getUserIdx();
        return new PostUserRes(userIdx, jwt);
    }

    public void deleteUserInfo(int userIdx) throws BaseException {
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        userInfo.setStatus("INACTIVE");
        try {
            userInfoRepository.save(userInfo);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_DELETE_USER);
        }
    }

    @Transactional
    public void patchUserPassword(String email) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByEmail(email);
        String password;
        try {
            String newPassword = mailService.sendPwFindMail(email);
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(newPassword);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_SEND_EMAIL);
        }
        userInfo.setPassword(password);
        userInfoRepository.save(userInfo);
    }

    @Transactional
    public void postSecureCode(int rand,String phoneNum) throws BaseException{
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
    @Transactional
    public void changeUserTypeToExpert(int userIdx, PatchUserTypeReq patchUserTypeReq)throws BaseException{

        UserInfo userInfo= userInfoProvider.retrieveUserInfoByUserIdx(userIdx);

        if(userInfo.getUserType()==4 || userInfo.getUserType()==5 || userInfo.getUserType()==6){
            throw new BaseException(ALREADY_EXPERT);
        }

        ArrayList<ArrayList<Integer>>jobCategoryIdxList = patchUserTypeReq.getJobCategoryIdx();

        try{
            if(jobCategoryIdxList!=null){
                for(int i=0;i<jobCategoryIdxList.size();i++){
                    JobCategoryParent jobCategoryParent = jobCategoryParentRepository.findAllByJobCategoryParentIdx(jobCategoryIdxList.get(i).get(0));
                    JobCategoryChild jobCategoryChild = jobCategoryChildRepository.findAllByJobCategoryChildIdx(jobCategoryIdxList.get(i).get(1));
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
        try{
            userInfoRepository.save(userInfo);
        }catch (Exception ignored) {
            throw new BaseException(FAILED_TO_CHANGE_TO_EXPERT);
        }
    }

    @Transactional
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

        if(password.equals(newPassword)){
            throw new BaseException(SAME_PASSWORD_BEFORE_CHANGE);
        }
        try {
            newPassword = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(newPassword);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_USER);
        }

        userInfo.setPassword(newPassword);
        try{
            userInfoRepository.save(userInfo);
        }catch (Exception ignored) {
            throw new BaseException(FAILED_SAVE_NEW_PASSWORD);
        }
    }


    @Transactional
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
        try{
            userInfoRepository.save(userInfo);
        }catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_USER);
        }
    }
}