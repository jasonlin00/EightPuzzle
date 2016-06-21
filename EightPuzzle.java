import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

//Author: Jason Lin
//CS 420 Professor Tang
//Date last modified: 4/26/16


public class EightPuzzle extends Astar {
	
	public static void main(String args[]) {
		
		System.out.println("What would you like to do?");
		System.out.println("1. Generate test cases");
		System.out.println("2. Use your own input");
		System.out.println("3. Show Three Sample Solutions with Depth > 10");
		
		Scanner kb = new Scanner(System.in);
		int input = kb.nextInt();
		
		switch(input) {
			case 1:
				test();
				break;
			case 2:
				input();
				break;
			case 3:
				showSample();
				break;
			default:
				System.out.println("Incorrect input");
				break;
			
		}
		
	}
	
	//handles custom board input
	public static void input() {
		
		System.out.println("Please enter custom board state: ");
		System.out.println("For example, for the board");
		System.out.println("0 1 2");
		System.out.println("3 4 5");
		System.out.println("6 7 8");
		System.out.println("Please enter 012345678.");
		System.out.println("Please enter your board state:");
		Scanner kb = new Scanner(System.in);
		String line = kb.nextLine();
		
		
		int[][] customBoard = new int[3][3];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				customBoard[i][j] = Character.getNumericValue(line.charAt(0));
				line = line.substring(1);
			}
		}
		System.out.println();
		
		System.out.println("Which heuristic would you like to use? (1 or 2)");
		int heuristic = kb.nextInt();
		
		search(customBoard, heuristic);
		
	}
	
	
	//3 sample solutions with Depth > 10
	public static void showSample() {
		
		//16 depth sample case using heuristic 1
		int[][] testCase1 = {{1, 4, 5}, {6, 0, 2}, {7, 8, 3}};
		
		search(testCase1, 1);
		
		//20 depth
		int[][] testCase2 = {{0, 5, 7}, {2, 8, 4}, {1, 3, 6}};
		
		search(testCase2, 2);
		
		//12 depth sample
		int[][] testCase3 = {{4, 3, 0}, {2, 1, 5}, {6, 7, 8}};
		
		search(testCase3, 2);
	}
	
	//uses provided sample test cases
	public static void test() {
		BufferedReader br = null;
		
		try {
			
			String line;
			br = new BufferedReader(new FileReader("sample.txt"));
			
			int counter = 0;
			double averageCost1 = 0;
			double averageCost2 = 0;
			int[][] results = new int[2][10];
			long startTime1 = 0;
			long endTime1 = 0;
			long startTime2 = 0;
			long endTime2 = 0;
			
			long averageTime1 = 0;
			long averageTime2 = 0;
			long[][] timeResults = new long[2][10];
			line = br.readLine();// get rid of first line
			
			while((line = br.readLine()) != null) {
				if(line.charAt(0) == 'D') {
					averageCost1 /= 200;
					averageCost2 /= 200;
					results[0][counter] = (int)averageCost1;
					results[1][counter] = (int)averageCost2;
					averageCost1 = 0;
					averageCost2 = 0;
					averageTime1 /= 200;
					averageTime2 /= 200;
					timeResults[0][counter] = averageTime1;
					timeResults[1][counter] = averageTime2;
					averageTime1 = 0;
					averageTime2 = 0;
					counter++;
					continue;
				}
				int[][] customBoard = new int[3][3];
				for(int i = 0; i < 3; i++) {
					for(int j = 0; j < 3; j++) {
						customBoard[i][j] = Character.getNumericValue(line.charAt(0));
						line = line.substring(1);
					}
				}
				System.out.println();
				
				//adding sum and search using both heuristic functions
				startTime1 = System.nanoTime();
				averageCost1 += search(customBoard, 1);
				endTime1 = System.nanoTime();
				averageTime1 += ((endTime1 - startTime1)/  100000);
				startTime2 = System.nanoTime();
				averageCost2 += search(customBoard, 2);
				endTime2 = System.nanoTime();
				averageTime2 += ((endTime2 - startTime2)/  100000);
			}
			
			//20 depth
			averageCost1 /= 200;
			averageCost2 /= 200;
			results[0][counter] = (int)averageCost1;
			results[1][counter] = (int)averageCost2;

			averageTime1 /= 200;
			averageTime2 /= 200;
			timeResults[0][counter] = averageTime1;
			timeResults[1][counter] = averageTime2;
			
			//print results of test
			for(int i = 0; i < results.length; i++) {
				for(int j = 0; j < results[0].length; j++) {
					System.out.print("For depth "  + (2 + 2 * j) + ", ");
					System.out.println("using heuristic " + (i + 1) +
					", " + results[i][j] + " nodes were used on average");
				}
			}
			for(int i = 0; i < timeResults.length; i++) {
				for(int j = 0; j < timeResults[0].length; j++) {
					System.out.print("For depth "  + (2 + 2 * j) + ", ");
					System.out.println("using heuristic " + (i + 1) +
					", " + timeResults[i][j] + " *10^-1 ms was used on average");
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}


