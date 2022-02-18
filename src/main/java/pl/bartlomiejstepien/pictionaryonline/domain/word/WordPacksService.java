package pl.bartlomiejstepien.pictionaryonline.domain.word;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bartlomiejstepien.pictionaryonline.domain.word.exception.CouldNotGetWordPackException;
import pl.bartlomiejstepien.pictionaryonline.domain.word.exception.CouldNotGetWordsPacksException;
import pl.bartlomiejstepien.pictionaryonline.interfaces.storage.WordPacksRepository;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class WordPacksService
{
    private final WordPacksRepository wordPacksRepository;

    public List<WordPack> getWordsPacks()
    {
        try
        {
            return this.wordPacksRepository.getWordsPacks();
        }
        catch (IOException e)
        {
            throw new CouldNotGetWordsPacksException(e);
        }
    }

    public List<String> getWordsForPack(final String packName)
    {
        try
        {
            return this.wordPacksRepository.getWordsForPack(packName);
        }
        catch (IOException e)
        {
            throw new CouldNotGetWordPackException(e);
        }
    }

    public WordPack getWordPack(String name)
    {
        try
        {
            return this.wordPacksRepository.getWordPack(name);
        }
        catch (IOException e)
        {
            throw new CouldNotGetWordPackException(e);
        }
    }
}
