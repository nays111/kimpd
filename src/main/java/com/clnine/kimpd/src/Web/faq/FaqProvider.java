package com.clnine.kimpd.src.Web.faq;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.faq.models.Faq;
import com.clnine.kimpd.src.Web.faq.models.GetFaqsRes;
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

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_PROJECTS;
import static com.clnine.kimpd.config.BaseResponseStatus.FAIlED_TO_GET_FAQ;

@Service
@RequiredArgsConstructor
public class FaqProvider {

    private final FaqRepository faqRepository;

    public List<GetFaqsRes> getFaqList(int page, int size) throws BaseException {
        Pageable pageable = PageRequest.of(page-1,size, Sort.by(Sort.Direction.DESC,"faqIdx"));
        List<Faq> faqList;
        try{
            faqList = faqRepository.findAllByStatus("ACTIVE",pageable);
        }catch(Exception ignored){
            throw new BaseException(FAIlED_TO_GET_FAQ);
        }

        List<GetFaqsRes> getFaqsResList = new ArrayList<>();
        for(int i=0;i<faqList.size();i++){
            int faqIdx = faqList.get(i).getFaqIdx();//1
            String faqQuestion = faqList.get(i).getFaqQuestion();//2
            faqQuestion = "Q."+faqQuestion;
            String faqAnswer = faqList.get(i).getFaqAnswer();//3
            GetFaqsRes getFaqsRes = new GetFaqsRes(faqIdx,faqQuestion,faqAnswer);
            getFaqsResList.add(getFaqsRes);
        }

        return getFaqsResList;
    }


}
