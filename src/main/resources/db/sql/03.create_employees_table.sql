CREATE TABLE Employees (
    ID SERIAL PRIMARY KEY,
    FullName VARCHAR(100) NOT NULL,
    BirthDate DATE NOT NULL,
    PhoneNumber VARCHAR(20),
    Email VARCHAR(100),
    PositionID INTEGER REFERENCES Positions(ID),
    DepartmentID INTEGER REFERENCES Departments(ID)
);


