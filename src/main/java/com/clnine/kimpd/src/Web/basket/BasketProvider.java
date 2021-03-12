package com.clnine.kimpd.src.Web.basket;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.basket.models.GetBasketListRes;
import com.clnine.kimpd.src.Web.basket.models.GetBasketsRes;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.category.CategoryProvider;
import com.clnine.kimpd.src.Web.project.ProjectProvider;
import com.clnine.kimpd.src.Web.project.ProjectRepository;
import com.clnine.kimpd.src.Web.project.models.GetProjectListRes;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_PROJECTS_LIST;

@Service
@RequiredArgsConstructor
public class BasketProvider {
    private final UserInfoProvider userInfoProvider;
    private final BasketRepository basketRepository;
    private final ProjectRepository projectRepository;
    private final CategoryProvider categoryProvider;
    private final ProjectProvider projectProvider;

    /**
     * 내 장바구니 조회 API
     */
    @Transactional(readOnly = true)
    public GetBasketListRes getBasketList(int userIdx,Integer projectIdx) throws BaseException{
        GetBasketListRes getBasketListRes = null;
        UserInfo userInfo= userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
        List<Casting> castingList = null;
        String projectBudget=null;
        if(projectIdx==null){
            castingList = basketRepository.findAllByUserInfoAndCastingStatusAndStatus(userInfo,0,"ACTIVE");
        }else{
            Project project = projectProvider.retrieveProjectByProjectIdx(projectIdx);
            if(project.getUserInfo()!=userInfo){
                throw new BaseException(BaseResponseStatus.NOT_USER_PROJECT);
            }
            projectBudget = project.getProjectBudget();
            castingList = basketRepository.findAllByUserInfoAndCastingStatusAndProjectAndStatus(userInfo,0,project,"ACTIVE");
        }
        int castingExpertCount=castingList.size();
        String totalCastingPrice="0";
        List<GetBasketsRes> getBasketsResList = new ArrayList<>();
        if(castingList.size()!=0){
            for(int i=0;i<castingList.size();i++){
                int castingIdx = castingList.get(i).getCastingIdx();
                UserInfo expert = castingList.get(i).getExpert();
                int expertIdx = expert.getUserIdx();
                String profileImageURL = expert.getProfileImageURL();
                String nickname = expert.getNickname();
                String userMainJobCategoryChildName = categoryProvider.getMainJobCategoryChild(expert);
                String introduce = expert.getIntroduce();
                String castingDate=null;
                String castingStartDate = null;
                String castingEndDate = null;
                if(castingList.get(i).getCastingStartDate()!=null && castingList.get(i).getCastingStartDate().length()!=0){
                    castingStartDate = castingList.get(i).getCastingStartDate();
                }
                if(castingList.get(i).getCastingEndDate()!=null && castingList.get(i).getCastingEndDate().length()!=0){
                    castingEndDate = castingList.get(i).getCastingEndDate();
                }
                if(castingStartDate==null && castingEndDate==null){
                    castingDate = "정보 없음";
                }else{
                    castingDate  = castingStartDate+"~"+castingEndDate;
                }
                String castingPrice=null;
                if(castingList.get(i).getCastingPrice()!=null){
                    castingPrice = castingList.get(i).getCastingPrice();
                }else{
                    castingPrice = "정보 없음";
                }
                String projectName = castingList.get(i).getProject().getProjectName();
                String castingCondition = "섭외 조건 입력";
                if(castingList.get(i).getCastingMessage()!=null && castingList.get(i).getCastingPriceDate()!=null && castingList.get(i).getCastingWork()!=null
                        && castingList.get(i).getCastingPrice()!=null && castingList.get(i).getCastingStartDate()!=null && castingList.get(i).getCastingEndDate()!=null){
                    castingCondition = "섭외 조건 입력 완료";
                }
                GetBasketsRes getBasketsRes = new GetBasketsRes(castingIdx,expertIdx,profileImageURL,nickname,userMainJobCategoryChildName,introduce,castingDate,projectName,castingPrice,castingCondition);
                getBasketsResList.add(getBasketsRes);
                if(castingPrice!="정보 없음"){
                    totalCastingPrice = Integer.toString(Integer.parseInt(totalCastingPrice) + Integer.parseInt(castingPrice));
                }
            }
            getBasketListRes = new GetBasketListRes(getBasketsResList,projectBudget,castingExpertCount,totalCastingPrice);
        }
        return getBasketListRes;
    }
}
