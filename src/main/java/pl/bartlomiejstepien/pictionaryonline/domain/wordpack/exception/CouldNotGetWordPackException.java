package pl.bartlomiejstepien.pictionaryonline.domain.wordpack.exception;

import java.io.IOException;

public class CouldNotGetWordPackException extends RuntimeException
{
    public CouldNotGetWordPackException(IOException e)
    {
        super(e);
    }
}
