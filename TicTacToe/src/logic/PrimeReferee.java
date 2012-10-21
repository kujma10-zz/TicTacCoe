package logic;

import model.Cell;
import model.CellValue;



public class PrimeReferee implements Referee {

	public GameStatus getGameStatus(ReadOnlyBoard board) {
		
		if (isWinner(board, CellValue.X)) {
			return GameStatus.XISWINNER;
		}
		
		if (isWinner(board, CellValue.O)) {
			return GameStatus.OISWINNER;
		}
		
		if (board.isFull()) {
			return GameStatus.DRAW;
		}
		
		return GameStatus.INPROGRESS;
	}
	
	private boolean isWinner(ReadOnlyBoard board, CellValue value) {
		for(int i=0;i<board.getSize();i++){
			for(int j=0;j<board.getSize();j++){
				Cell cell = new Cell(i,j);
				if(checkWin(board, value, cell)) return true;
			}
		}
		return false;
	}
	
	
	
	private boolean checkCell(ReadOnlyBoard board, CellValue value, Cell cell ){
			
		if(!board.isInBorders(cell.x, cell.y)) return false;
		CellValue currentValue = board.getValueAt(new CellWrapped(cell)).toCellValue();
		if (value!=currentValue)  return false;
		return true;
	}
	
	private boolean checkWin(ReadOnlyBoard board, CellValue value, Cell cell ){
		int [][] arr=new int[][] { {1,1,0,-1}, {0,1,1,1} };
		
		for(int i=0;i<4;i++){
			boolean ragaca1 = true;
			for(int k=0;k<board.getWinOption();k++){
				if(!board.isInBorders(cell.x+k*arr[0][i],cell.y+arr[1][i]*k))
					ragaca1=false;
				else{
					Cell c=new Cell(cell.x+k*arr[0][i],cell.y+arr[1][i]*k);
					if(!checkCell(board,value,c))
					ragaca1 = false;
				}
			}
			if(ragaca1) return true;
		}
		return false;
	}
	
	
}
