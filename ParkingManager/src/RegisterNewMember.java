/**
 * 회원을 가입하는 정보를 받는 클래스
 * 최종수정일 : 2018/05/29
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
 * 메인메뉴에서 회원가입버튼을 누르면 실행되는 actionListener
 */
class RegisterNewMember extends DBStart implements ActionListener {

	ParkingStartMenu frame; // 맨 처음 생성된 Frame인 ParkingStartView의 frame을 받기 위함
	
	JDialog registerDialog; // 큰 틀의 다이얼로그
	JPanel registerPanel = new JPanel(null); // 회원가입폼도 좌표로 지정하자!

	JLabel regIdLabel = new JLabel("아이디 : ");
	JTextField regIdText = new JTextField("", 20); // 아이디는 20자리까지만 입력가능
	JLabel regPwdLabel = new JLabel("비밀번호 : ");
	JPasswordField regPwdText = new JPasswordField("", 20); // 비밀번호는 20자리까지 입력가능
	JLabel regNameLabel = new JLabel("이름 : ");
	JTextField regNameText = new JTextField(""); // 이름 입력
	JLabel regPhoneLabel = new JLabel("핸드폰 : ");
	JTextField regPhoneText = new JTextField(""); // 핸드폰 번호 입력
	JLabel regCarNumLabel = new JLabel("차량번호 : ");
	JTextField regCarNumText = new JTextField("", 4); // 차량번호 입력 뒤에 네자리만 입력
	
	JButton regDupleBtn = new JButton("중복체크"); // 아이디 중복 체크 버튼
	JButton regOkBtn = new JButton("가입완료"); // 가입완료 버튼
	JButton regCancelBtn = new JButton("가입취소"); // 가입취소 버튼
	/**
	 * 디버깅 문제 떄문에 기본 생성자를 만든다
	 */
	public RegisterNewMember() { 

	}
	/**
	 * 회원가입창 생성자 
	 */
	public RegisterNewMember(ParkingStartMenu frame) {
		this.frame = frame;

		regDupleBtn.addActionListener(new regDupleAction()); // '중복확인'버튼 클릭 시
		regOkBtn.addActionListener(new regOkAction()); // '가입완료'버튼 클릭 시
		regCancelBtn.addActionListener(new regCancelAction()); // '가입취소'버튼 클릭 시
	}

	/**
	 * 다이얼로그의 모든 텍스트 필드를 초기화하는 곳
	 */
	public void actionPerformed(ActionEvent e) {

		// 다이얼로그 호출 전 각 텍스트필드 초기화
		regIdText.setText("");
		regPwdText.setText("");
		regNameText.setText("");
		regPhoneText.setText("");
		regCarNumText.setText("");
		RegisterDialog(); // 회원가입 버튼이 클릭되면 회원가입 다이어로그를 띄운다.
	}

	/**
	 * 회원가입 다이얼로그
	 */
	public void RegisterDialog() {
		registerDialog = new JDialog(frame, "회원가입 창");
		registerDialog.setBounds(500, 100, 300, 340); // (x위치, y위치, width크기, height크기)
		regPwdText.setEchoChar('＊');
		registerPanel.setBackground(Color.WHITE);

		//아이디 부분 패널에 붙히기
		regIdLabel.setFont(new Font("굴림", Font.BOLD, 15)); 
		regIdLabel.setBounds(17, 50, 100, 30);
		regIdText.setBounds(75, 50, 100, 30);
		registerPanel.add(regIdLabel); // 아이디 입력창
		registerPanel.add(regIdText);
		registerPanel.add(regDupleBtn);
		regDupleBtn.setBounds(180, 50, 90, 30); // 중복체크 버튼
		regIdText.requestFocus(); // 아이디 입력창에 포커스를 맞춘다.

		//비밀번호 부분 패널에 붙히기
		regPwdLabel.setFont(new Font("굴림", Font.BOLD, 15));
		registerPanel.add(regPwdLabel); // 비밀번호 입력창
		registerPanel.add(regPwdText);
		regPwdLabel.setBounds(2, 85, 130, 30);
		regPwdText.setBounds(75, 85, 130, 30);

		//이름 부분 패널에 붙히기
		regNameLabel.setFont(new Font("굴림", Font.BOLD, 15));
		registerPanel.add(regNameLabel); // 이름 입력창
		registerPanel.add(regNameText);
		regNameLabel.setBounds(30, 120, 100, 30);
		regNameText.setBounds(75, 120, 100, 30);

		//전화번호 부분 패널에 붙히기
		regPhoneLabel.setFont(new Font("굴림", Font.BOLD, 15));
		registerPanel.add(regPhoneLabel); // 휴대폰 번호 입력창
		registerPanel.add(regPhoneText);
		regPhoneLabel.setBounds(15, 155, 130, 30);
		regPhoneText.setBounds(75, 155, 130, 30);

		//차량번호 부분 패널에 붙히기
		regCarNumLabel.setFont(new Font("굴림", Font.BOLD, 15));
		registerPanel.add(regCarNumLabel); // 차량번호 입력창
		registerPanel.add(regCarNumText);
		regCarNumLabel.setBounds(1, 190, 130, 30);
		regCarNumText.setBounds(75, 190, 160, 30);

		//가입완료 버튼과 취소버튼 패널에 붙히기
		registerPanel.add(regOkBtn); // '가입완료'버튼
		registerPanel.add(regCancelBtn); // '가입취소'버튼
		regOkBtn.setBounds(50, 230, 90, 30);
		regCancelBtn.setBounds(155, 230, 90, 30);

		registerDialog.add(registerPanel);// 다이얼로그에 패널 부탁
		registerDialog.setVisible(true);
	} // RegisterDialog() 끝

	/**
	 *  취소버튼 눌렀을 때 실행되는 리스너
	 */
	class regCancelAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			registerDialog.setVisible(false);
		}
	}
	/**
	 * 중복확인 버튼 누르면 확인되는 리스너
	 */
	class regDupleAction extends DBStart implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String a = regIdText.getText(); //아이디 필드의 텍스트를 변수 a 에 저장
			boolean flag = false; 
			/**
			 * 데이터베이스의 있는 아이디와 비교하여 중복확인을 한다
			 */
			
			try{
				
				rs1 = stmt.executeQuery("select * from memberimfo");
				rs1.first();
				do {
					if(a.equals(rs1.getString("id"))) {
						flag = true;
						break;
					}
				}while(rs1.next());
				
				if(flag ==true) {
					JOptionPane.showMessageDialog(null, "중복된 아이디가 있습니다! 다른 아이디를 입력해주세요","경고창",JOptionPane.WARNING_MESSAGE); //중복아이디가 있는 경우 
					
				}
				else 
					JOptionPane.showMessageDialog(null, "사용가능한 아이디입니다");
			} catch(SQLException v) {
				v.printStackTrace();
			}
		}
	}
	/**
	 * ok 버튼을 누르면 데이터베이스에 값을 저장한다
	 */
	class regOkAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			 String sql = "INSERT memberimfo (id, password, name, phonenumber, carnumber,member,flag)" + "VALUES (?, ?, ?, ?, ?, ?,?)";
			 try (PreparedStatement statement = conn.prepareStatement(sql)) {
				            String str = new String(regPwdText.getPassword());
				            
				            statement.setString(1, regIdText.getText());
				            statement.setString(2, str);
				            statement.setString(3, regNameText.getText());
				            statement.setString(4, regPhoneText.getText());
				            statement.setString(5, regCarNumText.getText());
				            statement.setString(6, "T");
				            statement.setString(7, "T");
				            statement.executeUpdate();
				        
			 			} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
			registerDialog.setVisible(false); // 저장 완료됬으면 창을 끈다.
		}

	 }
}
