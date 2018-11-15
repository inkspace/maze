package maze;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Bfs implements FindWayable{
	/**
	 * ��¼dfsѰ·����·����ջ
	 */
	private Stack<SquareNode> bfsFindWayProcessResult = new Stack<>();
	
	/**
	 * ��¼dfsѰ··����ջ
	 */
	private Stack<SquareNode> bfsFindWayResult = new Stack<>();

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
	 * ��������������޲ι��캯��
	 */
	public Bfs() {
		
	}
	
	
	/**
	 * ������������Ĺ��캯��
	 * @param startX Ѱ·���ĺ�����
	 * @param startY Ѱ·����������
	 * @param endX Ѱ·�յ�ĺ�����
	 * @param endY Ѱ·�յ��������
	 * @param squareNodes װ�е�ͼ�Ľ�������
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
	 * �����������Ѱ·
	 * @param startX Ѱ·���ĺ�����
	 * @param startY Ѱ·����������
	 */
	public void bfsFindWay(int startX, int startY) {
		ArrayList<SquareNode> visitList = new ArrayList<>();
		Queue<SquareNode> queue = new LinkedList<>();
		
		queue.offer(squareNodes[startY][startX]);
		visitList.add(squareNodes[startY][startX]);
		// ������������
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
					
					// ���ж���û�г����Թ��ķ�Χ
		            if(nextX < 0 || nextX >= squareNodes[0].length-1 || nextY < 0 || nextY >= squareNodes.length-1) {
		                continue;
		            }
		            
		            //���ϰ���δ����߹�
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
	 * ���ع����������Ѱ·���
	 * @return Stack<SquareNode>���� Ѱ·���
	 */
	@Override
	public Stack<SquareNode> getFindWayProcessResult() {
		return bfsFindWayProcessResult;
	}
	
	
	/**
	 * ���ع����������Ѱ·���
	 * @return Stack<SquareNode>���� Ѱ·���
	 */
	@Override
	public Stack<SquareNode> getFindWayResult() {
		return bfsFindWayResult;
	}
	
	
	/**
	 * ���ݸ��׽ڵ㽫ͨ··����ԭ
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
