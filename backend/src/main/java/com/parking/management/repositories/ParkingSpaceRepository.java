package com.parking.management.repositories;

import com.parking.management.entities.ParkingSpace;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ParkingSpaceRepository extends MongoRepository<ParkingSpace, Long> {
    long countByStatus(ParkingSpace.Status status);

    List<ParkingSpace> findByStatus(ParkingSpace.Status status);
}
