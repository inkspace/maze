package maze;

public class SquareNode {
	/**
	 * 迷宫中方块的横坐标
	 */
	private int x;
	/**
	 * 迷宫中方块的纵坐标
	 */
	private int y;
	/**
	 * 是路为true 不是为false
	 */
	private boolean road;
	/**
	 * 在生成迷宫时 0 表示没访问过 1 表示放问过
	 */
	private int access = 0;
	/**
	 * 在寻路时表示是否走过这个方块 true表示走过 false表示没走过
	 */
	private boolean step = false;
	
	//用于A*算法寻路所需要的参数 算法公式为 F = G + H
	/**
	 * 起点到当前点的移动耗费
	 */
	private int G = 0;
	
	/**
	 * 当前点到终点的预计耗费
	 */
	private int H;

	/**
	 * 代价函数
	 */
	private int F;
	
	/**
	 * 父亲节点
	 * 用于从终点结点一直记录其父亲节点，最后的到完整的通路路径
	 */
	private SquareNode parent = null;
	
	public SquareNode getParent() {
		return parent;
	}

	public void setParent(SquareNode parent) {
		this.parent = parent;
	}

	public int getF() {
		this.F = this.G + this.H;
		return this.F;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	public int getH() {
		return H;
	}

	public void setH(int h) {
		H = h;
	}

	public SquareNode(){
		this(0,0,false);
	}
	
	public boolean isStep() {
		return step;
	}

	public void setStep(boolean step) {
		this.step = step;
	}

	public SquareNode(int x, int y, boolean road) {
		this.x = x;
		this.y = y;
		this.road = road;
	}
	
	public int getAccess() {
		return access;
	}

	public void setAccess(int access) {
		this.access = access;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isRoad() {
		return road;
	}

	public void setRoad(boolean road) {
		this.road = road;
	}
}