package com.parking.management.repositories;

import com.parking.management.entities.ParkingSpace;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ParkingSpaceRepository extends MongoRepository<ParkingSpace, String> {
    List<ParkingSpace> findByStatus(String status);
}
