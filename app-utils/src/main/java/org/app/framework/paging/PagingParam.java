package org.app.framework.paging;

import java.util.LinkedHashMap;

/**
 * here is the contract for paging. if the limit and start is null, then no
 * paging is specified, and all rows should be return.
 * 
 * @author aaron
 * 
 */
public class PagingParam {
	Integer limit;
	Integer start;
	LinkedHashMap<String, String> sort;
	LinkedHashMap<String, String> filter;

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public LinkedHashMap<String, String> getSort() {
		return sort;
	}

	public void setSort(LinkedHashMap<String, String> sort) {
		this.sort = sort;
	}

	public LinkedHashMap<String, String> getFilter() {
		return filter;
	}

	public void setFilter(LinkedHashMap<String, String> filter) {
		this.filter = filter;
	}

}
