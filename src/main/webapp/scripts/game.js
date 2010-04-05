var clearChat = function(){$('#message-textarea')[0].value="";}

var getArrows = function(ev) {
  arrows=((ev.which)||(ev.keyCode));
  switch(arrows) {
    case 37:
      post_to_server("control",{"direction": "left"});
      break;
    case 38:
      post_to_server("control",{"direction": "up"});
      break;
    case 39:
      post_to_server("control",{"direction": "right"});
      break;
    case 40:
      post_to_server("control",{"direction": "down"});
      break;
  }
}

jQuery('document').ready(function(){
  var submitChatOnEnter = function(e){
    var keynum;
    if(window.event) {
      keynum = e.keyCode;
    } else if(e.which) {
      keynum = e.which;
    }
    if (keynum == 13) {
      $('#posting-button').click();
      clearChat();
    }
  }

  $('#message-textarea')[0].onkeypress = submitChatOnEnter;
  document.onkeydown = getArrows;
});
