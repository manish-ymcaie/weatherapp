-- Create the table to store weather request history
CREATE TABLE weather_request (
    id SERIAL PRIMARY KEY, -- Auto-incrementing primary key
    postal_code VARCHAR(50) NOT NULL, -- US postal codes are 5 digits
    user_name VARCHAR(100) NOT NULL, -- User making the request
    weather_description VARCHAR(255), -- Description of the weather (e.g., "clear sky")
    temperature NUMERIC(5, 2), -- Temperature in Celsius
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Request timestamp
);

-- Indexes to improve search performance for postal code and user
CREATE INDEX idx_postal_code ON weather_request (postal_code);
CREATE INDEX idx_user_name ON weather_request (user_name);


CREATE SEQUENCE IF NOT EXISTS weather_seq START WITH 1 INCREMENT BY 1;


CREATE TABLE users (
    id SERIAL PRIMARY KEY, 
    name VARCHAR(50) NOT NULL, 
    email VARCHAR(100), 
    active boolean
);


CREATE SEQUENCE IF NOT EXISTS user_seq START WITH 1 INCREMENT BY 1;

ALTER TABLE users ADD CONSTRAINT users_unique_key UNIQUE (name);

