'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var userContainer = document.querySelector('#user-container')

var stompClient = null;
var username = null;
var password = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

const onSubmit = (event) => {
    username = document.querySelector('#name').value.trim();
    password = document.querySelector('#password').value;
    event.preventDefault();
    console.log(password);
    fetch('/api/users/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({username: username, password: password})
    }).then(response => {
        fetch('/api/users/getAllOnlineUsers', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        }).then(response => {
            connect(event);

                return response.json();
            }
        ).then(response => {
            if(response) {
                console.log(response);

                response.forEach(user => {

                    const userElement = document.createElement('div');
                    userElement.classList.add('user');

                    // Create circle element for the first letter of the username
                    const circleElement = document.createElement('div');
                    circleElement.classList.add('circle');
                    circleElement.textContent = user.username.charAt(0).toUpperCase();
                    const onlineBadge = document.createElement('span');
                    onlineBadge.classList.add('online-circle');
                    userElement.appendChild(circleElement);

                    // Create span element for the username
                    const usernameElement = document.createElement('span');
                    usernameElement.textContent = user.username;
                    userElement.appendChild(usernameElement);

                    // Append the user element to the user container
                    userContainer.appendChild(userElement);
                    userElement.appendChild(onlineBadge);
                })
            }
        })
    })
}
function connect(event) {
    console.log('here');
    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}


function onConnected() {

    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);


    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, password: password, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', onSubmit, true)
messageForm.addEventListener('submit', sendMessage, true)
