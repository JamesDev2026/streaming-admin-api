-- Expand Description column size to VARCHAR(2000)
-- Table: Account
-- Database: StreamingAccountsDB

USE StreamingAccountsDB;
GO

ALTER TABLE dbo.Account
    ALTER COLUMN Description VARCHAR(2000) NULL;
GO
