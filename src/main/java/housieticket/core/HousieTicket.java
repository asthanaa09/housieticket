package housieticket.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * Requirement:
 * 
 * 1. 3 rows and 9 columns (Dynamic value) in the ticket. Column 1 to 9 contains only 1 to 90 values.
 * 		a. First Column contains value from 1 to 10
 * 		b. Second column contains 11 to 20 and so on
 * 
 * 2. Each column has max to 2 cells filled in .
 * 3. The number can't be repeated in a column
 * 4. Each row has 5 max cells filled in. hence only 15 cells in all. 
 * 
 * 
 * @author anurag asthana
 *
 */
public class HousieTicket {
	
	private static class HousieTicketException extends RuntimeException {
		
		public static final long serialVersionUID = 1L;
		
		HousieTicketException(String message) {
			super(message);
		}
	}
	
	/**
	 * Added value should in between 1 to mentioned range
	 */
	private final Integer MAX_ROW_CELL_TO_FILL = 5;
	private final Integer MAX_COL_CELL_TO_FILL = 2;
	private final Integer VALUE_MULTIPLIER = 10;
	private Integer RANGE;
	/**
	 * Number of rows in ticket
	 */
	private Integer mRows; 
	/**
	 * Number of column each row contains
	 */
	private Integer mColumns;
	
	/**
	 * Holds filled column indices for corresponding row
	 */
	private Map<Integer, Set<Integer>> mRowIndeces;
	/**
	 * Contains set of value already added in ticket to overcome duplicate value insertion
	 */
	private Set<Integer> mTicketValues;
	
	/**
	 *  Matrix representation of ticket, contains Rows and columns
	 */
	private Integer mTicket[][];
	
	/**
	 * First time initialization of ticket
	 * 
	 * @param rows - Number of rows in ticket
	 * @param columns - Number of columns in ticket
	 */
	public void init(int rows, int columns) {
		if(mTicket == null) {
			// Destroy previous one and created fresh with new set of values
			mTicket = null;
			mRowIndeces = null;
			mTicketValues = null;
		}
		
		mRows = rows;
		mColumns = columns;
		RANGE = (mColumns * VALUE_MULTIPLIER);
		mRowIndeces = new HashMap<Integer, Set<Integer>>();
		
		// Empty row set
		for(int i = 0; i < mRows; i++)
			mRowIndeces.put(i, new HashSet<Integer>());
		
		mTicket = new Integer[rows][columns];
		mTicketValues = new HashSet<Integer>();
	}
	
	public void generateTicket() {
		int totalValues = mRows * MAX_ROW_CELL_TO_FILL;
		int iterations = 0;
 		int test = 0;
		while(true) {
			try {
				int value = getRand();
				if(addValue(value)) {
					// System.out.println("Iteration " + iterations  + ", value " + value);
					iterations++;
				}
				
				if(iterations == totalValues)
					break;
				
			} catch (Exception e) {
				// System.out.println(e.getMessage());
//				if(test == 5)
//					System.exit(10);
//				test++;
			}
			System.out.println("____________________________________________");
			printTicket();
		}
	}
	
	/**
	 * Add new value to the column
	 * 
	 * Rule:
	 * 	1. First Column contains value from 1 to 10, second column from 11 to 20 and so on
	 *  2. Number should not repeated in column
	 *  3. Each row has max 5 cells filled in, hence only 15 cells in all. 
	 *   
	 *  based on random value row and col will be decided
	 * @param value
	 */
	public boolean addValue(int value) {
		if(mTicketValues.contains(value)) 
			return false;
			
		Integer column = getColumn(value);
		Integer row = getRow(column);
		if(row == null || column == null)
			throw new HousieTicketException("Can't found valid position to add value for Value - " + value);
		
		Set<Integer> filledColumnIndexes = mRowIndeces.get(row);
		filledColumnIndexes.add(column);
		mRowIndeces.put(row, filledColumnIndexes);
		mTicketValues.add(value);
		mTicket[row][column] = value;
		
		return true;
	}
	
	/**
	 * Get row index corresponding column index at which value to be store
	 * 
	 * */
	public Integer getRow(Integer colIndex) {
		Set<Integer> rowIndexes = mRowIndeces.keySet(); 
		Integer rowIndex = null;
		int filledColumnCells = 0;
		
		if(colIndex == null)
			return null;

		// for each column only 2 cells can be filled
		for(Integer index : rowIndexes) {
			Set<Integer> filledIndexes = mRowIndeces.get(index);
			
			if(mTicket[index][colIndex] != null) { 
				filledColumnCells++;
			}
			
			if(filledColumnCells == MAX_COL_CELL_TO_FILL) { 
				return null;
			}
			
			if(filledIndexes != null && filledIndexes.size() == MAX_ROW_CELL_TO_FILL) 
				continue;
			
			if(mTicket[index][colIndex] == null) 
				rowIndex = index;
		}
		
		return rowIndex;
	}
	
	public Integer getColumn(int value) {
		int startRange = 1;
		int multiplier = VALUE_MULTIPLIER; 
		Integer colIndex = null;
		
		for(int i = 1; i <= mColumns; i++) {
			int maxRange = i * multiplier;
			if(value >= startRange && value <= maxRange) {
				colIndex =  (i-1);
				break;
			}
				
			startRange = maxRange + 1;	
		}
		
		return  colIndex;
	}
	
	/**
	 * Initiate Ticket generation process
	 * 
	 * Flow:
	 * 
	 * 1. Initialize initial values & data structure used e.g rows, columns
	 * 2. Generate ticket logic
	 * 3. Printing of ticket
	 * 
	 */
	public void initiateTicketGeneration() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please Enter Number of Rows And column for ticket \n Note: Column Should be more that 6 ");
		
		int rows = sc.nextInt();
		int column = sc.nextInt();
		sc.close();
		init(rows, column);
		generateTicket();
		printTicket();
	}
	
	/**
	 * Generate Random number
	 * 
	 * @return
	 */
	public int getRand() {
		int ranValue = 0;
		int min = 1;
		int max = RANGE;
		Random rand = new Random();
		
		while(true) {
			ranValue = rand.nextInt((max - min) + 1) + min;
			int divisor = (ranValue/10);
			int startFrom = divisor+1;
			int toEdn = (divisor * VALUE_MULTIPLIER);
			
			Set<Integer> betweenValues = new HashSet<Integer>();
			// TODO: Find another way
			for(int i = startFrom; i <= toEdn; i++ )
				betweenValues.add(i);
			
			if(!mTicketValues.contains(betweenValues) && ranValue <= RANGE) 
				break;
		}
		
		return ranValue;
	}
	
	/**
	 * Print tickets
	 */
	public void printTicket() {
		System.out.println("\nPrinting Ticket :");
		
		for(Integer[] row : mTicket) {
			String rowValue = "";
			
			for(Integer value : row) {
				if (value == null)
					rowValue += "";
				else
					rowValue += value;

				rowValue += ",";
			}

			System.out.print(rowValue + "\n");
		}
	}
}
