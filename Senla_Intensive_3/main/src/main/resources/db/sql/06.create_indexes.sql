CREATE INDEX idx_employees_department ON Employees(DepartmentID);
CREATE INDEX idx_employees_position ON Employees(PositionID);
CREATE INDEX idx_project_participation_employee ON ProjectParticipation(EmployeeID);
CREATE INDEX idx_project_participation_project ON ProjectParticipation(ProjectID);