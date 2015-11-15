//--------==== FOR COPYING THE SELECTION IN TEXTAREA TO DICITONARY BELOW ====---------


function GetSelectedText(){ // FOR COPYING THE SELECTION IN TEXTAREA TO DICITONARY BELOW
				var userSelection, ta;
  				if (window.getSelection && document.activeElement){
    				if (document.activeElement.nodeName == "TEXTAREA" ||
        				(document.activeElement.nodeName == "INPUT" && 
        				document.activeElement.getAttribute("type").toLowerCase() == "text")){
      					
						ta = document.activeElement;
      					userSelection = ta.value.substring(ta.selectionStart, ta.selectionEnd);
    				} else {
      						userSelection = window.getSelection();
					}
					if(userSelection.toString().trim()==""){
						bootbox.alert("Please Select a Word first!");
					}
					else{
						document.form1.transWord.value = userSelection.toString().trim();
					}
  				} else {
    				// all browsers, except IE before version 9
    				if (document.getSelection){       
        				userSelection = document.getSelection();
        				if(userSelection.toString().trim()==""){
							bootbox.alert("Please Select a Word first!");
						}
						else{
							document.form1.transWord.value = userSelection.toString().trim();
						}
    				}
    				// IE below version 9
    				else if (document.selection){
    					userSelection = document.selection.createRange();
    					if(userSelection.toString().trim()==""){
							bootbox.alert("Please Select a Word first!");
						}
						else{
							document.form1.transWord.value = userSelection.text;
						}
    				}
  				}
			}
			
//--------==== FOR COPYING THE SELECTION IN TEXTAREA TO DICITONARY BELOW ====---------
			
//--------==== To scroll down to the desired location ====---------
	
jQuery.fn.extend(
{
  scrollTo : function(speed, easing)
  {
    return this.each(function()
    {
      var targetOffset = $(this).offset().top;
      $('html,body').animate({scrollTop: targetOffset}, speed, easing);
    });
  }
});

//--------==== To scroll down to the desired location ====---------

//--------==== Find and Replace Function ( A huge personal success doing this ) ====---------

function findAndReplace(o){
	o = jQuery.extend({},{ find:"", down:"", side:"" },o);
				
	var formstr = '<div class="field"><label for="findText">Find Text</label><input type="text" name="findText" id="findText" value="'+ o.find +'" /></div>'+
				'<div class="field"><label for="findText"></label><input type="hidden" name="side" id="side" value="'+ o.side +'" /></div>'+
				'<div class="field"><label for="replaceText">Replace by</label><input type="text" name="replaceText" id="replaceText" value="'+ o.replace +'" /></div>';
					
	jqistates = {
		state0: {
			title: 'Find And Replace',
			html: formstr,
			focus: 1,
			buttons: { Cancel: false, Find: true },
			submit: function(e, v, m, f){
				var e = "";
				m.find('.errorBlock').hide('fast',function(){ jQuery(this).remove(); });
							
				if (v) {
								
					/*if(isNaN(f.findText))
						e += "Please enter a numeric amount (No commas)<br />";		
						if(isNaN(f.replaceText))
						e += "Please enter a numeric down payment (No commas)<br />";*/// Error Checking for the text fields, not required in our case
					if(f.findText == ""){
						e += "Please enter some text to find<br />";
					}
					if(f.replaceText == ""){
						e += "Please enter some text to replace<br />";
					}
					var findText = f.findText.replace(/ /g, "\\s");
					var replaceText = f.replaceText.replace(/ /g, "\\s");
					if (e == "") {
																	
						//var findText = f.findText;
						//var replaceText = f.replaceText;
						//alert('-'+findText+'-');
						//alert('-'+replaceText+'-');
						var side = f.side;				
						var xmlhttp;
						if (window.XMLHttpRequest){
							xmlhttp=new XMLHttpRequest(); // code for IE7+, Firefox, Chrome, Opera, Safari
						}
						else{
							xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); // code for IE6, IE5
						}
						xmlhttp.onreadystatechange=function(){
							if (xmlhttp.readyState==4 && xmlhttp.status==200){
								document.getElementById("replacedText").innerHTML=xmlhttp.responseText;
							}
						}
						xmlhttp.open("GET","findReplace.php?findWord="+ findText + "&replaceWord=" + replaceText + "&side=" + side,true);
						xmlhttp.send();
						
						jQuery.prompt.getStateContent('state1').find('#replacedText').text(m);									
						jQuery.prompt.goToState('state1',true);
									
					}
					else{
						jQuery('<div class="errorBlock" style="display: none;">'+ e +'</div>').prependTo(m).show('slow');
					}
					return false;
				}

				else return true;
			}
		},
		state1: {
			html: '<span id="replacedText"></span>',
			focus: 1,
			buttons: { Back: false, Replace: true },
			submit: function(e,v,m,f,findText,replacedTe){
				if(v){
					//alert('true');
					//alert('-'+f.findText+'-');
					//alert('-'+f.replaceText+'-');
					var findText = f.findText.replace(/ /g, "\\s");
					var replaceText = f.replaceText.replace(/ /g, "\\s");
					var side = f.side;
					var xmlhttp;
					if (window.XMLHttpRequest){
						xmlhttp=new XMLHttpRequest(); // code for IE7+, Firefox, Chrome, Opera, Safari
					}
					else{
						xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); // code for IE6, IE5
					}

					xmlhttp.onreadystatechange=function(){
						if (xmlhttp.readyState==4 && xmlhttp.status==200){
							document.getElementById("replacedText").innerHTML=xmlhttp.responseText;
							var resultNew =  document.getElementById("replacedText").textContent || document.getElementById("replacedText").innerText;
							alert(resultNew);
							location.reload();
						}
					}
					//alert('-'+findText+'-');
					xmlhttp.open("GET","mysql-replace.php?findWord="+ findText + "&replaceWord=" + replaceText + "&side=" + side + "&queryType=replace",true);
					xmlhttp.send();
					//return true;
				}
				jQuery.prompt.goToState('state0');
				return false;
			}
		}
	};
				
	$.prompt(jqistates);
}
			
//--------==== Find and Replace Function ====---------


//--------==== To send the sentences to a PHP for draggable boxes ====---------

function alignSentencesfunc(){
			$('#alignResults').html('<b>Please wait . . . </b>');
			var xmlhttp;
			var ssentence = $("#ssentence").val();
			var tsentence = $("#tsentence").val();	
			
			if (window.XMLHttpRequest){
  				xmlhttp=new XMLHttpRequest(); // code for IE7+, Firefox, Chrome, Opera, Safari
  			}
			else{
  				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); // code for IE6, IE5
  			}

			xmlhttp.onreadystatechange=function(){
  				if (xmlhttp.readyState==4 && xmlhttp.status==200){
    				document.getElementById("alignResults").innerHTML=xmlhttp.responseText;
    				$( "ul.droptrue" ).sortable({
						connectWith: "ul"
					});
					$( "#sortable1, #sortable2, #sortable3" ).disableSelection();
    			}
  			}
  			xmlhttp.open("POST","alignSen.php",true);
			xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
			xmlhttp.send("slang=ENG&tlang=HIN&ssentence=" + ssentence + "&tsentence=" + tsentence);
		}

//--------==== Draggable function ( to make boxes draggable )====---------

$(function() {
	$( "ul.droptrue" ).sortable({
		connectWith: "ul"
	});

	$( "ul.dropfalse" ).sortable({
		connectWith: "ul",
		dropOnEmpty: false
	});

	$( "#sortable1, #sortable2, #sortable3" ).disableSelection();
});

//--------==== Draggable function ====---------

			
//<!--SCRIPT TO SHOW TRANSLATION RESULTS-->


//--------==== Dictionary Search Function ====---------

function showtransresults(){
			var xmlhttp;
			if (window.XMLHttpRequest){
  				xmlhttp=new XMLHttpRequest(); // code for IE7+, Firefox, Chrome, Opera, Safari
  			}
			else{
  				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); // code for IE6, IE5
  			}

			xmlhttp.onreadystatechange=function(){
  				if (xmlhttp.readyState==4 && xmlhttp.status==200){
    				document.getElementById("transresults").innerHTML=xmlhttp.responseText;
    			}
  			}
			xmlhttp.open("GET","translate.php?transWord="+ document.forms["form1"]["transWord"].value +"&submit=Search&start=0" ,true);
			xmlhttp.send();	
			if(!$("#transresults").is(':visible')){
				$('#transresults').toggle('show');
			}
			$('#transresults').scrollTo(800);

		}

//--------==== Dictionary Search Function ====---------


//--------==== Translation from Eng to Hin Function ====---------
		
function showtranslationresults(){
				$('#translationResults').html('Please wait . . . ');
				var xmlhttp;
				if (window.XMLHttpRequest){
					xmlhttp=new XMLHttpRequest(); // code for IE7+, Firefox, Chrome, Opera, Safari
				}
				else{
					xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); // code for IE6, IE5
				}

				xmlhttp.onreadystatechange=function(){
					if (xmlhttp.readyState==4 && xmlhttp.status==200){
						//alert(xmlhttp.responseText);
						setTimeout(document.getElementById("translationResults").innerHTML=xmlhttp.responseText,2000);
						//document.getElementById("translationResults").innerHTML=xmlhttp.responseText;
					}
				}
				//xmlhttp.open("GET","http://10.144.22.105/~saadhak/translation/translation.php?sentence="+ document.forms["transForm"]["sendssentence"].value,true);
				xmlhttp.open("GET","src/translation/translation.php?sentence="+ document.forms["transForm"]["sendssentence"].value,true);
				xmlhttp.send();
				if(!$("#translationOutput").is(':visible')){
					$('#translationOutput').show();
				}
				//$(document).scrollTop( $("#translationOutput").offset().top );
				$('#translationOutput').scrollTo(800);
				
		}			

//--------==== Translation from Eng to Hin Function ====---------


//--------==== Selection of Word function ====---------

function GetWordnetWord(){ // TO GET WORD TO BE SEARCHED IN HINDI WORDNET
				var userSelection, ta;
  				if (window.getSelection && document.activeElement){
    				if (document.activeElement.nodeName == "TEXTAREA" ||
        				(document.activeElement.nodeName == "INPUT" && 
        				document.activeElement.getAttribute("type").toLowerCase() == "text")){
      					
						ta = document.activeElement;
      					userSelection = ta.value.substring(ta.selectionStart, ta.selectionEnd);
    				} else {
      						userSelection = window.getSelection();
					}
					if(userSelection.toString().trim()==""){
						bootbox.alert("Please Select a Word first!");
					}
					else{
						wordnetWord = userSelection.toString().trim();
					}
  				} else {
    				// all browsers, except IE before version 9
    				if (document.getSelection){       
        				userSelection = document.getSelection();
        				if(userSelection.toString().trim()==""){
							bootbox.alert("Please Select a Word first!");
						}
						else{
							wordnetWord = userSelection.toString().trim();
						}
    				}
    				// IE below version 9
    				else if (document.selection){
    					userSelection = document.selection.createRange();
    					if(userSelection.text==""){
							bootbox.alert("Please Select a Word first!");
						}
						else{
							wordnetWord = userSelection.text;
						}
    				}
  				}
			}
			function HWordnetSearch(){ // SEARCH IN HINDI WORDNET
				window.open('http://www.cfilt.iitb.ac.in/wordnet/webhwn/wn.php?nomorph=true&hwd='+wordnetWord,'_blank');
			}
			function EWordnetSearch(){ // SEARCH IN ENGLISH WORDNET
				window.open('http://wordnetweb.princeton.edu/perl/webwn?s='+wordnetWord,'_blank');
			}
			function shabdkoshSearch(){ // SEARCH IN SHABDKOSH
				window.open('http://www.shabdkosh.com/hi/translate?e='+wordnetWord+'+&l=hi','_blank');
			}
			function MWordnetSearch(){ // SEARCH IN MARATHI WORDNET
				window.open('http://www.cfilt.iitb.ac.in/wordnet/webmwn/wn.php?hwd='+wordnetWord,'_blank');
			}
			function GoogleSearch(){ // SEARCH ON GOOGLE
				var googleuri = 'http://www.google.co.in/search?q='+wordnetWord;
				var googleurl = encodeURI(googleuri);
				window.open(googleurl,'_blank');
			}
			function GoogleTranslateSearchENHI(){ // SEARCH ON GOOGLE ENGLISH TO HINDI
				var googletransuri = 'http://translate.google.co.in/#en/hi/'+wordnetWord;
				var googletransurl = encodeURI(googletransuri);
				window.open(googletransurl,'_blank');
			}
			function GoogleTranslateSearchAUTOEN(){ // SEARCH ON GOOGLE AUTOMATIC LANGUAGE DETECT TO ENGLISH
				var googletransuri = 'http://translate.google.co.in/#auto/en/'+wordnetWord;
				var googletransurl = encodeURI(googletransuri);
				window.open(googletransurl,'_blank');
			}
			function WikipediaSearch(){ // SEARCH ON GOOGLE
				
				var wikiuri = 'http://en.wikipedia.org/wiki/'+wordnetWord;
				var wikiurl = encodeURI(wikiuri);
				window.open(wikiurl,'_blank');
			}
			function WikitionarySearch(){ // SEARCH ON GOOGLE
				var wikitionaryuri = 'http://en.wiktionary.org/wiki/'+wordnetWord;
				var wikitionaryurl = encodeURI(wikitionaryuri);
				window.open(wikitionaryurl,'_blank');
			}

$(document).ready(function() {
            $("#ssentence").ContextMenu("context-menu-1");
            $("#tsentence").ContextMenu("context-menu-2");
         });
$(document).ready(function(){
			$(document).on('click',"#hideshow", function(event) {        
				$('#transresults').toggle('show');
			});
		});
$(document).ready(function(){
			$(document).on('click',"#hideshowTranslationPane", function(event) {        
				$('#translationBox').toggle('show');
			});
		});		
$(document).ready(function(){
			$(document).on('click',"#hideshowresults", function(event) {        
				$('#translationOutput').toggle('show');
			});
		});	
$(document).ready(function(){
			$(document).on('click',"#hideshowalignresults", function(event) {        
				$('#alignResults').toggle('show');
			});
		});			
		
function openwindow (url) {
   var win = window.open(url, "window1", "width=800,height=400,status=yes,scrollbars=yes,resizable=yes");
   win.focus();
}

/*$(document).ready(function(){
			$(document).on('click', '#gotoSentence', function(event) {      
				bootbox.prompt("Please enter sentence number: ", function(result) {
				if (result === null || result === '') {//do nothing					
				} else {
					window.location = '?offset=' + result;
				}
			});
		});
	});*/
	
function gotoSentencenew() {
	var result = document.getElementById('searchByThis').value;
	if (result  === null || result === '') {//do nothing					
	} else {
		window.location = 'search.php?offset=' + result;
	}
}
function gotoWordnew() {
	var result = document.getElementById('searchByThis').value;
	if (result  === null || result === '') {//do nothing					
	} else {
		window.location = '?word=' + result;
	}
}		
	
		
/*$(document).ready(function(){
			$(document).on('click', '#gotoWord', function(event) {      
				bootbox.prompt("Please enter phrase or word: ", function(result) {
				if (result === null || result === '') {//do nothing					
				} else {
					window.location = '?word=' + result;
				}
			});
		});
	});*/	
$(document).ready(function(){
	$(document).on('click', '#Next', function(event) {     
		event.preventDefault();   
		var nextval = $("#nextval").val();
		window.location = '?offset=' + nextval;
	});
});
$(document).ready(function(){
	$(document).on('click', '#Next2', function(event) {        
		event.preventDefault();
		var nextval = $("#nextval").val();
		window.location = '?offset=' + nextval;
	});
});
$(document).ready(function(){
	$(document).on('click', '#Prev', function(event) {        
		event.preventDefault();
		var prevval = $("#prevval").val();
		window.location = '?offset=' + prevval;
	});
});				
$(document).ready(function(){
	$(document).on('click', '#Prev2', function(event) {        
		event.preventDefault();
		var prevval = $("#prevval").val();
		window.location = '?offset=' + prevval;
	});
});		
$(document).ready(function(){
	$(document).on('click', '#first', function(event) {        
		event.preventDefault();
		window.location = '?offset=1';
	});
});	
$(document).ready(function(){
	$(document).on('click',"#skippedFirst", function(event) {        
		event.preventDefault();
		window.location = '?offset=0';
	});
});
$(document).ready(function(){
	$(document).on('click', '#lastSentence',function(event) {        
		event.preventDefault();
		var sentencecount = $("#sentencecount").val();
		window.location = '?offset=' + sentencecount;
	});
});				
$(document).ready(function(){
	$(document).on('click', '#skippedPrev', function(event) {        
		event.preventDefault();
		var prevval = $("#prevval").val();
		window.location = '?offset=' + prevval;
	});
});		
$(document).ready(function(){
	$(document).on('click', '#skippedPrev2', function(event) {        
		event.preventDefault();
		var prevval = $("#prevval").val();
		window.location = '?offset=' + prevval;
	});
});			
$(document).ready(function(){
	$(document).on('click', '#skippedNext', function(event) {        
		event.preventDefault();
		var skipnextval = $("#skipnextval").val();
		window.location = '?offset=' + skipnextval;
	});
});
$(document).ready(function(){
	$(document).on('click', '#skippedNext2', function(event) {        
		event.preventDefault();
		var skipnextval = $("#skipnextval").val();
		window.location = '?offset=' + skipnextval;
	});
});



//--------==== Click on Align word pairs ====---------

		
$(document).ready(function(){
			$(document).on('click',"#submitAlign", function(event) {        
				alert($("#resultDiv").html());
				var resultDiv = $("#resultDiv").html();
				var xmlhttp;
				if (window.XMLHttpRequest){
					xmlhttp=new XMLHttpRequest(); // code for IE7+, Firefox, Chrome, Opera, Safari
				}
				else{
					xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); // code for IE6, IE5
				}

				xmlhttp.onreadystatechange=function(){
					if (xmlhttp.readyState==4 && xmlhttp.status==200){
						setTimeout(document.getElementById("alignSubmitresults").innerHTML=xmlhttp.responseText,2000);
					}
				}
				xmlhttp.open("GET","alignSubmit.php?resultDiv="+ resultDiv,true);
				xmlhttp.send();
				
			});
		});			

//--------==== Click on Align word pairs ====---------

	
/*$(document).ready(function(){ //THIS JQUERY FUNCTION WAS PREVIOUSLY BEING USED FOR SUBMIT CLICK IN DICTIONARY, 
	jQuery('#submit').live('click', function(event) { // TO TOGGLE THE VISIBILITY OF transresults DIV IN CASE ITS HIDDEN.
		if(!$("#transresults").is(':visible')){ // BUT NOW THE FUNCTION showtransresults CHECK THIS IN ITSELF, DO NOT DELETE, FOR REFERENCE
			jQuery('#transresults').toggle('show');
		}
	});
});*/
		

//--------==== Click on Align Submit Button in Align word pairs ====---------

		
$(document).ready(function(){
	$(document).on('click',"#AlignButton", function(event) {        
		ele = document.getElementById("resultDiv");
		var result=new Array();
		for (i in ele.childNodes){ 
			var key ;
			var val = new Array();
			for(child in ele.childNodes[i].childNodes){
				if(ele.childNodes[i].childNodes[child].nodeName==="B"){
					key=ele.childNodes[i].childNodes[child].childNodes[0].innerHTML;
				}else  if(ele.childNodes[i].childNodes[child].className=="ui-state-default"){
					var html = ele.childNodes[i].innerHTML;
					val.push(ele.childNodes[i].childNodes[child].childNodes[0].innerHTML);
					//alert(ele.childNodes[i].innerHTML);
					//alert(ele.childNodes[i].childNodes[child].innerHTML);
				}
			}
			result.push([key,val]);
		}
		alert(JSON.stringify(result));
	});
});	

//--------==== Click on Align Submit Button in Align word pairs ====---------

/*$(window).on('load', function () {

	$('.selectpicker').selectpicker({
		'selectedText': 'cat'
	});
    // $('.selectpicker').selectpicker('hide');
});*/

//--------==== Click on Align Submit Button in Align word pairs ====---------

