package org.app.framework.paging;

import java.util.ArrayList;
import java.util.List;


public class PagingResult<T> {
    List<T> records;
    int total;

    public PagingResult(List<T> records, int total) {
        super();
        this.records = records;
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static <T> PagingResult<T> fromSingleResult(T element) {
        List<T> records = new ArrayList<T>(1);
        records.add(element);
        PagingResult<T> result = new PagingResult<T>(records, 1);
        return result;
    }

}
