<template>
  <PlayGround v-if=" $store.state.pk.status === 'playing'" />
</template>
<script>
import PlayGround from '../../components/PlayGround.vue'
import { useStore } from 'vuex';
import { onMounted, onUnmounted } from 'vue';

export default {
  components: {
    PlayGround
  },
  setup () {
    const store = useStore();
    const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}`;
    let socket = null;
    onMounted(() => {
      socket = new WebSocket(socketUrl);
      socket.onopen = () => {//如果连接成功，将socket存储到全局变量中
        console.log("connected连接成功");
        store.commit("updateSocket", socket);
      }
      socket.onmessage = msg => {
        const data = JSON.parse(msg.data);
        console.log(data);
      }
      socket.onclose = () => {
        console.log("disconnected");
      }
    });
    onUnmounted(() => {
      socket.close();
    })
  }
}
</script>

<style scoped>
</style>
