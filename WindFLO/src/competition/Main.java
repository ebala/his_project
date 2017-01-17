package competition;

import competition.scenarios.WindScenario;
import evaluation.CompetitionEvaluator;

public class Main {

	// Change the following lines to fit your needs for the competition
	private final String USER_API_TOKEN = "3GWPBVOLDWJX8X157GRF7OYQWQCVV4"; // Place your User API Token here!
	private final String RUN_API_TOKEN = "KZN66QJ04NZSUTA13WSQW7Q3Q7WIO8"; // Place your Run API Token here!
	private final int SCENARIO_NUMBER = 2;
	
	public static void main(String argv[]) {
		Main m = new Main();
		m.useLocalEvaluation();
		// WARNING: Uncomment the following statement to use the server evaluation.
//		 m.useCompetitionServerEvaluation();
	}

	public void useCompetitionServerEvaluation() {
		CompetitionEvaluator eval = new CompetitionEvaluator();
		if(RUN_API_TOKEN.isEmpty()){
			eval.initialize(SCENARIO_NUMBER, USER_API_TOKEN);
		}
		else{
			eval.initialize(SCENARIO_NUMBER, USER_API_TOKEN, RUN_API_TOKEN);
		}		
		GA algorithm = new GA(eval);
		algorithm.run();
	}

	public void useLocalEvaluation() {
		Out.createFile(SCENARIO_NUMBER);
		WindScenario ws = null;
		try {
			ws = new WindScenario(WindScenario.getScenarioFilename(SCENARIO_NUMBER, true));
		} catch (Exception e) {
			System.err.println(e);
			return;
		}
		KusiakLayoutEvaluator wfle = new KusiakLayoutEvaluator();
		wfle.initialize(ws);
		GA algorithm = new GA(wfle);
		algorithm.run();
	}
}
