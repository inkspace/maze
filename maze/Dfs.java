package maze;

import java.util.Stack;

public class Dfs implements FindWayable{
	/**
	 * ��¼dfsѰ·����·����ջ
	 */
	private Stack<SquareNode> dfsFindWayProcessResult = new Stack<>();
	
	/**
	 * ��¼dfsѰ··����ջ
	 */
	private Stack<SquareNode> dfsFindWayResult = new Stack<>();
	
	/**
	 * ���ڴ洢 ��¼dfsѰ··����ջ�ķ�����
	 */
	private Stack<SquareNode> temp = new Stack<>();
	
	/**
	 * �ж�Ѱ·�Ƿ����
	 */
	private boolean findWayFinshed = false;
	
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
	 * ������������޲ι��캯��
	 */
	public Dfs() {
		
	}
	
	
	/**
	 * ������������Ĺ��캯��
	 * @param startX Ѱ·���ĺ�����
	 * @param startY Ѱ·����������
	 * @param endX Ѱ·�յ�ĺ�����
	 * @param endY Ѱ·�յ��������
	 * @param squareNodes װ�е�ͼ�Ľ�������
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
	 * ������������㷨Ѱ·
	 * @param startX Ѱ·���ĺ�����
	 * @param startY Ѱ·����������
	 */
	public void dfsFindWay(int startX, int startY) {
		// ������������
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
            
            // ���ж���û�г����Թ��ķ�Χ
            if(nextX < 0 || nextX >= squareNodes[0].length-1 || nextY < 0 || nextY >= squareNodes.length-1) {
                continue;
            }
            
            if(squareNodes[nextY][nextX].isRoad() == true && squareNodes[nextY][nextX].isStep() == false) {  //���ϰ���δ����߹�
            	dfsFindWayProcessResult.push(squareNodes[nextY][nextX]);
            	dfsFindWayResult.push(squareNodes[nextY][nextX]);
                squareNodes[nextY][nextX].setStep(true);
                dfsFindWay(nextX, nextY);  //�ݹ����, �ƶ�����һ��
                if(findWayFinshed != true) {
                	dfsFindWayResult.pop();
                }
            }
        }
	}

	
	/**
	 * ���������������Ѱ·���̽��
	 * @return Stack<SquareNode>���� Ѱ·���̽��
	 */
	@Override
	public Stack<SquareNode> getFindWayProcessResult() {
		return dfsFindWayProcessResult;
	}
	
	
	/**
	 * ���������������Ѱ·���
	 * @return Stack<SquareNode>���� Ѱ·���
	 */
	@Override
	public Stack<SquareNode> getFindWayResult() {
		while(!dfsFindWayResult.isEmpty()) {
			temp.push(dfsFindWayResult.pop());
		}
		return temp;
	}
}
