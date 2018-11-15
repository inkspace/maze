package maze;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Bfs implements FindWayable{
	/**
	 * 记录dfs寻路过程路径的栈
	 */
	private Stack<SquareNode> bfsFindWayProcessResult = new Stack<>();
	
	/**
	 * 记录dfs寻路路径的栈
	 */
	private Stack<SquareNode> bfsFindWayResult = new Stack<>();

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
	 * 深度优先搜索的无参构造函数
	 */
	public Bfs() {
		
	}
	
	
	/**
	 * 深度优先搜索的构造函数
	 * @param startX 寻路起点的横坐标
	 * @param startY 寻路起点的纵坐标
	 * @param endX 寻路终点的横坐标
	 * @param endY 寻路终点的纵坐标
	 * @param squareNodes 装有地图的结点的数组
	 */
	public Bfs(int startX, int startY, int endX, int endY,SquareNode[][] squareNodes) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.squareNodes = squareNodes;
		bfsFindWay(this.startX, this.startY);
		createdFindWayResult();
	}
	
	
	/**
	 * 广度优先搜索寻路
	 * @param startX 寻路起点的横坐标
	 * @param startY 寻路起点的纵坐标
	 */
	public void bfsFindWay(int startX, int startY) {
		ArrayList<SquareNode> visitList = new ArrayList<>();
		Queue<SquareNode> queue = new LinkedList<>();
		
		queue.offer(squareNodes[startY][startX]);
		visitList.add(squareNodes[startY][startX]);
		// 方向右下左上
		int[][] direction = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};	
		
		while(!queue.isEmpty()) {
			SquareNode node = queue.poll();
			bfsFindWayProcessResult.add(node);
			if(node.getX() == endX && node.getY() == endY) {
				return ;
			}
			else {
				for (int i = 0; i < direction.length; i++) {
					int nextX = node.getX() + direction[i][1];
					int nextY = node.getY() + direction[i][0];
					
					// 先判断有没有超出迷宫的范围
		            if(nextX < 0 || nextX >= squareNodes[0].length-1 || nextY < 0 || nextY >= squareNodes.length-1) {
		                continue;
		            }
		            
		            //非障碍且未标记走过
		            if(squareNodes[nextY][nextX].isRoad() == true && !visitList.contains(squareNodes[nextY][nextX])) {  
		            	queue.offer(squareNodes[nextY][nextX]);
		            	squareNodes[nextY][nextX].setParent(node);
		            	visitList.add(squareNodes[nextY][nextX]);
		            }
				}
			}		
		}
	}	
	
	/**
	 * 返回广度优先搜索寻路结果
	 * @return Stack<SquareNode>类型 寻路结果
	 */
	@Override
	public Stack<SquareNode> getFindWayProcessResult() {
		return bfsFindWayProcessResult;
	}
	
	
	/**
	 * 返回广度优先搜索寻路结果
	 * @return Stack<SquareNode>类型 寻路结果
	 */
	@Override
	public Stack<SquareNode> getFindWayResult() {
		return bfsFindWayResult;
	}
	
	
	/**
	 * 根据父亲节点将通路路径还原
	 */
	private void createdFindWayResult() {
		SquareNode node = squareNodes[endY][endX];
		while(node.getParent() != null) {
			bfsFindWayResult.add(node);
			node = node.getParent();
		}
		bfsFindWayResult.add(node);
	}
}
