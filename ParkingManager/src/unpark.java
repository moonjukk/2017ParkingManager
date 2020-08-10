/**
 * 관리가자 차량을 강제 출차시기는 클래스
 * 최종수정일: 2018/06/10
 * @author 송원섭
 * 
 * @see 소스코드
 * 
 */

import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class unpark extends JFrame {
	dataBase1 db1 = new dataBase1();//객체 생성
	dataBase2 db2 = new dataBase2();//객체 생성
	JButton Btn[] = new JButton[10000];// 자리 버튼
	JLabel jl=new JLabel("출차 하시겠습니까?");
	JButton b1=new JButton("확인");
	JButton b2=new JButton("취소");
	int carnum;//차량번호 저장 변수
	int c=0;//자리번호 저장할 변수
	String a1,a2,a3,a4,a5;//id, password, cometime, ismember 의 값을 저장할 변수
	String stringFee;// pay 의 값을 저장할 변수
	
	unpark(int carNumber){//메인에서 carnum받아옴
		this.carnum=carNumber;//받아온 값을 carnum에 저장
		setTitle("출차 확인");
		setLayout(null);
		b1.setBackground(Color.white);
		b2.setBackground(Color.white);
		jl.setFont(new Font("굴림", Font.BOLD, 25));//폰트설정
		jl.setBounds(60, 32, 266, 60); //라벨 위치 설정
		b1.setBounds(90, 132, 66, 40); //버튼 위치 설정
		b2.setBounds(180, 132, 66, 40); //버튼 위치 설정
		add(jl);//
		add(b1);//
		add(b2);//버튼, 라벨, 패널 추가
		
		
		b1.addActionListener(new ActionListener() {//확인 버튼 클릭시
			/**
			 * 확인 클릭시 실행되는 이벤트
			 */
			public void actionPerformed(ActionEvent e) {//확인 버튼 클릭시
				for (int i = 1; i < Btn.length; i++) {// 버튼을 처음부터 끝까지 검색
					String temp = mainWindow.Btn[i].getLabel();// temp에 버튼의 라벨을 저장
					int temp2 = Integer.parseInt(temp);// temp1에 저장한 라벨을 정수형태로 변환하여
														// 저장해 둠
					if (temp2 == carnum) {
						// temp2에 저장해둔 라벨과 액션이벤트로 들어온 매개변수가 같다면
						// 차가 주차되어 있는 자리가 바로 그 자리 이므로
						mainWindow.Btn[i].setBackground(new Color(0xa79c8e));//관리자 메인 화면의 버튼 배경색 변경
						mainWindow.Btn[i].setText(String.valueOf(i));//관리자 메인 화면의 버튼 라벨 변경
						c=i;//자리번호를 저장
						db1.delete(temp2);//parktable값 삭제
						db2.insertaddmin();//admin에 값 추가
						
						break;// 루프를 나옴
						
					}dispose();// 창닫음
				}
				
			}
		});
		b2.addActionListener(new ActionListener() {//취소버튼 클릭시
			/**
			 * 취소 클릭시 창 닫는 이벤트
			 */
			public void actionPerformed(ActionEvent e) {//취소버튼 클릭시
				setVisible(false);
				dispose();// 창닫음
			}
		});
		
		setSize(350,250);
		setLocation(200, 200);
		setVisible(true);
		setResizable(false);// 크기변경 불가
	}
	class dataBase1 extends DBStart {
		/**
		 * 관리자에서  데이터베이스 강제 삭제하는 메소드
		 */
		public void delete(int carnum) {//차량 번호를 가져와 삭제
		         try {
		        	 rs1 = stmt.executeQuery("select * from parktable"); // 테이블 가져온다
					 rs1.first(); // 테이블을 첫행으로 커서를 가져온다
					 do {
						 if(Integer.toString(carnum).equals(rs1.getString("carnum"))) {//차량번호와 데이터베이스에있는 carnum과 값이 같다면
							 a1=rs1.getString("id");//id값을 a1에 저장
							 a2=rs1.getString("password");//password값을 a2에 저장
							 a3=rs1.getString("cometime");//cometime값을 a3에 저장
							 a4=rs1.getString("ismember");//ismember값을 a4에 저장
							 
							 String a=rs1.getString("parknum");//parknum값을 a에저장
							 String sql = "delete from parktable where parknum ='" + a + "'";//데이터 베이스 값 삭제
		         try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

		            pstmt.executeUpdate();//데이터 베이스 업데이트

		         } catch (SQLException e1) {
		            // TODO Auto-generated catch block
		            e1.printStackTrace();
		         }
						 }
					 }while(rs1.next());
		         }catch (SQLException v) {
						v.printStackTrace(); // 오류 출력
					}
		         
		         
		      }
		}
	class dataBase2 extends DBStart {
		/**
		 * 강제 출차시 admin 데베에 추가하는 메소드
		 */
		public void insertaddmin() {//admin 데이터베이스에 값 추가
			SimpleDateFormat outTime = new SimpleDateFormat("yyMMddHHmm");//날짜 시간
			String setoutTime = outTime.format(parkingIn.date);//날짜 시간
			
			int fee = Integer.parseInt(setoutTime) - Integer.parseInt(a3);//현재시간과 입차시간 계산
			int feeAlpha = 0;

				if (fee < 200) {//시간 차이가 200 이하면
					feeAlpha = 3000;//가격은 3000
					stringFee = Integer.toString(feeAlpha);//값 저장
				}

				else if (fee > 200) {//시간 차이가 200보다 클 경우 계산
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
					stringFee = Integer.toString(feeAlpha);//계산된 값 저장
				}
				
			
			
			String sql = "INSERT admin (parknum, carnum, id, password, cometime, outtime, ismember, pay)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {//admin데이터 베이스에 추가
				pstmt.setString(1, Integer.toString(c));//parknum에 자리번호 저장
				pstmt.setString(2, Integer.toString(carnum));//carnum에 차량번호 저장
				pstmt.setString(3, a1);//id에 a1값 저장
				pstmt.setString(4, a2);//password에 a2값 저장
				pstmt.setString(5, a3);//cometime에 a3값 저장
				pstmt.setString(6, setoutTime);//outtime에 현재시간 저장
				pstmt.setString(7, a4);//ismember에 a4값 저장
				pstmt.setString(8, stringFee);//pay에 srtingFee값 저장
				pstmt.executeUpdate();//업데이트


			} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
		         
		         
		      }
		}

	
}

