package maze;

import java.util.ArrayList;

public class Prim {
	/**
	 * 用于形成多条通路时计数 即是当 count == 120 时就进行一次挖墙
	 */
	private int count = 0;
	
	/**
	 * 用来装入代表墙的列表 由于不知道墙面的个数所以使用列表 方便在生成迷宫的算法中随机选择墙面
	 */
	private ArrayList<SquareNode> wallList = new ArrayList<>();
	
	// 起点终点的横纵坐标
	private int startX;
	private int startY;
	private int width;
	private int height;
	
	/**
	 * 地图结点数组
	 */
	private SquareNode[][] squareNodes;
	
	
	public Prim() {
		this(1,1,null);
	}
	
	
	/**
	 * A*寻路方法类的构造函数
	 * @param startX 寻路起点的横坐标
	 * @param startY 寻路起点的纵坐标
	 * @param endX 寻路终点的横坐标
	 * @param endY 寻路终点的纵坐标
	 * @param squareNodes 装有地图的结点的数组
	 */
	public Prim(int startX, int startY, SquareNode[][] squareNodes) {
		this.startX = startX;
		this.startY = startY;
		this.squareNodes = squareNodes;
		this.width = squareNodes[0].length;
		this.height = squareNodes.length;
		squareNodes[startY][startX].setAccess(1);
	}
	
	
	public int getStartX() {
		return startX;
	}


	public int getStartY() {
		return startY;
	}


	/**
	 * 将已形成的网格中的墙进行移除即是生成迷宫 运用的算法是Prim算法  但是在其中进行了随机挖洞，所以生成的迷宫不是最小生成树 所以会形成多条通路
	 * @param x 起点横坐标
	 * @param y 起点纵坐标
	 */
	public void RemoveWall(int x, int y){
		// 将细胞四周的墙都加入到列表中（四周的墙指的是 上下左右 斜角上的墙不算）
		if(y-1 > 0 && squareNodes[y-1][x].getAccess() == 0){
			wallList.add(squareNodes[y-1][x]);
		}
		if(x-1 > 0 && squareNodes[y][x-1].getAccess() == 0){
			wallList.add(squareNodes[y][x-1]);
		}
		if(x+1 < (width-1) && squareNodes[y][x+1].getAccess() == 0){
			wallList.add(squareNodes[y][x+1]);
		}
		if(y+1 < (height-1) && squareNodes[y+1][x].getAccess() == 0){
			wallList.add(squareNodes[y+1][x]);
		}
			
		while(wallList.size() != 0)
		{
			int numberOfWall = wallList.size();
			int n = ((int)(Math.random()*1000))%numberOfWall;
			
			SquareNode tempNode = wallList.get(n);
			int tempNodeX = tempNode.getX();
			int tempNodeY = tempNode.getY();
			// 判断墙是将左右的细胞分隔开 还是将上下的细胞分隔开
			if(squareNodes[tempNodeY][tempNodeX-1].getAccess() + squareNodes[tempNodeY][tempNodeX+1].getAccess() == 1){
				tempNode.setRoad(true);
				tempNode.setAccess(1);
				wallList.remove(n);
				if(squareNodes[tempNodeY][tempNodeX-1].getAccess() == 0){
					squareNodes[tempNodeY][tempNodeX-1].setAccess(1);
					RemoveWall(tempNodeX-1,tempNodeY);
				}
				else{
					squareNodes[tempNodeY][tempNodeX+1].setAccess(1);
					RemoveWall(tempNodeX+1,tempNodeY);
				}
			}
			else if(squareNodes[tempNodeY-1][tempNodeX].getAccess() + squareNodes[tempNodeY+1][tempNodeX].getAccess() == 1){
				tempNode.setRoad(true);
				tempNode.setAccess(1);
				wallList.remove(n);
				if(squareNodes[tempNodeY-1][tempNodeX].getAccess() == 0){
					squareNodes[tempNodeY-1][tempNodeX].setAccess(1);
					RemoveWall(tempNodeX, tempNodeY-1);
				}
				else{
					squareNodes[tempNodeY+1][tempNodeX].setAccess(1);
					RemoveWall(tempNodeX, tempNodeY+1);
				}
			}
			else if(squareNodes[tempNodeY][tempNodeX-1].getAccess() + squareNodes[tempNodeY][tempNodeX+1].getAccess() == 2
					|| squareNodes[tempNodeY-1][tempNodeX].getAccess() + squareNodes[tempNodeY+1][tempNodeX].getAccess() == 2){
				// 当两个细胞都被访问过后 进行随机挖墙 则会形成多条通路 用count来进行计数根据迷宫的大小来决定挖墙的次数 迷宫越大挖的越多
				if(count == this.width*this.height/30){
					tempNode.setRoad(true);
					tempNode.setAccess(1);
					count = 0;
				}
				count++;
				wallList.remove(n);
			}
		}
	}
}
