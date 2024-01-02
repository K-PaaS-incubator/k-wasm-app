package kr.or.kpass.kwasm.service.impl;

import kr.or.kpass.kwasm.dto.FileDTO;
import kr.or.kpass.kwasm.service.IKwasmExecute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Service
public class KwasmExecute implements IKwasmExecute {

    String homeDir = "/kwasm/data"; // 컴파일될 파일 임시 저장소

    /**
     * K-WASM 컴파일러 실행을 위한 함수
     *
     * @param pDTO 컴파일할 파일 정보
     */
    @Override
    public int doExecuteKwasm(FileDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + " doExecuteKwasm Start!");

        int res;

        String path = "/" + pDTO.saveFilePath() + "/" + pDTO.serverFileName();
        log.info("path : " + path);

        ProcessBuilder builder = new ProcessBuilder();

        builder.command("emcc -c " + path); // Windows OS

        Process process = builder.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        process.destroy();

        res = 1;

        log.info(this.getClass().getName() + " doExecuteKwasm End!");

        return res;
    }
}
