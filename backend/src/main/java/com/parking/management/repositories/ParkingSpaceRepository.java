package com.parking.management.repositories;

import com.parking.management.entities.ParkingSpace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSpaceRepository extends MongoRepository<ParkingSpace, Long> {

    /**
     * Рахує кількість паркувальних місць за їх статусом.
     * @param status статус паркувального місця
     * @return кількість паркувальних місць зі вказаним статусом
     */
    long countByStatus(ParkingSpace.Status status);

    /**
     * Знаходить паркувальні місця за їх статусом.
     * @param status статус паркувального місця
     * @return список паркувальних місць зі вказаним статусом
     */
    List<ParkingSpace> findByStatus(ParkingSpace.Status status);
}
