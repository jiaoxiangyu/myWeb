package server_req_resp;

/**
 * @author: 焦
 * @date:   createDate：2017年10月20日 下午10:29:48   
 * @Description: 
 * 
 */
import java.io.IOException;
import java.io.InputStream;
public class Request {
     private InputStream input;
     private String uri;
     public Request(InputStream input)
     {
           this.input = input;
     }
      
     public void parse() {
           // Read a set of characters from the socket
           StringBuffer request = new StringBuffer(2048);
           int i;
           byte[] buffer = new byte[2048];
           try {
                i = input.read(buffer); //将从输入流取2048长度的内容存到buffer字节数组中，如果内容不到2048，数组其他空间剩余空着
           } catch (IOException e) {
                e.printStackTrace();
                i = -1;
           }
            
           for (int j=0; j<i; j++)
           {
                request.append((char) buffer[j]);
           }
           System.out.println("************Http-request*****************");
           System.out.print(request.toString());
           uri = parseUri(request.toString().replace('/', '\\'));
          
     }
      
     private String parseUri(String requestString) {
           int index1, index2;
           index1 = requestString.indexOf(' ');
           /*
            * http请求行的结构:方法 统一资源标识符（URI） 协议/版本(它们之间以空格分隔)
            * 例如：POST //examples/default.jsp HTTP/1.1
            */
           if (index1 != -1) {// index1 == -1表示没找到
                     index2 = requestString.indexOf(' ', index1 + 1);//从index+1位置开始寻找‘ ’
                     if (index2 > index1)
                     return requestString.substring(index1 + 1, index2);
           }
           return null;
     }
      
     public String getUri()
     {
           return uri;
     }
}
