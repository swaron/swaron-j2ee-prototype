(function(Manager, Class, flexSetter, alias) {
	/**
	 * add wait/notify function in loader
	 */
	Ext.applyIf(Ext.Loader, {
		notify : function(expressions) {
			var classNames = Ext.Array.from(expressions);
			var requireLoading = false;
			for (i = 0, ln = classNames.length; i < ln; i++) {
				var className = classNames[i];
				if (this.isFileLoaded.hasOwnProperty(className) && this.isFileLoaded[className] == false) {
					this.isFileLoaded[className] = true;
					requireLoading = true;
				}
			}
			if (requireLoading) {
				this.numPendingFiles--;
			}
			this.refreshQueue();
		},
		wait : function(expressions, fn, scope) {
			var possibleClassNames = Ext.Array.from(expressions);
			var classNames = [];
			for (i = 0, length = possibleClassNames.length; i < length; i++) {
				possibleClassName = possibleClassNames[i];
				if (!Manager.isCreated(possibleClassName)) {
					Ext.Array.include(classNames, possibleClassName);
				}
			}
			// all classes have been defined
			if (classNames.length === 0) {
				fn.call(scope);
				return this;
			}
	
			// push require class into queue
			this.queue.push({
				requires : classNames,
				callback : fn,
				scope : scope
			});
	
			var requireLoading = false;
			// simulate script loading, increase pending count,and set isFileLoaded property
			for (i = 0, ln = classNames.length; i < ln; i++) {
				var className = classNames[i];
				if (!this.isFileLoaded.hasOwnProperty(className)) {
					this.isFileLoaded[className] = false;
					requireLoading = true;
				}
			}
			if (requireLoading) {
				this.numPendingFiles++;
			}
			this.isLoading = true;
	
			// ==================================================//
			// this.numLoadedFiles++;
			// this.isFileLoaded[className] = true;
	
			this.numPendingFiles--;
	
			if (this.numPendingFiles === 0) {
				this.refreshQueue();
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

