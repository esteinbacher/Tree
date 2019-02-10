import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
public class Tester {
	private static HashMap<Double, Double> readXYFromCSV(String fileName){
		HashMap<Double,Double> data = new HashMap<Double, Double>();
		Path pathToFile = Paths.get(fileName);
		try(BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)){
			String line = br.readLine();
			line = br.readLine();
			while(line != null) {
				String[] attributes = line.split(",");
				
				data.put(Double.parseDouble(attributes[0]), Double.parseDouble(attributes[1]));
				line = br.readLine();
			}
			
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return data;
	}
	public static void main(String[] args) {
		
		
		HashMap data = readXYFromCSV("dataset1.csv");
		System.out.println(data.get(2.99));
	}

}
