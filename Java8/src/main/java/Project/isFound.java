package Project;

import java.util.Stack;


public class isFound {
    private int x;
    private int y;//表示输入的坐标，从该坐标开始判定
    public isFound(int x,int y){
        this.x = x;
        this.y = y;
    }
    public Point Finding(){
        //寻路主方法，一旦找到一个新的通路，则返回该通路的坐标点作为下一次寻路的参数
        try{
            PathFinding.visited[x][y] = 1;//将该格设为已访问
            //检查终点
            if (this.x == PathFinding.x && this.y == PathFinding.y){
                PathFinding.array.addAll(PathFinding.stack);
                return new Point(this.x,this.y);
            }
        /*
        采用深度优先算法寻优。
        */
            if (PathFinding.map[x + 1][y] != 0 && PathFinding.visited[x + 1][y] == 0)//检测右边
                return new Point(x+1,y);
            else if (PathFinding.map[x][y + 1] != 0 && PathFinding.visited[x][y + 1] == 0)//检测下边
                return new Point(x,y+1);
            else if (PathFinding.map[x - 1][y] != 0 && PathFinding.visited[x - 1][y] == 0)//检测左边
                return new Point(x-1,y);
            else if (PathFinding.map[x][y - 1] != 0 && PathFinding.visited[x][y - 1] == 0)//检测上边
                return new Point(x,y-1);
            else {
                Point point = PathFinding.stack.pop();//弹出当前所在格并获取点对象
                PathFinding.map[point.x][point.y] = 1;//该语句是为了单步寻路模块而设计的，是为了能在程序上体现“回溯”这一特征
                if (!PathFinding.stack.empty()) {
                    //获取上一次的格子
                    return PathFinding.stack.peek();
                }
            }
        }catch (StackOverflowError ex){
            //栈溢出异常处理
            while(!PathFinding.stack.empty())
                PathFinding.array.add(PathFinding.stack.pop());
            //清空栈
            PathFinding.stack = new Stack<>();
            Point point = PathFinding.array.get(PathFinding.array.size()-1);//清除列表中对应的元素
            PathFinding.stack.push(point);
        }
        return null;
    }
}
