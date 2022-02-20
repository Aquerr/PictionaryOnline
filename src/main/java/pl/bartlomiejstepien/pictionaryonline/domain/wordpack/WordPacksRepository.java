package pl.bartlomiejstepien.pictionaryonline.domain.wordpack;

import java.io.IOException;
import java.util.List;

public interface WordPacksRepository
{
    List<WordPack> getWordsPacks() throws IOException;

    WordPack getWordPack(final String packName) throws IOException;
}
