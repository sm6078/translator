package org.javaacademy.translator.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
public class Word {
    @NonNull
    private String value;
    @NonNull
    @EqualsAndHashCode.Exclude
    private String description;
}
