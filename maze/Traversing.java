package maze;

import java.util.ArrayList;
import java.util.Stack;

public class Traversing implements FindWayable{
	/**
	 * 用来装每条通路的路径的栈的列表 记录所有走到终点的可能 后面通过比较各个通路的长度来选出最短的通路
	 */
	private ArrayList<Stack<SquareNode>> allwaylist = new ArrayList<>();
	
	/**
	 * 记录当前寻路路径的栈
	 */
	private Stack<SquareNode> NodeStack = new Stack<>();
	
	
	// 起点终点的横纵坐标
	private int startX;
	private int startY;
	private int endX;
	private int endY;
		
	/**
	 * 地图结点数组
	 */
	private SquareNode[][] squareNodes;
	
	
	/**
	 * A*寻路方法类的无参构造函数
	 */
	public Traversing() {

	}
	
	
	/**
	 * A*寻路方法类的构造函数
	 * @param startX 寻路起点的横坐标
	 * @param startY 寻路起点的纵坐标
	 * @param endX 寻路终点的横坐标
	 * @param endY 寻路终点的纵坐标
	 * @param squareNodes 装有地图的结点的数组
	 */
	public Traversing(int startX, int startY, int endX, int endY,SquareNode[][] squareNodes) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.squareNodes = squareNodes;
		TraversingTheMaze(this.startX, this.startY);
	}
	
	
	/**
	 * 遍历迷宫，求解出所有算法
	 * @param x 迷宫起点的横坐标
	 * @param y 迷宫起点的纵坐标
	 */
	public void TraversingTheMaze(int x, int y) {
		// 当到达终点时，记录下当前寻路路径
		if(x == squareNodes[endY][endX].getX() && y == squareNodes[endY][endX].getY()) {
			NodeStack.push(squareNodes[y][x]);
			Stack<SquareNode> temp = new Stack<>();
			for (SquareNode mazeNode : NodeStack) {
				temp.push(mazeNode);
			}
			allwaylist.add(temp);
			NodeStack.pop();
			return ;
		}
		
		// 对每个细胞的右下左上进行判断 是否是墙 是墙则不能走
		if(canGo(x+1,y)) {
			NodeStack.push(squareNodes[y][x]);
			TraversingTheMaze(x+1,y);
			NodeStack.pop();
		}
		if(canGo(x,y+1)) {
			NodeStack.push(squareNodes[y][x]);
			TraversingTheMaze(x,y+1);
			NodeStack.pop();
		}
		if(canGo(x-1,y)) {
			NodeStack.push(squareNodes[y][x]);
			TraversingTheMaze(x-1,y);
			NodeStack.pop();
		}
		if(canGo(x,y-1)) {
			NodeStack.push(squareNodes[y][x]);
			TraversingTheMaze(x,y-1);
			NodeStack.pop();
		}		
	}
	
	
	/**
	 * 判断坐标上的方格是否为墙，以及如果是路是否被走过
	 * @param x 节点的横坐标
	 * @param y 节点的纵坐标
	 * @return Boolean类型 可以走放回true， 不可以走返回false
	 */
	private boolean canGo(int x, int y) {
		if(squareNodes[y][x].isRoad() != true) {
			return false;
		}
		for (SquareNode mazeNode : NodeStack) {
			if(squareNodes[y][x] == mazeNode) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 根据记录所有解法的列表中每个元素的长度来判断最短的路径
	 * @return int类型 返回最短路径在路径列表中的下标
	 */
	private int ShortestPath() {
		int length = allwaylist.get(0).size();
		int index = 0;
		for (int i = 1; i < allwaylist.size(); i++) {
			if(allwaylist.get(i).size() < length) {
				length = allwaylist.get(i).size();
				index = i;
			}
		}
		return index;
	}
	
	
	/**
	 * 返回装有寻路结果的栈
	 * @return Stack类型 寻路结果
	 */
	@Override
	public Stack<SquareNode> getFindWayResult() {
		return allwaylist.get(ShortestPath());
	}
	
	
	/**
	 * 返回一个空的栈，因为找寻最短路径只需要结果
	 */
	@Override
	public Stack<SquareNode> getFindWayProcessResult() {
		return new Stack<SquareNode>();
	}
}
