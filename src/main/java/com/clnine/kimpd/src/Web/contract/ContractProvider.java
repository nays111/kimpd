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
import com.itextpdf.html2pdf.css.apply.ICssApplierFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.Leading;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
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
import java.util.List;

import static com.clnine.kimpd.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ContractProvider {
    private final CastingRepository castingRepository;
    private final ContractRepository contractRepository;

    /**
     * 계약서 조회
     */
    public GetContractRes getContract(int userIdx,int castingIdx) throws BaseException{

        Casting casting = castingRepository.findAllByCastingIdxAndStatus(castingIdx,"ACTIVE");

        if(casting==null){
            throw new BaseException(FAILED_TO_GET_CASTING);
        }
        if(casting.getUserInfo().getUserIdx()!=userIdx && casting.getExpert().getUserIdx()!=userIdx){
            throw new BaseException(NO_CASTING);
        }

        GetContractRes getContractRes = new GetContractRes(casting.getContractFileUrl());
        return getContractRes;
    }

    /**
     * 숫자를 한글로 변환하는 함수
     */
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

    /**
     * 날짜 형식 치환 함수
     */
    public String transformDateFrom(String dateForm){
        dateForm = dateForm.replaceFirst("[.]","년 ");
        dateForm = dateForm.replaceFirst("[.]","월 ");
        dateForm = dateForm+"일";
        return dateForm;
    }

    /**
     * html 형식의 계약서를 pdf로 전환
     */
    public String makepdf(Casting casting,int contractIdx) throws IOException,BaseException {

        /**
         * 계약서 가져오기
         */
        Contract contract = contractRepository.findByContractIdx(contractIdx);

        /**
         * 날짜 형식 치환
         */
        DateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String dateToStr = dateFormat.format(casting.getUpdatedAt());

        /**
         * castingPrice를 한글 형식으로 반환
         */
        String castingPrice = NumberToKor(casting.getCastingPrice());

        /**
         * html 변수 치환
         */
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
            BODY = BODY.replace("${userType1}","개인 전문가회원");
        }else if(casting.getExpert().getUserType()==5){
            BODY = BODY.replace("${name1}",casting.getExpert().getPrivateBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType1}","상호");
            BODY = BODY.replace("${userType1}","개인/법인사업자 전문가회원");
        }else if(casting.getExpert().getUserType()==6){
            BODY = BODY.replace("${name1}",casting.getExpert().getCorporationBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType1}","상호");
            BODY = BODY.replace("${userType1}","개인/법인사업자 전문가회원");
        }


        //수탁자 (일반인 + 전문가)
        BODY = BODY.replace("${address2}",casting.getUserInfo().getAddress());
        if(casting.getUserInfo().getUserType()==5){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getPrivateBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","상호");
            BODY = BODY.replace("${userType2}","제작사회원");
        }else if(casting.getUserInfo().getUserType()==6){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getCorporationBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","상호");
            BODY = BODY.replace("${userType2}","제작사회원");
        }else if(casting.getUserInfo().getUserType()==4){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","성명");
            BODY = BODY.replace("${userType2}","일반회원");
        }else if(casting.getUserInfo().getUserType()==3){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getCorporationBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","상호");
            BODY = BODY.replace("${userType2}","제작사회원");
        }else if(casting.getUserInfo().getUserType()==2){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getPrivateBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","상호");
            BODY = BODY.replace("${userType2}","제작사회원");
        }else if(casting.getUserInfo().getUserType()==1){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","성명");
            BODY = BODY.replace("${userType2}","일반회원");
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

        /**
         * 위에서 설정한 경로에 pdfdocument 생성
         */
        PdfDocument pdf = new PdfDocument(new PdfWriter(realName));
        pdf.setDefaultPageSize(PageSize.A4);


        Document document = new Document(pdf);

        //setMargins 매개변수순서 : 상, 우, 하, 좌
        document.setMargins(50, 50, 50, 50);
        document.setFontSize(12);

        int i=0;
        for (IElement element : elements) {
            BlockElement blockElement = (BlockElement) element;

            /**
             * 행간 조정
             */
            blockElement.setMargins(1,0,1,0);

            /**
             * "계약서" bold체 적용, 가운데로 조정, 테두리 적용
             */
            if(i==0){
                blockElement.setBold();
                blockElement.setFontSize(15);
                blockElement.setTextAlignment(TextAlignment.CENTER);
                blockElement.setBorder(new SolidBorder(1));
            }
            /**
             * "조항(1조~~~)" bold체 적용
             */
            if(i==4 || i==7 || i==12 || i==16 || i==23 || i==26 ||i==30 || i==34 || i==37 || i==40 || i==48 || i==54){
                blockElement.setBold();
            }
            /**
             * 문저 작성 날짜 bold체 적용, 가운데로 조정
             */
            if(i==45){
                blockElement.setBold();
                blockElement.setTextAlignment(TextAlignment.CENTER);
            }
            document.add(blockElement);
            i++;
        }


        document.setBorderTop(new SolidBorder(1));
        document.setBorderRight(new SolidBorder(1));
        document.setBorderLeft(new SolidBorder(1));
        document.setBorderBottom(new SolidBorder(1));
        document.setBorder(new SolidBorder(1F));

        document.close();

        /**
         * multipartfile로 변환
         */
        Path path = Paths.get(realName);
        String contentType = "application/pdf";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile result = new MockMultipartFile(filename,filename, contentType, content);

        /**
         * 파이어베이스에 multipartfile 업로드
         */
        String str = upload(result);
        return str;
    }

    public String uploadFile(File file, String fileName) throws IOException {

        String tokenName = fileName;
        /**
         * firebase storage bucket 추가, 저장 경로 지정
         */
        BlobId blobId = BlobId.of("kimpd-2ad1d.appspot.com", "ContractFile/"+fileName);

        /**
         * downloadtoken 생성
         */
        Map<String,String> map = new HashMap<>();
        map.put("firebaseStorageDownloadTokens", tokenName);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/pdf").setMetadata(map).build();

        /**
         * firebase admin sdk 사용을 위한 인증 credentials (json파일) 저장 경로
         */
        //Local 주소
        // Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/java/com/clnine/kimpd/config/secret/kimpd-2ad1d-firebase-adminsdk-ybxoh-c4cd6bdf89.json"));

        //Server 주소소
       Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("/var/www/html/kimpd/files/kimpd-2ad1d-firebase-adminsdk-ybxoh-c4cd6bdf89.json"));

        /**
         * firebase storage 가져오기
         */
       Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
       storage.create(blobInfo, Files.readAllBytes(file.toPath()));


        /**
         * 계약서 생성 경로 반환 (경로를 casting/contractFileUrl에 저장)
         */
        return String.format("https://firebasestorage.googleapis.com/v0/b/kimpd-2ad1d.appspot.com/o/ContractFile%%2F%s?alt=media&token=%s", URLEncoder.encode(fileName, StandardCharsets.UTF_8),URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    /**
     * multipartfile을 file로 변환
     */
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


    /**
     * multipartfile을 uuid를 부여하고 업로드 (부여받은 uuid로 만든 url 반환)
     */
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
