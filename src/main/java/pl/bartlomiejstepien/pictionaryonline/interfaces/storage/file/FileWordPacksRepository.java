package pl.bartlomiejstepien.pictionaryonline.interfaces.storage.file;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ResourceUtils;
import pl.bartlomiejstepien.pictionaryonline.domain.word.WordPack;
import pl.bartlomiejstepien.pictionaryonline.interfaces.storage.WordPacksRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class FileWordPacksRepository implements WordPacksRepository
{
    private final WordPackReader wordPackReader;
    private final String wordpacksDir;

    @Autowired
    public FileWordPacksRepository(@Value("${pictionaryonline.words.repository.directory}") String wordpacksDir,
                                   WordPackReader wordPackReader)
    {
        this.wordpacksDir = wordpacksDir;
        this.wordPackReader = wordPackReader;
        prepareWordPacksDirIfNotExists();
    }

    @Override
    @Cacheable("wordpacks")
    public List<WordPack> getWordsPacks() throws IOException
    {
        return Files.list(Paths.get(this.wordpacksDir))
                .map(path -> {
                    try
                    {
                        return this.wordPackReader.read(path);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    @Cacheable("words")
    public List<String> getWordsForPack(final String packName) throws IOException
    {
        return this.wordPackReader.read(Paths.get(this.wordpacksDir).resolve(packName)).getWords();
    }

    @Override
    @Cacheable("wordpacks")
    public WordPack getWordPack(String packName) throws IOException
    {
        return this.wordPackReader.read(Paths.get(this.wordpacksDir).resolve(packName));
    }

    private void prepareWordPacksDirIfNotExists()
    {
        final Path wordPacksDir = Paths.get(this.wordpacksDir);
        if (Files.notExists(wordPacksDir))
        {
            try
            {
                Files.createDirectory(wordPacksDir);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        copyWordPacksFromResources(wordPacksDir);
    }

    private void copyWordPacksFromResources(Path wordPacksDir)
    {
        try
        {
            final File file = ResourceUtils.getFile("classpath:wordpacks");
            FileSystemUtils.copyRecursively(file, wordPacksDir.toFile());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
