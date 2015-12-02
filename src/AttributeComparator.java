import java.util.Comparator;

public class AttributeComparator implements Comparator<Attribute> {
	@Override
	public int compare(Attribute o1, Attribute o2){
		return o1.department.compareTo(o2.department);
	}

}
