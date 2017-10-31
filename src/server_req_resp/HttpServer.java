package server_req_resp;

/**
 * @author: 焦
 * @date:   createDate：2017年10月20日 下午10:29:09   
 * @Description: 
 * 
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
public class HttpServer {
     public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
     // shutdown command
     private static final String SHUTDOWN_COMMAND = "\\SHUTDOWN";
     // the shutdown command received
     private boolean shutdown = false;
     public static void main(String[] args)
     {
           HttpServer server = new HttpServer();
           System.out.println("WEB_ROOT="+WEB_ROOT);
           server.await();
            
     }
     public void await() {
    	   ServerSocket serverSocket = null;
           int port = 8080;
           try {
                serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
           } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
           }
           while (!shutdown) {
                Socket socket = null;
                InputStream input = null;
                OutputStream output = null;
                try {
                     //接收了客户端发来的请求，否则一致是等待状态
                     socket = serverSocket.accept();
                     input = socket.getInputStream();
                     output = socket.getOutputStream();
                     // create Request object and parse
                     Request request = new Request(input);
                     request.parse(); //从请求中读取内容
                     System.out.println("shutdown.req="+request.getUri());
                     if(request.getUri()==null || request.getUri().equals("\\")){
                    	 continue;
                     }else if(request.getUri().equals(SHUTDOWN_COMMAND)){                   	 
                    	 shutdown = true;
                    	 break;
                     }
                     // create Response object
                     Response response = new Response(output);
                     response.setRequest(request);
                     response.sendStaticResource();
                     // Close the socket
                     socket.close();
                     //check if the previous URI is a shutdown command
                     
                    
                }catch (Exception e){
                     e.printStackTrace ();
                     continue;
                }
           }
    }
}
