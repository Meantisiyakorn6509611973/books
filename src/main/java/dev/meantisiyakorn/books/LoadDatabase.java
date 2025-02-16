package dev.meantisiyakorn.books;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    public static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(StorageRepository repository) {
        return args -> {
            log.info("loading " + repository.save(new Storage("Percy Jackson & the Olympians", "Bell", "House 1st floor", false, null)));
            log.info("loading " + repository.save(new Storage("The Hunger Games", "Bell", "House 2nd floor", false, null)));
            log.info("loading " + repository.save(new Storage("The Lord of the Rings", "Bell", "Dormitory", false, null)));
            log.info("loading " + repository.save(new Storage("Harry Potter and the Philosopher's Stone", "Bell", "Library", false, null)));
            log.info("loading " + repository.save(new Storage("The Hobbit", "Bell", "Dormitory", false, null)));
            log.info("loading " + repository.save(new Storage("To Kill a Mockingbird", "Sara", "Study Room", false, null)));
            log.info("loading " + repository.save(new Storage("1984", "John", "Shelf A", false, null)));
            log.info("loading " + repository.save(new Storage("Moby Dick", "Emma", "Shelf B", false, null)));
            log.info("loading " + repository.save(new Storage("Pride and Prejudice", "Lucas", "House 3rd floor", false, null)));
            log.info("loading " + repository.save(new Storage("War and Peace", "David", "Office", false, null)));
        };
    }
}
