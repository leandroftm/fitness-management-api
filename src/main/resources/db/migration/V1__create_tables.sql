CREATE TABLE students
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name      VARCHAR(255) NOT NULL,
    email          VARCHAR(255) NOT NULL,
    phone_number   VARCHAR(15)  NOT NULL,
    gender         VARCHAR(20)  NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    created_at     TIMESTAMP    NOT NULL,
    updated_at     TIMESTAMP    NOT NULL,
    deactivated_at TIMESTAMP NULL,

    CONSTRAINT uk_student_email UNIQUE (email)
);

CREATE TABLE address
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    zip_code   VARCHAR(10)  NOT NULL,
    street     VARCHAR(255) NOT NULL,
    number     VARCHAR(10)  NOT NULL,
    complement VARCHAR(50),
    city       VARCHAR(50)  NOT NULL,
    state      VARCHAR(50)  NOT NULL
);

ALTER TABLE students
    ADD COLUMN address_id BIGINT NOT NULL;

ALTER TABLE students
    ADD CONSTRAINT fk_student_address
        FOREIGN KEY (address_id) REFERENCES address (id);

CREATE TABLE exercises
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    name           VARCHAR(100) NOT NULL,
    description    VARCHAR(500) NOT NULL,
    video_url      VARCHAR(255) NOT NULL,
    muscle_group   VARCHAR(20)  NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    created_at     TIMESTAMP    NOT NULL,
    updated_at     TIMESTAMP    NOT NULL,
    deactivated_at TIMESTAMP NULL,

    CONSTRAINT uk_exercise_name UNIQUE (name)
);

CREATE TABLE training_programs
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    name           VARCHAR(100) NOT NULL,
    description    VARCHAR(500) NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    created_at     TIMESTAMP    NOT NULL,
    updated_at     TIMESTAMP    NOT NULL,
    deactivated_at TIMESTAMP NULL
);

CREATE TABLE training_program_exercises
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    training_program_id BIGINT    NOT NULL,
    exercise_id         BIGINT    NOT NULL,
    exercise_order      INT       NOT NULL,
    created_at          TIMESTAMP NOT NULL,

    CONSTRAINT fk_tpe_program
        FOREIGN KEY (training_program_id)
            REFERENCES training_programs (id),

    CONSTRAINT fk_tpe_exercise
        FOREIGN KEY (exercise_id)
            REFERENCES exercises (id),

    CONSTRAINT uk_program_exercise UNIQUE (training_program_id, exercise_id),
    CONSTRAINT uk_program_order UNIQUE (training_program_id, exercise_order)

);