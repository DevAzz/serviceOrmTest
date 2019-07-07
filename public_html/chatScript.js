let ws;
let username = document.getElementById("chatScript").src.toLowerCase();
let arrUsers = [];
let receiver;
let currentUser;

window.onbeforeunload = function () {
    exit();
};

window.addEventListener('load', function (event) {
    let textArea = document.querySelectorAll(".messageInputTextArea");
    [].forEach.call(textArea, function (el) {
        el.addEventListener('keydown', function (e) {
            if ((e.keyCode === 10 || e.keyCode === 13) && e.ctrlKey) {
                sendMessage();
            } else if (e.keyCode === 9) { // tab was pressed
                // get caret position/selection
                let start = this.selectionStart;
                let end = this.selectionEnd;

                let target = e.target;
                let value = target.value;

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

function getAllMessages() {
    receiver = undefined;
    let getMessagesRequest = new XMLHttpRequest();
    getMessagesRequest.open("GET", "/chatMessages/allMessages", true);
    getMessagesRequest.onload = function () {
        let messages = parseJson(getMessagesRequest.responseText);
        let chatBox = document.getElementById('chatbox');
        chatBox.innerHTML = '';
        messages.forEach(function (item, i, arr) {
            let now = moment(item.date).format('DD-MM-YYYY HH:mm:ss');
            let messageHead = now + "\n";
            showMessage(item.text, messageHead, item.author.login);
        });

    };
    getMessagesRequest.send(null);
}

function getMessagesByUserId(authorId, receiverId) {
    receiver = receiverId;
    let getMessagesRequest = new XMLHttpRequest();
    getMessagesRequest.open("GET", "/chatMessages/messagesByUserId/" + authorId + "/" + receiverId, true);
    getMessagesRequest.onload = function () {
        let messages = parseJson(getMessagesRequest.responseText);
        let chatBox = document.getElementById('chatbox');
        chatBox.innerHTML = '';
        messages.forEach(function (item, i, arr) {
            let now = moment(item.date).format('DD-MM-YYYY HH:mm:ss');
            let messageHead = now + "\n";
            showMessage(item.text, messageHead, item.author.login);
        });

    };
    getMessagesRequest.send(null);
}

function init() {
    ws = new WebSocket("ws://127.0.0.1:80/chat");
    ws.onopen = function (event) {
        username = username.substring(username.indexOf('=') + 1);
        let getCurrentUserRequest = new XMLHttpRequest();
        getCurrentUserRequest.open("GET", "/account/allUsers", true);
        getCurrentUserRequest.onload = function () {
            arrUsers = parseJson(getCurrentUserRequest.responseText);
            let usersBlock = document.getElementById("users");
            usersBlock.innerHTML = '';
            showCommonChatElement();
            arrUsers.forEach(function (item, i, arr) {
                if (username !== item.login) {
                    showUser(item);
                } else {
                    currentUser = item;
                }
            });
        };
        getCurrentUserRequest.send(null);
        getAllMessages();
    };
    ws.onmessage = function (event) {
        let now = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
        let messageHead = now + "\n";
        if (event.data.indexOf('USER_LIST') + 1) {
            let changeUsers = parseJson(event.data.substring(9, event.data.length));
            changeUsers.forEach(function (item, i, arr) {
                if (item !== null && username !== item.login) {
                    if (containsUser(item)) {
                        arrUsers[getUserIndex(item)] = item;
                    } else {
                        arrUsers.push(item);
                    }
                }
            });
            let usersBlock = document.getElementById("users");
            usersBlock.innerHTML = '';
            showCommonChatElement();
            arrUsers.forEach(function (item, i, arr) {
                if (item !== null && username !== item.login) {
                    showUser(item);
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
    let postUserOnline = new XMLHttpRequest();
    postUserOnline.open("POST", "/account/exitUser", true);
    postUserOnline.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    postUserOnline.send(username);
}

function containsUser(user) {
    let result = false;
    let BreakException = {};
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
    let result = -1;
    let BreakException = {};
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
    let string = json.replace(/\\n/g, "\\n")
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

function showUser(user) {
    let usersBlock = document.getElementById("users");
    let userBox = document.createElement('div');
    userBox.classList.add('usersBox');

    let userBoxName = document.createElement('div');
    userBoxName.classList.add("usersBoxName");
    userBoxName.appendChild(document.createTextNode(user.login));

    let userBoxOnline = document.createElement('div');
    userBoxOnline.classList.add("usersBoxOnline");
    userBoxOnline.appendChild(document.createTextNode(user.online));

    userBox.append(userBoxName, userBoxOnline);
    userBox.onclick = function () {
        return getMessagesByUserId(currentUser.id, user.id);
    };
    usersBlock.appendChild(userBox);

}

function showCommonChatElement() {
    let usersBlock = document.getElementById("users");
    let userBox = document.createElement('div');
    userBox.classList.add('usersBox');

    let commonChatBoxName = document.createElement('div');
    commonChatBoxName.classList.add("commonChatBoxName");
    commonChatBoxName.appendChild(document.createTextNode("Общий чат"));

    userBox.append(commonChatBoxName);
    userBox.onclick = getAllMessages;
    usersBlock.appendChild(userBox);

}

function sendMessage() {
    let messageField = document.getElementById("message");
    if (0 !== messageField.value.length) {
        let message = messageField.value;
        if (receiver !== undefined) {
            message = "receiver=" + receiver + ":" + messageField.value;
        }
        ws.send(message);
        messageField.value = '';
    }
    return false;
}

function showMessage(message, messageHeadValue, username) {
    let messageBox = document.createElement('div');
    messageBox.classList.add('messageBox');

    let messageHead = document.createElement('div');
    messageHead.classList.add('messageHead');

    let messageHeadTime = document.createElement('div');
    messageHeadTime.classList.add('messageHeadTime');
    messageHeadTime.appendChild(document.createTextNode(messageHeadValue));

    let messageUser = document.createElement('div');
    messageUser.classList.add('messageHeadUserName');
    messageUser.appendChild(document.createTextNode(username));

    messageHead.append(messageHeadTime, messageUser);

    messageBox.appendChild(messageHead);

    let messageElem = document.createElement('div');
    messageElem.classList.add('messageValue');
    let pre = document.createElement('pre');
    pre.classList.add('formatBox');
    pre.appendChild(document.createTextNode(message));
    messageElem.appendChild(pre);

    messageBox.appendChild(messageElem);

    document.getElementById('chatbox').appendChild(messageBox);

    messageBox.scrollIntoView();
}