/**********************************
 * 		Phase 3 - JDBC
 *		Team 8 
 ***********************************/

import java.sql.*;
import java.sql.Date;
import java.util.*;


public class JDBC {
	/*
	 * ------------------- DB 연결하기 -------------------
	 */
	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_DB8 = "db8";
	public static final String USER_PASSWD = "db8";

	public static void main(String[] args) {
		Connection conn = null; // Connection object
		Statement stmt = null; // Statement object

		try {
			// Load a JDBC driver for Oracle DBMS 드라이버 가져오기
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// Get a Connection object 
			System.out.println("Driver Loading: Success!");
		}catch(ClassNotFoundException e) {
			System.err.println("error = " + e.getMessage());
			System.exit(1);
		}

		// Make a connection 연결하기(conn)변수에
		try{
			conn = DriverManager.getConnection(URL, USER_DB8, USER_PASSWD); 
			System.out.println("Oracle Connected.");
		}catch(SQLException ex) {
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
		boolean repeat = true;

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
			while(repeat) {
				System.out.println("원하는 서비스를 선택하세요.");
				System.out.println("1: 거래방법별로 거래보기  2.특정 거래금액 이상인 거래 ID 찾기 3: 특정 신용도 이상 국가의 은행 정보 보기 ");
				System.out.println("4: 국가 신용 점수 수정하기 5: 특정 고객의 계좌 삭제하기, 11: 서비스 종료하기");
				select = input.nextInt();

				if (select == 11)
					repeat = false;

				doBankQueries(conn, stmt, select);
			}

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
			System.out.println("database connect 종료");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void doCustomerQueries(Connection conn, Statement stmt, int select) {
		String sql = ""; // an SQL statement

		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // auto-commit disabled 자동 커밋여부(default는 true)
		
		
		// Create a statement object
		ResultSet rs = null;
		
		try {
			// 
			String sql1 = "";
			rs = stmt.executeQuery(sql1);
			System.out.println("<< query 1 result >>");

			while(rs.next()) {
				// Fill out your code
				String gender = rs.getString(1);
				Float  avg_sal = rs.getFloat(2);
			}
			
			System.out.println();
			
			// 
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
			
			// Q3
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
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // auto-commit disabled 자동 커밋여부(default는 true)
		
		Scanner option = new Scanner(System.in);
		ResultSet rs = null;
		PreparedStatement ps = null;
		String sql = "";
		int feedback;

		try {
			
			switch(select) {
			
				case 1: //1: 거래방법별로 거래보기
					System.out.println("거래방법별로 거래 보기");
					sql = "WITH MTABLE AS\n" + 
							"(SELECT DISTINCT METHOD, COUNT(METHOD) AS COUNT FROM TRANSACTION GROUP BY METHOD)\n" + 
							"SELECT TXN_ID, TXN_DATE, M.METHOD, M.COUNT FROM TRANSACTION T, MTABLE M" + 
							" WHERE T.METHOD = M.METHOD AND M.COUNT >= ALL(M.COUNT)" + 
							" ORDER BY M.COUNT, TXN_ID";
					rs = stmt.executeQuery(sql);
					
					while(rs.next()) {
						// no impedance mismatch in JDBC
						int txn_id = rs.getInt(1);
						Date txn_date = rs.getDate(2);
						String method = rs.getString(3);
						int count = rs.getInt(4);
						System.out.println("거래ID = " + txn_id 
										+  ", 거래날짜 = " + txn_date 
										+  ", Method = " + method
										+  ", Method count = " + count);
					}
					conn.commit();
					break;
			
				case 2: //2.특정 거래금액 이상인 거래 ID 찾기 
					int amount;
					System.out.println("특정 거래금액 이상인 거래 ID찾기");
					System.out.print("거래 금액을 입력하세요: ");
					amount = option.nextInt();
					
					sql="SELECT TXN_ID, AMOUNT FROM TRANSACTION WHERE AMOUNT > ?";
					ps = conn.prepareStatement(sql); 
					ps.setInt(1, amount);
					
					rs = ps.executeQuery();
					
					while(rs.next()) {
						// no impedance mismatch in JDBC
						int txn_id = rs.getInt(1);
						int amount2 = rs.getInt(2);
						System.out.println("거래ID = " + txn_id
										+  ", 거래 금액 = " + amount2);
					}
					conn.commit();	
					
					int sort;
					System.out.println("정렬하시겠습니까?: 1 - 정렬안함, 2 - 거래 ID로 정렬, 3 - 금액순으로 정렬(내림차순)");
					sort=option.nextInt();
					
					switch(sort) {
						case 1: break;
						case 2:
							sql = sql + "ORDER BY TXN_ID";
							ps = conn.prepareStatement(sql); 
							ps.setInt(1, amount);
							
							rs = ps.executeQuery();
							
							while(rs.next()) {
								// no impedance mismatch in JDBC
								int txn_id = rs.getInt(1);
								int amount2 = rs.getInt(2);
								System.out.println("거래ID = " + txn_id
												+  ", 거래 금액 = " + amount2);
							}
							conn.commit();	
							break;
						case 3:
							sql = sql + "ORDER BY AMOUNT DESC";
							ps = conn.prepareStatement(sql); 
							ps.setInt(1, amount);
							
							rs = ps.executeQuery();
							
							while(rs.next()) {
								// no impedance mismatch in JDBC
								int txn_id = rs.getInt(1);
								int amount2 = rs.getInt(2);
								System.out.println("거래ID = " + txn_id
												+  ", 거래 금액 = " + amount2);
							}
							conn.commit();	
							break;
					}	
					
					break;
					
				case 3: //3: 특정 신용도 이상 국가의 은행 정보 보기 
					int score;
					System.out.println("특정 신용도 이상인 국가의 은행 정보 보기");
					System.out.print("신용도 점수를 입력하세요: ");
					score = option.nextInt();
					
					sql="SELECT BANK_ID, BRANCH_NAME, STREET_ADDRESS FROM BANK" + 
						" WHERE BANK_ID IN((SELECT BANK_ID FROM BANK)" +
						" MINUS (SELECT BANK_ID FROM COUNTRY_CREDIT WHERE CREDIT < ?))";
					ps = conn.prepareStatement(sql); 
					ps.setInt(1, score);
					
					rs = ps.executeQuery();
					
					while(rs.next()) {
						// no impedance mismatch in JDBC
						int bank_id = rs.getInt(1);
						String branch_name = rs.getString(2);
						String st_addr = rs.getString(3);
						System.out.println("은행ID = " + bank_id
										+  ", 지점명 = " + branch_name
										+ ", 주소= " + st_addr);
					}
					conn.commit();	
					
					break;
					
				case 4: //4: 국가 신용 점수 수정하기 
					String cname;
					int credit;
					System.out.println("국가 신용점수 수정하기");
					System.out.println("수정할 국가의 이름을 입력하세요: ");
					cname = option.nextLine();
					System.out.println("새로운 신용 점수를 입력하세요: ");
					credit = option.nextInt();

					sql="UPDATE COUNTRY_CREDIT SET CREDIT = ? WHERE NAME = ?";
					ps = conn.prepareStatement(sql); 
					ps.setInt(1, credit);
					ps.setString(2, cname);
					
					feedback = ps.executeUpdate();
					if(feedback==0) System.out.println("No Updated.");
					else System.out.println("Update Success");
						
					System.out.println("결과 확인: ");
					sql = "SELECT * FROM COUNTRY_CREDIT WHERE NAME = ?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, cname);
					rs = ps.executeQuery();
							
					while(rs.next()) {
						// no impedance mismatch in JDBC
						String name = rs.getString(1);
						String ccode = rs.getString(2);
						int credit2 = rs.getInt(3);
						int bank_id = rs.getInt(4);
						System.out.println("국가명 = " + name
										+  ", 국가코드 = " + ccode
										+ ", 신용점수= " + credit2
										+ ", 은행 ID= " + bank_id);
					}
					conn.commit();	
					
					break;
					
				case 5: //5: 특정 고객의 계좌 삭제하기
					String hname;
					String account;
					System.out.println("특정 고객의 계좌 삭제하기");
					System.out.println("계좌를 삭제할 고객의 이름을 입력하세요: ");
					hname = option.nextLine();
					System.out.println("삭제할 계좌번호를 입력하세요: ");
					account = option.nextLine();

					sql="DELETE FROM ACCOUNT WHERE H_ID IN (SELECT H_ID FROM HOLDER WHERE NAME = ?) AND ACCOUNT_NUMBER = ?";
					ps = conn.prepareStatement(sql); 
					ps.setString(1, hname);
					ps.setString(2, account);
					
					feedback = ps.executeUpdate();
					if(feedback==0) System.out.println("No Deleted.");
					else System.out.println("Delete Success");
					
					conn.commit();
					break;
					
				case 6:
					
					break;
					
				case 7:
					
					break;
					
				case 8:
					
					break;
					
				case 9:
					
					break;
					
				case 10:
					
					break;
					
			}
			
			switch(select) {
				case 1: case 2: case 3: case 4: rs.close();
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
