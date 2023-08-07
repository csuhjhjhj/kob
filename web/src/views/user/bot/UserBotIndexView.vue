<template>
  <div class="container">
    <div class="row">
      <div class="col-3">
        <div class="card">
          <div class="card-body">
            <img :src="$store.state.user.photo"
                 style="width:100%">
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card">
          <div class="card-header">
            <span style="font-size:130%">我的Bot</span>
            <!-- Button trigger modal -->
            <button type="button"
                    class="btn btn-primary float-end"
                    data-bs-toggle="modal"
                    data-bs-target="#add-bot-btn">
              创建
            </button>
            <!-- Modal -->
            <div class="modal fade"
                 id="add-bot-btn"
                 tabindex="-1"
                 aria-labelledby="exampleModalLabel"
                 aria-hidden="true">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <h1 class="modal-title fs-5"
                        id="exampleModalLabel">创建Bot</h1>
                    <button type="button"
                            class="btn-close"
                            data-bs-dismiss="modal"
                            aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                    <div class="mb-3">
                      <label for="add-bot-title"
                             class="form-label">名称</label>
                      <input v-model="botadd.title"
                             type="text"
                             class="form-control"
                             id="add-bot-title"
                             placeholder="请输入Bot名称">
                    </div>
                    <div class="mb-3">
                      <label for="add-bot-description"
                             class="form-label">简介</label>
                      <textarea v-model="botadd.description"
                                class="form-control"
                                id="add-bot-description"
                                rows="3"
                                placeholder="请输入Bot简介"></textarea>
                    </div>
                    <div class="mb-3">
                      <label for="add-bot-code"
                             class="form-label">代码</label>
                      <textarea v-model="botadd.content"
                                class="form-control"
                                id="add-bot-code"
                                rows="7"
                                placeholder="请编写Bot代码"></textarea>
                    </div>
                  </div>
                  <div class="modal-footer">
                    <button type="button"
                            class="btn btn-secondary"
                            @click="add_bot"
                            data-bs-dismiss="modal">创建</button>
                    <button type="button"
                            class="btn btn-primary">取消</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="card-body">
            <table class="table table-striped table-hover">
              <thead>
                <tr>
                  <th scope="col">名称</th>
                  <th scope="col">创建时间</th>
                  <th scope="col">操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="bot in bots"
                    :key="bot.id">
                  <td>{{ bot.title}}</td>
                  <td>{{ bot.createtime}}</td>
                  <td>
                    <!--Button trigger modal-->
                    <button type="button"
                            class="btn btn-secondary"
                            data-bs-toggle="modal"
                            :data-bs-target="'#update-bot-btn-' + bot.id">修改</button>
                    <button type="button"
                            class="btn btn-danger"
                            @click="remove_bot(bot)">删除</button>
                    <!--Modal-->
                    <!-- <div class="modal fade" :id="'update-bot-btn-' + bot.id" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                            <div class="modal-dialog">
                                            <div class="modal-content">
                                            <div class="modal-header">
                                                <h1 class="modal-title fs-5" id="exampleModalLabel">修改Bot</h1>
                                                <button type="button" class="btn-close"  data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">
                                                <div class="mb-3">
                                                    <label for="add-bot-title" class="form-label">名称</label>
                                                    <input v-model="botadd.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                                </div>
                                                <div class="mb-3">
                                                    <label for="add-bot-description" class="form-label">简介</label>
                                                    <textarea v-model="botadd.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot简介"></textarea>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="add-bot-code" class="form-label">代码</label>
                                                    <textarea v-model="botadd.content" class="form-control" id="add-bot-code" rows="7" placeholder="请编写Bot代码"></textarea>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary"  @click="update_bot(bot)" data-bs-dismiss="modal">保存修改</button>
                                                <button type="button" class="btn btn-primary">取消</button>
                                            </div>
                                            </div>
                                            </div>
                                            </div> -->
                    <div class="modal fade"
                         :id="'update-bot-btn-' + bot.id"
                         tabindex="-1">
                      <div class="modal-dialog modal-xl">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h5 class="modal-title">创建Bot</h5>
                            <button type="button"
                                    class="btn-close"
                                    data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                          </div>
                          <div class="modal-body">
                            <div class="mb-3">
                              <label for="add-bot-title"
                                     class="form-label">名称</label>
                              <input v-model="bot.title"
                                     type="text"
                                     class="form-control"
                                     id="add-bot-title"
                                     placeholder="请输入Bot名称" />
                            </div>
                            <div class="mb-3">
                              <label for="add-bot-description"
                                     class="form-label">简介</label>
                              <textarea v-model="bot.description"
                                        class="form-control"
                                        id="add-bot-description"
                                        rows="3"
                                        placeholder="请输入Bot简介"></textarea>
                            </div>
                            <div class="mb-3">
                              <label for="add-bot-code"
                                     class="form-label">代码</label>
                              <VAceEditor v-model:value="bot.content"
                                          @init="editorInit"
                                          lang="c_cpp"
                                          theme="textmate"
                                          style="height: 300px" />
                            </div>
                          </div>
                          <div class="modal-footer">
                            <div class="error-message">
                              {{ bot.error_message }}
                            </div>
                            <button type="button"
                                    class="btn btn-primary"
                                    @click="update_bot(bot)">
                              保存修改
                            </button>
                            <button type="button"
                                    class="btn btn-secondary"
                                    data-bs-dismiss="modal">
                              取消
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>

                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import ContentFieldVue from '../../../components/ContentField.vue'
import $ from 'jquery'
import { ref } from 'vue'
import { useStore } from 'vuex'
import { reactive } from 'vue'
import { Modal } from "bootstrap/dist/js/bootstrap"
import { VAceEditor } from 'vue3-ace-editor'
import ace from 'ace-builds'
export default {
  components: {
    // ContentFieldVue
    VAceEditor
  },
  setup () {
    const store = useStore();
    let bots = ref([]);//bots用于保存getlist返回的bot列表
    const refresh_bots = () => {//定义函数
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/getlist/",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          bots.value = resp;
          console.log("调用refresh_bots函数");
        },
        error (resp) {
          console.log(resp);
        }
      })
    }
    refresh_bots();//调用函数
    const botadd = reactive({
      title: "",
      description: "",
      content: "",
      error_message: "",
    });
    //定义函数-创建bot
    const add_bot = () => {
      botadd.error_message = "";
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/add/",
        type: "post",
        data: {
          title: botadd.title,
          description: botadd.description,
          content: botadd.content
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.error_message === "success") {
            botadd.title = "";
            botadd.description = "";
            botadd.content = "";
            Modal.getInstance("#add-bot-btn").hide();
            console.log(resp);
            refresh_bots();
          } else {
            botadd.error_message = resp.error_message;
          }
        },
        error (resp) {
          console.log(resp);
        }
      })
    };
    //定义函数-删除bot
    const remove_bot = (bot) => {
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/remove/",
        type: "post",
        data: {
          bot_id: bot.id
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.error_message === "success") {
            console.log("删除成功");
            refresh_bots();
          } else {
            alert(resp.error_message)
          }
        },
        error (resp) {
          console.log(resp);
        }
      })
    }
    //定义函数-修改bot
    const update_bot = (bot) => {
      bot.error_message = "";
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/update/",
        type: "post",
        data: {
          bot_id: bot.id,
          title: bot.title,
          description: bot.description,
          content: bot.content,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.error_message === "success") {
            Modal.getInstance('#update-bot-btn-' + bot.id).hide();
            alert("修改成功");
            refresh_bots();
          } else {
            bot.error_message = resp.error_message;
          }
        },
        error (resp) {
          console.log(resp);
        }
      })
      ace.config.set(
        "basePath",
        "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
    }
    return {
      bots,
      add_bot,
      botadd,
      remove_bot,
      update_bot,
      VAceEditor
    }
  }
}
</script>

<style scoped>
div.card {
  margin-top: 20px;
}
</style>
