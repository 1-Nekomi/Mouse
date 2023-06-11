package Project;

import java.util.ArrayList;
import java.util.Random;

public class Maze {
    // 初始化一个地图 默认所有路不通
    //最终产生的二维数组大小实际为(2width+1) * (2height+1)
    private final int width;
    private final int height;
    public int[][] map;// 存放迷宫的数组
    ArrayList<Point> array = new ArrayList<>();//存储通路的列表
    ArrayList<Point> boomer = new ArrayList<>();//存储墙的列表
    Maze(int width, int height) {
        this.width = width;
        this.height = height;
        map = new int[2*width+1][2*height+1];
    }
    public void inti() {
        for (int i = 0; i < map[0].length; i++) // 将所有格子都设为墙
            for (int j = 0; j < map.length; j++)
                map[i][j] = 0;// 0 为墙 1为路
        // 中间格子放为1
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                map[2 * i + 1][2 * j + 1] = 1;// 0 为墙 1为路
        /*Prim算法
        1.让迷宫全是墙.
        2.选一个单元格作为迷宫的通路，然后把它的邻墙放入列表
        3.当列表里还有墙时
          1.从列表里随机选一个墙，如果这面墙分隔的两个单元格只有一个单元格被访问过
                1.那就从列表里移除这面墙，即把墙打通，让未访问的单元格成为迷宫的通路
                2.把未访问单元格四周的墙加入列表
          2.如果墙两面的单元格都已经被访问过，那就从列表里移除这面墙，除此之外不做任何处理
        4.列表中不存在墙，结束。
         */
        rePrim();
    }
    public void rePrim() {
        Random rd = new Random();
        int x ;
        int y ;//表示随机抽到的通路坐标
        do {
            x = (int) ((Math.random() * (map.length - 2)) + 1);
            y = (int) ((Math.random() * (map.length- 2)) + 1);//一直随机，直到随机到通路。
        } while (map[x][y] != 1);
        array.add(new Point(x, y));//设置最初点
        //将最初点周围的墙放入列表，不包含边围墙
        if(x+1 != map.length-1)
            boomer.add(new Point(x+1,y));
        if(x-1 != 0)
            boomer.add(new Point(x-1,y));
        if(y+1 != map.length-1)
            boomer.add(new Point(x,y+1));
        if(y-1 != 0)
            boomer.add(new Point(x,y-1));//将该通路的上下左右四个格子加入列表
        int[] MOVE = {1,-1};//表示移动方向和距离，对水平而言为右左移动，对竖直而言下上移动
        while(true){
            if(!boomer.isEmpty()){
                int location = 0;//表示该墙哪个轴上隔了两格子，1为水平x轴，2为竖直y轴
                int i = rd.nextInt(boomer.size());
                Point point = boomer.get(i);
                Point point1 = null;
                Point point2 = null;
                Point point3 = null;
                Point point4 = null;//表示该墙隔开的上下或左右两格
                if(point.x+MOVE[0] != map.length-1 && map[point.x+MOVE[0]][point.y] == 1){
                    if(point.x+MOVE[1] != 0 && map[point.x+MOVE[1]][point.y] == 1){
                        //表示墙的左右两边存在通路
                        point4 = new Point(point.x+MOVE[0], point.y);//右
                        point3 = new Point(point.x+MOVE[1], point.y);//左
                        location = 1;
                    }
                }
                else if(point.y+MOVE[0] != map.length-1 && map[point.x][point.y+MOVE[0]] == 1){
                    if(point.y+MOVE[1] != 0 && map[point.x][point.y+MOVE[1]] == 1){
                        //表示墙的上下两边存在通路
                        point1 = new Point(point.x, point.y+MOVE[0]);//上
                        point2 = new Point(point.x, point.y+MOVE[1]);//下
                        location = 2;
                    }
                }
                if(location == 1){
                    //水平两格
                    if(array.contains(point4) && array.contains(point3))
                        //表示如果墙的两边通路均被访问过，则直接移除列表
                        boomer.remove(i);
                    else if(!array.contains(point3) || !array.contains(point4)){
                        //如果墙的两边通路只有一边被访问过，则将墙变为通路，并将之前墙其中一边未访问通路四周墙加入列表
                        if(array.contains(point4) ){
                            array.add(point3);
                            setArray(point,point3,i);
                        }
                        else if(array.contains(point3)){
                            array.add(point4);
                            setArray(point,point4,i);
                        }
                    }
                }
                else if(location == 2){
                    //竖直两格
                    if(array.contains(point1) && array.contains(point2))
                        boomer.remove(i);
                    else if(!array.contains(point1) || !array.contains(point2)){
                        if(array.contains(point2) ){
                            array.add(point1);
                            setArray(point,point1,i);
                        }
                        else if(array.contains(point1)){
                            array.add(point2);
                            setArray(point,point2,i);
                        }
                    }
                }
                else
                    boomer.remove(i);
            }
            else
                break;
        }
    }
    public void Clear(){
        //清除地图，将地图所有方块设为通路
        for(int i = 1;i < map.length;i++){
            for(int j = 1;j < map.length;j++)
                map[i][j] = 1;
        }
        //生成边墙
        for(int i = 0;i < map.length; i+= map.length-1)
            for(int j = 0; j < map.length;j++)
                map[i][j] = 0;//横边墙
        for(int j = 0;j < map.length; j+=map.length-1)
            for(int i = 0;i< map.length;i++)
                map[i][j] = 0;//纵边墙
    }
    public Point[] BoomerCheck(Point point){
        //检测通路四周可以加入列表的墙
        Point[] points = {null,null,null,null};
        if(point.x+1 != map.length-1 && map[point.x+1][point.y] == 0)//右
            points[0] = new Point(point.x+1, point.y);
        if(point.x-1 != 0 && map[point.x-1][point.y] == 0)//左
            points[1] = new Point(point.x-1, point.y);
        if(point.y+1 != map.length-1 && map[point.x][point.y+1] == 0)//下
            points[2] = new Point(point.x, point.y+1);
        if(point.y-1 != 0 && map[point.x][point.y-1] == 0)//上
            points[3] = new Point(point.x, point.y-1);
        return points;
    }
    public void setArray(Point point,Point point1,int i){
        //将未访问格四周的墙加入列表中
        map[point.x][point.y] = 1;
        boomer.remove(i);
        Point[] points = BoomerCheck(point1);
        for(Point point5 : points)
            if(point5 != null)
                boomer.add(point5);
    }
}
