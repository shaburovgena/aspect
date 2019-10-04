import SockJS from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'


let stompClient = null
const handlers = []

export function connect() {
    const socket = new SockJS('/gs-guide-websocket')
    stompClient = Stomp.over(socket)
    stompClient.debug = () => {
    }
    stompClient.connect({}, frame => {

        stompClient.subscribe('/topic/activity', user => {
            console.log(frame)
            handlers.forEach(handler => handler(JSON.parse(user.body)))
        })
    })
}

export function addHandler(handler) {
    handlers.push(handler)
}

export function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect()
    }
    console.log("Disconnected")
}

export function sendUser(user) {
    stompClient.send("/app/changeUser", {}, JSON.stringify(user))
}