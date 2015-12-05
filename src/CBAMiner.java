import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CBAMiner {
	
	/*
	 *private class Attribute {
	 *public String department;
	public int count;
	
	public Attribute(String department, int count){
		this.department = department;
		this.count = count;
	} 
	}
	 */
	private ArrayList<String[]> _tripList;
	private String[] _headers;
	//private Map<String, Integer> _columnindex_1; //provides an index of what columns represent in our matrix
	//private Map<Integer, String> _columnindex_2; //provides an index of what columns represent in our matrix
	//private Map<String, Integer> _rowindex; //provides an index of what rows represent in our matrix
	private Map<Integer, ArrayList<ArrayList<Attribute>>> _CBAmatrix; //the key is the integer (triptype for our case), and the arraylist holds an arraylist for each visit associated to the key
	
	private Map<String, Integer> _DepartmentCountMap; //maps all the different departments to the total number of occurences of each department in the matrix
	
	public CBAMiner(ArrayList<String[]> tripList, String[] headers){
		_tripList = tripList;
		_headers =headers;
		_CBAmatrix = new HashMap<Integer, ArrayList<ArrayList<Attribute>>>();
		_DepartmentCountMap = new HashMap<String, Integer>();
		
	}
	
	/**
	 * Builds the matrix for the _tripList ArrayList that was given to the class
	 * Essentially builds the matrix from the raw data
	 * @return A 2D array of the resulting matrix
	 */
public Map<Integer, ArrayList<ArrayList<Attribute>>> buildMatrix(){
		
		//ArrayList<ArrayList<Integer>> CBAmatrix = new ArrayList<ArrayList<Integer>>();
		System.out.print("in process of building matrix\n");
		/*Map<String, Integer> Departments = new HashMap<String, Integer>();
		
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
		CBAMiner_returnpackage CBAmatrix = new CBAMiner_returnpackage(_CBAmatrix, _columnindex_1, _columnindex_2);*/
		String visitnumber = "";
		
		for(String[] trip : _tripList){
			int triptype = Integer.parseInt(trip[0]);
			if(trip[5].equals("NULL")){
				continue; //there are some values in the training data where the department is null, ignore those
			}
			if(trip[1].equals(visitnumber)){
				if(_CBAmatrix.containsKey(triptype)){
					//get the last arraylist since that is the arraylist we're currently inserting
					ArrayList<ArrayList<Attribute>> visit_row = _CBAmatrix.get(triptype);
					int working_row_index = visit_row.size() - 1;
					ArrayList<Attribute> working_row = visit_row.get(working_row_index);
					
					//find if the attribute object exists in the working row
					boolean contains = false;
					for(Attribute attribute : working_row){
						if(attribute.department.equals(trip[5])){
							attribute.count++;
							AddDepartmentToMap(trip[5]);
							contains = true;
							break;
						}
					}
					
					if(!contains){
						working_row.add(new Attribute(trip[5], 1));
						AddDepartmentToMap(trip[5]);
					}
					
					/*if(!working_row.contains(trip[5])){
						working_row.add(trip[5]); //insert the department name
					}*/
					//we only want each department to appear once for every visit
					
					//since all objects are references, java should have updated the working_row for us
				}
				else{
					//this section may never be reached because if we are still working on the same visitnumber
					//then triptype must exist in _CBAmatrix, this section is here just in case if it ever changes
					//that a visitnumber will have 2 different triptypes
					ArrayList<ArrayList<Attribute>> new_triptype = new ArrayList<ArrayList<Attribute>>();
					ArrayList<Attribute> new_row = new ArrayList<Attribute>();
					new_row.add(new Attribute(trip[5], 1));
					new_triptype.add(new_row);
					AddDepartmentToMap(trip[5]);
					_CBAmatrix.put(triptype, new_triptype);
				}
			}
			else {
				//we're starting a new visitnumber
				visitnumber = trip[1];
				if(_CBAmatrix.containsKey(triptype)){
					//we'll need to build a new ArrayList<Attribute> since it's a new trip!
					ArrayList<ArrayList<Attribute>> visit_row = _CBAmatrix.get(triptype);
					ArrayList<Attribute> new_working_row = new ArrayList<Attribute>();
					new_working_row.add(new Attribute(trip[5], 1));
					visit_row.add(new_working_row);
					AddDepartmentToMap(trip[5]);
				}
				else{
					ArrayList<ArrayList<Attribute>> new_triptype = new ArrayList<ArrayList<Attribute>>();
					ArrayList<Attribute> new_row = new ArrayList<Attribute>();
					new_row.add(new Attribute(trip[5], 1));
					new_triptype.add(new_row);
					_CBAmatrix.put(triptype, new_triptype);
					AddDepartmentToMap(trip[5]);
				}
			}//end else
		}//end for
		
		
		//the matrix is constructed, let's jus t sort everything in there
		//sorting could be a different monster with custom classes...
		Set<Integer> working_triptype = _CBAmatrix.keySet();
		for(Integer trip : working_triptype){
			ArrayList<ArrayList<Attribute>> visit_row = _CBAmatrix.get(trip);
			for(ArrayList<Attribute> row : visit_row){
				Collections.sort(row, new AttributeComparator());
			}
		}
		//in the future, if anything funky happens when querying the attributes, it maybe because of this sort function...
		//now we have our matrix constructed and sorted!
		
		
		//build the DepartmentCountMap
		
		
		return _CBAmatrix;
	}

	/**
	 * Increments the count of a department in the DepartmentCountMap.
	 * @param department The department
	 */
	private void AddDepartmentToMap(String department){
		if(_DepartmentCountMap.containsKey(department)){
			_DepartmentCountMap.put(department, _DepartmentCountMap.get(department)+1);
		}
		else{
			_DepartmentCountMap.put(department, 1);
		}
	}

	public Map<Integer, ArrayList<ArrayList<Attribute>>> get_CBAmatrix(){
		return _CBAmatrix;
	}

	/**
	 * Verifies that for all classes in the matrix, there is at least a value of 1 in that class
	 * OBSOLETE
	 * @return
	 */
	public boolean checkMatrix(){
		/*Set<String> classes = _CBAmatrix.keySet();
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
		
		return true;*/
		return false;
	}
	
	//generate size 1 possible rule items
	/**
	 * Generates size 1 possible rule items.
	 * @return An ArrayList of RuleItems
	 */
	public ArrayList<RuleItem> generateRules(){
		ArrayList<RuleItem> rules = new ArrayList<RuleItem>();
		Set<Integer> classes = _CBAmatrix.keySet();
		RuleItem rule;
		//loop through each row of matrix
		for(int triptype : classes){
			ArrayList<ArrayList<Attribute>> triptype_row = _CBAmatrix.get(triptype);
			//iterate through the row and check for 1
			for(ArrayList<Attribute> visitList : triptype_row){
				//if 1, create a rule item and add to arraylist
				for(Attribute attr : visitList){	
					if(attr.count > 0){
						Set<String> depts = new HashSet<String>();
						depts.add(attr.department);
						rule = new RuleItem(depts,Integer.toString(triptype));
						if (rules.contains(rule) == false)
							rules.add(new RuleItem(depts,Integer.toString(triptype)));
					}
				}
			}
			
		}
		return rules;
	}
	
	//generate n+1-rules from n-candidates
	/**
	 * Generates n+1 rules from n-candidates
	 * @param prevRules The rules previously generated. Can be of any size.
	 * @param n The number of candidates.
	 * @return
	 */
	public ArrayList<RuleItem> generateRulesFromCandidates(ArrayList<RuleItem> prevRules, int n){
		//create a selfJoin list that will hold all the itemsets after self joining
		ArrayList<RuleItem> selfJoin = new ArrayList<RuleItem>();
		for(int i = 0; i < prevRules.size(); i++){
			for(int j = i+1; j < prevRules.size(); j++){
				
				RuleItem rule1 = prevRules.get(i);
				RuleItem rule2 = prevRules.get(j);
				ArrayList<String> departments1 = prevRules.get(i).getDepartmentsAsArray();
				ArrayList<String> departments2 = prevRules.get(j).getDepartmentsAsArray();
				if(rule1.getTripType().equals(rule2.getTripType())){
				//for length 2 candidates
					if(n == 2){
						
							String cand1 = departments1.get(0);
							String cand2 = departments2.get(0);
							
							//check that the two candidates are not part of the same attribute
							//as a trip cannot possible contain two values from the same attribute
		//					if(!_attributeMap.get(cand1).equals(_attributeMap.get(cand2))){
		//						ArrayList<String> newList = new ArrayList<String>();
		//						newList.add(cand1);
		//						newList.add(cand2);
		//						selfJoin.add(newList);
				//			}
							Set<String> newList = new HashSet<String>();
							newList.add(cand1);
							newList.add(cand2);
							selfJoin.add(new RuleItem(newList, rule1.getTripType()));
						
					}
					else{
						
						boolean same = true;
						for(int k = 0; k < n-2; k++){
							
							if(departments1.get(k) != departments2.get(k)){
								same = false;
								break;
							}
							
						}
						if(same){
							Set<String> newList = new HashSet<String>();
							for(int k = 0; k < n-2; k++){
								newList.add(departments1.get(k));
							}
							newList.add(departments1.get(n-2));
							newList.add(departments2.get(n-2));
							selfJoin.add(new RuleItem(newList, rule2.getTripType()));
						}
					}
				}
			}
		}
		
		return selfJoin;
		
		/*ArrayList<RuleItem> potentials = new ArrayList<RuleItem>();
		ArrayList<Integer> counts = new ArrayList<Integer>();
		for (int i = 0; i < prevRules.size(); i++){
			for (int j = i + 1; j < prevRules.size(); j++){
				RuleItem rule1 = prevRules.get(i);
				RuleItem rule2 = prevRules.get(j);
				if(!rule1.getTripType().equals(rule2.getTripType()))
					continue;
				String tripType = rule1.getTripType();
				Set<String> union = new HashSet<String>(rule1.getDepartments());
				union.addAll(rule2.getDepartments());
				if(union.size() == n + 1){
					boolean found = false;
					for (int x = 0; x < counts.size(); x++){
						if (potentials.get(x).getDepartments().equals(union) &&
								potentials.get(x).getTripType().equals(tripType)){
							counts.set(x,counts.get(x) + 1);
							found = true;
						}
					}
					if (!found){
						potentials.add(new RuleItem(union,tripType));
						counts.add(1);
					}
				}
			}
		}
		ArrayList<RuleItem> outputRules = new ArrayList<RuleItem>();
		for (int i = 0; i < counts.size(); i++){
			if(counts.get(i) == (n * (n+1) / 2)){
				outputRules.add(potentials.get(i));
			}
		}
		return outputRules;*/
	}
	
	/**
	 * Removes infrequent rules from the ArrayList of generatedRules.
	 * The minimum support and minimum confidence are defined within the function.
	 * @param generatedRules Rules generated by some other function.
	 * @return The original ArrayList but potentially with rules removed.
	 */
	public ArrayList<RuleItem> pruneInfrequentRules(ArrayList<RuleItem> generatedRules, int flag){
		double minSup = (double)6/28; //can set to whatever value we want
		double minConf = 0.3;
		if (flag == 1)
			minConf = 1.0;
		ArrayList<RuleItem> gr_temp = generatedRules;
		for (Iterator<RuleItem> rule = generatedRules.iterator(); rule.hasNext(); ){/*RuleItem: gr_temp*///){
			//if don't pass minimum support or minimum confidence
			RuleItem curr_rule = rule.next();
			if ((curr_rule.getSupport() < minSup) || (curr_rule.getConfidence() < minConf)){
				//remove from list
				//gr_temp.remove(rule); illegal in Java
				rule.remove();
				
			}
		}
		return generatedRules;
	}
	
	/**
	 * Computes the Support and Confidence of the possibleRules
	 * @param possibleRules An ArrayList of the rules generated previously
	 * @return
	 */
	public ArrayList<RuleItem> computeSupAndConf(ArrayList<RuleItem> possibleRules){
		//for each rule item 
		int totalTripNumber = _tripList.size();
		
		
		for (RuleItem rule : possibleRules){
			//compute support and confidence of item sets 
			//set support and confidence of rules
			int supportNumerator = 0;
			int supportDenominator = totalTripNumber;
			int confidenceNumerator = 0;
			int confidenceDenominator = 0;
			
			int min = Integer.MAX_VALUE;
			for(String dmp : rule.getDepartments()){
				int tempSup = _DepartmentCountMap.get(dmp);
				if(tempSup < min){
					min = tempSup;
				}
			}
			confidenceDenominator = min;
			Set<String> departments = rule.getDepartments();
			int numDepartments = departments.size();
			//Set<Integer> tripTypes = _CBAmatrix.keySet();
			
			//to find the numerators
			ArrayList<ArrayList<Attribute>> visitList = _CBAmatrix.get(Integer.parseInt(rule.getTripType()));
			
			for(ArrayList<Attribute> visit : visitList){
				int departmentCount = 0;
				int tempCount = 0;
				
				
				int minSup = Integer.MAX_VALUE;
				
				
				//this section checks that all the departments in rule.getDepartments are present in this particular visit
				/* If there are 2+ departments, checks that each department is present in the visit, then takes the minimum 
				 * count of those two departments to the as the support
				 */
				for(Attribute attribute : visit){
					for(String department : departments){
						
						if(attribute.department.equals(department)){
							departmentCount++;
							
							if(attribute.count < minSup){
								tempCount = attribute.count;
							}
							//if all departments are present
							if(departmentCount == numDepartments){
								supportNumerator += tempCount;
								confidenceNumerator += tempCount;
							}
						}//end if
						
					}//end department : departments for
				}//end attribute : visit for
			}
			
			double support = (double)supportNumerator/supportDenominator;
			double confidence = (double)confidenceNumerator/confidenceDenominator;
			if(support>1 || confidence>1){
				System.err.println("something bad happened");
			}
			rule.setSupport(support);
			rule.setConfidence(confidence);
			
		}
		return possibleRules;
	}
}
