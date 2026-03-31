package com.intent_exchange.uasl.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dao.RemoteDataMapper;
import com.intent_exchange.uasl.dto.request.LinkageInformationNotificationDto;
import com.intent_exchange.uasl.dto.request.LinkageInformationNotificationUasIdDto;
import com.intent_exchange.uasl.model.RemoteData;
import com.intent_exchange.uasl.util.ModelMapperUtil;

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
    uasId.setSpecificSessionId("specificSessionId01");

    dto.setUaslReservationId("uaslReservationId01");
    dto.setUasId(uasId);
    dto.setAircraftInfoId(12345);

    RemoteData map = new RemoteData();
    map.setUaslReservationId("uaslReservationId01");
    map.setSerialNumber("serialNumber01");
    map.setRegistrationId("registrationId01");
    map.setUtmId("utmId01");
    map.setSpecificSessionId("specificSessionId01");
    map.setAircraftInfoId(12345);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, RemoteData.class)).thenReturn(map);
      when(mapper.upsertSelective(map)).thenReturn(1);
      assertDoesNotThrow(() -> logic.upsert(dto));
    }
  }

  @Test
  @DisplayName("新しい予約IDとリモートIDの紐づけ情報を登録する(uasIdがnullの場合）")
  void testUpsert2() {
    LinkageInformationNotificationDto dto = new LinkageInformationNotificationDto();

    dto.setUaslReservationId("uaslReservationId01");
    dto.setUasId(null);
    dto.setAircraftInfoId(12345);

    RemoteData map = new RemoteData();
    map.setUaslReservationId("uaslReservationId01");
    map.setSerialNumber("serialNumber01");
    map.setRegistrationId("registrationId01");
    map.setUtmId("utmId01");
    map.setSpecificSessionId("specificSessionId01");
    map.setAircraftInfoId(12345);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, RemoteData.class)).thenReturn(map);
      when(mapper.upsertSelective(map)).thenReturn(1);
      assertDoesNotThrow(() -> logic.upsert(dto));
    }
  }

  @Test
  @DisplayName("新しい予約IDとリモートIDの紐づけ情報を登録する（requestIdあり）")
  void testUpsert3_withRequestId() {
    LinkageInformationNotificationDto dto = new LinkageInformationNotificationDto();
    LinkageInformationNotificationUasIdDto uasId = new LinkageInformationNotificationUasIdDto();

    uasId.setRegistrationId("registrationId03");

    dto.setUaslReservationId("uaslReservationId03");
    dto.setUasId(uasId);
    dto.setAircraftInfoId(999);
    dto.setRequestId("REQ-003"); // L45: requestId is now persisted

    RemoteData map = new RemoteData();
    map.setUaslReservationId("uaslReservationId03");
    map.setRegistrationId("registrationId03");
    map.setAircraftInfoId(999);
    map.setRequestId("REQ-003");

    ArgumentCaptor<RemoteData> captor = ArgumentCaptor.forClass(RemoteData.class);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, RemoteData.class)).thenReturn(map);
      when(mapper.upsertSelective(any(RemoteData.class))).thenReturn(1);
      assertDoesNotThrow(() -> logic.upsert(dto));
      verify(mapper).upsertSelective(captor.capture());
      RemoteData captured = captor.getValue();
      assertEquals("uaslReservationId03", captured.getUaslReservationId());
      assertEquals("registrationId03", captured.getRegistrationId());
      assertEquals(999, captured.getAircraftInfoId());
      assertEquals("REQ-003", captured.getRequestId());
    }
  }

  @Test
  @DisplayName("新しい予約IDとリモートIDの紐づけ情報を登録する（requestIdがnull）")
  void testUpsert4_nullRequestId() {
    LinkageInformationNotificationDto dto = new LinkageInformationNotificationDto();
    LinkageInformationNotificationUasIdDto uasId = new LinkageInformationNotificationUasIdDto();

    uasId.setRegistrationId("registrationId04");

    dto.setUaslReservationId("uaslReservationId04");
    dto.setUasId(uasId);
    dto.setAircraftInfoId(888);
    dto.setRequestId(null); // requestId is nullable per spec

    RemoteData map = new RemoteData();
    map.setUaslReservationId("uaslReservationId04");
    map.setRegistrationId("registrationId04");
    map.setAircraftInfoId(888);
    map.setRequestId(null);

    ArgumentCaptor<RemoteData> captor = ArgumentCaptor.forClass(RemoteData.class);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, RemoteData.class)).thenReturn(map);
      when(mapper.upsertSelective(any(RemoteData.class))).thenReturn(1);
      assertDoesNotThrow(() -> logic.upsert(dto));
      verify(mapper).upsertSelective(captor.capture());
      RemoteData captured = captor.getValue();
      assertEquals("uaslReservationId04", captured.getUaslReservationId());
      assertEquals("registrationId04", captured.getRegistrationId());
      assertEquals(888, captured.getAircraftInfoId());
      assertNull(captured.getRequestId());
    }
  }

  @Test
  @DisplayName("予約IDとリモートIDの紐づけ情報を削除する")
  void testDelete1() {
    String uaslReservationId = "uaslReservationId01";
    when(mapper.deleteByPrimaryKey(uaslReservationId)).thenReturn(1);
    assertDoesNotThrow(() -> logic.delete(uaslReservationId));
  }

}
