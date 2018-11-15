package maze;

import java.util.ArrayList;
import java.util.Stack;

public class AStar implements FindWayable{
	/**
	 * �ƶ�һ�����ķѵ�ֵΪ10
	 */
	private final static int MOVECOST_VALUE = 10;
	
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
	 * �ر��б�
	 */
	private ArrayList<SquareNode> closeList = new ArrayList<>();
	
	/**
	 * �����б�
	 */
	private ArrayList<SquareNode> openList = new ArrayList<>();
	
	/**
	 * �ƶ�������������
	 */
	private int[][] direction = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};	
	
	
	public AStar() {

	}
	
	
	/**
	 * A*Ѱ·������Ĺ��캯��
	 * @param startX Ѱ·���ĺ�����
	 * @param startY Ѱ·����������
	 * @param endX Ѱ·�յ�ĺ�����
	 * @param endY Ѱ·�յ��������
	 * @param squareNodes װ�е�ͼ�Ľ�������
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
	 * ��A*Ѱ·��������Ѱ·
	 */
	public void aStarFindWay() {	
		// �����б�
		closeList.clear();
		openList.clear();
		
		openList.add(squareNodes[startY][startX]);
		while(!openList.isEmpty()) {
			
			// ���յ�������ڿ����б���ʱ�򵽴��յ�
			if(openList.contains(squareNodes[endY][endX])) {
				closeList.add(squareNodes[endY][endX]);
				return ;
			}
			
			// �ӿ����б���ȡ�����ۺ���Fֵ��С�Ľڵ㲢�ŵ��ر��б�(F[���ۺ���]=G[�ߵ���ǰ�ڵ��Ѿ�����]+H[���յ������������])
			SquareNode node = minFvalueNode();
			
			closeList.add(node);
			
			for (int i = 0; i < direction.length; i++) {
				int nextX = node.getX() + direction[i][1];
				int nextY = node.getY() + direction[i][0];
				
				if(canGo(nextX, nextY)) {
					// ���û�ڿ����������������Gֵ ��������ӵ������б���
					if(!openList.contains(squareNodes[nextY][nextX])) {
						squareNodes[nextY][nextX].setH(getH(nextX, nextY));
						squareNodes[nextY][nextX].setG(node.getG() + MOVECOST_VALUE);
						squareNodes[nextY][nextX].setParent(node);
						openList.add(squareNodes[nextY][nextX]);
					}
					else {
						// ����Ѿ��ڿ����б����ҵ�ǰGֵ��С�����Gֵ�͸��ڵ� Hֵ���ø��£���Ϊ���нڵ��Hֵ���ǹ̶���
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
	 * ����װ��Ѱ·���̽����ջ
	 * @return Stack���� Ѱ·���
	 */
	@Override
	public Stack<SquareNode> getFindWayProcessResult() {
		Stack<SquareNode> AStarFindWayProcessResult = new Stack<>();
		// ��Ϊcloselist���б���Ҫ����ת����ջ����
		for (SquareNode squareNode : closeList) {
			AStarFindWayProcessResult.add(squareNode);
		}
		return AStarFindWayProcessResult;
	}
	
	
	/**
	 * ����װ��Ѱ·�����ջ
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
	 * ���ؿ����б��д��ۺ���Fֵ��С�Ľ�㲢�����ڿ����б���ɾ��
	 * @return SquareNode���� ��С��Fֵ�Ľڵ�
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
	 * �жϸõ��Ƿ����ǰ����������ǽ�ڣ�Ҳ��������ͼ��Χ
	 * @param nextX ǰ���ĺ�����
	 * @param nextY ǰ����������
	 * @return boolean���� ����true��ʾ�����ߣ�����false��ʾ��������
	 */
	private boolean canGo(int nextX, int nextY) {
		if(nextX < 0 || nextX >= squareNodes[0].length-1 || nextY < 0 || nextY >= squareNodes.length-1) {
			return false;			
		}
		
		// �Ƿ���· �� �Ƿ��Ѿ��߹���
		if(!squareNodes[nextY][nextX].isRoad() || closeList.contains(squareNodes[nextY][nextX])) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * ���ظõ�Hֵ(��ǰ�㵽�յ��Ԥ�ƺķ�)
	 * @param x �õ�ĺ�����
	 * @param y �õ��������
	 * @return int���� �õ��Hֵ
	 */
	private int getH(int x, int y) {
		return (Math.abs(endX - x) + Math.abs(endY - y)) * MOVECOST_VALUE;
	}

}

















