package com.clnine.kimpd.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;



    //임시 비밀번호 생성
    private String getTmpPw() {

        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int index;
        char password;

        //랜덤한 임시 비밀번호 생성
        char[] charPw=new char[] {
                '0','1','2','3','4','5','6','7','8','9',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
        };

        char[] charSpPw=new char[] {
                '!','@','#','$','%','^','&','*','?','~','_'
        };

        for(int i=0;i<8;i++) {
            index = random.nextInt(charPw.length);
            password = charPw[index];
            buffer.append(password);
        }
        for(int i=0;i<2;i++) {
            index = random.nextInt(charSpPw.length);
            password = charSpPw[index];
            buffer.append(password);
        }
        System.out.println(buffer.toString());

        return buffer.toString();
    }

    //pw찾기
    public String sendPwFindMail(String email) {
        //인증메일 전송 + 키 값 리턴
        String tmpPw = getTmpPw();
        try{
            MailUtils sendMail = new MailUtils(mailSender);
            sendMail.setSubject("KimPd 임시 비밀번호 전송");
            sendMail.setText(new StringBuffer().append("<h1>[KimPd 홈페이지 임시 비밀번호]</h1>")
                    .append("<p>임시 비밀번호 : "+tmpPw+"</p>")
                    .append("<p>위 비밀번호로 접속해주세요.</p>")
                    .toString());
            sendMail.setFrom("emailAddress","admin");
            sendMail.setTo(email);
            sendMail.send();
        }catch(MessagingException e) {
            e.printStackTrace();
        }catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return tmpPw;
    }





}
