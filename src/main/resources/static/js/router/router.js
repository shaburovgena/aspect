import Vue from 'vue'
import VueRouter from 'vue-router'
import UserList from 'pages/UserList.vue'
import UserForm from 'components/UserForm.vue'
import Profile from 'pages/Profile.vue'

Vue.use(VueRouter)

const routes = [
//Порядок маппинга имеет значение
    {path: '/', component: UserList},
    {path: '/profile', component: Profile},
    {path: '/edit', component: UserForm},
    {path: '*', component: UserList},
]

export default new VueRouter({
    mode: 'history',
    routes
})
