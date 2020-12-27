package lab14;

import lab14lib.*;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		/** Your code here. */
		/*Generator g1 = new SineWaveGenerator(200);
		Generator g2 = new SawToothGenerator(500);
		Generator g3 = new SawToothGenerator(220);

		ArrayList<Generator> generators = new ArrayList<Generator>();
		generators.add(g1);
		generators.add(g2);
		generators.add(g3);
		MultiGenerator mg = new MultiGenerator(generators);

		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(mg);
		gav.drawAndPlay(4096, 1000000);*/


		Generator generator = new StrangeBitwiseGenerator(1024);
		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
		gav.drawAndPlay(128000, 1000000);



	}
} 