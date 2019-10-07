<template>
        <v-card class="my-2">
            <v-card-text primary-title>
                <i>({{ user.id }})</i>
                {{ user.username }}
            </v-card-text>
<v-media v-if = "user.link" :user = "user"></v-media>
        <v-card-actions>
            <v-btn @click="edit">
                <v-icon>{{editBtn}}</v-icon>
            </v-btn>
            <v-btn @click="del" >
                <v-icon>{{deleteBtn}}</v-icon>
            </v-btn>
        </v-card-actions>
        </v-card>
</template>

<script>
    import { mapActions} from 'vuex'
    import {mdiAccountEdit} from '@mdi/js'
    import {mdiDelete} from '@mdi/js'
    export default {
        data() {
            return {
                editBtn:mdiAccountEdit,
                deleteBtn:mdiDelete
            }},
        props: ['user', 'editUser', 'deleteUser'],
        methods: {
            ...mapActions(['removeUserAction']),
            edit() {
                this.editUser(this.user)
            },
            del() {
                this.removeUserAction(this.user)
            }
        }
    }
</script>

<style>
</style>