import Vue from 'vue'
import Vuex from 'vuex'
import usersApi from 'api/users'
import 'core-js/stable'
import 'regenerator-runtime/runtime'

Vue.use(Vuex)
export default new Vuex.Store({

    state: {
        users: users,
        profile: profile
    },
    getters: {
        sortedUsers: state =>
            (state.users || []).sort((a, b) => -(a.id - b.id))

    },
    mutations: {
        addUserMutation(state, user) {
            state.users = [
                ...state.users,
                user
            ]
        },
        updateUserMutation(state, user) {
            const updateIndex = state.users.findIndex(item => item.id === user.id)
            state.users = [
                ...state.users.slice(0, updateIndex),
                user,
                ...state.users.slice(updateIndex + 1)
            ]
        },
        removeUserMutation(state, user) {
            const deletionIndex = state.users.findIndex(item => item.id === user.id)
            if (deletionIndex > -1) {
                state.users = [
                    ...state.users.slice(0, deletionIndex),
                    ...state.users.slice(deletionIndex + 1)
                ]
            }
        }
    },
    actions: {
        async  addUserAction({commit}, user) {
            const result = await usersApi.add(user)
            const data = await result.json()
            const index = this.state.users.findIndex(item => item.id === data.id)
            if (index > -1) {
                commit('updateUserMutation', data)
            } else {
                commit('addUserMutation', data)
            }

        },
        async  updateUserAction({commit}, user) {
            const result =  await usersApi.update(user)
            const data = await result.json()
            commit('updateUserMutation', data)

        },
         async removeUserAction({commit}, user) {
            const result = await usersApi.remove(user.id)
            if (result.ok) {
                commit('removeUserMutation', user)
            }
        }
    }
})