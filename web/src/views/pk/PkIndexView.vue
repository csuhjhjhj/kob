<template>
  <PlayGround v-if="$store.state.pk.status === 'playing'" />
  <MatchGround v-if="$store.state.pk.status === 'matching'" />
</template>
<script>
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchGround.vue'
import { onMounted } from 'vue'
import { onUnmounted } from 'vue'
import { useStore } from 'vuex'
export default {
  components: {
    PlayGround,
    MatchGround
  },
  setup () {
    const store = useStore();
    const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}`;
    let socket = null;
    onMounted(() => {
      store.commit("updateOpponent", {
        username: "我的对手",
        photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
      });
      socket = new WebSocket(socketUrl);
      socket.onopen = () => {//如果连接成功，将socket存储到全局变量中
        console.log("connected!");
        store.commit("updateSocket", socket);
      }
      socket.onmessage = msg => {
        const data = JSON.parse(msg.data);
        console.log(msg);
        console.log(data);
        if (data.event === "start-matching") {
          store.commit("updateOpponent", {
            username: data.opponent_username,
            photo: data.opponent_photo
          });
          // store.commit("updateStatus","playing")
          setTimeout(() => {
            store.commit("updateStatus", "playing")
          }, 200);
          console.log("输出地图")
          console.log(data.game)
          store.commit("updateGame", data.game)//更新地图
        } else if (data.event === "move") {
          console.log(data);
          console.log("move");
          const game = store.state.pk.gameObject;
          const [snack0, snack1] = game.snakes;
          snack0.set_direction(data.a_direction);
          snack1.set_direction(data.b_direction);
        } else if (data.event === "result") {
          console.log(data);
        }
      }
      socket.onclose = () => {
        console.log("disconnected!");
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
