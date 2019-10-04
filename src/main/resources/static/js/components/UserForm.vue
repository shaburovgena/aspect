<template>
    <v-layout row>
        <v-text-field label="New user" placeholder="Write username" v-model="username"/>
        <v-btn @click="save" flat>
            <v-icon>save</v-icon>
        </v-btn>
    </v-layout>
</template>

<script>
    import { mapActions } from 'vuex'
    export default {
        props: ['userAttr'],
        data() {
            return {
                username: '',
                id: ''
            }
        },
        watch: {
            userAttr(newVal, oldVal) {
                this.username = newVal.username
                this.id = newVal.id
            }
        },
        methods: {
            ...mapActions(['addUserAction', 'updateUserAction']),
            save() {
                const user = {
                    id: this.id,
                    username: this.username
                }
                if (this.id) {
                    this.updateUserAction(user)
                } else {
                    console.log(user.username)
                    this.addUserAction(user)
                }
                this.username = ''
                this.id = ''
            }
        }
    }
</script>

<style>
</style>