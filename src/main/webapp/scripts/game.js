var clearChat = function(){$('#message-textarea')[0].value="";}
 
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
});
