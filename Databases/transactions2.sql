2.
SELECT sys_context('USERENV', 'SID') FROM dual;
3.
UPDATE employees
SET salary=salary+100
WHERE surname = 'Wood';

SELECT salary FROM employees WHERE surname='Wood';
SELECT * FROM table(SBD.LOCKS);

4.

SELECT SALARY FROM EMPLOYEES WHERE surname='Wood';
--No change
UPDATE employees
SET add_salary=50
WHERE surname='Wood'
--The tool is freezed.

5.
SELECT * FROM table(SBD.LOCKS);
--the is_blocking field changed from 0 to 1 in both rows
SELECT * FROM table(SBD.LOCKS(30));
--B is waiting for Exclusive_lock(X) on the row
6.
ROLLBACK
--Sesion B finishes
--Lock held by B:
--ROW EXCLUSIVE (RX) EMPLOYEES 0
--EXCLUSIVE (X)	row	0
7. 
ROLLBACK


1.
--A
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
SELECT SALARY FROM EMPLOYEES WHERE surname='Edwards'
--1390
2.
--B
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
SELECT SALARY FROM EMPLOYEES WHERE surname='Edwards'
--1390

UPDATE EMPLOYEES 
SET SALARY = 1890
WHERE surname='Edwards'

COMMIT
3.
--A
UPDATE EMPLOYEES 
SET SALARY = 1390
WHERE surname='Edwards'
COMMIT
4.
--Lost update; salary:1390; correct amount: 1690
5.
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
--Last updated failed to execute, becuase command B introduced not-serializable changes.

1.
--A
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
2.
--B
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
3.
--A
UPDATE employees
SET salary = (SELECT salary
FROM employees
WHERE surname = 'Jones')
WHERE surname = 'Williams';
4.
--B
UPDATE employees
SET salary = (SELECT salary
FROM employees
WHERE surname = 'Williams')
WHERE surname = 'Jones';
5.
--Johnes: 3070, Williams: 3350 values were swaped. 

1.
--A
UPDATE EMPLOYEES 
SET SALARY = SALARY+10
WHERE emp_id = 210;
2.
--B
UPDATE EMPLOYEES 
SET SALARY = SALARY+10
WHERE emp_id = 220;
3.
--A
UPDATE EMPLOYEES 
SET SALARY = SALARY+20
WHERE emp_id = 220;
4.
--B
UPDATE EMPLOYEES 
SET SALARY = SALARY+20
WHERE emp_id = 210;
--Deadlock detected.
5.
ROLLBACK
COMMIT
6.
--We just need to add third transaction, while performing last two:

--C
UPDATE EMPLOYEES 
SET SALARY = SALARY+10
WHERE emp_id = 210;
--C
UPDATE EMPLOYEES 
SET SALARY = SALARY+30
WHERE emp_id = 230;

--B
ROLLBACK
--A
ROLLBACK