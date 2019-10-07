<template>
    <v-content>
        <v-container @keyup.enter="save">
            <v-text-field placeholder="Write username" v-model="username"></v-text-field>
            <v-text-field placeholder="Write full name" v-model="fullName"></v-text-field>
            <v-text-field placeholder="Write password" type="password" v-model="password"></v-text-field>
            <v-text-field placeholder="Confirm password" type="password" v-model="passwordConfirm"></v-text-field>
            <v-text-field placeholder="Write email" v-model="email"></v-text-field>
            <v-text-field placeholder="Write phone" v-model="phone"></v-text-field>
            <v-text-field placeholder="Write address" v-model="address"></v-text-field>
            <v-btn @click="save">
                <v-icon>{{saveBtn}}</v-icon>
            </v-btn>
        </v-container>

    </v-content>
</template>

<script>
    import {mapActions} from 'vuex'
    import {mdiContentSave} from '@mdi/js'

    export default {
        props: ['userAttr'],
        data() {
            return {
                username: '',
                fullName: '',
                id: '',
                password: '',
                passwordConfirm: '',
                email: '',
                address: '',
                phone: '',
                saveBtn: mdiContentSave
            }
        },
        watch: {
            userAttr(newVal) {
                this.username = newVal.username
                this.fullName = newVal.fullName
                this.id = newVal.id
                this.password = newVal.password
                this.passwordConfirm = newVal.passwordConfirm
                this.email = newVal.email
                this.address = newVal.address
                this.phone = newVal.phone
            }
        },
        methods: {
            ...mapActions(['addUserAction', 'updateUserAction']),
            save() {
                const user = {
                    id: this.id,
                    username: this.username,
                    fullName: this.fullName,
                    password: this.password,
                    passwordConfirm: this.passwordConfirm,
                    email: this.email,
                    address: this.address,
                    phone: this.phone
                }
                if (this.id) {
                    this.updateUserAction(user)
                } else {
                    this.addUserAction(user)
                }
                this.username = ''
                this.fullName = ''
                this.id = ''
                this.password = ''
                this.passwordConfirm = ''
                this.email = ''
                this.address = ''
                this.phone = ''
            }
        }
    }
</script>

<style>
</style>