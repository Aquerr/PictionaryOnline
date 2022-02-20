package pl.bartlomiejstepien.pictionaryonline.domain.wordpack

import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.exception.CouldNotGetWordPackException
import pl.bartlomiejstepien.pictionaryonline.domain.wordpack.exception.CouldNotGetWordsPacksException
import spock.lang.Specification

class WordPacksServiceTest extends Specification {

    private static final String EMPTY_WORD_PACK_NAME = "TEST"

    private WordPacksRepository wordPacksRepository
    private WordPacksService wordPacksService

    void setup() {
        wordPacksRepository = Mock()
        wordPacksService = new WordPacksService(wordPacksRepository)
    }

    def "getWordPacks should return word packs from repository"()
    {
        given:
        wordPacksRepository.getWordsPacks() >> Arrays.asList(prepareEmptyWordPack())

        when:
        List<WordPack> wordPacks = wordPacksService.getWordsPacks()

        then:
        wordPacks
        wordPacks.size() == 1
        wordPacks.get(0).getPackName() == EMPTY_WORD_PACK_NAME
        wordPacks.get(0).getWords() == Collections.emptyList()
    }

    def "getWordPacks should throw CouldNotGetWordsPacksException when repository throws IOException"()
    {
        given:
        wordPacksRepository.getWordsPacks() >> {
            throw new IOException()
        }

        when:
        wordPacksService.getWordsPacks()

        then:
        Exception exception = thrown()
        exception instanceof CouldNotGetWordsPacksException
    }

    def "getWordsForPack should get words from repository and return them"()
    {
        given:
        wordPacksRepository.getWordsForPack(EMPTY_WORD_PACK_NAME) >> prepareWordsList()

        when:
        List<String> words = wordPacksService.getWordsForPack(EMPTY_WORD_PACK_NAME)

        then:
        words
        words == prepareWordsList()
    }

    def "getWordsForPack should throw CouldNotGetWordPackException when repository throws IOException"()
    {
        given:
        wordPacksRepository.getWordsForPack(EMPTY_WORD_PACK_NAME) >> {
            throw new IOException()
        }

        when:
        wordPacksService.getWordsForPack(EMPTY_WORD_PACK_NAME)

        then:
        Exception exception = thrown()
        exception instanceof CouldNotGetWordPackException
    }

    def "getWordPack should get word pack from repository for given name"()
    {
        given:
        wordPacksRepository.getWordPack(EMPTY_WORD_PACK_NAME) >> prepareEmptyWordPack()

        when:
        WordPack wordPack = wordPacksService.getWordPack(EMPTY_WORD_PACK_NAME)

        then:
        wordPack
        wordPack.packName == EMPTY_WORD_PACK_NAME
    }

    def "getWordPack should throw CouldNotGetWordPackException when repository throws IOException"()
    {
        given:
        wordPacksRepository.getWordPack(EMPTY_WORD_PACK_NAME) >> {
            throw new IOException()
        }

        when:
        wordPacksService.getWordPack(EMPTY_WORD_PACK_NAME)

        then:
        Exception exception = thrown()
        exception instanceof CouldNotGetWordPackException
    }

    private WordPack prepareEmptyWordPack()
    {
        return new WordPack(EMPTY_WORD_PACK_NAME, Collections.emptyList())
    }

    private List<String> prepareWordsList()
    {
        return Arrays.asList("word1", "word2", "word3")
    }
}
