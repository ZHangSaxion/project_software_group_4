package main.java;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReadingsRepository extends CrudRepository<Readings, Long> {
    List<Readings> findByTime(int days);
}