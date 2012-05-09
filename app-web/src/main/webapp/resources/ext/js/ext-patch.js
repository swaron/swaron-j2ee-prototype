(function(Manager, Class, flexSetter, alias) {
	/**
	 * add wait/notify function in loader
	 */
	Ext.applyIf(Ext.Loader, {
		notify : function(expressions) {
			var classNames = Ext.Array.from(expressions);
			// support multiple classes
			var missingClasses = [];
			for (i = 0, ln = classNames.length; i < ln; i++) {
				var className = classNames[i];
				// simulate onfileload decrease pending count corresponding to the attribute in wait.
				if (this.isClassFileLoaded.hasOwnProperty(className) && this.isClassFileLoaded[className] == false) {
					var filePath = this.classNameToFilePathMap[className] || this.getPath(className);
					this.onFileLoaded(className, filePath);
				}
				// if the class is not created, then this classname is just a place holder.
				// normally, we should create this class before call notify method.
				if (!Manager.isCreated(className)) {
					missingClasses.push(className);
				}
				if(className.match(/Factory$/g)){
					Ext.Error.raise({
						sourceClass : "ext-patch:Ext.Loader",
						sourceMethod : "notify",
						msg : "Invalid expression '" + name
								+ "' specified,should notify class instead of Factory class."
					});
				}
			}
			if (missingClasses.length > 0) {
				// <debug>
				Ext.Error.raise({
					sourceClass : "ext-patch",
					sourceMethod : "notify",
					msg : "The following classes are not declared even if they have been notified." + " '"
							+ missingClasses.join("', '") + "'. Please check their factory class."
				});
				// </debug>
			}
		},
		/**
		 * wait function is used to wait an instance of class xxxFactory to be created. <br>
		 * in the construct of xxxFactory, the xxx class need to be defined.<br>
		 * for example: Loader.wait('App.DummyFactory',{name:'hello'},function(){App.log('after Dummy defined') }) <br>
		 * means system should load App/DummyFactory.js, the class 'App.DummyFactory' should be defined in this js file<br>
		 * after the js is loaded, Lorder will call Ext.create('App.DummyFactory',{name:'hello'}), <br>
		 * and the constructor should defined class 'App.Dummy' synchronizely or asynchronizely. in both case, after the
		 * class is defined.<br>
		 * the constructor need to invoke Loader.notify('App.Dummy' to indicate that the class have been defined.)<br>
		 * after the 'App.Dummy' class is defined and other required resources are defined, the Ext Ready functions will
		 * be trigger.
		 * 
		 * usage:
		 * <p>
		 * Loader.wait('App.DummyFactory',{name:'hello'},function(){App.log('after Dummy defined') })
		 * <p>
		 * in App/DummyFactory.js, the constructor should invoke following codes. <br>
		 * Ext.define('App.Dummy');
		 * Loader.notify('App.Dummy');
		 */
		wait : function(expression, config, fn, scope) {
			// <debug>
			if ((typeof expression !== 'string' || !expression.match(/Factory$/g))) {
				Ext.Error.raise({
					sourceClass : "Ext.Loader",
					sourceMethod : "wait",
					msg : "Invalid expression '" + name
							+ "' specified, must be factory class string, end with Factory."
				});
			}
			// </debug>
			fn = fn || Ext.emptyFn;
			scope = scope || Ext.global;
			config = config || {};
			var factoryClassName = expression;
			var createInstance = function() {
				Ext.create(factoryClassName, config);
				var className = factoryClassName.substr(0, factoryClassName.length - 'Factory'.length)
				if (!this.isClassFileLoaded.hasOwnProperty(className)) {
					this.isClassFileLoaded[className] = false;
					this.numPendingFiles++;
				}
				this.queue.push({
					requires : [className],
					callback : fn,
					scope : scope
				});
			};
			this.require(factoryClassName, createInstance, this);
		}
	});

	Class.registerPreprocessor('wait', function(cls, data, continueFn) {
		var me = this, dependencies = [], className = Manager.getName(cls), i, j, ln, subLn, value, config;
		var propertyName = 'wait', propertyValue;
		if (data.hasOwnProperty(propertyName)) {
			propertyValue = data[propertyName];

			if (typeof propertyValue === 'string') {
				dependencies.push(propertyValue);
			} else if (propertyValue instanceof Array) {
				for (j = 0, subLn = propertyValue.length; j < subLn; j++) {
					value = propertyValue[j];
					if(j == 0){
						if (typeof value === 'string') {
							dependencies.push(value);
						}
					}
					if(j == 1){
						config = value;
					}
				}
			}
		}

		if (dependencies.length === 0) {
			return;
		}
		Ext.Loader.wait(dependencies, config, function() {
			propertyName = 'wait';
			if (data.hasOwnProperty(propertyName)) {
				propertyValue = data[propertyName];
				if (typeof propertyValue === 'string') {
					data[propertyName] = Manager.get(propertyValue);
				} else if (propertyValue instanceof Array) {
					for (j = 0, subLn = propertyValue.length; j < subLn; j++) {
						value = propertyValue[j];
						if(j == 0){
							data[propertyName] = Manager.get(value);
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
