import Axios from "axios";

const api = Axios.create({
    baseURL: 'http://localhost:8085/api/',
});

const chatHttpApi = {
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

    createRoom: (name, session) => {
        let msg = {
            name,
        }
        return api.post(`room`, msg, {
            headers: {
                Session: session,
            }
        });
    },

    findRoom: (code, session) => {
        return api.get(`room/${code}`, {
            headers: {
                Session: session,
            }
        });
    },

    getRoom: (session) => {
        return api.get(`rooms`, {
            headers: {
                Session: session,
            }
        });
    }
}


export default chatHttpApi;
