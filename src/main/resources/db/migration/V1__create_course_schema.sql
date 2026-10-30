CREATE TABLE departments (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL,

    CONSTRAINT pk_departments PRIMARY KEY (id),
    CONSTRAINT uk_departments_code UNIQUE (code)
);

CREATE TABLE courses (
    id BIGINT NOT NULL,
    code VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    department_id BIGINT NOT NULL,

    CONSTRAINT pk_courses PRIMARY KEY (id),
    CONSTRAINT uk_courses_code UNIQUE (code),
    CONSTRAINT fk_courses_department
        FOREIGN KEY (department_id)
        REFERENCES departments (id)
);