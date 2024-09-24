CREATE TABLE Departments (
    ID SERIAL PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Location VARCHAR(100) NOT NULL,
    parentLacationId INTEGER REFERENCES Departments (ID) 
);

