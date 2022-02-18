package pl.bartlomiejstepien.pictionaryonline.domain.word;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WordPack
{
    private String packName;
    private List<String> words;
}
