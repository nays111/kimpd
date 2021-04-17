package com.clnine.kimpd.src.Web.basket;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.basket.models.PostBasketCastingReq;
import com.clnine.kimpd.src.Web.basket.models.PostBasketReq;
import com.clnine.kimpd.src.Web.casting.CastingProvider;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.project.ProjectProvider;
import com.clnine.kimpd.src.Web.project.ProjectRepository;
import com.clnine.kimpd.src.Web.project.models.Project;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.ALREADY_POST_BASKET;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final CastingRepository castingRepository;
    private final UserInfoProvider userInfoProvider;
    private final ProjectProvider projectProvider;
    private final CastingProvider castingProvider;
    private final BasketRepository basketRepository;

    /**
     * 장바구니 담기
     */
    @Transactional
    public void postBasket(int userIdx, PostBasketReq postBasketReq) throws BaseException{
        UserInfo userInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx);

        /**
         * 장바구니에 담을 전문가 리스트
         */
        List<Integer> expertIdxList = postBasketReq.getUserIdx();

        /**
         * 어떤 프로젝트에 담을지
         */
        int projectIdx = postBasketReq.getProjectIdx();
        Project project = projectProvider.retrieveProjectByProjectIdx(projectIdx);

        /**
         * 전문가들을 장바구니에 담기 (castingStatus=0 : 장바구니에 담긴 상태)
         */
        for(int i=0;i<expertIdxList.size();i++){
            int expertIdx = expertIdxList.get(i);
            UserInfo expertInfo = userInfoProvider.retrieveUserInfoByUserIdx(expertIdx);

            /**
             * 이미 장바구니에 담은 전문가가 존재할 경우 예외 처리
             */
            Casting existCasting = basketRepository.findAllByUserInfoAndExpertAndProjectAndStatusAndCastingStatus(userInfo,expertInfo,project,"ACTIVE",0);
            if(existCasting != null){
                throw new BaseException(ALREADY_POST_BASKET);
            }

            Casting casting = new Casting(userInfo,expertInfo,project,0);
            try{
                castingRepository.save(casting);
            }catch(Exception ignored){
                throw new BaseException(BaseResponseStatus.FAILED_TO_SAVE_BASKETS);
            }
        }
    }

    /**
     * 장바구니에 있는 걸로 섭외 요청하기
     */
    @Transactional
    public void postBasketCasting(PostBasketCastingReq postBasketCastingReq) throws BaseException{

        /**
         * 장바구니에서 섭외할 유저 리스트 (castingIdxList)
         * (장바구니에 담긴 순간, castingIdx로 취급)
         */
        List<Integer> castingIdxList = postBasketCastingReq.getCastingIdx();

        List<Casting> castingList = new ArrayList<>();
        for(int i=0;i<castingIdxList.size();i++){

            Casting casting = castingProvider.retrieveCastingByCastingIdx(castingIdxList.get(i));

            /**
             * 현재 장바구니에 담겨있는 섭외 내용
             */
            String castingStartDate = casting.getCastingStartDate();
            String castingEndDate = casting.getCastingEndDate();
            String castingPrice = casting.getCastingPrice();
            String castingMessage = casting.getCastingMessage();
            String castingWork = casting.getCastingWork();
            String castingPriceDate = casting.getCastingPriceDate();

            /**
             * 만약, 섭외 조건이 입력이 안되어있으면 섭외 신청 불가능
             */
            if(castingMessage==null || castingWork==null || castingPriceDate==null || castingStartDate==null || castingEndDate==null || castingPrice==null){
                throw new BaseException(BaseResponseStatus.DID_NOT_INSERT_CASTING_CONDITION);
            }else {
                castingList.add(casting);
            }
        }

        for(int i=0;i<castingList.size();i++) {
            Casting casting = castingList.get(i);
            /**
             * castingStatus=1 (섭외 미승인 상태)
             */
            casting.setCastingStatus(1);
            try{
                castingRepository.save(casting);
            }catch (Exception ignored){
                throw new BaseException(BaseResponseStatus.FAILED_TO_POST_CASTING);
            }
        }
    }
}
