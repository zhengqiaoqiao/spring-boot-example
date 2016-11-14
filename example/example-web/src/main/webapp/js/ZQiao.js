// Initialize your app
window.App = new Framework7({
    modalTitle: 'App',
    material: true,
    materialPageLoadDelay: 200
});

//Export selectors engine
var $$ = Dom7;

App.ajax = function(url, method, contentType, dataType, data, success, error){
	if(!url){
		console.log('请求url错误!');
		return;
	}
	var beforeSend = function(xhr){
		console.log('request send before!');
	};
	var complete = function(xhr, status){
		console.log('request send complete!');
	};
	if(!success || typeof(success) != 'function'){
		success = function(data, status, xhr){
			console.log('request send success!');
		};
	}
	if(!error || typeof(error) != 'function'){
		error = function(xhr, status){
			console.log('request send error!');
		};
	}
	if(!method){
		method = 'GET';
	}
	$$.ajax({
		url:url,
		method:method,
		contentType:contentType,
		dataType:dataType,
		data:data,
		beforeSend:beforeSend,
		error:error,
		success:success,
		complete:complete,
	});

}

App.initTemplate = function(contentId, templateId, data){
	// Template
    var template = document.getElementById(templateId).innerHTML;

    // Compile and render
    var compileStartTime = new Date().getTime();
    var compiled = Template7(template).compile();
    var compileEndTime = new Date().getTime();
    var compiledRendered = compiled(data);
    var compiledRenderedTime = new Date().getTime();

    // Insert rendered template
    document.getElementById(contentId).innerHTML = compiledRendered;
    console.log('<span>Compilation: ' + (compileEndTime - compileStartTime) + 'ms</span><span>Rendering: ' + (compiledRenderedTime - compileEndTime) + 'ms</span><span>Compilation + Rendering: ' + (compiledRenderedTime - compileStartTime) + 'ms</span>');
}
	
	

