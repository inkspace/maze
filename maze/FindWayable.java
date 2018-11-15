package maze;

import java.util.Stack;

public interface FindWayable {
	/**
	 * ����Ѱ·���̽��
	 * @return Stack<SquareNode>���� Ѱ·���
	 */
	Stack<SquareNode> getFindWayProcessResult();
	
	/**
	 * ����Ѱ·���
	 * @return Stack<SquareNode>���� Ѱ·���
	 */
	Stack<SquareNode> getFindWayResult();
}
