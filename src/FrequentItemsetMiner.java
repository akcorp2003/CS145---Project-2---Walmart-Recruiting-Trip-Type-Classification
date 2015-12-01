import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FrequentItemsetMiner {
	
	private ArrayList<String[]> _dataset;
	private String[] _headers;
	private Map<String, Integer> _attributeMap;
	
	
	public FrequentItemsetMiner(ArrayList<String[]> dataset, String[] headers){
		
		_dataset = dataset;
		_headers = headers;
		_attributeMap = new HashMap<String, Integer>();
	}
	
	/**
	 * Use Apriori to find frequent itemsets
	 * @param minSupCount
	 * @param confidence
	 * @return a Map of all frequent itemsets and their supports.
	 */
	public Map<String, Integer> Mine(double minSupCount, double confidence) {
		System.out.print("mining!\n");

		//first find length 1 candidates
		Map<String, Integer> supCounts = MineCandidates(null, minSupCount, 1);		
		Map<String, Integer> allCandidates = supCounts;
		System.out.print("done mining all frequent departments\n");
//		int candidateNumber = 2;
//		
//		//find length x candidates where x > 1
//		while(!supCounts.isEmpty()){
//			ArrayList<ArrayList<String>> prevCandidates = new ArrayList<ArrayList<String>>();
//			for(ArrayList<String> type : supCounts.keySet()){
//				prevCandidates.add(type);
//			}
//			supCounts = MineCandidates(prevCandidates, minSupCount, candidateNumber);
//			allCandidates.putAll(supCounts);
//			candidateNumber++;
//		}
		
		
		return allCandidates;
		
	}
	
	/**
	 * Mines for the candidates of size x given candidates of size x-1. 
	 * @param prevCandidates: If x == 1, prevCandidates is null.
	 * @param minSupCount: an absolute value instead of a percentage.
	 * @param x
	 * @return
	 */
	private Map<String, Integer> MineCandidates(ArrayList<ArrayList<String>> prevCandidates, double minSupCount, int x){
		
		//if we are interested in mining itemset candidates of size 1
		//currently only "department" and "weekdays" attributes are considered.
		if(x == 1){
			System.out.print("inside mineCandidates function\n");
			//Map<ArrayList<String>, Integer> candidates = new HashMap<ArrayList<String>,Integer>();
			Map<String, Integer> candidates = new HashMap<String,Integer>();
			for(String[] trip : _dataset){
				
				//add values from the department attribute to the list
				//ArrayList<String> list = new ArrayList<String>();
				String list = trip[5];
				//list.add(trip[5]);
				_attributeMap.put(trip[5], 5);
				if(candidates.containsKey(list)){
					
					candidates.put(list, (int)candidates.get(list)+1);
				}
				else{
					candidates.put(list, 1);
				}
				
//				//add values from the weekdays attribute to the list
//				ArrayList<String> list2 = new ArrayList<String>();
//				list2.add(trip[2]);
//				_attributeMap.put(trip[2], 2);
//				if(candidates.containsKey(list2)){
//					
//					candidates.put(list2, (int)candidates.get(list2)+1);
//				}
//				else{
//					candidates.put(list2, 1);
//				}
			}
			
			//prune infrequent items
			Map<String, Integer> prunedCandidates = PruneMap(candidates, minSupCount); 
			return prunedCandidates;
		}
		
//		//for x>1 length candidates
//		else if(x>1){
//
//			//create a selfJoin list that will hold all the itemsets after self joining
//			ArrayList<ArrayList<String>> selfJoin = new ArrayList<ArrayList<String>>();
//			for(int i = 0; i < prevCandidates.size(); i++){
//				for(int j = i+1; j < prevCandidates.size(); j++){
//					
//					//for length 2 candidates
//					if(x == 2){
//						String cand1 = prevCandidates.get(i).get(0);
//						String cand2 = prevCandidates.get(j).get(0);
//						
//						//check that the two candidates are not part of the same attribute
//						//as a trip cannot possible contain two values from the same attribute
//						if(!_attributeMap.get(cand1).equals(_attributeMap.get(cand2))){
//							ArrayList<String> newList = new ArrayList<String>();
//							newList.add(cand1);
//							newList.add(cand2);
//							selfJoin.add(newList);
//						}
//					}
//					else{
//						boolean same = true;
//						for(int k = 0; k < x-2; k++){
//							
//							if(prevCandidates.get(i).get(k) != prevCandidates.get(j).get(k)){
//								same = false;
//							}
//							
//						}
//						if(same){
//							ArrayList<String> newList = new ArrayList<String>();
//							for(int k = 0; k < x-2; k++){
//								newList.add(prevCandidates.get(i).get(k));
//							}
//							newList.add(prevCandidates.get(i).get(x-2));
//							newList.add(prevCandidates.get(j).get(x-2));
//							selfJoin.add(newList);
//						}
//					}
//				}
//			}
//			
//			
//			//find support of all itemsets in self joined list
//			Map<ArrayList<String>, Integer> candidates = new HashMap<ArrayList<String>,Integer>(); 
//			
//			triploop:
//			for(String[] trip : _dataset){
//				
//				for(ArrayList<String> attributeInJoinList : selfJoin){
//					
//					
//					int matching = 0;
//					int count = 0;
//					for(String attributeValue : trip){
//						if(count != 1 && count != 5){
//							count++;
//							continue;
//						}
//						for(String attribute : attributeInJoinList){
//							if(attribute.equals(attributeValue)){
//								matching++;
//							}
//						}
//						
//					}
//					if(matching == x){
//						if(candidates.containsKey(attributeInJoinList)){
//							candidates.put(attributeInJoinList, (int)candidates.get(attributeInJoinList)+1);
//						}
//						else{
//							candidates.put(attributeInJoinList, 1);
//						}	
//						continue triploop;
//					}	
//				}
//			}
//			
//			//prune infrequent items
//			Map<ArrayList<String>, Integer> prunedCandidates = PruneMap(candidates, minSupCount);
//			return prunedCandidates;
//			
//		}
		else{
			System.err.println("Program should not reach here");
			System.exit(1);
			return null;
		}
	}
	
	/**
	 * Used in the MineCandidates function, this function prunes all infrequent itemsets from the Map.
	 * @param candidates: a Map of all itemsets
	 * @param minSup: the min support threshold
	 * @return a Map of itemsets whose support >= min support threshold
	 */
	private Map<String, Integer> PruneMap(Map<String, Integer> candidates, double minSup){
		System.out.print("pruning now\n");
		Set<String> keySet = candidates.keySet();
		//System.out.print(keySet.size());
		//Map<ArrayList<String>, Integer> prunedCandidates = new HashMap<ArrayList<String>,Integer>(); 
		Map<String, Integer> prunedCandidates = new HashMap<String,Integer>(); 
		
		for(String type : keySet){
			int number = candidates.get(type);
			System.out.print(type);
			System.out.print(": ");
			System.out.print(number);
			System.out.print("\n");
			if(!(number < minSup)){
				prunedCandidates.put(type, number);
			}
		}
		System.out.print("number of departments left:");
		System.out.print(prunedCandidates.size());
		System.out.print("\n");
		return prunedCandidates;
	}
	

}