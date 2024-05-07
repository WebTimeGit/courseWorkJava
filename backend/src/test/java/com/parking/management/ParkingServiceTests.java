package com.parking.management;

import com.parking.management.entities.ParkingSpace;
import com.parking.management.repositories.ParkingSpaceRepository;
import com.parking.management.services.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class ParkingServiceTests {
    private ParkingSpaceRepository parkingSpaceRepository;
    private ParkingService parkingService;

    @BeforeEach
    public void setup() {
        parkingSpaceRepository = Mockito.mock(ParkingSpaceRepository.class);
        parkingService = new ParkingService(parkingSpaceRepository);
    }

    @Test
    public void testGetAllFreeSpaces() {
        List<ParkingSpace> expected = List.of(new ParkingSpace());
        Mockito.when(parkingSpaceRepository.findByStatus("FREE")).thenReturn(expected);

        List<ParkingSpace> result = parkingService.getAllFreeSpaces();

        assertEquals(expected, result);
    }
}
