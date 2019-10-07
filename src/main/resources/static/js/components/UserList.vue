<template>
    <v-content>
        <v-container  align-space-around justify-start column>
        <user-form :userAttr="user" />
        <user-row v-for="user in sortedUsers"
                     :key="user.id"
                     :user="user"
                     :editUser="editUser" />
        </v-container>
    </v-content>
</template>

<script>
    import {mapGetters} from 'vuex'
    import UserRow from 'components/UserRow.vue'
    import UserForm from 'components/UserForm.vue'


    export default {

        components: {
            UserRow,
            UserForm
        },
        data() {
            return {
                user: null
            }
        },
        computed: mapGetters(['sortedUsers']),
        methods: {
            editUser(user) {
                this.user = user
            }

        }
    }
</script>

<style>

</style>