package pl.bartlomiejstepien.pictionaryonline.interfaces.storage;

import pl.bartlomiejstepien.pictionaryonline.domain.word.WordPack;

import java.io.IOException;
import java.util.List;

public interface WordPacksRepository
{
    List<WordPack> getWordsPacks() throws IOException;

    List<String> getWordsForPack(final String packName) throws IOException;

    WordPack getWordPack(final String packName) throws IOException;
}
