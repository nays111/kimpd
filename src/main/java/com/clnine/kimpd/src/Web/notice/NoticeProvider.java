package com.clnine.kimpd.src.Web.notice;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.notice.models.GetNoticesDTO;
import com.clnine.kimpd.src.Web.notice.models.GetNoticesRes;
import com.clnine.kimpd.src.Web.notice.models.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.FAIlED_TO_GET_NOTICE;

@Service
@RequiredArgsConstructor
public class NoticeProvider {

    private final NoticeRepository noticeRepository;


    public GetNoticesRes getNoticeList(int page, int size) throws BaseException {
        Pageable pageable = PageRequest.of(page-1,size, Sort.by(Sort.Direction.DESC,"noticeIdx"));
        List<Notice> noticeList;
        int totalCount = 0;
        try{
            noticeList = noticeRepository.findAllByStatus("ACTIVE",pageable);
            totalCount = noticeRepository.countAllByStatus("ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(FAIlED_TO_GET_NOTICE);
        }

        List<GetNoticesDTO> getNoticesDTOList = new ArrayList<>();
        for(int i=0;i<noticeList.size();i++){
            int noticeIdx = noticeList.get(i).getNoticeIdx();//1
            String noticeTitle = noticeList.get(i).getNoticeTitle();//2
            String noticeDescription = noticeList.get(i).getNoticeDescription();//3

            Date createdAt = noticeList.get(i).getCreatedAt();
            SimpleDateFormat sDate = new SimpleDateFormat("yy/MM/dd");
            String createdDate = sDate.format(createdAt);//4
            GetNoticesDTO getNoticesDTO = new GetNoticesDTO(noticeIdx,noticeTitle,noticeDescription,createdDate);
            getNoticesDTOList.add(getNoticesDTO);
        }
        GetNoticesRes getNoticesRes = new GetNoticesRes(totalCount,getNoticesDTOList);

        return getNoticesRes;
    }
}
