package tom;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.xml.sax.InputSource;

/**
 * @author: 焦
 * @date:   createDate：2017年10月19日 下午3:48:19   
 * @Description: 
 * 
 */
public class MyClient implements Runnable {
	Socket clientSocket;
	boolean flag=true;
	Thread connenThread;//用于向服务器发送信息
	BufferedReader cin;
	DataOutputStream cout;
	
	public static void main(String[] args) {
		new MyClient().clientStart();
	}
	
	public void  clientStart(){
		try{
			clientSocket=new Socket("localhost", 8080);//连接服务器端，这里用本机
			System.out.println("客户端发出已建立连接");
			while(flag){
				InputStream is=clientSocket.getInputStream();
				cin=new BufferedReader(new InputStreamReader(is));
				OutputStream os=clientSocket.getOutputStream();
				cout=new DataOutputStream(os);
				connenThread=new Thread(this);
				connenThread.start();
				String aLine;
				while((aLine=cin.readLine())!=null){//从客户端读入信息
					System.out.println(aLine);
					if(aLine.equals("bye")){
						flag=false;
						connenThread.interrupt();
						break;
					}
				}
				cout.close();
				os.close();
				cin.close();
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
					cout.write((byte)ch);
					if(ch=='\n'){
						cout.flush();
					}
				}
			}catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
}
