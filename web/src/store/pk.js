export default ({ 
	state:{
		status:"matching",//matching表示匹配界面 playing表示对战界面
		socket:null,//存储前后端建立的connection
		opponent_username:"",//对手名
		opponent_photo:"",//对手头像
		gamemap:null
	},
	mutations:{
		updateSocket(state,socket){
			state.socket = socket;
		},
		updateOpponent(state,opponent){
			state.opponent_username = opponent.username;
			state.opponent_photo = opponent.photo;
		},
		updateStatus(state,status){
			state.status = status;
		},
		updateGamemap(state,gamemap){
			state.gamemap = gamemap;
		}
	},
	actions:{

	},
	modules:{

	}
})