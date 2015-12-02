import java.util.Set;

public class RuleItem {
	//keep track of departments, tripType, support, confidence
	private Set<String> _departments;
	private int _tripType;
	private double _support;
	private double _confidence;
	
	public RuleItem(Set<String> departments, int tripType){
		
		_departments = departments;
		_tripType = tripType;
		_support = 0;
		_confidence = 0;
	}
	
	void setSupport(double support){
		_support = support;
	}
	
	void setConfidence(double confidence){
		_confidence = confidence;
	}
	
	double getSupport(){
		return _support;
	}

	double getConfidence(){
		return _confidence;
	}

	Set<String> getDepartments()
	{
		return _departments;
	}

	int getTripType()
	{
		return _tripType;
	}
}
