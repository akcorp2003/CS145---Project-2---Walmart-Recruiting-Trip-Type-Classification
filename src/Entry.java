import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

//import static java.lang.Math.toIntExact;

public class Entry {
	
	public static void main(String[] args){
		
		if(args.length < 2){
			System.err.println("Error: Please specify filename and minSup where 0<=minSup<=1");
			System.exit(1);
		}
		
		String filename = args[0]; 
		double minSup = -1;
		try{
			minSup = Double.parseDouble(args[1]);
		}
		catch(NumberFormatException e){
			System.err.println("Error: Please specify a numerical value for minSup");
			System.exit(1);
		}
		
		if(minSup > 1 || minSup < 0){
			System.err.println("Error: Please specify a value between 0 and 1 for minSup");
			System.exit(1);
		}
		
		System.out.print("reading in file\n");
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
		System.out.print("done reading\n");
		//int j = 0;
		
		//Scan the database to find frequent itemsets
		//only scanning Department for now
		//int absoluteMinSup = toIntExact( Math.round(minSup*tripList.size()));
		//int absoluteMinSup = (int)( Math.round(minSup*tripList.size()));
		
		//find frequent departments
		int absoluteMinSup = (int) (Math.round(minSup * 95675));//hard coded as number of distinct visits
		System.out.print("absoluteMinSup: ");
		System.out.print(absoluteMinSup);
		System.out.print("\n");
		FrequentItemsetMiner fim = new FrequentItemsetMiner(tripList, columnHeaders);
		Map<String, Integer> freqDepartments= fim.Mine(absoluteMinSup, 1);
		System.out.print("num of departments: ");
		System.out.print(freqDepartments.size());
		System.out.print("\n");
		
		//generate grid to make rules
		System.out.print("making matrix!\n");
//		Set<ArrayList<String>> keySet = freqDepartments.keySet();
//		ArrayList<String[]> list = new ArrayList<String[]>();
//		for(ArrayList<String> type : keySet){
//			list.add((ArrayList<String[]>)type);
//		}
		//ArrayList<ArrayList<String>> newlist = new ArrayList<ArrayList<String>>(keySet);
		CBAMiner cba = new CBAMiner(tripList,columnHeaders);
		cba.buildMatrix();
		
		System.out.print("generating rules\n");
		ArrayList<RuleItem> possibleRuleItems = cba.generateRules();
		possibleRuleItems = cba.computeSupAndConf(possibleRuleItems);
	}

}