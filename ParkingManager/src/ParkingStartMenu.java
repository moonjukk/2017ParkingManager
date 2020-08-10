/**
 * 처음 아이디를 입력하는 메인화면
 * 최종수정일 : 2018/06/07
 * @author 이제구
 * 
 * @see 소스코드
 * 
 *
 */

import java.util.Calendar;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

/**
 * 로그인창 메인화면 구현 
 */
public class ParkingStartMenu extends JFrame {
	static boolean memberFlag = true; 
	Calendar cal = Calendar.getInstance(); //정기회원 결제를 위해 오늘이 며칠인지 확인을 위한 calendar
	public static boolean startFlag = true;
	MyPanel mp; // 처음 contentPane을 이걸로 설정한다.
	// 로그인패널 제목
	JLabel title = new JLabel();
	ImageIcon titleImage = new ImageIcon("Image/Title.png");
	Image titleImageObject;

	// 로그인, 회원가입 패널
	JPanel loginPanel = new JPanel(null); // 패널의 레이아웃을 null로 표시
	JLabel idLabel = new JLabel("아이디 : ");
	JTextField idText = new JTextField("");
	JLabel passwordLabel = new JLabel("패스워드 : ");
	JPasswordField passwordText = new JPasswordField("");

	// 로그인 버튼과 회원가입 버튼
	JButton loginBtn = new JButton("로그인");
	JButton newRegBtn = new JButton("회원가입");
	JButton notMemBtn = new JButton("비회원");

	// 로그인 창 왼쪽에 넣을 로고이미지
	JLabel logoImgLabel = new JLabel();
	ImageIcon logoImgIcon; // 이미지를 담을 ImageIcon
	Image changeSize; // 원본이미지의 크기를 바꿀 Image클래스 객체

	/**
	 * 로그인 창 생성자
	 */
	public ParkingStartMenu() {

		setTitle("주차장관리시스템");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 0, 1200, 700); // 화면 크기 지정
		setResizable(false); // 화면 크기 조정 불가능
		mp = new MyPanel();
		passwordText.setEchoChar('＊'); //비밀번호 표시를 *
		setContentPane(mp); // 컨탠트팬을 MyPanel로 변경
		mp.setLayout(null); // 컨탠트팬의 레이아웃을 null로 설정

		// 각 버튼의 핸드러를 달아준다.
		newRegBtn.addActionListener(new RegisterNewMember(this));
		loginBtn.addActionListener(new Memberlogin());
		notMemBtn.addActionListener(new RegisterNoMember(this));

		titleLabel(); // 타이틀 로고를 가져온다
		loginPaneladd(); // 로그인패널 가져온다.

		setVisible(true);
	}

	/**
	 * title을 다는 메소드
	 */
	public void titleLabel() {
		titleImageObject = titleImage.getImage();
		titleImageObject = titleImageObject.getScaledInstance(600, 500, java.awt.Image.SCALE_SMOOTH);
		titleImage = new ImageIcon(titleImageObject);
		title.setIcon(titleImage);
		title.setBounds(250, 180, 800, 280); // 이미지의 위치와 크기를 정한다.
		mp.add(title); // 이미지를 패널에 붙힌다
	}

	/**
	 * 로그인 구현된 패널을 붙히는 메소드
	 */
	public void loginPaneladd() {
		loginPanel.setBorder(new LineBorder(Color.BLACK, 3)); // 로그인 화면을 담을 패널 지정
		loginPanel.setBounds(396, 395, 420, 200);
		loginPanel.setBackground(Color.WHITE);
		mp.add(loginPanel);

		idLabel.setFont(new Font("굴림", Font.BOLD, 15)); // 아이디 입력창 구현
		idLabel.setBounds(174, 22, 66, 60);
		loginPanel.add(idLabel);
		idText.setBounds(236, 38, 172, 35);
		loginPanel.add(idText);

		passwordLabel.setFont(new Font("굴림", Font.BOLD, 13));// 패스워드 입력창 구현
		passwordLabel.setBounds(160, 70, 80, 60);
		loginPanel.add(passwordLabel);
		passwordText.setBounds(236, 84, 172, 35);
		loginPanel.add(passwordText);

		loginBtn.setBounds(160, 131, 75, 35); // 로그인 버튼
		loginBtn.setMargin(new Insets(0, 5, 0, 0));
		loginBtn.setBackground(new Color(0xEEEEEE));
		loginBtn.setFont(new Font("굴림", Font.BOLD, 15));

		newRegBtn.setBounds(242, 131, 75, 35); // 회원가입 버튼
		newRegBtn.setMargin(new Insets(0, 5, 0, 0));
		newRegBtn.setBackground(new Color(0xEEEEEE));
		newRegBtn.setFont(new Font("굴림", Font.BOLD, 15));

		notMemBtn.setBounds(328, 131, 75, 35); // 비회원 버튼
		notMemBtn.setMargin(new Insets(0, 5, 0, 0));
		notMemBtn.setBackground(new Color(0xEEEEEE));
		notMemBtn.setFont(new Font("굴림", Font.BOLD, 15));

		loginPanel.add(loginBtn);// 각 버튼 패널에 추가
		loginPanel.add(newRegBtn);
		loginPanel.add(notMemBtn);

		// 로그인 창 왼쪽 로고이미지
		logoImgIcon = new ImageIcon("Image/park.png"); // ImageIcon에 이미지를 담는다.
		changeSize = logoImgIcon.getImage(); // 이미지의 크기를 변환하기 위해 Image객체 변수에 이미지 담기
		changeSize = changeSize.getScaledInstance(140, 100, java.awt.Image.SCALE_SMOOTH); // 원본 이미지의 크기 조정
		logoImgIcon = new ImageIcon(changeSize); // 크기를 바꾼 이미지를 다시 ImageIcon에 넣는다.
		logoImgLabel.setIcon(logoImgIcon); // Label에 ImageIcon을 담는다.
		logoImgLabel.setBounds(10, 30, 140, 100); // 이미지가 담긴 Label의 크기와 위치 지정
		loginPanel.add(logoImgLabel); // 이미지 Label 패널에 추가
	}

	/**
	 * 배경화면 패널
	 */
	class MyPanel extends JPanel {
		ImageIcon ic = new ImageIcon("Image/낙엽.gif"); // 배경화면 이미지 업로드
		Image img = ic.getImage(); // 배경화면 이미지 객체

		public void paintComponent(Graphics g) { // 배경화면 그리기
			super.paintComponent(g);
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
		}
	}

	/**
	 * 아이디 입력 후 로그인 버튼을 누르면 발생하는 것
	 */
	class Memberlogin extends DBStart implements ActionListener { // 회원가입버튼
		public void actionPerformed(ActionEvent e) {
			memberFlag = true;
			String a = idText.getText(); // 아이디 텍스트를 읽기
			String b = new String(passwordText.getPassword()); // 비밀번호 텍스트 읽기
			String c = "admin"; // 관리자 계정 아이디
			int flag = 2;
			int date = cal.get(Calendar.DATE);
			String flag2 = "F";
			String sql = "update memberimfo set flag=?";
			String pay = "";

			if (date == 21) { // 매달 1일마다 결제할 수 있도록
				
				try {
					rs1 = stmt.executeQuery("select * from memberimfo"); // 테이블 가져온다
					rs1.first(); // 테이블을 첫행으로 커서를 가져온다
					do {
						if (a.equals(rs1.getString("id"))) {
							if(a.equals("admin"))
								break;
							if (flag2.equals(rs1.getString("flag"))) { // 1일에 결제를 두 번하면 안되니까 그 조건문
								mainWindows.MyID = rs1.getString("id");
								new memberPayAleart(); // 결제 알림창 ! 

							}
							

						}

					} while (rs1.next()); // 데이터베이스를 다움행으로 커서를 옮긴다.

				} catch (SQLException v) {
					v.printStackTrace(); // 오류 출력
				}
			} else if (date == 20) { // 
				try {
					rs1 = stmt.executeQuery("select * from memberimfo"); // 테이블 가져온다
					rs1.first(); // 테이블을 첫행으로 커서를 가져온다

					do {

						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, "F");
						pstmt.executeUpdate();

					} while (rs1.next());

				} catch (SQLException v) {
					v.printStackTrace(); // 오류 출력
				}
			}

			if (a.equals(c)) { // 관리자이면 flag 0 지정
				flag = 0;

			}

			else {
				try {
					rs1 = stmt.executeQuery("select * from memberimfo"); // 테이블 가져온다
					rs1.first(); // 테이블을 첫행으로 커서를 가져온다
					do {
						if (a.equals(rs1.getString("id"))) {
							if (b.equals(rs1.getString("password"))) {
								pay = rs1.getString("flag");
								flag = 1; // 아이디와 비밀번호가 데이터베이스에 있다면 flag 1 지정
								mainWindows.MyID = rs1.getString("id");
							}
							break;

						}

					} while (rs1.next()); // 데이터베이스를 다움행으로 커서를 옮긴다.

				} catch (SQLException v) {
					v.printStackTrace(); // 오류 출력
				}
			}

			if (flag == 0) {
				new mainWindow();// 관리화면
				dispose();
			} else if (flag == 1) {
				if(!flag2.equals(pay)) {
				    new mainWindows();// 유저 화면으로 이동
					dispose();
				}
			} else
				JOptionPane.showMessageDialog(null, "회원정보가 일치하지 않습니다!!"); // 정보가 일치하지 않는다는 것을 알려준닽
		}
	}

}
