package com.clnine.kimpd.src.Web.expert;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.category.*;
import com.clnine.kimpd.src.Web.category.models.*;
import com.clnine.kimpd.src.Web.expert.models.PatchMyExpertReq;
import com.clnine.kimpd.src.Web.expert.models.Portfolio;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 전문가 프로필 수정 (관리)
     * @param patchMyExpertReq
     * @param userIdx
     * @throws BaseException
     */
    @Transactional
    public void patchMyExpert(PatchMyExpertReq patchMyExpertReq,int userIdx) throws BaseException{
        UserInfo userInfo;
        try {
            userInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx, "ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
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

        //기존에 있던거 지워야함
        List<UserJobCategory> userJobCategoryList = userJobCategoryRepository.findByUserInfo(userInfo);
        for(int i=0;i<userJobCategoryList.size();i++){
            userJobCategoryRepository.delete(userJobCategoryList.get(i));
        }
        List<UserGenreCategory> userGenreCategoryList = userGenreCategoryRepository.findByUserInfo(userInfo);
        for(int i=0;i<userGenreCategoryList.size();i++){
            userGenreCategoryRepository.delete(userGenreCategoryList.get(i));
        }

        //새로운걸 저장
        for(int i=0;i<jobCategoryParentIdxList.size();i++){
            JobCategoryParent jobCategoryParent = jobCategoryParentRepository.findAllByJobCategoryParentIdx(jobCategoryParentIdxList.get(i));
            JobCategoryChild jobCategoryChild = jobCategoryChildRepository.findAllByJobCategoryChildIdx(jobCategoryChildIdxList.get(i));
            UserJobCategory userJobCategory = new UserJobCategory(userInfo,jobCategoryParent,jobCategoryChild);
            userJobCategoryRepository.save(userJobCategory);
        }
        for(int i=0;i<genreCategoryIdxList.size();i++){
            GenreCategory genreCategory = genreCategoryRepository.findAllByGenreCategoryIdx(genreCategoryIdxList.get(i));
            UserGenreCategory userGenreCategory = new UserGenreCategory(userInfo,genreCategory);
            userGenreCategoryRepository.save(userGenreCategory);
        }
        for(int i=0;i<portfolioFileURL.size();i++){
            Portfolio portfolio = new Portfolio(userInfo,portfolioFileURL.get(i));
            portfolioRepository.save(portfolio);
        }


        userInfo.setIntroduce(introduce);
        userInfo.setCareer(career);
        userInfo.setEtc(etc);
        userInfo.setMinimumCastingPRice(minimumCastingPrice);
        userInfo.setCastingPossibleStartDate(castingStartPossibleDate);
        userInfo.setCastingPossibleEndDate(castingEndPossibleDate);
        userInfo.setAgreeShowDB(agreeShowDB);
        userInfoRepository.save(userInfo);
    }
}
