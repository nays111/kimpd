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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    public String NumberToKor(String amt) {

        String amt_msg = "";
        String[] arrayNum = {"", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
        String[] arrayUnit = {"", "십", "백", "천", "만", "십만", "백만", "천만", "억", "십억", "백업", "천억", "조", "십조", "백조", "천조", "경", "십경", "백경", "천경", "해", "십해", "백해", "천해"};

        if (amt.length() > 0) {
            int len = amt.length();

            String[] arrayStr = new String[len];
            String hanStr = "";
            String tmpUnit = "";
            for (int i = 0; i < len; i++) {
                arrayStr[i] = amt.substring(i, i + 1);
            }
            int code = len;
            for (int i = 0; i < len; i++) {
                code--;
                tmpUnit = "";
                if (arrayNum[Integer.parseInt(arrayStr[i])] != "") {
                    tmpUnit = arrayUnit[code];
                    if (code > 4) {
                        if ((Math.floor(code / 4) == Math.floor((code - 1) / 4)
                                && arrayNum[Integer.parseInt(arrayStr[i + 1])] != "") ||
                                (Math.floor(code / 4) == Math.floor((code - 2) / 4)
                                        && arrayNum[Integer.parseInt(arrayStr[i + 2])] != "")) {
                            tmpUnit = arrayUnit[code].substring(0, 1);
                        }
                    }
                }
                hanStr += arrayNum[Integer.parseInt(arrayStr[i])] + tmpUnit;
            }
            amt_msg = hanStr;
        } else {
            amt_msg = amt;
        }

        return amt_msg;
    }
    public String transformDateFrom(String dateForm){
        dateForm = dateForm.replaceFirst("[.]","년 ");
        dateForm = dateForm.replaceFirst("[.]","월 ");
        dateForm = dateForm+"일";
        return dateForm;
    }

    public String makepdf(Casting casting,int contractIdx) throws IOException {

        //todo 가장 최근 계약서를 가져옴
        Contract contract = contractRepository.findByContractIdx(contractIdx);

        DateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String dateToStr = dateFormat.format(casting.getUpdatedAt());

        String castingPrice = NumberToKor(casting.getCastingPrice());



        String BODY = contract.getContractContent();
        BODY=BODY.replace("${userName}",casting.getUserInfo().getName());
        BODY=BODY.replace("${expertName}",casting.getExpert().getName());
        BODY=BODY.replace("${projectName}",casting.getProject().getProjectName());
        BODY=BODY.replace("${projectDescription}",casting.getProject().getProjectDescription());
        BODY=BODY.replace("${castingStartDate}",transformDateFrom(casting.getCastingStartDate()));
        BODY=BODY.replace("${castingEndDate}",transformDateFrom(casting.getCastingEndDate()));
        BODY=BODY.replace("${castingPriceDate}",transformDateFrom(casting.getCastingPriceDate()));
        BODY=BODY.replace("${castingPrice}",castingPrice);
        BODY=BODY.replace("${updatedAt}",dateToStr);

        if(casting.getUserInfo().getUserType()==1 || casting.getUserInfo().getUserType()==4){
            BODY = BODY.replace("${priceType}","(세금 포함)");
        }else{
            BODY = BODY.replace("${priceType}","(VAT 포함)");
        }


        //위탁자 (전문가)
        BODY = BODY.replace("${address1}",casting.getExpert().getAddress());
        if(casting.getExpert().getUserType()==4){
            BODY = BODY.replace("${name1}",casting.getExpert().getName());
            BODY = BODY.replace("${inputNameTitleByUserType1}","성명");
            BODY = BODY.replace("${userType1}","<개인 전문가회원>");
        }else if(casting.getExpert().getUserType()==5){
            BODY = BODY.replace("${name1}",casting.getExpert().getPrivateBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType1}","상호");
            BODY = BODY.replace("${userType1}","<개인/법인사업자 전문가회원>");
        }else if(casting.getExpert().getUserType()==6){
            BODY = BODY.replace("${name1}",casting.getExpert().getCorporationBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType1}","상호");
            BODY = BODY.replace("${userType1}","<개인/법인사업자 전문가회원>");
        }


        //수탁자 (일반인 + 전문가)
        BODY = BODY.replace("${address2}",casting.getUserInfo().getAddress());
        if(casting.getUserInfo().getUserType()==5){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getPrivateBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","상호");
            BODY = BODY.replace("${userType2}","<제작사회원>");
        }else if(casting.getUserInfo().getUserType()==6){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getCorporationBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","상호");
            BODY = BODY.replace("${userType2}","<제작사회원>");
        }else if(casting.getUserInfo().getUserType()==4){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","성명");
            BODY = BODY.replace("${userType2}","<일반회원>");
        }else if(casting.getUserInfo().getUserType()==3){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getCorporationBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","상호");
            BODY = BODY.replace("${userType2}","<제작사회원>");
        }else if(casting.getUserInfo().getUserType()==2){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getPrivateBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","상호");
            BODY = BODY.replace("${userType2}","<제작사회원>");
        }else if(casting.getUserInfo().getUserType()==1){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","성명");
            BODY = BODY.replace("${userType2}","<일반회원>");
        }

        //한국어를 표시하기 위해 폰트 적용

        //Local 주소
        //String FONT = "src/main/resources/static/MalgunGothic.TTF";

        //서버 주소
        String FONT = "/var/www/html/kimpd/files/MalgunGothic.TTF";


        //ConverterProperties : htmlconverter의 property를 지정하는 메소드인듯
        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        FontProgram fontProgram = FontProgramFactory.createFont(FONT);
        fontProvider.addFont(fontProgram);
        properties.setFontProvider(fontProvider);

        String filename = "contract.pdf";


        //Local 주소
        //String storePathString = "src/main/resources/static/";

        //Server 주소
        String storePathString = "/var/www/html/kimpd/files/";
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

        //Local 주소
         //Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/java/com/clnine/kimpd/config/secret/kimpd-2ad1d-firebase-adminsdk-ybxoh-c4cd6bdf89.json"));

        //Server 주소소
       Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("/var/www/html/kimpd/files/kimpd-2ad1d-firebase-adminsdk-ybxoh-c4cd6bdf89.json"));

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

        String fileName = multipartFile.getOriginalFilename();//to get original file name
        fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));//to generated random string values for file name.
        File file = this.convertToFile(multipartFile, fileName);// to convert multipartFile to File

        String TEMP_URL = this.uploadFile(file, fileName);// to get uploaded file link
        //TEMP_URL="ContractFile/"+TEMP_URL;
        file.delete();// to delete the copy of uploaded file stored in the project folder

        return TEMP_URL;
    }



}
