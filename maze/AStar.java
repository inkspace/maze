package maze;

import java.util.ArrayList;
import java.util.Stack;

public class AStar implements FindWayable{
	/**
	 * 移动一格所耗费的值为10
	 */
	private final static int MOVECOST_VALUE = 10;
	
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
	 * 关闭列表
	 */
	private ArrayList<SquareNode> closeList = new ArrayList<>();
	
	/**
	 * 开启列表
	 */
	private ArrayList<SquareNode> openList = new ArrayList<>();
	
	/**
	 * 移动方向右下左上
	 */
	private int[][] direction = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};	
	
	
	public AStar() {

	}
	
	
	/**
	 * A*寻路方法类的构造函数
	 * @param startX 寻路起点的横坐标
	 * @param startY 寻路起点的纵坐标
	 * @param endX 寻路终点的横坐标
	 * @param endY 寻路终点的纵坐标
	 * @param squareNodes 装有地图的结点的数组
	 */
	public AStar(int startX, int startY, int endX, int endY,SquareNode[][] squareNodes) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.squareNodes = squareNodes;
		aStarFindWay();
	}
	
	
	/**
	 * 用A*寻路方法进行寻路
	 */
	public void aStarFindWay() {	
		// 清理列表
		closeList.clear();
		openList.clear();
		
		openList.add(squareNodes[startY][startX]);
		while(!openList.isEmpty()) {
			
			// 当终点结点出现在开启列表中时则到达终点
			if(openList.contains(squareNodes[endY][endX])) {
				closeList.add(squareNodes[endY][endX]);
				return ;
			}
			
			// 从开起列表中取出代价函数F值最小的节点并放到关闭列表(F[代价函数]=G[走到当前节点已经消耗]+H[离终点距离最少消耗])
			SquareNode node = minFvalueNode();
			
			closeList.add(node);
			
			for (int i = 0; i < direction.length; i++) {
				int nextX = node.getX() + direction[i][1];
				int nextY = node.getY() + direction[i][0];
				
				if(canGo(nextX, nextY)) {
					// 如果没在开启类表里则计算结点的G值 并将其添加到开启列表中
					if(!openList.contains(squareNodes[nextY][nextX])) {
						squareNodes[nextY][nextX].setH(getH(nextX, nextY));
						squareNodes[nextY][nextX].setG(node.getG() + MOVECOST_VALUE);
						squareNodes[nextY][nextX].setParent(node);
						openList.add(squareNodes[nextY][nextX]);
					}
					else {
						// 如果已经在开起列表里且当前G值更小则更新G值和父节点 H值不用更新，因为所有节点的H值都是固定的
						if(squareNodes[nextY][nextX].getG() > node.getG() + MOVECOST_VALUE) {
							squareNodes[nextY][nextX].setG(node.getG() + MOVECOST_VALUE);
							squareNodes[nextY][nextX].setParent(node);
							openList.add(squareNodes[nextY][nextX]);
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * 返回装有寻路过程结果的栈
	 * @return Stack类型 寻路结果
	 */
	@Override
	public Stack<SquareNode> getFindWayProcessResult() {
		Stack<SquareNode> AStarFindWayProcessResult = new Stack<>();
		// 因为closelist是列表，需要将其转换成栈类型
		for (SquareNode squareNode : closeList) {
			AStarFindWayProcessResult.add(squareNode);
		}
		return AStarFindWayProcessResult;
	}
	
	
	/**
	 * 返回装有寻路结果的栈
	 * @return
	 */
	@Override
	public Stack<SquareNode> getFindWayResult() {
		Stack<SquareNode> AStarFindWayResult = new Stack<>();
		SquareNode node = squareNodes[endY][endX];
		while(node.getParent() != null) {
			AStarFindWayResult.add(node);
			node = node.getParent();
		}
		AStarFindWayResult.add(node);
		return AStarFindWayResult;
	}


	/**
	 * 返回开启列表中代价函数F值最小的结点并将其在开启列表中删除
	 * @return SquareNode类型 最小的F值的节点
	 */
	public SquareNode minFvalueNode() {
		SquareNode minNode = openList.get(0);
		int F = openList.get(0).getF();
		
		for (SquareNode squareNode : openList) {
			if(squareNode.getF() < F) {
				minNode = squareNode;
				F = squareNode.getF();
			}
		}
		openList.remove(minNode);
		return minNode;
	}
	
	
	/**
	 * 判断该点是否可以前进，即不是墙壁，也不超出地图范围
	 * @param nextX 前进的横坐标
	 * @param nextY 前进的纵坐标
	 * @return boolean类型 返回true表示可以走，返回false表示不可以走
	 */
	private boolean canGo(int nextX, int nextY) {
		if(nextX < 0 || nextX >= squareNodes[0].length-1 || nextY < 0 || nextY >= squareNodes.length-1) {
			return false;			
		}
		
		// 是否是路 且 是否已经走过了
		if(!squareNodes[nextY][nextX].isRoad() || closeList.contains(squareNodes[nextY][nextX])) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * 返回该点H值(当前点到终点的预计耗费)
	 * @param x 该点的横坐标
	 * @param y 该点的纵坐标
	 * @return int类型 该点的H值
	 */
	private int getH(int x, int y) {
		return (Math.abs(endX - x) + Math.abs(endY - y)) * MOVECOST_VALUE;
	}

}

















