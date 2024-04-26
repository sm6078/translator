package org.javaacademy.translator.controller;


import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.javaacademy.translator.dto.WordDtoPaginationRs;
import org.javaacademy.translator.dto.WordDtoRq;
import org.javaacademy.translator.dto.WordDtoRs;
import org.javaacademy.translator.service.WordService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/word")
@CacheConfig(cacheNames = "paginat")
public class WordController {
    private final WordService wordService;

    @GetMapping
    public List<WordDtoRs> getAll() {
        return wordService.getAll();
    }

    @GetMapping("/pagination")
    @Cacheable(cacheNames = "paginat")
    @CachePut(cacheNames = "paginat", condition = "#refresh==true")
    public WordDtoPaginationRs<List<WordDtoRs>> getWords(@RequestParam Integer startPosition,
                                                         @RequestParam Integer countWord,
                                                         @RequestParam(required = false)
                                                             boolean refresh) {
        return wordService.getWords(startPosition, countWord);
    }

    @PostMapping
    @CacheEvict(cacheNames = "paginat", allEntries = true)
    public ResponseEntity<WordDtoRs> createWord(@RequestBody WordDtoRs dto) {
        return ResponseEntity.status(CREATED).body(wordService.create(dto));
    }

    @PutMapping("/{key}")
    @CacheEvict(cacheNames = "paginat", allEntries = true)
    public ResponseEntity<?> updateWord(@PathVariable String key, @RequestBody WordDtoRq dto) {
        wordService.update(key, dto);
        return ResponseEntity.status(ACCEPTED).build();
    }

    @GetMapping("/{key}")
    public WordDtoRs getWordByKey(@PathVariable String key) {
        return wordService.getByKey(key);
    }

    @PatchMapping("/{key}")
    @CacheEvict(cacheNames = "paginat", allEntries = true)
    public ResponseEntity<WordDtoRs> patchBook(@PathVariable String key,
                                               @RequestBody WordDtoRq dto) {
        return ResponseEntity.status(ACCEPTED).body(wordService.patch(key, dto));
    }

    @DeleteMapping("/{key}")
    @CacheEvict(cacheNames = "paginat", allEntries = true)
    public ResponseEntity<?> deleteWord(@PathVariable String key) {
        boolean result = wordService.deleteByKey(key);
        return result
                ? ResponseEntity.status(ACCEPTED).build()
                : ResponseEntity.status(NOT_FOUND).build();
    }

    @DeleteMapping
    @CacheEvict(cacheNames = "paginat", allEntries = true)
    public ResponseEntity<?> deleteAll() {
        boolean result = wordService.deleteAll();
        return result
                ? ResponseEntity.status(ACCEPTED).build()
                : ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
    }
}