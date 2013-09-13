package org.app.framework.paging;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

public class OrFilter extends Filter {

	List<Filter> filters = new ArrayList<Filter>();

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public void addFilter(String key, Object value) {
		filters.add(new Filter(key, value));
	}

	public void addFilter(SingularAttribute<?, ?> attr, Object value) {
		filters.add(new Filter(attr.getName(), value));
	}

	public void addFilter(String operator, String key, Object value) {
		Filter f = new Filter(key, value);
		f.setOperator(operator);
		filters.add(f);
	}

	public void addFilter(String operator, SingularAttribute<?, ?> attr, Object value) {
		Filter f = new Filter(attr.getName(), value);
		f.setOperator(operator);
		filters.add(f);
	}
}
