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
		
		if(args.length < 3){
			System.err.println("Error: Please specify filename of training, filename of test data, and minSup where 0<=minSup<=1");
			System.exit(1);
		}
		
		String filename = args[0]; 
		double minSup = -1;
		try{
			minSup = Double.parseDouble(args[2]);
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
		/*BufferedReader br = null;
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
		}*/
		CSVfile_returnpackage CSVcontents = CSVfileReader(filename);
		ArrayList<String[]> tripList = CSVcontents.get_visitsArrayList();
		String[] columnHeaders = CSVcontents.get_columnHeaders();
		System.out.print("done reading\n");
		//int j = 0;
		
		//Scan the database to find frequent itemsets
		//only scanning Department for now
		//int absoluteMinSup = toIntExact( Math.round(minSup*tripList.size()));
		//int absoluteMinSup = (int)( Math.round(minSup*tripList.size()));
		
		//find frequent departments
		/*int absoluteMinSup = (int) (Math.round(minSup * 95675));//hard coded as number of distinct visits
		System.out.print("absoluteMinSup: ");
		System.out.print(absoluteMinSup);
		System.out.print("\n");
		FrequentItemsetMiner fim = new FrequentItemsetMiner(tripList, columnHeaders);
		Map<String, Integer> freqDepartments= fim.Mine(absoluteMinSup, 1);
		System.out.print("num of departments: ");
		System.out.print(freqDepartments.size());
		System.out.print("\n");*/
		
		//generate grid to make rules
		System.out.print("making matrix!\n");
//		Set<ArrayList<String>> keySet = freqDepartments.keySet();
//		ArrayList<String[]> list = new ArrayList<String[]>();
//		for(ArrayList<String> type : keySet){
//			list.add((ArrayList<String[]>)type);
//		}
		//ArrayList<ArrayList<String>> newlist = new ArrayList<ArrayList<String>>(keySet);
		
		//Read in data from csv file and organize in map
		CBAMiner cba = new CBAMiner(tripList,columnHeaders);
		cba.buildMatrix();
		
		//generate size 1 candidate rule items, calculate support and confidence
		//and prune infrequent
		System.out.print("generating rules\n");
		ArrayList<RuleItem> possibleRuleItems = cba.generateRules();
		possibleRuleItems = cba.computeSupAndConf(possibleRuleItems);
		possibleRuleItems = cba.pruneInfrequentRules(possibleRuleItems,1);
		
		//generate new rules of larger size
		int n = 2;
		ArrayList<RuleItem> newRules = cba.generateRulesFromCandidates(possibleRuleItems,n);
		//there are still new rules being generated
		while (newRules.size() != 0 ){
			n= n +1;
			newRules = cba.computeSupAndConf(newRules);
			newRules = cba.pruneInfrequentRules(newRules,1);
			possibleRuleItems.addAll(newRules);
			newRules = cba.generateRulesFromCandidates(newRules,n);
		}
		possibleRuleItems = cba.pruneInfrequentRules(possibleRuleItems,0);
		
		//so all rules combined in possibleRuleItems, now sort by confidence, support, then order of generation
		RuleSorter ruleSorter = new RuleSorter(possibleRuleItems);
		ruleSorter.Sort();
		
		//here add function of generating classifiers
		
		//then read in test data set and compare to classifiers
		// need way to write probabilities into csv file
		CSVfile_returnpackage CSVcontents_test = CSVfileReader(args[1]);
		testdata_formatter test_f = new testdata_formatter(CSVcontents_test.get_visitsArrayList(), CSVcontents_test.get_columnHeaders());
		ArrayList<Set<String>> test_departments = test_f.format_visits();
		//each index in the ArrayList represents an individual visit
		//inside the Set<String> holds the departments that were purchased from that visit
		//I imagine iterating through test_departments and for each loop, generate the probabilities and write those out
		//to an appropriate CSV file
		
		// then we are DONE!
	
	}
	
	public static CSVfile_returnpackage CSVfileReader(String filename){
		
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
		}//end finally
		
		CSVfile_returnpackage contents = new CSVfile_returnpackage(tripList, columnHeaders);
		
		return contents;
	}

}
