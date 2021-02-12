package com.clnine.kimpd.src.Web.message;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.config.BaseResponse;
import com.clnine.kimpd.src.Web.message.models.GetMessageRes;
import com.clnine.kimpd.src.Web.message.models.GetMessagesRes;
import com.clnine.kimpd.src.Web.message.models.PatchMessageStatusReq;
import com.clnine.kimpd.src.Web.message.models.PostMessageReq;
import com.clnine.kimpd.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final JwtService jwtService;
    private final MessageProvider messageProvider;

    /**
     * 쪽지 전송 API
     * @param userIdx
     * @param postMessageReq
     * @return
     */
    @ResponseBody
    @PostMapping("/messages/{userIdx}")
    public BaseResponse<String> postMessage(@PathVariable(required = true,value = "userIdx")int userIdx,
                                            @RequestBody PostMessageReq postMessageReq){
        int senderIdx;
        try{
            senderIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        if(postMessageReq.getDescription()==null || postMessageReq.getDescription().length()==0){
            return new BaseResponse<>(EMPTY_MESSAGE_DESCRIPTION);
        }
        if(postMessageReq.getDescription().length()>1000){
            return new BaseResponse<>(TOO_LONG_MESSAGE_DESCRIPTION);
        }
        try{
            messageService.postMessage(senderIdx,userIdx,postMessageReq);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 쪽지 삭제 API
     * @param patchMessageStatusReqList
     * @return
     */
    @ResponseBody
    @PatchMapping("/messages/status")
    public BaseResponse<String> deleteMessage(@RequestBody List<PatchMessageStatusReq> patchMessageStatusReqList){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            messageService.deleteMessage(patchMessageStatusReqList);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 쪽지 조회 API
     * @param messageIdx
     * @return
     */
    @ResponseBody
    @GetMapping("/messages/{messageIdx}")
    public BaseResponse<GetMessageRes> getMessage(@PathVariable(required = true,value = "messageIdx")int messageIdx){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            GetMessageRes getMessageRes = messageProvider.getMessage(messageIdx);
            return new BaseResponse<>(SUCCESS,getMessageRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 쪽지 리스트 조회 API
     * @return
     */
    @ResponseBody
    @GetMapping("/messages")
    public BaseResponse<List<GetMessagesRes>> getMessages(@RequestParam(value="page",required = true)int page){
        int userIdx;
        try{
            userIdx = jwtService.getUserIdx();
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
        try{
            List<GetMessagesRes> getMessagesResList = messageProvider.getMessages(userIdx,page,3);
            return new BaseResponse<>(SUCCESS,getMessagesResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}