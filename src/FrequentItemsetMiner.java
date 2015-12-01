import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FrequentItemsetMiner {
	
	private ArrayList<String[]> _dataset;
	private String[] _headers;
	
	public FrequentItemsetMiner(ArrayList<String[]> dataset, String[] headers){
		
		_dataset = dataset;
		_headers = headers;
		
	}
	
	/**
	 * Use Apriori to find frequent itemsets
	 * @param minSupCount
	 * @param confidence
	 * @return
	 */
	public Map<String[], Integer> Mine(double minSupCount, double confidence) {
		
		ArrayList<String[]> prevCandidates = _dataset;
		
		Map<ArrayList<String>, Integer> supCounts = MineCandidates(null, minSupCount, 1);
		
		//only count department for now
		
		
		return null;
		
	}
	
	/**
	 * Mines for the candidates of size x given candidates of size x-1. 
	 * @param prevCandidates: If x == 1, prevCandidates is null.
	 * @param minSupCount: an absolute value instead of a percentage.
	 * @param x
	 * @return
	 */
	private Map<ArrayList<String>, Integer> MineCandidates(ArrayList<String[]> prevCandidates, double minSupCount, int x){
		
		//if we are interested in mining candidates of size 1
		if(x == 1){
			
			Map<ArrayList<String>, Integer> candidates = new HashMap<ArrayList<String>,Integer>();
			for(String[] trip : _dataset){
				ArrayList<String> list = new ArrayList<String>();
				list.add(trip[5]);
				//ArrayList<String> immutableList = new ArrayList<String>(Collections.unmodifiableList(list));
				
				if(candidates.containsKey(list)){ //department
					
					candidates.put(list, (int)candidates.get(list)+1);
				}
				else{
					candidates.put(list, 1);
				}
			}
			
			//prune infrequent items
			Set<ArrayList<String>> keySet = candidates.keySet();
			Map<ArrayList<String>, Integer> prunedCandidates = new HashMap<ArrayList<String>,Integer>(); 
			for(ArrayList<String> type : keySet){
				int number = candidates.get(type);
				if(!(number < minSupCount)){
					prunedCandidates.put(type, number);
				}
			}
					
			return prunedCandidates;
		}
		
		else if(x>1){

			ArrayList<String[]> selfJoin = new ArrayList<String[]>();
			for(int i = 0; i < prevCandidates.size(); i++){
				for(int j = i+1; j < prevCandidates.size(); j++){
					if(x == 2){
						selfJoin.add(new String[]{prevCandidates.get(i)[0], prevCandidates.get(j)[0]});
					}
					else{
						boolean same = true;
						for(int k = 0; k < x-2; k++){
							
							if(prevCandidates.get(i)[k] != prevCandidates.get(j)[k]){
								same = false;
							}
							
						}
						if(same){
							
						}
					}
				}
			}
			/*for(String[] typeArray : keySet){
				for(String type : typeArray){
					
				}*/
			
			
			
		}
		
		
		return null;
		
	}
	

}
