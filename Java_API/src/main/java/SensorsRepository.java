package main.java;

import org.springframework.data.repository.CrudRepository;

public interface SensorsRepository extends CrudRepository<Sensor, Long> {
}