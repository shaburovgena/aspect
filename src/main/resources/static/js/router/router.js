import Vue from 'vue'
import VueRouter from 'vue-router'
import UserList from 'pages/UserList.vue'
import Profile from 'pages/Profile.vue'

Vue.use(VueRouter)

const routes = [
//Порядок маппинга имеет значение
    {path: '/', component: UserList},
    {path: '/profile', component: Profile},
    {path: '*', component: UserList},
]

export default new VueRouter({
    mode: 'history',
    routes
})
