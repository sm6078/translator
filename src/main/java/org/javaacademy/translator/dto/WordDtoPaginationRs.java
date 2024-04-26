package org.javaacademy.translator.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class WordDtoPaginationRs<T> {
    @NonNull
    private Integer totalSize;
    @NonNull
    private Integer count;
    @NonNull
    private Integer startPosition;
    @NonNull
    private Integer endPosition;
    @NonNull
    private T content;

}
