import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CBAMiner {
	
	private ArrayList<String[]> _tripList;
	private String[] _headers;
	private Map<String, Integer> _columnindex; //provides an index of what columns represent in our matrix
	private Map<String, Integer> _rowindex; //provides an index of what rows represent in our matrix
	
	public CBAMiner(ArrayList<String[]> tripList, String[] headers){
		_tripList = tripList;
		_headers =headers;
	}
	
	/**
	 * Builds the matrix for the _tripList ArrayList that was given to the class
	 * @return A 2D array of the resulting matrix
	 */
	public ArrayList<ArrayList<Integer>> buildMatrix(){
		
		ArrayList<ArrayList<Integer>> CBAmatrix = new ArrayList<ArrayList<Integer>>();
		
		Map<String, Integer> Departments = new HashMap<String, Integer>();
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
		
		Set<String> COLUMN = Departments.keySet();
		Set<String> ROW = TripTypes.keySet();
		
		//begin building the matrix
		//we first build the _rowindex Map
		//each entry in CBAmatrix is the triptype, so in other words, CBAmatrix holds the rows of our matrix
		ArrayList<Integer> triptype_row = new ArrayList<Integer>();
		int index = 0;
		for(String triptype : ROW){
			if(!_rowindex.containsKey(triptype)){
				_rowindex.put(triptype, index);
				index++;
			}
		}
		
		
		
		return CBAmatrix;
	}
}
