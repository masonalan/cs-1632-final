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

public class Bean {
	
	private static final double SKILL_AVERAGE = 4.5;	// MainPanel.SLOT_COUNT * 0.5;
	private static final double SKILL_STDEV = 1.5;		// Math.sqrt(SLOT_COUNT * 0.5 * (1 - 0.5));

	Random rand;
	boolean isLuck;
	int right = -1;

	/**
	 * Constructor - creates a bean in either luck mode or skill mode.
	 * 
	 * @param isLuck	whether the bean is in luck mode
	 * @param rand      the random number generator
	 */
	Bean(boolean isLuck, Random rand) {
		this.rand = rand;
		this.isLuck = isLuck;
		if (isLuck)
			return;
		double ds = rand.nextGaussian() * SKILL_STDEV + SKILL_AVERAGE;
		if (ds < 0.5)
			this.right = 0;
		else if (ds < 1.5)
			this.right = 1;
		else if (ds < 2.5)
			this.right = 2;
		else if (ds < 3.5)
			this.right = 3;
		else if (ds < 4.5)
			this.right = 4;
		else if (ds < 5.5)
			this.right = 5;
		else if (ds < 6.5)
			this.right = 6;
		else if (ds < 7.5)
			this.right = 7;
		else if (ds < 8.5)
			this.right = 8;
		else
			this.right = 9;
	}

	/**
	 * Returns whether the bean falls left or right
	 * 0 is left and 1 is right
	 */
	int fall()
	{
		if (this.isLuck)
			return this.rand.nextInt(2);
		if (this.right > 0)
		{
			this.right --;
			return 1;
		}
		return 0;
	}
}
