package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dao.RemoteDataMapper;
import com.intent_exchange.drone_highway.dto.request.LinkageInformationNotificationDto;
import com.intent_exchange.drone_highway.dto.request.LinkageInformationNotificationUasIdDto;
import com.intent_exchange.drone_highway.model.RemoteData;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class RemoteDataLogicTest {

  @Mock
  private RemoteDataMapper mapper;

  @InjectMocks
  private RemoteDataLogic logic;

  @Test
  @DisplayName("新しい予約IDとリモートIDの紐づけ情報を登録する")
  void testUpsert1() {
    LinkageInformationNotificationDto dto = new LinkageInformationNotificationDto();
    LinkageInformationNotificationUasIdDto uasId = new LinkageInformationNotificationUasIdDto();

    uasId.setSerialNumber("serialNumber01");
    uasId.setRegistrationId("registrationId01");
    uasId.setUtmId("utmId01");
    uasId.setSpecificSessoionId("specificSessionId01");

    dto.setAirwayReservationId("airwayReservationId01");
    dto.setUasId(uasId);
    dto.setAircraftInfoId(12345);

    RemoteData map = new RemoteData();
    map.setAirwayReservationId("airwayReservationId01");
    map.setSerialNumber("serialNumber01");
    map.setRegistrationId("registrationId01");
    map.setUtmId("utmId01");
    map.setSpecificSessoionId("specificSessionId01");
    map.setAircraftInfoId(12345);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, RemoteData.class)).thenReturn(map);
      when(mapper.upsertSelective(map)).thenReturn(1);
      assertDoesNotThrow(() -> logic.upsert(dto));
    }
  }

  @Test
  @DisplayName("予約IDとリモートIDの紐づけ情報を削除する")
  void testDelete1() {
    String airwayReservationId = "airwayReservationId01";
    when(mapper.deleteByPrimaryKey(airwayReservationId)).thenReturn(1);
    assertDoesNotThrow(() -> logic.delete(airwayReservationId));
  }

}
