<%@ page trimDirectiveWhitespaces="true"%>
<%@taglib tagdir="/WEB-INF/tags/layouts" prefix="layout"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="u" uri="http://app.org/tags/utils"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<layout:basic title="Admin Cache" pageClass="cache">
	<jsp:attribute name="scripts">
<script type="text/javascript">
	Ext.namespace('Page');
	Ext.apply(Page, {
		loadStats : function() {
			Ext.Ajax.request({
				url : FM.cfg.contextPath + '/admin/cache.do',
				params : {
					'type' : 'getstats'
				},
				method : 'POST',
				success : this.loadStatsHandle,
				scope : this
			});
		},
		init : function() {
			Ext.select('a.clear-ehcache').on('click',function(evt,dom,o){
				this.purgeCache(dom.getAttribute('data-cachename'));
			},this);
		},
		purgeCache: function(name){
			if(confirm('Are you sure you want to purge all the elements in cache: ' + name)){
				Ext.Ajax.request({
					url : App.cfg.contextPath + '/admin/cache/purgeEhcache.json',
					params : {
						'name' : name
					},
					method : 'POST',
					success : this.purgeResultHandle,
					scope : this
				});
			}
		},
		purgeResultHandle: function(response){
			var res = Ext.decode(response.responseText);
			if(res.result == "success"){
				location.reload(true);
			}else{
				alert('Failed to clear cache due to cache not found.');
			}
		}
	});
	Ext.onReady(Page.init,Page);
</script>
</jsp:attribute>
<jsp:attribute name="head">
	<style type="text/css">
table {
	border-collapse: collapse;
	border-spacing: 0;
	margin: 1px;
	text-align: left;
	margin-top: 20px;
}
table caption{
	font-size: large;
}
table caption a{
	float:right;
	font-size: 12px;
	padding-top: 5px;
}
td, th{
	border: 1px solid #CCC;
	padding: 2px;
}
h3{
font-size: large;
}
	</style>
</jsp:attribute>
	<jsp:body>
	<div id="jpa_cache_statistics" class="round-box">
	<h3>Web Portal JPA Cache</h3>
	
	<table> 
		<caption>JPA Session Information</caption>
		<tr class="bgBlue2">
			<th>Session Open Count</th>
			<th>Session Close Count</th>
			<th>Connect Count</th>
			<th>Flush Count</th>
			<th>Transaction Count</th>
			<th>Successful Transaction Count</th>
			<th>Optimistic Failure Count</th>
		</tr>
		<tr>
			<td><c:out value="${statistics.sessionOpenCount}" /></td>
			<td><c:out value="${statistics.sessionCloseCount}" /></td>
			<td><c:out value="${statistics.connectCount}" /></td>
			<td><c:out value="${statistics.flushCount}" /></td>
			<td><c:out value="${statistics.transactionCount}" /></td>
			<td><c:out value="${statistics.successfulTransactionCount}" /></td>
			<td><c:out value="${statistics.optimisticFailureCount}" /></td>
		</tr>
	</table>
	<table> 
		<caption>Entity Information</caption>
		<c:forEach items="${entity}" var="e">
		<tr class="bgBlue1">
			<th colspan="${fn:length(e.value) }"><c:out value="${e.key}" /></th>
		<tr>
		<tr class="bgBlue2">
			<c:forEach items="${e.value}" var="it">
			<th><c:out value="${it.key}" /></th>
			</c:forEach>
		<tr>
		<tr>
			<c:forEach items="${e.value}" var="it">
			<td><c:out value="${it.value}" /></td>
			</c:forEach>
		<tr>
		</c:forEach>
	</table>
	<table> 
		<caption>Collection Information</caption>
		<c:forEach items="${collection}" var="e">
		<tr class="bgBlue1">
			<th colspan="${fn:length(e.value) }"><c:out value="${e.key}" /></th>
		<tr>
		<tr class="bgBlue2">
			<c:forEach items="${e.value}" var="it">
			<th><c:out value="${it.key}" /></th>
			</c:forEach>
		<tr>
		<tr>
			<c:forEach items="${e.value}" var="it">
			<td><c:out value="${it.value}" /></td>
			</c:forEach>
		<tr>
		</c:forEach>
	</table>
	<table> 
		<caption>Query Information</caption>
		<c:forEach items="${query}" var="e">
		<tr class="bgBlue1">
			<th colspan="${fn:length(e.value) }"><c:out value="${e.key}" /></th>
		<tr>
		<tr class="bgBlue2">
			<c:forEach items="${e.value}" var="it">
			<th><c:out value="${it.key}" /></th>
			</c:forEach>
		<tr>
		<tr>
			<c:forEach items="${e.value}" var="it">
			<td><c:out value="${it.value}" /></td>
			</c:forEach>
		<tr>
		</c:forEach>
	</table>
	<table> 
		<caption>SecondLevelCache Information</caption>
		<c:forEach items="${secondLevelCache}" var="e">
		<tr class="bgBlue1">
			<th colspan="${fn:length(e.value) }"><c:out value="${e.key}" /></th>
		<tr>
		<tr class="bgBlue2">
			<c:forEach items="${e.value}" var="it">
			<th><c:out value="${it.key}" /></th>
			</c:forEach>
		<tr>
		<tr>
			<c:forEach items="${e.value}" var="it">
			<td><c:out value="${it.value}" /></td>
			</c:forEach>
		<tr>
		</c:forEach>
	</table>
	</div>
	<br>
	<div id="ehcache_statistics" class="round-box">
		<h3>Web Portal EhCache Information</h3>
		<c:forEach items="${ehcache}" var="e">
		<table> 
			<caption>
				<b>${e.key}</b>
				<a href="javascript:void(0);" class="clear-ehcache" data-cachename="${e.key}">Clear This Cache</a>
			</caption>
			<c:set var="it" value="${e.value}"></c:set>
			<tr>
				<td colspan="4">
					AverageGetTime <c:out value="${it.averageGetTime}"/> ,
					AverageSearchTime <c:out value="${it.averageSearchTime}"/>, 
					SearchesPerSecond <c:out value="${it.searchesPerSecond}"/>,
					EvictionCount <c:out value="${it.evictionCount}"/> 
				</td>
			</tr>
			<tr class="bgBlue2">
				<th>Object Count</th>
				<th>Memory Store Object Count</th>
				<th>Disk Store Object Count</th>
				<th>OffHeap Store Object Count</th>
			</tr>
			<tr>
				<td><c:out value="${it.objectCount}" /></td>
				<td><c:out value="${it.memoryStoreObjectCount}" /></td>
				<td><c:out value="${it.diskStoreObjectCount}" /></td>
				<td><c:out value="${it.offHeapStoreObjectCount}" /></td>
			</tr>
			<tr class="bgBlue2">
				<th>Cache Hits</th>
				<th>Memory Hits</th>
				<th>Disk Hits</th>
				<th>OffHeap Hits</th>
			</tr>
			<tr>
				<td><c:out value="${it.cacheHits}" /></td>
				<td><c:out value="${it.inMemoryHits}" /></td>
				<td><c:out value="${it.onDiskHits}" /></td>
				<td><c:out value="${it.offHeapHits}" /></td>
			</tr>
			<tr class="bgBlue2">
				<th>Cache Misses</th>
				<th>Memory Misses</th>
				<th>Disk Misses</th>
				<th>OffHeap Misses</th>
			</tr>
			<tr>
				<td><c:out value="${it.cacheMisses}" /></td>
				<td><c:out value="${it.inMemoryMisses}" /></td>
				<td><c:out value="${it.onDiskMisses}" /></td>
				<td><c:out value="${it.offHeapMisses}" /></td>
			</tr>
		</table>
		</c:forEach>
	</div>
</jsp:body>
</layout:basic>
