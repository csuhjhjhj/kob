export default ({
	state:{
			is_record:false,
			a_steps:"",
			b_steps:"",
			record_loser:"",//多个store文件的state数据名不能重复
	},
	mutations:{
			updateIsRecord(state,is_record) {
					state.is_record = is_record;
			},
			updateSteps(state,data) {
					state.a_steps = data.a_steps;
					state.b_steps = data.b_steps;
			},
			updateRecordLoser(state,loser) {
					state.record_loser = loser;
			}
	},
	actions:{

	},
	modules:{

	}
})
