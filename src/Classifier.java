import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

public class Classifier 
{	
	private Map<Integer, ArrayList<ArrayList<Attribute>>> _data;
	private ArrayList<RuleItem> _rules;
	private Map<ArrayList<String>,Integer> _classifier;
	private ArrayList<Integer> _tripTypes;
	private double _accuracy;
	private int _default;
	private Map<Integer,ArrayList<Integer>> _mark;
	
	public Classifier(ArrayList<RuleItem> rules, Map<Integer, ArrayList<ArrayList<Attribute>>> data)
	{
		_data = data;
		_rules = rules;
		_classifier = new HashMap<ArrayList<String>,Integer>();
		_tripTypes = new ArrayList<Integer>();
		_mark = new HashMap<Integer,ArrayList<Integer>>();
		_accuracy = 0.0;
		_default = 0;
		CreateClassifier();
		addDefault();

		//testing
		Set<ArrayList<String>> setString = _classifier.keySet();
		Iterator<ArrayList<String>> it = setString.iterator();
		int len, i;
		ArrayList<String> temp;
		while (it.hasNext())
		{
			temp = it.next();
			len = temp.size();
			for (i = 0; i < len; i++)
				System.out.print(temp.get(i) + " ");
			System.out.println("" + _classifier.get(temp));
		}
	}

	private void addDefault()
	{
		Map<Integer,Integer> not = new HashMap<Integer,Integer>();

		int count;
		int trip, len, i,j,numTripTypes;
		ArrayList<Integer> visitList;
		int max = 0;
		int maxTrip = 999;

		numTripTypes = _tripTypes.size();

		for (j = 0; j < numTripTypes; j++)
		{
			count = 0;
			trip = _tripTypes.get(j);
			visitList = _mark.get(trip);
			len = visitList.size();

			for (i = 0; i < len; i++)
			{
				if (visitList.get(i) == 0)
					count++;
			}
			if (count > max) 
			{
				max = count;
				maxTrip = trip;
			}
		}	
		_default = maxTrip;
	}

	private void fill()
	{
		int trip, len, i,j,numTripTypes;
		ArrayList<Integer> visits;
		ArrayList<ArrayList<Attribute>> visitList;

		numTripTypes = _tripTypes.size();

		for (j = 0; j < numTripTypes; j++)
		{
			trip = _tripTypes.get(j);
			visits = new ArrayList<Integer>();
			visitList = _data.get(trip);
			len = visitList.size();

			for (i = 0; i < len; i++)
			{
				_accuracy += 1.0;
				visits.add(0);
			}
			_mark.put(trip, visits);
		}
	}

	private void CreateClassifier()
	{
		int a,i,j,r,s,t, trip, z, numTripTypes, isRule;
		RuleItem rule;
		ArrayList<ArrayList<Attribute>> visitList;
		ArrayList<Attribute> visit;
		ArrayList<Integer> temp;
		Set<Integer> tripTypes;
		Set<String> depts;
		String ruleTrip;
		Iterator<Integer> it;
		Iterator<String> sti;
		ArrayList<String> cond;


		tripTypes = _data.keySet();
		r = _rules.size();

		it = tripTypes.iterator();
		while (it.hasNext())
			_tripTypes.add(it.next());
		numTripTypes = _tripTypes.size();
		fill();
		isRule = 0;

		for (a = 0; a < r; a++)
		{
			rule = _rules.get(a);
			depts = rule.getDepartments();
			ruleTrip = rule.getTripType();

			for (z = 0; z < numTripTypes; z++)
			{
				trip = _tripTypes.get(z);

				if (trip != Integer.parseInt(ruleTrip))
				{
					continue;
				}

				visitList = _data.get(trip);
				s = visitList.size();
				for (i = 0; i < s; i++)
				{
					temp = _mark.get(trip);
					if (temp.get(i) == 1)
						continue;

					visit = visitList.get(i);
					t = visit.size();

					for (j = 0; j < t; j++)
					{
						if (depts.contains((visit.get(j)).department))		
							continue;
					}
					if (j == t)
					{
						isRule = 1;
						temp.set(i,1);
						_mark.put(trip,temp);
					}
				}
			}
				
			if (isRule == 1)
			{
				isRule = 0;
				cond = new ArrayList<String>();
				sti = depts.iterator();
				while (sti.hasNext())
					cond.add(sti.next());
				_classifier.put(cond, Integer.parseInt(ruleTrip));
			}
		}
	}

	public ArrayList<Map<Integer,Double>> classify (ArrayList<Set<String>> conditions)
	{
		ArrayList<Map<Integer,Double>> tripTypeProb = new ArrayList<Map<Integer,Double>>(); 
		Map<Integer,Double> prob;
		Iterator<ArrayList<String>> ait;
		Set<ArrayList<String>> keys = (_classifier.keySet());
		Set<String> sit; 
		ArrayList<String> cond;
		Iterator<Integer> nit;
		int numVisits = conditions.size();
		int i,j;
		int len;
		int apply = 1;
		int tripType;
		int count = 0;

		for (i = 0; i < numVisits; i++)
		{
			ait = keys.iterator();
			prob = new HashMap<Integer,Double>();
			while (ait.hasNext())
			{
				cond = ait.next();
				sit = conditions.get(i);
				len = cond.size();
				for (j = 0; j < len; j++)
				{
					if (sit.contains(cond.get(j)) == false)
					{
						apply = 0;
						break;
					}
				}
				if (apply == 1)
				{
					count++;
					tripType = _classifier.get(cond);
					if (prob.containsKey(tripType))
						prob.put(tripType, prob.get(tripType) + 1);
					else
						prob.put(tripType,1.0);
				}
				else apply = 1;
			}
			nit = (prob.keySet()).iterator();
			while (nit.hasNext())
			{
				tripType = nit.next();
				prob.put(tripType, (double) prob.get(tripType) / (double) count);
			}

			if (prob.isEmpty())
				prob.put(_default, 1.0);
			tripTypeProb.add(prob);
			count = 0;
		}

		return tripTypeProb;
	}
}	
