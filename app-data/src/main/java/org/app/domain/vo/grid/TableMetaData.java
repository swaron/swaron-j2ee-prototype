package org.app.domain.vo.grid;

public class TableMetaData {
    private String tableCat;
    private String tableSchem;
    private String tableName;
    private String selfReferencingColName;
    private String tableType;
    private String remarks;

    private String primaryKeyCol;
    
    public String getPrimaryKeyCol() {
        return primaryKeyCol;
    }

    public void setPrimaryKeyCol(String primaryKeyCol) {
        this.primaryKeyCol = primaryKeyCol;
    }

    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(String tableCat) {
        this.tableCat = tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSelfReferencingColName() {
        return selfReferencingColName;
    }

    public void setSelfReferencingColName(String selfReferencingColName) {
        this.selfReferencingColName = selfReferencingColName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
