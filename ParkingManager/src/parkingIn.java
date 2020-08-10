/**
 * 
 * 주차버튼 클릭 시 주차가 되어있지 않은 빈 공간일 경우 실행되는 클래스
 * 최종수정일 : 2018/06/02
 * 
 * @author 문주환
 * @see 소스코드
 * 
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import java.sql.*;

/**메인 클래스
 */
@SuppressWarnings("serial")
public class parkingIn extends JFrame implements ActionListener {

	static Date date = new Date(); // 출입시간 및 요금계산을 위한 날짜
	
	static boolean checkFlag = false; // 중복 주차시도를 확인 하는 flag

	JButton jb1 = new JButton("확인");
	JButton jb2 = new JButton("취소");

	private int num; // 주차장 번호를 저장할 변수 초기화
	static int carNum; // 차량번호 가져올 변수 초기화
	int w = 111;
	private JTextField jt1 = new JTextField(6); // 차량번호를 입력할 필드
	private JLabel jl1 = new JLabel("차량번호");
	static boolean parkflag = false;

	/** parkingIn 메인메소드
	 */
	public parkingIn(int num) {
		setTitle("주차하기");// 제목표시줄
		setSize(350, 250);// 사이즈
		setLocation(600, 250);// 창이 뜨는 위치
		setLayout(new FlowLayout());

		this.num = num; // 버튼 클릭시 가져온 주차장 번호를 저장

		pan1();// 메소드 호출
		setVisible(true);// 보이로독 함
		setResizable(false);// 크기변경 불가
	}
	
	/** 메인 매널을 작성하는 메소드
	 */
	public void pan1() {
		setLayout(new GridLayout(4, 1)); // Grid레이아웃 사용

		JPanel WelcomPan = new JPanel();
		JPanel a = new JPanel();
		JPanel b = new JPanel();
		JPanel c = new JPanel();

		// "n번 주차공간 입니다" 출력하기
		JLabel WelcomLabel1 = new JLabel(num + "");
		WelcomLabel1.setForeground(Color.BLUE);
		WelcomLabel1.setFont(new Font("고딕", Font.BOLD, 25));
		JLabel WelcomLabel2 = new JLabel("번 주차공간 입니다");
		WelcomLabel2.setFont(new Font("고딕", Font.BOLD, 20));

		WelcomPan.add(WelcomLabel1);
		WelcomPan.add(WelcomLabel2);

		a.add(jl1); // 차량 번호 입력
		a.add(jt1);
		
		JLabel jl3 = new JLabel("※ 정확하게 입력해주세요");
		b.add(jl3);

		jb1.addActionListener(this);
		jb2.addActionListener(this);
		c.add(jb1);
		c.add(jb2);

		add(WelcomPan);
		add(a);
		add(b);
		add(c);
	}

	// 액션리스너
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jb1) {
			parkflag = true; // 주차되었기 때문에 parkflag true
			carNum = Integer.parseInt(jt1.getText()); // 작성한 carNum읽어오기
			boolean checkException = false; // 예외조건들을 체크하기 위한 boolean 변수

			checkException = checkTextFieldString();

			if (checkException == false) {
				jt1.setText("");
				jt1.requestFocus();
				return; // 텍스트 필드에 입력한 문자열이 문자이거나 4자리 숫자가 아니므로 다시 입력받게 한다.
			}

			dataBase dataBase = new dataBase();
			dataBase.duplParkCheck(); // 하나의 id가 두 대이상의 주차를 시도하는지 확인하는 메소드

			setVisible(false);
			dispose();// 창닫음
		} else if (e.getSource() == jb2) {
			parkflag = false;
			setVisible(false);
			dispose();// 창닫음
		}

	}

	// parktable DB에 출차정보 등록
	class dataBase extends DBStart {
		public void setDB() {

			SimpleDateFormat inTime = new SimpleDateFormat("yyMMddHHmm"); // 날짜 표식 형성하는 포맷 설정
			String setinTime = inTime.format(date); // 현재시간을 저장
			
			// 주차정보를 데이터베이스 parktable에 넣는다.
			String sql = "INSERT parktable (parknum, carnum, id, password, cometime, ismember, pay)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, Integer.toString(num));
				pstmt.setString(2, Integer.toString(carNum));
				pstmt.setString(3, mainWindows.MyID);
				pstmt.setString(4, mainWindows.MyPasswd);
				pstmt.setString(5, setinTime);
				pstmt.setString(6, mainWindows.isMember);
				pstmt.setString(7, null);
				pstmt.executeUpdate();

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// 하나의 ID로 두 대 이상의 차 주차를 시도하는지 확인하는 메소드
		public void duplParkCheck() {
			try {
				rs1 = stmt.executeQuery("select * from parktable"); // 테이블 가져온다
				rs1.first(); // 테이블을 첫행으로 커서를 가져온다
				
				
				String MyID = mainWindows.MyID;
				
				do {
					String tableID = rs1.getString("id");
					// 만약 MyID가 parktable에 주차된 차의 동일한 id가 존재한다면 주차를 막는 if문
					if (MyID.equals(tableID)) {
						JOptionPane.showMessageDialog(null, "ID하나로 차 1대만 주차 할 수있습니다.", "이미 주차한 ID입니다.", 0);
						checkFlag = true;
						break;
					}
				} while (rs1.next()); // 데이터베이스를 다움행으로 커서를 옮긴다.

				if (checkFlag == false) {
					mainWindows.Btn[num].setBackground(Color.YELLOW);
					mainWindows.parkflag[num] = false;
					mainWindows.Btn[num].setText(String.valueOf(carNum));
					mainWindows.outcarNum[carNum] = num;

					setDB();
				}

			} catch (SQLException v) {
				v.printStackTrace(); // 오류 출력
			}
		}
	}

	// 텍스트필드에서 가져온 차량번호가 문자열이거나 4자리 숫자가 아니면 다시 입력받게 한다.
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
}