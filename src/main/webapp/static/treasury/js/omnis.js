function messageAlert(title, message)
{
	bootbox.dialog({
	  title: title,
	  message: message,
	  buttons: {
		    success: {
		      label: "Ok",
		      className: "btn-primary",
		    }
	}
});
}

angular.isUndefinedOrNull = function(val) {
	alert(val);
    return angular.isUndefined(val) || val == null 
}
	


function createAngularPostbackFunction(angular_scope){
	return function(model) {

		angular_scope.$apply();
 		var form = $('form[name="' + angular_scope.form.$name + '"]');
 		var previousActionURL = form.attr("action");
			form.submit = function(e)
					{
					    var postData = $(this).serializeArray();
					    var formURL = $(this).attr("action");
					    $.ajax(
					    {
					        url : formURL,
					        type: "POST",
					        data : postData,
					        success:function(data, textStatus, jqXHR) 
					        {
					        	angular_scope.object = data;
					        	angular_scope.$apply();
					        },
					        error: function(jqXHR, textStatus, errorThrown) 
					        {
					        	messageAlert("error submiting data");     
					        },
					    });
					};
			
			form.attr("action", form.find('input[name="postback"]').attr('value'));
 			form.submit();
        	form.attr("action", previousActionURL);		
	};
}