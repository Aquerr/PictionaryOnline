package pl.bartlomiejstepien.pictionaryonline;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

public class TestUtils
{
    private static final String mocksDir = "mocks";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .findAndRegisterModules();

    public static String loadMockJsonFromFile(String fileName)
    {
        Resource resource = new ClassPathResource(mocksDir + File.separator + fileName);
        try
        {
            return OBJECT_MAPPER.readTree(resource.getInputStream()).toPrettyString();
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Could not load mock json file: " + fileName, e);
        }
    }
}
