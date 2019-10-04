<template>
    <v-container>
        <form class="form" action="/registration" method="post">
            <v-content>
                <v-layout column wrap>
                    <v-text-field
                            name="username"
                            autofocus solo type="text"
                            v-model="username"
                            :rules="[rules.nameRules, rules.nameLengthRules ]"
                            placeholder="Username"
                            @keyup.enter="submit"/>
                    <v-text-field
                            name="password"
                            solo type="password"
                            v-model="password"
                            counter
                            :rules="[rules.passwordLength]"
                            placeholder="Password"
                            @keyup.enter="submit"/>
                    <v-text-field
                            name="passwordConfirm"
                            solo type="password"
                            v-model="passwordConfirm"
                            counter
                            :rules="[rules.passwordLength, passwordConfirmationRule]"
                            placeholder="Confirm Password"
                            @keyup.enter="submit"/>
                    <v-text-field
                            name="email"
                            solo type="text"
                            v-model="email"
                            :rules="[rules.emailCheck, rules.emailRules]"
                            placeholder="email@example.com"
                            @keyup.enter="submit"/>
                </v-layout>
            </v-content>
            <v-content>
                <v-layout justify-center row wrap>
                    <v-btn type="submit">Sign Up</v-btn>
                    <v-spacer></v-spacer>
                </v-layout>
            </v-content>
        </form>
    </v-container>
</template>

<script>
    export default {
        name: "RegisterPage",
        data() {
            return {
                password: '',
                passwordConfirm: '',
                username: '',
                rules: {
                    passwordLength: v => (v && v.length >= 8) || 'Min 8 characters',
                    nameLengthRules: v => (v && v.length <= 10) || 'Name must be less than 10 characters',
                    nameRules: v => !!v || 'Enter a username',
                    emailRules: v => /.+@.+\..+/.test(v) || 'E-mail must be valid, email@example.com',
                    emailCheck: v => !!v || 'Enter a e-mail',
                }
            }
        },
        computed: {
            passwordConfirmationRule() {
                return () => (this.password === this.passwordConfirm) || 'Passwords must match'
            },
        }
    }
</script>

<style scoped>

</style>