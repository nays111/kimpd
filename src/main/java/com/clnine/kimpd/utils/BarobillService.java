package com.clnine.kimpd.utils;

import com.baroservice.api.BarobillApiProfile;
import com.baroservice.api.BarobillApiService;
import com.baroservice.ws.ArrayOfCorpState;
import com.baroservice.ws.ArrayOfString;
import com.baroservice.ws.CorpState;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

@Service
@RequiredArgsConstructor
public class BarobillService {
    /**
     * 바로빌 API 정의 클래스
     *
     * 환경에 따라 BarobillApiProfile 를 지정해주세요.
     */

    private final BarobillApiService barobillApiService;


    public BarobillService() throws MalformedURLException {
        barobillApiService = new BarobillApiService(BarobillApiProfile.TESTBED);
    }

    /**
     * GetCorpState - 단건 조회
     */
    public int GetCorpState(String checkCorpNum) throws RemoteException {

        String certKey = "1CD1F6A5-33A6-40E0-B5D6-0A4256685139";            // 인증키
        String corpNum = "2648100466";            // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
        //String checkCorpNum = "";

        CorpState result = barobillApiService.corpState.getCorpState(certKey, corpNum, checkCorpNum);
        System.out.println(result);
        return result.getTaxType();


    }

    /**
     * GetCorpStates - 대량 조회
     */
    public void GetCorpStates() throws RemoteException {
        String certKey = "";            //인증키
        String corpNum = "";            //연계사업자 사업자번호 ('-' 제외, 10자리)


        ArrayOfString checkCorpNumList = new ArrayOfString();        //확인할 사업자번호 ('-' 제외, 10자리)
        checkCorpNumList.getString().add("");
        checkCorpNumList.getString().add("");

        ArrayOfCorpState result = barobillApiService.corpState.getCorpStates(certKey, corpNum, checkCorpNumList);

        System.out.println(result);
    }

    /**
     * GetCorpStateScrapRequestURL - 휴폐업 서비스 신청 URL
     */
    public void GetCorpStateScrapRequestURL() throws RemoteException {

        String certKey = "";            // 인증키
        String corpNum = "";            // 바로빌 회원 사업자번호 ('-' 제외, 10자리)
        String id = "";                // 바로빌 회원 아이디
        String pwd = "";                // 바로빌 회원 비밀번호

        String result = barobillApiService.corpState.getCorpStateScrapRequestURL(certKey, corpNum, id, pwd);

        System.out.println(result);

    }
}
