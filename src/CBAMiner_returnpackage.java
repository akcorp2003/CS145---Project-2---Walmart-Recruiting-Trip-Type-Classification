import java.util.ArrayList;
import java.util.Map;

public class CBAMiner_returnpackage {
	
	private Map<String, ArrayList<Integer>> _CBAMatrix;
	private Map<String, Integer> _CBAMatrix_index;
	
	public CBAMiner_returnpackage(Map<String, ArrayList<Integer>> CBAMatrix, Map<String, Integer> CBAMatrix_index){
		_CBAMatrix = CBAMatrix;
		_CBAMatrix_index = CBAMatrix_index;
	}
}
