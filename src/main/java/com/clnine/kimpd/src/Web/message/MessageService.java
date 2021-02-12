package com.clnine.kimpd.src.Web.message;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.message.models.Message;
import com.clnine.kimpd.src.Web.message.models.PatchMessageStatusReq;
import com.clnine.kimpd.src.Web.message.models.PostMessageReq;
import com.clnine.kimpd.src.Web.user.UserInfoRepository;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserInfoRepository userInfoRepository;
    private final MessageRepository messageRepository;
    public void postMessage(int senderIdx, int receiverIdx, PostMessageReq postMessageReq) throws BaseException {
        UserInfo senderInfo;
        try {
            senderInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(senderIdx,"ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        UserInfo receiverInfo;
        try {
            receiverInfo = userInfoRepository.findUserInfoByUserIdxAndStatus(receiverIdx,"ACTIVE");
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_USER);
        }
        String description = postMessageReq.getDescription();
        Message message = new Message(senderInfo,receiverInfo,description);
        messageRepository.save(message);
    }

    public void deleteMessage(PatchMessageStatusReq patchMessageStatusReq) throws BaseException{

        //System.out.println(patchMessageStatusReqList.size());
        //System.out.println(patchMessageStatusReqList.get(0).getMessageIdx());


        for(int i=0;i<patchMessageStatusReq.getMessageIdx().size();i++){
            int messageIdx = patchMessageStatusReq.getMessageIdx().get(i);
            System.out.println(messageIdx);
            Message message;
            try{
                message = messageRepository.findByMessageIdxAndStatus(messageIdx,"ACTIVE");
            }catch(Exception ignored){
                throw new BaseException(BaseResponseStatus.NOT_FOUND_MESSAGE);
            }
            message.setStatus("INACTIVE");
            messageRepository.save(message);
        }
    }
}
