import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class InformationGainCalculator {
	
	ArrayList<String[]> _tripList;
	String[] _attributes;
	
	public InformationGainCalculator(ArrayList<String[]> tripList, String[] attributes){
		_tripList = tripList;
		_attributes = attributes;
	}

	/**
	 * Calculate the entropy for the entire dataset, Info(D).
	 * @param columnHeaders
	 * @return entropy for entire list
	 */
	public double CalculateEntropy(){
		
		int numberOfTrips = _tripList.size();
		Iterator it = _tripList.listIterator();
		Map<String, Integer> tripTypes = new HashMap<String, Integer>();
		while(it.hasNext()){
			String[] currTrip = (String[]) it.next();
			if(tripTypes.containsKey(currTrip[0])){
				tripTypes.put(currTrip[0], (int)tripTypes.get(currTrip[0])+1);
			}
			else{
				tripTypes.put(currTrip[0], 1);
			}
		}
		double value = 0;
		Set<String> s = tripTypes.keySet();
		
		//calculate entropy of Dataset
		for(String type : s){
			int num = tripTypes.get(type);
			double fraction = (double)num/numberOfTrips;
			value -= fraction * (Math.log(fraction)/Math.log(2));
		}
		
		
		return value;
	}
	
	/**
	 * Calculate the entropy of a specific attribute, InfoA(D). 
	 * @param index - the index of the attribute (stored in the attributes variable) to calculate
	 * @return entropy
	 */
	public double CalculateAttributeEntropy(int index){
		
		return 0;
		
	}
	
	
}
