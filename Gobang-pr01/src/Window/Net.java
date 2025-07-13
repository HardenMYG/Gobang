package Window;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static Window.OnlinePanel.*;

public class Net {
	private ServerSocket ss ;
	private Socket s;
	private BufferedReader in;
	private PrintStream out;
	public void beginListen(int port){
		new Thread(){
			public void run(){
				try {
					ss = new ServerSocket(port);
					s = ss.accept();
					in = new BufferedReader(new InputStreamReader(s.getInputStream()));
					out = new PrintStream(s.getOutputStream(), true);
					startReadThread();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
		}.start();
	}
	public void endListen() {
		try {
			// 先关闭Socket连接，这会同时关闭其关联的输入输出流
			if (s!= null &&!s.isClosed()) {
				s.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// 再关闭ServerSocket，释放监听的端口资源
			if (ss!= null &&!ss.isClosed()) {
				ss.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ss = null;
		s = null;
		in = null;
		out = null;
	}
	public void connect(String ip,int port){
		try {
			s = new Socket(ip,port);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintStream(s.getOutputStream(),true);
			startReadThread();
		} catch (Exception e) {
		}
	}
	public void endConnect() {
		try {
			// 关闭Socket连接，同时关闭关联的输入输出流
			if (s!= null &&!s.isClosed()) {
				s.close();
			}
			// 将相关对象引用赋值为null，释放引用（非必须但良好实践）
			s = null;
			in = null;
			out = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	protected void startReadThread() {
		new Thread(){
			public void run(){
				String line;
				try {
					while(s!=null&&in!=null&&(line=in.readLine())!=null&&!s.isClosed()){
						parseMessage(line);
					}
					if (in!= null) {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}
	protected void parseMessage(String line) {
		if(line.startsWith("chess:")){
			parseChess(line.substring(6));
		}else if(line.startsWith("WIN")){
			onlinebattlePanel.reset();
			JOptionPane.showMessageDialog(null, "WIN!");
			onlinebattlePanel.repaint();
		}else if(line.startsWith("NEED")){
			onlinebattlePanel.reset();
			JOptionPane.showMessageDialog(null, "END CONNECT!");
			endConnect();
			onlinebattlePanel.repaint();
			Connect=false;
			isConnected=false;
		}else if(line.startsWith("chat:")){
			textArea.append(line.substring(5)+System.lineSeparator());
		}else if(line.startsWith("CONNECT")){
			isConnected = true;
		}else if(line.startsWith("No")){
			isConnected = false;
			onlinebattlePanel.reset();
			JOptionPane.showMessageDialog(null, "END CONNECT!");
			onlinebattlePanel.repaint();
			endListen();
			Listen=false;
		}
	}
	private void parseChess(String msg) {
		String[] a = msg.split(",");
		int row = Integer.parseInt(a[0]);
		int col = Integer.parseInt(a[1]);
		onlinebattlePanel.otherPutChess(row,col);
	}

	public Net(){}
	public void sendChess(int row, int col) {
		if(out!=null)
			out.println("chess:" + row + "," + col);
	}
	public void sendWIN() {
		if(out!=null)
			out.println("WIN!");
	}
	public void sendNEEDTOEXIT() {
		if(out!=null)
			out.println("NEED TO EXIT!");
	}
	public void sendMessage(String text) {
		if(out!=null)
			out.println("chat:"+"He:"+text);
	}
	public void sendConnect() {
		if(out!=null)
			out.println("CONNECT");
	}
	public void sendNoConnection() {
		if(out!=null)
			out.println("No connection");
	}
}
