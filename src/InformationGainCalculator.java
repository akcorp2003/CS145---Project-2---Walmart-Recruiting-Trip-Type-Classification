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
		}//end while
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
		
		int numberOfTrips = _tripList.size();
		Iterator it = _tripList.listIterator();
		Map<String, Integer> attributeTypes = new HashMap<String, Integer>();
		
		//stores the number of different triptypes that occur in that attribute
		Map<String, HashMap<String, Integer>> attr_triptypes = new HashMap<String, HashMap<String, Integer>>();
		
		while(it.hasNext()){
			String[] currTrip = (String[]) it.next();
			if(attributeTypes.containsKey(currTrip[index])){
				attributeTypes.put(currTrip[index], (int)attributeTypes.get(currTrip[index])+1);
			}
			else{
				attributeTypes.put(currTrip[index], 1);
			}
			
			//check for occurences in attr_triptypes
			if(attr_triptypes.containsKey(currTrip[index])){
				HashMap<String, Integer> trip_occurences = attr_triptypes.get(currTrip[index]);
				//the index 0 always refers to the TripType
				if(trip_occurences.containsKey(currTrip[0])){
					trip_occurences.put(currTrip[0], (int)trip_occurences.get(currTrip[0])+1);
					attr_triptypes.put(currTrip[index], trip_occurences);
				}
				else{
					trip_occurences.put(currTrip[0], 1);
					attr_triptypes.put(currTrip[index], trip_occurences);
				}
			}
			else{
				//we do not necessarily need this, just in case something went awry in the last if-else
				HashMap<String, Integer> triptypes_temp = new HashMap<String, Integer>();
				triptypes_temp.put(currTrip[0], 1);
				attr_triptypes.put(currTrip[index], triptypes_temp);
			}//end else
			
		}//end while
		
		double value = 0;
		Set<String> s_attrtriptypes = attr_triptypes.keySet();
		Set<String> s_attributeTypes = attributeTypes.keySet();
		
		//calculate the entropy of the attribute
		for(String attr : s_attributeTypes){
			//first compute the coefficient value
			int num = attributeTypes.get(attr);
			double outer_fraction = (double)num / numberOfTrips;
			
			//compute the inner entropy values
			double inner_value = 0;
			HashMap<String, Integer> triptypes_temp2 = attr_triptypes.get(attr);
			Set<String> s_attr_triptypes_temp2 = triptypes_temp2.keySet();
			for(String trip : s_attr_triptypes_temp2){
				int num_occur = triptypes_temp2.get(trip);
				double fraction = (double)num_occur/num;
				inner_value -= fraction * (Math.log(fraction)/Math.log(2));
				
			}//end for
			
			value += (outer_fraction * inner_value);
		
		}//end for
		
		return value;
		
	}
	
	
}
