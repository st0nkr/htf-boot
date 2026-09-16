package com.teto.command.keywords;

import com.teto.IFile;
import com.teto.IProperties;
import com.teto.command.AbstractCommand;
import com.teto.command.Context;
import com.teto.domain.meta.ConfigProperty;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class AddNoisyWords extends AbstractCommand<Boolean> implements IProperties, IFile {
    private final List<String> words;

    public AddNoisyWords(List<String> words) {
        this.words = words;
    }

    @Override
    public Optional<Boolean> apply(Context ctx) {
        String noisyWords = property(ctx, ConfigProperty.NOISY_WORDS_FILE);
        Optional<List<String>> content = readFileAsLines(noisyWords);
        if(isPresent(content)) {
            Boolean changed = false;
            Collection<String> lines = new TreeSet<>(content.get());
            for(String word : words) {
                if(!lines.contains(noisyWords)) {
                    lines.add(word);
                    changed = true;
                }
            }
            if(changed) {
                saveFile(noisyWords, lines);
            }
        }
        return optional(true);
    }
}
