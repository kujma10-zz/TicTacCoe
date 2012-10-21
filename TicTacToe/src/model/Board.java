package model;


public class Board {

	private int size;
	private int winOption;
	private final CellValue[][] matrix;

	private int moveCount; 
	
	public Board(int size, int opt) {
		this.matrix = new CellValue[size][size];
		this.winOption = opt;
		this.size = size;
		clear();
	}

	private void clear() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				matrix[i][j] = CellValue.EMPTY;
			}
		}
		moveCount = 0;
	}
	
	public boolean isFull() {
		return (moveCount == size * size);
	}
	
	public boolean isInBorders(int x, int y){
		if(x>=size || x<0) return false;
		if(y>=size || y<0) return false;
		return true;
	}
	
	public int getWinOption(){
		return this.winOption;
	}
	
	public CellValue getValueAt(Cell cell) {
		if (cell.x >= size)
			throw new IllegalArgumentException("X is out of range.");
		if (cell.y >= size)
			throw new IllegalArgumentException("Y is out of range.");

		return matrix[cell.x][cell.y];
	}

	public boolean isEmpty(Cell cell) {
		return getValueAt(cell) == CellValue.EMPTY;
	}
	
	public void makeMove(Cell cell, CellValue value){
		if (value==CellValue.EMPTY) 
			throw new IllegalArgumentException("A cell can not be set to emty during move.");
		setValueAt(cell, value);
		moveCount++;
	}

	public int getSize() {
		return size;
	}

	private void setValueAt(Cell cell, CellValue value) {
		if (!isEmpty(cell)) 
			throw new IllegalArgumentException("The cell is not emty.");
		
		matrix[cell.x][cell.y] = value;
	}
}