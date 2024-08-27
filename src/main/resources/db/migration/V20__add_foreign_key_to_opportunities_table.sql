ALTER TABLE opportunities
ADD COLUMN assigned_to INT,
ADD FOREIGN KEY (assigned_to) REFERENCES sales_representatives(id);
