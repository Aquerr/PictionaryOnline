package pl.bartlomiejstepien.pictionaryonline.domain.wordpack;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.exception.CouldNotGetWordPackException;
import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.exception.CouldNotGetWordsPacksException;

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
