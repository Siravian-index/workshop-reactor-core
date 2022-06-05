package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Service
public class PersonService {
    @Autowired
    private PersonRepository repository;

    public Flux<Person> listAll() {
        return repository.findAll();
    }

    public Mono<Void> insert(Mono<Person> personMono) {
        return personMono
                .flatMap(person -> repository.findByName(person.getName()))
                .switchIfEmpty(Mono.defer(() -> personMono.doOnNext(repository::save)))
                .then();
    }

    public Mono<Person> getOnePerson(String id) {
        return repository.findById(id);
    }

    public Mono<Void> updatePerson(Mono<Person> personMono) {
        return personMono
                .flatMap(person -> repository.save(person))
                .then();
    }

    public Mono<Void> deleteOnePerson(String id) {
        return repository.deleteById(id);
    }

}
