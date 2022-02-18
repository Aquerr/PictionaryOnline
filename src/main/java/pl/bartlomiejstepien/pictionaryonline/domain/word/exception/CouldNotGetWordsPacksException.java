package pl.bartlomiejstepien.pictionaryonline.domain.word.exception;

import java.io.IOException;

public class CouldNotGetWordsPacksException extends RuntimeException
{
    public CouldNotGetWordsPacksException(IOException e)
    {
        super(e);
    }
}
