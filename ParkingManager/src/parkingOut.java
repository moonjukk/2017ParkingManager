/**
 * 
 * 주차버튼 클릭 시 주차가 되어있는 공간일 경우 실행되는 클래스, 출차관리
 * 최종수정일 : 2018/05/26
 * 
 * @author 문주환
 * @see 소스코드
 * 
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.*;

public class parkingOut extends JFrame implements ActionListener {
	
	dataBase a = new dataBase();
	String cometimes;
	JButton jb1 = new JButton("확인");
	JButton jb2 = new JButton("취소");
	private int num;
	static int carNum;
	static String passwd;
	private JTextField jt1 = new JTextField(6);
	JPasswordField jt2 = new JPasswordField("",6); // 비밀번호는 4자리까지 입력가능
	private JLabel jl1 = new JLabel("차량번호");
	private JLabel jl2 = new JLabel("비밀번호");
	static boolean parkflag = false;
	String stringFee = "0";

	/** 비밀번호 입력하면 *로 출력되게 하는 Echo 설정
	 */
	public parkingOut() {
		jt2.setEchoChar('＊');
	}
	
	/** 메인 메소드
	 */
	public parkingOut(int num) {
		
		setTitle("출차하기");// 제목표시줄
		setSize(350, 250);// 사이즈
		setLocation(600, 250);// 창이 뜨는 위치
		setLayout(new FlowLayout());
	
		this.num = num;

		pan1();// 메소드 호출
		setVisible(true);// 보이로독 함
		setResizable(false);// 크기변경 불가
	}
	
	/** 출차의 메인 패널
	 */
	public void pan1() {
		setLayout(new GridLayout(4, 1));
		
		JPanel WelcomPan = new JPanel();
		JPanel a = new JPanel();
		JPanel b = new JPanel();
		JPanel c = new JPanel();

		JLabel WelcomLabel1 = new JLabel(num + "");
		WelcomLabel1.setForeground(Color.RED);
		WelcomLabel1.setFont(new Font("고딕", Font.BOLD, 25));
		JLabel WelcomLabel2 = new JLabel("번 주차공간 입니다");
		WelcomLabel2.setFont(new Font("고딕", Font.BOLD, 20));
		//라벨 폰트 등 설정

		WelcomPan.add(WelcomLabel1);
		WelcomPan.add(WelcomLabel2);

		a.add(jl1);
		a.add(jt1);

		b.add(jl2);
		b.add(jt2);

		jb1.addActionListener(this);
		jb2.addActionListener(this);
		c.add(jb1);
		c.add(jb2);

		add(WelcomPan);
		add(a);
		add(b);
		add(c);
	}

	@Override
	/** 액션 리스너
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == jb1) {
			parkflag = true;

			carNum = Integer.parseInt(jt1.getText()); // 입력한 차량 번호 저장
			passwd = new String(jt2.getPassword()); // 입력한 패스워드 값 저장 getPassword이유는 ****때문

			boolean checkException = false; // 예외조건들을 체크하기 위한 boolean 변수

			checkException = checkTextFieldString();

			if (checkException == false) {
				jt1.setText("");
				jt1.requestFocus();
				return; // 텍스트 필드에 입력한 문자열이 문자이거나 4자리 숫자가 아니므로 다시 입력받게 한다.
			}
			
			// 만일 차량번호가 일치하다면
			if (mainWindows.outcarNum[carNum] == num) {
				a.checkPasswd();
				//만일 일치하지 않는 다면
			} else {
				JOptionPane.showMessageDialog(null, "당신의 차가 주차된곳이 아닌 것 같은데요?", "차량번호 입력오류", 0);
				parkflag = false;
				setVisible(false);
				dispose();// 창닫음
			}

			parkingIn.checkFlag = false; // 중복주차시도 플래그 초기화
			parkflag = false;
			setVisible(false);
			dispose();// 창닫음

		}

		else if (e.getSource() == jb2) {
			parkflag = false;
			setVisible(false);
			dispose();// 창닫음
		}

	}

	/** 텍스트필드에서 가져온 차량번호가 문자열이거나 4자리 숫자가 아니면 다시 입력받게 한다.
	 */
	public boolean checkTextFieldString() {
		try { // 번호 입력창에 숫자가 아닌 문자 입력 시 오류창을 띄우기 위한 try-catch문
			int check = Integer.parseInt(jt1.getText());
		} catch (Exception ae) {
			JOptionPane.showMessageDialog(null, "문자는 입력하시면 안돼요", "숫자를 입력하세요.", 0);
			return false; // 입력받은게 숫자가 아닌 문자열이라서 예외 발생 시 false 리턴
		}

		if (jt1.getText().length() != 4) // 차량번호 입력은 4자리만 가능하게 설정
		{
			JOptionPane.showMessageDialog(null, "차량번호는 4자리겠지?", "차량번호 입력", 0);
			return false; // 4자리 숫자가 아닐 시 false 리턴
		}
		return true; // 텍스트필드에서 가져온 값이 4자리 숫자가 정확하면 true리턴
	}

	/** 데이터베이스 값을 얻어오고 값을 수정하기 위한 클래스
	 */
	class dataBase extends DBStart {
		/** 패스워드가 일치한지 확인하는 메소드
		 */
		public void checkPasswd() {

			try {
				rs1 = stmt.executeQuery("select * from parktable"); // 테이블 가져온다
				rs1.first(); // 테이블을 첫행으로 커서를 가져온다

				do {
					// 만약 주차된 차량번호와 입력한 값이 같다면
					if (Integer.toString(num).equals(rs1.getString("parknum"))) {
						// 또한 패스워드도 일치한다면
						if (passwd.equals(rs1.getString("password"))) {
							cometimes = rs1.getString("cometime");
							mainWindows.Btn[num].setBackground(Color.PINK);
							mainWindows.parkflag[num] = true;
							mainWindows.Btn[num].setText(String.valueOf(num));
							outDB outDB = new outDB();
							outDB.setAdminDB();
							setVisible(false);
							dispose();// 창닫음
							break;
							// 차량번호 혹은 패스워드 하나라도 잘못 입력한다면
						} else {
							JOptionPane.showMessageDialog(null, "패스워드 잘못 입력하신 것 같은데요?", "패스워드 입력오류", 0);
							parkflag = false;
							setVisible(false);
							dispose();// 창닫음
							break;
						}
					}
				} while (rs1.next()); // 데이터베이스를 다움행으로 커서를 옮긴다.
			} catch (SQLException v) {
				v.printStackTrace(); // 오류 출력
			}
		}
	}
	
	/** 출차 시 데이터베이스의 정보를 삭제하기 위한 클래스
	 */
	class outDB extends DBStart {
		/** 비회원인 경우 데이터베이스 삭제
		 */
		public void deleteNomemberimfo() {
			String carnum = jt1.getText(); // 차량번호를 읽어온다
			String sql = "delete from nomemberimfo where carnum ='" + carnum + "'"; // 사용할 sql문(데이터 삭제)
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.executeUpdate(); // 데이터 삭제
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		/** 정회원인경우 데이터 베이스 삭제
		 */
		public void setDB() {
			String id = mainWindows.MyID;
			String sql = "delete from parktable where id ='" + id + "'";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.executeUpdate();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		/** 출차하는 경우 admin테이블에도 정보를 입력한다.
		 */
		public void setAdminDB() {

			SimpleDateFormat outTime = new SimpleDateFormat("yyMMddHHmm");
			String setoutTime = outTime.format(parkingIn.date);
			cometimes = outTime.format(parkingIn.date); // 입차시간 조회용 변수
			try {
				rs1 = stmt.executeQuery("select * from parktable"); // 테이블 가져온다
				rs1.first(); // 테이블을 첫행으로 커서를 가져온다

				String parkNum = rs1.getString("parknum");
				String numss = Integer.toString(num);
				do {
					if (numss.equals(parkNum)) {
						cometimes = rs1.getString("cometime");
						System.out.println("입차시간" + cometimes);
						break;
					}
				} while (rs1.next()); // 데이터베이스를 다움행으로 커서를 옮긴다.

			} catch (SQLException v) {
				v.printStackTrace(); // 오류 출력
			}

			int fee = Integer.parseInt(setoutTime) - Integer.parseInt(cometimes);
			int feeAlpha = 0;
			
			// 비회인 경우 주차요금을 구하는 구간
			if (ParkingStartMenu.memberFlag == false) {
				
				deleteNomemberimfo();
				if (fee < 200) {
					feeAlpha = 3000;
				}

				else if (fee > 200) {
					if (fee >= 10000) {
						feeAlpha = fee / 10000;
						feeAlpha *= 12000;
						fee %= 10000;
						if (fee > 200) {
							fee -= 200;
							fee /= 30;
							fee *= 500;
							feeAlpha += fee;
						} else {
							feeAlpha += 3000;
						}
					} else if (fee < 10000) {
						if (fee > 200) {
							fee -= 200;
							fee /= 30;
							fee *= 500;
							feeAlpha = fee;
						} else {
							feeAlpha = 3000;
						}
					}
				}
				stringFee = Integer.toString(feeAlpha);
			}
			// 정회원이라면 0원
			else if (ParkingStartMenu.memberFlag == false) {
				stringFee = "0";
			}
			
			JOptionPane.showMessageDialog(null, "주차요금 : "+feeAlpha , "결제대금", 1); // 주차요금 창 출력해주기

			

			String sql = "INSERT admin (parknum, carnum, id, password, cometime, outtime, ismember, pay)"
					+ "VALUES (?, ? ,?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, Integer.toString(num));
				pstmt.setString(2, Integer.toString(carNum));
				pstmt.setString(3, mainWindows.MyID);
				pstmt.setString(4, mainWindows.MyPasswd);
				pstmt.setString(5, cometimes);
				pstmt.setString(6, setoutTime);
				pstmt.setString(7, mainWindows.isMember);
				pstmt.setString(8, stringFee);
				pstmt.executeUpdate();

				setDB();

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}