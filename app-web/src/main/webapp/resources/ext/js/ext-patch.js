(function(Manager, Class, flexSetter, alias) {
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
				factory.newInstance(config);
			}
            //move following to notify method.
        },
		notify : function(expressions) {
			var classNames = Ext.Array.from(expressions);
			//support multiple classes
			var missingClasses = [];
			for (i = 0, ln = classNames.length; i < ln; i++) {
				var className = classNames[i];
				//decrease pending count corresponding to the attribute in wait.
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
                // we need to define thise classes, becuase we use queue underline, the queue will not be cleared if these classes are not defined.
                for (var i = 0, length = missingClasses.length; i < length; i++) {
                	var className = missingClasses[i];
                	Ext.define(className,{
                		dummy:true
                	});
                }
			}
		},
		wait : function(expressions,config,fn,scope) {
			fn = fn || Ext.emptyFn;
			scope = scope || Ext.global;
			config = config || {};
			
			//(try to) support wait on multiple classes, exclude already created classes.
			//currrently, we don't support multiple classes, because they may expect different constructor values
			var possibleClassNames = Ext.Array.from(expressions);
			//<debug>
			if(possibleClassNames.length > 1){
                Ext.Error.raise({
                    sourceClass: "ext-patch",
                    sourceMethod: "requireSingleton",
                    msg: "requireSingleton don't support multiple classes currently."
                });
			}
			//</debug>
			var classNames = [];
			for (i = 0, length = possibleClassNames.length; i < length; i++) {
				possibleClassName = possibleClassNames[i];
				if (!Manager.isCreated(possibleClassName)) {
					Ext.Array.include(classNames, possibleClassName);
				}
			}
			//if all classes have been defined, return immediately
			if (classNames.length === 0) {
				fn.call(scope);
				return this;
			}

			this.queue.push({
				requires : classNames,
				config : config,
				callback : fn,
				scope : scope
			});
	
			//below are same as require method			
			var i, ln, className;
			for (i = 0, ln = classNames.length; i < ln; i++) {
                className = classNames[i];

                if (!this.isFileLoaded.hasOwnProperty(className)) {
                    this.isFileLoaded[className] = false;

                    filePath = this.getPath(className);

                    this.classNameToFilePathMap[className] = filePath;

                    this.numPendingFiles++;

                    //<if nonBrowser>
                    if (isNonBrowser) {
                        if (isNodeJS) {
                            require(filePath);
                        }
                        // Temporary support for hammerjs
                        else {
                            var f = fs.open(filePath),
                                content = '',
                                line;

                            while (true) {
                                line = f.readLine();
                                if (line.length === 0) {
                                    break;
                                }
                                content += line;
                            }

                            f.close();
                            eval(content);
                        }

                        this.onFileLoaded(className, filePath);

                        if (ln === 1) {
                            return Manager.get(className);
                        }

                        continue;
                    }
                    //</if>
                    this.loadScriptFile(
                        filePath,
                        Ext.Function.pass(this.onFactoryFileLoaded, [className, filePath, config], this),
                        Ext.Function.pass(this.onFileLoadError, [className, filePath]),
                        this,
                        this.syncModeEnabled
                    );
                }
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

