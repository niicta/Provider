CREATE TABLE PROV_USERS (ID INTEGER NOT NULL,NAME VARCHAR(20) NOT NULL,SURNAME VARCHAR(20) NOT NULL,TYPE VARCHAR(20) NOT NULL,BALANCE VARCHAR(20),PRIMARY KEY (ID));

CREATE TABLE  PROV_INSTANCES (ID INTEGER NOT NULL,TEMPLATE VARCHAR(20) NOT NULL,CUSTOMER_ID INTEGER NOT NULL,CONNECTION_DATE DATE NOT NULL,INSTANCE_STATUS VARCHAR(20) NOT NULL,SUSPENDION_DATE DATE,RESUME_DATE DATE,DELTA INTEGER NOT NULL,PRIMARY KEY (ID),FOREIGN KEY (CUSTOMER_ID) REFERENCES PROV_USERS(ID));

CREATE TABLE  PROV_ORDERS (ID INTEGER NOT NULL,CUSTOMER_ID INTEGER NOT NULL,EMPLOYEE_ID INTEGER,TEMPLATE VARCHAR(20),INSTANCE_ID INTEGER,COMPLETE_DATE DATE,REQUEST_DATE DATE NOT NULL,ORDER_STATUS VARCHAR(20) NOT NULL,ORDER_ACTION VARCHAR(20) NOT NULL,PRIMARY KEY (ID),FOREIGN KEY (EMPLOYEE_ID) REFERENCES PROV_USERS(ID),FOREIGN KEY (CUSTOMER_ID) REFERENCES PROV_USERS(ID),FOREIGN KEY (INSTANCE_ID) REFERENCES PROV_INSTANCES(ID));
