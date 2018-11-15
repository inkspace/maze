package maze;

import java.util.ArrayList;
import java.util.Stack;

public class Traversing implements FindWayable{
	/**
	 * ����װÿ��ͨ·��·����ջ���б� ��¼�����ߵ��յ�Ŀ��� ����ͨ���Ƚϸ���ͨ·�ĳ�����ѡ����̵�ͨ·
	 */
	private ArrayList<Stack<SquareNode>> allwaylist = new ArrayList<>();
	
	/**
	 * ��¼��ǰѰ··����ջ
	 */
	private Stack<SquareNode> NodeStack = new Stack<>();
	
	
	// ����յ�ĺ�������
	private int startX;
	private int startY;
	private int endX;
	private int endY;
		
	/**
	 * ��ͼ�������
	 */
	private SquareNode[][] squareNodes;
	
	
	/**
	 * A*Ѱ·��������޲ι��캯��
	 */
	public Traversing() {

	}
	
	
	/**
	 * A*Ѱ·������Ĺ��캯��
	 * @param startX Ѱ·���ĺ�����
	 * @param startY Ѱ·����������
	 * @param endX Ѱ·�յ�ĺ�����
	 * @param endY Ѱ·�յ��������
	 * @param squareNodes װ�е�ͼ�Ľ�������
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
	 * �����Թ������������㷨
	 * @param x �Թ����ĺ�����
	 * @param y �Թ�����������
	 */
	public void TraversingTheMaze(int x, int y) {
		// �������յ�ʱ����¼�µ�ǰѰ··��
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
		
		// ��ÿ��ϸ�����������Ͻ����ж� �Ƿ���ǽ ��ǽ������
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
	 * �ж������ϵķ����Ƿ�Ϊǽ���Լ������·�Ƿ��߹�
	 * @param x �ڵ�ĺ�����
	 * @param y �ڵ��������
	 * @return Boolean���� �����߷Ż�true�� �������߷���false
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
	 * ���ݼ�¼���нⷨ���б���ÿ��Ԫ�صĳ������ж���̵�·��
	 * @return int���� �������·����·���б��е��±�
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
	 * ����װ��Ѱ·�����ջ
	 * @return Stack���� Ѱ·���
	 */
	@Override
	public Stack<SquareNode> getFindWayResult() {
		return allwaylist.get(ShortestPath());
	}
	
	
	/**
	 * ����һ���յ�ջ����Ϊ��Ѱ���·��ֻ��Ҫ���
	 */
	@Override
	public Stack<SquareNode> getFindWayProcessResult() {
		return new Stack<SquareNode>();
	}
}
