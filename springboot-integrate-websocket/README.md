# Spring Boot WebSocket 聊天室模块

本项目是一个基于 Spring Boot 实现的 WebSocket 匿名聊天室模块。

## 功能特性

- **实时匿名聊天**: 用户只需输入一个用户名即可加入聊天室。
- **用户状态广播**: 当用户加入或离开聊天室时，系统会向所有在线用户广播通知。
- **公共聊天频道**: 所有消息都会被广播到 `/topic/public` 频道，所有连接的客户端都能接收到。
- **WebSocket & SockJS**: 使用 STOMP over WebSocket 进行通信，并启用 SockJS 作为备用方案，以兼容不支持 WebSocket 的浏览器。
- **前后端分离**: 后端由 Spring Boot 提供支持，前端是一个独立的静态页面 (`index.html`)，通过 STOMP.js 与后端进行交互。

## 技术栈

- **后端**: Spring Boot, Spring WebSocket, Maven
- **前端**: HTML, CSS, JavaScript, Bootstrap 5, SockJS, STOMP.js

## 如何运行

1.  **启动后端服务**:
    - 在 IDE 中直接运行 `WebSocketApplication.java`。
    - 或者在模块根目录 `springboot-integrate-websocket/` 下执行 Maven 命令:
      ```bash
      mvn spring-boot:run
      ```

2.  **访问前端页面**:
    - 服务启动后，默认监听 `8080` 端口。
    - 打开浏览器，访问 `http://localhost:8080`。

## 如何测试

1.  打开两个或多个浏览器窗口或标签页，并都访问 `http://localhost:8080`。
2.  在每个页面中输入不同的用户名并登录。
3.  在一个窗口中发送消息，消息会实时显示在所有其他窗口中。

## 主要代码文件说明

- `WebSocketApplication.java`: Spring Boot 主启动类。
- `config/WebSocketConfig.java`: WebSocket 和 STOMP 消息代理的配置。
- `controller/ChatController.java`: 处理聊天消息和用户加入事件的控制器。
- `event/WebSocketEventListener.java`: 监听 WebSocket 连接和断开事件，用于广播用户离开通知。
- `model/ChatMessage.java`: 聊天消息的数据模型。
- `resources/static/index.html`: 聊天室的前端 UI 界面。
