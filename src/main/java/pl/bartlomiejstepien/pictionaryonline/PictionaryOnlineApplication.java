package pl.bartlomiejstepien.pictionaryonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PictionaryOnlineApplication
{
	public static void main(String[] args) {
		SpringApplication.run(PictionaryOnlineApplication.class, args);
	}
}
