package org.javaacademy.translator.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import lombok.SneakyThrows;
import org.javaacademy.translator.entity.Word;
import org.springframework.stereotype.Component;

@Component
public class WordRepository {
    private final Map<String, Word> dictionary = new TreeMap<>();

    @SneakyThrows
    public Word add(Word newWord) {
        Thread.sleep(3000);
        return dictionary.put(newWord.getValue(), newWord);
    }

    @SneakyThrows
    public List<Word> findAll() {
        Thread.sleep(3000);
        return new ArrayList<>(dictionary.values());
    }

    @SneakyThrows
    public Optional<Word> findByKey(String key) {
        Thread.sleep(3000);
        return Optional.ofNullable(dictionary.get(key));
    }

    @SneakyThrows
    public void updateByKey(String key, Word word) {
        Thread.sleep(3000);
        if (!dictionary.containsKey(key)) {
            throw new RuntimeException("Слово не найдено в словаре");
        }
        dictionary.remove(key);
        dictionary.put(word.getValue(), word);
    }

    @SneakyThrows
    public boolean deleteByKey(String key) {
        Thread.sleep(3000);
        return dictionary.remove(key) != null;
    }

    @SneakyThrows
    public boolean deleteAll() {
        Thread.sleep(3000);
        dictionary.clear();
        return dictionary.isEmpty();
    }
}
