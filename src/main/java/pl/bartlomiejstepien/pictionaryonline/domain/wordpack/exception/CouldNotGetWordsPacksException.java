package pl.bartlomiejstepien.pictionaryonline.domain.wordpack.exception;

import java.io.IOException;

public class CouldNotGetWordsPacksException extends RuntimeException
{
    public CouldNotGetWordsPacksException(IOException e)
    {
        super(e);
    }
}
