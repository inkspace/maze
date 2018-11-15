package maze;

import java.util.Stack;

public interface FindWayable {
	/**
	 * 返回寻路过程结果
	 * @return Stack<SquareNode>类型 寻路结果
	 */
	Stack<SquareNode> getFindWayProcessResult();
	
	/**
	 * 返回寻路结果
	 * @return Stack<SquareNode>类型 寻路结果
	 */
	Stack<SquareNode> getFindWayResult();
}
