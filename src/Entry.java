import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.opencsv.CSVWriter;

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
		long starttime = System.nanoTime();
		ArrayList<RuleItem> possibleRuleItems = cba.generateRules();
		long endtime = System.nanoTime();
		double seconds = (double) (endtime-starttime)/1000000000.0;
		System.out.println("Time it took to generate rules: " + Double.toString(seconds));
		
		starttime = System.nanoTime();
		possibleRuleItems = cba.computeSupAndConf(possibleRuleItems);
		endtime = System.nanoTime();
		seconds = (double) (endtime-starttime)/1000000000.0;
		System.out.println("Time it took to compute support and confidence: " + Double.toString(seconds));
		possibleRuleItems = cba.pruneInfrequentRules(possibleRuleItems,1);
		
		//generate new rules of larger size
		int n = 2;
		starttime = System.nanoTime();
		ArrayList<RuleItem> newRules = cba.generateRulesFromCandidates(possibleRuleItems,n);
		endtime = System.nanoTime();
		seconds = (double) (endtime-starttime)/1000000000.0;
		System.out.println("Time it took to generate 2-candidate from possibleRules: " + Double.toString(seconds));
		//there are still new rules being generated
		starttime = System.nanoTime();
		while (newRules.size() != 0 ){
			n= n +1;
			newRules = cba.computeSupAndConf(newRules);
			newRules = cba.pruneInfrequentRules(newRules,1);
			possibleRuleItems.addAll(newRules);
			newRules = cba.generateRulesFromCandidates(newRules,n);
		}
		endtime = System.nanoTime();
		seconds = (double) (endtime-starttime)/1000000000.0;
		System.out.println("Time it took to generate n-candidates from possibleRules: " + Double.toString(seconds));
		possibleRuleItems = cba.pruneInfrequentRules(possibleRuleItems,0);
		
		//so all rules combined in possibleRuleItems, now sort by confidence, support, then order of generation
		RuleSorter ruleSorter = new RuleSorter(possibleRuleItems);
		ruleSorter.Sort();
		
		//here add function of generating classifiers
		Classifier testdata_classifier = new Classifier(possibleRuleItems, cba.get_CBAmatrix());
		
		//then read in test data set and compare to classifiers
		//read in test data
		CSVfile_returnpackage CSVcontents_test = CSVfileReader(args[1]);
		testdata_formatter test_f = new testdata_formatter(CSVcontents_test.get_visitsArrayList(), CSVcontents_test.get_columnHeaders());
		//format the data
		ArrayList<Set<String>> test_departments = test_f.format_visits();
		
		//here is where we classify the data!
		ArrayList<Map<Integer, Double>> predicted_data = testdata_classifier.classify(test_departments);
		
		//write out the probabilities to a CSV file!
		CSVProbabilityWriter(cba.get_CBAmatrix(), predicted_data, test_f);
		
		
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
	
	public static void CSVProbabilityWriter(Map<Integer, ArrayList<ArrayList<Attribute>>> cbamatrix,
			ArrayList<Map<Integer, Double>> predicted_data, testdata_formatter test_f) {
		// write out to CSV file with properties
		try {
			CSVWriter outputwriter = new CSVWriter(new FileWriter("visit_predictions.csv"));
			// first write the headers
			ArrayList<String> headers = new ArrayList<String>();
			headers.add("VisitNumber");

			// we are sorting the keys because the requirements of the output is
			// to have the CSV headers to be nicely assorted in
			// ascending order. Also, it provides uniform layout to the file.
			// Map<Integer, ArrayList<ArrayList<Attribute>>> cbamatrix =
			// cba.get_CBAmatrix();
			Set<Integer> triptypes = cbamatrix.keySet();
			Object[] triptypes_sorted = cbamatrix.keySet().toArray();
			Arrays.sort(triptypes_sorted);

			// complete the job of writing the headers
			for (Object trip : triptypes_sorted) {
				headers.add("TripType_" + Integer.toString((int) trip));
			}
			String[] my_array = new String[headers.size()];
			headers.toArray(my_array);

			outputwriter.writeNext(my_array);

			Map<Integer, Integer> testdata_indextovisitno = test_f.get_visitnumber_to_indexmap();

			// now write the actual data stuff
			int index = 0;
			ArrayList<String> dataline = new ArrayList<String>();
			for (Map<Integer, Double> visit_predictions : predicted_data) {
				
				//first we need to get the visit number we are currently working on
				//we need this because the [0] entry of the CSV file contains the trip number
				//this can be found by querying the map
				if (testdata_indextovisitno.containsKey(index)) {
					int visitno = testdata_indextovisitno.get(index);
					String visitno_s = Integer.toString(visitno);
					dataline.add(visitno_s);
				}
				for (Object trip : triptypes_sorted) {
					if (visit_predictions.containsKey((int) trip)) {
						String probability = new Double(visit_predictions.get((int) trip)).toString();
						dataline.add(probability);
					} else {
						dataline.add("0.0");
					}
				} // endfor
					// with the current visit's probabilities read into the
					// arraylist for each triptype,
					// we can prepare it for output!
				String[] t_array = new String[dataline.size()];
				dataline.toArray(t_array);

				outputwriter.writeNext(t_array);
				index++;
				dataline.clear(); // refresh the dataline

			}

			outputwriter.close(); // is this the right place to put it?

		} catch (IOException e) {
			e.printStackTrace();
		}
		// end try-catch block
	}

}
