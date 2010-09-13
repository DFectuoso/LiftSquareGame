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
 document.onkeydown = getArrows;
});

function u(id,x,y) {
  node = document.getElementById(id)
  node.style.top = x + "px";
  node.style.left = y + "px";
}
