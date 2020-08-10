/**
 * 출차후 데이터 값들을 출력해주는 클래스
 * 최종수정일: 2018/05/30
 * @author 송원섭
 * 
 * @see 소스코드
 * 
 */
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

import javax.swing.*;

@SuppressWarnings("serial")
public class logWindow extends JFrame implements ActionListener {
	dataBase1 db1 = new dataBase1();
	JRadioButton cb1;//차량번호 버튼
	//DefaultTableColumnModel cModel;
	String[] historyCol = { "자리번호", "차량번호","id", "password", "입차시간", "출차시간", "회원", "요금(원)" };//테이블 상단 목록
	JLabel label1;//주차내역 라벨
	JLabel label2;//통계 라벨
	DefaultTableModel model;//차량 검색 테이블 모델
	DefaultTableModel model2;//통계 테이블 모델
	JPanel pan1;//테이블 판넬
	JPanel pan3;//검색 부분 판넬
	JScrollPane scrollpane;//자량 검색 테이블 스크롤
	JScrollPane scrollpane2;//통계 테이블 스크롤
	JButton search;//검색 버튼
	JTable table;//차량 검색 테이블
	JTable table2;//통계 테이블
	String temp1[]=new String[8];//admin 값 저장할 배열
	JTextField tf1;//차량번호 검색할 텍스트필드
	String[] totalCol = { "통계", "차량댓수", "요금합계(원)" };//테이블 상단 목록

	/**
	 * 생성자
	 */
	public logWindow() {
		Container c=getContentPane();
		setTitle("주차내역");// 제목표시줄
		setSize(1000, 700);// 사이즈
		setLocation(600, 100);
		model = new DefaultTableModel(historyCol, 0);// 주차 내역 테이블 모델
		table = new JTable(model);// 테이블
		model2 = new DefaultTableModel(totalCol, 0);// 통계 테이블 모델
		table2 = new JTable(model2);// 테이블
		pan1 = new JPanel();// 패널 생성
		pan3 = new JPanel();// 패널 생성
		pan1();// 메소드 호출
		pan3();// 메소드 호출
		
		setVisible(true);// 보이게 함
		setResizable(false);// 크기 조절 불가
		c.setBackground(new Color(0x7C7877));//배경색 설정
	}

	/**
	 *  검색 버튼 클릭시 발생하는 액션이벤트
	 */
	public void actionPerformed(ActionEvent ae) {//액션 이벤트
		if (ae.getSource() == search) {// 검색 버튼
			String temp1 = tf1.getText();// 차량 번호
			removeTable();// 테이블을 지움
			// 검색 부분
			// 차량 번호로 검색
			if (!temp1.isEmpty() ) {//검색 칸이 비어있지 않을경우
				db1.NumberSearch(tf1.getText());//NumberSearch 호출
			}
			
			else {// 칸이 모두 비어있을 경우
				db1.asdf();// asdf호출
				db1.qwer();// qwer호출
			}

		}

	}

	/**
	 * 테이블 부분 판넬 메소드
	 */
	public void pan1() {
		Font f1 = new Font("돋움", Font.BOLD, 24);// 폰트 생성
		BoxLayout blayout = new BoxLayout(pan1, BoxLayout.Y_AXIS);// 박스레이아웃 생성
		
		label1 = new JLabel("주차내역");// 라벨 생성
		label1.setFont(f1);// 폰트설정
		label1.setForeground(new Color(0xF8FAFF));//배경색 설정
		pan1.add(label1);// 라벨 부착
		pan1.setLayout(blayout);// 레이아웃 설정
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);// 테이블 자동 리사이즈를 off
		table.getColumnModel().getColumn(0).setPreferredWidth(100);//
		table.getColumnModel().getColumn(1).setPreferredWidth(100);//
		table.getColumnModel().getColumn(2).setPreferredWidth(120);//
		table.getColumnModel().getColumn(3).setPreferredWidth(120);//
		table.getColumnModel().getColumn(4).setPreferredWidth(220);//
		table.getColumnModel().getColumn(5).setPreferredWidth(220);//
		table.getColumnModel().getColumn(6).setPreferredWidth(100);//
		table.getColumnModel().getColumn(7).setPreferredWidth(100);// 각각의
																	// colomn의
																	// 길이를 정해줌
		scrollpane = new JScrollPane(table);// 스크롤바가 생기는 판넬에 테이블 부착
		scrollpane.getViewport().setBackground(new Color(0xD9D4CF)); //테이블 배경색
		table.getTableHeader().setBackground(new Color(0xF0E5DE)); // 테이블 상단색
		pan1.add(scrollpane);// 스크롤판넬을 판넬1에 부착
		label2 = new JLabel("통계");// 라벨 생성
		label2.setFont(f1);// 폰트설정
		label2.setForeground(new Color(0xF8FAFF));//라벨 배경색 설정
		pan1.add(label2);// 라벨 부착
		scrollpane2 = new JScrollPane(table2);// 스크롤바가 생기는 판넬에 테이블 부착
		scrollpane2.getViewport().setBackground(new Color(0xD9D4CF)); //테이블 배경색
		table2.getTableHeader().setBackground(new Color(0xF0E5DE)); // 테이블 상단색
		label2.setBackground(new Color(0x56445D));//라벨 배경색 설정
		pan1.add(scrollpane2);// 스크롤판넬을 판넬1에 부착
		table2.setPreferredScrollableViewportSize(new Dimension(300, 30));// 크기
																			// 설정
		pan1.setBackground(new Color(0x7C7877));//패널 배경색
		add(pan1, "North");// 판넬1 부착
		table.setEnabled(false);//
		table2.setEnabled(false);// 테이블을 비활성화

	}

	/**
	 * 검색부분 판넬
	 */
	public void pan3() {
		ButtonGroup cbg = new ButtonGroup();// 체크박스 그룹화
		cb1 = new JRadioButton("차량번호", true);// 체크박스 생성
		//cb1.setBackground(new Color(0x56445D)); //버튼 배경색
		cb1.setForeground(Color.white);//"차량번호" 라벨색
		cb1.setForeground(Color.white); //버튼 배경색
		cb1.setBackground(new Color(0x7C7877));//버튼 배경색 설정
		cbg.add(cb1);//체크박스 그룹화
		tf1 = new JTextField(10);//텍스트 필드 크기 설정
		pan3.add(cb1);// 체크박스 부착
		pan3.add(tf1);// 텍스트필드 부착
		search = new JButton("검색");// 검색버튼
		search.addActionListener(this);// 액션리스너 등록
		pan3.add(search);// //검색버튼 부착
		add(pan3, "South");// 판넬 부착
		pan3.setBackground(new Color(0x7C7877));//pan3배경색 설정


	}
	class dataBase1 extends DBStart {
		/**
		 * 검색 클릭시 admin 데이터베이스 값 테이블에 추가하는 메소드
		 */
		public void asdf() {
		         try {
		        	 rs1 = stmt.executeQuery("select * from admin"); // 테이블 가져온다
					 rs1.first(); // 테이블을 첫행으로 커서를 가져온다
					 do {
						 temp1[0]=rs1.getString("parknum");//
					 	 temp1[1]=rs1.getString("carnum");//
					 	 temp1[2]=rs1.getString("id");//
					 	 temp1[3]=rs1.getString("password");//
					 	 temp1[4]=rs1.getString("cometime");//
					 	 temp1[5]=rs1.getString("outtime");//
					 	 temp1[6]=rs1.getString("ismember");//
					 	 temp1[7]=rs1.getString("pay");//temp1 배열에 순서대로 admin데이터 베이스 값 저장
					 	 model.addRow(temp1);//table에 배열에 저장된 값 추가
					 	 //dispose();// 창을 닫음
		         
						 }while(rs1.next());//테이블 다음줄로 이동
					 }
		         	catch (SQLException v) {
						v.printStackTrace(); // 오류 출력
					}
		         
		         
		      }
		/**
		 * 차량의 통계값을 테이블에 추가하는 메소드
		 */
		public void qwer() {
			int a = 0;//pay값 저장할 변수
			int b = 0;//차량 댓수 저장할 변수
			try {
				 rs1 = stmt.executeQuery("select * from admin"); // 테이블 가져온다
				 rs1.first(); // 테이블을 첫행으로 커서를 가져온다
				 do {
					 a=a+Integer.parseInt(rs1.getString("pay"));//a에 admin의 pay값 저장
					 b++;//차량 댓수 저장
				 }while(rs1.next());//다음줄로 커서 이동
				 temp1[0]="";
				 temp1[1]=Integer.toString(b);//배열에 차량대수 값 저장
				 temp1[2]=Integer.toString(a);//배열에 pay값 저장
				 model2.addRow(temp1);//통계 테이블에 추가
				 
			}catch (SQLException v) {
				v.printStackTrace(); // 오류 출력
			}
	
		}
		/**
		 * 차량번호 검색시 검색된 값만  테이블에추가하는 메소드
		 */
		public void NumberSearch(String carNumber) {//
			int b=0;//차량댓수 저장할 변수
			int a=0;//pay값 저장할 변수
			try {
				 rs1 = stmt.executeQuery("select * from admin"); // 테이블 가져온다
				 rs1.first(); // 테이블을 첫행으로 커서를 가져온다
				 do {
					 if(carNumber.equals(rs1.getString("carnum"))) {//차량 번호와 admin 테이블의 carnum값이 같다면
						 temp1[0]=rs1.getString("parknum");//
					 	 temp1[1]=rs1.getString("carnum");//
					 	 temp1[2]=rs1.getString("id");//
					 	 temp1[3]=rs1.getString("password");//
					 	 temp1[4]=rs1.getString("cometime");//
					 	 temp1[5]=rs1.getString("outtime");//
					 	 temp1[6]=rs1.getString("ismember");//
					 	 temp1[7]=rs1.getString("pay");//temp1배열에 순서대로 admin데이터 베이스 값 저장
					 	 model.addRow(temp1);//table에 배열에 저장된 값 추가
					 }
				 }while(rs1.next());//다음줄로 커서 이동
				 
				 rs1.first(); // 테이블을 첫행으로 커서를 가져온다
				 do {
					 if(carNumber.equals(rs1.getString("carnum"))) {//차량 번호와 admin 테이블의 carnum값이 같다면
						 b++;//차량 댓수
						 a=a+Integer.parseInt(rs1.getString("pay"));//pay값 저장
						 
					 }
				 }while(rs1.next());//다음줄로 커서이동
				 		 temp1[0]="";
						 temp1[1]=Integer.toString(b);//배열에 차량대수 값 저장
					 	 temp1[2]=Integer.toString(a);//배열에 pay 값 저장
					 	 model2.addRow(temp1);//통계 테이블에 값 추가
				 
			}catch (SQLException v) {
				v.printStackTrace(); // 오류 출력
			}
		}
	}

	/**
	 * 테이블 지우는 메소드
	 */
	public void removeTable() {
		int temp = table.getRowCount();// 테이블을 줄 수를 받아와서
		for (int i = 0; i < temp; i++) {
			model.removeRow(0);// 제거
		}
		temp = table2.getRowCount();// 테이블의 줄 수를 받아와서
		for (int i = 0; i < temp; i++) {
			model2.removeRow(0);// 제거
		}
	}

}
