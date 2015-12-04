import java.util.ArrayList;

public class CSVfile_returnpackage {
	
	private ArrayList<String[]> _visits;
	private String[] _columnHeaders;
	
	public CSVfile_returnpackage(ArrayList<String[]> visits, String[] columnHeaders){
		_visits = visits;
		_columnHeaders = columnHeaders;
	}
	
	public ArrayList<String[]> get_visitsArrayList(){
		return _visits;
	}
	
	public String[] get_columnHeaders(){
		return _columnHeaders;
	}
}
