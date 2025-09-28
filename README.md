# MedSync - Serviço de Histórico de Consultas

Este é o serviço de histórico de consultas do sistema MedSync, responsável por armazenar e consultar o histórico médico dos pacientes, incluindo consultas finalizadas e agendadas. Recebe informações do serviço de cadastro e agendamento através do RabbitMQ para manter um registro completo das consultas e suas respectivas mudanças de estado.

## 🏗️ Arquitetura

O projeto segue os princípios de **Domain-Driven Design (DDD)** e **Clean Architecture**, com as seguintes camadas:

- **Domain**: Entidades, repositórios e regras de negócio do histórico médico
- **Application**: Casos de uso e orquestração para manipulação do histórico
- **Infrastructure**: Persistência no MongoDB, configurações RabbitMQ e integrações externas
- **Presentation**: Controllers GraphQL e DTOs para consultas do histórico

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3.5.5**
- **MongoDB** (banco de dados NoSQL)
- **Spring GraphQL** (API GraphQL para consultas)
- **RabbitMQ** (message broker para eventos)
- **MapStruct** (mapeamento de objetos)
- **Lombok** (redução de boilerplate)
- **Gradle** (gerenciamento de dependências)
- **Docker & Docker Compose** (containerização)

## 📋 Pré-requisitos

- Java 21
- Gradle 8.5+
- Docker e Docker Compose
- MongoDB 7.0+
- RabbitMQ 3.12+

## 🛠️ Configuração e Execução

### 1. Usando Docker Compose (Recomendado)

```bash
# Clonar o repositório
git clone <repository-url>
cd historico-api

# Executar com Docker Compose
docker-compose up -d

# O serviço estará disponível em http://localhost:8081
# GraphiQL estará disponível em http://localhost:8081/graphiql
```

### 2. Execução Local

```bash
# 1. Iniciar MongoDB
docker-compose up -d db

# 2. Compilar o projeto
./gradlew build

# 3. Executar a aplicação
./gradlew bootRun
```

## 📊 Banco de Dados

### Estrutura do MongoDB

O serviço utiliza MongoDB com as seguintes collections:

#### Collection `medical_histories`
Documento principal que armazena o histórico médico completo do paciente:

```json
{
  "_id": "ObjectId",
  "patient": {
    "id": 123,
    "name": "João Silva",
    "cpf": "12345678901",
    "email": "joao@email.com",
    "dateOfBirth": "1985-01-15"
  },
  "appointments": [
    {
      "id": 456,
      "status": "AGENDADA",
      "doctor": {
        "id": 789,
        "name": "Dr. Maria Santos",
        "specialty": "Cardiologia"
      },
      "created_by": {
        "id": 101,
        "name": "Recepcionista Ana",
        "email": "ana@medsync.com",
        "role": "ENFERMEIRO"
      },
      "appointment_datetime": "2023-12-15T14:30:00",
      "action_logs": [
        {
          "actionType": "CREATION",
          "timestamp": "2023-12-10T10:00:00",
          "user": {
            "id": 101,
            "name": "Recepcionista Ana",
            "email": "ana@medsync.com",
            "role": "ENFERMEIRO"
          }
        }
      ]
    }
  ]
}
```

### Campos e Relacionamentos

- **Patient**: Dados básicos do paciente
- **Appointments**: Lista de todas as consultas do paciente
- **ActionLogs**: Histórico de todas as ações realizadas em cada consulta (criação, edição, cancelamento, etc.)

## 🔌 API GraphQL

O serviço expõe uma API GraphQL para consultas do histórico médico:

### Queries Disponíveis

#### 1. Buscar Histórico Médico por ID do Paciente
```graphql
query {
  getMedicalHistoryByPatientId(patientId: "123") {
    patient {
      id
      name
      cpf
      email
      dateOfBirth
    }
    appointments {
      id
      appointmentDateTime
      status
      doctor {
        id
        name
        specialty
      }
      actionLogs {
        actionType
        timestamp
        user {
          name
          role
        }
      }
    }
  }
}
```

#### 2. Buscar Consulta Específica por ID
```graphql
query {
  getAppointmentById(appointmentId: "456", patientId: "123") {
    id
    appointmentDateTime
    status
    doctor {
      id
      name
      specialty
    }
  }
}
```

### Enums Disponíveis

- **AppointmentStatus**: `AGENDADA`, `CONFIRMADA`, `CANCELADA`, `REALIZADA`, `FALTA`
- **ActionType**: `CREATION`, `EDITION`, `CANCELLATION`, `COMPLETION`

## 📨 Sistema de Eventos e Mensageria

### Arquitetura de Eventos
O serviço consome eventos do RabbitMQ para manter o histórico atualizado:

#### Eventos Consumidos
- **`AppointmentEvent`**: Evento completo com dados da consulta, paciente, médico e usuário

#### Fluxo de Eventos
```
Serviço de Cadastro/Agendamento → RabbitMQ → AppointmentConsumer → MedicalHistoryService
                                              ↓
                                    Atualiza/Cria Histórico no MongoDB
```

### Configuração RabbitMQ

#### Exchange
- `ex_consultas`: Exchange principal para eventos de consultas

#### Fila
- `q_historico_consultas`: Fila específica para o serviço de histórico

#### Routing Key
- `consulta.historico`: Para eventos direcionados ao histórico

#### Tipos de Eventos Processados
- **CREATION**: Criação de nova consulta → Adiciona ao histórico
- **EDITION**: Edição de consulta existente → Atualiza histórico e adiciona log de ação

### Consumer Configuration
- **Acknowledgment Manual**: Para garantia de processamento
- **JSON Message Converter**: Para deserialização automática
- **Error Handling**: Logging detalhado para troubleshooting

## 🧪 Testes

O projeto possui uma suíte abrangente de testes unitários seguindo Clean Architecture:

### Estrutura dos Testes

```
src/test/java/com/medsync/historico/
├── application/
│   ├── services/
│   │   └── MedicalHistoryServiceTest.java
│   └── usecases/
│       ├── CreateMedicalHistoryUseCaseTest.java
│       ├── SaveNewAppointmentUseCaseTest.java
│       ├── UpdateAppointmentUseCaseTest.java
│       └── GetAppointmentByIdUseCaseTest.java
```

### Cobertura de Testes

#### Testes de Use Cases
- ✅ **Criação de Histórico**: Criação de novo histórico médico
- ✅ **Adição de Consulta**: Adicionar consulta ao histórico existente
- ✅ **Atualização de Consulta**: Atualizar dados de consulta existente
- ✅ **Consulta por ID**: Buscar consulta específica
- ✅ **Tratamento de Exceções**: Cenários de erro e validações

#### Testes de Services
- ✅ **Orquestração**: Chamadas corretas aos use cases
- ✅ **Fallback**: Criação automática quando histórico não existe
- ✅ **Integração**: Verificação entre camadas

### Executando os Testes

```bash
# Executar todos os testes
./gradlew test

# Executar testes específicos
./gradlew test --tests "*UseCaseTest"

# Executar com relatório de cobertura
./gradlew test jacocoTestReport
```

### Configuração de Teste

- **JUnit 5**: Framework de testes
- **Mockito**: Para mocks e stubs
- **Spring Test**: Contexto de teste do Spring
- **Embedded MongoDB**: Para testes de integração

## 📝 Logs

Sistema de logging estruturado para rastreabilidade:

```java
// Exemplo de logs no AppointmentConsumer
log.info("Evento de criação recebido para o agendamento ID: {}", event.consultaId());
log.info("Histórico médico criado/atualizado com ID: {}", medicalHistory.getId());
log.error("Erro ao processar o evento: {}", event, e);
```

### Níveis de Log
- **INFO**: Processamento normal de eventos
- **WARN**: Tipos de eventos desconhecidos
- **ERROR**: Falhas no processamento

## 🔧 Configurações

### Variáveis de Ambiente

```bash
# MongoDB
SPRING_DATA_MONGODB_URI=mongodb://medsync:medsync_password@localhost:27017/medsync_db?authSource=admin

# RabbitMQ
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
RABBITMQ_VHOST=/

# Configurações da Aplicação
app.rabbitmq.exchange-consultas=ex_consultas
app.rabbitmq.queue-historico=q_historico_consultas
app.rabbitmq.routing-key-historico=consulta.historico
```

### Configuração GraphQL

```yaml
spring:
  graphql:
    graphiql:
      enabled: true
    http:
      path: /graphql
    schema:
      locations: classpath:graphql/**/
      file-extensions: .graphqls
```

## 🐛 Troubleshooting

### Problemas Comuns

1. **Erro de conexão com MongoDB**
   - Verificar se o MongoDB está rodando: `docker-compose ps`
   - Verificar credenciais no `application.yml`
   - Testar conexão: `mongosh mongodb://localhost:27017`

2. **Erro de conexão com RabbitMQ**
   - Verificar se o RabbitMQ está rodando
   - Acessar interface de management: http://localhost:15672
   - Verificar se as filas estão criadas corretamente

3. **Eventos não sendo consumidos**
   - Verificar logs do consumer
   - Verificar se a fila `q_historico_consultas` existe
   - Verificar routing key e binding

4. **Erro ao consultar GraphQL**
   - Verificar se o GraphiQL está acessível: http://localhost:8081/graphiql
   - Validar sintaxe da query
   - Verificar se os dados existem no MongoDB

## 🎯 Funcionalidades Principais

### ✅ Consumo de Eventos
- Processamento automático de eventos de criação/edição de consultas
- Criação automática de histórico médico quando não existe
- Manutenção de logs de ações para auditoria

### ✅ Consultas GraphQL
- Busca de histórico completo por paciente
- Consulta de consultas específicas
- Interface GraphiQL para testes

### ✅ Persistência MongoDB
- Armazenamento desnormalizado para performance
- Estrutura flexível para diferentes tipos de consulta
- Índices otimizados para consultas por paciente

### ✅ Rastreabilidade
- Log completo de todas as ações em cada consulta
- Histórico de mudanças de status
- Identificação do usuário que realizou cada ação

## 📚 Documentação Adicional

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring GraphQL Documentation](https://spring.io/projects/spring-graphql)
- [MongoDB Documentation](https://www.mongodb.com/docs/)
- [RabbitMQ Documentation](https://www.rabbitmq.com/documentation.html)
- [MapStruct Documentation](https://mapstruct.org/documentation/)

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.