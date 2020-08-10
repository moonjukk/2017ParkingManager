
/**
 * 
 * 사용자가 회원/비회원으로 로그인 했을 경우 실행되는 클래스
 * 최종수정일 : 2018/06/17
 * 
 * @author 문주환
 * @see 소스코드
 * 
*/
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import java.sql.*;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mainWindows extends JFrame implements Runnable, ActionListener {
   dataBase db = new dataBase(); // db클래스에 접근하기 위해 미리 호출한다
   static String MyID = null; // 추후에 아이디 받아오면 수정 예정
   private Image img = null; // img를 넣기위해 변수값 초기화
   static String MyPasswd = null; // 패스워드 저장변수 초기화
   static String isMember = null; // 정회원 여부 변수
   static JPanel jp1;
   static JPanel jp2;
   private Boolean watchFlag = false; // 프로세스가 종료되면 스레드도 종료
   private Thread thread; // 시계 쓰레드
   public static JButton Btn[] = new JButton[150];// 자리 버튼
   static JButton notUseBtn[] = new JButton[100];// 사용하지 않는 버튼(자리 사이사이의 버튼)
   static JPanel cardPanel0 = new JPanel(); // 디폴트 상태의 leftPan 학교사진 출력
   static JPanel cardPanel1 = new JPanel(); // 지하주차장 구현
   static JPanel cardPanel2 = new JPanel(); // 지상주차장_입구 앞 구현
   static JPanel cardPanel3 = new JPanel(); // 지상주차장_승연관 앞 구현
   private JLabel la1 = new JLabel("학관");
   private JLabel la2 = new JLabel("<---");
   private JLabel la3 = new JLabel("느티아래");
   private JLabel calLabel = new JLabel(); // 디지털 시계구현하는 JLabel
   static CardLayout card = new CardLayout();// 레이아웃은 카드레이아웃으로 구현
   private JPanel cardPanel = new JPanel(card);
   private JButton jb1_1 = new JButton(new ImageIcon("Image/changemap.png")); // 장소변경 버튼 이미지
   private JButton jb1_2 = new JButton(new ImageIcon("Image/warning.png")); // 신고하기 버튼 이미지
   private JButton jb1_3 = new JButton(new ImageIcon("Image/refresh.png")); // 새로고침 버튼 이미지
   private JButton jb1_4 = new JButton(new ImageIcon("Image/notice.png")); // 공지사항 버튼 이미지
   static boolean[] parkflag = new boolean[91]; // 주차장에 주차가되있는지의 여부를 저장하는 flag
   static int outcarNum[] = new int[10000]; // 차량번호
   static int carNum = 0; // 차량번호 저장
   static int mapFlag = 0; // 카드레이아웃이 1인지 2인지 3인지 저장하는 변수

   /**
    * 메인메소드
    */
   public mainWindows() {
      setTitle("프로토타입");
      setSize(1500, 800);
      setLocation(150, 100);// 창이 뜨는 위치
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      db = new dataBase(); // 데이터베이스에 접근하기 위해서 호출
      db.getInfomation(); // 데이터베이스 클래스에 정보를 얻는 메소드 출력

      mapMaker(); // mapMaker메소드 호출
      leftPan(); // leftPan메소드 호출
      rightPan(); // rightPan메소드 호출

      parkflagManage(); // 주차공간이 주차중인지 확인하는 flag를 만들어주는 메소드

      setResizable(false);// 크기변경 불가
      setVisible(true);
   }

   /**
    * cardPanel에 미리 패널들을 저장하는 메소드
    */
   public void mapMaker() {

      // 각각의 카드레이아웃칸에 주차장칸을 구현한다.
      cardPanel1.setLayout(new GridLayout(6, 15));
      cardPanel2.setLayout(new GridLayout(3, 10));
      cardPanel3.setLayout(new GridLayout(3, 15));

      gridInit1(); // 1번카드패널에 버튼배치
      gridInit2(); // 2번카드패널에 버튼배치
      gridInit3(); // 3번카드패널에 버튼배츠

      cardPanel.add(cardPanel0, "default");
      cardPanel.add(cardPanel1, "underPark"); // 지하주차장
      cardPanel.add(cardPanel2, "entrance"); // 입구측 주차장
      cardPanel.add(cardPanel3, "sungyeon"); // 승연관 앞 주차장
   }

   /**
    * 새로고침을 누른 경우
    */
   public void refresh() {
      if (mapFlag == 1) {
         for (int i = 1; i <= 60; i++) { // 데이터베이스에 추가 주차된 차량이 있다면 상태창에도 추가하기
            db.getParkInfomation(i);
         }
         card.show(cardPanel, "underPark"); // 카드패널1 지하주차장 호출
      } else if (mapFlag == 2) {
         card.show(cardPanel, "entrance"); // 카드패널2 야외주차장(입구) 호출
         for (int i = 61; i <= 70; i++) { // 데이터베이스에 추가 주차된 차량이 있다면 상태창에도 추가하기
            db.getParkInfomation(i);
         }
      } else if (mapFlag == 3) {
         card.show(cardPanel, "sungyeon"); // 카드패널3 야외주차장(승연관 앞) 호출
         for (int i = 71; i <= 85; i++) { // 데이터베이스에 추가 주차된 차량이 있다면 상태창에도 추가하기
            db.getParkInfomation(i);
         }
      }
   }

   /**
    * 좌측 메인패널을 나타내는 메소드 디폴트값은 학교사진이다.
    */
   public void leftPan() {
      try {
         File sourceimage = new File("Image/학교배경.PNG"); // 학교배경 이미지 저장
         img = ImageIO.read(sourceimage);
      } catch (IOException e) {
         System.out.println("이미지파일이 없습니다.");
      }
      Image resizeImage = img.getScaledInstance(1000, 770, Image.SCALE_SMOOTH); // 이미지 크기 조절
      JLabel label = new JLabel(new ImageIcon(resizeImage));
      cardPanel0.add(label); // 카드레이아웃의 디폴드값인 카드패널0에 추가
      cardPanel0.setBackground(Color.WHITE); // 배경은 하얀색
      cardPanel.setBounds(0, 0, 950, 765); // 카드레이아웃의 패널의 절대위치 선언

      add(cardPanel);
   }

   /**
    * 오른쪽패널 생성 메소드
    */
   public void rightPan() {
      jp2 = new JPanel(); // 패널 생성
         jp2.setLayout(null); // 우측패널 레이아웃 설정

         pan1(); // 메소드호출
         pan2(); // 메소드호출
         pan3(); // 메소드호출

         jp2.setBounds(950,0,550,765); // 우측 전체패널 크기설정
         add(jp2);
   }

   /**
    * 시간과 사용자 정보 출력하는 메소드
    */
   public void pan1() {
      JPanel jpan1 = new JPanel();
       jpan1.setLayout(null);
       jpan1.setBounds(950, 0, 543, 60);
       
      JPanel ClockPan = new JPanel();
      ClockPan.setBounds(950, 0, 275, 60); // 위치지정
      thread = new Thread(this); // 시계쓰레드 작동
      thread.start(); // 스레드 작동
      ClockPan.add(calLabel, "Center"); // 시계를 중앙에 출력함
      ClockPan.setBackground(Color.WHITE); // 배경은 하얀색
      add(ClockPan);
      // 여기까지 시계추가 하는 곳

      JPanel WelcomPan = new JPanel(); // 회원정보 패널
      WelcomPan.setBounds(1225, 0, 275, 60); // 위치지정

      JLabel IDlabel1 = new JLabel(MyID);
      JLabel IDlabel2 = new JLabel("님 환영합니다");
      JButton LogOut = new JButton("로그아웃");
      
      // 로그아웃 클릭시 리스너
      LogOut.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            System.exit(0);
            new ParkingStartMenu();
            ParkingStartMenu.startFlag = false; // startFlag 초기화
            dispose();
         }
      });
      IDlabel1.setFont(new Font("고딕", Font.BOLD, 20)); // 글꼴 수정
      IDlabel1.setForeground(Color.BLUE);
      IDlabel2.setFont(new Font("고딕", Font.BOLD, 16));
      WelcomPan.add(IDlabel1); // 아이디 부분
      WelcomPan.add(IDlabel2); // 어서오세요 부분
      LogOut.setBackground(Color.ORANGE);
      WelcomPan.add(LogOut); // 로그아웃 버튼 넣는다
      WelcomPan.setBackground(Color.WHITE);
      add(WelcomPan);
      // 여기까지 회원정보를 출력한는 패널
   }

   /**
    * 주차공간과 주차요금안내를 출력하는 메소드
    */
   public void pan2() {
      JPanel jpan2 = new JPanel();
      jpan2.setLayout(new GridLayout(1, 2));
      jpan2.setBounds(950, 60, 543, 340);

      JPanel RemainPark = new JPanel();
      
      // 배경 이미지 삽입
      try {
         File sourceimage = new File("Image/배경.PNG");
         img = ImageIO.read(sourceimage);
      } catch (IOException e) {
         System.out.println("이미지파일이 없습니다.");
      }
      Image resizeImage2 = img.getScaledInstance(270, 335, Image.SCALE_SMOOTH);
      JLabel label2 = new JLabel(new ImageIcon(resizeImage2));
      RemainPark.add(label2);
      add(RemainPark);

      jpan2.add(RemainPark); // 이미지를 패널에 삽입한다.

      JPanel chargePanel = new JPanel();
      
      //주차요금 이미지 삽입
      try {
         File sourceimage = new File("Image/parkfee.png");
         img = ImageIO.read(sourceimage);
      } catch (IOException e) {
         System.out.println("이미지파일이 없습니다.");
      }
      Image resizeImage = img.getScaledInstance(270, 340, Image.SCALE_SMOOTH);
      JLabel label = new JLabel(new ImageIcon(resizeImage));
      chargePanel.add(label);
      add(chargePanel);

      jpan2.add(label);

      add(jpan2);
   }

   /**
    * 우측 아래 버튼3개를 생성하는 메소드
    */
   public void pan3() {
      JPanel jpan3 = new JPanel();
      jpan3.setLayout(new GridLayout(2, 2)); // 2x2꼴의 그리드레이아웃

      jb1_1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new changeMap(); // 메소드를 호출
         }
      });

      jb1_2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            new report(); // 메소드를 호출
         }
      });

      jb1_3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            refresh(); // 메소드를 호출
         }
      });

      jb1_4.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "사용에 문제가 있으면 신고하기 혹은 010-1234-5678 전화주세요", "공지사항", 1);
         }
      });

      jpan3.add(jb1_1);
      jpan3.add(jb1_2);
      jpan3.add(jb1_3);
      jpan3.add(jb1_4);

      jpan3.setBounds(950, 400, 543, 365); // 위치지정

      add(jpan3);
   }

   /**
    * 지하주차장 패널에 버튼을 추가하는 메소드
    */
   private void gridInit1() {
      // 주차가 가능한 공간과 불가능 한 공간을 나누어서 생성
      for (int i = 1; i <= 15; i++) {
         Btn[i] = new JButton(i + "");
         Btn[i].setBackground(Color.PINK);
         cardPanel1.add(Btn[i]); // 카드 패널에 생성한 버튼을 삽입
         Btn[i].addActionListener(this); // 버튼에 리스너를 삽입(주차하기,출차하기)
         db.getParkInfomation(i); // 데이터베이스에서 정보를 확인하기
      }
      for (int i = 1; i <= 15; i++) {
         cardPanel1.add(notUseBtn[i] = new JButton(""));
         notUseBtn[i].setEnabled(false); // 주차공간이 아닌곳은 Enable을 false
      }
      for (int i = 16; i <= 45; i++) {
         Btn[i] = new JButton(i + "");
         Btn[i].setBackground(Color.PINK);
         cardPanel1.add(Btn[i]);
         Btn[i].addActionListener(this);
         db.getParkInfomation(i);
      }
      for (int i = 16; i <= 30; i++) {
         cardPanel1.add(notUseBtn[i] = new JButton(""));
         notUseBtn[i].setEnabled(false);
      }
      for (int i = 46; i <= 60; i++) {
         Btn[i] = new JButton(i + "");
         Btn[i].setBackground(Color.PINK);
         cardPanel1.add(Btn[i]);
         Btn[i].addActionListener(this);
         db.getParkInfomation(i);
      }
   }

   /**
    * 지상주차장 (입구쪽) 패널에 버튼을 추가하는 메소드
    */
   private void gridInit2() {
      // gridInit1과 같은 방식
      for (int i = 1; i <= 10; i++) {
         cardPanel2.add(notUseBtn[i] = new JButton(""));
         notUseBtn[i].setBackground(new Color(230, 252, 255));
         notUseBtn[i].setEnabled(false);
         db.getParkInfomation(i);
      }
      for (int i = 11; i <= 20; i++) {
         cardPanel2.add(notUseBtn[i] = new JButton(""));
         notUseBtn[i].setBackground(new Color(207, 207, 207));
         notUseBtn[i].setEnabled(false);
      }
      for (int i = 61; i <= 70; i++) {
         Btn[i] = new JButton(i + "");
         Btn[i].setBackground(Color.PINK);
         cardPanel2.add(Btn[i]);
         Btn[i].addActionListener(this);
      }

   }

   /**
    * 지상주차장(승연관 앞) 버튼을 추가하는 메소드
    */
   private void gridInit3() {
      // gridInit1,2와 같은 방식
      for (int i = 1; i <= 15; i++) {
         cardPanel3.add(notUseBtn[i] = new JButton(""));
         notUseBtn[i].setBackground(new Color(230, 252, 255));
         notUseBtn[i].setEnabled(false);
      }
      for (int i = 16; i <= 30; i++) {
         cardPanel3.add(notUseBtn[i] = new JButton(""));
         notUseBtn[i].setBackground(new Color(207, 207, 207));
         notUseBtn[i].setEnabled(false);
      }
      for (int i = 71; i <= 85; i++) {
         Btn[i] = new JButton(i + "");
         Btn[i].setBackground(Color.PINK);
         cardPanel3.add(Btn[i]);
         Btn[i].addActionListener(this);
         db.getParkInfomation(i);
      }
   }

   /**
    * 주차공간이 주차중인지 확인하는 flag를 만들어주는 메소드
    */
   public void parkflagManage() {
      for (int i = 0; i <= 90; i++) { // 총 90칸의 주차공간이 있기 때문에
         parkflag[i] = true;
      }
   }

   /**
    * 주차버튼 클릭 시 발생하는 액션퍼펌드
    */
   public void actionPerformed(ActionEvent e) {
      JButton moon = (JButton) e.getSource();
      int num = Integer.parseInt(moon.getText());
      
      // 숫자가 91보다 작은경우는 주차가되지않아서 주차장 번호가 버튼에 있기 때문임
      if (num < 91) {
         if (parkflag[num] == true) {
            // moon.setText("주차");
            new parkingIn(num);
            // carNum = parkingIn.carNum;
         }
      // 반면 999보다크면 최소 1000인 차량번호가 삽입 되어있기 때문임
      } else if (num > 999) {

         new parkingOut(outcarNum[num]);
      }
   }

   /**
    * 시계를 만들기 위한 메소드
    */
   private void calShow() {
      Calendar calendar = Calendar.getInstance(); // 싱글톤
      int m = calendar.get(Calendar.MONTH) + 1; // Month는 0부터 시작
      int d = calendar.get(Calendar.DATE);
      int h = calendar.get(Calendar.HOUR_OF_DAY);
      int mi = calendar.get(Calendar.MINUTE);
      int s = calendar.get(Calendar.SECOND);
      calLabel.setText(m + "월" + d + "일 " + h + "시" + mi + "분" + s + "초");
      calLabel.setFont(new Font("굴림", Font.BOLD, 20)); // 글꼴지정
   }

   /** 시계를 작동하기 위한 쓰레드 메소드
    */
   public void run() {
      while (true) {
         if (watchFlag)
            break; // b가 true이면(프로세스가 종료되면)
         calShow();
         try {
            Thread.sleep(1000); // 1초마다 시작해야해서
         } catch (Exception e) {
            // TODO: handle exception
         }
      }
   }

   /**
    * 데이터베이스를 사용하기 위한 클래스
    */
   class dataBase extends DBStart {

      /**
       * 처음에 프로그램을 켰을 때 데이터 베이스에서 회원의 정보를 얻기위한 메소드
       */
      public void getInfomation() {

         // 회원인 경우
         if (ParkingStartMenu.memberFlag == true) {
            try {
               rs1 = stmt.executeQuery("select * from memberimfo"); // 테이블 가져온다
               rs1.first(); // 테이블을 첫행으로 커서를 가져온다

               do {
                  if (MyID.equals(rs1.getString("id"))) {
                     MyPasswd = rs1.getString("password"); // passwd를 읽어온다
                     isMember = rs1.getString("member"); // 정회원인지 여부를 읽어온다
                     carNum = Integer.parseInt(rs1.getString("carnumber"));

                     
                     break;

                  }

               } while (rs1.next()); // 데이터베이스를 다움행으로 커서를 옮긴다.

            } catch (SQLException v) {
               v.printStackTrace(); // 오류 출력
            }
         }

         // 비회원인 경우
         else if (ParkingStartMenu.memberFlag == false) {

            try {
               rs1 = stmt.executeQuery("select * from nomemberimfo"); // 테이블 가져온다
               rs1.first(); // 테이블을 첫행으로 커서를 가져온다

               do {
                  if (MyID.equals(rs1.getString("carnum"))) {
                     MyPasswd = rs1.getString("password"); // PW를 읽어온다
                     carNum = Integer.parseInt(rs1.getString("carnum")); // 차량번호를 읽어온다
                     isMember = "F"; // 비회원이기 때문에 읽어올 필요없이 F\
                     break;

                  }

               } while (rs1.next()); // 데이터베이스를 다움행으로 커서를 옮긴다.

            } catch (SQLException v) {
               v.printStackTrace(); // 오류 출력
            }

         }

      }

      /**
       * 주차장에 차가 주차되어있는지 확인하는 메소드
       */
      public void getParkInfomation(int num) {

         try {
            rs1 = stmt.executeQuery("select * from parktable"); // 테이블 가져온다
            rs1.first(); // 테이블을 첫행으로 커서를 가져온다
            do {
               // 만약 주차된 공간인지 데이터베이스에서 확인이 되면 주차된 곳으로 색,데이터값을 바꿔준다
               if (Integer.toString(num).equals(rs1.getString("parknum"))) {
                  Btn[num].setBackground(Color.YELLOW);
                  parkflag[num] = false;
                  Btn[num].setText(rs1.getString("carnum"));
                  outcarNum[Integer.parseInt(rs1.getString("carnum"))] = num;
                  break;
               }
            } while (rs1.next()); // 데이터베이스를 다움행으로 커서를 옮긴다.

         } catch (SQLException v) {
            v.printStackTrace(); // 오류 출력
         }

      }
   }
}