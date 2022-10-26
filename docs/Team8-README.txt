SQL 동작 환경:
Arm64 MacBook 21.6.0 Darwin Kernel Version 21.6.0
colima version 0.4.6
Docker version 20.10.17
gvenzl/oracle-xe
Oracle Database 21c Express Edition Release 21.0.0.0.0 - Production
Version 21.3.0.0.0
ORACLE SQL Developer 22.2.1


실행 방법:
Team8-Phase2-1.sql (DDL), Team8-Phase2-2.sql (INSERT문)
두 개의 파일을 SQL Developer에서 각각 열어 순차적으로 스크립트를 실행합니다.
Team8-Phase2-3.sql (QUERY문)을 이용하여 쿼리 결과를 조회합니다.


실행 주의 사항:
DNG_TXN 테이블에 INSERT를 진행하기전
반드시 SET DEFINE OFF; 문장을 먼저 실행하여야 합니다.
(데이터 내 &기호를 변수가 아닌 데이터로 사용하기 위함)

추가될 사항:
TRANSACTION에 데이터가 들어올 때, 데이터 검사 후 위험거래임이 판별되면 DNG_TXN에 삽입되도록 TRIGGER를 구성하였습니다.
버그 수정이 필요해 아직 반영은 안되었지만 참고하시면 좋겠습니다.
/* TXN_ALERT TRIGGER-----------------------

SET DEFINE OFF;
CREATE OR REPLACE TRIGGER TXN_ALERT
AFTER INSERT ON TRANSACTION
FOR EACH ROW
--WHEN(NEW.AMOUNT > 1000000 OR NEW.ACCOUNT_NUMBER OR NEW.CNTR_NAME IN DNG_PERS.NAME)
DECLARE NAME_ALERT VARCHAR2(50);
ACCOUNT_ALERT VARCHAR2(20);
SCORE NUMBER(3);
REASON VARCHAR2(50);
STATUS CHAR(1);

BEGIN
-- create dual table for name_alert, account_alert
SELECT(SELECT CNTR_NAME FROM TRANSACTION WHERE EXISTS(SELECT CNTR_NAME FROM TRANSACTION, DNG_PERS WHERE CNTR_NAME = NAME))
INTO NAME_ALERT FROM DUAL;

SELECT(SELECT ACCOUNT_NUMBER FROM TRANSACTION WHERE EXISTS(SELECT ACCOUNT_NUMBER FROM TRANSACTION T, DNG_ACCT D WHERE T.ACCOUNT_NUMBER = D.ACCT_NO))
INTO ACCOUNT_ALERT FROM DUAL;

STATUS := '0'; --judging...

-- insert into dng_txn
IF(:NEW.AMOUNT>1000000 AND (NAME_ALERT IS NOT NULL) AND (ACCOUNT_ALERT IS NOT NULL)) THEN
REASON := 'Suspicious person & account & transfer too much money.';
SCORE := 999;
INSERT INTO DNG_TXN (TXN_ID, SCORE, REASON, STATUS) VALUES
(:NEW.TXN_ID, SCORE, REASON, STATUS);
dbms_output.put_line('New Alert of Transaction occured.: '||:New.TXN_ID);
dbms_output.put_line('REASON: '||REASON);

ELSIF(:NEW.AMOUNT>1000000 AND (NAME_ALERT IS NOT NULL)) THEN
REASON := 'Suspicious person & transfer too much money.';
SCORE := 750;
INSERT INTO DNG_TXN (TXN_ID, SCORE, REASON, STATUS) VALUES
(:NEW.TXN_ID, SCORE, REASON, STATUS);
dbms_output.put_line('New Alert of Transaction occured.: '||:New.TXN_ID);
dbms_output.put_line('REASON: '||REASON);

ELSIF(:NEW.AMOUNT>1000000 AND (ACCOUNT_ALERT IS NOT NULL)) THEN
REASON := 'Suspicious account & transfer too much money.';
SCORE := 750;
INSERT INTO DNG_TXN (TXN_ID, SCORE, REASON, STATUS) VALUES
(:NEW.TXN_ID, SCORE, REASON, STATUS);
dbms_output.put_line('New Alert of Transaction occured.: '||:New.TXN_ID);
dbms_output.put_line('REASON: '||REASON);

ELSIF(NAME_ALERT AND ACCOUNT_ALERT) THEN
REASON := 'Suspicious person & account.';
SCORE := 800;
INSERT INTO DNG_TXN (TXN_ID, SCORE, REASON, STATUS) VALUES
(:NEW.TXN_ID, SCORE, REASON, STATUS);
dbms_output.put_line('New Alert of Transaction occured.: '||:New.TXN_ID);
dbms_output.put_line('REASON: '||REASON);

ELSIF(NAME_ALERT IS NOT NULL) THEN
REASON := 'Suspicious person.';
SCORE := 600;
INSERT INTO DNG_TXN (TXN_ID, SCORE, REASON, STATUS) VALUES
(:NEW.TXN_ID, SCORE, REASON, STATUS);
dbms_output.put_line('New Alert of Transaction occured.: '||:New.TXN_ID);
dbms_output.put_line('REASON: '||REASON);

ELSIF(ACCOUNT_ALERT IS NOT NULL) THEN
REASON := 'Suspicious account.';
SCORE := 600;
INSERT INTO DNG_TXN (TXN_ID, SCORE, REASON, STATUS) VALUES
(:NEW.TXN_ID, SCORE, REASON, STATUS);
dbms_output.put_line('New Alert of Transaction occured.: '||:New.TXN_ID);
dbms_output.put_line('REASON: '||REASON);

ELSIF(:NEW.AMOUNT>1000000) THEN
REASON := 'transfer too much money.';
SCORE := 500;
INSERT INTO DNG_TXN (TXN_ID, SCORE, REASON, STATUS) VALUES
(:NEW.TXN_ID, SCORE, REASON, STATUS);
dbms_output.put_line('New Alert of Transaction occured.: '||:New.TXN_ID);
dbms_output.put_line('REASON: '||REASON);

ELSE dbms_output.put_line('Transaction successd.');
END IF;

END TXN_ALERT;

-------------------*/
