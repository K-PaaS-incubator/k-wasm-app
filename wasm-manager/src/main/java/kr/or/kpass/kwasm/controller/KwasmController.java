package kr.or.kpass.kwasm.controller;


import kr.or.kpass.kwasm.dto.FileDTO;
import kr.or.kpass.kwasm.service.IFileUploadService;
import kr.or.kpass.kwasm.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = {"http://localhost:13000"}, allowedHeaders = {"POST, GET"}, allowCredentials = "true")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1")
public class KwasmController {


    private final IFileUploadService fileUploadService;

    /**
     * @param mf 프로그래밍 파일
     * @return 저장된 파일 url
     * @throws Exception
     */
    @PostMapping("upload")
    public int fileUpload(@RequestParam(value = "fileUpload") MultipartFile mf) throws Exception {

        log.info(getClass().getName() + "FileUpload 시작");

        int res = 0;

        try {
            // 업로드하는 실제 파일명
            String originalFileName = mf.getOriginalFilename();

            // 파일 확장자 가져오기
            String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1,
                    originalFileName.length()).toLowerCase();

            // Object storage에 저장될 파일 이름
            String saveFileName = DateUtil.getDateTime("HHmmss") + "." + ext;

            // Object storage에 저장 후 저장된 파일 URL 반환
            String fileUrl = fileUploadService.upload(mf, saveFileName);

            log.info("FileURL : " + fileUrl);

            FileDTO pDTO = FileDTO.builder()
                    .orgFileName(originalFileName) // 원래 파일 이름
                    .ext(ext)// 파일 확장자
                    .serverFileUrl(fileUrl) // 서버 저장된 파일 url
                    .serverFileName(saveFileName) // 서버 저장된 파일 이름
                    .build();

            //RDB 에 파일 정보 저장
            res = fileUploadService.saveFileData(pDTO);

            log.info("파일 저장 성공 여부 " + res);

        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }

        log.info(getClass().getName() + "FileUpload 종료");

        return res;
    }

}
