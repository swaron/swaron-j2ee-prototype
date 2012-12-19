$(function(){
			var deviceTypeList = [{key:'101',value:'交换机'},{key:'102',value:'集线器'},{key:'103',value:'防火墙'},{key:'104',value:'VPN'},{key:'105',value:'路由器'},{key:'201',value:'入侵检测系统IDS'},{key:'202',value:'病毒监测系统'},{key:'203',value:'网络审计系统'},{key:'204',value:'网站安全检测系统'},{key:'205',value:'邮件监控系统'},{key:'300',value:'服务器-未知'},{key:'301',value:'监控设备控制台'},{key:'302',value:'监控设备管理中心'},{key:'303',value:'应用服务器'},{key:'304',value:'数据库服务器'},{key:'305',value:'数据采集服务器'},{key:'401',value:'显示操作终端'},{key:'402',value:'液晶屏幕'},{key:'403',value:'LED显示屏幕'},{key:'500',value:'工作站'},{key:'501',value:'未知设备'},{key:'502',value:'个人办公终端'},{key:'503',value:'打印机'},{key:'504',value:'碎纸机'},{key:'505',value:'复印机'}];
			var osTypeList = [{key:'1',value:'网站类'},{key:'2',value:'服务类'},{key:'100',value:'AIX'},{key:'101',value:'BSD'},{key:'102',value:'HP-UX'},{key:'103',value:'LINUX'},{key:'104',value:'MACOS'},{key:'105',value:'SOLARIS'},{key:'106',value:'UNIX'},{key:'107',value:'WINDOWS'},{key:'108',value:'UNKNOWN'},{key:'109',value:'Tru64 UNIX'},{key:'110',value:'VRP'},{key:'111',value:'IOS'},{key:'112',value:'SGOS'},{key:'113',value:'EMBED'},{key:'201',value:'默认联系人'},{key:'202',value:'其他联系人'},{key:'203',value:'领导'},{key:'301',value:'自管'},{key:'302',value:'托管'},{key:'303',value:'虚拟空间'},{key:'402',value:'互联网'},{key:'403',value:'内网'},{key:'404',value:'其他'},{key:'5555',value:'机房'},{key:'5556',value:'监控室'},{key:'5557',value:'库房'},{key:'5558',value:'监控节点'},{key:'100001',value:'其他'},{key:'100005',value:'监控区'},{key:'100026',value:'网络'}];
			var locList = [{key:'2',value:'北京'},{key:'3',value:'上海'},{key:'4',value:'西安'},{key:'5',value:'海口'},{key:'6',value:'厦门'},{key:'7',value:'天津'},{key:'8',value:'沈阳'},{key:'9',value:'泉州'},{key:'10',value:'深圳'},{key:'11',value:'汕头'},{key:'12',value:'武汉'},{key:'13',value:'null'}];
			var infoSysList = [];
			$('#queryLocId').combobox({  
				mode : 'remote',
				data : locList,
				valueField:'key',  
				textField:'value',
				editable: false,
				panelHeight : 200,
				multiple : false,
				onChange:function(){
					onSearch();
		        }  
			});
			/*把枚举类型的key，转化成value，供页面显示*/	
			function typeFormatter(datas,value){
				for(var i=0; i<datas.length; i++){
					if (datas[i].key == value) return datas[i].value;
				}
				return value;
			}
			function listTypeFormatter(datas,value){
				if(value ==null){
					value = [];
				}
				var result = '';
				for(var j=0; j<value.length; j++){
					for(var i=0; i<datas.length; i++){
						if (datas[i].key == value[j]){
							result = result + datas[i].value;
							if(j != (value.length -1)){
								result = result+",";
							}
							break;
						}
					}
				}
				return result;
			}
			function onSearch() {
				$('#district-event-table').datagrid('load', {
					locId : $('#queryLocId').combo('getValues').toString()
				});
			}
			$('#district-event-table').datagrid({
				pagination:true,
				rownumbers:true,
				url:'soc.asset.AssetEventAction$queryGrid.json',
				//sortName: 'LocId',
				sortOrder: 'asc',
				fitColumns: true,
				singleSelect: true,
				striped: true,
				collapsible: false,
				idField: 'id',
				pageSize: 10,//每页显示的记录条数，默认为10 
				pageList: [10,20,50],//可以设置每页记录条数的列表 	
				frozenColumns:[
		             [{field:'ck',checkbox:true}]
		        ],
		        toolbar: "#toolbar",
		        columns:[[
					{field:'edId',title:'资产id',hidden:true}, 
					{field:'edName',title:'资产名称',width:100,sortable:true}, 
					{field:'deviceType',title:'资产类型',width:100,sortable:true,formatter:function(value){return typeFormatter(deviceTypeList,value)}},   
		            {field:'osType',title:'操作系统',width:100,align:'center', sortable:true,formatter:function(value){return typeFormatter(osTypeList,value)}},
					{field:'edIp',title:'资产IP',width:140,align:'center',sortable:true },
					{field:'orgName', title:'组织机构',width:100,align:'left', sortable:false},
		          	{field:'isId',title:'信息系统',width:120,align:'left',sortable:true,formatter:function(value){return listTypeFormatter(infoSysList,value)}},
		            {field:'locId',title:'地理区域',width:120,align:'center',sortable:false,formatter:function(value){return typeFormatter(locList,value)}},
		            {field:'edCode',title:'资产编码',width:100,align:'center'},
		            {field:'eventCount',title:'事件数量',width:100,align:'center'}
				]],
				onClickCell: function(rowIndex, field, value){
					var rec = $('#district-event-table').datagrid('getRowByIndex',rowIndex);
					var edId = rec.edId;
					if(field == 'eventCount'){
						$('#assetEvemtList').window({
							title:"安全事件列表",
							minimizable:false,
							maximizable:false,
							collapsible:false,
							inline:true,
						    width:800,
						    height:370,  
						});
						$('#assetEvemtList').window('open').window('refresh','/socweb/jsp/soc/asset/assetEventList.jsp?edId='+edId);
					}
				}
			});
		});
