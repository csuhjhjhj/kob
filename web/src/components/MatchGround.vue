<template>
  <div class="matchground">
    <div class="row">
      <div class="col-6">
        <div class="user_photo">
          <img :src="$store.state.user.photo"
               alt="">
        </div>
        <div class="user_username">
          {{ $store.state.user.username }}
        </div>
      </div>
      <div class="col-6">
        <div class="user_photo">
          <img :src="$store.state.pk.opponent_photo"
               alt="">
        </div>
        <div class="user_username">
          {{ $store.state.pk.opponent_username }}
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-12"
           style="text-align:center; padding-top:12vh">
        <button @click="click_match_btn"
                class="btn btn-success btn-lg">{{match_btn_info}}</button>
      </div>
    </div>
  </div>
</template>
<script>
import { ref } from 'vue'
import { useStore } from 'vuex';
export default {
  setup () {
    const store = useStore();
    let match_btn_info = ref("开始匹配");
    const click_match_btn = () => {
      if (match_btn_info.value === "开始匹配") {
        match_btn_info.value = "取消";
        //JSON.stringify将JSON转换为字符串
        store.state.pk.socket.sent(JSON.stringify({
          event: "stop-matching",
        }));
      } else {
        match_btn_info.value = "开始匹配";
        store.state.pk.socket.sent(JSON.stringify({
          event: "stop-matching",
        }))
      }
    };
    return {
      match_btn_info,
      click_match_btn,
    }
  }
}
</script>