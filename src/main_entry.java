import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class main_entry {
	public static void main(String args[])
	{
		if(args.length < 1){
			System.err.println("Please specify filename");
			System.exit(1);
		}
		
		String filename = "train2.csv"; 
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<String[]> tripList = new ArrayList<String[]>();
		
		try {

			br = new BufferedReader(new FileReader(filename));
			line = br.readLine(); //skip the first line that contains the titles of the columns.
			while ((line = br.readLine()) != null) {

			        // use comma as separator
				String[] trip = line.split(cvsSplitBy);
				tripList.add(trip);
//hello
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		
		System.out.println(filename);
		
		
		//I'm also thinking we need some sort of node class to
		//hold all the branches and decisions
	}
}
