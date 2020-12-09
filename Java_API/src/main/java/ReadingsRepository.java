package main.java;

import org.springframework.data.repository.CrudRepository;

public interface ReadingsRepository extends CrudRepository<Readings, Long> {
}