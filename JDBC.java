/**********************************
 * 		Phase 3 - JDBC
 *		Team 8 
 ***********************************/

import java.sql.*;
import java.util.*;


public class JDBC {
	/*
	 * ------------------- DB 연결하기 -------------------
	 */
	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_UNIVERSITY = "university";
	public static final String USER_PASSWD = "comp322";

	public static void main(String[] args) {
		Connection conn = null; // Connection object
		Statement stmt = null; // Statement object

		try {
			// Load a JDBC driver for Oracle DBMS 드라이버 가져오기
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// Get a Connection object
			System.out.println("Driver Loading: Success!");
		} catch (ClassNotFoundException e) {
			System.err.println("error = " + e.getMessage());
			System.exit(1);
		}

		// Make a connection 연결하기(conn)변수에
		try {
			conn = DriverManager.getConnection(URL, USER_UNIVERSITY, USER_PASSWD);
			System.out.println("Oracle Connected.");
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println("Cannot get a connection: " + ex.getLocalizedMessage());
			System.err.println("Cannot get a connection: " + ex.getMessage());
			System.exit(1);
		}
		
		/*
		 * ------------------- SERVICE -------------------
		 */

		final int CUSTOMER = 1;
		final int BANK = 2;
		int select;

		System.out.println("Role을 선택하세요.");
		System.out.println("1: 고객, 2: 은행");
		Scanner input = new Scanner(System.in);

		int role = input.nextInt();
		System.out.println(role);

		if (role == CUSTOMER) {
			System.out.println("원하는 서비스를 선택하세요.");
			System.out.println("1: 거래 하기(insert), 2: 개인정보 변경하기(update)");
			select = input.nextInt();
			// 1: 필요한 데이터 넣어서 transaction table에 insert
			// 2: 원래값을, 바꾸고싶은 값 받기 --> update

			doCustomerQueries(conn, stmt, select);

		} else if (role == BANK) {
			System.out.println("원하는 서비스를 선택하세요.");
			System.out.println("Phase2 select 쿼리들");
			select = input.nextInt();
			
			doBankQueries(conn, stmt, select);

		} else
			System.out.println("프로그램을 다시 실행한 후, 1,2 중에서 선택해주세요");
		
		/*------------------------------
		 * 		DB disconnect
		 * ----------------------------*/		
		
		// Release database resources.
		try {
			// Close the Statement object.
			//stmt.close(); 
			// Close the Connection object.
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void doCustomerQueries(Connection conn, Statement stmt, int select) {
		String sql = ""; // an SQL statement
		String source = "";
		
		try {
			conn.setAutoCommit(true);
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // auto-commit disabled 자동 커밋여부(default는 true)
		
		
		// Create a statement object
		ResultSet rs = null;
		
		try {
			// Q1: Complete your query.
			String sql1 = "";
			rs = stmt.executeQuery(sql1);
			System.out.println("<< query 1 result >>");

			while(rs.next()) {
				// Fill out your code
				String gender = rs.getString(1);
				Float  avg_sal = rs.getFloat(2);
			}
			
			System.out.println();
			
			// Q2: Complete your query.
			sql1 = "C";
			rs=stmt.executeQuery(sql1);
			System.out.println("<< query 2 result >>");

			while(rs.next()) {
				// Fill out your code
				String ssn = rs.getString(1);
				String lname = rs.getString(2);
				String fname = rs.getString(3);
				Float  salary = rs.getFloat(4);
			}
					
			System.out.println();
			
			// Q3: Complete your query.
			sql1 = "";		
			rs = stmt.executeQuery(sql1);
			System.out.println("<< query 3 result >>");

			while(rs.next()) {
				// Fill out your code	
				String dname = rs.getString(1);
				String pname = rs.getString(2);
				String lname = rs.getString(3);
				String  fname = rs.getString(4);
			}
			
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void doBankQueries(Connection conn, Statement stmt, int select) {
		try {
			conn.setAutoCommit(true);
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // auto-commit disabled 자동 커밋여부(default는 true)
		// Create a statement object
		ResultSet rs = null;
		
		try {
			// Q1: Complete your query.
			String sql = "l";
			rs = stmt.executeQuery(sql);
			System.out.println("<< query 1 result >>");
			while(rs.next()) {
				// Fill out your code
				String gender = rs.getString(1);
				Float  avg_sal = rs.getFloat(2);
			}
			
			System.out.println();
			
			// Q2: Complete your query.
			sql = "";
			rs=stmt.executeQuery(sql);
			System.out.println("<< query 2 result >>");
			while(rs.next()) {
				// Fill out your code
				String ssn = rs.getString(1);
				String lname = rs.getString(2);
				String fname = rs.getString(3);
				Float  salary = rs.getFloat(4);
			}
					
			System.out.println();
			
			// Q3: Complete your query.
			sql = "";		
			rs = stmt.executeQuery(sql);
			System.out.println("<< query 3 result >>");

			while(rs.next()) {
				// Fill out your code	
				String dname = rs.getString(1);
				String pname = rs.getString(2);
				String lname = rs.getString(3);
				String  fname = rs.getString(4);
			}
			
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
