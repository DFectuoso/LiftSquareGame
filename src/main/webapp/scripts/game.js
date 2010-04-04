var clearChat = function(){$('#message-textarea')[0].value="";}

var getArrows = function(ev) {
  arrows=((ev.which)||(ev.keyCode));
  switch(arrows) {
    case 37:
      console.log('left arrow');
      break;
    case 38:
      console.log('up arrow');
      break;
    case 39:
      console.log('right arrow');
      break;
    case 40:
      console.log('down arrow');
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
