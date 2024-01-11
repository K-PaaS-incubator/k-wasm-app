package kr.or.kpass.kwasm.repository;

import kr.or.kpass.kwasm.repository.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    /**
     * 컴파일 결과 메타정보 조회하기
     */
    Optional<FileEntity> findByFileSeq(Long fileSeq);
}
