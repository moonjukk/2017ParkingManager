/**
 * 사용자로부터 신고 내용 받아와 출력하는 클래스
 * 최종수정일: 2018/06/18
 * @author 송원섭
 * 
 * @see 소스코드
 * 
 */
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.awt.*;


public class chatServer {

	private BufferedReader in = null;

	private ServerSocket listener = null;
	private Socket socket = null;
	int pos;

	/**
	 * 채팅 생성자
	 */
	public chatServer() {
		try {

			listener = new ServerSocket(9999); // 서버 소켓 생성

			System.out.println("연결을 기다리고 있습니다....."); // 이거 관리자에 추가해야함
			socket = listener.accept(); // 클라이언트로부터 연결 요청 대기

			mainWindow.condition.append("연결되었습니다." + "\n"); //관리자 TextArea에 값을 보낸다
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			mainWindow.condition.append("<< 클라이언트 수신대기 중 >>\n");
			
			String inMessage = null;

			try {
				inMessage = in.readLine();

			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
			if (inMessage == null)
				System.exit(1);
			mainWindow.condition.append("유저: " + inMessage + "\n");//사용자로부터 받아온 문자를 상황판에 출력
			pos = mainWindow.condition.getText().length();
			mainWindow.condition.setCaretPosition(pos);

		
		}

		catch (IOException e) {
			System.out.println(e.getMessage());
		}

		finally {
			try {
				socket.close(); // 통신용 소켓 닫기
				listener.close(); // 서버 소켓 닫기
			} catch (IOException e) {
				System.out.println("클라이언트와 채팅 중 오류가 발생했습니다.");
			}
		}
	}
}