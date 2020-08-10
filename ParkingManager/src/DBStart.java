/**
 * DB를 실행하는 클래스
 * 최종수정일 : 2018/05/22
 * @author 이제구
 * 
 * @see 소스코드
 * 
 *
 */
import java.sql.*;

public class DBStart {

	Statement stmt = null; 
	ResultSet rs1 = null;
	PreparedStatement pstmt;
	Connection conn;

	public DBStart() {
		try {
			Class.forName("com.oracle.jdbc.driver"); //드라이버 로드
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:myoracle", "user1", "hong");//lee라는 계정으로 lee DB접근

			System.out.println("DB 연결 완료"); // DB 연결완료 창

			stmt = conn.createStatement(); 
			
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		} catch (SQLException e) {
			System.out.println("DB 연결 오류");
		}

	}

}
