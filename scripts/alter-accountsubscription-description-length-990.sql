-- Expand Description column size to VARCHAR(990)
-- Table: AccountSubscription
-- Database: StreamingAccountsDB

USE StreamingAccountsDB;
GO

ALTER TABLE dbo.AccountSubscription
    ALTER COLUMN Description VARCHAR(990) NULL;
GO
