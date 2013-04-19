package org.app.framework.paging;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
	List<SimpleEntry<String,String>> sort = new ArrayList<SimpleEntry<String,String>>();
	List<SimpleEntry<String,String>> filter = new ArrayList<SimpleEntry<String,String>>();
	
	public List<SimpleEntry<String, String>> getSort() {
		return sort;
	}

	protected void setSort(List<SimpleEntry<String, String>> sort) {
		this.sort = sort;
	}

	public List<SimpleEntry<String, String>> getFilter() {
		return filter;
	}

	protected void setFilter(List<SimpleEntry<String, String>> filter) {
		this.filter = filter;
	}


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

	public void addFilter(String key,String value){
		SimpleEntry<String, String> e = new SimpleEntry<String, String>(key, value);
		filter.add(e);
	}
	public void addSort(String key,String value){
		SimpleEntry<String, String> e = new SimpleEntry<String, String>(key, value);
		sort.add(e);
	}
}
