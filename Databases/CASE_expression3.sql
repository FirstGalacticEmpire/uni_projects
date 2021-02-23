1. SELECT surname, salary, CASE
  WHEN salary < 1500 THEN 'low paid'
    WHEN salary > 3000 then 'well paid'
      ELSE 'average paid' END AS label
FROM employees ORDER BY surname
-- What's the difference between ' and "? I'm not entierly sure
2. SELECT distinct boss_id FROM employees WHERE boss_id is not NULL ORDER BY boss_id
3. SELECT DISTINCT dept_id,  job FROM EMPLOYEES WHERE dept_id is not NULL ORDER BY dept_id
4. SELECT DISTINCT EXTRACT(YEAR FROM HIRE_DATE) as years FROM EMPLOYEES ORDER BY years
5. SELECT
    DEPT_ID
FROM
    DEPARTMENTS 
MINUS
SELECT DISTINCT
    DEPT_ID
FROM
    EMPLOYEES
WHERE
    DEPT_ID IS NOT NULL
6. 
SELECT
    SURNAME,
    SALARY,
    'low salary' AS LABEL
FROM
    EMPLOYEES
WHERE
    SALARY < 1500
UNION
SELECT
    SURNAME,
    SALARY,
    'average salary' AS LABEL
FROM
    EMPLOYEES
WHERE
    SALARY > 1500 AND SALARY <=3000
UNION
SELECT
    SURNAME,
    SALARY,
    'well paid' AS LABEL
FROM
    EMPLOYEES
WHERE
    SALARY > 3000
ORDER BY surname;
      