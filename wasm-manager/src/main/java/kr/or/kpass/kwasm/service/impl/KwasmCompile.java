package kr.or.kpass.kwasm.service.impl;

import kr.or.kpass.kwasm.dto.FileDTO;
import kr.or.kpass.kwasm.service.IKwasmCompile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Service
public class KwasmCompile implements IKwasmCompile {

    /**
     * WASM 컴파일 수행하기
     *
     * @param pDTO 파일정보
     * @return 컴파일 성공여부
     */
    @Override
    public int doCompile(FileDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + " doCompile Start!");

        int res = 0;

        String path = pDTO.saveFilePath() + "/" + pDTO.saveFileName() + "." + pDTO.ext();
        log.info("path : " + path);

        if (new File(path).exists()) {
            log.info("Start K-WASM");

            // emcc /kwasm/data/2024/01/02/111901.c -o 111901.html
//            String command = "emcc " + path + " -o " + pDTO.saveFileName() + ".html";
//            log.info("command : " + command);

            ProcessBuilder builder = new ProcessBuilder();

            List<String> command = List.of("emcc", path, "-o", pDTO.saveFilePath() + "/" + pDTO.saveFileName() + ".html");

            log.info("command : " + command);

            builder.command(command); // Windows OS

            Process process = builder.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            process.destroy();

            res = 1;

        } else {
            log.info("Error : " + path + " Not Exists File");

        }

        log.info(this.getClass().getName() + " doCompile End!");

        return res;
    }
}
