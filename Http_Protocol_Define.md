# HTTP 通信协议定义

APP 与服务器通信包括登录验证、获取数据、记录数据等，使用 HTTP 协议通信，除了正常通信外，还涉及安全验证、加密通信等，当前采用 OkHttp 框架

在 APP 框架内，封装 OkHttp 为单独静态类，可在 APP 启动或需要获取数据时调用，在获取数据后，将数据优先保存在本地数据库 `SQLite` ，APP 从本地数据库中获取数据更新界面

主要的通信请求分为两类：`Post` 和 `Get` ，是 `Http` 协议中向后台服务器发送数据的一种机制，区别为 `Post` 请求是将要提及到到后台服务器的数据放在 `Http` 包的包体中，`Get` 请求是将数据直接放在 `URL` 之后，但 `URL` 的长度是有一定限制的，另外，也容易被用户获取，所以 `Post` 请求方式更安全，也适合于参数比较大的环境

结合本 APP 获取数据较为庞杂，以及安全性考虑，暂定所有请求均使用 `Post` 

当前服务器未定，暂使用临时服务器配置 `URL` = http://xxx.xxx.xxx.xxx:xxxx (server_url)

## 通信协议说明

- 登录

  ```markdown
  请求方式：Post
  参数：username, password
  URL: server_url/login?
  返回值：
  	用户名不存在 {status:400, msg:"No User", data:null}
  	密码错误 {status:400, msg:"Wrong Password", data:null}
  	登录成功 {status:200, msg:"Success", data:null}
  ```

  ​                                          

