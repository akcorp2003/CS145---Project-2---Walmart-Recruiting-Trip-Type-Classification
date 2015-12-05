import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class testdata_formatter {
	
	private ArrayList<String[]> _transactions;
	private String[] _columnHeaders;
	private Map<Integer, Integer> _index_to_visitno;
	
	public testdata_formatter(ArrayList<String[]> visits, String[] columnHeaders){
		_transactions = visits;
		_columnHeaders = columnHeaders;
		_index_to_visitno = new HashMap<Integer, Integer>();
	}
	
	/**
	 * Parses the raw data of visits into ArrayList<Set<String>> where each index in the ArrayList represents a visit
	 * and the strings are the departments.
	 * @return An ArrayList of Sets of Strings where the strings are departments and each index in the arraylist is a visit
	 */
	public ArrayList<Set<String>> format_visits(){
		
		ArrayList<Set<String>> visits_formatted = new ArrayList<Set<String>>();
		int workingvisit = Integer.MIN_VALUE;
		int index = 0;
		for(String[] line : _transactions){
			int curr_visit = Integer.parseInt(line[0]);
			if(curr_visit == workingvisit){
				//we are always working at the end of the ArrayList
				int working_index = visits_formatted.size()-1;
				Set<String> visit_departments = visits_formatted.get(working_index);
				if(!visit_departments.contains(line[4])){
					visit_departments.add(line[4]);
				}
			}
			else{
				//we're starting on a new visit!
				workingvisit = curr_visit;
				Set<String> visit_departments = new HashSet<String>();
				visit_departments.add(line[4]);
				visits_formatted.add(visit_departments);
				if(!_index_to_visitno.containsKey(index)){
					_index_to_visitno.put(index, curr_visit);
					index++;
				}
			}//end else
		}//end for
		
		return visits_formatted;
	}
	
	public Map<Integer, Integer> get_visitnumber_to_indexmap(){
		return _index_to_visitno;
	}
}
