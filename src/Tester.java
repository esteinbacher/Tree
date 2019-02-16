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

	static int dynamic_limit;
	static double best_fitness = 1000000000;
	static Tree best_tree;
	static int numPassed = 0;
	static int numUpdated = 0;
	static int numInject = 0;

	//returns true if should accept tree, updates best_fitness if needed
	private static boolean dynamicLimitTest(Tree t){
		int depth = t.maxDepth();

		if (depth <= dynamic_limit){ //if under limit
			if (t.fitness < best_fitness){
				numUpdated++;
				best_fitness = t.fitness; //if best, set best_fit
				best_tree = t;
			}
			return true; //accept
		}

		if (depth > dynamic_limit && t.fitness < best_fitness){ //too big, but best fit
			numUpdated++;
			best_fitness = t.fitness; //set best_fit
			best_tree = t;
			dynamic_limit = depth; //change dynamic_limit to allow this tree
			return true; //accept
		}

		return false;

	}

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
		int init_depth = 3;
		dynamic_limit = init_depth;

		ArrayList<Double> keys = new ArrayList<Double>();
		HashMap data = readXYFromCSV("dataset1.csv");

		//2. divide up data into training and testing

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

		//3. create intial trees
		for(int i = 0; i < 1000; i++) {
			trees.add(new Tree(init_depth, data, training));
		}


		//4. put all mean Squared errors, can be sorted and best found
		for(Tree t: trees) {
			errors.put(t.fitness, t);
		}
		Tree init_best = errors.get(Collections.min(errors.keySet()));
		best_fitness = init_best.fitness;
		best_tree = init_best;
		System.out.println("Init best: " + best_fitness);

		ArrayList<Tree> mutations = new ArrayList<Tree>();
		ArrayList<Tree> crossovers = new ArrayList<Tree>();
		int gen = 0;

		//5. keep making new generations until squared error below .5, or # of gens hits 200
		while(!(best_fitness < .05 || gen >= 200)) { //TODO: change?
			trees = new ArrayList<Tree>();

			//1. get top 100 trees in population, reproduce (trees), and add to crossovers and mutations
			while(trees.size() <= 100) { //go until we have gotten 100
				if(!errors.isEmpty()){
					Tree t = errors.get(Collections.min(errors.keySet()));
					errors.remove(Collections.min(errors.keySet()));
					if (dynamicLimitTest(t)){ //make sure it passes dynamic limit test
						if (crossovers.size()<= 40){
							crossovers.add(t);
							if (mutations.size() <= 10){
								mutations.add(t);
							}
						}
						trees.add(t);
					}
				}
				else{ //if tried all old trees, gen new ones
					numInject ++;
					Tree t = new Tree(init_depth, data, training);

					if (crossovers.size()<= 40){
						crossovers.add(t);
						if (mutations.size() <= 10){
							mutations.add(t);
						}
					}
					trees.add(t);
				}
			}

			//2. cross the top 40 trees, and cross each with the best 10
			Tree t1;
			Tree t2;
			for(int i = 0; i < 40; i++) {
				for(int j = 0; j <10; j++) {
					t1 = Tree.copy(crossovers.get(i));
					t2 = Tree.copy(crossovers.get(j));
					Tree.crossover(t1, t2);

					while ( !(dynamicLimitTest(t1) && dynamicLimitTest(t2)) ){
						t1 = Tree.copy(crossovers.get(i));
						t2 = Tree.copy(crossovers.get(j));

						Tree.crossover(t1, t2);
					}
					trees.add(t1);
					trees.add(t2);
				}
			}

			//3. Mutate the top 10 trees, each tree 10 seperate times -> 100 trees
			Tree t;
			for(int k = 0; k < 10; k++) {
				for(int m = 0; m < 10; m++) {
					t = Tree.copy(mutations.get(m));
					Tree.mutate(t);
					while(!dynamicLimitTest(t)){
						t = Tree.copy(mutations.get(m));
						Tree.mutate(t);
					}

					trees.add(t);
				}
			}

			//currently have 1000 trees (100 reproductions, 800 cross, 100 mutate)

			//4. clear error map, so can put in new trees
			errors = new HashMap<Double, Tree>();
			for(Tree i: trees) {
				//t.printTree();
				errors.put(i.fitness,i);
			}
			//6. find best tree in new pop
			Double best_of_gen = Collections.min(errors.keySet());
			System.out.println("Best Gen " + gen + ": " + best_of_gen);

			gen++;

			//7. clear mutations and cross overs
			mutations = new ArrayList<Tree>();
			crossovers = new ArrayList<Tree>();

		}

	//6. print out best tree
	//Tree t = errors.get(Collections.min(errors.keySet()));
	System.out.println("end best: " + best_fitness);
	//mean squared error of training set
	System.out.println("Eqn: " + best_tree.evaluateEqn(best_tree.myRoot));
	System.out.println("Depth: " + best_tree.maxDepth());
	System.out.println("Gen: " + gen);
	System.out.println("numPassed: " + numPassed);
	System.out.println("numUpdated: " + numUpdated);
	System.out.println("numInject: " + numInject);
	System.out.println("\n");
	System.out.println("TRAINING: \n");
	System.out.println("squared "+best_tree.fitness);
	//average mean error of training
	System.out.println("mean "+best_tree.meanError(data, training));

	System.out.println("\n");
	System.out.println("TESTING: \n");
	System.out.println("squared "+best_tree.meanSquaredError(data, testing));
	System.out.println("mean "+best_tree.meanError(data, testing));
	//System.out.println(t.evaluate(t.myRoot, 1.22));
	//System.out.println(t.evaluate(t.myRoot, -4.38));

	}
}
