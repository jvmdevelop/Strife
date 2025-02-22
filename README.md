# Strife
# Strife
# Дока

## WebRtcHandler

### handleTextMessage
- **Тип запроса:** WebSocket
- **Сообщения:**
  - `"PING"` → Сервер отвечает `"PONG"` с текущим временем.
  - SDP offer → Сервер отвечает SDP answer.

### afterConnectionClosed
- **Тип запроса:** WebSocket
- **Описание:** Закрывает WebSocket-соединение.

### connectPeers
- **Тип запроса:** HTTP GET
- **URL:** `/webrtc/connectPeers/{channel}`
- **Параметры:**
  - `channel` (string) - Имя канала.

### getChannelInfo
- **Тип запроса:** HTTP GET
- **URL:** `/webrtc/getChannelInfo/{channel}`
- **Параметры:**
  - `channel` (string) - Имя канала.

### sendMessage
- **Тип запроса:** WebSocket
- **Сообщение:** Отправляет текстовое сообщение в WebSocket-сессию.

---

## AuthController

### register (Регистрация)
- **Тип запроса:** HTTP POST
- **URL:** `/api/v1/auth/register`
- **Тело запроса:**
  ```json
  {
    "username": "string",
    "password": "string",
    "email": "string",
    "description": "string",
    "role": "string",
    "avatarUrl": "string"
  }

- **Описание:** Регистрирует нового пользователя и возвращает JWT-токен.

### login (Авторизация)
- **Тип запроса:** HTTP POST
- **URL:** `/api/v1/auth/login`
- **Тело запроса:**
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Описание:** Аутентифицирует пользователя и возвращает JWT-токен.

---

## ChatController

### createChat (Создание чата)
- **Тип запроса:** HTTP POST
- **URL:** `/api/v1/chat/createChat`
- **Заголовки:**
  - `Authorization: Bearer <token>`
- **Тело запроса:**
  ```json
  {
    "title": "string",
    "userIds": ["long"],
    "isTetATet": "boolean",
    "recipientId": "long"
  }
  ```
- **Описание:** Создаёт новый чат с указанными пользователями.

### getCurrentChat (Получение текущего чата)
- **Тип запроса:** HTTP POST
- **URL:** `/api/v1/chat/getCurrentChat`
- **Заголовки:**
  - `Authorization: Bearer <token>`
- **Тело запроса:**
  ```json
  {
    "userId": "long"
  }
  ```
- **Описание:** Получает текущий чат с указанным пользователем.

### addUserToChat (Добавление пользователя в чат)
- **Тип запроса:** HTTP POST
- **URL:** `/api/v1/chat/addUserToChat`
- **Тело запроса:**
  ```json
  {
    "chatId": "long",
    "userId": "long"
  }
  ```
- **Описание:** Добавляет пользователя в указанный чат.

### sendMessage (Отправка сообщения)
- **Тип запроса:** HTTP POST
- **URL:** `/api/v1/chat/sendMessage`
- **Заголовки:**
  - `Authorization: Bearer <token>`
- **Тело запроса:**
  ```json
  {
    "chatId": "long",
    "content": "string"
  }
  ```
- **Описание:** Отправляет сообщение в указанный чат.

### getChatMessages (Получение сообщений чата)
- **Тип запроса:** HTTP POST
- **URL:** `/api/v1/chat/getChatMessages`
- **Тело запроса:**
  ```json
  {
    "chatId": "long",
    "offset": "int"
  }
  ```
- **Описание:** Получает сообщения из указанного чата.

### editMessage (Редактирование сообщения)
- **Тип запроса:** HTTP POST
- **URL:** `/api/v1/chat/editMessage`
- **Заголовки:**
  - `Authorization: Bearer <token>`
- **Тело запроса:**
  ```json
  {
    "messageId": "long",
    "content": "string"
  }
  ```
- **Описание:** Редактирует указанное сообщение.

### deleteMessage (Удаление сообщения)
- **Тип запроса:** HTTP POST
- **URL:** `/api/v1/chat/deleteMessage`
- **Заголовки:**
  - `Authorization: Bearer <token>`
- **Тело запроса:**
  ```json
  {
    "messageId": "long"
  }
  ```
- **Описание:** Удаляет указанное сообщение.

---

## UserController

### getCurrentSession (Получение текущей сессии)
- **Тип запроса:** HTTP GET
- **URL:** `/api/v1/me`
- **Описание:** Получает имя пользователя текущей сессии.

### getUserByLogin (Получение пользователя по логину)
- **Тип запроса:** HTTP GET
- **URL:** `/api/v1/getUserByLogin/{username}`
- **Описание:** Получает информацию о пользователе по логину.

### getUserById (Получение пользователя по ID)
- **Тип запроса:** HTTP GET
- **URL:** `/api/v1/getUserById/{id}`
- **Описание:** Получает информацию о пользователе по ID.

### getUserInfo (Получение информации о пользователе)
- **Тип запроса:** HTTP GET
- **URL:** `/api/v1/user`
- **Параметры:**
  - `username` (string) - Логин пользователя.
- **Описание:** Получает информацию о пользователе по логину.

### cname (Изменение имени пользователя)
- **Тип запроса:** HTTP POST
- **URL:** `/api/v1/cname`
- **Заголовки:**
  - `Authorization: Bearer <token>`
- **Параметры:**
  - `username` (string) - Новое имя пользователя.
- **Описание:** Изменяет имя текущего пользователя.

### cdesc (Изменение описания)
- **Тип запроса:** HTTP POST
- **URL:** `/api/v1/description`
- **Заголовки:**
  - `Authorization: Bearer <token>`
- **Параметры:**
  - `description` (string) - Новое описание.
- **Описание:** Обновляет описание текущего пользователя.

### cavatar (Изменение аватара)
- **Тип запроса:** HTTP POST
- **URL:** `/api/v1/avatar`
- **Заголовки:**
  - `Authorization: Bearer <token>`
- **Параметры:**
  - `avatarUrl` (string) - Новый URL аватара.
- **Описание:** Обновляет аватар текущего пользователя.
