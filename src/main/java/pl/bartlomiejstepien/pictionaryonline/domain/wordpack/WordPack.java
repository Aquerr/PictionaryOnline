package pl.bartlomiejstepien.pictionaryonline.domain.wordpack;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WordPack
{
    private String name;
    private List<String> words;
}
