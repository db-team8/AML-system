/**********************************
 * 		Phase 3 - JDBC
 *		Team 8 
 ***********************************/

import java.sql.*;
import java.sql.Date;
import java.util.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SHA256 {

    public static String encrypt(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());

        return bytesToHex(md.digest());
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}

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

        int select;
        boolean repeat = true;
        Scanner input = new Scanner(System.in);

        while(repeat) {
            System.out.println("원하는 서비스를 선택하세요.");
            System.out.println("1: 거래방법별로 거래보기  2.특정 거래금액 이상인 거래 ID 찾기 3: 특정 신용도 이상 국가의 은행 정보 보기 ");
            System.out.println("4: 국가 신용 점수 수정하기 5: 특정 고객의 계좌 삭제하기, 6: 특정 국가 신용도보다 낮은 국가 조회 ");
            System.out.println("7: 위험 계좌주 신상정보 조회, 8: 국가별 계좌주 평균 자산 조회, 9: 기준 위험 점수보다 높은 거래 조회 " );
            System.out.println("10: 위험인물 거래 조회, 11: 위험계좌 거래 조회, 12: 위험 거래 심사 상태 변경 " );
            System.out.println("13: 기간별 개인정보를 제외한 거래 정보 조회, 14: 특정 금액 이상 거래한 고객 정보 조회" );
            System.out.println("15: 다수의 계좌를 갖고 있는 고객이 특정 국가와 거래한 거래 정보 조회, 16: 고객 정보 추가 및 계좌 생성" );
            System.out.println("17: 서비스 종료하기 " );
            select = input.nextInt();

            if (select == 17)
                repeat = false;

            doBankQueries(conn, stmt, select);
        }

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
                    System.out.print("계좌를 삭제할 고객의 이름을 입력하세요: ");
                    hname = option.nextLine();
                    System.out.print("삭제할 계좌번호를 입력하세요: ");
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

                case 6: //6: 특정 국가 신용도보다 낮은 국가 조회
                    int creditScore;
                    System.out.println("특정 신용도보다 낮은 국가 조회");
                    System.out.print("기준 신용도를 입력하세요: ");
                    creditScore = option.nextInt();

                    sql="SELECT NAME, CREDIT FROM COUNTRY_CREDIT WHERE CREDIT < ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, creditScore);

                    rs = ps.executeQuery();

                    while(rs.next()) {
                        // no impedance mismatch in JDBC
                        String countryName = rs.getString(1);
                        int Score = rs.getInt(2);
                        System.out.println("국가명 = " + countryName
                                +  ", 신용도 = " + Score);
                    }

                    sort=0;
                    System.out.println("정렬하시겠습니까?: 1 - 정렬안함, 2 - 신용도순으로 정렬(오름차순), 3 - 신용도순으로 정렬(내림차순)");
                    sort=option.nextInt();

                    switch(sort) {
                        case 1:
                            break;
                        case 2:
                            sql = sql + "ORDER BY CREDIT ASC";
                            ps = conn.prepareStatement(sql);
                            ps.setInt(1, creditScore);
                            rs = ps.executeQuery();
                            while(rs.next()) {
                                // no impedance mismatch in JDBC
                                String countryName = rs.getString(1);
                                int Score = rs.getInt(2);
                                System.out.println("국가명 = " + countryName
                                        +  ", 신용도 = " + Score);
                            }
                            break;
                        case 3:
                            sql = sql + "ORDER BY CREDIT DESC";
                            ps = conn.prepareStatement(sql);
                            ps.setInt(1, creditScore);
                            rs = ps.executeQuery();
                            while(rs.next()) {
                                // no impedance mismatch in JDBC
                                String countryName = rs.getString(1);
                                int Score = rs.getInt(2);
                                System.out.println("국가명 = " + countryName
                                        +  ", 신용도 = " + Score);
                            }
                            break;
                    }
                    break;

                case 7: //7: 위험 계좌주 신상정보 조회
                    System.out.println("위험 계좌주 신상정보 조회");
                    sql="SELECT H.H_ID, H.NAME, H.SEX, H.ADDRESS, H.NATIONALITY, H.PHONE_NUMBER, D.SCORE\n" +
                            "FROM HOLDER H, DNG_HOLDER D\n" +
                            "WHERE H.H_ID = D.H_ID";
                    rs = stmt.executeQuery(sql);
                    while(rs.next()) {
                        // no impedance mismatch in JDBC
                        String h_id = rs.getString(1);
                        String name = rs.getString(2);
                        String sex = rs.getString(3);
                        String address = rs.getString(4);
                        String nationality = rs.getString(5);
                        String phone_number = rs.getString(6);
                        int dngScore = rs.getInt(7);

                        System.out.println("ID = " + h_id + ", 이름 = " + name + ", 성별 = " + sex +
                                ", 주소 = " + address + ", 국적 = " + nationality + ", 연락처 = " + phone_number +
                                ", 위험도 = " + dngScore);
                    }
                    break;

                case 8: //8:국가별 계좌주 평균 자산 조회
                    System.out.println("국가별 계좌주 평균 자산 조회");
                    sql="SELECT NATIONALITY, ROUND(AVG(BALANCE), 2) AS AVG_BAL\n" +
                            "FROM HOLDER H, ACCOUNT A\n" +
                            "WHERE H.H_ID = A.H_ID\n" +
                            "GROUP BY NATIONALITY";
                    rs = stmt.executeQuery(sql);
                    while(rs.next()) {
                        // no impedance mismatch in JDBC
                        String nationality = rs.getString(1);
                        String balance = rs.getString(2);
                        System.out.println("국가 = " + nationality + ", 평균 자산 = " + balance);
                    }
                    break;

                case 9: //9:기준 위험 점수보다 높은 거래 조회
                    System.out.println("지정 위험 점수보다 높은 거래 조회");
                    System.out.println("처리상태: 0: On Judging(심사 중), 1: Permitted(거래허가됨),\n" +
                            "2: Rejected(거래거부됨), 3: Reported(금융당국에 보고됨)");
                    System.out.print("기준 위험 점수를 입력하세요(0 입력시 모든 거래 조회):");
                    int standardScore = option.nextInt();
                    sql="SELECT * FROM DNG_TXN WHERE SCORE > ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, standardScore);
                    rs = ps.executeQuery();
                    while(rs.next()) {
                        // no impedance mismatch in JDBC
                        String txn_id = rs.getString(1);
                        String dngScore = rs.getString(2);
                        String reason = rs.getString(3);
                        String status = rs.getString(4);
                        System.out.println("ID = " + txn_id + ", 위험도 = " + dngScore +
                                ", 사유 = " + reason + ", 처리상태 = " + status);
                    }
                    break;

                case 10: //10:위험인물 거래 조회
                    System.out.println("위험인물 거래 조회");
                    System.out.println("송금 -- 0: 출금, 1: 입금");
                    sql="SELECT * FROM TRANSACTION\n" +
                            "WHERE EXISTS ( SELECT * FROM DNG_PERS WHERE NAME=CNTR_NAME )";
                    rs = stmt.executeQuery(sql);
                    while(rs.next()) {
                        // no impedance mismatch in JDBC
                        int txn_id = rs.getInt(1);
                        Date txn_date = rs.getDate(2);
                        String method = rs.getString(3);
                        int transfer_direction = rs.getInt(4);
                        int txn_amount = rs.getInt(5);
                        String cntr_name = rs.getString(6);
                        String cntr_ctry = rs.getString(7);
                        String cntr_acc_no = rs.getString(8);
                        String account_number = rs.getString(9);
                        System.out.println("ID = " + txn_id + ", 날짜 = " + txn_date + ", 방식 = " + method +
                                ", 입출금 = " + transfer_direction + ", 액수 = " + txn_amount + "\n 상대이름 = " + cntr_name +
                                ", 상대국가 = " + cntr_ctry + ", 상대계좌번호 = " + cntr_acc_no + ", 계좌번호 = " + account_number);

                    }
                    break;

                case 11: //11:위험계좌 거래 조회
                    System.out.println("위험계좌 거래 조회");
                    sql="SELECT * FROM TRANSACTION WHERE (CNTR_ACC_NO) IN (SELECT ACCT_NO FROM DNG_ACCT)";
                    rs = stmt.executeQuery(sql);
                    while(rs.next()) {
                        // no impedance mismatch in JDBC
                        int txn_id = rs.getInt(1);
                        Date txn_date = rs.getDate(2);
                        String method = rs.getString(3);
                        int transfer_direction = rs.getInt(4);
                        int txn_amount = rs.getInt(5);
                        String cntr_name = rs.getString(6);
                        String cntr_ctry = rs.getString(7);
                        String cntr_acc_no = rs.getString(8);
                        String account_number = rs.getString(9);
                        System.out.println("ID = " + txn_id + ", 날짜 = " + txn_date + ", 방식 = " + method +
                                ", 입출금 = " + transfer_direction + ", 액수 = " + txn_amount + "\n 상대이름 = " + cntr_name +
                                ", 상대국가 = " + cntr_ctry + ", 상대계좌번호 = " + cntr_acc_no + ", 계좌번호 = " + account_number);
                    }
                    break;

                case 12: //12:위험 거래 심사 상태 변경
                    System.out.println("위험 거래 심사 상태 변경");
                    System.out.print("변경할 거래 ID를 입력하세요:");
                    int changeID = option.nextInt();
                    System.out.println("\n거래 내역");
                    sql="SELECT * FROM TRANSACTION WHERE TXN_ID = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, changeID);
                    rs = ps.executeQuery();
                    while(rs.next()) {
                        // no impedance mismatch in JDBC
                        int txn_id = rs.getInt(1);
                        Date txn_date = rs.getDate(2);
                        String method = rs.getString(3);
                        int transfer_direction = rs.getInt(4);
                        int txn_amount = rs.getInt(5);
                        String cntr_name = rs.getString(6);
                        String cntr_ctry = rs.getString(7);
                        String cntr_acc_no = rs.getString(8);
                        String account_number = rs.getString(9);
                        System.out.println("ID = " + txn_id + ", 날짜 = " + txn_date + ", 방식 = " + method +
                                ", 입출금 = " + transfer_direction + ", 액수 = " + txn_amount + "\n 상대이름 = " + cntr_name +
                                ", 상대국가 = " + cntr_ctry + ", 상대계좌번호 = " + cntr_acc_no + ", 계좌번호 = " + account_number);
                    }
                    System.out.println();
                    System.out.println("심사 상태");
                    sql="SELECT * FROM DNG_TXN WHERE TXN_ID = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, changeID);
                    rs = ps.executeQuery();
                    while(rs.next()) {
                        // no impedance mismatch in JDBC
                        String txn_id = rs.getString(1);
                        String dngScore = rs.getString(2);
                        String reason = rs.getString(3);
                        String status = rs.getString(4);
                        System.out.println("ID = " + txn_id + ", 위험도 = " + dngScore +
                                ", 사유 = " + reason + ", 처리상태 = " + status);
                    }
                    System.out.println();
                    System.out.println("변경할 위험 거래 상태를 입력하세요.");
                    System.out.print("1: Permitted(거래허가), 2: Rejected(거래거부), 3: Reported(금융당국 보고):");
                    int changeStatus = option.nextInt();
                    sql="UPDATE DNG_TXN SET STATUS = ? WHERE TXN_ID = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, changeStatus);
                    ps.setInt(2, changeID);
                    feedback = ps.executeUpdate();
                    if(feedback==0) System.out.println("No Updated.");
                    else System.out.println("Update Success");
                    System.out.println();
                    System.out.println("결과 확인");
                    sql="SELECT * FROM DNG_TXN WHERE TXN_ID = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, changeID);
                    rs = ps.executeQuery();
                    while(rs.next()) {
                        // no impedance mismatch in JDBC
                        String txn_id = rs.getString(1);
                        String dngScore = rs.getString(2);
                        String reason = rs.getString(3);
                        String status = rs.getString(4);
                        System.out.println("ID = " + txn_id + ", 위험도 = " + dngScore +
                                ", 사유 = " + reason + ", 처리상태 = " + status);
                    }
                    conn.commit();
                    System.out.println();
                    break;
                case 13: // DY - TYPE1
                    Scanner scan = new Scanner(System.in);
                    System.out.println("시작 날짜를 yyyy-mm-dd 형태로 입력해주세요: ");
                    String startDate = scan.nextLine();

                    System.out.println("끝 날짜를 yyyy-mm-dd 형태로 입력해주세요: ");
                    String endDate = scan.nextLine();

                    sql = "SELECT T.TXN_DATE, T.AMOUNT, CNTR_CTRY FROM TRANSACTION T " +
                            "WHERE T.TXN_DATE BETWEEN ? AND ?";

                    ps = conn.prepareStatement(sql);
                    ps.setDate(1, Date.valueOf(startDate));
                    ps.setDate(2, Date.valueOf(endDate));

                    rs = ps.executeQuery();

                    while (rs.next()) {
                        Date transDate = rs.getDate(1);
                        int transAmount = rs.getInt(2);
                        String counterCountry = rs.getString(3);

                        String s = "거래 날짜: %s, 거래 금액: %d, 거래 상대 국가: %s";
                        System.out.println(String.format(s, transDate.toString(), transAmount, counterCountry));
                    }

                    rs.close();
                    ps.close();
                    break;
                case 14: // DY - TYPE2
                    scan = new Scanner(System.in);
                    System.out.println("거래 금익을 입력하세요: ");
                    int amnt = Integer.parseInt(scan.nextLine());

                    sql = "SELECT H.H_ID, H.Name, T.TXN_DATE, T.AMOUNT " +
                            "FROM INITIATION I, HOLDER H, TRANSACTION T " +
                            "WHERE I.H_ID=H.H_id AND I.TXN_ID=T.TXN_ID AND T.AMOUNT >= ?";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, amnt);

                    rs = ps.executeQuery();

                    while (rs.next()) {
                        int holderID = rs.getInt(1);
                        String holderName = rs.getString(2);
                        Date transDate = rs.getDate(3);
                        int transAmount = rs.getInt(4);

                        String s = "고객 ID: %d, 고객 이름: %s, 거래 날짜: %s, 거래 금액: %d";
                        System.out.println(String.format(s, holderID, holderName, transDate.toString(), transAmount));
                    }

                    rs.close();
                    ps.close();
                    break;
                case 15: // DY - TYPE 7
                    scan = new Scanner(System.in);
                    System.out.println("몇 개의 계좌를 갖고 있는 고객에 대해 조회하시겠습니까?: ");
                    int accCnt = Integer.parseInt(scan.nextLine());

                    System.out.println("거래 상대의 국가를 입력해주세요: ");
                    String countryCode = scan.nextLine();
                    sql = "WITH TEMP_H AS (" +
                            "SELECT A.H_ID FROM ACCOUNT A GROUP BY A.H_ID HAVING COUNT(*) >= ? ) " +
                            "SELECT H.Name, T.TXN_DATE, T.METHOD, T.AMOUNT FROM INITIATION I" +
                            " JOIN TEMP_H TH ON I.H_ID=TH.H_ID JOIN TRANSACTION T ON I.TXN_ID = T.TXN_ID " +
                            "JOIN HOLDER H ON H.H_id=TH.H_ID " +
                            "WHERE T.CNTR_CTRY = ?";

                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, accCnt);
                    ps.setString(2, countryCode);

                    rs = ps.executeQuery();

                    while (rs.next()) {
                        String holderName = rs.getString(1);
                        Date transDate = rs.getDate(2);
                        String transMethod = rs.getString(3);
                        int transAmnt = rs.getInt(4);

                        String s = "고객 이름: %s, 거래 날짜: %s, 거래 방식: %s, 거래 긂액: %d";
                        System.out.println(String.format(s, holderName, transDate, transMethod, transAmnt));
                    }

                    rs.close();
                    ps.close();
                    break;
                case 16:
                    scan = new Scanner(System.in);
                    System.out.println("고객의 이름을 입력해주세요: ");
                    String cName = scan.nextLine();

                    String cGender;
                    do {
                        System.out.println("고객의 성별을 선택해주세요: 1. 남, 2. 여");
                        cGender = scan.nextLine();
                    } while (!cGender.equals("1") && !cGender.equals("2"));
                    if (cGender.equals("1")) {
                        cGender = "Male";
                    } else {
                        cGender = "Female";
                    }

                    System.out.println("고객의 주소를 입력해주세요: ");
                    String cAddress = scan.nextLine();

                    System.out.println("고객의 국적의 국가 코드를 입력해주세요: ");
                    String cNationality = scan.nextLine();

                    String cPhoneNum;
                    String phoneNumRegex = "[0-9]{3}-[0-9]{3}-[0-9]{4}";
                    do {
                        System.out.println("고객의 연락처를 XXX-XXX-XXXX 형태로 입력해주세요:  ");
                        cPhoneNum = scan.nextLine();
                    } while (!cPhoneNum.matches(phoneNumRegex));

                    System.out.println("생성할 고객 계좌의 비밀번호를 설정해주세요: ");
                    String cPaasword = scan.nextLine();

//                    고객 정보 생성
                    sql = "Insert into HOLDER (H_ID, NAME, SEX, ADDRESS, NATIONALITY, PHONE_NUMBER) " +
                            "values ((select max(H_ID)+1 from HOLDER), ?, ?, ?, ?, ?)";

                    ps = conn.prepareStatement(sql);
                    ps.setString(1, cName);
                    ps.setString(2, cGender);
                    ps.setString(3, cAddress);
                    ps.setString(4, cNationality);
                    ps.setString(5, cPhoneNum);

                    int res1 = ps.executeUpdate();

//                    고객 계좌 생성
                    Random random = new Random();
                    String[] accntPrefixes = {"011", "052", "057", "101"};
                    String cAccntNum = accntPrefixes[random.nextInt(accntPrefixes.length)];

                    for (int i=0; i<9; i++) {
                        int n = random.nextInt(10);
                        cAccntNum += n + "";
                    }
                    String encryptedPassword = "";

                    try {
                        encryptedPassword = SHA256.encrypt(cPaasword);
                    } catch (NoSuchAlgorithmException e) {
                        System.out.println("error digesting password.");
                        System.exit(1);
                    }

                    sql = "INSERT INTO ACCOUNT VALUES (?, ?, ?, (select max(H_ID) from HOLDER))";

                    ps = conn.prepareStatement(sql);
                    ps.setString(1, cAccntNum);
                    ps.setInt(2, 0);
                    ps.setString(3, encryptedPassword);

                    int res2 = ps.executeUpdate();

                    if (res1 == 1 && res2 == 1) {
                        conn.commit();
                        System.out.println("성공적으로 고객 정보와 계좌를 생성하였습니다.");
                    } else {
                        System.out.println("고객 정보와 계좌 생성에 실패하였습니다.");
                    }

                    ps.close();
                    break;
                case 17:
                    break;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
