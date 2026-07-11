-- Expand Description column size to VARCHAR(990)
-- Tables: Category, AccountType, Panel
-- Database: StreamingAccountsDB

USE StreamingAccountsDB;
GO

ALTER TABLE dbo.Category
    ALTER COLUMN Description VARCHAR(990) NULL;
GO

ALTER TABLE dbo.AccountType
    ALTER COLUMN Description VARCHAR(990) NULL;
GO

ALTER TABLE dbo.Panel
    ALTER COLUMN Description VARCHAR(990) NULL;
GO
