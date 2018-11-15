package maze;

import java.util.ArrayList;

public class Prim {
	/**
	 * �����γɶ���ͨ·ʱ���� ���ǵ� count == 120 ʱ�ͽ���һ����ǽ
	 */
	private int count = 0;
	
	/**
	 * ����װ�����ǽ���б� ���ڲ�֪��ǽ��ĸ�������ʹ���б� �����������Թ����㷨�����ѡ��ǽ��
	 */
	private ArrayList<SquareNode> wallList = new ArrayList<>();
	
	// ����յ�ĺ�������
	private int startX;
	private int startY;
	private int width;
	private int height;
	
	/**
	 * ��ͼ�������
	 */
	private SquareNode[][] squareNodes;
	
	
	public Prim() {
		this(1,1,null);
	}
	
	
	/**
	 * A*Ѱ·������Ĺ��캯��
	 * @param startX Ѱ·���ĺ�����
	 * @param startY Ѱ·����������
	 * @param endX Ѱ·�յ�ĺ�����
	 * @param endY Ѱ·�յ��������
	 * @param squareNodes װ�е�ͼ�Ľ�������
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
	 * �����γɵ������е�ǽ�����Ƴ����������Թ� ���õ��㷨��Prim�㷨  ���������н���������ڶ����������ɵ��Թ�������С������ ���Ի��γɶ���ͨ·
	 * @param x ��������
	 * @param y ���������
	 */
	public void RemoveWall(int x, int y){
		// ��ϸ�����ܵ�ǽ�����뵽�б��У����ܵ�ǽָ���� �������� б���ϵ�ǽ���㣩
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
			// �ж�ǽ�ǽ����ҵ�ϸ���ָ��� ���ǽ����µ�ϸ���ָ���
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
				// ������ϸ���������ʹ��� ���������ǽ ����γɶ���ͨ· ��count�����м��������Թ��Ĵ�С��������ǽ�Ĵ��� �Թ�Խ���ڵ�Խ��
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
