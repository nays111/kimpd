package com.clnine.kimpd.src.Web.contract;


import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.contract.models.Contract;
import com.clnine.kimpd.src.Web.contract.models.GetContractRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_CASTING;
import static com.clnine.kimpd.config.BaseResponseStatus.FAILED_TO_GET_CONTRACT;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.font.FontProvider;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractProvider {
    private final CastingRepository castingRepository;
    private final ContractRepository contractRepository;
//    public GetContractRes getContractRes(int castingIdx) throws BaseException{
//        Contract contract;
//        Casting casting;
//        try{
//            casting = castingRepository.findAllByCastingIdxAndStatus(castingIdx,"ACTIVE");
//        }catch (Exception ignored) {
//            throw new BaseException(FAILED_TO_GET_CASTING);
//        }
//        try{
//            contract = contractRepository.findByCastingAndStatus(casting,"ACTIVE");
//        }catch (Exception ignored) {
//            throw new BaseException(FAILED_TO_GET_CONTRACT);
//        }
//        GetContractRes getContractRes = new GetContractRes(contract.getContractIdx(), contract.getContractFileURL());
//        return getContractRes;
//
//    }




    //BODY : html string , dest : pdf를 만들 경로(D:\\sample.pdf)
    public void makepdf(int contractIdx, String dest) throws IOException {

        Contract contract = contractRepository.findByContractIdx(contractIdx);
        String BODY = contract.getContractContent();
        //한국어를 표시하기 위해 폰트 적용
        String FONT = "src/main/resources/static/MalgunGothic.TTF";
        //ConverterProperties : htmlconverter의 property를 지정하는 메소드인듯
        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        FontProgram fontProgram = FontProgramFactory.createFont(FONT);
        fontProvider.addFont(fontProgram);
        properties.setFontProvider(fontProvider);

        //pdf 페이지 크기를 조정
        List<IElement> elements = HtmlConverter.convertToElements(BODY, properties);
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdf);
        //setMargins 매개변수순서 : 상, 우, 하, 좌
        document.setMargins(50, 0, 50, 0);
        for (IElement element : elements) {
            document.add((IBlockElement) element);
        }
        document.close();
    }
}
