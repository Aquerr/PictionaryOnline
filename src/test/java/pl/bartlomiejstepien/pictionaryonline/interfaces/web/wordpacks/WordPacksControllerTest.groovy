package pl.bartlomiejstepien.pictionaryonline.interfaces.web.wordpacks

import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.bartlomiejstepien.pictionaryonline.TestUtils
import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.WordPack
import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.WordPacksService
import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.exception.CouldNotGetWordPackException
import spock.lang.Specification

@WebMvcTest(controllers = WordPacksController)
class WordPacksControllerTest extends Specification {

    private static final String GET_WORD_PACKS_URL = "/api/wordpacks"
    private static final String GET_WORD_PACK_URL = "/api/wordpacks/{name}"

    private static final String EMPTY_WORD_PACK_NAME = "EMPTY_PACK"
    private static final String WORD_PACK_NAME = "PACK"

    @SpringBean
    private WordPacksService wordPacksService = Mock()

    @Autowired
    private WordPacksController wordPacksController

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    def "getWordPacks should return list of word packs"()
    {
        given:
        wordPacksService.getWordsPacks() >> prepareWordPacks()

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get(GET_WORD_PACKS_URL))

        then:
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())

        and:
        result.andExpect(MockMvcResultMatchers.content()
                .json(TestUtils.loadMockJsonFromFile("wordpacks/get-wordpacks.json")))
    }

    def "getWordPacks should return empty list of word packs"()
    {
        given:
        wordPacksService.getWordsPacks() >> Collections.emptyList()

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get(GET_WORD_PACKS_URL))

        then:
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())

        and:
        result.andExpect(MockMvcResultMatchers.content()
                .json(TestUtils.loadMockJsonFromFile("wordpacks/get-wordpacks-empty.json")))
    }

    def "getWordPack should return WordPack for given name"()
    {
        given:
        wordPacksService.getWordPack(WORD_PACK_NAME) >> prepareWordPack(WORD_PACK_NAME, Arrays.asList("word1", "word2", "word3"))

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get(GET_WORD_PACK_URL, WORD_PACK_NAME))

        then:
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())

        and:
        result.andExpect(MockMvcResultMatchers.content()
            .json(TestUtils.loadMockJsonFromFile("wordpacks/get-wordpack.json")))
    }

    def "getWordPack should return 500 when service throws CouldNotGetWordPackException"()
    {
        given:
        wordPacksService.getWordPack(WORD_PACK_NAME) >> {
            throw new CouldNotGetWordPackException(null)
        }

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get(GET_WORD_PACK_URL, WORD_PACK_NAME))

        then:
        result.andExpect(MockMvcResultMatchers.status().is5xxServerError())
        result.andExpect(MockMvcResultMatchers.status().reason("Something went wrong while getting the word pack"))
    }

    private WordPack prepareWordPack(String name, List<String> words)
    {
        return new WordPack(name, words)
    }

    private List<WordPack> prepareWordPacks()
    {
        return Arrays.asList(
                prepareWordPack(EMPTY_WORD_PACK_NAME, Collections.emptyList()),
                prepareWordPack(WORD_PACK_NAME, Arrays.asList("word1", "word2", "word3"))
        )
    }
}
