# LocalNetSocketServiceAndClient;局域网内，Service App与Client App Socket通信
# Android Socket实现多个客户端连接同一个服务端（IM chat）

1.socket一个服务端多个客户端，多线程这么实现，2种方式，一种：你处理客户端的连接，开一个子线程（需要注意：控制线程个数），第二种：异步挂起处理?

2.socket服务器端需要给每个连接的客户端启动一个线程？怎么实现socket一对多，使用多线程？多线程实现服务器与多个客户端通信？

3.Socket发消息及消息回执，收到消息并分发给其他客户端消息

4.Socket断开与心跳？

5.自定义UDP协议？协议有哪些内容

6.一对多客户端的问题？ Socket客户端断开后，重新连接服务器端？

7.自定义Tcp/Udp Socket通信协议（IM）