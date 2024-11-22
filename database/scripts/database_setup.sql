CREATE TABLE finance_records(
    databaseId INT AUTO_INCREMENT PRIMARY KEY,
    recordId CHAR(36) NOT NULL,
    type VARCHAR(30) NOT NULL,
    category VARCHAR(30) NOT NULL,
    description TEXT,
    amount DECIMAL(18, 2) NOT NULL,
    date DATE NOT NULL
);

INSERT INTO finance_records(recordId, type, category, description, amount, date) VALUES
    ("58e7526e-e36e-416e-b164-b3673493be2e", "EXPENSE", "FOOD", "A transaction", 1.00, '2024-09-12'),
    ("9838d7a0-feaf-48e0-b42d-17fd3dc9fdcf", "EXPENSE", "GROCERY", "A transaction", 2.50, '2024-09-11'),
    ("f73a61eb-7c52-4aeb-891f-212c16c55047", "EXPENSE", "BILLS_AND_SUBSCRIPTIONS", "A transaction", 5.00, '2024-09-10'),
    ("a6d1b188-b9c6-4324-92c5-f1e29054359c", "EXPENSE", "RENT", "A transaction", 6.50, '2024-09-09'),
    ("dd7d4f23-2c80-463d-a9fe-f942683d1f09", "INCOME", "PAYCHECK", "A transaction", 8.00, '2024-09-08'),
    ("1ea60090-d4d2-4f13-9341-b60d9603c421", "INCOME", "OTHER", "A transaction", 9.50, '2024-09-07');