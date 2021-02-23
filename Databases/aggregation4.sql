1. 
SELECT  MIN(SALARY)  AS min_salary, MAX(salary) as max_salary, MAX(SALARY) - MIN(SALARY) as  difference FROM employees
--
SELECT  MIN(SALARY)  AS min_salary, MAX(salary) as max_salary, max_salary-min_salary as  difference FROM employees
-- I thought this one would  work, but it doesn't
2.
SELECT COUNT(*) AS employee FROM employees WHERE dept_id IS NULL
3.
SELECT COUNT(*) AS professors FROM employees WHERE job = 'PROFESSOR'
4.
SELECT job, ROUND(AVG(salary),2) AS average_salary FROM employees GROUP BY job ORDER BY average_salary DESC
-- How to make irrelevant zeros not to disappear?
5.
SELECT job, ROUND(AVG(salary+NVL(add_salary, 0)),2) AS average_salary, count(*) AS number_of_employees 
FROM employees 
GROUP BY job
ORDER BY average_salary DESC
6.
SELECT job, ROUND(AVG(salary+NVL(add_salary, 0)),2) AS average_salary, count(*) AS number_of_employees 
FROM employees 
GROUP BY job 
HAVING count(*)  > 1
ORDER BY average_salary DESC
7.
SELECT
    DEPT_ID,
    COUNT(EMP_ID) as number_of_employees
FROM
    EMPLOYEES
WHERE
    ADD_SALARY IS NOT NULL
    AND DEPT_ID IS NOT NULL
GROUP BY
    DEPT_ID
ORDER BY
    DEPT_ID
8.
SELECT
    DEPT_ID,
    COUNT(EMP_ID) as number_of_employees,
    AVG(ADD_SALARY)  as average_of_salaries,
    SUM(ADD_SALARY) as sum_of_add_salaries
FROM
    EMPLOYEES
WHERE
    ADD_SALARY IS NOT NULL
    AND DEPT_ID IS NOT NULL
GROUP BY
    DEPT_ID
ORDER BY
    DEPT_ID
9.
SELECT
    boss_id,
	count(emp_id)
FROM
    EMPLOYEES
WHERE
	emp_id is NOT  NULL
	AND
	boss_id  is NOT  NuLL
GROUP BY
    boss_id
ORDER BY
    boss_id
10.
SELECT
    EXTRACT(YEAR from hire_date) as hire_year,
	count(emp_id)
FROM
    EMPLOYEES
GROUP BY
    EXTRACT(YEAR from hire_date)
ORDER BY
    hire_year
11.
SELECT
    EXTRACT(YEAR from hire_date) as hire_year,
	count(emp_id)
FROM
    EMPLOYEES
GROUP BY
    EXTRACT(YEAR from hire_date)
ORDER BY
    hire_year
12.
SELECT
    count(emp_id) as surnames_with_a
FROM employees
WHERE REGEXP_LIKE(surname, '[aA]') 

SELECT
    count(emp_id) as surnames_with_e
FROM employees
WHERE REGEXP_LIKE(surname, '[eE]');
-- alternativelly
SELECT
    COUNT(SURNAME)
FROM
    EMPLOYEES
WHERE
    SURNAME LIKE '%A%'
    OR SURNAME LIKE '%a%'
13.
SELECT
    COUNT(
        CASE
        WHEN REGEXP_LIKE(surname, '[aA]') THEN
        1
        ELSE
        NULL
        END
    )  AS COUNT_A,
    COUNT(
        CASE
        WHEN REGEXP_LIKE(surname, '[eE]') THEN
        1
        ELSE
        NULL
        END
    )  AS COUNT_E
FROM
    EMPLOYEES
-- is there a simpler way to do  this?




