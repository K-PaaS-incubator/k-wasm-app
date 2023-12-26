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
    @Column(name = "FILE_SEQ", length = 30, nullable = false)
    private Long fileSeq;

    @NonNull // 원래 사용자 파일 이름
    @Column(name = "ORG_FILE_NAME", length = 100, nullable = false)
    private String orgFileName;

    @NonNull // 서버에 저장된 파일 이름
    @Column(name = "SERVER_FILE_NAME", length = 30, nullable = false)
    private String serverFileName;

    @NonNull  // 서버에 저장된 파일 URL
    @Column(name = "SERVER_FILE_URL", length = 50, nullable = false)
    private String serverFileUrl;

    @NonNull// 파일 확장자
    @Column(name = "EXT", length = 5, nullable = false)
    private String ext;


}
