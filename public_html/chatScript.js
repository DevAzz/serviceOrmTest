var ws;
var username = "${username}";
var arrUsers = [];
var currentUser;

window.onbeforeunload = function () {
    exit();
};

window.addEventListener('load', function (event) {
    var textArea = document.querySelectorAll(".messageInputTextArea");
    [].forEach.call(textArea, function (el) {
        el.addEventListener('keydown', function (e) {
            if ((e.keyCode === 10 || e.keyCode === 13) && e.ctrlKey) {
                sendMessage();
            } else if (e.keyCode === 9) { // tab was pressed
                // get caret position/selection
                var start = this.selectionStart;
                var end = this.selectionEnd;

                var target = e.target;
                var value = target.value;

                // set textarea value to: text before caret + tab + text after caret
                target.value = value.substring(0, start)
                    + "\t"
                    + value.substring(end);

                // put caret at right position again (add one for the tab)
                this.selectionStart = this.selectionEnd = start + 1;

                // prevent the focus lose
                e.preventDefault();
            }
        });
    });
});

function getMessages() {
    var getMessagesRequest = new XMLHttpRequest();
    getMessagesRequest.open("GET", "/chatMessages/allMessages", true);
    getMessagesRequest.onload = function () {
        var messages = parseJson(getMessagesRequest.responseText);
        var chatBox = document.getElementById('chatbox');
        chatBox.innerHTML = '';
        messages.forEach(function (item, i, arr) {
            var now = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
            var messageHead = now + "\n";
            showMessage(item.text, messageHead, item.author.login);
        });

    };
    getMessagesRequest.send(null);
}

function init() {
    ws = new WebSocket("ws://127.0.0.1:80/chat");
    ws.onopen = function (event) {
        var getCurrentUserRequest = new XMLHttpRequest();
        getCurrentUserRequest.open("GET", "/account/allUsers", true);
        getCurrentUserRequest.onload = function () {
            arrUsers = parseJson(getCurrentUserRequest.responseText);
            var usersBlock = document.getElementById("users");
            usersBlock.innerHTML = '';
            arrUsers.forEach(function (item, i, arr) {
                if (username !== item.login) {
                    currentUser = item;
                    showUser(item.login, item.online);
                }
            });
        };
        getCurrentUserRequest.send(null);
        getMessages();
    };
    ws.onmessage = function (event) {
        var now = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
        var messageHead = now + "\n";
        if (event.data.indexOf('USER_LIST') + 1) {
            var changeUsers = parseJson(event.data.substring(9, event.data.length));
            changeUsers.forEach(function (item, i, arr) {
                if (username !== item.login) {
                    if (containsUser(item)) {
                        arrUsers[getUserIndex(item)] = item;
                    } else {
                        arrUsers.push(item);
                    }
                }
            });
            var usersBlock = document.getElementById("users");
            usersBlock.innerHTML = '';
            arrUsers.forEach(function (item, i, arr) {
                if (username !== item.login) {
                    showUser(item.login, item.online);
                }
            });
        } else {
            showMessage(event.data.substring(event.data.indexOf(':') + 1),
                messageHead, event.data.substring(0, event.data.indexOf(':')));
        }

    };
    ws.onclose = function (event) {

    }
}

function exit() {
    var postUserOnline = new XMLHttpRequest();
    postUserOnline.open("POST", "/account/exitUser", true);
    postUserOnline.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    postUserOnline.send(username);
}

function containsUser(user) {
    var result = false;
    var BreakException = {};
    try {
        arrUsers.forEach(function (item, i, arr) {
            if (item.login === user.login) {
                result = true;
                throw BreakException;
            }
        });
    } catch (e) {
        if (e !== BreakException) throw e;
    }
    return result;
}

function getUserIndex(user) {
    var result = -1;
    var BreakException = {};
    try {
        arrUsers.forEach(function (item, i, arr) {
            if (item.login === user.login) {
                result = arrUsers.indexOf(item);
                throw BreakException;
            }
        });
    } catch (e) {
        if (e !== BreakException) throw e;
    }
    return result;
}

function parseJson(json) {
    var string = json.replace(/\\n/g, "\\n")
        .replace(/\\'/g, "\\'")
        .replace(/\\"/g, '\\"')
        .replace(/\\&/g, "\\&")
        .replace(/\\r/g, "\\r")
        .replace(/\\t/g, "\\t")
        .replace(/\\b/g, "\\b")
        .replace(/\\f/g, "\\f");
    string = json.replace(/[\u0000-\u0019]+/g, "");
    return JSON.parse(string);
}

function showUser(userName, userOnline) {
    var usersBlock = document.getElementById("users");
    var userBox = document.createElement('div');
    userBox.classList.add('usersBox');

    var userBoxName = document.createElement('div');
    userBoxName.classList.add("usersBoxName");
    userBoxName.appendChild(document.createTextNode(userName));

    var userBoxOnline = document.createElement('div');
    userBoxOnline.classList.add("usersBoxOnline");
    userBoxOnline.appendChild(document.createTextNode(userOnline));

    userBox.append(userBoxName, userBoxOnline);
    usersBlock.appendChild(userBox);

}

function sendMessage() {
    var messageField = document.getElementById("message");
    if (0 !== messageField.value.length) {
        var message = messageField.value;
        ws.send(message);
        messageField.value = '';
    }
    return false;
}

function showMessage(message, messageHeadValue, username) {
    var messageBox = document.createElement('div');
    messageBox.classList.add('messageBox');

    var messageHead = document.createElement('div');
    messageHead.classList.add('messageHead');

    var messageHeadTime = document.createElement('div');
    messageHeadTime.classList.add('messageHeadTime');
    messageHeadTime.appendChild(document.createTextNode(messageHeadValue));

    var messageUser = document.createElement('div');
    messageUser.classList.add('messageHeadUserName');
    messageUser.appendChild(document.createTextNode(username));

    messageHead.append(messageHeadTime, messageUser);

    messageBox.appendChild(messageHead);

    var messageElem = document.createElement('div');
    messageElem.classList.add('messageValue');
    var pre = document.createElement('pre');
    pre.classList.add('formatBox');
    pre.appendChild(document.createTextNode(message));
    messageElem.appendChild(pre);

    messageBox.appendChild(messageElem);

    document.getElementById('chatbox').appendChild(messageBox);

    messageBox.scrollIntoView();
}