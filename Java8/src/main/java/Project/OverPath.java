package Project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

import static Project.Mouse.*;

public class OverPath extends PathFinding{
    //遍历地图，采用深度度优先算法遍历
    private int total;//表示地图中可以通行的格子数量
    private int count;//表示已经走过的格子数量
    public OverPath(int[][]map,int x1,int y1,int x,int y){
        super(map,x1,y1,x,y);
    }
    @Override
    public int[][] setAction(){
        //执行总方法
        total = TotalCount();//得到可通行格总数量
        count = 0;//表示遍历到的格子数量
        stack.push(new Point(1,1));
        array.add(new Point(1,1));//表示起点遍历
        count++;
        while(true){
            Point point1 = Finding(stack.peek().x,stack.peek().y);//以起点横纵坐标作为初始参数
            if(point1!=null)
                if(!stack.peek().equals(point1)) {
                    stack.push(point1);
                    array.add(point1);
                    count++;
                }
            if(count==total) {
                break;
            }
        }
        button5.setDisable(true);
        button6.setDisable(true);//动画期间部分按钮处于不可操作状态
        AtomicBoolean isTeak = new AtomicBoolean(true);//说明按钮是否处于关闭状态
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis((double) map.length/2+20), event -> {
            //动画部分
            Point point;
            if(!array.isEmpty()){
                point = array.get(0);
                array.remove(0);
                map[point.x][point.y] = 4;
                canvas.PaintMap(map,x1,y1,x,y);//动画输出
            }
            if(array.isEmpty()) {
                if(isTeak.get()) {
                    button3.setDisable(false);
                    button5.setDisable(false);
                    button6.setDisable(false);//动画结束后恢复
                    isTeak.set(false);
                }
            }
        }));
        timeline.setCycleCount(total);
        timeline.play();
        return map;
    }
    @Override
    public Point Finding(int x,int y) {
        try{
            //寻路主方法
            visited[x][y] = 1;//将该格设为已访问
            //检查终点
            if (count == total){
                array.addAll(stack);
                return new Point(PathFinding.x,PathFinding.y);
            }
        /*
        采用深度优先算法寻优。
        */
            if (map[x + 1][y] != 0 && visited[x + 1][y] == 0)//检测右边
                return new Point(x+1,y);
            else if (map[x][y + 1] != 0 && visited[x][y + 1] == 0)//检测下边
                return new Point(x,y+1);
            else if (map[x - 1][y] != 0 && visited[x - 1][y] == 0)//检测左边
                return new Point(x-1,y);
            else if (map[x][y - 1] != 0 && visited[x][y - 1] == 0)//检测上边
                return new Point(x,y-1);
            else {
                stack.pop();//弹出当前所在格
                if (!stack.empty()) {
                    //获取上一次的格子
                    return stack.peek();
                }
            }
        }catch (StackOverflowError ex){
            //栈溢出异常处理
            while(!stack.empty())
                array.add(stack.pop());
            //清空栈
            stack = new Stack<>();
            Point point = array.get(array.size()-1);
            stack.push(point);
        }
        return null;
    }
    public int TotalCount(){
        //统计全地图可通行格子数量
        int total = 0;
        for(int i = 0;i < map.length;i++)
            for(int j = 0;j < map.length;j++){
                if(map[i][j] != 0)
                    total++;
            }
        return total;
    }
}
