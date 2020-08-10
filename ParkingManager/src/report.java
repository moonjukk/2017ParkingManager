/**
 * 
 * 신고하기 버튼 시 관리자와 통신하기 위한 클래스
 * 최종수정일 : 2018/05/29
 * 
 * @author 문주환
 * @see 소스코드
 * 
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class report extends JFrame {

	JButton jb1 = new JButton("전송");
	JButton jb2 = new JButton("취소");
	JPanel jp = new JPanel(); // 기본 패널 생성
	private JLabel jl1 = new JLabel("신고내용을 자세히 작성해주세요"); 
	private JTextArea jta = new JTextArea(8, 30); // 신고를 작성할 textArea
	BufferedWriter out = null; // 버퍼라이터 값 초기화
	Socket socket = null; // 소켓 값 초기화

	/** 생성자 메소드 
	 */
	public report() {
		setTitle("신고/건의 하기");// 제목표시줄
		setSize(350, 250);// 사이즈
		setLocation(350, 250);// 창이 뜨는 위치
		setLayout(new FlowLayout());
		setBackground(Color.GREEN);

		try {
			socket = new Socket("172.30.2.229", 9999); // 관리자 아이피 입력하여 통신하기
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // 내가 입력한 값을 out에 저장
			System.out.println("서버와 연결완료(신고하기)");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		pan1();// 메소드 호출

		setVisible(true);// 보이로독 함
		setResizable(false);// 크기변경 불가
	}
	
	/** 기본패널 생성 메소드
	 */
	public void pan1() {
		add(jl1); // "신고내용 작성해주세요" J라벨
		add(jta); // 신고내용을 입력 할 extArea
		
		// 전송 버튼 클릭시 리스너
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					out.write(jta.getText() + "\n"); // 입력한 값을 out에 저장
					out.flush(); // out에 저장한 입력값을 관리자에게 전송
					JOptionPane.showMessageDialog(null, "건의사항 송신완료! 소중한 의견 감사합니다", "송신 완료", 1);
					setVisible(false);
					dispose();// 창닫음
				} catch (IOException ex) {
					System.err.println(ex.getMessage()); // 에러메세지 출력
					JOptionPane.showMessageDialog(null, "송신 실패 아이피 및 소켓을 확인해주세요", "송신 실패", 0);
				}

			}
		});
		
		//취소버튼 클릭 시 리스너
		jb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();// 창닫음
			}
		});

		add(jb1);
		add(jb2);
	}

}