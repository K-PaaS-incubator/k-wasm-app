package kr.or.kpass.kwasm.service.impl;

import kr.or.kpass.kwasm.dto.FileDTO;
import kr.or.kpass.kwasm.service.IFileManageService;
import kr.or.kpass.kwasm.service.IKwasmCompile;
import kr.or.kpass.kwasm.service.IKwasmExecute;
import kr.or.kpass.kwasm.util.CmmUtil;
import kr.or.kpass.kwasm.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class KwasmExecute implements IKwasmExecute {

    private final IFileManageService fileManageService; // 파일 관리

    private final IKwasmCompile kwasmCompile; // K-WASM 컴파일러

    /**
     * K-WASM 컴파일러 실행을 위한 함수
     *
     * @param pDTO 컴파일할 파일 정보
     */
    @Override
    public FileDTO doExecuteKwasm(FileDTO pDTO, MultipartFile mf) throws Exception {

        log.info(this.getClass().getName() + " doExecuteKwasm Start!");

        // Controller로부터 전달받은 값
        String orgFileName = CmmUtil.nvl(pDTO.orgFileName()); // 원본 파일명
        String saveFileName = CmmUtil.nvl(pDTO.saveFileName()); // 저장된 파일명
        String ext = CmmUtil.nvl(pDTO.ext()); // 파일 확장자

        // Object storage에 저장 후 저장된 파일 URL 반환 (이번 버전에서 제외)
//        FileDTO rDTO = fileUploadService.uploadObjectStorage(mf, pDTO);
//        log.info("FileURL : " + rDTO.serverFileUrl());

        // 1단계 . 파일 시스템에 컴파일할 파일 저장
        FileDTO rDTO = fileManageService.uploadFileSystem(mf, pDTO);

        // 2단계 . K-WASM 컴파일 실행
        int res = kwasmCompile.doCompile(rDTO);

        // 3단계 . K-WASM 컴파일 결과를 ZIP 파일로 압축하기(.wasm, .html, .js, .c)
        rDTO = fileManageService.compressionZipFileSystem(rDTO);

        // 4단계 . K-WASM 컴파일 결과를 RDBMS에 파일 메타정보를 저장할 형태 만들기
        FileDTO dto = FileDTO.builder()
                .orgFileName(orgFileName) // 원래 파일 이름
                .saveFileName(rDTO.saveFileName())
                .saveFilePath(rDTO.saveFilePath())
                .ext(ext)// 파일 확장자
                .serverFileUrl("") // 서버 저장된 파일 url
                .serverFileName("") // 서버 저장된 파일 이름
                .regDt(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss"))
                .build();

        //RDBMS 파일 메타정보 저장
        FileDTO result = fileManageService.saveFileData(dto);

        log.info(this.getClass().getName() + " doExecuteKwasm End!");

        return result;
    }
}
