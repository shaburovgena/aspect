<template>
    <v-app>
        <v-app-bar app>
            <v-btn v-if="profile" icon :disabled="$route.path === '/'" @click="showUsers">
                <v-icon>{{homeBtn}}</v-icon>
            </v-btn>
            <v-spacer></v-spacer>
            <v-toolbar-title>Aspect</v-toolbar-title>
            <v-btn v-if="profile" icon href="/">
                <v-icon>{{refreshBtn}}</v-icon>
            </v-btn>
            <v-spacer></v-spacer>
            <div v-if="profile">
                <v-btn  icon :disabled="$route.path === '/profile'" @click="showProfile">
                    {{profile.username}}
                </v-btn>
                <v-btn icon href="/logout">
                    <v-icon>{{logoutBtn}}</v-icon>
                </v-btn>
            </div>
            <div v-else>
                <v-btn icon @click="loginPage">
                    <v-icon>{{loginBtn}}</v-icon>
                </v-btn>
                <v-btn icon @click="registerPage">
                    <v-icon>{{registerBtn}}</v-icon>
                </v-btn>
            </div>
        </v-app-bar>

        <v-content v-if="!profile">
            <v-container grid-list-lg mt-12>
                <v-layout align-start justify-end row wrap>
                    <v-flex lg3 d-flex v-if="isLogin">
                        <login-page/>
                    </v-flex>
                    <v-flex lg3 d-flex v-if="isRegister">
                        <register-page/>
                    </v-flex>

                </v-layout>
            </v-container>
        </v-content>
        <v-content v-if="profile">
            <router-view></router-view>
        </v-content>
    </v-app>
</template>

<script>
    import {mdiLogout} from '@mdi/js'
    import {mdiLogin} from '@mdi/js'
    import {mdiHome} from '@mdi/js'
    import {mdiRefresh} from '@mdi/js'
    import {mdiAccountPlusOutline} from '@mdi/js'
    import LoginPage from '../components/LoginPage.vue'
    import RegisterPage from '../components/RegisterPage.vue'
    import {mapMutations, mapState} from "vuex";
    import {addHandler} from 'util/ws'

    export default {
        computed: mapState(['profile']),
        components: {LoginPage, RegisterPage},
        data() {
            return {
                // profile: profile,

                logoutBtn: mdiLogout,
                loginBtn: mdiLogin,
                registerBtn: mdiAccountPlusOutline,
                homeBtn: mdiHome,
                refreshBtn: mdiRefresh,
                isLogin: frontendData.isLoginForm,
                isRegister: frontendData.isRegisterForm,
            }
        },
        methods: {
            ...mapMutations(['addUserMutation', 'updateUserMutation', 'removeUserMutation']),
            showUsers() {
                this.$router.push('/')
            },
            showProfile() {
                this.$router.push('/profile')
            },
            loginPage: function () {
                if (!this.isLogin) {
                    this.isLogin = true
                    this.isRegister = false
                } else {
                    this.isLogin = false
                }
            },
            registerPage: function () {
                if (!this.isRegister) {
                    this.isRegister = true
                    this.isLogin = false
                } else {
                    this.isRegister = false
                }
            }


        },
        created() {

            addHandler(data => {
                if (data.objectType === 'USER') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.addUserMutation(data.body)
                            break
                        case 'UPDATE':
                            this.updateUserMutation(data.body)
                            break
                        case 'REMOVE':
                            this.removeUserMutation(data.body)
                            break
                        default://Интерполяция js вставит в вывод консоли тип события
                            console.error('Looks like the event type if unknown "${data.eventType}"')
                    }

                } else {
                    console.error('Looks like the object type if unknown "${data.objectType}"')
                }
            })
        },
        beforeMount() {
            if (!this.profile) {
                this.$router.replace('/')
            }
        }
    }
</script>

<style>

</style>