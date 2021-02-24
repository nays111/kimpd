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
import java.util.Optional;
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
    private final ExpertRepository expertRepository;

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
        List<String> userJobCategoryChildName = categoryProvider.getJobCategoryChildName(userInfo);

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
        float sum = 0;
        List<Review> reviewList = reviewRepository.findAllByEvaluatedUserInfoAndStatus(userInfo, "ACTIVE");
        List<GetReviewListRes> getReviewListResList = new ArrayList<>();
        for (int i = 0; i < reviewList.size(); i++) {
            int reviewIdx = reviewList.get(i).getReviewIdx();
            int reviewerUserIdx = reviewList.get(i).getEvaluateUserInfo().getUserIdx();
            String reviewNickname = reviewList.get(i).getEvaluateUserInfo().getNickname();
            String reviewUserImageProfile = reviewList.get(i).getEvaluateUserInfo().getProfileImageURL();
            float star = reviewList.get(i).getStar();
            String description = reviewList.get(i).getDescription();
            sum += star;
            Date reviewCreatedAt = reviewList.get(i).getCreatedAt();
            SimpleDateFormat sDate = new SimpleDateFormat("yy.MM.dd");
            String reviewMadeTime = sDate.format(reviewCreatedAt);

            GetReviewListRes getReviewListRes = new GetReviewListRes(reviewIdx, reviewerUserIdx, reviewNickname, reviewUserImageProfile, star, description, reviewMadeTime);
            getReviewListResList.add(getReviewListRes);
        }
        float average = (float) (Math.round((sum / reviewCount) * 10) / 10.0);

        GetExpertRes getExpertRes = new GetExpertRes(userIdx, userProfileImage, nickname, city, userJobCategoryChildName,average, reviewCount, introduce, career, etc,getPortfolioListResList, getReviewListResList, projectCount);
        return getExpertRes;
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
    public GetExpertsRes findExperts(String word,List<Long> jobCategoryParentIdx,List<Long> jobCategoryChildIdx,
                                     List<Long> genreCategoryIdx, List<String> city,
                                     String minimumCastingPrice,int page,int sort) throws BaseException{
        int pageSearch = (page-1)*5;

        List<Object[]> resultList = null;


        if(jobCategoryParentIdx.size()==0 || jobCategoryParentIdx.isEmpty() || jobCategoryParentIdx==null){
            jobCategoryParentIdx.add(1L);
            jobCategoryParentIdx.add(2L);
            jobCategoryParentIdx.add(3L);
            jobCategoryParentIdx.add(4L);
            jobCategoryParentIdx.add(5L);
        }
        if(jobCategoryChildIdx.size()==0 || jobCategoryChildIdx.isEmpty()){
            for(Long i=1L;i<=31L;i++){
                jobCategoryChildIdx.add(i);
            }
        }
        if(genreCategoryIdx.size()==0 || genreCategoryIdx.isEmpty()){
            for(Long i=1L;i<=11L;i++){
                genreCategoryIdx.add(i);
            }
        }
        if(city.size()==0 || city.isEmpty() ){
            city.add("서울");
            city.add("경기");
            city.add("인천");
            city.add("부산");
            city.add("대구");
            city.add("대전");
            city.add("광주");
            city.add("울산");
            city.add("세종");
            city.add("강원");
            city.add("경남");
            city.add("경북");
            city.add("전남");
            city.add("전북");
            city.add("충남");
            city.add("제주");
        }


        if(sort==1){ //퍙점순 정렬
            resultList = expertRepository.findExpertListOrderByReview(jobCategoryParentIdx,jobCategoryChildIdx,genreCategoryIdx,city,word,word,pageSearch);
        } else if(sort==2){ //섭외순 정렬
            resultList = expertRepository.findExpertListOrderByCasting(jobCategoryParentIdx,jobCategoryChildIdx,genreCategoryIdx,city,word,word,pageSearch);

        }
        int size = resultList.size();

        List<GetUsersRes> getUsersResList = resultList.stream().map(getUsersRes-> new GetUsersRes(
                Integer.parseInt(String.valueOf(getUsersRes[0])), //userIdx
                (String) getUsersRes[1], //profileImageURL
                (String) getUsersRes[2], //nickname
                (String) getUsersRes[3],
                (String) getUsersRes[4],
                Float.parseFloat(String.valueOf(getUsersRes[5])),
                Integer.parseInt(String.valueOf(getUsersRes[6]))
        )).collect(Collectors.toList());

        GetExpertsRes getExpertsRes = new GetExpertsRes(size,getUsersResList);
        return getExpertsRes;
    }

}
