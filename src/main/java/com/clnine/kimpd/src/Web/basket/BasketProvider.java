package com.clnine.kimpd.src.Web.basket;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.basket.models.GetBasketListRes;
import com.clnine.kimpd.src.Web.basket.models.GetBasketsRes;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.category.CategoryProvider;
import com.clnine.kimpd.src.Web.project.ProjectRepository;
import com.clnine.kimpd.src.Web.project.models.GetProjectListRes;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_PROJECTS_LIST;

@Service
@RequiredArgsConstructor
public class BasketProvider {
    private final UserInfoProvider userInfoProvider;
    private final BasketRepository basketRepository;
    private final UserInfoRepository userInfoRepository;
    private final ProjectRepository projectRepository;
    private final CategoryProvider categoryProvider;


//    /**
//     * 장바구니에 담긴 프로젝트 리스트 조회 (projectIdx,projectName)
//     * @param userIdx
//     * @return
//     * @throws BaseException
//     */
//    public List<GetProjectListRes> GetProjectList(int userIdx) throws BaseException {
//        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);
//        List<Project> projectList;
//        try{
//            projectList = basketRepository.findByUserInfoAndStatus(userInfo,"ACTIVE");
//        }catch(Exception ignored){
//            throw new BaseException(FAILED_TO_GET_PROJECTS_LIST);
//        }
//        return projectList.stream().map(project -> {
//            int projectIdx = project.getProjectIdx();
//            String projectName = project.getProjectName();
//            return new GetProjectListRes(projectIdx,projectName);
//        }).collect(Collectors.toList());
//
//    }


    public GetBasketListRes getBasketList(int userIdx,Integer projectIdx) throws BaseException{
        UserInfo userInfo;
        try{
            userInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }


        List<Casting> castingList = new ArrayList<>();

        String projectBudget=null;

        if(projectIdx==null){
            //전체 장바구니 내역
            castingList = basketRepository.findAllByUserInfoAndCastingStatusAndStatus(userInfo,0,"ACTIVE");
        }else{
            Project project;
            try{
                project = projectRepository.findByProjectIdxAndStatus(projectIdx,"ACTIVE");
                projectBudget = project.getProjectBudget();
            }catch(Exception ignored){
                throw new BaseException(BaseResponseStatus.FAILED_TO_GET_PROJECTS);
            }
            castingList = basketRepository.findAllByUserInfoAndCastingStatusAndProjectAndStatus(userInfo,0,project,"ACTIVE");
        }


        int castingExpertCount=castingList.size();
        String totalCastingPrice="0";

        List<GetBasketsRes> getBasketsResList = new ArrayList<>();
        for(int i=0;i<castingList.size();i++){
            int castingIdx = castingList.get(i).getCastingIdx();
            UserInfo expert = castingList.get(i).getExpert();
            int expertIdx = expert.getUserIdx();
            String profileImageURL = expert.getProfileImageURL();
            String nickname = expert.getNickname();
            String userMainJobCategoryChildName = categoryProvider.getMainJobCategoryChild(expert);
            String introduce = expert.getIntroduce();

            String castingDate=null;
            if(castingList.get(i).getCastingStartDate()!=null && castingList.get(i).getCastingEndDate()!=null){
                String castingStartDate = "20"+castingList.get(i).getCastingStartDate();
                String castingEndDate = "20"+castingList.get(i).getCastingEndDate();
                castingDate = castingStartDate+"~"+castingEndDate;
                castingDate = castingDate.replace("/",".");
            }else{
                castingDate = "정보 없음";
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


            totalCastingPrice = Integer.toString(Integer.parseInt(totalCastingPrice) + Integer.parseInt(castingPrice));
        }

        GetBasketListRes getBasketListRes = new GetBasketListRes(getBasketsResList,projectBudget,castingExpertCount,totalCastingPrice);
        return getBasketListRes;



    }
}
