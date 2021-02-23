1.
CREATE TABLE PROJECTS(
  PROJECT_ID INTEGER GENERATED ALWAYS AS IDENTITY,
  PROJECT_NAME VARCHAR(200),
  DESCRIPTION VARCHAR(1000),
  START_DATE DATE DEFAULT CURRENT_DATE,
  END_DATE DATE,
  BUDGET NUMERIC(10,2)
);
2.
INSERT INTO PROJECTS (project_name, description, start_date,end_date, budget) VALUES ('New Technologies Survey','A project aimed at reviewing the area
of advanced database technologies.', TO_DATE('01 01 2018', 'DD MM YYYY'), default, 1500000);

INSERT INTO PROJECTS (project_name, description, start_date,end_date, budget) VALUES ('Advanced Data Analysis','Analyzing data obtained from various
organizations.', TO_DATE('20 09 2017', 'DD MM YYYY'), TO_DATE('01 10 2018','DD MM YYYY'), 2750000);

SELECT
    *
FROM
    PROJECTS

3.
INSERT INTO PROJECTS (project_id, project_name, description, start_date,end_date, budget) VALUES (55,'Creating backbone network','Expanding the organization''s network
infrastructure.', TO_DATE('06 01 2019', 'DD MM YYYY'), TO_DATE('31 05 2020','DD MM YYYY'),3000000);
--Error

4.
INSERT INTO PROJECTS (project_name, description, start_date,end_date, budget) VALUES ('Creating backbone network','Expanding the organization''s network
infrastructure.', TO_DATE('06 01 2019', 'DD MM YYYY'), TO_DATE('31 05 2020','DD MM YYYY'),5000000);

SELECT
    PROJECT_ID,
    PROJECT_NAME
FROM
    PROJECTS
5.
--Error

6.
CREATE TABLE PROJECTS_COPY
AS
SELECT
*
FROM
PROJECTS

SELECT
*
FROM
PROJECTS_COPY

7.
INSERT INTO PROJECTS_copy (project_id, project_name, description, start_date,end_date, budget) VALUES (100, 'Creating mobile network', 'Expanding the organization''s network
infrastructure – part 2.', TO_DATE('01 06 2020', 'DD MM YYYY'), TO_DATE('31 05 2021','DD MM YYYY'), 4000000);
--Only type of field was coppied
8.
DELETE FROM PROJECTS_COPY
WHERE
    PROJECT_NAME = 'Creating backbone network'
--Deletion failed

9.
ALTER TABLE PROJECTS ADD NUMBER_OF_EMP NUMERIC(3) MODIFY
    DESCRIPTION VARCHAR(1500)
10.

SELECT
    MAX(LENGTH(PROJECT_NAME))
FROM
    PROJECTS
	
ALTER TABLE projects
MODIFY project_name varchar(20)

--Error 

11.
ALTER TABLE PROJECTS RENAME COLUMN BUDGET TO PROJECT_BUDGET

12.
DROP TABLE PROJECTS_COPY