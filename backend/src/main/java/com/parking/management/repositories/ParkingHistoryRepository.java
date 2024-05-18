package com.parking.management.repositories;

import com.parking.management.entities.ParkingHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingHistoryRepository extends MongoRepository<ParkingHistory, String> {

    /**
     * Знаходить історію паркування за ID користувача.
     * @param userId ID користувача
     * @return Список історій паркування
     */
    List<ParkingHistory> findByUserId(Long userId);

    /**
     * Знаходить історію паркування за ID паркувального місця.
     * @param parkingSpaceId ID паркувального місця
     * @return Список історій паркування
     */
    List<ParkingHistory> findByParkingSpaceId(Long parkingSpaceId);
}
