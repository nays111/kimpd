package com.clnine.kimpd.src.Web.expert;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.expert.models.PatchMyExpertReq;
import com.clnine.kimpd.src.Web.expert.models.Portfolio;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertService {

    private final UserInfoRepository userInfoRepository;
    private final PortfolioRepository portfolioRepository;

    /**
     * 전문가 프로필 수정
     * @param patchMyExpertReq
     * @param userIdx
     * @throws BaseException
     */
    public void patchMyExpert(PatchMyExpertReq patchMyExpertReq,int userIdx) throws BaseException{
        UserInfo userInfo;
        try {
            userInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx, "ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        List<Integer> jobCategoryParentIdxList = patchMyExpertReq.getJobCategoryParentIdx();
        List<Integer> jobCategoryChildIdxList = patchMyExpertReq.getJobCategoryChildIdx();
        List<Integer> jobCategoryGenreIdxList = patchMyExpertReq.getGenreCategoryIdx();
        String introduce = patchMyExpertReq.getIntroduce();
        String career = patchMyExpertReq.getCareer();
        String etc = patchMyExpertReq.getEtc();
        String minimumCastingPrice = patchMyExpertReq.getMinimumCastingPrice();
        List<String> portfolioURLFile = patchMyExpertReq.getPortfolioFileURL();

        List<Portfolio> portfolioList = portfolioRepository.findAllByUserInfo(userInfo);



        userInfo.setIntroduce(introduce);
        userInfo.setCareer(career);
        userInfo.setEtc(etc);
    }
}
