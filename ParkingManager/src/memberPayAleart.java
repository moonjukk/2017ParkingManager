/**
 * 결제화면 알림 창
 * 최종수정일 : 2018/06/11
 * @author 이제구
 * 
 * @see 소스코드
 * 
 *
 */
//정회원 1일마다 알림알려주는 클래스
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
public class memberPayAleart extends JFrame{
	
	JButton btn1 = new JButton("결제하기");
	JButton btn2 = new JButton("취소");
	JLabel jl = new JLabel("정회원인 분들의 결제 날입니다.");
	JLabel j2 = new JLabel("결제를 하지 않으시면 로그인 되지 않습니다.");
	public memberPayAleart () {
		setTitle("결제 알림");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBackground(Color.WHITE);
		setLayout(null);
		
		jl.setBounds(50,50,300,40);
		j2.setBounds(50,92,300,40);
		btn1.setBounds(50,180,90,50);
		btn2.setBounds(200,180,90,50);
		
		btn1.addActionListener(new MyActionListener());
		btn2.addActionListener(new MyActionListener2());
		add(btn1);
		add(btn2);
		add(jl);
		add(j2);
		setBounds(600,500,380,300);
		setVisible(true);
	}
	class MyActionListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			new memberPay();
			dispose();
		}
	}
	class MyActionListener2 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();
			
		}
	}

}
