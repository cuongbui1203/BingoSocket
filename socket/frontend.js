$(function () {
    // if user is running mozilla then use it's built-in WebSocket
    window.WebSocket = window.WebSocket || window.MozWebSocket;
  
    var connection = new WebSocket('ws://127.0.0.1:10637/game/bingo');
  
    connection.onopen = function () {
      // connection is opened and ready to use
      let data = {
        type: 0,
        select:1,
        dataTable:[]
      }

      let msg = {
        type:0,
        select:1,
        sender:"1",
        table:[1,2],
        pass: 'password',
      }
      connection.send(JSON.stringify(msg))
      console.log(JSON.stringify(msg))
    };
  
    connection.onerror = function (error) {
      // an error occurred when sending/receiving data
      console.log(error)
    };
  
    connection.onmessage = function (message) {
      // try to decode json (I assume that each message
      // from server is json)
      try {
        var json = JSON.parse(message.data);
      } catch (e) {
        console.log('This doesn\'t look like a valid JSON: ',
            message.data);
        return;
      }
      console.log(json)
      console.log(json.sender)
      
      // handle incoming message
    };
    document.getElementById('btn').onclick = function(){
      let t = document.getElementById('in').value;
      message = {
        type:4,
        select:document.getElementById('in').value,
        sender:'wed',
        table:[-1]
      }
      connection.send(JSON.stringify(message))
      console.log(JSON.stringify(message))
    }
  });