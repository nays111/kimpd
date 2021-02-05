package com.clnine.kimpd.src.Web.user;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.secret.Secret;
import com.clnine.kimpd.src.Web.category.*;
import com.clnine.kimpd.src.Web.category.models.*;
import com.clnine.kimpd.src.Web.user.models.*;
import com.clnine.kimpd.utils.AES128;
import com.clnine.kimpd.utils.JwtService;
import com.clnine.kimpd.utils.MailService;
import com.clnine.kimpd.utils.SmsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.clnine.kimpd.config.BaseResponseStatus.*;
import static com.clnine.kimpd.utils.SmsService.sendMessage;


@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserInfoProvider userInfoProvider;
    private final JwtService jwtService;
    private final MailService mailService;
    private final SmsService smsService;
    private final CertificateRepository certificateRepository;
    private final GenreCategoryRepository genreCategoryRepository;
    private final UserGenreCategoryRepository userGenreCategoryRepository;
    private final JobCategoryChildRepository jobCategoryChildRepository;
    private final JobCategoryParentRepository jobCategoryParentRepository;
    private final UserJobCategoryRepository userJobCategoryRepository;

    /**
     * 회원가입
     * @param postUserReq
     * @return PostUserRes
     * @throws BaseException
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
        String address = postUserReq.getAddress();
        int agreeAdvertisement = postUserReq.getAgreeAdvertisement();;
        String privateBusinessName = postUserReq.getPrivateBusinessName();
        String businessNumber = postUserReq.getBusinessNumber();
        String businessImageURL = postUserReq.getBusinessImageURL();
        String corporationBusinessName = postUserReq.getCorporationBusinessName();
        String corporationBusinessNumber = postUserReq.getCorporationBusinessNumber();
        String nickname = postUserReq.getNickname();



        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_POST_USER);
        }


        /**
         * 1,2,3,4, 이런식으로 카테고리 인덱스를 입력받음
         * genreCategory에서 해당되는 genreCategory 객체 생성
         * userGenreCategory에 userInfo와 genreCategory 를 담는다.
         */
        UserInfo userInfo = new UserInfo(userType,id,password,email,
                phoneNumber,address,agreeAdvertisement,privateBusinessName,businessNumber,businessImageURL,
                corporationBusinessName,corporationBusinessNumber,nickname);

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
     * 회원 정보 수정 (POST uri 가 겹쳤을때의 예시 용도)
     * @param patchUserReq
     * @return PatchUserRes
     * @throws BaseException
     */
    public PatchUserRes updateUserInfo(@NonNull Integer userId, PatchUserReq patchUserReq) throws BaseException {
        try {
            String email = patchUserReq.getEmail().concat("_edited");
            String nickname = patchUserReq.getNickname().concat("_edited");
            String phoneNumber = patchUserReq.getPhoneNumber().concat("_edited");
            return new PatchUserRes(email, nickname, phoneNumber);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_PATCH_USER);
        }
    }

    /**
     * 회원 탈퇴
     * @param userIdx
     * @throws BaseException
     */
    public void deleteUserInfo(int userIdx) throws BaseException {
        // 1. 존재하는 UserInfo가 있는지 확인 후 저장
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);

        // 2-1. 해당 UserInfo를 완전히 삭제
//        try {
//            userInfoRepository.delete(userInfo);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR_USER_INFO);
//        }

        // 2-2. 해당 UserInfo의 status를 INACTIVE로 설정
        userInfo.setStatus("INACTIVE");
        try {
            userInfoRepository.save(userInfo);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_DELETE_USER);
        }
    }

    /**
     * 이메일로 새로운 랜덤 비밀번호 전송후 유저 비밀번호 업데이트
     * @param email
     * @return
     * @throws BaseException
     */

    public GetNewPasswordRes patchUserPassword(String email) throws BaseException{
        UserInfo existsUserInfo = null;
        try{
            //존재한다면
            existsUserInfo = userInfoProvider.retrieveUserInfoByEmail(email);
        }catch(BaseException exception){
            throw new BaseException(NOT_FOUND_USER);
        }
        //새비밀번호 전송
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
     * @param rand
     * @param phoneNum
     * @throws BaseException
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


}