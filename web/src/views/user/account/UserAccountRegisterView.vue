<template>
    <ContentFieldVue>
        <div class="row  justify-content-md-center">
            <div class="col-3">
                <!-- submit时触发login函数，并阻止默认行为 -->
                <form @submit.prevent="register">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <!-- 使用v-model与定义的变量username绑定 -->
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <!-- 使用v-mdoel与定义变量的password绑定 -->
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <div class="mb-3">
                        <label for="confirmedPassword" class="form-label">确认密码</label>
                        <!-- 使用v-mdoel与定义变量的password绑定 -->
                        <input v-model="confirmedPassword" type="password" class="form-control" id="confirmedPassword" placeholder="请再次输入密码">
                    </div>
                    <!-- error_message显示变量error-message的值 -->
                    <div class="error-message">{{error_message}}</div>
                    <button type="submit" class="btn btn-primary">注册</button>
                </form>
            </div>
        </div>
    </ContentFieldVue>
</template>
<script>
import ContentFieldVue from '../../../components/ContentField.vue'
import router from '../../../router';
import { ref } from 'vue';//所有变量的定义借助ref表示
import $ from 'jquery'
export default {
    components:{
        ContentFieldVue
    },
    setup(){
        
        let username = ref("");//定义username 初始为空
        let password = ref("");//定义password
        let confirmedPassword = ref("");
        let error_message = ref("");//表示是否成功登录

        const register = () => {//定义loginh函数，当页面提交时触发
            error_message.value = ""
            $.ajax({
                url:"http://localhost:3000/user/account/register/",
                type:"post",
                data:{
                    username:username.value,
                    password:password.value,
                    confirmedPassword:confirmedPassword.value
                },
                success(resp){
                    if(resp.error_message === "success"){
                       router.push({name:"user_account_login"});
                    }else{
                        console.log(resp.error_message);
                        error_message.value = resp.error_message;//如果注册失败，显示错误信息
                    }
                }
            })
        }
        return{
            username,
            password,
            confirmedPassword,
            error_message,
            register,
        }
    }
}
</script>
<style scoped>

</style>