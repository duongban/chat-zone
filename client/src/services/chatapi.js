import Axios from "axios";

const api = Axios.create({
    baseURL: 'http://localhost:8085/api/',
});

const chatAPI = {
    getMessages: (groupId) => {
        console.log('Calling get messages from API');
        return api.get(`messages/${groupId}`);
    },

    sendMessage: (username, text) => {
        let msg = {
            sender: username,
            content: text
        }
        return api.post(`send`, msg);
    },

    register: (username, password) => {
        let msg = {
            username,
            password,
        }
        return api.post(`register`, msg);
    },

    login: (username, password) => {
        let msg = {
            username,
            password,
        }
        return api.post(`login`, msg);
    },

    createRoom: (name) => {
        let msg = {
            name,
        }
        return api.post(`room`, msg);
    }
}


export default chatAPI;
