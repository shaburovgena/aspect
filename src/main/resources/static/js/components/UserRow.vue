<template>
        <v-card class="my-2">
            <v-card-text primary-title>
                <i>({{ user.id }})</i>
                {{ user.username }}
            </v-card-text>
<v-media v-if = "user.link" :user = "user"></v-media>
        <v-card-actions>
            <v-btn @click="edit" flat>
                <v-icon>edit</v-icon>
            </v-btn>
            <v-btn @click="del" flat>
                <v-icon>delete</v-icon>
            </v-btn>
        </v-card-actions>
        </v-card>
</template>

<script>
    import { mapActions} from 'vuex'
    export default {
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