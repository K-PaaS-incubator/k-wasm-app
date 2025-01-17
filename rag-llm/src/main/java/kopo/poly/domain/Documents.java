package kopo.poly.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "documents")
public class Documents {

    @Id
    private String id;

    private String content;

    private List<Double> embedding;

    @Transient
    private double similarityScore; // 유사도 점수
}
