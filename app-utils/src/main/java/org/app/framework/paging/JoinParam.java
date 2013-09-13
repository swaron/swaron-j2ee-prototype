package org.app.framework.paging;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;


/**
 * work with pagingParam to provide join tables WITHOUT Foreign Key
 * relationship.
 * 
 * @author aaron
 * 
 */
public class JoinParam<T> extends PagingParam{
	Class<T> joinClass;
	List<JoinColumn> joins;

	public JoinParam(Class<T> joinClass) {
		this.joinClass = joinClass;
	}

	public static <E> JoinParam<E> newJoinParam(Class<E> joinClass, String masterField, String referField) {
		JoinParam<E> joinParam = new JoinParam<E>(joinClass);
		List<JoinColumn>joins = new ArrayList<JoinColumn>();
		joins.add(new JoinColumn(masterField, referField));
		joinParam.setJoins(joins);
		return joinParam;
	}
	
	public static <E> JoinParam<E> newJoinParam(Class<E> joinClass, SingularAttribute<?,?> masterField, SingularAttribute<E, ?> referField) {
		JoinParam<E> joinParam = new JoinParam<E>(joinClass);
		List<JoinColumn> joins =  new ArrayList<JoinColumn>();
		joins.add(new JoinColumn(masterField.getName(), referField.getName()));
		joinParam.setJoins(joins);
		return joinParam;
	}
	
	public Class<T> getJoinClass() {
		return joinClass;
	}

	public void setJoinClass(Class<T> joinClass) {
		this.joinClass = joinClass;
	}

	public List<JoinColumn> getJoins() {
		return joins;
	}

	public void setJoins(List<JoinColumn> joins) {
		this.joins = joins;
	}

}
