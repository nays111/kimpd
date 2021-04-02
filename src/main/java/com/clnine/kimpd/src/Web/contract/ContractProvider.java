package com.clnine.kimpd.src.Web.contract;


import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.contract.models.Contract;
import com.clnine.kimpd.src.Web.contract.models.GetContractRes;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

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
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ContractProvider {
    private final CastingRepository castingRepository;
    private final ContractRepository contractRepository;
    public GetContractRes getContract(int userIdx,int castingIdx) throws BaseException{
        Casting casting = castingRepository.findAllByCastingIdxAndStatus(castingIdx,"ACTIVE");
        if(casting==null){
            throw new BaseException(FAILED_TO_GET_CASTING);
        }
        if(casting.getUserInfo().getUserIdx()!=userIdx){
            throw new BaseException(NO_CASTING);
        }
        GetContractRes getContractRes = new GetContractRes(casting.getContractFileUrl());
        return getContractRes;
    }

    public String makepdf(Casting casting,int contractIdx) throws IOException {

        //todo 가장 최근 계약서를 가져옴
        Contract contract = contractRepository.findByContractIdx(contractIdx);

//        String castingUserName = casting.getUserInfo().getName();
//        String expertName = casting.getExpert().getName();
//        String projectName = casting.getProject().getProjectName();


        //todo html 변수 바꾸기
        //계약서 html (string)
        String BODY = contract.getContractContent();
//        BODY=BODY.replace("표준계약서","팀");
//        BODY=BODY.replace("표준계약서","팀");
//        BODY=BODY.replace("표준계약서","팀");
//        BODY=BODY.replace("표준계약서","팀");
//        BODY=BODY.replace("표준계약서","팀");
//        BODY=BODY.replace("표준계약서","팀");
//        BODY=BODY.replace("표준계약서","팀");
//        BODY=BODY.replace("표준계약서","팀");
//        BODY=BODY.replace("표준계약서","팀");
//        BODY=BODY.replace("표준계약서","팀");
//        BODY=BODY.replace("표준계약서","팀");
//        BODY=BODY.replace("표준계약서","팀");
//        BODY=BODY.replace("표준계약서","팀");


        //한국어를 표시하기 위해 폰트 적용
        //todo 폰트 파일 경로 서버로 수정 필요
        String FONT = "src/main/resources/static/MalgunGothic.TTF";
        //ConverterProperties : htmlconverter의 property를 지정하는 메소드인듯
        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        FontProgram fontProgram = FontProgramFactory.createFont(FONT);
        fontProvider.addFont(fontProgram);
        properties.setFontProvider(fontProvider);

        String filename = "contract.pdf";
        String storePathString = "src/main/resources/static/";
        String realName = storePathString;
        realName+=filename;

        //pdf 페이지 크기를 조정
        List<IElement> elements = HtmlConverter.convertToElements(BODY, properties);
        PdfDocument pdf = new PdfDocument(new PdfWriter(realName));
        Document document = new Document(pdf);
        //setMargins 매개변수순서 : 상, 우, 하, 좌
        document.setMargins(50, 50, 50, 50);
        for (IElement element : elements) {
            document.add((IBlockElement) element);
        }
        document.close();

        Path path = Paths.get(realName);
        String contentType = "application/pdf";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile result = new MockMultipartFile(filename,filename, contentType, content);
        String str = upload(result);
        return str;
    }

    public String uploadFile(File file, String fileName) throws IOException {

        String tokenName = fileName;
        BlobId blobId = BlobId.of("kimpd-2ad1d.appspot.com", "ContractFile/"+fileName);

        Map<String,String> map = new HashMap<>();
        map.put("firebaseStorageDownloadTokens", tokenName);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/pdf").setMetadata(map).build();
        System.out.println("b:"+blobInfo.getBucket());

        //todo 서버 경로로 수정
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/java/com/clnine/kimpd/config/secret/kimpd-2ad1d-firebase-adminsdk-ybxoh-c4cd6bdf89.json"));

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));


        return String.format("https://firebasestorage.googleapis.com/v0/b/kimpd-2ad1d.appspot.com/o/ContractFile%%2F%s?alt=media&token=%s", URLEncoder.encode(fileName, StandardCharsets.UTF_8),URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }


    public String upload(MultipartFile multipartFile) throws IOException {

//        try {

        String fileName = multipartFile.getOriginalFilename();// to get original file name
        System.out.println(fileName);
        fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.
        System.out.println(fileName);

        File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File



        String TEMP_URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
        //TEMP_URL="ContractFile/"+TEMP_URL;
        file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
        //return TEMP_URL;                     // Your customized response
        //} catch (Exception e) {
        //e.printStackTrace();
        return TEMP_URL;
    }



}
