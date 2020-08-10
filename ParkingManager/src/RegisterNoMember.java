
/**
 * 프로젝트를 시작하는 클래스
 * 최종수정일 : 2018/06/01
 * @author 이제구
 * 
 * @see 소스코드
 * 
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
/**
 * 비회원 가입 창 클래스
 */
public class RegisterNoMember extends DBStart implements ActionListener {
	JDialog NoregisterDialog; // 큰 틀의 다이얼로그
	ParkingStartMenu frame; // 맨 처음 생성된 Frame인 ParkingStartView의 frame을 받기 위함
	JPanel registerPanel = new JPanel(null); // 회원가입폼도 좌표로 지정하자!

	JLabel regCarNumLabel = new JLabel("차량번호 : ");
	JTextField regCarNumText = new JTextField(""); // 차량번호입력

	JLabel regPassWordLabel = new JLabel("비밀번호 : ");
	JPasswordField regPassWordText = new JPasswordField(""); // 비밀번호 입력

	JButton regOkBtn = new JButton("가입완료"); // 가입완료 버튼
	JButton regCancelBtn = new JButton("가입취소"); // 가입취소 버튼

	/**
	 * 기본생성자
	 */
	public RegisterNoMember() {

	}
	/**
	 * 비회원 가입 창 
	 */
	public RegisterNoMember(ParkingStartMenu frame) {
		this.frame = frame; // ParkingStartMenu의 frame을 가져온다

		regOkBtn.addActionListener(new regOkAction()); // '가입완료'버튼 클릭 시
		regCancelBtn.addActionListener(new regCancelAction()); // '가입취소'버튼 클릭 시
	}

	/**
	 * 초기화 시켜주는 메소드
	 */
	public void actionPerformed(ActionEvent e) {
		regPassWordText.setEchoChar('*');
		regCarNumText.setText(""); // 초기화
		regPassWordText.setText(""); // 초기화
		NoRegisterDialog(); // 회원가입 버튼이 클릭되면 회원가입 다이어로그를 띄운다.
	}
	/**
	 * 비 회원 가입 다이얼로그
	 */
	public void NoRegisterDialog() {
		NoregisterDialog = new JDialog(frame, "비회원가입 창");

		NoregisterDialog.setBounds(1000, 300, 300, 250); // (x위치, y위치, width크기, height크기)

		registerPanel.setBackground(Color.WHITE); // 배경화면 색을 흰색으로 바꾼다.

		// 차량번호 부분 패널에 붙히기
		regCarNumLabel.setFont(new Font("굴림", Font.BOLD, 15)); 
		registerPanel.add(regCarNumLabel); 
		registerPanel.add(regCarNumText);
		regCarNumLabel.setBounds(10, 30, 150, 30);
		regCarNumText.setBounds(83, 30, 150, 30);

		// 비밀번호 부분 패널에 붙히기
		regPassWordLabel.setFont(new Font("굴림", Font.BOLD, 15));
		registerPanel.add(regPassWordLabel); // 비밀번호 번호 입력창
		registerPanel.add(regPassWordText);
		regPassWordLabel.setBounds(10, 70, 150, 30);
		regPassWordText.setBounds(83, 70, 150, 30);

		// 가입완료 버튼과 취소버튼 패널에 붙히기
		registerPanel.add(regOkBtn); // '가입완료'버튼
		registerPanel.add(regCancelBtn); // '가입취소'버튼
		regOkBtn.setBounds(50, 140, 90, 30);
		regCancelBtn.setBounds(155, 140, 90, 30);

		NoregisterDialog.add(registerPanel);// 다이얼로그에 패널 부탁
		NoregisterDialog.setVisible(true); // 다이얼로그 visible
	}
	/**
	 * 취소버튼 actionListener
	 */
	class regCancelAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			NoregisterDialog.setVisible(false);
		}
	}
	/**
	 * ok버튼 actionListener 데베에 회원 정보를 저장한다.
	 */
	class regOkAction extends DBStart implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			ParkingStartMenu.memberFlag = false; // 맴버 flag를 처음 false로 초기화
			String a = regCarNumText.getText(); // 가입창에서 차량번호를 가져온다
			String b = new String(regPassWordText.getPassword()); // 패스워드
			boolean flag = false; // flag를 false로 초기화
			mainWindows.MyID = a; // mainWindows의 MyID 변수에 아이디 받아온 값을 저장한다 이유는 데이터베이스에서 다음 클래스에서 비교하려고
			
			String sql = "INSERT nomemberimfo (carnum, password, member)" + "VALUES (?, ?, ?)";
			try (PreparedStatement statement = conn.prepareStatement(sql)) {

				statement.setString(1, regCarNumText.getText());
				statement.setString(2, b);
				statement.setString(3, "F");
				statement.executeUpdate();

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			NoregisterDialog.setVisible(false); // 저장을 하면 다이얼로그를 끈다.

			new mainWindows(); // 비회원으로 저장하면 바로 메인메뉴로 간다
			frame.dispose(); // 프레임을 끈다.
		}

	}

}
