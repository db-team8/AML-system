실행 방법:
Team8-Phase3.zip의 압축을 풀고 프로젝트 전체를 eclipse 상에서 프로젝트 열기를 합니다.
JDBC.java를 eclipse 상에서 실행합니다.

실행 주의 사항:
기존 Phase 2의 Team8-Phase-2-1.sql와 Team8-Phase-2-2.sql에 대해 데이터가 DB상에 추가되어있어야합니다.
JDBC.java를 실행하기전에 eclipse 상에서 ojdbc8.jar를 라이브러리로 추가해 드라이버를 불러와야합니다.
DB가 가동되어있는 상태에서 JDBC.java를 실행하여야합니다.

JDBC.java 파일안에 아래 내용을 실행하는 환경에 맞게 바꿔주어야 합니다.

public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl"; // orcl 또는 xe
public static final String USER_DB8 = "db8"; // DB 유저명
public static final String USER_PASSWD = "db8"; // DB 패스워드명


기능 설명:
고객 또는 은행 두 가지 Role 중 하나를 선택합니다.
고객을 선택한 경우 고객에 접근 권한이 주어지는 기능들을 선택할 수 있으며 '거래 하기' 기능과, '개인정보 변경하기' 기능이 있습니다.
은행을 선택한 경우 고객은 접근 할 수 없고, 은행만이 접근 할 수 있는 기능들을 선택할 수 있습니다.
기능들은 다음과 같습니다. (옆에 괄호로 Phase2의 어떤 쿼리를 사용했는지 표시해뒀습니다.)
1: 거래방법별로 거래보기
2: 특정 거래금액 이상인 거래 ID 찾기
3: 특정 신용도 이상 국가의 은행 정보 보기 
4: 국가 신용 점수 수정하기
5: 특정 고객의 계좌 삭제하기
6: 특정 국가 신용도보다 낮은 국가 조회
7: 위험 계좌주 신상정보 조회,
8: 국가별 계좌주 평균 자산 조회
9: 기준 위험 점수보다 높은 거래 조회 
10: 위험인물 거래 조회
11: 위험계좌 거래 조회
12: 위험 거래 심사 상태 변경
13: 기간별 개인정보를 제외한 거래 정보 조회 (DY - TYPE1)
14: 특정 금액 이상 거래한 고객 정보 조회 (DY - TYPE2)
15: 다수의 계좌를 갖고 있는 고객이 특정 국가와 거래한 거래 정보 조회 (DY - TYPE 7)
16: 고객 정보 추가 및 계좌 생성
17: 서비스 종료하기

Application 제작 환경:
IntelliJ 버전: 2022.2.3 빌드: 222.4345.14
Arm64 MacBook 21.6.0 Darwin Kernel Version 21.6.0
colima version 0.4.6
Docker version 20.10.17
gvenzl/oracle-xe
Oracle Database 21c Express Edition Release 21.0.0.0.0 - Production
Version 21.3.0.0.0
ORACLE SQL Developer 22.2.1
