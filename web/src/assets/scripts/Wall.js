//障碍物类
import { AcGameObject } from "./AcGameObject";

export class Wall extends AcGameObject{
    constructor(r, c, gamemap){
        super();//若继承基类，则必须要有这句

        this.r = r;//障碍物横坐标
        this.c = c;//障碍物纵坐标
        this.gamemap = gamemap;
        this.color = "#b47226";//障碍物颜色
    }

    update(){
        this.render();//每帧渲染一次
    }

    render() {
        const L = this.gamemap.L;//取出一个単位格的边长 动态取 因为L会动态变化
        const ctx = this.gamemap.ctx;//取出canvas的引用
        ctx.fillStyle = this.color;
        ctx.fillRect (this.c * L, this.r * L, L, L);
    }
}