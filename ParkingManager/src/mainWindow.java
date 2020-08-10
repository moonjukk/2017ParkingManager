/**
 * 주차 자리, 신고내용 텍스트, 버튼 등 관리자 메인 화면 클래스
 * 최종수정일: 2018/06/16
 * @author 송원섭
 * 
 * @see 소스코드
 * 
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

@SuppressWarnings("serial")
class mainWindow extends JFrame implements Runnable, ActionListener {
	dataBase dataBase = new dataBase();//객체 생성
	public static JButton Btn[] = new JButton[150];// 자리 버튼
	static JTextArea condition;// 상황판 텍스트 에어리어
	int carNumber;// 차량번호
	JButton notUseBtn[] = new JButton[100];// 사용하지 않는 버튼(자리 사이사이의 버튼)

	JPanel pan2;// 판넬2
	JPanel pan3;// 판넬3
	JButton refresh=new JButton("새로고침");//새로고침 버튼
	JButton history=new JButton("내역보기");//내역보기 버튼
	JButton configBtn=new JButton("채팅설정");//채팅설정 버튼

	/**
	 * 생성자
	 *
	 */
	public mainWindow() {
		setTitle("주차장 관리 프로그램");// 제목표시줄
		setSize(1000, 700);// 사이즈
		setLocation(600, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 종료버튼 클릭시 종료함
		pan2();// 메소드 호출
		pan3();// 메소드 호출
		gridInit();// 메소드 호출
		
		setVisible(true);// 창을 보여줌
		setResizable(false);// 크기변경 불가
	}

	@SuppressWarnings("deprecation")
	/**
	 * 버튼 클릭시 발생하는 이벤트
	 *
	 */
	public void actionPerformed(ActionEvent e) {//액션 이벤트
		if (e.getSource() == configBtn) {// 채팅설정 버튼
			new chatServer();// 채팅설정 클래스 실행
		} else if (e.getSource() == history) {// 내역보기 버튼
			new logWindow();// 내역보기 클래스 실행
		}
		else if(e.getSource() == refresh) {//새로고침 버튼
			mainWindow cl = new mainWindow();// 메인윈도우 객체 생성
			Thread th = new Thread(cl);// 객체를 스레드로 생성
			th.start();// 스레드를 실행
			dispose();//창 닫음
		}
		else if (Integer.parseInt(e.getActionCommand()) >= 0// 버튼의 이름을 정수형태로
																// 변환하여
				&& Integer.parseInt(e.getActionCommand()) <= Btn.length) {// 0~150사이의 숫자일경우 이벤트 없음

		} else {// 0~150사이의 숫자가 아닐시에는
			for (int i = 1; i < Btn.length; i++) {// 버튼을 처음부터 끝까지 검색
				String temp = Btn[i].getLabel();// temp에 버튼의 라벨을 저장
				int temp2 = Integer.parseInt(temp);// temp1에 저장한 라벨을 정수형태로 변환하여
													// 저장해 둠
				if (temp2 == Integer.parseInt(e.getActionCommand())) {
					// temp2에 저장해둔 라벨과 액션이벤트로 들어온 매개변수가 같다면
					// 차가 주차되어 있는 자리가 바로 그 자리 이므로
					carNumber = temp2;// 차량번호를 저장
					
					break;// 루프를 나옴
				}
			}
			new unpark(carNumber);//강제 출차시키는 unpark 클래스 실행
			
		}
		
	}

	@SuppressWarnings("deprecation")
	/**
	 * 버튼을 생성하는 메소드
	 *
	 */
	public void gridInit() {//버튼 생성하는 메소드
		for (int i = 1; i <= 15; i++) {
			pan2.add(Btn[i] = new JButton(i + ""));//1~15까지 버튼 생성후 판넬2에 추가
			Btn[i].addActionListener(this);//버튼에 액션 리스너 
			Btn[i].setBackground(new Color(0xa79c8e));//버튼 색깔 설정
			dataBase.duplParkCheck(i);//duplParkCheck() 호출
			
		}
		for (int i = 1; i <= 15; i++) {
			pan2.add(notUseBtn[i] = new JButton(""));//1~15까지 버튼 생성후 판넬2에 추가
			notUseBtn[i].setEnabled(false);//버튼 비활성화
		}
		for (int i = 16; i <= 45; i++) {
			pan2.add(Btn[i] = new JButton(i + ""));//16~45까지 버튼 생성후 판넬2에 추가
			Btn[i].addActionListener(this);//버튼에 액션 리스너 
			Btn[i].setBackground(new Color(0xa79c8e));//버튼 색깔 설정
			dataBase.duplParkCheck(i);//duplParkCheck() 호출
		}
		for (int i = 16; i <= 30; i++) {
			pan2.add(notUseBtn[i] = new JButton(""));//16~45까지 버튼 생성후 판넬2에 추가
			notUseBtn[i].setEnabled(false);//버튼 비활성화
		}
		for (int i = 46; i <= 75; i++) {
			pan2.add(Btn[i] = new JButton(i + ""));//46~75까지 버튼 생성후 판넬2에 추가
			Btn[i].addActionListener(this);//버튼에 액션 리스너 
			Btn[i].setBackground(new Color(0xa79c8e));//버튼 색깔 설정
			dataBase.duplParkCheck(i);//duplParkCheck() 호출
		}
		for (int i = 31; i <= 45; i++) {
			pan2.add(notUseBtn[i] = new JButton(""));//31~35까지 버튼 생성후 판넬2에 추가
			notUseBtn[i].setEnabled(false);//버튼 비활성화
		}
		for (int i = 76; i <= 90; i++) {
			pan2.add(Btn[i] = new JButton(i + ""));//76~90까지 버튼 생성후 판넬2에 추가
			Btn[i].addActionListener(this);//버튼에 액션 리스너 
			Btn[i].setBackground(new Color(0xa79c8e));//버튼 색깔 설정
			dataBase.duplParkCheck(i);//duplParkCheck() 호출
		}
		// 여기까지 버튼 생성
		
	}


	/**
	 * 중앙 버튼 부분 판넬
	 */
	public void pan2() {
		pan2 = new JPanel();// 패널 생성
		GridLayout layout = new GridLayout(9, 1);// 그리드 레이아웃 생성
		pan2.setLayout(layout);// 레이아웃 설정
		add(pan2,"Center");// 판넬2 부착
	}

	/**
	 * 하단 상황판 부분 판넬
	 */
	public void pan3() {
		pan3 = new JPanel();// 패널 생성
		pan3.add(condition = new JTextArea());// 상황판을 생성
		
		condition.setPreferredSize(new Dimension(700, 200));// 상황판 사이즈
		condition.setBackground(new Color(0xF0E5DE));//상황판 배경색 설정
		condition.setEditable(false);// 편집 불가
		refresh.setBackground(Color.white);//버튼 배경색 설정
		pan3.add(refresh);//버튼부착
		pan3.add(history);// 버튼 부착
		pan3.add(configBtn);// 버튼 부착
		history.setBackground(Color.white);//버튼 배경색 설정
		configBtn.setBackground(Color.white);//버튼 배경색 설정
		history.addActionListener(this);// 액션리스너 추가
		configBtn.addActionListener(this);// 액션리스터 추가
		refresh.addActionListener(this);//액션리스너 추가
		pan3.setBackground(new Color(0x7C7877));//판넬3 배경색 설정
		add(pan3, "South");// 판넬3 부착
	}
	
	class dataBase extends DBStart{
	/**
	 * 입차시 버튼 색, 번호 변경하는 메소드
	 */
		public void duplParkCheck(int i){
			try {
				rs1 = stmt.executeQuery("select * from parktable"); // 테이블 가져온다
				rs1.first(); // 테이블을 첫행으로 커서를 가져온다	
					do {
						if (Integer.toString(i).equals(rs1.getString("parknum"))) {//자리번호와 parknum 이 같다면
						Btn[i].setBackground(Color.YELLOW);//버튼 배경색 설정
						Btn[i].setText(rs1.getString("carnum"));//버튼 라벨 차량번호로 설정
						}
					}while(rs1.next());//테이블 커서 다음줄로

			} catch (SQLException v) {
				v.printStackTrace(); // 오류 출력
			}
		}
	}

	@Override
	public void run() {
		
		
	}

}