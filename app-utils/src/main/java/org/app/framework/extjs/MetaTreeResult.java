package org.app.framework.extjs;

import java.util.List;

import org.app.framework.web.tree.TreeNode;
import org.app.framework.web.tree.TreeResult;

/**
 * used for extjs json reader meta support.
 * 
 * <pre>
 * {
 *     "count": 1,
 *     "ok": true,
 *     "msg": "Users found",
 *     "children": [{
 *         "userId": 123,
 *         "name": "Ed Spencer",
 *         "email": "ed@sencha.com"
 *     }],
 *     "metaData": {
 *         "root": "children",
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
public class MetaTreeResult<T> extends TreeResult<T>{
	GridMetaData metaData;

	public void setMetaData(GridMetaData metaData) {
		metaData.setRoot("children");
		this.metaData = metaData;
	}

	public GridMetaData getMetaData() {
		return metaData;
	}

	public static <T> MetaTreeResult<T> fromList(GridMetaData metaData, List<TreeNode<T>> children) {
		metaData.setRoot("children");
		MetaTreeResult<T> result = new MetaTreeResult<T>();
		result.setMetaData(metaData);
		result.setChildren(children);
		return result;
	}

}
