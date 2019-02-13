import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
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
		Random rand = new Random();
		ArrayList<Tree> trees = new ArrayList<Tree>();
		HashMap<Double, Tree> errors = new HashMap<Double, Tree>();
		ArrayList<Double> e = new ArrayList<Double>();
		ArrayList<Double> training = new ArrayList<Double>();
		ArrayList<Double> testing = new ArrayList<Double>();
		for(int i = 0; i < 100; i++) {
			trees.add(new Tree(3));
		}
		ArrayList<Double> keys = new ArrayList<Double>();
		HashMap data = readXYFromCSV("dataset1.csv");

		keys.addAll(data.keySet());
		Collections.sort(keys);
		for(int x = 0; x < 4; x++) {
			while(training.size() < 750) {
				int index = rand.nextInt(1002);
				if(!training.contains(keys.get(index))) {
					training.add(keys.get(index));
				}
			}
			while(testing.size() < 251) {
				int index = rand.nextInt(1002);
				if(!testing.contains(keys.get(index))&&!training.contains(keys.get(index))) {
					testing.add(keys.get(index));
				}
			}



			for(Tree t: trees) {
				errors.put(t.squaredError(data, training),t);
			}
			ArrayList<Tree> mutations = new ArrayList<Tree>();
			ArrayList<Tree> crossovers = new ArrayList<Tree>();
			int gen = 0;
			double best = 100000000;
			while(!(best < .5 || gen >= 500)) {
				trees.removeAll(trees);
				crossovers.removeAll(crossovers);
				mutations.removeAll(mutations);

				for(int t = 0; t < 10; t++) {
					Tree t1 = errors.get(Collections.min(errors.keySet()));
					errors.remove(Collections.min(errors.keySet()));
					crossovers.add(t1);
					mutations.add(t1);
					trees.add(t1);
				}

				for(int i = 0; i < 10; i++) {
					Tree t1 = new Tree(Tree.copy(crossovers.get(i).myRoot));
					for(int j = 0; j <5; j++) {
						Tree t2 = new Tree(Tree.copy(crossovers.get(j).myRoot));
						if(t1.myRoot.equals(t2.myRoot)) {
							Tree.crossover(t1, t2);
							t1.depthLimit(12);
							t2.depthLimit(12);
							trees.add(t1);
							trees.add(t2);
						}
					}
				}

				for(int j = 0; j < 5; j++) {
					for(int m = 0; m < 10; m++) {
						Tree t = new Tree(Tree.copy(mutations.get(m).myRoot));

						Tree.mutate(t);

						trees.add(t);
					}
				}
				best = Collections.min(errors.keySet());
				errors = new HashMap<Double, Tree>();
				//System.out.println(gen);
				for(Tree t: trees) {
					//t.printTree();
					errors.put(t.squaredError(data, training),t);
				}
				//System.out.println(gen);
				gen++;


			}
			Tree t = errors.get(Collections.min(errors.keySet()));
			System.out.println(Collections.min(errors.keySet()));
			System.out.println(gen);
			System.out.println(t.squaredError(data, testing));


		}
	}

}
