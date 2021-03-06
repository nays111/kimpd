package com.clnine.kimpd.src.Web.expert;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.category.*;
import com.clnine.kimpd.src.Web.expert.models.GetMyReceivedCastingsByCalendarRes;
import com.clnine.kimpd.src.Web.category.models.*;
import com.clnine.kimpd.src.Web.expert.models.*;
import com.clnine.kimpd.src.Web.project.ProjectRepository;
import com.clnine.kimpd.src.Web.review.ReviewRepository;
import com.clnine.kimpd.src.Web.review.models.GetReviewListRes;
import com.clnine.kimpd.src.Web.review.models.Review;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

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
    private final UserGenreCategoryRepository userGenreCategoryRepository;
    private final UserInfoProvider userInfoProvider;
    private final CastingRepository castingRepository;
    private final JobCategoryChildRepository jobCategoryChildRepository;
    private final JobCategoryParentRepository jobCategoryParentRepository;
    private final GenreCategoryRepository genreCategoryRepository;
    private final ExpertCastingDateRepository expertCastingDateRepository;

    /**
     * ????????? ????????????
     */
    @Transactional(readOnly = true)
    public GetExpertRes getExpertRes(int userIdx) throws BaseException {
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        /**
         * ?????? ?????? ????????????
         */
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
     * ????????? ????????? ??????
     */
    @Transactional(readOnly = true)
    public GetExpertsRes findExperts(PostExpertsReq postExpertsReq) throws BaseException{
        String word = postExpertsReq.getWord();
        List<Long> jobCategoryParentIdx = postExpertsReq.getJobCategoryParentIdx();
        List<Long> jobCategoryChildIdx = postExpertsReq.getJobCategoryChildIdx();
        List<Long> genreCategoryIdx = postExpertsReq.getGenreCategoryIdx();
        List<String> city = postExpertsReq.getCity();
//        String castingStartDate = postExpertsReq.getCastingStartDate();
//        String castingEndDate = postExpertsReq.getCastingEndDate();
        List<String> castingDateList = postExpertsReq.getCastingDate();

        Integer minimumCastingPrice = postExpertsReq.getMinimumCastingPrice();
        int page = postExpertsReq.getPage();
        int sort = postExpertsReq.getSort();
        int pageSearch = (page-1)*5;

        /**
         * ?????? 1??? ??????????????? ?????? ????????? ?????? (???????????? ?????? 1??? ?????? ????????????)
         */
        if(jobCategoryParentIdx.size()==0 || jobCategoryParentIdx.isEmpty() || jobCategoryParentIdx==null){
            JobCategoryParent jobCategoryParent = jobCategoryParentRepository.findTopByOrderByJobCategoryParentIdxDesc();
            long lastJobCategoryParent = jobCategoryParent.getJobCategoryParentIdx();
            while(lastJobCategoryParent > 0){
                jobCategoryParentIdx.add(lastJobCategoryParent);
                lastJobCategoryParent--;
            }
            jobCategoryParentIdx.add(1L);jobCategoryParentIdx.add(2L);
            jobCategoryParentIdx.add(3L);jobCategoryParentIdx.add(4L);
            jobCategoryParentIdx.add(5L);jobCategoryParentIdx.add(6L);
        }
        /**
         * ?????? 2??? ??????????????? ?????? ????????? ?????? (???????????? ?????? 2??? ?????? ????????????)
         */
        if(jobCategoryChildIdx.size()==0 || jobCategoryChildIdx.isEmpty()){
            JobCategoryChild jobCategoryChild  = jobCategoryChildRepository.findTopByOrderByJobCategoryChildIdxDesc();
            long lastJobCategoryChild = jobCategoryChild.getJobCategoryChildIdx();
            while(lastJobCategoryChild > 0){
                jobCategoryChildIdx.add(lastJobCategoryChild);
                lastJobCategoryChild--;
            }
        }
        /**
         * ?????? ??????????????? ?????? ????????? ?????? (???????????? ?????? ???????????? ?????? ????????????)
         */
        if(genreCategoryIdx.size()==0 || genreCategoryIdx.isEmpty()){
            GenreCategory genreCategory = genreCategoryRepository.findTopByOrderByGenreCategoryIdxDesc();
            long lastGenreCategoryIdx = genreCategory.getGenreCategoryIdx();
            while(lastGenreCategoryIdx>0){
                genreCategoryIdx.add(lastGenreCategoryIdx);
                lastGenreCategoryIdx--;
            }
        }
        /**
         * ?????? ????????? ???????????? ????????? ?????? (???????????? ????????? ?????? ????????????)
         */
        if(city.size()==0 || city.isEmpty() ){
            city.add("??????");city.add("??????");city.add("??????");city.add("??????");city.add("??????");
            city.add("??????");city.add("??????");city.add("??????");city.add("??????");city.add("??????");
            city.add("??????");city.add("??????");city.add("??????");city.add("??????");city.add("??????");
            city.add("??????");city.add("??????");city.add("??????");
        }
        /**
         * ????????? ?????? ??????????????? ?????? ??????
         */
        if(city!=null || city.size()!=0){
            city.add("??????");
        }

        /**
         * ?????? ????????? ?????? ???????????? ????????? ?????? (""~ "9999.99.99" ?????? ??????)
         */
//        if(castingStartDate==null || castingStartDate.length()==0){
//            castingStartDate=null;
//        }
//        if(castingEndDate==null || castingEndDate.length()==0){
//            castingEndDate=null;
//        }
        /**
         * 2021.04.25
         * castingDate : ?????? ???????????? ?????? ???????????? ??????
         */
        String castingDate="";
        for(String date : castingDateList){
            castingDate += date;
        }

        /**
         * ?????? ????????? ?????? ???????????? ????????? ?????? (?????? ????????? ?????? ?????? ??????)
         */
        if(minimumCastingPrice==null){
            minimumCastingPrice=999999999;
        }


        List<Object[]> resultList = null;
        List<Object[]> sizeResultList = null;
        if(sort==1){ //????????? ??????
            resultList = expertRepository.findExpertListOrderByReview(jobCategoryParentIdx,jobCategoryChildIdx,genreCategoryIdx,city,word,word,minimumCastingPrice,castingDate,pageSearch);
            sizeResultList = expertRepository.findExpertListCountOrderByReview(jobCategoryParentIdx,jobCategoryChildIdx,genreCategoryIdx,city,word,word,minimumCastingPrice,castingDate);
        } else if(sort==2){ //????????? ??????
            resultList = expertRepository.findExpertListOrderByCasting(jobCategoryParentIdx,jobCategoryChildIdx,genreCategoryIdx,city,word,word,minimumCastingPrice,castingDate,pageSearch);
            sizeResultList = expertRepository.findExpertListCountOrderByCasting(jobCategoryParentIdx,jobCategoryChildIdx,genreCategoryIdx,city,word,word,minimumCastingPrice,castingDate);
        }

        int size = sizeResultList.size(); //?????? ?????? ??????

        /**
         * native query mapping ??????
         */
        List<GetUsersRes> getUsersResList = resultList.stream().map(getUsersRes-> new GetUsersRes(
                Integer.parseInt(String.valueOf(getUsersRes[0])), //userIdx
                (String) getUsersRes[1], //profileImageURL
                (String) getUsersRes[2], //nickname
                (String) getUsersRes[3], //jobCategoryChildName
                (String) getUsersRes[4], //introduce
                Float.parseFloat(String.valueOf(getUsersRes[5])), //??????
                Integer.parseInt(String.valueOf(getUsersRes[6])) //?????? ??????
        )).collect(Collectors.toList());

        GetExpertsRes getExpertsRes = new GetExpertsRes(size,getUsersResList);
        return getExpertsRes;
    }

    /**
     * ??? ?????? ?????? (???????????? ??????)
     */
    @Transactional(readOnly = true)
    public GetMyExpertRes getMyExpertRes(int userIdx) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        /**
         * ???????????? ?????? ??????
         */
        if(userInfo.getUserType()==1 || userInfo.getUserType()==2 || userInfo.getUserType()==3){
            throw new BaseException(NOT_EXPERT);
        }
        List<UserJobCategory> userJobCategoryList = userJobCategoryRepository.findByUserInfo(userInfo);
        List<UserGenreCategory> userGenreCategoryList = userGenreCategoryRepository.findByUserInfo(userInfo);
        List<Portfolio> portfolioList = portfolioRepository.findAllByUserInfo(userInfo);

        List<ExpertCastingDate> expertCastingDateList = expertCastingDateRepository.findAllByUserInfo(userInfo);

        List<UserJobCategoryParentDTO> jobCategoryParentDTOS = new ArrayList<>();
        List<UserJobCategoryChildDTO> jobCategoryChildDTOS = new ArrayList<>();
        List<UserGenreCategoryDTO> genreCategoryDTOS = new ArrayList<>();

        if(userJobCategoryList!=null){
            for(int i=0;i<userJobCategoryList.size();i++){
                UserJobCategory userJobCategory = userJobCategoryList.get(i);
                JobCategoryChild jobCategoryChild = userJobCategory.getJobCategoryChild();
                JobCategoryParent jobCategoryParent = userJobCategory.getJobCategoryParent();
                UserJobCategoryParentDTO userJobCategoryParentDTO = new UserJobCategoryParentDTO(jobCategoryParent.getJobCategoryParentIdx(), jobCategoryParent.getJobCategoryName());
                UserJobCategoryChildDTO userJobCategoryChildDTO = new UserJobCategoryChildDTO(jobCategoryChild.getJobCategoryChildIdx(), jobCategoryChild.getJobCategoryChildName());
                jobCategoryParentDTOS.add(userJobCategoryParentDTO);
                jobCategoryChildDTOS.add(userJobCategoryChildDTO);
            }
        }

        if(userGenreCategoryList!=null){
            for(int i=0;i<userGenreCategoryList.size();i++){
                UserGenreCategory userGenreCategory = userGenreCategoryList.get(i);
                GenreCategory genreCategory = userGenreCategory.getGenreCategory();
                UserGenreCategoryDTO userGenreCategoryDTO = new UserGenreCategoryDTO(genreCategory.getGenreCategoryIdx(),genreCategory.getGenreCategoryName());
                genreCategoryDTOS.add(userGenreCategoryDTO);
            }
        }

        List<String> portfolioFileURL = new ArrayList<>();
        if(portfolioFileURL!=null){
            for(int i=0;i<portfolioList.size();i++){
                String portfolioURL = portfolioList.get(i).getPortfolioURL();
                portfolioFileURL.add(portfolioURL);
            }
        }
        /**
         * ??? ?????? ?????? ?????? ??????
         * ?????? : 2021.04.23
         */
        List<String> castingPossibleDateList = new ArrayList<>();
        if(castingPossibleDateList!=null){
            for(int i=0;i<expertCastingDateList.size();i++){
                String castingPossibleDate = expertCastingDateList.get(i).getCastingPossibleDate();
                castingPossibleDateList.add(castingPossibleDate);
            }
        }

        String introduce,career,etc;
        Integer minimumCastingPrice,agreeShowDB;

         introduce = userInfo.getIntroduce();
         career = userInfo.getCareer();
         etc = userInfo.getEtc();
         minimumCastingPrice = userInfo.getMinimumCastingPrice();

         agreeShowDB = userInfo.getAgreeShowDB();

        GetMyExpertRes getMyExpertRes = new GetMyExpertRes(jobCategoryParentDTOS,jobCategoryChildDTOS,genreCategoryDTOS,introduce,career,portfolioFileURL,etc,minimumCastingPrice,castingPossibleDateList,agreeShowDB);
        return getMyExpertRes;
    }

    /**
     * ???????????? ????????? ????????? ???????????? (????????? ?????? ????????? ????????? ?????? ?????? ?????? ??????)
     */
    @Transactional(readOnly = true)
    public GetMyExpertSchedulesManage getMyExpertSchedule(int userIdx,String year,String month) throws BaseException{

        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);

        /**
         * ???????????? ?????? ??????
         */
        if(userInfo.getUserType()==1 || userInfo.getUserType()==2 || userInfo.getUserType()==3){
            throw new BaseException(NOT_EXPERT);
        }
        String startMonth = year+"."+month+"."+"01";

        String size=null;

        /**
         * 1~12 ?????? ?????? ?????? ????????? ???????????????, ?????? ?????? ??????
         */
        if(month.equals("02")){
            size="28";
        }else if(month.equals("01")||month.equals("03")||month.equals("05")||month.equals("07")||month.equals("08")||month.equals("10")||month.equals("12")){
            size="31";
        }else if(month.equals("04")||month.equals("06")||month.equals("09")||month.equals("11")){
            size="30";
        }
        String endMonth = year+"."+month+"."+size;

        ArrayList<Integer> dayList = new ArrayList<>();
        List<Casting> castingList = castingRepository.findAllByExpertAndStatusAndCastingStatusAndCastingStartDateLessThanEqualAndCastingEndDateGreaterThanEqual(userInfo,"ACTIVE",2,endMonth,startMonth);

        //List<GetMyExpertSchedulesManage> getMyExpertSchedulesManageList = new ArrayList<>();
        for(int i=0;i<castingList.size();i++){

            String startDate;
            if(castingList.get(i).getCastingStartDate().compareTo(startMonth)<0){
                startDate = startMonth;
            }else{
                startDate = castingList.get(i).getCastingStartDate();
            }

            String endDate;
            if(castingList.get(i).getCastingEndDate().compareTo(endMonth)>0){
                endDate = endMonth;
            }else{
                endDate = castingList.get(i).getCastingEndDate();
            }

            int startDay = Integer.parseInt(startDate.substring(8));
            int endDay = Integer.parseInt(endDate.substring(8));

            for(int j=startDay;j<=endDay;j++){
                dayList.add(j);
            }
        }

        ArrayList<Integer> resultList = new ArrayList<Integer>();
        for (int i = 0; i < dayList.size(); i++) {
            if (!resultList.contains(dayList.get(i))) {
                resultList.add(dayList.get(i));
            }
        }
        GetMyExpertSchedulesManage getMyExpertSchedulesManage = new GetMyExpertSchedulesManage(resultList);
        return getMyExpertSchedulesManage;
    }

    /**
     * ????????? ????????? ?????? ???????????? (???????????? ????????? ???????????? ???, ??? ????????? ?????? ????????? ???????????? ?????????)
     */
    public List<GetMyReceivedCastingsByCalendarRes> getMySpecificSchedules(int expertIdx, String year, String month, String day) throws BaseException{
        UserInfo expertInfo = userInfoProvider.retrieveUserInfoByUserIdx(expertIdx);
        if(expertInfo.getUserType()==1 || expertInfo.getUserType()==2 || expertInfo.getUserType()==3){
            throw new BaseException(NOT_EXPERT);
        }
        String searchDay = year+"."+month+"."+day;

        System.out.println(searchDay);
        List<GetMyReceivedCastingsByCalendarRes> getMyReceivedCastingsByCalendarResList = new ArrayList<>();

        List<Casting> castingList = castingRepository.findAllByExpertAndStatusAndCastingStatusAndCastingStartDateLessThanEqualAndCastingEndDateGreaterThanEqual(expertInfo,"ACTIVE",2,searchDay,searchDay);

        for(int i=0;i<castingList.size();i++){
            /**
             * ?????? ?????? ????????????
             */
            int userIdx = castingList.get(i).getUserInfo().getUserIdx();
            String userProfileImageUrl = castingList.get(i).getUserInfo().getProfileImageURL();
            String name = castingList.get(i).getUserInfo().getName();
            String nickname = castingList.get(i).getUserInfo().getNickname();

            /**
             * ?????? ?????? ????????????
             */
            String castingStartDate = castingList.get(i).getCastingStartDate();
            String castingEndDate = castingList.get(i).getCastingEndDate();
            String castingDate = castingStartDate + "~" + castingEndDate;
            String castingPrice = castingList.get(i).getCastingPrice();
            String projectName = castingList.get(i).getProject().getProjectName();
            String castingPriceDate = castingList.get(i).getCastingPriceDate();
            String castingMessage = castingList.get(i).getCastingMessage();

            /**
             * ???????????? ?????? ????????????
             */
            String projectDescription = castingList.get(i).getProject().getProjectDescription();
            String projectMaker = castingList.get(i).getProject().getProjectMaker();
            String projectManager = castingList.get(i).getProject().getProjectManager();

            GetMyReceivedCastingsByCalendarRes getMyReceivedCastingsByCalendarRes =
                    new GetMyReceivedCastingsByCalendarRes(userIdx,userProfileImageUrl,name,nickname,
                            castingDate,castingPrice,projectName,projectDescription,projectMaker,
                            projectManager,castingPriceDate,castingMessage);

            getMyReceivedCastingsByCalendarResList.add(getMyReceivedCastingsByCalendarRes);
        }

        return getMyReceivedCastingsByCalendarResList;
    }
}
