package com.parking.management;

import com.parking.management.entities.ParkingSpace;
import com.parking.management.repositories.ParkingSpaceRepository;
import com.parking.management.services.ParkingSpaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class ParkingServiceTests {
    private ParkingSpaceRepository parkingSpaceRepository;
    private ParkingSpaceService parkingService;

    @BeforeEach
    public void setup() {
        parkingSpaceRepository = Mockito.mock(ParkingSpaceRepository.class);
        parkingService = new ParkingSpaceService(parkingSpaceRepository);
    }

    @Test
    public void testGetAllFreeSpaces() {
        List<ParkingSpace> expected = List.of(new ParkingSpace(ParkingSpace.Status.FREE));
        Mockito.when(parkingSpaceRepository.findByStatus(ParkingSpace.Status.FREE)).thenReturn(expected);

        List<ParkingSpace> result = parkingService.getAllFreeSpaces();

        assertEquals(expected, result);
    }
}
