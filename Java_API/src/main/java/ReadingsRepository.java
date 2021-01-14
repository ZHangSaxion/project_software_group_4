package main.java;

import org.springframework.data.repository.CrudRepository;
/**
 * An entity class instancing springboot class enable repository functions
 * for getting data in table "readings" from database.
 *
 * @author Ziru Hang
 * @version 1.0
 * @since 02-12-2020
 */
public interface ReadingsRepository extends CrudRepository<Readings, Long> {
}