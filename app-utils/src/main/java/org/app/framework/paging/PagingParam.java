package org.app.framework.paging;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

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
	List<Sorter> sort = new ArrayList<Sorter>();
	List<Filter> filter = new ArrayList<Filter>();
	JoinParam<?> joinParam;

	public JoinParam<?> getJoinParam() {
		return joinParam;
	}

	public void setJoinParam(JoinParam<?> joinParam) {
		this.joinParam = joinParam;
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

	public List<Sorter> getSort() {
		return sort;
	}

	public List<Filter> getFilter() {
		return filter;
	}

	public void addFilter(String key, Object value) {
		filter.add(new Filter(key, value));
	}
	
	public void addFilter(SingularAttribute<?, ?> attr, Object value) {
		filter.add(new Filter(attr.getName(), value));
	}
	
	public void addFilter(String operator,String key, Object value) {
		Filter f = new Filter(key, value);
		f.setOperator(operator);
		filter.add(f);
	}
	
	public void addFilter(String operator,SingularAttribute<?, ?> attr, Object value) {
		Filter f = new Filter(attr.getName(), value);
		f.setOperator(operator);
		filter.add(f);
	}
	
	public void addExactMatchFilter(String key, Object value) {
		filter.add(new Filter(key, value, Filter.EXACT_MATCH));
	}
	
	public void addExactMatchFilter(SingularAttribute<?, ?> attr, Object value) {
		filter.add(new Filter(attr.getName(), value, Filter.EXACT_MATCH));
	}

	public void addAnyMatchFilter(String key, Object value) {
		filter.add(new Filter(key, value, Filter.ANY_MATCH));
	}
	
	public void addAnyMatchFilter(SingularAttribute<?, ?> attr, Object value) {
		filter.add(new Filter(attr.getName(), value, Filter.ANY_MATCH));
	}
	
	public void addNullFilter(String key) {
		Filter f = new Filter();
		f.setOperator(Filter.OPERATOR_NULL);
		f.setProperty(key);
		filter.add(f);
	}
	
	public void addNotNullFilter(String key) {
		Filter f = new Filter();
		f.setOperator(Filter.OPERATOR_NOT_NULL);
		f.setProperty(key);
		filter.add(f);
	}

	public void addFilter(OrFilter orFilter) {
		filter.add(orFilter);
	}
	
	public void addSort(String key, String value) {
		sort.add(new Sorter(key, value));
	}
	
	public void addSort(SingularAttribute<?, ?> attr, String value) {
		sort.add(new Sorter(attr.getName(), value));
	}

	public boolean hasFilter(String key) {
		for (Filter f : filter) {
			if(f.getProperty().equals(key)){
				return true;
			}
		}
		return false;
	}

}
