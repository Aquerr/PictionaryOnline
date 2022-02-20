package pl.bartlomiejstepien.pictionaryonline.interfaces.file.wordpack;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.WordPack;
import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.WordPacksRepository;
import pl.bartlomiejstepien.pictionaryonline.interfaces.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Cacheable("wordpacksCache")
    public List<WordPack> getWordsPacks() throws IOException
    {
        File wordpacksDirFile = new File(this.wordpacksDir);
        File[] files = wordpacksDirFile.listFiles();
        List<WordPack> wordpacks = new ArrayList<>(files.length);
        for (final File file : files)
        {
            wordpacks.add(this.wordPackReader.read(file.toPath()));
        }
        return wordpacks;
    }

    @Override
    @Cacheable("wordpacksCache")
    public WordPack getWordPack(String packName) throws IOException
    {
        if (!StringUtils.hasText(packName))
            throw new IOException("Pack name cannot be empty!");

        File packFile = findWordPackFile(packName)
                .orElseThrow(() -> new IOException("Word Pack not found!"));

        return this.wordPackReader.read(packFile.toPath());
    }

    private Optional<File> findWordPackFile(String packName)
    {
        File wordpacksDir = new File(this.wordpacksDir);
        for (final File file : wordpacksDir.listFiles())
        {
            if (FileUtils.removeFileExtension(file.getName(), false).equals(packName))
                return Optional.of(file);
        }
        return Optional.empty();
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
