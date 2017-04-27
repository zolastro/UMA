package prGeneticRobot;

import robocode.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
 
/**
 * SuperTracker - a Super Sample Robot by CrazyBassoonist based on the robot Tracker by Mathew Nelson and maintained by Flemming N. Larsen
 * <p/>
 * Locks onto a robot, moves close, fires when close.
 */
public class SuperTracker extends AdvancedRobot {
	static final int SEED = 0;
	Random randomGenerator;
	double distanceLimit;
	double changeSpeedProb;
	double speedRange;
	double minSpeed;
	int moveDirection=1;//which way to move
	
	/**
	 * run:  Tracker's main run function
	 */
	public void run() {
		randomGenerator = new Random(SEED);
		setParameters();
		printParameters();
		setAdjustRadarForRobotTurn(true);//keep the radar still while we turn
		setBodyColor(new Color(128, 128, 50));
		setGunColor(new Color(50, 50, 20));
		setRadarColor(new Color(200, 200, 70));
		setScanColor(Color.white);
		setBulletColor(Color.blue);
		setAdjustGunForRobotTurn(true); // Keep the gun still when we turn
		turnRadarRightRadians(Double.POSITIVE_INFINITY);//keep turning radar right
		}
 
	/**
	 * onScannedRobot:  Here's the good stuff
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double absBearing=e.getBearingRadians()+getHeadingRadians();//enemies absolute bearing
		double latVel=e.getVelocity() * Math.sin(e.getHeadingRadians() -absBearing);//enemies later velocity
		double gunTurnAmt;//amount to turn our gun
		setTurnRadarLeftRadians(getRadarTurnRemainingRadians());//lock on the radar
		if(randomGenerator.nextDouble()>changeSpeedProb){
			setMaxVelocity((speedRange*randomGenerator.nextDouble())+minSpeed);//randomly change speed
		}
		if (e.getDistance() > distanceLimit) {//if distance is greater than 150
			gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+latVel/22);//amount to turn our gun, lead just a little bit
			setTurnGunRightRadians(gunTurnAmt); //turn our gun
			setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absBearing-getHeadingRadians()+latVel/getVelocity()));//drive towards the enemies predicted future location
			setAhead((e.getDistance() - 140)*moveDirection);//move forward
			setFire(3);//fire
		}
		else{//if we are close enough...
			gunTurnAmt = robocode.util.Utils.normalRelativeAngle(absBearing- getGunHeadingRadians()+latVel/15);//amount to turn our gun, lead just a little bit
			setTurnGunRightRadians(gunTurnAmt);//turn our gun
			setTurnLeft(-90-e.getBearing()); //turn perpendicular to the enemy
			setAhead((e.getDistance() - 140)*moveDirection);//move forward
			setFire(3);//fire
		}	
	}
	public void onHitWall(HitWallEvent e){
		moveDirection=-moveDirection;//reverse direction upon hitting a wall
	}
	/**
	 * onWin:  Do a victory dance
	 */
	public void onWin(WinEvent e) {
		for (int i = 0; i < 50; i++) {
			turnRight(30);
			turnLeft(30);
		}
	}
	
	public void setParameters() {
		BufferedReader reader = null;
		ArrayList<Double> list = new ArrayList<Double>(); 
		try {
            reader = new BufferedReader(new FileReader("C:\\parameters.txt"));
            String text = null;

            while ((text = reader.readLine()) != null) {
                list.add(Double.parseDouble(text));
            }
            
            distanceLimit = list.get(0);
            changeSpeedProb = list.get(1);
            speedRange = list.get(2);
            minSpeed = list.get(3);
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }		
	}
	
	public void printParameters() {
		System.out.println("distanceLimit: " + distanceLimit);
		System.out.println("changeSpeedProb: " + changeSpeedProb);
		System.out.println("speedRange: " + speedRange);
		System.out.println("minSpeed: " + minSpeed);
	}
}