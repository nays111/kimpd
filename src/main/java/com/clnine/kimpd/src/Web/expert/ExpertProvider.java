package com.clnine.kimpd.src.Web.expert;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.category.CategoryProvider;
import com.clnine.kimpd.src.Web.category.UserJobCategoryRepository;
import com.clnine.kimpd.src.Web.category.models.UserJobCategory;
import com.clnine.kimpd.src.Web.expert.models.*;
import com.clnine.kimpd.src.Web.project.ProjectRepository;
import com.clnine.kimpd.src.Web.review.ReviewRepository;
import com.clnine.kimpd.src.Web.review.models.GetReviewListRes;
import com.clnine.kimpd.src.Web.review.models.Review;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_USER;

@Service
@RequiredArgsConstructor
public class ExpertProvider {
    private final UserInfoRepository userInfoRepository;
    private final ProjectRepository projectRepository;
    private final ReviewRepository reviewRepository;
    private final PortfolioRepository portfolioRepository;
    private final CategoryProvider categoryProvider;
    private final UserJobCategoryRepository userJobCategoryRepository;

    /**
     * 전문가 상세보기
     * @param userIdx
     * @return
     * @throws BaseException
     */
    public GetExpertRes getExpertRes(int userIdx) throws BaseException {
        UserInfo userInfo;
        try {
            userInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx, "ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        String userProfileImage = userInfo.getProfileImageURL();
        String nickname = userInfo.getNickname();
        String city = userInfo.getCity();
        String introduce = userInfo.getIntroduce();
        String career = userInfo.getCareer();
        String userMainJobCategoryChildName = categoryProvider.getMainJobCategoryChild(userInfo);
        String etc = userInfo.getEtc();
        int projectCount = projectRepository.countAllByUserInfoAndStatus(userInfo, "ACTIVE");
        int reviewCount = reviewRepository.countAllByEvaluatedUserInfoAndStatus(userInfo, "ACTIVE");
        List<Portfolio> portfolioList = portfolioRepository.findAllByUserInfo(userInfo);
        List<GetPortfolioListRes> getPortfolioListResList = new ArrayList<>();
        for(int i=0;i<portfolioList.size();i++){
            String portfolioURL = portfolioList.get(i).getPortfolioURL();
            GetPortfolioListRes getPortfolioList = new GetPortfolioListRes(portfolioURL);
            getPortfolioListResList.add(getPortfolioList);
        }
        double sum = 0;
        List<Review> reviewList = reviewRepository.findAllByEvaluatedUserInfoAndStatus(userInfo, "ACTIVE");
        List<GetReviewListRes> getReviewListResList = new ArrayList<>();
        for (int i = 0; i < reviewList.size(); i++) {
            int reviewIdx = reviewList.get(i).getReviewIdx();
            int reviewerUserIdx = reviewList.get(i).getEvaluateUserInfo().getUserIdx();
            String reviewNickname = reviewList.get(i).getEvaluateUserInfo().getNickname();
            String reviewUserImageProfile = reviewList.get(i).getEvaluateUserInfo().getProfileImageURL();
            int star = reviewList.get(i).getStar();
            String description = reviewList.get(i).getDescription();
            sum += star;
            Date reviewCreatedAt = reviewList.get(i).getCreatedAt();
            SimpleDateFormat sDate = new SimpleDateFormat("yy.MM.dd");
            String reviewMadeTime = sDate.format(reviewCreatedAt);

            GetReviewListRes getReviewListRes = new GetReviewListRes(reviewIdx, reviewerUserIdx, reviewNickname, reviewUserImageProfile, star, description, reviewMadeTime);
            getReviewListResList.add(getReviewListRes);
        }
        double average = Math.round((sum / reviewCount) * 10) / 10.0;

        GetExpertRes getExpertRes = new GetExpertRes(userIdx, userProfileImage, nickname, city, userMainJobCategoryChildName,average, reviewCount, introduce, career, etc,getPortfolioListResList, getReviewListResList, projectCount);
        return getExpertRes;
    }


    /**
     * 전문가 리스트로 보기 (검색 필터링) - 미완
     */
    public List<GetUsersRes> getExpertsList(String word) throws BaseException {
        List<UserInfo> userInfoList;


        //userInfo를 가지고 userJobCategory 리스트
        //userJobCategory를 가지고 jobCategory 에 가서 jobCategoryName;
        try {
            if (word == null) {
                userInfoList = userInfoRepository.findByStatusAndUserType("ACTIVE", 2);
            } else {
                userInfoList = userInfoRepository.findByUserTypeAndStatusAndNicknameIsContainingOrIntroduceIsContaining(2, "ACTIVE", word, word);
            }
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }
        ArrayList<GetUsersRes> getUsersResList = new ArrayList<>();
        for (int i = 0; i < userInfoList.size(); i++) {
            UserInfo userInfo = userInfoList.get(i);
            int userIdx = userInfo.getUserIdx();
            String nickname = userInfo.getNickname();
            String introduce = userInfo.getIntroduce();
            String profileImageURL = userInfo.getProfileImageURL();
            List<UserJobCategory> userJobCategoryList = userJobCategoryRepository.findByUserInfo(userInfo);
            ArrayList<String> jobCategoryParentNameList = new ArrayList<>();
            for (int j = 0; j < userJobCategoryList.size(); j++) {
                String categoryName = userJobCategoryList.get(j).getJobCategoryParent().getJobCategoryName();
                jobCategoryParentNameList.add(categoryName);
            }

            //UserInfo 있으니깐 UserInfo 로 JobCategoryParent를 찾아냄
            //jobCategoryParent가  찾아냈으니깐 거기서 parentName가져온다.

            Integer count = reviewRepository.countAllByEvaluatedUserInfoAndStatus(userInfo, "ACTIVE");
            if (count == null) {
                count = 0;
            }
            List<Review> reviewList = reviewRepository.findAllByEvaluatedUserInfoAndStatus(userInfo, "ACTIVE");
            double sum = 0;
            for (int j = 0; j < reviewList.size(); j++) {
                sum += reviewList.get(j).getStar();
            }

            //double average = Math.round((sum/count)*100)/100.0;

            //GetUsersRes getUsersRes = new GetUsersRes(userIdx, profileImageURL, nickname, introduce, average, count);
            //getUsersResList.add(getUsersRes);
        }

        return getUsersResList;
    }

    /**
     * 전문가 리스트 검색
     * @param word
     * @param jobCategoryParentIdx
     * @param jobCategoryChildIdx
     * @param genreCategoryIdx
     * @param city
     * @param minimumCastingPrice
     * @param page
     * @param sort
     * @return
     * @throws BaseException
     */

    public GetExpertsRes findExperts(String word,Integer jobCategoryParentIdx,Integer jobCategoryChildIdx, Integer genreCategoryIdx, String city,String minimumCastingPrice,int page,int sort) throws BaseException{
        int pageSearch = (page-1)*5;
        List<Object[]> resultList = null;
        List<Object[]> resultCount = null;

        if(sort==1){ //퍙점순 정렬
            resultList = userInfoRepository.findExpertOrderByReview(jobCategoryParentIdx,jobCategoryChildIdx,genreCategoryIdx,city,word,word,minimumCastingPrice,pageSearch);
            resultCount = userInfoRepository.findExpertCount(jobCategoryParentIdx,jobCategoryChildIdx,genreCategoryIdx,city,word,word,minimumCastingPrice);
        } else if(sort==2){ //섭외순 정렬
            resultList = userInfoRepository.findExpertOrderByCasting(jobCategoryParentIdx,jobCategoryChildIdx,genreCategoryIdx,city,word,word,minimumCastingPrice,pageSearch);
            resultCount = userInfoRepository.findExpertCount(jobCategoryParentIdx,jobCategoryChildIdx,genreCategoryIdx,city,word,word,minimumCastingPrice);
        }


        List<GetUsersRes> getUsersResList = resultList.stream().map(getUsersRes-> new GetUsersRes(
                Integer.parseInt(String.valueOf(getUsersRes[0])), //userIdx
                (String) getUsersRes[1], //profileImageURL
                (String) getUsersRes[2], //nickname
                (String) getUsersRes[3],
                (String) getUsersRes[4],
                Float.parseFloat(String.valueOf(getUsersRes[5])),
                Integer.parseInt(String.valueOf(getUsersRes[6]))
        )).collect(Collectors.toList());

        GetExpertsRes getExpertsRes = new GetExpertsRes(resultCount.size(),getUsersResList);
        return getExpertsRes;
    }


}
