import java.util.ArrayList;
import java.util.Map;

public class CBAMiner_returnpackage {
	
	private Map<String, ArrayList<Integer>> _CBAMatrix;
	private Map<String, Integer> _CBAMatrix_INDEX_nametoindex;
	private Map<Integer, String> _CBAMatrix_INDEX_indextoname;
	
	public CBAMiner_returnpackage(Map<String, ArrayList<Integer>> CBAMatrix, Map<String, Integer> CBAMatrix_index_s_i, Map<Integer, String> CBAMatrix_index_i_s){
		_CBAMatrix = CBAMatrix;
		_CBAMatrix_INDEX_nametoindex = CBAMatrix_index_s_i;
		_CBAMatrix_INDEX_indextoname = CBAMatrix_index_i_s;
	}
	
	public Map<String, Integer> get_matrixindex_nametoindex(){
		return _CBAMatrix_INDEX_nametoindex;
	}
	
	public Map<Integer, String> get_matrixINDEX_indextoname(){
		return _CBAMatrix_INDEX_indextoname;
	}
}