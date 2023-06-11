package Project;

public class StepFinding extends PathFinding{
    //单步寻路类
    public StepFinding(int[][]map,int x1,int y1,int x,int y){
        super(map,x1,y1,x,y);//继承寻优的构造方法
        stack.push(new Point(x1,y1));//设置起点
    }
    public int[][] setAction(){
        Point point = Finding(stack.peek().x,stack.peek().y);
        if(point!=null) {
            if (!point.equals(stack.peek()))
                stack.push(point);
        }
        PaintMap();
        return map;
    }
    @Override
    public void PaintMap(){
        //将列表中所有点对象取出，在对应的格子上变为紫色
        for(Point point : stack) {
            map[point.x][point.y] = 4;
        }
    }
}
