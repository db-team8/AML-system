(1) 실행 방법:
Team8-Phase3.zip의 압축을 풀고 프로젝트 전체를 eclipse 상에서 프로젝트 열기를 합니다.
JDBC.java를 eclipse 상에서 실행합니다.

(2) 실행 주의 사항:
기존 Phase 2의 Team8-Phase-2-1.sql와 Team8-Phase-2-2.sql에 대해 데이터가 DB상에 추가되어있어야합니다.
JDBC.java를 실행하기전에 eclipse 상에서 ojdbc8.jar를 라이브러리로 추가해 드라이버를 불러와야합니다.
DB가 가동되어있는 상태에서 JDBC.java를 실행하여야합니다.

(3) JDBC.java 파일안에 아래 내용을 실행하는 환경에 맞게 바꿔주어야 합니다.

public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl"; // orcl 또는 xe
public static final String USER_DB8 = "db8"; // DB 유저명
public static final String USER_PASSWD = "db8"; // DB 패스워드명


(4) 기능 설명:
은행의 기본 업무(고객 정보 추가 및 계좌 생성)와 AML과 관련된 기능들을 수행할 수 있습니다.
기능들은 다음과 같습니다. (옆에 괄호로 Phase2의 어떤 쿼리를 사용했는지 표시해뒀습니다. 표시가 없는 것은 Phase2에 없는 insert/update/delete쿼리 입니다.)
1: 거래방법별로 거래보기(SJ - TYPE7)
2: 특정 거래금액 이상인 거래 ID 찾기(SJ - TYPE1)
3: 특정 신용도 이상인 국가의 정보 변경을 담당하는 은행 정보 보기(SJ - TYPE10)
4: 국가 신용 점수 수정하기
5: 특정 고객의 계좌 삭제하기
6: 특정 국가 신용도보다 낮은 국가 조회 (JY - TYPE1)
7: 위험 계좌주 신상정보 조회 (JY - Type 2)
8: 국가별 계좌주 평균 자산 조회 (JY - Type 3)
9: 기준 위험 점수보다 높은 거래 조회 (JY - Type 4)
10: 위험인물 거래 조회 (JY - Type 5)
11: 위험계좌 거래 조회 (JY - Type 6)
12: 위험 거래 심사 상태 변경
13: 기간별 개인정보를 제외한 거래 정보 조회 (DY - TYPE1)
14: 특정 금액 이상 거래한 고객 정보 조회 (DY - TYPE2)
15: 다수의 계좌를 갖고 있는 고객이 특정 국가와 거래한 거래 정보 조회 (DY - TYPE 7)
16: 고객 정보 추가 및 계좌 생성
17: 서비스 종료하기

Application 제작 환경:
IntelliJ 버전: 2022.2.3 빌드: 222.4345.14
Ecliple IDE for Java Developers 버전: 2019-06 (4.12.0)
Arm64 MacBook 21.6.0 Darwin Kernel Version 21.6.0
colima version 0.4.6
Docker version 20.10.17
gvenzl/oracle-xe
Oracle Database 21c Express Edition Release 21.0.0.0.0 - Production
Version 21.3.0.0.0
ORACLE SQL Developer 22.2.1


+ 추가 사항
쿼리가 변경된 것은 아니나, 컬럼값 해석에서 살짝 의미가 잘못 해석되어 기능의 이름을 정정했습니다.
(Phase2기준 SJ-TYPE10)특정 신용도 이상인 국가의 은행 정보 보기 --> 특정 신용도 이상인 국가의 정보 변경을 담당하는 은행 정보 보기
