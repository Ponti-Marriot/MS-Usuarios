package com.pontimarriott.ms_usuarios.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pontimarriott.ms_usuarios.Model.Empleado;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;

@Service
public class MessageEmpleadoService {

  @Value("${spring.cloud.stream.bindings.sendEmpleado-out-0.destination}")
  private String queueName;

  @Autowired
  private StreamBridge streamBridge;

  public boolean sendMessageEmpleado(Empleado empleado) {
    return streamBridge.send(
        queueName,
        MessageBuilder
            .withPayload(empleado)
            .setHeader("eventType", "EMPLOYEE_CREATED")  // Indica que es creación
            .build()
    );
}

}
