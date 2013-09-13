package org.app.framework.extjs;

import java.util.List;

import org.app.framework.paging.PagingResult;

/**
 * used for extjs json reader meta support.
 * 
 * <pre>
 * {
 *     "count": 1,
 *     "ok": true,
 *     "msg": "Users found",
 *     "records": [{
 *         "userId": 123,
 *         "name": "Ed Spencer",
 *         "email": "ed@sencha.com"
 *     }],
 *     "metaData": {
 *         "root": "users",
 *         "idProperty": 'userId',
 *         "totalProperty": 'count',
 *         "successProperty": 'ok',
 *         "messageProperty": 'msg',
 *         "fields": [
 *             { "name": "userId", "type": "int" },
 *             { "name": "name", "type": "string" },
 *             { "name": "birthday", "type": "date", "dateFormat": "Y-j-m" },
 *         ],
 *         "columns": [
 *             { "text": "User ID", "dataIndex": "userId", "width": 40 },
 *             { "text": "User Name", "dataIndex": "name", "flex": 1 },
 *             { "text": "Birthday", "dataIndex": "birthday", "flex": 1, "format": 'Y-j-m', "xtype": "datecolumn" }
 *         ]
 *     }
 * 	}
 * </pre>
 * 
 * @author aaron
 * 
 */
public class MetaPagingResult<T> extends PagingResult<T> {
	GridMetaData metaData;

	public MetaPagingResult() {
	}

	public MetaPagingResult(List<T> records, int total) {
		this.records = records;
		this.total = total;
	}

	public MetaPagingResult(GridMetaData metaData, List<T> records, int total) {
		this.metaData = metaData;
		this.records = records;
		this.total = total;
	}

	public void setMetaData(GridMetaData metaData) {
		this.metaData = metaData;
	}

	public GridMetaData getMetaData() {
		return metaData;
	}

	public static <T> MetaPagingResult<T> fromList(GridMetaData metaData, List<T> records, int total) {
		return new MetaPagingResult<T>(metaData, records, total);
	}
}
