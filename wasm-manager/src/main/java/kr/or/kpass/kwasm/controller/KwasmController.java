package kr.or.kpass.kwasm.controller;


import jakarta.servlet.http.HttpServletRequest;
import kr.or.kpass.kwasm.dto.ApiResponse;
import kr.or.kpass.kwasm.dto.FileDTO;
import kr.or.kpass.kwasm.dto.MsgResult;
import kr.or.kpass.kwasm.service.IFileManageService;
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

@CrossOrigin(origins = {"http://www.k-wasm.kr", "http://k-wasm.kr", "http://api.k-wasm.kr",
        "https://www.k-wasm.kr", "https://k-wasm.kr", "https://api.k-wasm.kr"},
        allowedHeaders = {"POST, GET"}, allowCredentials = "true")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1")
public class KwasmController {

    private final IKwasmExecute kwasmExecute; // K-WASM 실행용 서비스

    private final IFileManageService fileManageService; // K-WASM 파일 관리 서비스


    /**
     * @param mf 프로그래밍 파일
     * @return 저장된 파일 url
     */
    @PostMapping("upload")
    public ApiResponse fileUpload(@RequestParam(value = "fileUpload") MultipartFile mf) throws Exception {

        log.info(getClass().getName() + " FileUpload Start!");

        // 업로드하는 실제 파일명
        String orgFileFullName = CmmUtil.nvl(mf.getOriginalFilename());

        // 확장자 제외된 실제 파일명
        String orgFileName = orgFileFullName.substring(0, orgFileFullName.lastIndexOf("."));

        // 파일 확장자 가져오기
        String ext = orgFileFullName.substring(orgFileFullName.lastIndexOf(".") + 1).toLowerCase();

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

        // 응답 결과 처리
        ApiResponse result = ApiResponse.builder()
                .status(MsgResult.Success.isStatus())
                .code(MsgResult.Success.getCode())
                .msg(MsgResult.Success.getMsg())
                .result(rDTO).build();

        log.info(this.getClass().getName() + ".fileUpload End!");

        return result;

    }

    /**
     * K-WASM 파일 다운로드
     */
    @GetMapping("download")
    public ResponseEntity<Object> fileDownload(HttpServletRequest request) throws Exception {

        log.info(getClass().getName() + " fileDownload Start!");

        String fileSeq = CmmUtil.nvl(request.getParameter("fileSeq"));
        log.info("fileSeq : " + fileSeq);

        FileDTO rDTO = fileManageService.downloadWasmFileSystem(
                FileDTO.builder().fileSeq(Long.parseLong(fileSeq)).build()
        );

        String saveFileName = CmmUtil.nvl(rDTO.saveFileName());
        String saveFilePath = CmmUtil.nvl(rDTO.saveFilePath());
        String orgFileName = CmmUtil.nvl(rDTO.orgFileName());

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
                .filename(CmmUtil.nvl(orgFileName + ".zip")).build());

        log.info(this.getClass().getName() + ".fileDownload End!");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }

}
