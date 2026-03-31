package com.intent_exchange.uasl.task;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;



@ExtendWith(MockitoExtension.class)
public class AbstractTaskControllerTest {

  @Mock
  private ThreadPoolTaskScheduler taskScheduler;

  @InjectMocks
  private AbstractTaskController abstractTaskController = new AbstractTaskController() {
    @Override
    public void performTask() {}
  };

  @Test
  @DisplayName("taskSchedulerがnullではない")
  void testOnDestroy() {
    abstractTaskController.onDestroy();
    verify(taskScheduler, times(1)).shutdown();
  }

  @Test
  @DisplayName("taskSchedulerがnull")
  void testOnDestroyWithNullTaskScheduler() {
    abstractTaskController.taskScheduler = null;
    abstractTaskController.onDestroy();
    verify(taskScheduler, never()).shutdown();
  }

}
