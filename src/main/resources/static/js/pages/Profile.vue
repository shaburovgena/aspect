<template>
    <v-container>
        <v-layout justify-space-around>
            <v-flex :xs6="!$vuetify.breakpoint.xsOnly">
                <div class="title mb-3">User profile</div>
                <v-layout row justify-space-between>
                    <v-flex>
                    <v-img :src="profile.userpic"></v-img>
                    </v-flex>
                    <v-flex class="px-1">
                        <v-layout column>
                            <span>{{profile.username}}</span>
                            <span>{{profile.email}}</span>
                            <span>{{profile.fullName}}</span>
                            <span>{{profile.address}}</span>
                            <span>{{profile.phone}}</span>
                        </v-layout>
                    </v-flex>
                </v-layout>
            </v-flex>
        </v-layout>

    </v-container>
</template>

<script>
    import {mapState} from 'vuex'

    export default {
        name: "Profile",
        computed: mapState(['profile'])
    }
</script>

<style scoped>
    img {
        max-width: 100%;
        height: auto;
    }
</style>