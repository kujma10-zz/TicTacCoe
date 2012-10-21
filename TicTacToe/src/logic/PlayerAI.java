package logic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import model.CellValue;

/**
 * MINI_MAX
 * 
 * @author Guga
 * 
 */

public class PlayerAI implements Player {

	private int size, win;
	private int moveSearchDepth = 3;		//shota es aris svlis dzebnis sigrme
	private int considerBestMoves = 6;		//da es aris dzebnis sigane. tu sakmarisad swrafad ar imushavebs romelime sheamcire

	public void makeMove(ReadOnlyBoard boardObj, PlayerColor colorObj, MoveListener moveListener) {
		size = boardObj.getSize();
		win = boardObj.getWinOption();
		int color = getColor(colorObj);
		int[][] board = fillBoard(boardObj);
		moveListener.makeMove(getBestMove(board, color, moveSearchDepth));
	}

	private int getColor(PlayerColor colorObj) {
		return (colorObj == PlayerColor.O) ? 0 : 1;
	}

	private int[][] fillBoard(ReadOnlyBoard boardObj) {
		int[][] ans = new int[size][size];
		for (int i = 0; i < ans.length; i++) {
			for (int j = 0; j < ans[0].length; j++) {
				CellValueWrapped sv = boardObj.getValueAt(new CellWrapped(i, j));
				if (sv == CellValueWrapped.O)
					ans[i][j] = 0;
				else if (sv == CellValueWrapped.X)
					ans[i][j] = 1;
				else 
					ans[i][j] = -1;
			}
		}
		return ans;
	}

	private CellWrapped getBestMove(int[][] board, int color, int depth) {
		double[] bestMove = miniMax(board, color, depth);
		return new CellWrapped((int)bestMove[0], (int)bestMove[1]);
	}

	private double[] miniMax(int[][] board, int color, int depth) {
		double[] ans = {-1, -1, Double.NEGATIVE_INFINITY};
		double curGr;
		Comparator<double[]> comparator = new GradeComparator();
		PriorityQueue<double[]> pq = new PriorityQueue<double[]>(considerBestMoves, comparator);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if(board[i][j]==-1){
					board[i][j] = color;
					curGr = gradeBoard(board, color);
					board[i][j] = -1;
					if(curGr >ans[2]){
						ans[0] = i;
						ans[1] = j;
						ans[2] = curGr;
					}
					pq.add(new double[]{(double)i, (double)j, curGr});
				}
			}
		}
		if(depth == 0)
			return ans;
		
		if(ans[2] == Double.POSITIVE_INFINITY)
			return ans;
		
		ans[2] = Double.NEGATIVE_INFINITY;
		for (int k = 0; pq.size()!=0 && k<considerBestMoves*(1-((moveSearchDepth-depth)/moveSearchDepth)); k++) {
			double[] ijGrade = pq.poll();
			int i = (int) ijGrade[0];
			int j = (int) ijGrade[1];
			
			board[i][j] = color;
			curGr = -miniMax(board, (color+1)%2, depth-1)[2];
			board[i][j] = -1;
			if(curGr >ans[2]){
				ans[0]=i;
				ans[1]=j;
				ans[2] = curGr;
			}
		}
//		if(depth == moveSearchDepth)
//			System.out.println(depth+" "+ans[2]);
		return ans;
	}
	private double gradeBoard(int[][] board, int color) {
		double grade = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				grade += gradeCell(board, color, i, j) - gradeCell(board, (color+1)%2, i, j);
			}
		}
		return grade;
	}

	private double gradeCell(int[][] board, int color, int i, int j) {
		double grade = 0;
		
		int filled = 0;
		for (int k = 0; k < win; k++) {
			if(outOfRAnge(i+k,j) || board[i+k][j] == (color+1)%2){
				break;
			}
			if(board[i+k][j] == color)
				filled++;
			if(k == win-1)
				grade += Math.pow(filled, 4);// win / Math.pow(win - filled, 10); 
		}
		if(filled == win)
			return Double.POSITIVE_INFINITY;
		filled = 0;
		for (int k = 0; k < win; k++) {
			if(outOfRAnge(i,j+k) || board[i][j+k] == (color+1)%2)
				break;
			if(board[i][j+k] == color)
				filled++;
			if(k == win-1)
				grade += Math.pow(filled, 4);// win / Math.pow(win - filled, 10); 
		}

		if(filled == win)
			return Double.POSITIVE_INFINITY;
		filled = 0;
		for (int k = 0; k < win; k++) {
			if(outOfRAnge(i+k,j+k) || board[i+k][j+k] == (color+1)%2)
				break;
			if(board[i+k][j+k] == color)
				filled++;
			if(k == win-1)
				grade += Math.pow(filled, 4);// win / Math.pow(win - filled, 10); 
		}

		if(filled == win)
			return Double.POSITIVE_INFINITY;
		filled = 0;
		for (int k = 0; k < win; k++) {
			if(outOfRAnge(i-k,j+k) || board[i-k][j+k] == (color+1)%2)
				break;
			if(board[i-k][j+k] == color)
				filled++;
			if(k == win-1)
				grade += Math.pow(filled, 4);// win / Math.pow(win - filled, 10); 
		}
		if(filled == win)
			return Double.POSITIVE_INFINITY;
		return grade;
	}

	private boolean outOfRAnge(int i, int j) {
		return !(i>=0 && j>=0 && i<size && j<size);
	}

	public class GradeComparator implements Comparator<double[]> {
		@Override
		public int compare(double[] x, double[] y) {
			if (x[2] < y[2])
				return 1;
			if (x[2] > y[2])
				return -1;
			return 0;
		}
	}
}









