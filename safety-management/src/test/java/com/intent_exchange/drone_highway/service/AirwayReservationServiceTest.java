package com.intent_exchange.drone_highway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.logic.IdidLogic;

@ExtendWith(MockitoExtension.class)
class AirwayReservationServiceTest {

  @Mock
  private IdidLogic ididLogic;

  @InjectMocks
  private AirwayReservationService service;


  @Test
  @DisplayName("予約情報の削除")
  void testDeleteAirwayReservation1() {
    String airwayReservationId = "abc-123";
    doNothing().when(ididLogic).delete(airwayReservationId);

    assertDoesNotThrow(() -> service.deleteAirwayReservation(airwayReservationId));
  }

}
