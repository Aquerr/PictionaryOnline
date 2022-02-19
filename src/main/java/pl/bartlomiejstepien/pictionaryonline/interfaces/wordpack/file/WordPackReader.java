package pl.bartlomiejstepien.pictionaryonline.interfaces.wordpack.file;

import org.springframework.stereotype.Component;
import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.WordPack;
import pl.bartlomiejstepien.pictionaryonline.interfaces.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class WordPackReader
{
    public WordPack read(Path packPath) throws IOException
    {
        final String wordPackName = FileUtils.removeFileExtension(packPath.toFile().getName(), false);
        final List<String> words = Files.readAllLines(packPath);
        return new WordPack(wordPackName, words);
    }
}