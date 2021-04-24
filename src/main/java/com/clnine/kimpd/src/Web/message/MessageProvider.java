package com.clnine.kimpd.src.Web.message;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponseStatus;
import com.clnine.kimpd.src.Web.category.CategoryProvider;
import com.clnine.kimpd.src.Web.message.models.GetMessageRes;
import com.clnine.kimpd.src.Web.message.models.GetMessagesDTO;
import com.clnine.kimpd.src.Web.message.models.GetMessagesRes;
import com.clnine.kimpd.src.Web.message.models.Message;
import com.clnine.kimpd.src.Web.user.UserInfoProvider;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.NOT_FOUND_MESSAGE;
import static com.clnine.kimpd.config.BaseResponseStatus.NOT_USER_MESSAGE;

@Service
@RequiredArgsConstructor
public class MessageProvider {
    private final MessageRepository messageRepository;
    private final CategoryProvider categoryProvider;
    private final UserInfoProvider userInfoProvider;

    public GetMessageRes getMessage(int messageIdx,int userIdx) throws BaseException {
        Message message = messageRepository.findByMessageIdxAndStatus(messageIdx,"ACTIVE");
        if(message==null){
            throw new BaseException(NOT_FOUND_MESSAGE);
        }
        if(message.getReceiver().getUserIdx()!=userIdx){
            throw new BaseException(NOT_USER_MESSAGE);
        }
        message.setReadStatus(1);
        messageRepository.save(message);
        int senderIdx = message.getSender().getUserIdx();
        String senderNickname = message.getSender().getNickname();
        String description = message.getDescription();
        Date sendTimeDateForm = message.getCreatedAt();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd[hh:mm]");
        String sendTime = simpleDateFormat.format(sendTimeDateForm);

        GetMessageRes getMessageRes = new GetMessageRes(messageIdx, senderIdx,senderNickname, sendTime, description);
        return getMessageRes;
    }


    public GetMessagesRes getMessages(int userIdx, int page, int size) throws BaseException{
        UserInfo receiverInfo = userInfoProvider.retrieveUserInfoByUserIdx(userIdx); //쪽지 받은 사람은 userIdx

        Pageable pageable = PageRequest.of(page-1,size, Sort.by(Sort.Direction.DESC,"createdAt"));
        List<GetMessagesDTO> getMessagesDTOList = new ArrayList<>();

        List<Message> messageList = messageRepository.findByReceiverAndStatus(receiverInfo,"ACTIVE",pageable);
        int totalCount = messageRepository.countAllByReceiverAndStatus(receiverInfo,"ACTIVE");
        for(Message message : messageList){
            UserInfo sender = message.getSender();

            int messageIdx = message.getMessageIdx();
            int senderIdx = sender.getUserIdx();
            String senderNickname = sender.getNickname(); //쪽지 보낸 사람 닉네임
            String senderProfileImageUrl = sender.getProfileImageURL();

            String senderJobName = "";
            if(sender.getUserType()==1 || sender.getUserType()==2 || sender.getUserType()==3){
                senderJobName ="";
            }else{
                senderJobName = categoryProvider.getMainJobCategoryChild(sender);//쪽지 보낸 사람 직종 이름
            }

            Date sendTimeDateForm = message.getCreatedAt();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd[HH]:mm]");
            String sendTime = simpleDateFormat.format(sendTimeDateForm); //쪽지 보낸 시간
            String description = message.getDescription();
            int readStatus = message.getReadStatus();
            GetMessagesDTO getMessagesDTO = new GetMessagesDTO(messageIdx,senderIdx,senderNickname,senderProfileImageUrl,senderJobName,sendTime,description,readStatus);
            getMessagesDTOList.add(getMessagesDTO);
        }
        GetMessagesRes getMessagesRes = new GetMessagesRes(totalCount,getMessagesDTOList);
        return getMessagesRes;
    }
}
