package maze;

import java.util.Stack;

public class Dfs implements FindWayable{
	/**
	 * 记录dfs寻路过程路径的栈
	 */
	private Stack<SquareNode> dfsFindWayProcessResult = new Stack<>();
	
	/**
	 * 记录dfs寻路路径的栈
	 */
	private Stack<SquareNode> dfsFindWayResult = new Stack<>();
	
	/**
	 * 用于存储 记录dfs寻路路径的栈的反序结果
	 */
	private Stack<SquareNode> temp = new Stack<>();
	
	/**
	 * 判断寻路是否结束
	 */
	private boolean findWayFinshed = false;
	
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
	 * 深度优先搜索无参构造函数
	 */
	public Dfs() {
		
	}
	
	
	/**
	 * 深度优先搜索的构造函数
	 * @param startX 寻路起点的横坐标
	 * @param startY 寻路起点的纵坐标
	 * @param endX 寻路终点的横坐标
	 * @param endY 寻路终点的纵坐标
	 * @param squareNodes 装有地图的结点的数组
	 */
	public Dfs(int startX, int startY, int endX, int endY,SquareNode[][] squareNodes) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.squareNodes = squareNodes;
		dfsFindWayProcessResult.push(squareNodes[startY][startX]);
		squareNodes[startY][startX].setStep(true);
		dfsFindWayResult.push(squareNodes[startY][startX]);
		dfsFindWay(this.startX, this.startY);
	}
	
	
	/**
	 * 深度优先搜索算法寻路
	 * @param startX 寻路起点的横坐标
	 * @param startY 寻路起点的纵坐标
	 */
	public void dfsFindWay(int startX, int startY) {
		// 方向右下左上
		int[][] direction = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};	
			
		if(startX == endX && startY == endY) {
			findWayFinshed = true;
			return;
		}
			
		for(int i = 0; i < direction.length; i++) {
			if(findWayFinshed == true) {
    			return;
    		}
            int nextX = startX + direction[i][0];
            int nextY = startY + direction[i][1];
            
            // 先判断有没有超出迷宫的范围
            if(nextX < 0 || nextX >= squareNodes[0].length-1 || nextY < 0 || nextY >= squareNodes.length-1) {
                continue;
            }
            
            if(squareNodes[nextY][nextX].isRoad() == true && squareNodes[nextY][nextX].isStep() == false) {  //非障碍且未标记走过
            	dfsFindWayProcessResult.push(squareNodes[nextY][nextX]);
            	dfsFindWayResult.push(squareNodes[nextY][nextX]);
                squareNodes[nextY][nextX].setStep(true);
                dfsFindWay(nextX, nextY);  //递归调用, 移动到下一格
                if(findWayFinshed != true) {
                	dfsFindWayResult.pop();
                }
            }
        }
	}

	
	/**
	 * 返回深度优先搜索寻路过程结果
	 * @return Stack<SquareNode>类型 寻路过程结果
	 */
	@Override
	public Stack<SquareNode> getFindWayProcessResult() {
		return dfsFindWayProcessResult;
	}
	
	
	/**
	 * 返回深度优先搜索寻路结果
	 * @return Stack<SquareNode>类型 寻路结果
	 */
	@Override
	public Stack<SquareNode> getFindWayResult() {
		while(!dfsFindWayResult.isEmpty()) {
			temp.push(dfsFindWayResult.pop());
		}
		return temp;
	}
}
