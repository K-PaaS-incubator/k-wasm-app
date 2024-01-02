package kr.or.kpass.kwasm.controller;


import jakarta.servlet.http.HttpServletRequest;
import kr.or.kpass.kwasm.dto.FileDTO;
import kr.or.kpass.kwasm.service.IKwasmExecute;
import kr.or.kpass.kwasm.util.CmmUtil;
import kr.or.kpass.kwasm.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;

@CrossOrigin(origins = {"http://192.168.2.128:13000", "http://192.168.2.128:14000"},
        allowedHeaders = {"POST, GET"}, allowCredentials = "true")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1")
public class KwasmController {

    private final IKwasmExecute kwasmExecute;

    /**
     * @param mf 프로그래밍 파일
     * @return 저장된 파일 url
     */
    @RequestMapping("upload")
    public FileDTO fileUpload(@RequestParam(value = "fileUpload") MultipartFile mf) throws Exception {

        log.info(getClass().getName() + " FileUpload Start!");

        int res = 0;

        // 업로드하는 실제 파일명
        String orgFileName = mf.getOriginalFilename();

        // 파일 확장자 가져오기
        String ext = orgFileName.substring(orgFileName.lastIndexOf(".") + 1,
                orgFileName.length()).toLowerCase();

        // 실제 저장되는 파일 이름
        String saveFileName = DateUtil.getDateTime("HHmmss");

        // 전달받은 파일 정보
        FileDTO dto = FileDTO.builder()
                .orgFileName(orgFileName) // 원래 파일 이름
                .ext(ext)// 파일 확장자
                .saveFileName(saveFileName) // 확장자 제외한 파일명
                .serverFileName(saveFileName + "." + ext) // 실제 저장될 파일 이름
                .build();

        // K-WASM 실행하기
        FileDTO rDTO = kwasmExecute.doExecuteKwasm(dto, mf);

        log.info(this.getClass().getName() + ".fileUpload End!");

        return rDTO;

    }

    /**
     * K-WASM 파일 다운로드
     */
    @PostMapping("download")
    public ResponseEntity<Object> fileDownload(HttpServletRequest request) throws Exception {

        log.info(getClass().getName() + " fileDownload Start!");

        String saveFileName = CmmUtil.nvl(request.getParameter("saveFileName"));
        String saveFilePath = CmmUtil.nvl(request.getParameter("saveFilePath"));
        String orgFileName = CmmUtil.nvl(request.getParameter("orgFileName"));

        log.info("saveFileName : " + saveFileName);
        log.info("saveFilePath : " + saveFilePath);
        log.info("orgFileName : " + orgFileName);

        String downloadFile = saveFilePath + "/" + saveFileName + ".zip";

        log.info("downloadFile : " + downloadFile);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(downloadFile));

        // 파일 다운로드를 위한 HTTP 통신 헤더값 설정
        HttpHeaders headers = new HttpHeaders();

        // 파일 다운로드 헤더값 설정
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(CmmUtil.nvl(orgFileName)).build());

        log.info(this.getClass().getName() + ".fileDownload End!");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }

}
