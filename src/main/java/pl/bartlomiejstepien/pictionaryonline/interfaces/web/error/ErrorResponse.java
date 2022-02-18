package pl.bartlomiejstepien.pictionaryonline.interfaces.web.error;

import lombok.Value;

@Value
public class ErrorResponse
{
    int status;
    String message;
}
