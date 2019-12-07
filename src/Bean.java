import java.util.Random;

/**
 * Code by @author Wonsun Ahn
 * 
 * Bean: Each bean is assigned a skill level from 0-9 on creation according to
 * a normal distribution with average SKILL_AVERAGE and standard deviation
 * SKILL_STDEV. A skill level of 9 means it always makes the "right" choices
 * (pun intended) when the machine is operating in skill mode ("skill" passed on
 * command kine). That means the bean will always go right when a peg is
 * encountered, resulting it falling into slot 9. A skill evel of 0 means that
 * the bean will always go left, resulting it falling into slot 0. For the
 * in-between skill levels, the bean will first go right then left. For example,
 * for a skill level of 7, the bean will go right 7 times then go left twice.
 * 
 * Skill levels are irrelevant when the machine operates in luck mode. In that
 * case, the bean will have a 50/50 chance of going right or left, regardless of
 * skill level.
 */

public class Bean implements Comparable<Bean> {
	// TODO: Add member methods and variables as needed 
	
	private static final double SKILL_AVERAGE = 4.5;	// MainPanel.SLOT_COUNT * 0.5;
	private static final double SKILL_STDEV = 1.5;		// Math.sqrt(SLOT_COUNT * 0.5 * (1 - 0.5));

	private boolean luckModeOn;
	private Random random;
	private int xCoord;
	private int skill;
	private int spacesToMove;
	

	/**
	 * Constructor - creates a bean in either luck mode or skill mode.
	 * 
	 * @param isLuck	whether the bean is in luck mode
	 * @param random      the random number generator
	 */
	Bean(boolean isLuck, Random random) {
		this.luckModeOn = isLuck;
		this.random = random;
		this.skill = (int)((this.random.nextGaussian() * SKILL_STDEV) + SKILL_AVERAGE);

		if (this.skill > 9) {
			this.skill = 9;
		}
		if (this.skill < 0) {
			this.skill = 0;
		}

		this.spacesToMove = this.skill;
		this.xCoord = 0;
	}

	public Random getRandom() {
		return this.random;
	}

	/**
	 * Fetch and return the x-coordinate of this bean.
	 * @return 			the x-coordinate of this bean
	 */
	public int getXPos() {
		return this.xCoord;
	}

	/**
	 * Simulate a single iteration of the bean falling. If the bean is in luck mode,
	 * then flip a coin to determine whether to fall left or right. Otherwise, if the
	 * bean is in skill mode, move right iff fewer iterations have passed than the skill
	 * level of the bean, otherwise move right.
	 */
	public void fallOnce() {
		if(this.luckModeOn) {
			if (this.random.nextBoolean()) {
				this.xCoord++;
			}	
		}
		else{
			if (this.spacesToMove > 0) {
				this.xCoord++;
				this.spacesToMove--;
			}
		}
	}

	public int getSkill() {
		return this.skill;
	}

	public boolean getLuckModeOn() {
		return this.luckModeOn;
	}

	public int getSpacesToMove() {
		return this.spacesToMove;
	}

	/**
	 * Reset the x-coordinate of this bean to 0.
	 */
	public void reset() {
		this.xCoord = 0;
		this.spacesToMove = this.skill;
	}

	public int compareTo(Bean other) {
		return this.getXPos()-other.getXPos();
	}
}