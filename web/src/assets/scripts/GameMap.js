import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall"
import { Snake } from './Snake'
export class GameMap extends AcGameObject {
    constructor(ctx, parent){
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;
        this.rows = 13;
        this.cols = 14;
        this.inner_walls_count = 20;//定义内部障碍物数量
        this.walls = [];//用于保存障碍物,属于对象数组
        this.snakes = [
            new Snake({id: 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
            new Snake({id: 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
        ];

    }

    //判断连通性
    //参考题目:蛇形矩阵
    check_connectivity(g, sx, sy, tx, ty){
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = true;

        let dx = [-1, 0, 1, 0];
        let dy = [0, 1, 0, -1];
        for(let i = 0; i < 4; i++){
            let x = sx + dx[i];
            let y = sy + dy[i];
            if(!g[x][y] && this.check_connectivity(g, x, y, tx, ty))
                return true;
        }
        return false;
    }
    //创建障碍物
    create_walls(){
        const g = [];//用bool数组来保存是否存在障碍物 初始置位false
        for(let r = 0; r < this.rows; r++){
            g[r] = [];
            for(let c = 0; c < this.cols; c++){
                g[r][c] = false;
            }
        }

        //给四周加上障碍物
        for(let r = 0; r < this.rows; r++){//给左右两侧设置为true
            g[r][0]=true;
            g[r][this.cols-1]=true;
        }

        for(let c = 0; c < this.cols; c++){//给上下两侧设置为true
            g[0][c] = g[this.rows-1][c] = true;
        }
        
        //在内部设置inner_walls_count个对称的障碍物
        for(let i = 0; i < this.inner_walls_count / 2; i++){
            for (let j = 0; j < 1000; j++) {
                let r = parseInt(Math.random()*this.rows);
                let c = parseInt(Math.random()*this.cols);
                if (g[r][c] || g[this.rows - 1 - r][this.cols - 1 - c]) continue;
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols-2)
                    continue;//保证左上角和右下角不能有障碍物
                
                //成功设置一个障碍物后 直接退出当前for i++
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true;
                break;
            }
        }
        //需要将状态g的副本传入check_connectivity
        //深度拷贝一个对象:先转换成json再将json解析出来 就一定是一个新的东西
        const copy_g = JSON.parse(JSON.stringify(g));

        //如果地图不连通
        //传入g以及起点和终点的横纵坐标
        if(!this.check_connectivity(copy_g,this.rows-2,1,1,this.cols-2)) 
            return false;

        //创建障碍物对象 并添加到this.walls数组
        for(let r = 0; r < this.rows; r++){
            for(let c = 0; c < this.cols; c++){
                if(g[r][c]){
                    this.walls.push(new Wall (r,c,this));
                }
            }
        }

        return true;//如果连通  
    }
    add_listening_events() {
        this.ctx.canvas.focus();

        const [snake0, snake1] = this.snakes;
        this.ctx.canvas.addEventListener("keydown", e => {
            if (e.key === 'w') snake0.set_direction(0);
            else if (e.key === 'd') snake0.set_direction(1);
            else if (e.key === 's') snake0.set_direction(2);
            else if (e.key === 'a') snake0.set_direction(3);
            else if (e.key === 'ArrowUp') snake1.set_direction(0);
            else if (e.key === 'ArrowRight') snake1.set_direction(1);
            else if (e.key === 'ArrowDown') snake1.set_direction(2);
            else if (e.key === 'ArrowLeft') snake1.set_direction(3);
        });
    }

    start(){
        for (let i = 0; i < 1000; i++) {
            if(this.create_walls())
            break;
        }
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