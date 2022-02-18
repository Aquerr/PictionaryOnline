package pl.bartlomiejstepien.pictionaryonline.domain.word.exception;

import java.io.IOException;

public class CouldNotGetWordPackException extends RuntimeException
{
    public CouldNotGetWordPackException(IOException e)
    {
        super(e);
    }
}
