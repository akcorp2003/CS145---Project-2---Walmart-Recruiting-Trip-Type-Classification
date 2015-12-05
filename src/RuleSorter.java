import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class RuleSorter {
	
	private ArrayList<RuleItem> _original;

	public RuleSorter(ArrayList<RuleItem> rules)
	{
		_original = rules;
	}

	private int partition(ArrayList<RuleItem> rules, int pos, int left, int right)
	{
		int i = left;
		int j = right;
		RuleItem tmp;
		double pivot;

		if (pos == 0)
			pivot = (rules.get((left + right) / 2)).getConfidence();
		else
			pivot = (rules.get((left + right) / 2)).getSupport();

		while (i <= j)
		{
			if (pos == 0)
			{
				while ( (rules.get(i)).getConfidence() > pivot)
					i++;
				while ( (rules.get(j)).getConfidence() < pivot)
					j--;
			}
			else
			{
				while ( (rules.get(i)).getSupport() > pivot)
					i++;
				while ( (rules.get(j)).getSupport() < pivot)
					j--;
			}

			if (i <= j) 
			{
				tmp = rules.get(i);
				rules.set(i, rules.get(j));
				rules.set(j, tmp);
				i++;
				j--;
			}
		}
		return i;
	}

	// pos 0 confidence, pos 1 support
	// quick sort on either confidence or support
	private void quickSort(ArrayList<RuleItem> rules,int pos, int left, int right)
	{
		int index = 0;
		if (pos != 1)
			index = partition(rules, pos, left, right);

		if (pos == 0 || pos == 2)
		{
			if (left < index - 1)
				quickSort(rules, pos, left, index - 1);
			if (index < right)
				quickSort(rules, pos, index, right);
		}
		else
		{
			ArrayList<RuleItem> whole = new ArrayList<RuleItem>();
			ArrayList<RuleItem> part;
			int len = rules.size();
			int i,count = 0;
			RuleItem cur;
			RuleItem prev;

			prev = rules.get(0);
			for (i = 1; i < len; i++)
			{
				cur = rules.get(i);
				if (cur.getConfidence() == prev.getConfidence())
				{
					if (i == len - 1)
						quickSort(rules,2,count,i);
				}
				else
				{
					quickSort(rules,2, count,i-1);
					count = i;
				}
				prev = cur;
			}
		}
	}
	
	private int partitionPart(ArrayList<RuleItem> rules, int left, int right)
	{
		int i = left;
		int j = right;
		RuleItem tmp;
		double pivot;

		pivot = ((rules.get((left + right) / 2)).getDepartments()).size();

		while (i <= j)
		{
			while ( ((rules.get(i)).getDepartments()).size() < pivot)
				i++;
			while ( ((rules.get(j)).getDepartments()).size() > pivot)
				j--;

			if (i <= j) 
			{
				tmp = rules.get(i);
				rules.set(i, rules.get(j));
				rules.set(j, tmp);
				i++;
				j--;
			}
		}
		return i;
	}

	private void quickSortPart(ArrayList<RuleItem> rules, int left, int right)
	{
		int index = 0;
		index = partitionPart(rules,left,right);
		if (left < index - 1)
			quickSortPart(rules, left, index - 1);
		if (index < right)
			quickSortPart(rules, index, right);
	}

	private void quickSortGenOrder(ArrayList<RuleItem> rules)
	{
		int len = rules.size();
		int i,count = 0;
		RuleItem cur;
		RuleItem prev;

		prev = rules.get(0);
		for (i = 1; i < len; i++)
		{
			cur = rules.get(i);
			if (cur.getConfidence() == prev.getConfidence())
			{
				if (cur.getSupport() == prev.getSupport())
				{
					if (i == len - 1)
						quickSortPart(rules,count,i);
				}
				else
				{
					quickSortPart(rules, count,i-1);
					count = i;
				}
			}
			else
			{
				quickSortPart(rules, count,i-1);
				count = i;
			}
			prev = cur;
		}
	}
	
	public ArrayList<RuleItem> Sort()
	{
		ArrayList<RuleItem> rules = new ArrayList<RuleItem>();
		int i = 0;
		int len = _original.size();

		for (i = 0; i < len; i++)
			rules.add(_original.get(i));

		quickSort(rules,0,0, len - 1);
		quickSort(rules,1,0, len - 1);
		quickSortGenOrder(rules);
		
		return rules;
	}
}
