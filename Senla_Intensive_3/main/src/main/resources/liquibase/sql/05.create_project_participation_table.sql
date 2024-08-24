CREATE TABLE ProjectParticipation (
    EmployeeID INT REFERENCES Employees(ID),
    ProjectID INT REFERENCES Projects(ID),
    Role VARCHAR(100),
    StartDate DATE NOT NULL,
    EndDate DATE,
    PRIMARY KEY (EmployeeID, ProjectID)
);