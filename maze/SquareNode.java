package maze;

public class SquareNode {
	/**
	 * �Թ��з���ĺ�����
	 */
	private int x;
	/**
	 * �Թ��з����������
	 */
	private int y;
	/**
	 * ��·Ϊtrue ����Ϊfalse
	 */
	private boolean road;
	/**
	 * �������Թ�ʱ 0 ��ʾû���ʹ� 1 ��ʾ���ʹ�
	 */
	private int access = 0;
	/**
	 * ��Ѱ·ʱ��ʾ�Ƿ��߹�������� true��ʾ�߹� false��ʾû�߹�
	 */
	private boolean step = false;
	
	//����A*�㷨Ѱ·����Ҫ�Ĳ��� �㷨��ʽΪ F = G + H
	/**
	 * ��㵽��ǰ����ƶ��ķ�
	 */
	private int G = 0;
	
	/**
	 * ��ǰ�㵽�յ��Ԥ�ƺķ�
	 */
	private int H;

	/**
	 * ���ۺ���
	 */
	private int F;
	
	/**
	 * ���׽ڵ�
	 * ���ڴ��յ���һֱ��¼�丸�׽ڵ㣬���ĵ�������ͨ··��
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