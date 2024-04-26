package org.javaacademy.translator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WordDtoRq {
    private String value;
    private String translate;
}
