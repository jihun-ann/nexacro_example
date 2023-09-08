CREATE TABLE BOARD
(
  NUM        NUMBER(22,7)                       NOT NULL,
  WRITER     VARCHAR2(12 BYTE)                  NOT NULL,
  EMAIL      VARCHAR2(30 BYTE)                  NOT NULL,
  SUBJECT    VARCHAR2(50 BYTE)                  NOT NULL,
  PASS       VARCHAR2(10 BYTE)                  NOT NULL,
  READCOUNT  NUMBER(22,5)                       DEFAULT 0                     NOT NULL,
  REF        NUMBER(22,5)                       DEFAULT 0                     NOT NULL,
  STEP       NUMBER(22,3)                       DEFAULT 0                     NOT NULL,
  DEPTH      NUMBER(22,3)                       DEFAULT 0                     NOT NULL,
  REGDATE    DATE                               DEFAULT SYSDATE               NOT NULL,
  CONTENT    VARCHAR2(4000 BYTE)                NOT NULL,
  IP         VARCHAR2(20 BYTE)                  NOT NULL
);



CREATE SEQUENCE board_seq
START WITH 1
INCREMENT BY 1
NOMAXVALUE
NOCACHE
NOCYCLE;