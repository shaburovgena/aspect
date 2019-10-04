import Vue from 'vue'
import App from 'pages/App.vue'
import 'api/resource'
import router from 'router/router'
import 'vuetify/dist/vuetify.min.css'
import vuetify from './plugin/vuetify'
import {connect} from "./util/ws"
import store from 'store/store'

if (profile) {
    connect()
}

new Vue({
    el: '#app',
    vuetify,
    router,
    store,
    //Необходимо в компонент #app поместить отрендеренный App
    render: a => a(App),
});