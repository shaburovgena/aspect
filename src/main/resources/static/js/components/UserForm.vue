<template>
        <v-container @keyup.enter="save">
            <v-text-field solo placeholder="Write username"
                          v-model="username"
                          :rules="[rules.nameRules, rules.nameLengthRules ]"></v-text-field>
            <v-text-field solo placeholder="Write password"
                          type="password" v-model="password"
                          :rules="[rules.passwordLength]" counter></v-text-field>
            <v-text-field solo placeholder="Confirm password"
                          type="password" v-model="passwordConfirm"
                          :rules="[rules.passwordLength, passwordConfirmationRule]" counter></v-text-field>
            <v-text-field solo placeholder="Write email"
                          v-model="email"
                          :rules="[rules.emailCheck, rules.emailRules]"></v-text-field>
            <v-text-field solo placeholder="Write full name"
                          v-model="fullName"></v-text-field>
            <v-text-field solo placeholder="Write phone" v-model="phone"></v-text-field>
            <v-text-field  solo placeholder="Write address" v-model="address"></v-text-field>
            <v-btn @click="save" :disabled="!this.isValid">
                <v-icon>{{saveBtn}}</v-icon>
            </v-btn>
        </v-container>

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
                saveBtn: mdiContentSave,
                rules: {
                    passwordLength: v => (v && v.length >= 8) || 'Min 8 characters',
                    nameLengthRules: v => (v && v.length <= 10) || 'Name must be less than 10 characters',
                    nameRules: v => !!v || 'Enter username',
                    emailRules: v => /.+@.+\..+/.test(v) || 'E-mail must be valid, email@example.com',
                    emailCheck: v => !!v || 'Enter e-mail',
                }
            }
        },computed: {
            passwordConfirmationRule() {
                return () => (this.password === this.passwordConfirm) || 'Passwords must match'
            },
            isValid(){
                if(this.isUsernameValid){
                    return true;
                }else {
                    return false;
                }
            },
            isUsernameValid(){
                if(this.username.length<=10&&this.username.length>0)return true
            },
        // isPasswordValid(){
        //         if(this.password.length>7&&this.password === this.passwordConfirm)return true
        //     }
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