import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CBAMiner {
	
	private ArrayList<String[]> _tripList;
	private String[] _headers;
	private Map<String, Integer> _columnindex_1; //provides an index of what columns represent in our matrix
	private Map<Integer, String> _columnindex_2; //provides an index of what columns represent in our matrix
	//private Map<String, Integer> _rowindex; //provides an index of what rows represent in our matrix
	private Map<String, ArrayList<Integer>> _CBAmatrix; //the string is the row of the matrix and the ArrayList is the column
	
	public CBAMiner(ArrayList<String[]> tripList, String[] headers){
		_tripList = tripList;
		_headers =headers;
		_CBAmatrix = new HashMap<String, ArrayList<Integer>>();
		_columnindex_1 = new HashMap<String, Integer>();
	}
	
	/**
	 * Builds the matrix for the _tripList ArrayList that was given to the class
	 * @return A 2D array of the resulting matrix
	 */
public CBAMiner_returnpackage buildMatrix(Map<String, Integer> freqDepartments){
		
		//ArrayList<ArrayList<Integer>> CBAmatrix = new ArrayList<ArrayList<Integer>>();
		System.out.print("in process of building matrix\n");
		Map<String, Integer> Departments = new HashMap<String, Integer>();
		
		//SHOULD BE ABLE TO REPLACE THIS WITH PASSED IN VALUE -> DONE
		//get an idea of how many columns (departments) are in the dataset
		Set<String> freqDepartment_keys = freqDepartments.keySet();
		for(String trip : freqDepartment_keys){
			String department = trip;
			if(!Departments.containsKey(department)){
				Departments.put(department, 1);
			}
		}
		
		//get an idea of how many rows (triptype) are in the dataset
		//we look back at the CSV file for this
		Map<String, Integer> TripTypes = new HashMap<String, Integer>();
		for(String[] trip : _tripList){
			String triptype = trip[0];
			if(!TripTypes.containsKey(trip[0])){
				TripTypes.put(triptype, 1);
			}
				
		}
		
		Set<String> COLUMN = freqDepartments.keySet();
		Set<String> ROW = TripTypes.keySet();
		
		//begin building the matrix
		//we begin building the _CBAmatrix Map
		//each entry in CBAmatrix is the triptype, so in other words, CBAmatrix holds the rows of our matrix
		ArrayList<Integer> triptype_row;
		for(String triptype : ROW){
			if(!_CBAmatrix.containsKey(triptype)){
				triptype_row = new ArrayList<Integer>();
				//initialize triptype_row
				for(int i = 0; i < COLUMN.size(); i++){
					triptype_row.add(0);
				}
				_CBAmatrix.put(triptype, triptype_row);
			}
		}
		
		//build the index that will help us locate where the Departments are in the ArrayList in _CBAmatrix
		int index = 0;
		for(String department : COLUMN){
			if(!_columnindex_1.containsKey(department)){
				_columnindex_1.put(department, index);
			}
			if(!_columnindex_2.containsKey(index)){
				_columnindex_2.put(index, department);
			}
			index++;
		}
		
		//with all the setup things done, we can finally construct the matrix
		for(String[] trip : _tripList){
			if(_CBAmatrix.containsKey(trip[0])){
				ArrayList<Integer> temp_triptype_row = _CBAmatrix.get(trip[0]);
				//get index of the column
				//some departments will be pruned out so it's possible that trip[5] will not be indexed
				//and it's safe to ignore it
				if(_columnindex_1.containsKey(trip[5])){
					index = _columnindex_1.get(trip[5]);
				}
				else{
					continue;
				}
				
				if(temp_triptype_row.get(index) != 1){
					temp_triptype_row.set(index, 1);
				}
				//else the item is already there and for now, we're only counting that it happened once
			}
		}
		
		//we now have our matrix!
		CBAMiner_returnpackage CBAmatrix = new CBAMiner_returnpackage(_CBAmatrix, _columnindex_1, _columnindex_2);
		
		
		return CBAmatrix;
	}

	/**
	 * Verifies that for all classes in the matrix, there is at least a value of 1 in that class
	 * @return
	 */
	public boolean checkMatrix(){
		Set<String> classes = _CBAmatrix.keySet();
		Set<String> column_indices = _columnindex_1.keySet();
		
		for(String triptype : classes){
			ArrayList<Integer> triptype_row = _CBAmatrix.get(triptype);
			//iterate through the row and check for 1
			boolean one_exists = false;
			for(Integer value : triptype_row){
				if(value == 1)
					one_exists = true;
			}
			if(!one_exists)
				return false;
			
		}
		
		return true;
	}
	
	//generate size 1 possible rule items
	public ArrayList<RuleItem> generateRules(){
		ArrayList<RuleItem> rules = new ArrayList<RuleItem>();
		Set<String> classes = _CBAmatrix.keySet();
		Set<String> column_indices = _columnindex_1.keySet();
		
		//loop through each row of matrix
		for(String triptype : classes){
			ArrayList<Integer> triptype_row = _CBAmatrix.get(triptype);
			//iterate through the row and check for 1
			for(Integer value : triptype_row){
				//if 1, create a rule item and add to arraylist
				if(value == 1){
					//need way to get department via column_indices
					//RuleItem newRule = newRuleTime(department,tripType);
					//rules.add(newRule);
				}
			}
			
		}
		return rules;
	}
	
	public ArrayList<RuleItem> computeSupAndConf(ArrayList<RuleItem> possibleRules){
		//for each rule item 
		for (RuleItem r : possibleRules){
			//compute support and confidence of item sets 
			//set support and confidence of rules
		}
		return possibleRules;
	}
}