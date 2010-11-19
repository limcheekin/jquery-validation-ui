<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>jQuery validation plug-in - comment form example</title>

<g:javascript library="jquery" plugin="jquery"/>
<jqval:resources />
<jqvalui:resources />

<!-- JQuery UI -->
<link rel="stylesheet" type="text/css" media="screen" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/themes/cupertino/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="http://jquery-ui.googlecode.com/svn/branches/dev/themes/base/ui.button.css" />

<script src="http://jquery-ui.googlecode.com/svn/tags/latest/ui/jquery.ui.core.js" type="text/javascript"></script>
<script src="http://jquery-ui.googlecode.com/svn/tags/latest/ui/jquery.ui.widget.js" type="text/javascript"></script>
<script src="http://jquery-ui.googlecode.com/svn/tags/latest/ui/jquery.ui.button.js" type="text/javascript"></script>

<script type="text/javascript" src="http://jqueryui.com/themeroller/themeswitchertool/"></script>


<script type="text/javascript">
$.validator.setDefaults({
//	submitHandler: function() { alert("submitted!"); },
	highlight: function(input) {
		$(input).addClass("ui-state-highlight");
	},
	unhighlight: function(input) {
		$(input).removeClass("ui-state-highlight");
	}
});

$(function() {
	var myForm = $('form:first');
	myForm.validate({
			debug: true,
			onkeyup: false,
			errorClass: 'error',
			validClass: 'valid',			
			success: function(label)
			{
				$('#' + label.attr("for")).qtip('destroy');	
			},			
			errorPlacement: function(error, element)
			{	
				if ($(error).text())		
				$(element).filter(':not(.valid)').qtip({
					overwrite: true,
					content: error,
					position: { my: 'left center', at: 'right center' },
					show: {
						event: false,
						ready: true
					},
					hide: false,
					style: {
						widget: true,
						// classes: 'ui-tooltip-red', 
						tip: true
					}
				});
			},		
			rules: {
				email: {
					required:true,
          email:true, 
				},
			  testInList: {
				  required:true,
				  inList:["A","B","C"]
				},
				testMatches: {
					required:true,
					matches:"[a-z]+"
				},
				testNotEqual: {
					required:true,
					notEqual:"ABC"
				},
				testMinDate: {
					required:true,
					date:true,
					minDate: new Date(2010, 10, 2)
				},
				testMaxDate: {
					required:true,
					date:true,
					maxDate: new Date(2010, 10, 2)
				},
				testRangeDate: {
					required:true,
					date:true,
					rangeDate: [new Date(2010, 10, 2), new Date(2010,11,12)]
				},				
				title: {
			       remote: {
			          url: '${createLink(controller:'jQueryRemoteValidator', action:'validate')}',
			          type: 'post',
			          data: {
			            validatableClass: 'org.grails.jquery.validation.ui.DummyDomain', 
			            property: 'title', 
			            constraint: 'blank'
			                   } 
			             }
			      }															
			},		
			messages: {
				email: {
					required:'Please enter your email address', 
					email:'Please enter a valid email address'
				},		
				testMatches: {
						required:'Please enter Test Matches', 
						matches:function(){ return 'Invalid value ' + $('#testMatches').val() + '! Please enter letter only, given format';}
				}							
			}		
	});
	
	$.fn.themeswitcher && $('<div/>').css({
		position: "absolute",
		right: 10,
		top: 10
	}).appendTo(document.body).themeswitcher();	
	
});
</script>

</head>
<body>
<div id="main">	

<!-- Custom messages with default "meta" setting -->
<form class="cmxform" id="commentForm2" method="post" action="">
	<fieldset class="ui-widget ui-widget-content ui-corner-all">
		<legend class="ui-widget ui-widget-header ui-corner-all">Please enter your email address</legend>
		<p>
			<label for="cemail">E-Mail *</label>
			<input id="cemail" name="email" class="ui-widget-content"/>
		</p>
		<p>
			<label for="testInList">Test inList *</label>
			<input id="testInList" name="testInList" class="ui-widget-content"/>
		</p>	
		<p>
			<label for="testMatches">Test matches *</label>
			<input id="testMatches" name="testMatches" class="ui-widget-content"/>
		</p>			
		<p>
			<label for="testNotEqual">Test notEqual *</label>
			<input id="testNotEqual" name="testNotEqual" class="ui-widget-content"/>
		</p>	
		<p>
			<label for="testMinDate">Test minDate *</label>
			<input id="testMinDate" name="testMinDate" class="ui-widget-content"/>
			Eg: October 13, 1975 11:13:00
		</p>	
		<p>
			<label for="testMaxDate">Test maxDate *</label>
			<input id="testMaxDate" name="testMaxDate" class="ui-widget-content"/>
		</p>	
		<p>
			<label for="testRangeDate">Test rangeDate *</label>
			<input id="testRangeDate" name="testRangeDate" class="ui-widget-content"/>
		</p>											
		<p>
			<button class="submit" type="submit">Submit</button>
		</p>
	</fieldset>
</form>

            <div id="controllerList" class="dialog">
                <h2>Available Controllers:</h2>
                <ul>
                    <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                        <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
                    </g:each>
                </ul>
            </div>

</body>
</html>