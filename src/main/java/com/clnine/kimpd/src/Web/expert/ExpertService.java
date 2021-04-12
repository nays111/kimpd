package com.clnine.kimpd.src.Web.expert;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.category.*;
import com.clnine.kimpd.src.Web.category.models.*;
import com.clnine.kimpd.src.Web.expert.models.PatchMyExpertReq;
import com.clnine.kimpd.src.Web.expert.models.Portfolio;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.NOT_EXPERT;

@Service
@RequiredArgsConstructor
public class ExpertService {

    private final UserInfoRepository userInfoRepository;
    private final PortfolioRepository portfolioRepository;
    private final UserJobCategoryRepository userJobCategoryRepository;
    private final UserGenreCategoryRepository userGenreCategoryRepository;
    private final JobCategoryParentRepository jobCategoryParentRepository;
    private final JobCategoryChildRepository jobCategoryChildRepository;
    private final GenreCategoryRepository genreCategoryRepository;
    private final UserInfoProvider userInfoProvider;


    /**
     * 전문가 프로필 수정
     */
    @Transactional
    public void patchMyExpert(PatchMyExpertReq patchMyExpertReq,int userIdx) throws BaseException{

        UserInfo userInfo=userInfoProvider.retrieveUserInfoByUserIdx(userIdx);

        /**
         * 전문가가 아닌 경우
         */
        if(userInfo.getUserType()==1 || userInfo.getUserType()==2 || userInfo.getUserType()==3){
            throw new BaseException(NOT_EXPERT);
        }

        List<Integer> jobCategoryParentIdxList = patchMyExpertReq.getJobCategoryParentIdx();
        List<Integer> jobCategoryChildIdxList = patchMyExpertReq.getJobCategoryChildIdx();
        List<Integer> genreCategoryIdxList = patchMyExpertReq.getGenreCategoryIdx();
        String introduce = patchMyExpertReq.getIntroduce();
        String career = patchMyExpertReq.getCareer();
        String etc = patchMyExpertReq.getEtc();
        int minimumCastingPrice = patchMyExpertReq.getMinimumCastingPrice();
        List<String> portfolioFileURL = patchMyExpertReq.getPortfolioFileURL();
        String castingStartPossibleDate = patchMyExpertReq.getCastingStartPossibleDate();
        String castingEndPossibleDate = patchMyExpertReq.getCastingEndPossibleDate();
        int agreeShowDB = patchMyExpertReq.getAgreeShowDB();

        /**
         * 기존에 선택했던 직종 카테고리들 제거
         */
        List<UserJobCategory> userJobCategoryList = userJobCategoryRepository.findByUserInfo(userInfo);
        for(int i=0;i<userJobCategoryList.size();i++){
            userJobCategoryRepository.delete(userJobCategoryList.get(i));
        }

        /**
         * 기존에 선택했던 장르 카테고리들 제거
         */
        List<UserGenreCategory> userGenreCategoryList = userGenreCategoryRepository.findByUserInfo(userInfo);
        for(int i=0;i<userGenreCategoryList.size();i++){
            userGenreCategoryRepository.delete(userGenreCategoryList.get(i));
        }

        /**
         * 기존에 생성했던 포트폴리오 리스트들 제거
         */
        List<Portfolio> portfolioList = portfolioRepository.findAllByUserInfo(userInfo);
        for(int i=0;i<portfolioList.size();i++){
            portfolioRepository.delete(portfolioList.get(i));
        }

        /**
         * 새롭게 입력받은 직종 카테고리들 저장
         */
        for(int i=0;i<jobCategoryParentIdxList.size();i++){
            JobCategoryParent jobCategoryParent = jobCategoryParentRepository.findAllByJobCategoryParentIdx(jobCategoryParentIdxList.get(i));
            JobCategoryChild jobCategoryChild = jobCategoryChildRepository.findAllByJobCategoryChildIdx(jobCategoryChildIdxList.get(i));
            UserJobCategory userJobCategory = new UserJobCategory(userInfo,jobCategoryParent,jobCategoryChild);
            userJobCategoryRepository.save(userJobCategory);
        }

        /**
         * 새롭게 입력받은 장르 카테고리들 저장
         */
        for(int i=0;i<genreCategoryIdxList.size();i++){
            GenreCategory genreCategory = genreCategoryRepository.findAllByGenreCategoryIdx(genreCategoryIdxList.get(i));
            UserGenreCategory userGenreCategory = new UserGenreCategory(userInfo,genreCategory);
            userGenreCategoryRepository.save(userGenreCategory);
        }
        /**
         * 새롭게 입력받은 포트폴리오 리스트를 저장
         */
        for(int i=0;i<portfolioFileURL.size();i++){
            Portfolio portfolio = new Portfolio(userInfo,portfolioFileURL.get(i));
            portfolioRepository.save(portfolio);
        }

        /**
         * 전문가 프로필 정보 수정
         */
        userInfo.setIntroduce(introduce);
        userInfo.setCareer(career);
        userInfo.setEtc(etc);
        userInfo.setMinimumCastingPrice(minimumCastingPrice);
        userInfo.setCastingPossibleStartDate(castingStartPossibleDate);
        userInfo.setCastingPossibleEndDate(castingEndPossibleDate);
        userInfo.setAgreeShowDB(agreeShowDB);
        userInfoRepository.save(userInfo);
    }
}
