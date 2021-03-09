package com.clnine.kimpd.src.Web.faq;

import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.faq.models.Faq;
import com.clnine.kimpd.src.Web.faq.models.GetFaqsDTO;
import com.clnine.kimpd.src.Web.faq.models.GetFaqsRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.FAIlED_TO_GET_FAQ;

@Service
@RequiredArgsConstructor
public class FaqProvider {
    private final FaqRepository faqRepository;
    public GetFaqsRes getFaqList(int page, int size) throws BaseException {
        Pageable pageable = PageRequest.of(page-1,size, Sort.by(Sort.Direction.DESC,"faqIdx"));
        List<Faq> faqList = new ArrayList<>();
        int totalCount = 0;
        try{
            faqList = faqRepository.findAllByStatus("ACTIVE",pageable);
            totalCount = faqRepository.countAllByStatus("ACTIVE");
        }catch(Exception ignored){
            throw new BaseException(FAIlED_TO_GET_FAQ);
        }
        List<GetFaqsDTO> getFaqsDTOList = new ArrayList<>();
        for(int i=0;i<faqList.size();i++){
            int faqIdx = faqList.get(i).getFaqIdx();//1
            String faqQuestion = faqList.get(i).getFaqQuestion();//2
            faqQuestion = "Q."+faqQuestion;
            String faqAnswer = faqList.get(i).getFaqAnswer();//3
            GetFaqsDTO getFaqsDTO = new GetFaqsDTO(faqIdx,faqQuestion,faqAnswer);
            getFaqsDTOList.add(getFaqsDTO);
        }
        GetFaqsRes getFaqsRes = new GetFaqsRes(totalCount,getFaqsDTOList);

        return getFaqsRes;
    }


}
