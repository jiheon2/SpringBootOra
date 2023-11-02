package kopo.poly.controller;

import kopo.poly.dto.OcrDTO;
import kopo.poly.service.IOcrService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import kopo.poly.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value="/ocr")
@RequiredArgsConstructor
@Controller
public class OcrController {
    private final IOcrService ocrService;

    final private String FILE_UPLOAD_SAVE_PATH = "c:/upload"; // c:/upload 폴더에 저장

    // 파일 업로드 화면 호출
    @GetMapping(value="uploadImage")
    public String uploadImage() {

        log.info(this.getClass().getName() + ".uploadImage!");

        return "ocr/uploadImage";
    }

    // 파일 업로드 및 이미지 인식
    @PostMapping(value = "readImage")
    public String readImage(ModelMap model, @RequestParam(value="fileUpload") MultipartFile mf) throws Exception {

        log.info(this.getClass().getName() + ".readImage Start!");

        String res = ""; // OCR 실행결과

        String regId = "Jiheon";
        String chgId = regId;

        String originalFileName = mf.getOriginalFilename(); // 업로드하는 실제 파일명 / 다운로드 구현 시 임의로 정의된 파일명을 되돌리기 위함

        String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1, originalFileName.length()).toLowerCase(); // 파일 확장자 가져오기

        if (ext.equals("jpeg") || ext.equals("jpg") || ext.equals("gif") || ext.equals("png")) { // 이미지 파일만 실행되도록

            String saveFileName = DateUtil.getDateTime("HHmmss") + "." + ext; // 웹서버에 저장되는 파일명 > 다국어 지원이 취약하기에 숫자로 변경하여 저장

            String saveFilePath = FileUtil.mkdirForDate(FILE_UPLOAD_SAVE_PATH); // 업로드 할 폴더 생성

            String fullFileInfo = saveFilePath + "/" + saveFileName;

            log.info("ext : " + ext);
            log.info("saveFIleName : " + saveFileName);
            log.info("saveFilePath : " + saveFilePath);
            log.info("fullFileInfo : " + fullFileInfo);

            mf.transferTo(new File(fullFileInfo)); // 스프링 프레임워크가 제공하는 파일 업로드 함수(transferTO)

            OcrDTO pDTO = new OcrDTO();

            pDTO.setSaveFileName(saveFileName);
            pDTO.setSaveFilePath(saveFilePath);
            pDTO.setExt(ext);
            pDTO.setOrgFileName(originalFileName);
            pDTO.setRegId("admin");

            OcrDTO rDTO = Optional.ofNullable(ocrService.getReadforImageText(pDTO)).orElseGet(OcrDTO::new); // null 값을 체크하여 rDTO에 저장

            res = CmmUtil.nvl(rDTO.getTextFormImage()); // 인식결과

            pDTO.setOcrText(res);
            pDTO.setRegId(regId);
            pDTO.setChgId(chgId);
            // DB 등록
            ocrService.insertOcrInfo(pDTO);

            rDTO = null;
            pDTO = null;

        } else {
            res = "이미지 파일이 아니어서 인식이 불가합니다.";
        }

        model.addAttribute("res", res); // 이미지로부터 인식된 문자 JSP에 전달

        log.info(this.getClass().getName() + ".readImage End!");

        return "ocr/readImage";
    }

    @GetMapping(value="ocrList")
    public String OcrList(ModelMap model) throws Exception{

        log.info(this.getClass().getName() + ".OcrList Start!");

        List<OcrDTO> rList = Optional.ofNullable(ocrService.getOcrList()).orElseGet(ArrayList::new);

        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".OcrList End!");

        return "ocr/ocrList";
    }

    @GetMapping(value = "ocrInfo")
    public String ocrInfo(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() + ".ocrInfo Start!");

        String seq = CmmUtil.nvl(request.getParameter("seq"));

        log.info("seq : " + seq);

        OcrDTO pDTO = new OcrDTO();
        pDTO.setSeq(seq);

        OcrDTO rDTO = Optional.ofNullable(ocrService.getOcrInfo(pDTO)).orElseGet(OcrDTO::new);

        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".ocrInfo End!");

        return "ocr/ocrInfo";
    }

    @GetMapping(value ="download")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request, @RequestParam("filename") String filename) throws Exception {

        log.info(this.getClass().getName() + ".downloadFIle Start!");

        filename = CmmUtil.nvl(request.getParameter("filename")); // jsp에서 dto에서 SAVE_FILE_NAME을 꺼내 filename에 값을 넣음

        log.info("file : " + filename);

        OcrDTO pDTO = new OcrDTO(); // OcrDTO에 파일이름을 저장
        pDTO.setSaveFileName(filename);

        OcrDTO rDTO = Optional.ofNullable(ocrService.getFilePath(pDTO)).orElseGet(OcrDTO::new); // 파일경로를 찾는 mapper파일에 값을 넣고 결과를 rDTO에 저장

        String filePath = rDTO.getSaveFilePath() + "/" + filename; // 파일경로 + 파일명

        log.info("filePath : " + filePath);

        Resource resource = new FileSystemResource(filePath); // 파일경로로 리소스 생성

        HttpHeaders headers = new HttpHeaders(); // HTTP 응답 헤더 생성
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + rDTO.getOrgFileName());
        // CONTENT_DISPOSION : 브라우저가 파일을 첨부파일로 처리하도록 하는 헤더
        // filename 변수로 다운로드 파일이름 설정

        return ResponseEntity.ok().headers(headers).body(resource);
        // ResponseEntity : HTTP응답을 나타내는 Spring 클래스
        // .ok() : HTTP상태코드 200(OK)를 반환
        // headers 객체 : 헤더 담기
        // body 객체 : 파일 리소스를 응답 본문으로 설정

        // 컨트롤러 > 엔드포인트로 요청 보내기 > 서버가 파일을 첨부파일로 다운할수 있도록 응답 반환
    }
}
