1.
SELECT
    SURNAME,
    JOB
FROM
    EMPLOYEES
WHERE
    DEPT_ID = (
        SELECT
            DEPT_ID
        FROM
            EMPLOYEES
        WHERE
            SURNAME = 'Johnson'
    )
    AND SURNAME != 'Johnson'
2.
SELECT
    SURNAME,
    JOB,
    DEPT_NAME
FROM
    EMPLOYEES
    INNER JOIN DEPARTMENTS USING ( DEPT_ID )
WHERE
    DEPT_ID = (
        SELECT
            DEPT_ID
        FROM
            EMPLOYEES
        WHERE
            SURNAME = 'Johnson'
    )
    AND SURNAME != 'Johnson'
3.
SELECT
    SURNAME,
    JOB,
    HIRE_DATE
FROM
    EMPLOYEES
WHERE
    HIRE_DATE = (
        SELECT
            MIN(HIRE_DATE)
        FROM
            EMPLOYEES
    )    
4.
SELECT
    DEPT_NAME AS DEPARTMENT,
    SURNAME,
    HIRE_DATE
FROM
    DEPARTMENTS
    INNER JOIN EMPLOYEES USING ( DEPT_ID )
WHERE
    HIRE_DATE IN (
        SELECT
            MAX(HIRE_DATE)
        FROM
            DEPARTMENTS
            INNER JOIN EMPLOYEES USING ( DEPT_ID )
        GROUP BY
            DEPT_ID
    )
    
5.
SELECT
    DEPT_ID,
    DEPT_NAME,
    ADDRESS
FROM
    DEPARTMENTS
WHERE
    DEPT_ID NOT IN (
        SELECT
            DEPT_ID
        FROM
            DEPARTMENTS
            INNER JOIN EMPLOYEES USING ( DEPT_ID )
    )
6.
SELECT
    SURNAME,
    JOB,
    SALARY
FROM
    EMPLOYEES
WHERE
    JOB = 'PROFESSOR'
    AND EMP_ID NOT IN (
        SELECT
            BOSS_ID
        FROM
            EMPLOYEES
            INNER JOIN JOBS ON EMPLOYEES.JOB = JOBS.NAME
        WHERE
            JOBS.NAME = 'PHD STUDENT'
    )
7.
SELECT
    DEPT_NAME,
    COUNT(EMP_ID)
FROM
    DEPARTMENTS
    INNER JOIN EMPLOYEES ON EMPLOYEES.DEPT_ID = DEPARTMENTS.DEPT_ID
GROUP BY
    DEPARTMENTS.DEPT_NAME
HAVING
    COUNT(EMP_ID) > (
        SELECT
            COUNT(EMP_ID)
        FROM
            DEPARTMENTS
            INNER JOIN EMPLOYEES ON EMPLOYEES.DEPT_ID = DEPARTMENTS.DEPT_ID
        WHERE
            DEPT_NAME = 'ADMINISTRATION'
        GROUP BY
            DEPARTMENTS.DEPT_NAME
    )
8.
SELECT
    EXTRACT(YEAR FROM HIRE_DATE)     AS HIRE_YEAR,
    COUNT(EMP_ID)                    AS EMP_COUNT
FROM
    EMPLOYEES
WHERE
    JOB LIKE 'PROFESSOR'
GROUP BY
    EXTRACT(YEAR FROM HIRE_DATE)
HAVING
    COUNT(EMP_ID) = (
        SELECT
            MAX(EMP_COUNT)
        FROM
            (
                SELECT
                    COUNT(E.EMP_ID) AS EMP_COUNT
                FROM
                    EMPLOYEES E
                WHERE
                    E.JOB LIKE 'PROFESSOR'
                GROUP BY
                    EXTRACT(YEAR FROM E.HIRE_DATE)
                ORDER BY
                    COUNT(E.EMP_ID) DESC
            )
    )