import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Entry {
	
	public static void main(String[] args){
		
		if(args.length < 1){
			System.err.println("Please specify filename");
			System.exit(1);
		}
		
		String filename = args[0]; 
		BufferedReader br = null;
		String line = "";
		String delimiter = ",";
		ArrayList<String[]> tripList = new ArrayList<String[]>();
		String[] columnHeaders = null;
				
		try {

			br = new BufferedReader(new FileReader(filename));
			line = br.readLine(); //skip the first line that contains the titles of the columns.
			columnHeaders = line.split(delimiter);
			while ((line = br.readLine()) != null) {

			        // use comma as separator
				String[] trip = line.split(delimiter);
				tripList.add(trip);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		int j = 0;
		
		//Scan the database to find frequent itemsets
		//only scanning Department for now
		
		FrequentItemsetMiner fim = new FrequentItemsetMiner(tripList, columnHeaders);
		fim.Mine(3, 1);
		
		
		
		
		
	}

}
