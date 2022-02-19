package pl.bartlomiejstepien.pictionaryonline.interfaces.web.wordpacks;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.WordPack;
import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.WordPacksService;
import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.exception.CouldNotGetWordPackException;
import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.exception.CouldNotGetWordsPacksException;
import pl.bartlomiejstepien.pictionaryonline.interfaces.web.error.ErrorResponse;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/wordpacks")
public class WordPacksController
{
    private final WordPacksService wordPacksService;

    @GetMapping
    public List<WordPack> getWordPacks()
    {
        return this.wordPacksService.getWordsPacks();
    }

    @GetMapping("/{name}")
    public WordPack getWordPack(@PathVariable("name") final String name)
    {
        return this.wordPacksService.getWordPack(name);
    }

    @GetMapping("/{name}/words")
    public List<String> getWordsForPack(@PathVariable("name") final String name)
    {
        return this.wordPacksService.getWordsForPack(name);
    }

    @ExceptionHandler({CouldNotGetWordPackException.class, CouldNotGetWordsPacksException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Something went wrong while getting the word pack")
    public ErrorResponse handleException(Exception exception)
    {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }
}
