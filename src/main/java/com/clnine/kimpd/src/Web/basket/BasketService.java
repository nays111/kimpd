package com.clnine.kimpd.src.Web.basket;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.basket.models.PostBasketCastingReq;
import com.clnine.kimpd.src.Web.basket.models.PostBasketReq;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.project.ProjectRepository;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

//        String castingStartDate = postBasketReq.getCastingStartDate();//삽입할 정보
//        String castingEndDate = postBasketReq.getCastingEndDate();//삽입할 정보
//        String castingPrice = postBasketReq.getCastingPrice();//삽입할 정보

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

//            Casting casting = new Casting(userInfo,expertInfo,project,castingPrice,castingStartDate,castingEndDate,castingStatus);


            Casting casting = new Casting(userInfo,expertInfo,project,castingStatus);

            castingRepository.save(casting);
        }

    }

    /**
     * 장바구니에 있는 걸로 섭외 요청하기
     * @param postBasketCastingReq
     * @throws BaseException
     */
    public void postBasketCasting(PostBasketCastingReq postBasketCastingReq) throws BaseException{
        List<Integer> castingIdxList = postBasketCastingReq.getCastingIdx();
        List<Casting> castingList = new ArrayList<>();
        for(int i=0;i<castingIdxList.size();i++){
            Casting casting;
            try{
                casting = castingRepository.findAllByCastingIdxAndStatus(castingIdxList.get(i),"ACTIVE");
            }catch (Exception ignored){
                throw new BaseException(BaseResponseStatus.NOT_FOUND_CASTING);
            }
            String castingStartDate = casting.getCastingStartDate();
            String castingEndDate = casting.getCastingEndDate();
            String castingPrice = casting.getCastingPrice();
            String castingMessage = casting.getCastingMessage();
            String castingWork = casting.getCastingWork();
            String castingPriceDate = casting.getCastingPriceDate();

            if(castingMessage==null || castingWork==null || castingPriceDate==null || castingStartDate==null || castingEndDate==null || castingPrice==null){
                throw new BaseException(BaseResponseStatus.DID_NOT_INSERT_CASTING_CONDITION);
            }else {
                castingList.add(casting);
            }
        }

        for(int i=0;i<castingList.size();i++) {
            Casting casting = castingList.get(i);
            casting.setCastingStatus(1);
            castingRepository.save(casting);
        }
    }

}
