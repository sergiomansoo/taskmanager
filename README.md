# Task Manager API

API REST para gerenciamento de tarefas, desenvolvida com Java e Spring Boot.

Este projeto foi criado para praticar a construção de uma API completa, passando por autenticação, autorização, persistência em banco de dados, documentação interativa e deploy. A ideia foi sair de um CRUD simples e montar uma aplicação com regras de acesso, separação de responsabilidades e um fluxo mais próximo do que eu encontraria em um projeto real.

A aplicação permite cadastrar usuários, realizar login com JWT e gerenciar tarefas. Usuários comuns conseguem acessar apenas suas próprias tarefas, enquanto usuários administradores possuem permissões maiores dentro do sistema.

## Funcionalidades

- Cadastro de usuários
- Login com autenticação JWT
- Criptografia de senha
- Controle de acesso com Spring Security
- Perfis de acesso `USER` e `ADMIN`
- Criação, listagem, atualização e exclusão de tarefas
- Conclusão de tarefas
- Filtro de tarefas por status e prioridade
- Consulta de usuários
- Tratamento centralizado de exceções
- Documentação da API com Swagger
- Deploy com Docker no Render
- Banco PostgreSQL em produção

## Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- PostgreSQL
- H2 Database
- Maven
- Lombok
- Swagger / OpenAPI
- Docker
- Render

## Arquitetura

O projeto foi organizado em camadas para manter cada parte da aplicação com uma responsabilidade clara:

```text
src/main/java/com/sergio/taskmanager
+-- auth
+-- config
+-- exception
+-- security
+-- tarefa
+-- usuario
```

- `auth`: cadastro, login e geração de token
- `security`: configuração de segurança, filtro JWT e autenticação
- `usuario`: regras, endpoints e persistência de usuários
- `tarefa`: regras, endpoints e persistência de tarefas
- `exception`: tratamento de erros da aplicação
- `config`: configurações auxiliares, como Swagger

## Banco de Dados

Durante o desenvolvimento local, o projeto começou usando H2 para facilitar os testes iniciais. Depois, a aplicação foi adaptada para PostgreSQL, que é o banco utilizado no ambiente hospedado.

As configurações sensíveis do banco são carregadas por variáveis de ambiente:

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

O Hibernate é usado junto com o Spring Data JPA para mapear as entidades Java para tabelas no banco de dados e facilitar operações como salvar, buscar, atualizar e excluir registros.

## Modelo Relacional

O modelo abaixo representa a estrutura principal do banco de dados, com a relação entre usuários e tarefas:

![Modelo relacional do Task Manager](docs/database-model.png)

Cada usuário pode possuir várias tarefas, enquanto cada tarefa pertence a um único usuário. No código, o usuário também possui um campo `role`, usado para diferenciar os perfis `USER` e `ADMIN`.

## Deploy

A API está hospedada no Render e a documentação pode ser acessada pelo Swagger:

[https://taskmanager-xlm1.onrender.com/swagger-ui/index.html](https://taskmanager-xlm1.onrender.com/swagger-ui/index.html)

Como o projeto está em um plano gratuito, a primeira requisição pode demorar alguns segundos caso o serviço esteja em repouso.

## Acesso Para Teste

Já existem dois usuários cadastrados no banco para facilitar a avaliação do projeto:

```text
ADMIN
Email: admin@taskmanager.com
Senha: admin123
```

```text
USER
Email: user@taskmanager.com
Senha: user123
```

Para testar rotas protegidas pelo Swagger:

1. Acesse o endpoint `/auth/login`
2. Faça login com uma das contas acima
3. Copie o token retornado
4. Clique em **Authorize**
5. Informe o token no formato:

```text
Bearer seu_token_aqui
```

## Endpoints Principais

### Autenticação

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/auth/register` | Cadastra um novo usuário |
| POST | `/auth/login` | Realiza login e retorna um token JWT |

### Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/usuario/me` | Retorna os dados do usuário autenticado |
| GET | `/usuario` | Lista todos os usuários |
| GET | `/usuario/{id}` | Busca um usuário por ID |
| POST | `/usuario` | Cria um usuário |
| PUT | `/usuario` | Atualiza o usuário autenticado |
| PUT | `/usuario/{id}` | Atualiza um usuário por ID |
| DELETE | `/usuario/{id}` | Remove um usuário |

### Tarefas

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/tarefa` | Cria uma tarefa para o usuário autenticado |
| POST | `/tarefa/{id}` | Cria uma tarefa para um usuário específico |
| GET | `/tarefa` | Lista tarefas |
| GET | `/tarefa/{id}` | Busca uma tarefa por ID |
| GET | `/tarefa/filtrar` | Filtra tarefas por status e prioridade |
| GET | `/tarefa/usuario/{usuarioId}` | Lista tarefas de um usuário específico |
| PUT | `/tarefa/{id}` | Atualiza uma tarefa |
| PATCH | `/tarefa/{id}/concluir` | Marca uma tarefa como concluída |
| DELETE | `/tarefa/{id}` | Remove uma tarefa |

## Exemplos de Requisição

### Login

```json
{
  "email": "user@taskmanager.com",
  "senha": "user123"
}
```

### Criar Tarefa

```json
{
  "titulo": "Revisar documentação da API",
  "descricao": "Conferir exemplos de uso e endpoints no Swagger",
  "status": "PENDENTE",
  "prioridade": "ALTA",
  "dataEntrega": "2026-06-01"
}
```

### Criar Usuário

```json
{
  "nome": "Usuario Teste",
  "email": "teste@taskmanager.com",
  "senha": "teste123",
  "role": "USER"
}
```

## Status e Prioridades

Status disponíveis:

```text
PENDENTE
EM_ANDAMENTO
CONCLUIDA
```

Prioridades disponíveis:

```text
BAIXA
MEDIA
ALTA
```

## Como Rodar Localmente

Clone o repositório:

```bash
git clone <url-do-repositorio>
```

Entre na pasta do projeto:

```bash
cd taskmanager
```

Execute com Maven Wrapper:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

A aplicação será iniciada em:

```text
http://localhost:8080
```

## Docker

O projeto possui um `Dockerfile`, usado no deploy da aplicação no Render. Com isso, o ambiente de execução fica mais previsível, já que a aplicação roda dentro de um container com Java 21.

## O Que Aprendi

Com esse projeto, consegui praticar melhor a construção de uma API REST com Spring Boot, principalmente a parte de autenticação com JWT e controle de permissões com Spring Security.

Também trabalhei com persistência usando JPA/Hibernate, organização em camadas, uso de DTOs, tratamento de exceções e documentação com Swagger.

Além da parte de código, também passei pelo processo de preparar a aplicação para produção: configurar variáveis de ambiente, usar PostgreSQL no deploy e criar um Dockerfile para rodar o projeto no Render.
