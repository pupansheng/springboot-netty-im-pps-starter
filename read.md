# 封装netty 一个webSocket即时聊天的Im服务器springboot 启动器 
### 只需要几步就可自己创建一个im聊天服务器
* 引入依赖（需要本地安装 jar包在[github]()）

               <dependency>
                   <groupId>com.pps.im</groupId>
                   <artifactId>springboot-netty-im-pps-starter</artifactId>
                   <version>1.0-SNAPSHOT</version>
               </dependency>
               
*  配置文件
   ````
   pps:
     netty:
       im:
         enable : true
         port: 9090
         websocketPath: /pps
    ````
*  自定义两个类（必须）
     * webscoket连接权限自定义实现 并加入容器 这个类能决定客户端传入的连接请求能否被允许 客户端可以在连接url后面添加参数 在方法的map里会封装这些参数  
     例如：
    ````
   @Component
   public class WebConnetionAuth extends WebSocketConnetion {
   
       public WebConnetionAuth() {
           super("/pps");
       }
   
       public WebConnetionAuth(String wsUrl) {
           super(wsUrl);
       }
   
       @Override
       public Boolean webSocketCanConnetion(Map<String, Object> requestParams, ChannelHandlerContext ctx) {
   
   
          return  true;
   
       }
   
       @Override
       public void customOpreate(ChannelHandlerContext ctx) {
   
       }
   }
   ````
   * 自定义监听事件 例如：
   ````
   @Component
   public class CustomListener implements SocketAutoListenr {
   
   
   
   
       @Override
       public void loginSuccessful(SocketEnvent socketEnvent) {
   
           System.out.println("登录");
   
       }
   
       @Override
       public void loginFail(SocketEnvent socketEnvent) {
   
           System.out.println("登陆失败");
   
       }
   
       @Override
       public void loginOut(SocketEnvent socketEnvent) {
   
   
           System.out.println("用户退出：");
   
   
       }
   
       @Override
       public void acceptText(ChannelHandlerContext content, ChatFromProtocl chatFromProtocl, Date happenTime) {
   
           System.out.println("服务器收到信息：");
           System.out.println("消息类型:"+chatFromProtocl.getMsgType());
           System.out.println("消息内容："+chatFromProtocl.getData());
           int action = chatFromProtocl.getAction();
           MsgAction actionByType = MsgAction.getActionByType(action);
           System.out.print("发送者希望动作："+ actionByType);
           switch (actionByType){
               case sendToSingle:
                   String to = chatFromProtocl.getTo();
                   if(true){
   
   
   
                   }else {
                       boolean existsUser=false;
                       //todo 如果用户存在 只是不在线 可以离线保存 上线时再发送
                      if(existsUser){
   
   
                       }else {//todo 如果用户不存在
   
   
   
                       }
   
   
                   }
   
                   break;
               case sendToAll:
   
                   break;
               case other:
   
                   break;
               default:
                   SendMsgUtil.sendMsgToClientForErrorInfo(content,"不支持该动作！");
           }
       }
   
       @Override
       public void acceptBin(ChannelHandlerContext content, CharSequence fileInfo, ByteBuf fileData, Date happenTime) {
           System.out.println("服务器收到二进制消息：文件信息："+fileInfo);
           Map map = JSON.parseObject(String.valueOf(fileInfo), Map.class);
   
   
       }
   
       @Override
       public void other(SocketEnvent socketEnvent) {
   
   
       }
   ````
*    可选的http服务  继承HttpService 并加入容器（LoginImService也是继承了httpService）  例如
       ````
     @Component
     public class MyLoginServiseImpl extends LoginImService {
         public MyLoginServiseImpl() {
             super("/login");
         }
         public MyLoginServiseImpl(String url) {
             super(url);
         }
         @Override
         public Object validUser(Map<String, Object> requestParams, ChannelHandlerContext ctx) {
             String username = (String) requestParams.get("username");
             String password = (String) requestParams.get("password");
             if("123".equals(password)){
               return "ppppppp";
             }
             return null;
         }
     }
     ````
     
      
    