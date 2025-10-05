# MedSync - Serviço de Histórico de Consultas

Este é o serviço de histórico de consultas do sistema MedSync, responsável por armazenar e consultar o histórico médico dos pacientes, incluindo consultas finalizadas e agendadas. Recebe informações do serviço de cadastro e agendamento através de **mutations GraphQL**, proporcionando uma comunicação direta e síncrona para manter um registro completo das consultas e suas respectivas mudanças de estado.

## 🏗️ Arquitetura

O projeto segue os princípios de **Domain-Driven Design (DDD)** e **Clean Architecture**, com as seguintes camadas:

- **Domain**: Entidades, repositórios e regras de negócio do histórico médico
- **Application**: Casos de uso e orquestração para manipulação do histórico
- **Infrastructure**: Persistência no MongoDB e integrações externas
- **Presentation**: Controllers GraphQL (Queries e Mutations) e DTOs para consultas e manipulação do histórico

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3.5.5**
- **MongoDB** (banco de dados NoSQL)
- **Spring GraphQL** (API GraphQL para consultas e mutations)
- **MapStruct** (mapeamento de objetos)
- **Lombok** (redução de boilerplate)
- **Gradle** (gerenciamento de dependências)
- **Docker** (containerização)

## 📋 Pré-requisitos

- Java 21
- Gradle 8.5+
- Docker
- MongoDB 7.0+

## 🛠️ Configuração e Execução

### 1. Usando Docker (Recomendado)

```bash
# Clonar o repositório
git clone <repository-url>
cd historico-api

# Executar MongoDB via Docker
docker run -d --name mongodb-medsync \
  -e MONGO_INITDB_ROOT_USERNAME=medsync \
  -e MONGO_INITDB_ROOT_PASSWORD=medsync_password \
  -p 27017:27017 \
  mongo:7.0

# Executar a aplicação
./gradlew bootRun

# O serviço estará disponível em http://localhost:8081
# GraphiQL estará disponível em http://localhost:8081/graphiql
```

### 2. Execução com Docker Build

```bash
# 1. Construir a imagem
docker build -t historico-api .

# 2. Executar com MongoDB
docker run -d --name mongodb-medsync \
  -e MONGO_INITDB_ROOT_USERNAME=medsync \
  -e MONGO_INITDB_ROOT_PASSWORD=medsync_password \
  -p 27017:27017 \
  mongo:7.0

# 3. Executar a aplicação
docker run -p 8081:8081 --link mongodb-medsync:mongo historico-api
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
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "João Silva",
    "cpf": "12345678901",
    "email": "joao@email.com",
    "dateOfBirth": "1985-01-15"
  },
  "appointments": [
    {
      "id": "850e8400-e29b-41d4-a716-446655440225",
      "status": "AGENDADA",
      "doctor": {
        "id": "750e8400-e29b-41d4-a716-446655440123",
        "name": "Dr. Maria Santos",
        "specialty": "Cardiologia"
      },
      "created_by": {
        "id": "950e8400-e29b-41d4-a716-446655440456",
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
            "id": "950e8400-e29b-41d4-a716-446655440456",
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

O serviço expõe uma API GraphQL completa para consultas e manipulação do histórico médico:

### Queries Disponíveis

#### 1. Buscar Histórico Médico por ID do Paciente
```graphql
query {
  getMedicalHistoryByPatientId(patientId: "550e8400-e29b-41d4-a716-446655440000") {
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
  getAppointmentById(
    appointmentId: "850e8400-e29b-41d4-a716-446655440225", 
    patientId: "550e8400-e29b-41d4-a716-446655440000"
  ) {
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

### Mutations Disponíveis

#### 1. Criar Nova Consulta no Histórico
```graphql
mutation {
  saveNewAppointment(newAppointmentInput: {
    consultaId: "850e8400-e29b-41d4-a716-446655440225"
    dataHora: "2024-01-15T14:30:00"
    status: "AGENDADA"
    observacoes: "Consulta de rotina"
    tipoEvento: "CRIADA"
    timestamp: "2024-01-10T10:00:00"
    
    # Dados do paciente
    pacienteId: "550e8400-e29b-41d4-a716-446655440000"
    pacienteNome: "João Silva"
    pacienteCpf: "12345678901"
    pacienteEmail: "joao@email.com"
    pacienteDataNascimento: "1985-01-15"
    
    # Dados do médico
    medicoId: "750e8400-e29b-41d4-a716-446655440123"
    medicoNome: "Dr. Maria Santos"
    medicoCpf: "98765432100"
    medicoEmail: "maria.santos@medsync.com"
    medicoEspecialidade: "Cardiologia"
    
    # Dados do usuário
    usuarioId: "950e8400-e29b-41d4-a716-446655440456"
    usuarioNome: "Recepcionista Ana"
    usuarioEmail: "ana@medsync.com"
    usuarioRole: "ENFERMEIRO"
  }) {
    patient {
      id
      name
    }
    appointments {
      id
      status
      appointmentDateTime
    }
  }
}
```

#### 2. Atualizar Consulta Existente
```graphql
mutation {
  updateAppointment(updateAppointmentInput: {
    consultaId: "850e8400-e29b-41d4-a716-446655440225"
    dataHora: "2024-01-15T15:00:00"
    status: "CONFIRMADA"
    observacoes: "Consulta confirmada pelo paciente"
    tipoEvento: "EDITADA"
    timestamp: "2024-01-12T09:00:00"
    
    # Dados do paciente (obrigatórios)
    pacienteId: "550e8400-e29b-41d4-a716-446655440000"
    pacienteNome: "João Silva"
    pacienteCpf: "12345678901"
    pacienteEmail: "joao@email.com"
    pacienteDataNascimento: "1985-01-15"
    
    # Dados do médico (podem ser alterados)
    medicoId: "750e8400-e29b-41d4-a716-446655440123"
    medicoNome: "Dr. Maria Santos"
    medicoCpf: "98765432100"
    medicoEmail: "maria.santos@medsync.com"
    medicoEspecialidade: "Cardiologia"
    
    # Dados do usuário que fez a alteração
    usuarioId: "950e8400-e29b-41d4-a716-446655440456"
    usuarioNome: "Recepcionista Ana"
    usuarioEmail: "ana@medsync.com"
    usuarioRole: "ENFERMEIRO"
  }) {
    patient {
      id
      name
    }
    appointments {
      id
      status
      appointmentDateTime
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

### Enums Disponíveis

- **AppointmentStatus**: `AGENDADA`, `CONFIRMADA`, `CANCELADA`, `REALIZADA`, `FALTA`
- **ActionType**: `CREATION`, `EDITION`, `CANCELLATION`, `COMPLETION`
- **EventType**: `CRIADA`, `EDITADA`

## 🔄 Integração com Serviço de Agendamento

### Arquitetura de Comunicação
O serviço recebe chamadas diretas do serviço de agendamento através de **mutations GraphQL**:

#### Fluxo de Integração
```
Serviço de Cadastro/Agendamento → HTTP/GraphQL → MedicalHistoryController → MedicalHistoryService
                                                         ↓
                                               Atualiza/Cria Histórico no MongoDB
```

### Tipos de Operações

#### Criação de Consulta
- **Mutation**: `saveNewAppointment`
- **Validação**: Verifica se `tipoEvento = "CRIADA"`
- **Comportamento**: 
  - Se histórico médico existe → Adiciona nova consulta
  - Se histórico não existe → Cria novo histórico com a consulta

#### Atualização de Consulta
- **Mutation**: `updateAppointment`
- **Validação**: Verifica se `tipoEvento = "EDITADA"`
- **Comportamento**: 
  - Localiza consulta existente no histórico
  - Atualiza campos modificados
  - Adiciona log de ação com timestamp e usuário

### Tratamento de Erros

#### GraphQL Exception Resolver
- **Validação de Input**: Tipos de evento inválidos retornam `BAD_REQUEST`
- **Recursos Não Encontrados**: Retorna `NOT_FOUND` com mensagem específica
- **Erros Internos**: Tratamento e logging de exceções inesperadas

#### Logs de Auditoria
- **Criação**: Log automático com `ActionType.CREATION`
- **Edição**: Log automático com `ActionType.EDITION`
- **Rastreabilidade**: Cada ação registra usuário, timestamp e tipo de operação

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
// Exemplo de logs no MedicalHistoryController
log.info("New appointment added in medical history with ID: {}", medicalHistory.getId());
log.info("Medical history updated with ID: {}", medicalHistory.getId());
log.error("Erro ao processar requisição GraphQL: {}", input, e);
```

### Níveis de Log
- **INFO**: Processamento normal de mutations e queries
- **WARN**: Validações de tipo de evento
- **ERROR**: Falhas no processamento de requisições GraphQL

## 🔧 Configurações

### Variáveis de Ambiente

```bash
# MongoDB
SPRING_DATA_MONGODB_URI=mongodb://medsync:medsync_password@localhost:27017/medsync_db?authSource=admin
MONGO_USERNAME=medsync
MONGO_PASSWORD=medsync_password
MONGO_HOST=localhost
MONGO_PORT=27017
MONGO_DB=medsync_db

# Configurações da Aplicação
app.name=medsync-historico
app.version=1.0.0
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
   - Verificar se o MongoDB está rodando: `docker ps | grep mongo`
   - Verificar credenciais no `application.yml`
   - Testar conexão: `mongosh mongodb://medsync:medsync_password@localhost:27017/medsync_db?authSource=admin`

2. **Erro ao executar mutations GraphQL**
   - Verificar se o GraphiQL está acessível: http://localhost:8081/graphiql
   - Validar sintaxe da mutation e tipos de dados
   - Verificar se todos os campos obrigatórios estão preenchidos
   - Confirmar se o `tipoEvento` é válido ("CRIADA" ou "EDITADA")

3. **Erro ao consultar histórico existente**
   - Verificar se o `patientId` está correto
   - Verificar se os dados existem no MongoDB
   - Validar formato dos IDs (devem ser strings)

4. **Erro de validação de tipos de evento**
   - Verificar se `tipoEvento` para `saveNewAppointment` é "CRIADA"
   - Verificar se `tipoEvento` para `updateAppointment` é "EDITADA"
   - Caso contrário, será retornado erro `BAD_REQUEST`

## 🎯 Funcionalidades Principais

### ✅ API GraphQL Completa
- **Queries**: Busca de histórico completo por paciente e consultas específicas
- **Mutations**: Criação e atualização de consultas no histórico
- **Interface GraphiQL**: Para testes e desenvolvimento
- **Tratamento de Erros**: Sistema robusto de exception handling

### ✅ Integração Direta com Serviço de Agendamento
- Comunicação síncrona via HTTP/GraphQL
- Validação de tipos de evento em tempo real
- Resposta imediata sobre sucesso/falha das operações

### ✅ Persistência MongoDB Otimizada
- Armazenamento desnormalizado para performance
- Estrutura flexível para diferentes tipos de consulta
- Índices otimizados para consultas por paciente
- Suporte a IDs como strings (UUIDs)

### ✅ Auditoria e Rastreabilidade
- Log automático de todas as ações em cada consulta
- Histórico completo de mudanças de status
- Identificação do usuário que realizou cada ação
- Timestamps precisos para todas as operações

### ✅ Gestão Inteligente do Histórico
- Criação automática de histórico médico quando não existe
- Adição de consultas a históricos existentes
- Atualização de consultas com manutenção do histórico de ações

## 📚 Documentação Adicional

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring GraphQL Documentation](https://spring.io/projects/spring-graphql)
- [MongoDB Documentation](https://www.mongodb.com/docs/)
- [GraphQL Documentation](https://graphql.org/learn/)
- [MapStruct Documentation](https://mapstruct.org/documentation/)

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.