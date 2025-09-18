-- DDL para la base de datos de GoodTricount en PostgreSQL

-- Se recomienda crear la base de datos manualmente o con un script de shell
-- y luego conectarse a ella para ejecutar este script.
-- Ejemplo de comando para la terminal:
-- CREATE DATABASE goodtricount;
-- \c goodtricount;

--
-- Estructura de la tabla Users
--
CREATE TABLE IF NOT EXISTS Users (
username VARCHAR(50) PRIMARY KEY,
password VARCHAR(255) NOT NULL,
email VARCHAR(100) NOT NULL UNIQUE,
name VARCHAR(100)
);

--
-- Estructura de la tabla Groups
--
CREATE TABLE IF NOT EXISTS Groups (
id VARCHAR(50) PRIMARY KEY,
name VARCHAR(100) NOT NULL
);

--
-- Estructura de la tabla GroupParticipants
--
CREATE TABLE IF NOT EXISTS GroupParticipants (
group_id VARCHAR(50) NOT NULL,
user_id VARCHAR(50) NOT NULL,
PRIMARY KEY (group_id, user_id),
FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES Users(username) ON DELETE CASCADE
);

--
-- Estructura de la tabla GroupAdmins
--
CREATE TABLE IF NOT EXISTS GroupAdmins (
group_id VARCHAR(50) NOT NULL,
admin_id VARCHAR(50) NOT NULL,
PRIMARY KEY (group_id, admin_id),
FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE,
FOREIGN KEY (admin_id) REFERENCES Users(username) ON DELETE CASCADE
);

--
-- Estructura de la tabla Expenses
--
CREATE TABLE IF NOT EXISTS Expenses (
expense_id SERIAL PRIMARY KEY,
group_id VARCHAR(50) NOT NULL,
payer VARCHAR(50) NOT NULL,
amount NUMERIC(10, 2) NOT NULL,
description VARCHAR(255),
FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE,
FOREIGN KEY (payer) REFERENCES Users(username)
);

--
-- Estructura de la tabla Payments
--
CREATE TABLE IF NOT EXISTS Payments (
payment_id SERIAL PRIMARY KEY,
group_id VARCHAR(50) NOT NULL,
payer VARCHAR(50) NOT NULL,
receiver VARCHAR(50) NOT NULL,
amount NUMERIC(10, 2) NOT NULL,
status VARCHAR(10) NOT NULL CHECK (status IN ('pending', 'confirmed')),
timestamp TIMESTAMP NOT NULL,
confirmedTimestamp TIMESTAMP,
FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE,
FOREIGN KEY (payer) REFERENCES Users(username),
FOREIGN KEY (receiver) REFERENCES Users(username)
);