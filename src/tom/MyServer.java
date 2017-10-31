package tom;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: 焦
 * @date:   createDate：2017年10月17日 下午9:26:23   
 * @Description: 
 * 
 */
public class MyServer implements Runnable {
	ServerSocket server=null;
	Socket clientSocket;
	boolean flag=true;
	Thread connetThread;//向客户端发送信息的线程
	BufferedReader sin;
	DataOutputStream sout;
	public static void main(String[] args) {
		MyServer ms=new MyServer();
		ms.serverStart();
	}
	
	public void serverStart(){
		try{
			server=new ServerSocket(8080);
			System.out.println("服务器端口号："+server.getLocalPort());
			while(flag){
				clientSocket=server.accept();
				System.out.println("服务器端发出连接已建立完成！");
				InputStream is=clientSocket.getInputStream();
				sin=new BufferedReader(new InputStreamReader(is));
				OutputStream os=clientSocket.getOutputStream();
				sout=new DataOutputStream(os);
				connetThread=new Thread(this);
				connetThread.start();//启动线程，向客户端发送信息
				String aLine;
				while((aLine=sin.readLine())!=null){//从客户端读入信息
					System.out.println(aLine);
					if(aLine.equals("bye")){
						flag=false;
						connetThread.interrupt();
						break;
					}
				}
				sout.close();
				os.close();
				sin.close();
				is.close();
				clientSocket.close();
				System.exit(0);//程序运行结束
			}
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void run() {
		while(true){
			try{
				int ch;
				while((ch=System.in.read())!=-1){
					sout.write((byte)ch);
					if(ch=='\n'){
						sout.flush();
					}
				}
			}catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	public void finalize(){
		try{
			server.close();
		}catch (IOException e) {
			System.out.println(e);
		}
	}
}
