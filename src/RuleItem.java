import java.util.Set;

public class RuleItem {
	//keep track of departments, tripType, support, confidence
	private Set<String> _departments;
	private String _tripType;
	private double _support;
	private double _confidence;
	
	public RuleItem(Set<String> departments, String tripType){
		
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

	String getTripType()
	{
		return _tripType;
	}
	
	@Override
    	public boolean equals(Object object)
    	{
		if (this == null || object == null)
			return false;

		Set<String> depts;
		Set<String> depts2;
		RuleItem rule;
		if (object instanceof RuleItem)
		{
			rule = (RuleItem) object;
			if ((this.getTripType()).equals(rule.getTripType()))
			{
				depts = this.getDepartments();
				depts2 = rule.getDepartments();
				for (String dept : depts)
					if (depts2.contains(dept) == false)
						return false;

				for (String dept2 : depts2)
					if (depts.contains(dept2) == false)
						return false;
				return true;
			}
			else
				return false;
		}
		else
			return false;
    	}
}
