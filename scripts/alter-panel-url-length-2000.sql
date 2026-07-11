-- Expand url column size to VARCHAR(2000)
-- Table: Panel
-- Database: StreamingAccountsDB

USE StreamingAccountsDB;
GO

ALTER TABLE dbo.Panel
    ALTER COLUMN url VARCHAR(2000) NULL;
GO
