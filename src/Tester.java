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

		//1. instantiate variables: random, pop array, map of data, training lists
		Random rand = new Random();
		ArrayList<Tree> trees = new ArrayList<Tree>();
		HashMap<Double, Tree> errors = new HashMap<Double, Tree>();
		ArrayList<Double> e = new ArrayList<Double>();
		ArrayList<Double> training = new ArrayList<Double>();
		ArrayList<Double> testing = new ArrayList<Double>();

		//2. create intial trees
		for(int i = 0; i < 500; i++) {
			trees.add(new Tree(3));
		}

		ArrayList<Double> keys = new ArrayList<Double>();
		HashMap data = readXYFromCSV("dataset1.csv");

		//3. divide up data into training and testing

		//From Paper, can CITE
		keys.addAll(data.keySet());
		Collections.sort(keys);

		while(training.size() < 701) {
			int index = rand.nextInt(1002);
			if(!training.contains(keys.get(index))) {
				training.add(keys.get(index));
			}
		}
		while(testing.size() < 301) {
			int index = rand.nextInt(1002);
			if(!testing.contains(keys.get(index))&&!training.contains(keys.get(index))) {
				testing.add(keys.get(index));
			}
		}

		//4. find all mean Squared errors, and put into errors map, with squared error as key
		for(Tree t: trees) {
			errors.put(t.meanSquaredError(data, training),t);
		}
		ArrayList<Tree> mutations = new ArrayList<Tree>();
		ArrayList<Tree> crossovers = new ArrayList<Tree>();
		int gen = 0;
		double best = 100000000;

		//5. keep making new generations until squared error below .5, or # of gens hits 200
		while(!(best < .05 || gen >= 200)) {
			trees = new ArrayList<Tree>();
			training = new ArrayList<Double>();
			while(training.size() < 601) {
				int index = rand.nextInt(701);
				if(!training.contains(keys.get(index))) {
					training.add(keys.get(index));
				}
			}


			//1. get top 100 trees in population, reproduce (trees), and add to crossovers and mutations
			for(int t = 0; t < 100; t++) {
				Tree t1 = errors.get(Collections.min(errors.keySet()));
				errors.remove(Collections.min(errors.keySet()));
				crossovers.add(t1);
				mutations.add(t1);

				trees.add(t1);

			}
			//System.out.println("r " + trees.size());

		//2. cross the top 40 trees, and cross each with the best 10
		for(int i = 0; i < 40; i++) {
			Tree t1 = new Tree(Tree.copy(crossovers.get(i).myRoot));
			for(int j = 0; j <10; j++) {
				Tree t2 = new Tree(Tree.copy(crossovers.get(j).myRoot));

				Tree.crossover(t1, t2);
				t1.depthLimit(12);
				t2.depthLimit(12);
				trees.add(t1);
				trees.add(t2);


			}
		}
		//System.out.println("c " +trees.size());

		//3. Mutate the top 10 trees, each tree 10 seperate times -> 100 trees

			for(int m = 0; m < 100; m++) {
				Tree t = new Tree(Tree.copy(mutations.get(m).myRoot));

				Tree.mutate(t);

				trees.add(t);
			}


		//System.out.println("m "+trees.size());

		//currently have 500 trees (100 reproductions, 300 cross, 100 mutate)

		//4. clear error map, so can put in new trees
		errors = new HashMap<Double, Tree>();
		//System.out.println(trees.size());
		for(Tree t: trees) {
			//t.printTree();
			errors.put(t.meanSquaredError(data, training),t);
		}
		//6. find best tree in new pop
		best = Collections.min(errors.keySet());

		gen++;

		//7. clear mutations and cross overs
		mutations = new ArrayList<Tree>();
		crossovers = new ArrayList<Tree>();

		}

	//6. print out best tree
	Tree t = errors.get(Collections.min(errors.keySet()));
	System.out.println("squared "+Collections.min(errors.keySet()));
	System.out.println("mean "+t.meanError(data, training));
	System.out.println(gen);
	System.out.println("squared "+t.meanSquaredError(data, testing));
	System.out.println("mean "+t.meanError(data, testing));
	System.out.println(t.size(t.myRoot));;
	t.printTree();
	System.out.println(t.evaluateEqn(t.myRoot));
	System.out.println(t.evaluate(t.myRoot, 1.92));

	}
}
