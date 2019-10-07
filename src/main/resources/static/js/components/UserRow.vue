<template>
    <v-card class="pa-0">
        <v-row>
            <v-flex lg1 d-flex>
            <v-col >
                <v-avatar v-if="user.userpic" :size="`${size || 36}px`">
                <img :src="user.userpic" :alt="user.username">
                </v-avatar>
                <v-avatar v-else :size="`${size || 36}px`" color="indigo">
                <v-icon dark>{{accountCircle}}</v-icon>
                </v-avatar>
            </v-col>
            </v-flex>
            <v-flex lg1 d-flex>
                <v-col >
                    {{user.username}}
                </v-col>
            </v-flex>
            <v-flex lg2 d-flex>
            <v-col >
                {{user.email}}
            </v-col>
            </v-flex>
            <v-flex lg2 d-flex>
            <v-col >
                {{user.fullName}}
            </v-col>
            </v-flex>
            <v-flex lg2 d-flex>
            <v-col >
                {{user.address}}
            </v-col>
            </v-flex>
            <v-flex lg2 d-flex>
            <v-col >
                {{user.phone}}
            </v-col>
            </v-flex>
            <v-flex lg2 d-flex>
            <v-col>
                <v-row>

                    <v-btn @click="del">
                        <v-icon>{{deleteBtn}}</v-icon>
                    </v-btn>
                <v-btn @click="edit">
                        <v-icon>{{editBtn}}</v-icon>
                    </v-btn>
                </v-row>
            </v-col>
            </v-flex>
        </v-row>

    </v-card>
</template>

<script>
    import {mapActions} from 'vuex'
    import {mdiAccountEdit} from '@mdi/js'
    import {mdiDelete} from '@mdi/js'
    import {mdiAccountCircle} from '@mdi/js'

    export default {
        data() {
            return {
                editBtn: mdiAccountEdit,
                deleteBtn: mdiDelete,
                accountCircle: mdiAccountCircle
            }
        },
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