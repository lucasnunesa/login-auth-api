# Login Auth API

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen?style=flat-square)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red?style=flat-square)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Production%20Ready-success?style=flat-square)]()

## 📋 Tabela de Conteúdo

- [Sobre](#sobre)
- [Características](#características)
- [Arquitetura](#arquitetura)
- [Segurança](#segurança)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [API Documentation](#api-documentation)
- [Utilização](#utilização)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Autenticação](#autenticação)
- [Rate Limiting](#rate-limiting)
- [Logging](#logging)
- [Testes](#testes)
- [Troubleshooting](#troubleshooting)
- [Contribuindo](#contribuindo)
- [Licença](#licença)

---

## 🎯 Sobre

**Login Auth API** é uma API RESTful robusta para autenticação de usuários e geração de tokens JWT. Desenvolvida com Spring Boot 4.1.0 e Java 21, a aplicação oferece recursos avançados de segurança, tratamento centralizado de erros e documentação automática via Swagger.

A API foi projetada com foco em:
- 🔐 **Segurança em primeiro lugar**
- 📊 **Logs estruturados**
- 🚀 **Performance otimizada**
- 📚 **Documentação completa**
- 🧪 **Testes abrangentes**

---

## ✨ Características

### Autenticação e Autorização
- ✅ Registro de novos usuários com validações
- ✅ Login com geração de JWT
- ✅ Refresh token com duração estendida
- ✅ Acesso token com duração curta
- ✅ Validação automatizada de credenciais

### Segurança
- ✅ Senhas encriptadas com bcrypt (Password4j)
- ✅ Email único por usuário
- ✅ Rate limiting contra brute force (10 req/min)
- ✅ Tratamento centralizado de exceções
- ✅ Logs de segurança estruturados
- ✅ Validações de entrada com Jakarta Validation

### API
- ✅ Respostas padronizadas
- ✅ Documentação automática com Swagger 3.0
- ✅ DTOs específicos para requisições e respostas
- ✅ Exceções customizadas
- ✅ Timestamps de auditoria (createdAt, updatedAt)

### Desenvolvimento
- ✅ Testes unitários com JUnit 5
- ✅ Cobertura de testes
- ✅ Logs estruturados com SLF4J
- ✅ Database H2 para testes
- ✅ PostgreSQL para produção

---

## 🏗️ Arquitetura

### Padrão Arquitetural: MVC com Camadas

```
┌─────────────────────────────────────────────────────┐
│                   CONTROLLER LAYER                   │
│             (AuthController, REST Endpoints)         │
└──────────────────────┬──────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────┐
│                   SERVICE LAYER                      │
│      (TokenService, AuthService, UserService)       │
└──────────────────────┬──────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────┐
│               REPOSITORY LAYER                       │
│              (UserRepository, JPA)                   │
└──────────────────────┬──────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────┐
│                DATABASE LAYER                        │
│           (PostgreSQL / H2 para testes)             │
└─────────────────────────────────────────────────────┘
```

### Stack Tecnológico

```
Frontend           ↔    API Gateway/Load Balancer
                              ↓
                        Spring Boot 4.1.0
                              ↓
                 ┌────────────┬────────────┐
                 ↓            ↓            ↓
            Controller     Security     Exception Handler
                 ↓            ↓            ↓
            Service Layer   JWT Token   Logger
                 ↓            ↓            ↓
            Repository      Database    Audit Trail
```

### Componentes Principais

| Componente | Responsabilidade | Localização |
|---|---|---|
| **AuthController** | Endpoints de autenticação | `controller/` |
| **TokenService** | Geração e validação de tokens JWT | `infra/security/` |
| **SecurityConfig** | Configuração de segurança Spring | `infra/security/` |
| **GlobalExceptionHandler** | Tratamento centralizado de exceções | `handler/` |
| **RateLimitInterceptor** | Proteção contra brute force | `infra/security/` |
| **User Entity** | Modelo de dados do usuário | `domain/user/` |
| **UserRepository** | Operações de BD | `repository/` |

---

## 🔐 Segurança

### Camadas de Segurança

#### 1. **Nível de Entrada**
```
Rate Limiting (10 req/min por IP)
         ↓
Validação de Input (Jakarta Validation)
         ↓
HTTPS/TLS (em produção)
```

#### 2. **Nível de Autenticação**
```
Email único no banco de dados
         ↓
Senha encriptada com bcrypt
         ↓
JWT com assinatura HMAC-SHA256
         ↓
Access Token (10 min) + Refresh Token (7 dias)
```

#### 3. **Nível de Aplicação**
```
Exceções customizadas
         ↓
Logging estruturado
         ↓
Tratamento centralizado de erros
```

#### 4. **Nível de Banco de Dados**
```
Constraints (UNIQUE, NOT NULL)
         ↓
Índices otimizados
         ↓
Audit timestamps (createdAt, updatedAt)
```

### Tecnologias de Segurança Utilizadas

| Tecnologia | Uso | Versão |
|---|---|---|
| **Spring Security** | Framework de segurança | 7.1.0 |
| **Password4j** | Encriptação de senhas | 1.8.2 |
| **JWT (Auth0)** | Tokens JWT | 4.4.0 |
| **Jakarta Validation** | Validação de dados | 3.0 |
| **Bucket4j** | Rate limiting | 7.6.0 |

### Boas Práticas Implementadas

✅ **Passwords:** Nunca retornadas em responses
✅ **Tokens:** Expiração automática
✅ **Logs:** Não registram senhas ou tokens sensíveis
✅ **CORS:** Configurável por ambiente
✅ **HTTPS:** Obrigatório em produção
✅ **SQL Injection:** Protegido via JPA Parameterizado

---

## 📦 Pré-requisitos

### Sistema Operacional
- Windows 10 ou superior
- macOS 11 ou superior
- Linux (Ubuntu 18.04+)

### Software Necessário
- **Java 21 JDK** - [Download](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven 3.8+** - [Download](https://maven.apache.org/download.cgi)
- **Git** - [Download](https://git-scm.com/)

### Opcionais
- **Docker** - Para containerização
- **PostgreSQL 14+** - Para ambiente de produção
- **Postman** - Para testar a API
- **IDE** - IntelliJ IDEA, Eclipse ou VS Code

### Verificar Instalação

```bash
# Verificar Java
java -version
# Output esperado: openjdk version "21" 2023-09-19

# Verificar Maven
mvn -v
# Output esperado: Apache Maven 3.8+

# Verificar Git
git --version
# Output esperado: git version 2.x.x
```

---

## 🚀 Instalação

### 1. Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/login-auth-api.git
cd login-auth-api
```

### 2. Verificar Estrutura

```bash
ls -la
# Esperado: pom.xml, src/, target/, mvnw, etc.
```

### 3. Instalar Dependências

```bash
mvn clean install
```

**Saída esperada:**
```
[INFO] Building login-auth-api 0.0.1-SNAPSHOT
[INFO] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
[INFO] BUILD SUCCESS
```

### 4. Compilar o Projeto

```bash
mvn clean compile
```

### 5. Executar Testes

```bash
mvn test
```

---

## ⚙️ Configuração

### Arquivo de Configuração Principal

Crie ou edite `src/main/resources/application.properties`:

```properties
# ===== SERVIDOR =====
server.port=8080
server.servlet.context-path=/
spring.application.name=login-auth-api

# ===== BANCO DE DADOS =====
# H2 (Padrão - Testes)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# PostgreSQL (Produção)
# spring.datasource.url=jdbc:postgresql://localhost:5432/login_auth_db
# spring.datasource.username=postgres
# spring.datasource.password=seu_senha
# spring.datasource.driverClassName=org.postgresql.Driver
# spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# ===== JPA/HIBERNATE =====
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# ===== SEGURANÇA =====
api.security.token.secret=sua_chave_secreta_super_longa_e_segura_aqui_minimo_32_caracteres

# ===== LOGGING =====
logging.level.root=INFO
logging.level.dev.nunes.login_auth_api=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n

# ===== JACKSON =====
spring.jackson.serialization.write-dates-as-timestamps=false
spring.jackson.time-zone=America/Sao_Paulo

# ===== SWAGGER =====
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### Configuração YAML (Alternativo)

Crie `src/main/resources/application.yml`:

```yaml
server:
  port: 8080
  servlet:
    context-path: /
spring:
  application:
    name: login-auth-api
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
    show-sql: false
  jackson:
    serialization:
      write-dates-as-timestamps: false
    time-zone: America/Sao_Paulo

api:
  security:
    token:
      secret: sua_chave_secreta_super_longa_e_segura_aqui_minimo_32_caracteres

logging:
  level:
    root: INFO
    dev.nunes.login_auth_api: DEBUG
```

### Variáveis de Ambiente

```bash
# Linux/macOS
export API_TOKEN_SECRET="sua_chave_secreta"
export DB_URL="jdbc:postgresql://localhost:5432/login_auth_db"
export DB_USER="postgres"
export DB_PASSWORD="senha"

# Windows
set API_TOKEN_SECRET=sua_chave_secreta
set DB_URL=jdbc:postgresql://localhost:5432/login_auth_db
set DB_USER=postgres
set DB_PASSWORD=senha
```

---

## 📚 API Documentation

### Acessar Swagger UI

Após iniciar a aplicação, acesse:

```
http://localhost:8080/swagger-ui.html
```

### OpenAPI JSON Schema

```
http://localhost:8080/v3/api-docs
```

### Documentação em YAML

```
http://localhost:8080/v3/api-docs.yaml
```

---

## 🎯 Utilização

### 1. Iniciar a Aplicação

#### Opção A: Maven
```bash
mvn spring-boot:run
```

#### Opção B: IDE (IntelliJ/Eclipse)
- Clique direito em `LoginAuthApiApplication.java`
- Selecione "Run"

#### Opção C: JAR Compilado
```bash
mvn clean package -DskipTests
java -jar target/login-auth-api-0.0.1-SNAPSHOT.jar
```

### 2. Verificar se está Rodando

```bash
curl -i http://localhost:8080/api/v1/auth/login
```

Esperado: Retorno 400 Bad Request (pois não enviou dados)

### 3. Registrar Novo Usuário

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "Senha@123"
  }'
```

**Response (201 Created):**
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 600,
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "João Silva",
    "email": "joao@example.com",
    "created_at": "2026-06-24T00:10:00",
    "updated_at": "2026-06-24T00:10:00"
  }
}
```

### 4. Fazer Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "Senha@123"
  }'
```

### 5. Renovar Access Token

```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }'
```

### 6. Usar Access Token

```bash
curl -X GET http://localhost:8080/api/v1/users/profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Exemplos com Postman

#### Import Collection
1. Abra Postman
2. Clique em "Import"
3. URL: `http://localhost:8080/v3/api-docs`
4. Clique "Import"

#### Collection gerada automaticamente

A collection será criada com todos os endpoints.

---

## 📂 Estrutura do Projeto

```
login-auth-api/
├── src/
│   ├── main/
│   │   ├── java/dev/nunes/login_auth_api/
│   │   │   ├── controller/
│   │   │   │   └── AuthController.java          # Endpoints de autenticação
│   │   │   ├── domain/
│   │   │   │   └── user/
│   │   │   │       └── User.java                # Entidade Usuario
│   │   │   ├── exception/
│   │   │   │   ├── UserAlreadyExistsException.java
│   │   │   │   ├── InvalidCredentialsException.java
│   │   │   │   └── TokenExpiredException.java
│   │   │   ├── handler/
│   │   │   │   └── GlobalExceptionHandler.java  # Tratamento centralizado
│   │   │   ├── infra/
│   │   │   │   ├── config/
│   │   │   │   │   ├── OpenApiConfig.java       # Swagger
│   │   │   │   │   └── WebConfig.java           # Configurações Web
│   │   │   │   └── security/
│   │   │   │       ├── TokenService.java        # Geração JWT
│   │   │   │       ├── SecurityConfig.java      # Config Spring Security
│   │   │   │       ├── SecurityConstants.java   # Constantes
│   │   │   │       ├── RateLimitInterceptor.java # Rate limiting
│   │   │   │       ├── SecurityFilter.java      # Filtro JWT
│   │   │   │       └── CustomUserDetailsService.java
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java          # JPA Repository
│   │   │   ├── vo/
│   │   │   │   ├── request/
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   └── RefreshTokenRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── TokenResponse.java
│   │   │   │       ├── UserResponse.java
│   │   │   │       └── ErrorResponse.java
│   │   │   └── LoginAuthApiApplication.java     # Main
│   │   └── resources/
│   │       └── application.properties           # Configurações
│   └── test/
│       └── java/dev/nunes/login_auth_api/
│           ├── infra/security/
│           │   └── TokenServiceTest.java
│           ├── controller/
│           │   └── AuthControllerTest.java
│           └── vo/response/
│               └── TokenResponseTest.java
├── pom.xml                                      # Dependências Maven
├── README.md                                    # Este arquivo
├── IMPLEMENTACOES.md                            # Detalhes técnicos
├── GUIA_USO.md                                  # Guia de uso
└── mvnw/mvnw.cmd                               # Maven wrapper
```

---

## 🔐 Autenticação

### Fluxo de Autenticação

```
┌─────────────────────────────────────────────────────┐
│ 1. REGISTRO                                          │
│ POST /api/v1/auth/register                          │
│ Body: { name, email, password }                     │
│                                                      │
│ ↓                                                    │
│ Validações:                                         │
│ - Email formato válido                              │
│ - Senha ≥ 8 caracteres                              │
│ - Email único                                       │
│                                                      │
│ ↓                                                    │
│ Sucesso (201):                                      │
│ { access_token, refresh_token, user }               │
└─────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────┐
│ 2. LOGIN                                             │
│ POST /api/v1/auth/login                             │
│ Body: { email, password }                           │
│                                                      │
│ ↓                                                    │
│ Verificações:                                       │
│ - Usuário existe?                                   │
│ - Senha está correta?                               │
│                                                      │
│ ↓                                                    │
│ Sucesso (200):                                      │
│ { access_token, refresh_token, user }               │
└─────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────┐
│ 3. REFRESH TOKEN                                    │
│ POST /api/v1/auth/refresh                           │
│ Body: { refreshToken }                              │
│                                                      │
│ ↓                                                    │
│ Validações:                                         │
│ - Token é válido?                                   │
│ - Token não expirou?                                │
│                                                      │
│ ↓                                                    │
│ Sucesso (200):                                      │
│ { access_token, refresh_token, user }               │
└─────────────────────────────────────────────────────┘
```

### Estrutura JWT Token

```
Header:
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload (Access Token):
{
  "iss": "login-auth-api",        // Issuer
  "sub": "usuario@example.com",   // Subject (email)
  "exp": 1719184800,              // Expiração
  "iat": 1719184200               // Issued at
}

Payload (Refresh Token):
{
  "iss": "login-auth-api",
  "sub": "usuario@example.com",
  "exp": 1719789000,
  "type": "refresh"
}

Signature:
HMACSHA256(base64(header) + "." + base64(payload) + secret)
```

### Duração dos Tokens

| Token | Duração | Renovável | Uso |
|-------|---------|-----------|-----|
| Access Token | 10 minutos | Sim (via refresh) | Requisições autenticadas |
| Refresh Token | 7 dias | Sim (novo gerado) | Renovação de access token |

### Usar Bearer Token

```bash
curl -X GET http://localhost:8080/api/v1/users/profile \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

---

## 🚨 Rate Limiting

### Configuração

| Parâmetro | Valor | Descrição |
|---|---|---|
| **Limite** | 10 requisições | Por janela de tempo |
| **Período** | 1 minuto | Janela deslizante |
| **Aplicado em** | `/login`, `/register` | Endpoints de autenticação |
| **Chave** | IP do cliente | X-FORWARDED-FOR ou remoteAddr |

### Resposta ao Limite Excedido

```
HTTP/1.1 429 Too Many Requests
Content-Type: application/json

Too many requests. Please try again later.
```

### Verificar Limite Restante

```bash
# Fazer múltiplas requisições
for i in {1..15}; do
  curl -X POST http://localhost:8080/api/v1/auth/login \
    -H "Content-Type: application/json" \
    -d '{"email":"test@example.com","password":"password123"}'
done
```

A 11ª requisição retornará 429.

---

## 📊 Logging

### Níveis de Log

| Nível | Descrição | Exemplos |
|---|---|---|
| **DEBUG** | Informações detalhadas | Tokens gerados, validações |
| **INFO** | Eventos importantes | Login bem-sucedido, registro |
| **WARN** | Avisos | Login falhado, token inválido |
| **ERROR** | Erros | Exceções, falhas BD |

### Configurar Níveis

Em `application.properties`:

```properties
# Todos os logs em INFO
logging.level.root=INFO

# Login Auth API em DEBUG
logging.level.dev.nunes.login_auth_api=DEBUG

# Spring Security em DEBUG
logging.level.org.springframework.security=DEBUG

# Hibernate em DEBUG
logging.level.org.hibernate=DEBUG
```

### Exemplo de Logs

```
2026-06-24 00:15:30 - Login attempt for user: joao@example.com
2026-06-24 00:15:30 - Generating access token for user: joao@example.com
2026-06-24 00:15:30 - Generating refresh token for user: joao@example.com
2026-06-24 00:15:30 - Login successful for user: joao@example.com
```

---

## 🧪 Testes

### Executar Todos os Testes

```bash
mvn test
```

### Executar Teste Específico

```bash
mvn test -Dtest=TokenServiceTest
mvn test -Dtest=AuthControllerTest
mvn test -Dtest=TokenResponseTest
```

### Cobertura de Testes

```bash
mvn test jacoco:report
```

Resultado em: `target/site/jacoco/index.html`

### Testes Implementados

#### TokenServiceTest
- ✅ Geração de access token
- ✅ Geração de refresh token
- ✅ Validação de access token
- ✅ Validação de refresh token
- ✅ Rejeição de token inválido

#### AuthControllerTest
- ✅ Login bem-sucedido
- ✅ Login com email inválido
- ✅ Registro bem-sucedido
- ✅ Registro com email duplicado
- ✅ Validação de campos obrigatórios

#### TokenResponseTest
- ✅ Construção de TokenResponse
- ✅ Construção de UserResponse

---

## 🐛 Troubleshooting

### Erro: "Port 8080 already in use"

**Causa:** Outra aplicação está usando a porta 8080

**Solução:**
```bash
# Linux/macOS - Encontrar processo
lsof -i :8080
kill -9 <PID>

# Windows - Encontrar processo
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Ou usar porta diferente
export SERVER_PORT=8081
mvn spring-boot:run
```

### Erro: "User already exists"

**Causa:** Email já registrado no banco

**Solução:**
```bash
# Tente com outro email
{
  "email": "novo_email@example.com",
  "password": "SenhaV@lida123"
}

# Ou limpe o banco (H2 - teste)
# Reinicie a aplicação
```

### Erro: "Invalid email or password"

**Causa:** Email ou senha incorreta

**Solução:**
- Verifique o email registrado
- Verifique a senha (case-sensitive)
- Confirme que o usuário foi registrado

### Erro: "Too many requests"

**Causa:** Exceeded rate limit (10 req/min)

**Solução:**
- Aguarde 1 minuto
- Resgate de IP diferente
- Aumentar limite em `RateLimitInterceptor.java`

### Erro: "Database is locked"

**Causa:** H2 database lock (testes)

**Solução:**
```bash
# Reiniciar a aplicação
mvn clean spring-boot:run

# Ou usar PostgreSQL
```

### Erro: "Invalid token"

**Causa:** Token expirado ou mal formatado

**Solução:**
```bash
# Faça login novamente para obter novo token
# Ou use refresh token se válido

POST /api/v1/auth/login
Body: { email, password }
```

### Erro: "Method not allowed"

**Causa:** HTTP method incorreto (GET em vez de POST)

**Solução:**
```bash
# ❌ ERRADO
curl -X GET http://localhost:8080/api/v1/auth/login

# ✅ CORRETO
curl -X POST http://localhost:8080/api/v1/auth/login
```

---

## 📈 Performance

### Otimizações Implementadas

✅ **Índices no BD:** Email com índice único
✅ **Conexão Pool:** HikariCP
✅ **Cache:** Spring Cache (pode ser habilitado)
✅ **Lazy Loading:** JPA lazy associations
✅ **Rate Limiting:** Protege contra DDoS

### Métricas

Em produção, considere adicionar:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

Acessar em: `http://localhost:8080/actuator/metrics`

---

## 🤝 Contribuindo

### Como Contribuir

1. **Fork** o projeto
2. **Clone** seu fork
3. **Crie uma branch** para sua feature (`git checkout -b feature/nova-funcionalidade`)
4. **Commit** suas mudanças (`git commit -m 'Add nueva funcionalidad'`)
5. **Push** para a branch (`git push origin feature/nova-funcionalidade`)
6. **Abra um Pull Request**

### Padrões de Código

- ✅ Siga o estilo Google Java
- ✅ Adicione testes para új código
- ✅ Documente com Javadoc
- ✅ Use nomes descritivos
- ✅ Mantenha linha máx. 120 caracteres

### Commit Message

```
[TIPO] Descrição breve

Descrição mais detalhada se necessário

TIPO: feat, fix, docs, style, refactor, test, chore
```

---

## 📄 Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

## 📞 Suporte

### Contato

- **Email:** support@login-auth-api.com
- **Issues:** [GitHub Issues](https://github.com/seu-usuario/login-auth-api/issues)
- **Documentação:** [Wiki](https://github.com/seu-usuario/login-auth-api/wiki)

### Recursos Úteis

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Guide](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/)
- [OpenAPI Specification](https://spec.openapis.org/)

---

## 🎓 Aprendizados

Este projeto demonstra:

✅ Spring Boot best practices
✅ JWT authentication and authorization
✅ Exception handling patterns
✅ RESTful API design
✅ Rate limiting implementação
✅ Automated testing
✅ API documentation com Swagger
✅ Security hardening
✅ Database design and optimization
✅ Logging estruturado

---

## 📅 Histórico de Versões

### v1.0.0 (2026-06-24)
- ✅ Implementação inicial
- ✅ Autenticação com JWT
- ✅ Refresh Token
- ✅ Rate Limiting
- ✅ Swagger UI
- ✅ Testes Unitários
- ✅ Tratamento de Erros Centralizado

---

## 🙏 Agradecimentos

Agradecimentos especiais a:

- [Spring Boot Team](https://spring.io/)
- [Auth0](https://auth0.com/) por JWT
- [OpenAPI Initiative](https://www.openapis.org/)
- Comunidade open-source Java

---

## ⭐ Status do Projeto

```
Desenvolvido por: Dev Nunes
Versão: 1.0.0
Status: ✅ Production Ready
Última atualização: 2026-06-24
Manutenção: Ativa
```

---

**Feito com ❤️ usando Spring Boot | Last Updated: 2026-06-24**

<!-- Footer -->
<div align="center">

Made with ☕ Java and 🚀 Spring Boot

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen?style=for-the-badge)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)

</div>

