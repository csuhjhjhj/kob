import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall"
import { Snake } from './Snake'
export class GameMap extends AcGameObject {
    constructor(ctx, parent,store){
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.store = store;
        this.L = 0;
        this.rows = 13;
        this.cols = 14;
        this.inner_walls_count = 10;//定义内部障碍物数量
        this.walls = [];//用于保存障碍物,属于对象数组
        this.snakes = [
            new Snake({id: 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
            new Snake({id: 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
        ];
    }

    //判断连通性
    
    //画地图：创建障碍物
    create_walls(){
        //直接将地图取出--后端传过来
        // console.log(this.store)
        const g = this.store.state.pk.gamemap;
        //创建障碍物对象 并添加到this.walls数组
        for(let r = 0; r < this.rows; r++){
            for(let c = 0; c < this.cols; c++){
                if(g[r][c]){
                    this.walls.push(new Wall (r,c,this));
                }
            }
        }
    }
    add_listening_events() {
        if(this.store.state.record.is_record) {
            let k = 0;
            const a_steps = this.store.state.record.a_steps;
            const b_steps = this.store.state.record.b_steps;
            const loser   = this.store.state.record.record_loser;
            const [snake0,snake1] = this.snakes;
            const interval_id = setInterval(() =>{
                console.log("输出a的步骤");
                console.log(a_steps);
                //将除了死亡操作每300ms渲染出来
                if(k >= a_steps.length-1) {
                    if(loser === "all" || loser === "A") {
                        snake0.status = "die";
                    }
                    if(loser === "all" || loser === "B") {
                        snake1.status = "die";
                    }
                    clearInterval(interval_id);
                }else {
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                }
                k++;
            },300);
            
        }else {
            this.ctx.canvas.focus();
            this.ctx.canvas.addEventListener("keydown", e => {
                let d = -1;
                if (e.key === 'w') d = 0;
                else if (e.key === 'd') d = 1;
                else if (e.key === 's') d = 2;
                else if (e.key === 'a') d = 3;
    
                if (d >= 0) {
                    this.store.state.pk.socket.send(JSON.stringify({
                        event: "move",
                        direction: d,
                    }));
                }
            });
        }
    }


    

    start(){
        this.create_walls();//不用循环1000次，因为直接接收的后端生成的
        this.add_listening_events();
    }
    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }
    check_ready() {  // 判断两条蛇是否都准备好下一回合了
        for (const snake of this.snakes) {
            if (snake.status !== "idle") return false;
            if (snake.direction === -1) return false;
        }
        return true;
    }
    check_valid(cell) {  // 检测目标位置是否合法：没有撞到两条蛇的身体和障碍物
        for (const wall of this.walls) {
            if (wall.r === cell.r && wall.c === cell.c)
                return false;
        }

        for (const snake of this.snakes) {
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) {  // 当蛇尾会前进的时候，蛇尾不要判断
                k -- ;
            }
            for (let i = 0; i < k; i ++ ) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
                    return false;
            }
        }

        return true;
    }
    next_step() {  // 让两条蛇进入下一回合
        for (const snake of this.snakes) {
            snake.next_step();
        }
    }

    update(){
        // 正方形的大小 自适应变化
        this.update_size();
        if (this.check_ready()) {
            this.next_step();
        }
        this.render();
    }
    render() {//渲染    
        const color_even = '#AAD751'//偶数颜色
        const color_odd = '#A2D149'//奇数颜色
        for(let r = 0 ; r < this.rows; r++){
            for(let c = 0; c < this.cols; c++){
                if(( r + c ) % 2 == 0){
                    this.ctx.fillStyle = color_even;
                }else{
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c* this.L, r* this.L, this.L, this.L);
            }
        }
    }
}