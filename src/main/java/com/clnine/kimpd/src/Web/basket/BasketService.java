package com.clnine.kimpd.src.Web.basket;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.basket.models.PostBasketReq;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.project.ProjectRepository;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final UserInfoRepository userInfoRepository;
    private final ProjectRepository projectRepository;
    private final CastingRepository castingRepository;

    /**
     * 장바구니 담기 함수
     * @param userIdx
     * @param postBasketReq
     * @throws BaseException
     */
    public void postBasket(int userIdx, PostBasketReq postBasketReq) throws BaseException{
        /**
         * 장바구니의 주체 찾기
         */
        UserInfo userInfo;
        try{
            userInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(userIdx,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        /**
         * 장바구니의 담을 전문가
         */
        List<Integer> expertIdxList = postBasketReq.getUserIdx();

        String castingStartDate = postBasketReq.getCastingStartDate();//삽입할 정보
        String castingEndDate = postBasketReq.getCastingEndDate();//삽입할 정보
        String castingPrice = postBasketReq.getCastingPrice();//삽입할 정보

        int projectIdx = postBasketReq.getProjectIdx();
        Project project; //삽입할 정보
        try{
            project = projectRepository.findByProjectIdxAndStatus(projectIdx,"ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_PROJECTS);
        }




        for(int i=0;i<expertIdxList.size();i++){
            int expertIdx = expertIdxList.get(i);
            /**
             * 전문가 찾기
             */
            UserInfo expertInfo;
            try{
                expertInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(expertIdx,"ACTIVE");
            }catch(Exception ignored){
                throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
            }
            int castingStatus = 0;

            Casting casting = new Casting(userInfo,expertInfo,project,castingPrice,castingStartDate,castingEndDate,castingStatus);
            castingRepository.save(casting);
        }

    }





}
