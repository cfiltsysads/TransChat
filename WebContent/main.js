// to keep the session id
var sessionId = '';
 
// name of the client
var name = '';
 
// socket connection url and port
var socket_url = '10.105.19.101';
var port = '8000';
var lang = 'English'
 
$(document).ready(function() {
 
    $("#form_submit, #form_send_message").submit(function(e) {
        e.preventDefault();
        join();
    });
});
function changeLang(){
	var x = document.getElementById("langSelect").value;
	//alert(x);
	lang=x;
	//alert(lang);
} 
var webSocket;
 
/**
 * Connecting to socket
 */
function join() {
    // Checking person name
    if ($('#input_name').val().trim().length <= 0) {
        alert('Enter your name');
    } else {
        name = $('#input_name').val().trim();
 
        $('#prompt_name_container').fadeOut(1000, function() {
            // opening socket connection
            openSocket();
        });
    }
 
    return false;
}
 
/**
 * Will open the socket connection
 */
function openSocket() {
    // Ensures only one connection is open at a time
    if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
        return;
    }
 
    // Create a new instance of the websocket
    webSocket = new WebSocket("ws://" + socket_url + ":" + port
            + "/WebMobileGroupChatServer/chat?name=" + name);
 
    /**
     * Binds functions to the listeners for the websocket.
     */
    webSocket.onopen = function(event) {
        $('#message_container').fadeIn();
 
        if (event.data === undefined)
            return;
 
    };
 
    webSocket.onmessage = function(event) {
 
        // parsing the json data
        parseMessage(event.data);
    };
 
    webSocket.onclose = function(event) {
        alert('Error! Connection is closed. Try connecting again.');
    };
}
 
/**
 * Sending the chat message to server
 */
function send() {
    var message = $('#input_message').val();
 
    if (message.trim().length > 0) {
        sendMessageToServer('message', message);
    } else {
        alert('Please enter message to send!');
    }
 
}
 
/**
 * Closing the socket connection
 */
function closeSocket() {
    webSocket.close();
 
    $('#message_container').fadeOut(600, function() {
        $('#prompt_name_container').fadeIn();
        // clearing the name and session id
        sessionId = '';
        name = '';
 
        // clear the ul li messages
        $('#messages').html('');
        $('p.online_count').hide();
    });
}
 
/**
 * Parsing the json message. The type of message is identified by 'flag' node
 * value flag can be self, new, message, exit
 */
function parseMessage(message) {
    var jObj = $.parseJSON(message);
 
    // if the flag is 'self' message contains the session id
    if (jObj.flag == 'self') {
 
        sessionId = jObj.sessionId;
 
    } else if (jObj.flag == 'new') {
        // if the flag is 'new', a client joined the chat room
        var new_name = 'You';
 
        // number of people online
        var online_count = jObj.onlineCount;
 
        $('p.online_count').html(
                'Hello, <span class="green">' + name + '</span>. <b>'
                        + online_count + '</b> people online right now')
                .fadeIn();
 
        if (jObj.sessionId != sessionId) {
            new_name = jObj.name;
        }
 
        var li = '<li class="new"><span class="name">' + new_name + '</span> '
                + jObj.message +'</li>';
        $('#messages').append(li);
 
        $('#input_message').val('');
 
    } else if (jObj.flag == 'message') {
        // if the json flag is 'message', it means somebody sent the chat
        // message
 
        var from_name = 'You';
 
        if (jObj.sessionId != sessionId) {
            from_name = jObj.name;
        }
        if(lang == "English")
        {
        var li = '<br><span class="name"><b>' + from_name + '</b></span> '
                + jObj.message  ;
        }
        else if(lang == "Hindi")
        {
            var li = '<br><span class="name"><b>' + from_name + '</b></span> '
                    + jObj.hindi  ;
         }
        else if(lang == "Gujarati")
        {
            var li = '<br><span class="name"><b>' + from_name + '</b></span> '
                    + jObj.gujarati  ;
            }
        else if(lang == "Marathi")
        {
            var li = '<br><span class="name"><b>' + from_name + '</b></span> '
                    + jObj.marathi  ;
            }
        else if(lang == "Punjabi")
        {
            var li = '<br><span class="name"><b>' + from_name + '</b></span> '
                    + jObj.punjabi  ;
            }
        else if(lang == "Malayalam")
        {
            var li = '<br><span class="name"><b>' + from_name + '</b></span> '
                    + jObj.malayalam  ;
            }
        else
        {
            var li = '<br><span class="name"><b>' + from_name + '</b></span> '
                    + jObj.message  ;
            }
        
        
        // appending the chat message to list
        appendChatMessage(li);
 
        $('#input_message').val('');
 
    } else if (jObj.flag == 'exit') {
        // if the json flag is 'exit', it means somebody left the chat room
        var li = '<li class="exit"><span class="name red">' + jObj.name
                + '</span> ' + jObj.message +'</li>';
 
        var online_count = jObj.onlineCount;
 
        $('p.online_count').html(
                'Hello, <span class="green">' + name + '</span>. <b>'
                        + online_count + '</b> people online right now');
 
        appendChatMessage(li);
    }
}
 
/**
 * Appending the chat message to list
 */
function appendChatMessage(li) {
    $('#messages').append(li);
 
    // scrolling the list to bottom so that new message will be visible
    $('#messages').scrollTop($('#messages').height());
}
 
/**
 * Sending message to socket server message will be in json format
 */
function sendMessageToServer(flag, message) {
    var json = '{""}';
 
    // preparing json object
    var myObject = new Object();
    myObject.sessionId = sessionId;
    myObject.message = message;
    myObject.flag = flag;
 
    // converting json object to json string
    json = JSON.stringify(myObject);
 
    // sending message to server
    webSocket.send(json);
}