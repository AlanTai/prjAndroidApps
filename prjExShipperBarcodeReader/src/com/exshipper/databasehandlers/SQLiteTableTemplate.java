package com.exshipper.databasehandlers;

public abstract class SQLiteTableTemplate {
	protected String tableName = null;
	protected String commandOfCreatingTable = null;
	
	public String getTableName(){
		return tableName;
	}
	public String getCommandOfCreatingTable(){
		return commandOfCreatingTable;
	}

}
