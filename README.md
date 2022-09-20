# redis-sse
a reactive web server that surport SSE by redis's pub/sub.
## 基于redis的pub/sub实现sse
### subscribe接口
请求后发起一个sse请求,持续接收服务端发出的数据
### publish接口
请求后向redis发布一条msg,所有在线的subscriber都会收到这个msg
