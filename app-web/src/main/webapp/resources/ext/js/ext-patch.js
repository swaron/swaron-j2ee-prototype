(function(Manager, Class, flexSetter, alias) {
	//<if nonBrowser>
        var isNonBrowser = typeof window === 'undefined',
        isNodeJS = isNonBrowser && (typeof require === 'function'),
        isPhantomJS = (typeof phantom !== 'undefined' && phantom.fs);
    //</if>
	/**
	 * add wait/notify function in loader
	 */
	Ext.applyIf(Ext.Loader, {
        onFactoryFileLoaded:function(className,filePath, config){
			var factory = 'ClassFactory.' + className;
			if(!Manager.isCreated(factory)){
				//<debug>
                Ext.Error.raise({
                    sourceClass: "ext-patch",
                    sourceMethod: "notify.instantiate",
                    msg: "The following Factory classes are not declared even if their class have been loaded:" +
                            " '" + factory + "'. please check the source file:" + filePath
                });
				//</debug>
			}else{
				factory = Ext.create(factory,config);
			}
            //move following to notify method.
        },
		notify : function(expressions) {
			var classNames = Ext.Array.from(expressions);
			//support multiple classes
			var missingClasses = [];
			for (i = 0, ln = classNames.length; i < ln; i++) {
				var className = classNames[i];
				//simulate onfileload decrease pending count corresponding to the attribute in wait.
				if (this.isFileLoaded.hasOwnProperty(className) && this.isFileLoaded[className] == false) {
					var filePath = this.classNameToFilePathMap[className] || this.getPath(className);
					this.onFileLoaded(className,filePath);
				}
				//if the class is not created, then this classname is just a place holder.
				//normally, we should create this class before call notify method.
				if(!Manager.isCreated(className )){
					missingClasses.push(className);
				}
			}
			if(missingClasses.length > 0){
				//<debug>
                Ext.Error.raise({
                    sourceClass: "ext-patch",
                    sourceMethod: "notify",
                    msg: "The following classes are not declared even if they have been notified." +
                            " '" + missingClasses.join("', '") + "'. They will be created implicited now."
                });
				//</debug>
			}
		},
		wait : function(expression,config,fn,scope) {
			//<debug>
		    if ((typeof expression !== 'string' || !expression.match(/Factory$/g))) {
	            Ext.Error.raise({
	                sourceClass: "Ext.Loader",
	                sourceMethod: "wait",
	                msg: "Invalid expression '" + name + "' specified, must be factory class string, end with Factory."
	            });
	        }
			//</debug>
			fn = fn || Ext.emptyFn;
			scope = scope || Ext.global;
			config = config || {};
			var factoryClassName = expression;
			var createInstance = function(){
				Ext.create(factoryClassName,config);
			};
			this.require(factoryClassName,createInstance,scope);
			var className =  factoryClassName.substr(0, factoryClassName.length - 'Factory'.length)
			
			this.queue.push({
				requires : className,
				config : config,
				callback : fn,
				scope : scope
			});
	
			//below are same as require method			
			var i, ln, className;
            if (!this.isFileLoaded.hasOwnProperty(className)) {
                this.isFileLoaded[className] = false;
                this.numPendingFiles++;
            }
		}
	});
	
	Class.registerPreprocessor('wait', function(cls, data, continueFn) {
		var me = this, dependencies = [], className = Manager.getName(cls), i, j, ln, subLn, value;
		var propertyName = 'wait', propertyValue;
		if (data.hasOwnProperty(propertyName)) {
			propertyValue = data[propertyName];

			if (typeof propertyValue === 'string') {
				dependencies.push(propertyValue);
			} else if (propertyValue instanceof Array) {
				
				for (j = 0, subLn = propertyValue.length; j < subLn; j++) {
					value = propertyValue[j];

					if (typeof value === 'string') {
						dependencies.push(value);
					}
				}
			} else if (typeof propertyValue != 'function') {
				for (j in propertyValue) {
					if (propertyValue.hasOwnProperty(j)) {
						value = propertyValue[j];

						if (typeof value === 'string') {
							dependencies.push(value);
						}
					}
				}
			}
		}

		if (dependencies.length === 0) {
			// Loader.historyPush(className);
			return;
		}
		Ext.Loader.wait(dependencies, function() {
			propertyName = 'wait';
			if (data.hasOwnProperty(propertyName)) {
				propertyValue = data[propertyName];
				if (typeof propertyValue === 'string') {
					data[propertyName] = Manager.get(propertyValue);
				} else if (propertyValue instanceof Array) {
					for (j = 0, subLn = propertyValue.length; j < subLn; j++) {
						value = propertyValue[j];

						if (typeof value === 'string') {
							data[propertyName][j] = Manager.get(value);
						}
					}
				} else if (typeof propertyValue != 'function') {
					for (var k in propertyValue) {
						if (propertyValue.hasOwnProperty(k)) {
							value = propertyValue[k];

							if (typeof value === 'string') {
								data[propertyName][k] = Manager.get(value);
							}
						}
					}
				}
			}

			continueFn.call(me, cls, data);
		});
		return false;
	}, true);

	Class.setDefaultPreprocessorPosition('wait', 'last');

})(Ext.ClassManager, Ext.Class, Ext.Function.flexSetter, Ext.Function.alias);

