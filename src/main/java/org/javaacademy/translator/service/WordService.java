package org.javaacademy.translator.service;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.javaacademy.translator.dto.WordDtoPaginationRs;
import org.javaacademy.translator.dto.WordDtoRq;
import org.javaacademy.translator.dto.WordDtoRs;
import org.javaacademy.translator.entity.Word;
import org.javaacademy.translator.exception.TranslatorException;
import org.javaacademy.translator.repository.WordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository wordRepository;

    public WordDtoRs create(@NonNull WordDtoRs dto) {
        Word word = new Word(dto.getValue(), dto.getTranslate());
        if (wordRepository.add(word) != null) {
            throw new TranslatorException("Такое слово уже есть в словаре");
        }
        return convertToDtoRs(word);
    }

    public List<WordDtoRs> getAll() {
        return wordRepository.findAll()
                .stream()
                .map(this::convertToDtoRs)
                .toList();
    }

    public WordDtoRs getByKey(String key) {
        return wordRepository.findByKey(key)
                .map(this::convertToDtoRs)
                .orElseThrow();
    }

    public WordDtoPaginationRs<List<WordDtoRs>> getWords(@NonNull Integer startPosition,
                                                         @NonNull Integer countWord) {
        List<Word> list = wordRepository.findAll();
        List<WordDtoRs> words = list
                .stream()
                .skip(startPosition)
                .limit(countWord)
                .map(e -> new WordDtoRs(e.getValue(), e.getDescription())).toList();
        return new WordDtoPaginationRs<>(list.size(), countWord, startPosition,
                calculateEndPosition(startPosition, countWord, list.size()), words);
    }

    private int calculateEndPosition(int startPosition, int countWord, int size) {
        return Math.min(startPosition + countWord, size);
    }

    private WordDtoRs convertToDtoRs(Word word) {
        return new WordDtoRs(word.getValue(), word.getDescription());
    }

    public void update(String key, WordDtoRq dtoRq) {
        wordRepository.updateByKey(key, new Word(dtoRq.getValue(), dtoRq.getTranslate()));
    }

    public WordDtoRs patch(String key, WordDtoRq updateDto) {
        Word oldWord = wordRepository.findByKey(key).orElseThrow();
        if (oldWord.getValue().equals(updateDto.getValue())) {
            oldWord.setDescription(updateDto.getTranslate()
                    != null ? updateDto.getTranslate() : oldWord.getDescription());
            return convertToDtoRs(oldWord);
        }
        Word newWord = new Word(updateDto.getValue(), updateDto.getTranslate());
        wordRepository.updateByKey(key, newWord);
        return convertToDtoRs(newWord);
    }

    public boolean deleteByKey(String key) {
        return wordRepository.deleteByKey(key);
    }

    public boolean deleteAll() {
        return wordRepository.deleteAll();
    }
}
