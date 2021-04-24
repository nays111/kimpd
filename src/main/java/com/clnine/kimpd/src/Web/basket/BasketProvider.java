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

        /**
         * 1.(projectIdx==null) 프로젝트 고르지 않으면 전체 장바구니 리스트 조회
         * 2.(projectIdx!=null) 프로젝트를 고르면 해당 프로젝트에 담은 장바구니 리트 조회
         */
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

        /**
         * castingList의 크기가 곧, 장바구니에 담긴 사람의 수
         */
        int castingExpertCount=castingList.size();

        /**
         * totalCastingPrice : 장바구니에 담긴 castingPrice들의 전체 합
         */
        String totalCastingPrice="0";
        List<GetBasketsRes> getBasketsResList = new ArrayList<>();
        if(castingList.size()!=0){
            for(Casting casting : castingList){
                int castingIdx = casting.getCastingIdx();
                UserInfo expert = casting.getExpert();
                int expertIdx = expert.getUserIdx();
                String profileImageURL = expert.getProfileImageURL();
                String nickname = expert.getNickname();

                /**
                 * 메인 2차 직종 이름 가져오기
                 */
                String userMainJobCategoryChildName = categoryProvider.getMainJobCategoryChild(expert);
                String introduce = expert.getIntroduce();


                String castingDate=null;
                /**
                 * 섭외 시작일자를 입력했을 경우는 섭외 정보 가져오기 (입력 안했을시, null)
                 */
                String castingStartDate = null;
                if(casting.getCastingStartDate()!=null && casting.getCastingStartDate().length()!=0){
                    castingStartDate = casting.getCastingStartDate();
                }

                /**
                 * 섭외 종료일자를 입력했을 경우는 섭외 정보 가져오기 (입력 안했을시, null)
                 */
                String castingEndDate = null;
                if(casting.getCastingEndDate()!=null && casting.getCastingEndDate().length()!=0){
                    castingEndDate = casting.getCastingEndDate();
                }

                /**
                 * 섭외 시작일자, 종료일자를 입력하지 않은 경우 -> 정보없음
                 */
                if(castingStartDate==null && castingEndDate==null){
                    castingDate = "";
                }else{
                    castingDate  = castingStartDate+"~"+castingEndDate;
                }

                /**
                 * 섭외 금액을 입력하지 않은 경우 -> 정보 없음
                 */
                String castingPrice=null;
                if(casting.getCastingPrice()!=null){
                    castingPrice = casting.getCastingPrice();
                }else{
                    castingPrice = "";
                }

                /**
                 * project 이름 가져오기
                 */
                String projectName = casting.getProject().getProjectName();

                /**
                 * 섭외 조건을 입력한 상태 / 입력하지 않은 상태 구분
                 */
                int castingCondition = 1; //섭외 조건을 입력하지 않은 상태
                if(casting.getCastingMessage()!=null && casting.getCastingPriceDate()!=null && casting.getCastingWork()!=null
                        && casting.getCastingPrice()!=null && casting.getCastingStartDate()!=null && casting.getCastingEndDate()!=null){
                    castingCondition = 0; // 섭외 조건을 입력한 상태
                }

                GetBasketsRes getBasketsRes = new GetBasketsRes(castingIdx,expertIdx,profileImageURL,nickname,userMainJobCategoryChildName,introduce,castingDate,projectName,castingPrice,castingCondition);
                getBasketsResList.add(getBasketsRes);


                /**
                 * totalCastingPrice : 장바구니에 담긴 castingPrice들의 합
                 */
                if(castingPrice!=""){
                    totalCastingPrice = Integer.toString(Integer.parseInt(totalCastingPrice) + Integer.parseInt(castingPrice));
                }
            }
            getBasketListRes = new GetBasketListRes(getBasketsResList,projectBudget,castingExpertCount,totalCastingPrice);
        }
        return getBasketListRes;
    }
}
