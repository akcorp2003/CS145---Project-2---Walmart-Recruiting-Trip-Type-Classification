import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CBAMiner {
	
	private ArrayList<String[]> _tripList;
	private String[] _headers;
	private Map<String, Integer> _columnindex; //provides an index of what columns represent in our matrix
	//private Map<String, Integer> _rowindex; //provides an index of what rows represent in our matrix
	private Map<String, ArrayList<Integer>> _CBAmatrix; //the string is the row of the matrix and the ArrayList is the column
	
	public CBAMiner(ArrayList<String[]> tripList, String[] headers){
		_tripList = tripList;
		_headers =headers;
		_CBAmatrix = new HashMap<String, ArrayList<Integer>>();
		_columnindex = new HashMap<String, Integer>();
	}
	
	/**
	 * Builds the matrix for the _tripList ArrayList that was given to the class
	 * @return A 2D array of the resulting matrix
	 */
	public CBAMiner_returnpackage buildMatrix(Map<ArrayList<String>, Integer> freqDepartments){
		
		//ArrayList<ArrayList<Integer>> CBAmatrix = new ArrayList<ArrayList<Integer>>();
		System.out.print("in process of building matrix\n");
		Map<String, Integer> Departments = new HashMap<String, Integer>();
		
		//SHOULD BE ABLE TO REPLACE THIS WITH PASSED IN VALUE
		//get an idea of how many columns (departments) are in the dataset
		for(String[] trip : _tripList){
			String department = trip[5];
			if(!Departments.containsKey(department)){
				Departments.put(department, 1);
			}
		}
		
		//get an idea of how many rows (triptype) are in the dataset
		Map<String, Integer> TripTypes = new HashMap<String, Integer>();
		for(String[] trip : _tripList){
			String triptype = trip[0];
			if(!TripTypes.containsKey(trip[0])){
				TripTypes.put(triptype, 1);
			}
				
		}
		
		//NEED TO FIX SO THAT BECOME SET<STRING> NOT SET<ARRAYLIST<STRING>>
		//Set<String> COLUMN = freqDepartments.keySet();
		Set<String> COLUMN = Departments.keySet();
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
			if(!_columnindex.containsKey(department)){
				_columnindex.put(department, index);
				index++;
			}
		}
		
		//with all the setup things done, we can finally construct the matrix
		for(String[] trip : _tripList){
			if(_CBAmatrix.containsKey(trip[0])){
				ArrayList<Integer> temp_triptype_row = _CBAmatrix.get(trip[0]);
				//get index of the column
				if(_columnindex.containsKey(trip[5])){
					index = _columnindex.get(trip[5]);
				}
				if(temp_triptype_row.get(index) != 1){
					temp_triptype_row.set(index, 1);
				}
				//else the item is already there and for now, we're only counting that it happened once
			}
		}
		
		//we now have our matrix!
		CBAMiner_returnpackage CBAmatrix = new CBAMiner_returnpackage(_CBAmatrix, _columnindex);
		
		
		return CBAmatrix;
	}
}