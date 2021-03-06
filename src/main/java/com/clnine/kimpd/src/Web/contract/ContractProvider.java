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
     * ????????? ??????
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
     * ????????? ????????? ???????????? ??????
     */
    public String NumberToKor(String amt) {

        String amt_msg = "";
        String[] arrayNum = {"", "???", "???", "???", "???", "???", "???", "???", "???", "???"};
        String[] arrayUnit = {"", "???", "???", "???", "???", "??????", "??????", "??????", "???", "??????", "??????", "??????", "???", "??????", "??????", "??????", "???", "??????", "??????", "??????", "???", "??????", "??????", "??????"};

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
     * ?????? ?????? ?????? ??????
     */
    public String transformDateFrom(String dateForm){
        dateForm = dateForm.replaceFirst("[.]","??? ");
        dateForm = dateForm.replaceFirst("[.]","??? ");
        dateForm = dateForm+"???";
        return dateForm;
    }

    /**
     * html ????????? ???????????? pdf??? ??????
     */
    public String makepdf(Casting casting,int contractIdx) throws IOException,BaseException {

        /**
         * ????????? ????????????
         */
        Contract contract = contractRepository.findByContractIdx(contractIdx);

        /**
         * ?????? ?????? ??????
         */
        DateFormat dateFormat = new SimpleDateFormat("yyyy??? MM??? dd???");
        String dateToStr = dateFormat.format(casting.getUpdatedAt());

        /**
         * castingPrice??? ?????? ???????????? ??????
         */
        String castingPrice = NumberToKor(casting.getCastingPrice());

        /**
         * html ?????? ??????
         */
        String BODY = contract.getContractContent();
        BODY=BODY.replace("${userName}",casting.getUserInfo().getName());
        BODY=BODY.replace("${expertName}",casting.getExpert().getName());
        BODY=BODY.replace("${projectName}",casting.getProject().getProjectName());
        BODY=BODY.replace("${projectDescription}",casting.getCastingWork()); //projectDescription?????? castingWork??? ??????
        BODY=BODY.replace("${castingStartDate}",transformDateFrom(casting.getCastingStartDate()));
        BODY=BODY.replace("${castingEndDate}",transformDateFrom(casting.getCastingEndDate()));
        BODY=BODY.replace("${castingPriceDate}",transformDateFrom(casting.getCastingPriceDate()));
        BODY=BODY.replace("${castingPrice}",castingPrice);
        BODY=BODY.replace("${updatedAt}",dateToStr);

        if(casting.getUserInfo().getUserType()==1 || casting.getUserInfo().getUserType()==4){
            BODY = BODY.replace("${priceType}","(?????? ??????)");
        }else{
            BODY = BODY.replace("${priceType}","(VAT ??????)");
        }


        //????????? (?????????)
        BODY = BODY.replace("${address1}",casting.getExpert().getAddress());
        if(casting.getExpert().getUserType()==4){
            BODY = BODY.replace("${name1}",casting.getExpert().getName());
            BODY = BODY.replace("${inputNameTitleByUserType1}","??????");
            BODY = BODY.replace("${userType1}","?????? ???????????????");
        }else if(casting.getExpert().getUserType()==5){
            BODY = BODY.replace("${name1}",casting.getExpert().getPrivateBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType1}","??????");
            BODY = BODY.replace("${userType1}","??????/??????????????? ???????????????");
        }else if(casting.getExpert().getUserType()==6){
            BODY = BODY.replace("${name1}",casting.getExpert().getCorporationBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType1}","??????");
            BODY = BODY.replace("${userType1}","??????/??????????????? ???????????????");
        }


        //????????? (????????? + ?????????)
        BODY = BODY.replace("${address2}",casting.getUserInfo().getAddress());
        if(casting.getUserInfo().getUserType()==5){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getPrivateBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","??????");
            BODY = BODY.replace("${userType2}","???????????????");
        }else if(casting.getUserInfo().getUserType()==6){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getCorporationBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","??????");
            BODY = BODY.replace("${userType2}","???????????????");
        }else if(casting.getUserInfo().getUserType()==4){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","??????");
            BODY = BODY.replace("${userType2}","????????????");
        }else if(casting.getUserInfo().getUserType()==3){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getCorporationBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","??????");
            BODY = BODY.replace("${userType2}","???????????????");
        }else if(casting.getUserInfo().getUserType()==2){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getPrivateBusinessName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","??????");
            BODY = BODY.replace("${userType2}","???????????????");
        }else if(casting.getUserInfo().getUserType()==1){
            BODY = BODY.replace("${name2}",casting.getUserInfo().getName());
            BODY = BODY.replace("${inputNameTitleByUserType2}","??????");
            BODY = BODY.replace("${userType2}","????????????");
        }

        //???????????? ???????????? ?????? ?????? ??????
        //Local ??????
        //String FONT = "src/main/resources/static/MalgunGothic.TTF";


        //?????? ??????
        String FONT = "/var/www/html/kimpd/files/MalgunGothic.TTF";


        //ConverterProperties : htmlconverter??? property??? ???????????? ???????????????
        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        FontProgram fontProgram = FontProgramFactory.createFont(FONT);
        fontProvider.addFont(fontProgram);
        properties.setFontProvider(fontProvider);

        String filename = "contract.pdf";

        //Local ??????
        //String storePathString = "src/main/resources/static/";

        //Server ??????
        String storePathString = "/var/www/html/kimpd/files/";
        String realName = storePathString;
        realName+=filename;

        //pdf ????????? ????????? ??????
        List<IElement> elements = HtmlConverter.convertToElements(BODY, properties);

        /**
         * ????????? ????????? ????????? pdfdocument ??????
         */
        PdfDocument pdf = new PdfDocument(new PdfWriter(realName));
        pdf.setDefaultPageSize(PageSize.A4);


        Document document = new Document(pdf);

        //setMargins ?????????????????? : ???, ???, ???, ???
        document.setMargins(50, 50, 50, 50);
        document.setFontSize(12);

        int i=0;
        for (IElement element : elements) {
            BlockElement blockElement = (BlockElement) element;

            /**
             * ?????? ??????
             */
            blockElement.setMargins(1,0,1,0);

            /**
             * "?????????" bold??? ??????, ???????????? ??????, ????????? ??????
             */
            if(i==0){
                blockElement.setBold();
                blockElement.setFontSize(15);
                blockElement.setTextAlignment(TextAlignment.CENTER);
                blockElement.setBorder(new SolidBorder(1));
            }
            /**
             * "??????(1???~~~)" bold??? ??????
             */
            if(i==4 || i==7 || i==12 || i==16 || i==23 || i==26 ||i==30 || i==34 || i==37 || i==40 || i==48 || i==53){
                blockElement.setBold();
            }
            /**
             * ?????? ?????? ?????? bold??? ??????, ???????????? ??????
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
         * multipartfile??? ??????
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
         * ????????????????????? multipartfile ?????????
         */
        String str = upload(result);
        return str;
    }

    public String uploadFile(File file, String fileName) throws IOException {

        String tokenName = fileName;
        /**
         * firebase storage bucket ??????, ?????? ?????? ??????
         */
        BlobId blobId = BlobId.of("kimpd-2ad1d.appspot.com", "ContractFile/"+fileName);

        /**
         * downloadtoken ??????
         */
        Map<String,String> map = new HashMap<>();
        map.put("firebaseStorageDownloadTokens", tokenName);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/pdf").setMetadata(map).build();

        /**
         * firebase admin sdk ????????? ?????? ?????? credentials (json??????) ?????? ??????
         */
        //Local ??????
         //Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/java/com/clnine/kimpd/config/secret/kimpd-2ad1d-firebase-adminsdk-ybxoh-c4cd6bdf89.json"));

        //Server ?????????
       Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("/var/www/html/kimpd/files/kimpd-2ad1d-firebase-adminsdk-ybxoh-c4cd6bdf89.json"));

        /**
         * firebase storage ????????????
         */
       Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
       storage.create(blobInfo, Files.readAllBytes(file.toPath()));


        /**
         * ????????? ?????? ?????? ?????? (????????? casting/contractFileUrl??? ??????)
         */
        return String.format("https://firebasestorage.googleapis.com/v0/b/kimpd-2ad1d.appspot.com/o/ContractFile%%2F%s?alt=media&token=%s", URLEncoder.encode(fileName, StandardCharsets.UTF_8),URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    /**
     * multipartfile??? file??? ??????
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
     * multipartfile??? uuid??? ???????????? ????????? (???????????? uuid??? ?????? url ??????)
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
