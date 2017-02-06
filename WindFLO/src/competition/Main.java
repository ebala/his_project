package competition;

import competition.scenarios.WindScenario;
import evaluation.CompetitionEvaluator;

public class Main {

	// Change the following lines to fit your needs for the competition
	private final String USER_API_TOKEN = "9HQBW1V8IXEPXBR19HKVPMO7PHQSLT"; // Place your User API Token here!
	private final String RUN_API_TOKEN = "PEHIYHCYXDI22KT8UZDN5ZWKFSM7DO"; // Place your Run API Token here!
	private final int SCENARIO_NUMBER = 3;
	
	public static void main(String argv[]) {
		Main m = new Main();
		
		m.useLocalEvaluation();
		// WARNING: Uncomment the following statement to use the server evaluation.
//		 m.useCompetitionServerEvaluation();
	}

	public void useCompetitionServerEvaluation() {
		Out.createFile("server",SCENARIO_NUMBER);
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
		
		
		for(int scenerio =0; scenerio <5 ; scenerio++){
			for(int repeat =0; repeat<1;repeat++){
				
				Out.createFile("local",scenerio);
				WindScenario ws = null;
				try {
					ws = new WindScenario(WindScenario.getScenarioFilename(scenerio, true));
				} catch (Exception e) {
					System.err.println(e);
					return;
				}
				KusiakLayoutEvaluator wfle = new KusiakLayoutEvaluator();
				wfle.initialize(ws);
				GA algorithm = new GA(wfle);
				algorithm.run1();
		 }
		 }
		
	}
}
