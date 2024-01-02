package kr.or.kpass.kwasm.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FILE")
@DynamicInsert
@DynamicUpdate
@Entity
@Builder
public class FileEntity {

    @Id  // File Table SEQ
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_SEQ", nullable = false)
    private Long fileSeq;

    @NonNull // 원래 사용자 파일 이름
    @Column(name = "ORG_FILE_NAME", length = 1000, nullable = false)
    private String orgFileName;

    // 저장된 파일 이름
    @Column(name = "SAVE_FILE_NAME", length = 1000, nullable = false)
    private String saveFileName;

    // 저장된 파일 경로
    @Column(name = "SAVE_FILE_PATH", length = 1000, nullable = false)
    private String saveFilePath;

    @NonNull// 파일 확장자
    @Column(name = "EXT", length = 5, nullable = false)
    private String ext;

    // Object Storage에 저장된 파일명
    @Column(name = "SERVER_FILE_NAME", length = 1000, nullable = false)
    private String serverFileName;

    // Object Storage에 저장된 파일 URL
    @Column(name = "SERVER_FILE_URL", length = 1000, nullable = false)
    private String serverFileUrl;

    // 파일 생성 일시
    @Column(name = "REG_DT", length = 1000, nullable = false)
    private String regDt;

}
