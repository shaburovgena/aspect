<template>
    <v-content>
        <v-container  column>
            <v-container grid-list-lg>
                <v-layout align-start justify-end row wrap>
                    <v-flex lg9 d-flex>
                        <v-container align-start justify-start column>
                            <user-row v-for="user in sortedUsers"

                                      :key="user.id"
                                      :user="user"
                                      :editUser="editUser"/>
                        </v-container>
                    </v-flex>
                    <v-spacer></v-spacer>
                    <v-flex lg3 d-flex>
                        <v-container align-start justify-start row>
                        <user-form :userAttr="user"/>
                        </v-container>
                    </v-flex>

                </v-layout>
            </v-container>
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
            },


        }
    }
</script>

<style>

</style>