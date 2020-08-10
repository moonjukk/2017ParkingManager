/**
 * 
 * 사용자가 메인클래스에서 주차변경 버튼을 클릭시 호출되는 메소드
 * 최종수정일 : 2018/05/28
 * 
 * @author 문주환
 * @see 소스코드
 * 
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

// 주차 장소변경 버튼 클릭 시 실행되는 클래스로, 메인클래스의 left패널을 표현한다.
public class changeMap extends JFrame {
	
	private Image img = null; // 이미지 변수를 초기화

	JButton jb1 = new JButton("변경"); // 버튼 구현
	JButton jb2 = new JButton("취소");
	
	JPanel jp = new JPanel(); // 기본 패널 구현
	Container contentPane = getContentPane();
	
	// 생성자
	public changeMap() {
		setTitle("주차공간변경");// 제목표시줄
		setSize(450, 526);// 사이즈
		setLocation(600, 250);// 창이 뜨는 위치
		setLayout(new FlowLayout());
		setBackground(Color.GREEN);
		
		pan1();// 메소드 호출
		//pan2();// 메소드 호출
		
		setVisible(true);// 보이로독 함
		setResizable(false);// 크기변경 불가
	}

	public void pan1() {
		ButtonGroup g = new ButtonGroup(); // 하나만 선택하게 하기
		
		//학교이미지 삽입
		try {
			File sourceimage = new File("Image/성공회대.jpg");
			img = ImageIO.read(sourceimage);
		} catch (IOException e) {
			System.out.println("이미지파일이 없습니다.");
		}
		Image resizeImage = img.getScaledInstance( 436, 450, Image.SCALE_SMOOTH);	
		JLabel label = new JLabel(new ImageIcon(resizeImage));
		contentPane.add(label);
		
		JButton one = new JButton("지하주차장");
		JButton two = new JButton("야외주차장(정문입구)");
		JButton three = new JButton("야외주차장(승연관 앞)");
		
		// 지하주차장버튼 클릭시 실행되는 리스너
		one.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainWindows.mapFlag = 1;
				JOptionPane.showMessageDialog(null, "변경완료, 새로고침 눌러주세요", "주차장 장소변경", 1); 
				setVisible(false);
				dispose();// 창닫음
				}
		});
		
		// 야외주차장(정문입구) 클릭시 실행되는 리스너
		two.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainWindows.mapFlag = 2;
				JOptionPane.showMessageDialog(null, "변경완료, 새로고침 눌러주세요", "주차장 장소변경", 1);
				setVisible(false);
				dispose();// 창닫음
				}
		});
		
		// 야외주차장(승연관 앞) 클릭시 실행되는 리스너
		three.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainWindows.mapFlag = 3;
				JOptionPane.showMessageDialog(null, "변경완료, 새로고침 눌러주세요", "주차장 장소변경", 1);
				setVisible(false);
				dispose();// 창닫음
				}
		});
		
		g.add(one);
		g.add(two);
		g.add(three);
		
		contentPane.add(one);
		contentPane.add(two);
		contentPane.add(three);
	}
}