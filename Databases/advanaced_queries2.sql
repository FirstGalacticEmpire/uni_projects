Dear Sir You can find my questions in the comments between solutions.

1. SELECT surname, emp_id, UPPER(SUBSTR(surname,1,2) || emp_id) as login FROM EMPLOYEES ORDER BY surname
2. SELECT surname FROM employees WHERE REGEXP_LIKE(surname, '[lL]') ORDER  BY surname
--Czy mogę to  zrobić za pomocą REGEXA? Alternatywnie:
SELECT SURNAME FROM EMPLOYEES WHERE INSTR(SURNAME, 'L') > 0 OR INSTR(SURNAME, 'l') > 0 
3. SELECT surname FROM employees WHERE REGEXP_LIKE(SUBSTR(surname, 1, LENGTH(surname)/2), '[lL]') ORDER  BY surname
4. SELECT surname, salary AS "original_salary", ROUND(salary*1.15,0) AS "increased_salary" FROM employees ORDER  BY surname 
5. SELECT TO_CHAR(CURRENT_DATE, 'DAY') AS "today  is"
FROM EMPLOYEES
WHERE ROWNUM = 1
--How to print name of the day, without a call to table "EMPLOYEES"?
6.
SELECT TO_CHAR(DATE '2000-10-06', 'DAY') AS TODAY
FROM EMPLOYEES
WHERE ROWNUM = 1
7. SELECT surname, TO_CHAR(hire_date, 'DD MONTH YYYY, DAY') as hire_date  FROM EMPLOYEES
8.	
SELECT SURNAME,
    JOB,
    (TO_DATE('01.01.2000', 'DD.MM.YYYY') - HIRE_DATE) YEAR TO MONTH AS experience_in_2000
FROM EMPLOYEES
WHERE JOB = 'PROFESSOR'
    OR JOB = 'LECTURER'
    OR JOB = 'ASSISTANT'
ORDER BY experience_in_2000 DESC,
    SURNAME
9.
SELECT SURNAME,
    JOB,
    (TO_DATE('01.01.2000', 'DD.MM.YYYY') - HIRE_DATE) YEAR TO MONTH AS experience
FROM EMPLOYEES
WHERE (JOB = 'PROFESSOR'
    OR JOB = 'LECTURER'
    OR JOB = 'ASSISTANT')
AND EXTRACT(YEAR
        FROM (TO_DATE('01.01.2000', 'DD.MM.YYYY') - HIRE_DATE) YEAR TO MONTH) > 10
ORDER BY experience DESC,
    SURNAME
	
-- Why isn't  this working?'

SELECT SURNAME,
    JOB,
    (TO_DATE('01.01.2000', 'DD.MM.YYYY') - HIRE_DATE) YEAR TO MONTH AS experience
FROM EMPLOYEES
WHERE (JOB = 'PROFESSOR'
    OR JOB = 'LECTURER'
    OR JOB = 'ASSISTANT')
AND EXTRACT(YEAR FROM experience) > 10
ORDER BY experience DESC,
    SURNAME

10.
SELECT SURNAME,
    JOB,
    EXTRACT(
        YEAR
        FROM (TO_DATE('01.01.2000', 'DD.MM.YYYY') - HIRE_DATE) YEAR TO MONTH
    ) AS experience
FROM EMPLOYEES
WHERE (
        JOB = 'PROFESSOR'
        OR JOB = 'LECTURER'
        OR JOB = 'ASSISTANT'
    )
    AND EXTRACT(
        YEAR
        FROM (TO_DATE('01.01.2000', 'DD.MM.YYYY') - HIRE_DATE) YEAR TO MONTH
    ) > 10
ORDER BY experience DESC,
    SURNAME

