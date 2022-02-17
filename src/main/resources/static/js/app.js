const ACTION_DRAW = "DRAW";
const ACTION_ERASE = "ERASE";
const ACTION_DOT = "DOT";
const ACTION_UNDO = "UNDO";

var stompClient = null;

// let context2d = $("#canvas")[0].getContext('2d');

let players = [];

let player = new Player();
console.log(player);

let history = [];

function Player()
{
    this.color = "black";
    this.prevX = 0;
    this.prevY = 0;
    this.currX = 0;
    this.currY = 0;
    this.lineWidth = 7;
    this.flag = false;
    this.dot_flag = false;
}

function Message(action)
{
    this.action = action;
    this.data = {};
}

var canvas = undefined;
var ctx = undefined;

function init() {
    canvas = document.getElementById('can');
    ctx = canvas.getContext("2d");

    canvas.addEventListener("mousemove", function (e) {
        findxy('move', e)
    }, false);
    canvas.addEventListener("mousedown", function (e) {
        findxy('down', e)
    }, false);
    canvas.addEventListener("mouseup", function (e) {
        findxy('up', e)
    }, false);
    canvas.addEventListener("mouseout", function (e) {
        findxy('out', e)
    }, false);

    connect();
}

function color(obj) {
    let previousColor = player.color;
    switch (obj.id) {
        case "green":
            player.color = "green";
            break;
        case "blue":
            player.color = "blue";
            break;
        case "red":
            player.color = "red";
            break;
        case "yellow":
            player.color = "yellow";
            break;
        case "orange":
            player.color = "orange";
            break;
        case "black":
            player.color = "black";
            break;
        case "white":
            player.color = "white";
            break;
    }
    if (player.color === "white")
        player.lineWidth = 14;
    else
        player.lineWidth = 5;

    toggleColorSelection(previousColor, player.color);
}

function toggleColorSelection(previousColor, color) {

    if (previousColor === "white") {
        $("#" + previousColor).css({"-moz-box-shadow": "none",
            "-webkit-box-shadow": "none",
            "box-shadow": "none"});

        // $("#" + previousColor).css("border", "solid black 2px");
    } else {
        console.log("Removing shadow for " + previousColor);
        $("#" + previousColor).css({"-moz-box-shadow": "none",
            "-webkit-box-shadow": "none",
            "box-shadow": "none"});

        // $("#" + previousColor).css("border", "");
    }

    // $("#" + color).css("border", "solid #44f 3px");
    $("#" + color).css({"-moz-box-shadow": "inset 0px 0px 0px 2px #44f",
        "-webkit-box-shadow": "inset 0px 0px 0px 2px #44f",
        "box-shadow": "inset 0px 0px 0px 2px #44f"});
}

function draw(player) {
    ctx.beginPath();
    ctx.moveTo(player.prevX + 1, player.prevY + 1);
    ctx.lineTo(player.currX + 1, player.currY + 1);
    ctx.strokeStyle = player.color;
    ctx.lineWidth = player.lineWidth;
    ctx.stroke();
    ctx.closePath();
}

function eraseWithConfirmation() {
    var m = confirm("Want to clear");
    if (m) {
        sendErase();
    }
}

function erase() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    // document.getElementById("canvasimg").style.display = "none";
}

// function save() {
//     document.getElementById("canvasimg").style.border = "2px solid";
//     var dataURL = canvas.toDataURL();
//     document.getElementById("canvasimg").src = dataURL;
//     document.getElementById("canvasimg").style.display = "inline";
// }

function restoreLastHistory(data) {
    let lastHistory = data.lastHistory;
    console.debug("Restoring to: " + lastHistory);
    var img = new Image();
    img.onload = function() {
        erase();
        console.debug("Drawing image");
        console.debug(img);
        ctx.drawImage(img, 0, 0, canvas.width, canvas.height); // Or at whatever offset you like
    };
    img.src = lastHistory;
}

function processWebsocketMessage(message) {
    if (message.action === ACTION_DRAW) {
        draw(message.data);
    } else if (message.action === ACTION_DOT) {
      drawDot(message.data);
    } else if (message.action === ACTION_ERASE) {
        erase();
    } else if (message.action === ACTION_UNDO) {
        restoreLastHistory(message.data);
    }
}

function sendErase() {
    let message = new Message(ACTION_ERASE);
    stompClient.send("/app/action", {}, JSON.stringify(message));
}

function sendDrawData() {
    let message = new Message(ACTION_DRAW);
    message.data = player;
    stompClient.send("/app/action", {}, JSON.stringify(message));
}

function sendDotData() {
    let message = new Message(ACTION_DOT);
    let data = new Player();
    data.color = player.color;
    data.currY = player.currY;
    data.currX = player.currX;
    message.data = data;
    stompClient.send("/app/action", {}, JSON.stringify(message));
}

function drawDot(dotData) {
    ctx.beginPath();
    ctx.fillStyle = dotData.color;
    ctx.arc(dotData.currX + 1, dotData.currY + 1, player.lineWidth / 2, 0, 2 * Math.PI);
    ctx.fill();
}

function findxy(res, e) {
    if (res == 'down') {
        player.prevX = player.currX;
        player.prevY = player.currY;
        // player.currX = e.clientX - offsetX;
        // player.currY = e.clientY - offsetY;
        var rect = getMousePos(canvas, e);
        player.currX = rect.x;
        player.currY = rect.y;


        player.flag = true;
        player.dot_flag = true;
        if (player.dot_flag) {
            sendDotData();
            player.dot_flag = false;
        }
    }
    if (res == 'up' || res == "out") {
        player.flag = false;
        if (res == 'up') {
            //TODO: Storing dataurl is expensive... store every two points in array as user is drawing and then redraw canvas from them.
            history.push(canvas.toDataURL());
        }
    }
    if (res == 'move') {
        if (player.flag) {
            player.prevX = player.currX;
            player.prevY = player.currY;
            var rect = getMousePos(canvas, e);
            player.currX = rect.x;
            player.currY = rect.y;
            sendDrawData();
        }
    }
}


function getMousePos(canvas, evt) {
    // var canvasOffset = $("#can").offset();
    // var offsetX = canvasOffset.left;
    // var offsetY = canvasOffset.top;
    //
    // console.log(`OffsetX: ${offsetX} | OffsetY: ${offsetY}`);
    //
    // return {
    //     x: parseInt(evt.clientX - offsetX),
    //     y: parseInt(evt.clientY - offsetY)
    // }
    // // return {
    // //     x: parseInt(e.clientX - offsetX),
    // //     y: parseInt(e.clientY - offsetY)
    // // }


    var rect = canvas.getBoundingClientRect();
    return {
        x: (evt.clientX - rect.left),
        y: (evt.clientY - rect.top)
    };


    // Working
    // var rect = canvas.getBoundingClientRect();
    // return {
    //     x: (evt.clientX - rect.left) / (rect.right - rect.left) * canvas.width,
    //     y: (evt.clientY - rect.top) / (rect.bottom - rect.top) * canvas.height
    // };
}

// function setConnected(connected) {
//     $("#connect").prop("disabled", connected);
//     $("#disconnect").prop("disabled", !connected);
//     if (connected) {
//         $("#conversation").show();
//     }
//     else {
//         $("#conversation").hide();
//     }
//     $("#greetings").html("");
// }

function connect() {
    var socket = new SockJS('/drawing');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        // setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/action', function (message) {
            processWebsocketMessage(JSON.parse(message.body));
        });
    });
}

// function disconnect() {
//     if (stompClient !== null) {
//         stompClient.disconnect();
//     }
//     setConnected(false);
//     console.log("Disconnected");
// }

function undo() {
    console.debug(history);
    let lastHistory = history.pop();
    if (lastHistory === undefined) {
        return;
    }

    console.debug("Sending restore request to: " + lastHistory);
    let message = new Message(ACTION_UNDO);
    let data = {};
    data.lastHistory = lastHistory;
    message.data = data;
    stompClient.send("/app/action", {}, JSON.stringify(message));
}