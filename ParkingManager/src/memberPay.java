/**
 * 결제를 하는 창
 * 최종수정일 : 2018/06/07
 * @author 이제구
 * 
 * @see 소스코드
 * 
 *
 */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;

/**
 * 결제를 하는 JFrame
 */
public class memberPay extends JFrame {

	JButton jb1 = new JButton("결제하기"); 
	JButton jb2 = new JButton("나가기");

	JPanel jp = new JPanel();
	String m = "30000"; // 한달에 30000원 결제
	static int money;

	// 신용카드 번호 입력하는 텍스트 필드
	private JTextField jt1 = new JTextField(10);
	private JTextField jt2 = new JTextField(6);
	private JTextField jt3 = new JTextField(4);
	private JTextField jt4 = new JTextField(4);
	private JTextField jt5 = new JTextField(4);
	private JTextField jt6 = new JTextField(4);
	private JTextField jt7 = new JTextField(7);
	

	private JLabel jl1 = new JLabel("결제금액");
	private JLabel jl2 = new JLabel("카드선택");
	private JLabel jl3 = new JLabel(" - ");
	private JLabel jl4 = new JLabel(" - ");
	private JLabel jl5 = new JLabel(" - ");
	private JLabel jl6 = new JLabel("결제금액");
	
	// 어떤 카드로 결제하는 지 배열
	String[] bank = { " ", "신한카드", "BC카드", "KB국민카드", "삼성카드", "현대카드", "롯데카드", "하나카드", "NH농협카드", "씨티카드", "우리카드",
			"카카오뱅크카드", "케이뱅크카드", "전북은행카드", "새마을금고카드" };

	/**
	 * memberPay 기본 생성자
	 */
	public memberPay() {
		setTitle("결제하기");// 제목표시줄
		setSize(350, 250);// 사이즈
		setLocation(350, 250);// 창이 뜨는 위치
		setLayout(new GridLayout(5, 1));
		setBackground(Color.GREEN);

		

		pan1();// 메소드 호출

		setVisible(true);// 보이로독 함
		setResizable(false);// 크기변경 불가
	}
	
	public void pan1() {
		JPanel a = new JPanel();
		JPanel b = new JPanel();
		JPanel c = new JPanel();
		JPanel d = new JPanel();
		JPanel e = new JPanel();

		a.add(jl1); // 보유 캐시
		jt1.setText(m); // 캐시 보유량 jt1에 추가하기
		jt1.setEnabled(false);
		a.add(jt1); // ㅇㅇㅇ 원 보유중

		b.add(jl2);
		JComboBox combo2 = new JComboBox(bank);
		b.add(combo2);

		// b.add(jt2);

		c.add(jt3);
		c.add(jl3);
		c.add(jt4);
		c.add(jl4);
		c.add(jt5);
		c.add(jl5);
		c.add(jt6);

		d.add(jl6);
		d.add(jt7);

		// 캐쉬충전 버튼 클릭시 실행되는 리스너
		jb1.addActionListener(new PayActionListener());

		// 취소버튼 클릭시 실행되는 리스너
		jb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();// 창닫음
			}
		});

		e.add(jb1);
		e.add(jb2);

		add(a);
		add(b);
		add(c);
		add(d);
		add(e);
	}
	/**
	 * 카드 결제하는 리스너
	 */
	class PayActionListener extends DBStart implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (m.equals(jt7.getText())) {
				JOptionPane.showMessageDialog(null, "결제완료되었습니다", "결제 완료", 0);
				String sql = "update memberimfo set flag=?";
				sql  = sql + " WHERE id=?";
				try {

					rs1 = stmt.executeQuery("select * from memberimfo"); // 테이블 가져온다
					rs1.first(); // 테이블을 첫행으로 커서를 가져온다
					do {
						
						if (mainWindows.MyID.equals(rs1.getString("id"))) {
							
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, "T");
							pstmt.setString(2, mainWindows.MyID);
							pstmt.executeUpdate();

						}
					} while (rs1.next());

				} catch (SQLException v) {
					v.printStackTrace();
				}

				setVisible(false);
				dispose();// 창닫음
				

			} else
				JOptionPane.showMessageDialog(null, "금액이 부족합니다", "결제 대기", 0);
		}

	}

}