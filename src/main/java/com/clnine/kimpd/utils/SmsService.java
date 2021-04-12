package com.clnine.kimpd.utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.clnine.kimpd.config.secret.Secret.*;

@Service
public class SmsService {
    public  static Map<String, Object> sendMessage(String message, String phoneNum)
            throws IOException, JsonParseException {
        /**************** 문자전송하기 예제 ******************/
        /* "result_code":결과코드,"message":결과문구, */
        /* "msg_id":메세지ID,"error_cnt":에러갯수,"success_cnt":성공갯수 */
		/* 동일내용 > 전송용 입니다.
		/******************** 인증정보 ********************/
        String sms_url = "https://apis.aligo.in/send/"; // 전송요청 URL

        Map<String, String> sms = new HashMap<String, String>();

        sms.put("user_id", aligoId); // SMS 아이디
        sms.put("key", aligoKey); //인증키

        /******************** 인증정보 ********************/

        /******************** 전송정보 ********************/
        sms.put("msg", message); // 메세지 내용
        sms.put("receiver", phoneNum); // 수신번호
        sms.put("sender", aligoSender); // 발신번호
        sms.put("testmode_yn", "N"); // Y 인경우 실제문자 전송X , 자동취소(환불) 처리

        /******************** 전송정보 ********************/
        String result = getResponseString(sms_url, sms);

        System.out.print(result);
        return JsonUtil.jsonStringToMap(result);
    }


    private static String getResponseString(String sms_url, Map<String, String> sms)
            throws IOException, ClientProtocolException, UnsupportedEncodingException {
        String encodingType = "utf-8";
        String boundary = "____boundary____";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        builder.setBoundary(boundary);
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(Charset.forName(encodingType));

        for (Iterator<String> i = sms.keySet().iterator(); i.hasNext(); ) {
            String key = i.next();
            builder.addTextBody(key, sms.get(key), ContentType.create("Multipart/related", encodingType));
        }

        HttpEntity entity = (HttpEntity) builder.build();

        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(sms_url);
        post.setEntity(entity);

        HttpResponse res = client.execute(post);

        String result = "";
        if (res != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), encodingType));
            String buffer = null;
            while ((buffer = in.readLine()) != null) {
                result += buffer;
            }
            in.close();
        }
        return result;
    }

//    public static Map<String, Object> sendSecureCode(GetUserPhoneCertification params) throws ClientProtocolException, IOException, ParseException {
//        return SmsService.sendMessage("김피디 입니다. 화면의 인증번호란에 [" + params.getCertificationValue() + "]를 입력하여 주십시오.", params);
//    }


}
