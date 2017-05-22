package neural;

import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;

public class NetworkTrainer {

	public final int MINIMUM_EPOCHS;
	public final int EXTRA_EPOCHS;
	
	private BasicNetwork bestNetwork;
	
	private int totalEpochs = 0;
	private int numberOfWorseEpochs = 0;
	private double minimumError = Double.MAX_VALUE;
	
	private MLDataSet  validationSet;
	
	public NetworkTrainer(MLDataSet validationSet, BasicNetwork network,  int minimum_epochs, int extra_epochs){
		this.validationSet = validationSet;
		this.MINIMUM_EPOCHS = minimum_epochs;
		this.EXTRA_EPOCHS = extra_epochs;
		this.bestNetwork = network;
	}
	
	
	public BasicNetwork getBestNetwork(){
		return bestNetwork;
	}
	
	public int numberOfEpochs(){
		return totalEpochs;
	}
	
	public boolean hasFinished(BasicNetwork network){
		totalEpochs++;
		double error = network.calculateError(validationSet);
//		System.out.println(error);
		if(error < minimumError){
			bestNetwork = (BasicNetwork) network.clone();
			minimumError = error;
			numberOfWorseEpochs = 0;
		}else{
			numberOfWorseEpochs++;
//			System.out.println("Worse than : " + minimumError);
		}
		if(numberOfWorseEpochs > EXTRA_EPOCHS &&
				totalEpochs > MINIMUM_EPOCHS){
			return true;
		}else{
			return false;
		}
	}	
}
