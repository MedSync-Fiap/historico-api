package com.medsync.historico.infrastructure.messaging;

import com.medsync.historico.application.dto.AppointmentEvent;
import com.medsync.historico.application.services.MedicalHistoryService;
import com.medsync.historico.domain.entities.MedicalHistory;
import com.medsync.historico.domain.enums.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.medsync.historico.domain.enums.EventType.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppointmentConsumer {

    private final MedicalHistoryService medicalHistoryService;

    @RabbitListener(queues = "${app.rabbitmq.queue-historico}")
    public void onAppointmentCreated(AppointmentEvent event) {
        try {

            EventType eventType = fromValue(event.tipoEvento());

            switch (eventType) {
                case CREATION -> {
                    log.info("Evento de criação recebido para o agendamento ID: {}", event.consultaId());
                    MedicalHistory medicalHistory = medicalHistoryService.addAppointmentInMedicalHistory(event);
                    log.info("Histórico médico criado/atualizado com ID: {}", medicalHistory.getId());
                }
                case EDITION -> {
                    log.info("Evento de edição recebido para o agendamento ID: {}", event.consultaId());
                    // TODO: Implementar lógica de edição
                    log.info("Histórico médico atualizado com ID:");
                }
                default -> log.warn("Tipo de evento desconhecido: {}", event.tipoEvento());
            }

        } catch (Exception e) {
            log.error("Erro ao processar o evento de criação de agendamento: {}", event, e);
        }
    }

}
