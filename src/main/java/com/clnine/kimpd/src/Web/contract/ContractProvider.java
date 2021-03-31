package com.clnine.kimpd.src.Web.contract;


import com.clnine.kimpd.config.BaseException;
import com.clnine.kimpd.src.Web.casting.CastingRepository;
import com.clnine.kimpd.src.Web.casting.models.Casting;
import com.clnine.kimpd.src.Web.contract.models.Contract;
import com.clnine.kimpd.src.Web.contract.models.GetContractRes;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    public String makepdf(Casting casting,int contractIdx) throws IOException {

        //가장 최근 계약서를 가져옴
        Contract contract = contractRepository.findByContractIdx(contractIdx);






        //계약서 html (string)
        String BODY = contract.getContractContent();
        //한국어를 표시하기 위해 폰트 적용
        String FONT = "src/main/resources/static/MalgunGothic.TTF";
        //ConverterProperties : htmlconverter의 property를 지정하는 메소드인듯
        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        FontProgram fontProgram = FontProgramFactory.createFont(FONT);
        fontProvider.addFont(fontProgram);
        properties.setFontProvider(fontProvider);


        String filename = "contract.pdf";
        String storePathString = "C:/Users/nays1/OneDrive/Desktop/";
        String realName = storePathString;
        realName+=filename;



        //pdf 페이지 크기를 조정
        List<IElement> elements = HtmlConverter.convertToElements(BODY, properties);
        PdfDocument pdf = new PdfDocument(new PdfWriter(realName));
        Document document = new Document(pdf);
        //setMargins 매개변수순서 : 상, 우, 하, 좌
        document.setMargins(50, 50, 50, 50);
        //System.out.println(pdf.)
        for (IElement element : elements) {
            document.add((IBlockElement) element);
        }

        //upload((MultipartFile) document);
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



        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/java/com/clnine/kimpd/config/secret/kimpd-2ad1d-firebase-adminsdk-ybxoh-c4cd6bdf89.json"));

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        System.out.println("s:"+storage);
        //storage.list(blobInfo.getBucket(),Storage.BlobListOption.prefix("ContractFile/"));


        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        System.out.println("f:"+file.toPath());

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
