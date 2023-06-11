package Project;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

import static Project.Mouse.*;

public class PathFinding {
    //自动寻路类
    public static int x1;
    public static int y1;//起点横纵坐标数据
    public static int x;
    public static int y;//终点的横纵坐标数据
    public static int[][] map;
    public static int[][] visited;//表示已访问坐标,0代表未访问，1代表已访问
    public  static Stack<Point> stack;
    public  static ArrayList<Point> array;//储存已经确定路线点对象
    public PathFinding(int[][] map,int x1,int y1,int x,int y){
        this.map = map;
        visited = new int[map.length][map.length];
        //将map内容转给visited
        for(int i = 0;i < map.length;i++)
            for(int j = 0;j < map.length;j++)
                visited[i][j] = map[i][j];
        //初始化已访问坐标，默认所有墙均已访问（将墙设为2），所有通路均未访问。
        for(int i = 0;i< map.length;i++)
            for(int j = 0;j < map.length;j++){
                if(map[i][j] == 0)
                    visited[i][j] = 2;
                else if(map[i][j] == 1 || map[i][j]==2||map[i][j]==3||map[i][j]==4)
                    visited[i][j] = 0;
            }
        PathFinding.x = x;
        PathFinding.y = y;//终点坐标
        PathFinding.x1 = x1;
        PathFinding.y1 = y1;
        map[x1][y1] = 1;//设置起点
        array = new ArrayList<>();
        stack = new Stack<>();
    }
    public int[][] setAction() {
        //执行方法
        stack.push(new Point(x1,y1));
        while(true){
            Point point1 = Finding(stack.peek().x,stack.peek().y);//以起点横纵坐标作为初始参数
            if(point1!=null)
                if(!stack.peek().equals(point1))
                    stack.push(point1);
            if(point1.x == x && point1.y == y) {
                array.addAll(stack);
                break;
            }
        }
        button2.setDisable(true);
        button3.setDisable(true);
        button5.setDisable(true);
        button6.setDisable(true);//动画期间所有按钮不可操作，防止bug
        AtomicBoolean isTeak = new AtomicBoolean(true);//表示按钮是否处于不可操作状态
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis((double) map.length/4+10), event -> {
            //动画内容
            Point point;
            if (!array.isEmpty()){
                point = array.get(0);
                array.remove(0);
                map[point.x][point.y] = 4;
                canvas.PaintMap(map,x1,y1,x,y);//不断进行动画输出交换
            }
            if(array.isEmpty()){
                if (isTeak.get()) {
                    button3.setDisable(false);
                    button5.setDisable(false);
                    button6.setDisable(false);//动画任务完成后恢复部分按钮状态
                    isTeak.set(false);
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        return map;
    }
    //用数字4代表已经访问过的路径
    public Point Finding(int x,int y){
        isFound isFound = new isFound(x,y);
        return isFound.Finding();
    }
    public void PaintMap(){
        //将列表中所有点对象取出，在对应的格子上变为紫色
        for(Point point : array) {
            map[point.x][point.y] = 4;
        }
    }
}