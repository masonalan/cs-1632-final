import gov.nasa.jpf.vm.Verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Code by @author Wonsun Ahn
 * 
 * <p>BeanCounterLogic: The bean counter, also known as a quincunx or the Galton
 * box, is a device for statistics experiments named after English scientist Sir
 * Francis Galton. It consists of an upright board with evenly spaced nails (or
 * pegs) in a triangular form. Each bean takes a random path and falls into a
 * slot.</p>
 *
 * <p>Beans are dropped from the opening of the board. Every time a bean hits a
 * nail, it has a 50% chance of falling to the left or to the right. The piles
 * of beans are accumulated in the slots at the bottom of the board.</p>
 * 
 * <p>This class implements the core logic of the machine. The MainPanel uses the
 * state inside BeanCounterLogic to display on the screen.</p>
 * 
 * <p>Note that BeanCounterLogic uses a logical coordinate system to store the
 * positions of in-flight beans.For example, for a 4-slot machine: (0, 0) (1, 0)
 *                      (0, 0)
 *               (1, 0)        (1, 1)
 *        (2, 0)        (2, 1)        (2, 2)
 * [Slot0]       [Slot1]       [Slot2]      [Slot3]
 * 0 1 3 6 10 15
 * 0 1 2 3 4 5 6 7 8 9 
 * 0 1 1 2 2 2 3 3 3 3</p>
 */

public class BeanCounterLogic {
	// TODO: Add member methods and variables as needed
	public Queue<Bean> waitingBeans;
	public Bean[] pegs;
	public ArrayList<Bean>[] slots;
	public int slottedBeanCount = 0;

	// No bean in that particular Y coordinate
	public static final int NO_BEAN_IN_YPOS = -1;


	/**
	 * Constructor - creates the bean counter logic object that implements the core
	 * logic. Our bean counter should start with a single bean at the top.
	 * 
	 * @param slotCount the number of slots in the machine
	 */
	BeanCounterLogic(int slotCount) {
		slots = new ArrayList[slotCount];
		Bean[] arr = {new Bean(true, new Random())};
		reset(arr);
	}

	/**
	 * Returns the number of beans remaining that are waiting to get inserted.
	 * 
	 * @return number of beans remaining
	 */
	public int getRemainingBeanCount() {
		return waitingBeans.size();
	}

	/**
	 * Returns the x-coordinate for the in-flight bean at the provided y-coordinate.
	 * 
	 * @param yPos the y-coordinate in which to look for the in-flight bean
	 * @return the x-coordinate of the in-flight bean
	 */
	public int getInFlightBeanXPos(int yPos) {
		if (pegs.length == 0) {
			return NO_BEAN_IN_YPOS;
		}
//		if (pegs.length == 1 && pegs[0] != null) {
//			return 0;
//		}
		int i = yPos * (yPos + 1) / 2;
		for (int j = i; j < i + yPos + 1 && j < pegs.length; j ++) {
			Bean b = pegs[j];
			if (b != null) {
				return j - i;
			}
		}
		return NO_BEAN_IN_YPOS;
	}

	/**
	 * Returns the number of beans in the ith slot.
	 * 
	 * @param i index of slot
	 * @return number of beans in slot
	 */
	public int getSlotBeanCount(int i) {
		return slots[i].size();
	}

	/**
	 * Calculates the average slot bean count.
	 * 
	 * @return average of all slot bean counts
	 */
	public double getAverageSlotBeanCount() {
		int l = slots.length;
		double t = 0.0;
		for (int i = 0; i < l; i ++) {
			t += (double)slots[i].size();
		}
		return t / (double)l;
	}

	/**
	 * Returns the size of half of the slot count
	 * @return half the size
	 */
	public int getHalf() {
		int count = 0;
		for (int i = 0; i < slots.length; i ++) {
			count += slots[i].size();
		}
		return count / 2;
	}

	/**
	 * Removes the lower half of all beans currently in slots, keeping only the
	 * upper half.
	 */
	public void upperHalf() {
		int half = getHalf();
		int s = 0;
		for (int i = 0; i < half; i ++) {
			ArrayList slot = slots[s];
			while (slot.size() == 0) {
				s++;
				slot = slots[s];
			}
			slot.remove(0);
		}
	}

	/**
	 * Removes the upper half of all beans currently in slots, keeping only the
	 * lower half.
	 */
	public void lowerHalf() {
		int half = getHalf();
		int s = slots.length - 1;
		for (int i = 0; i < half; i ++) {
			ArrayList slot = slots[s];
			while (slot.size() == 0) {
				s--;
				slot = slots[s];
			}
			slot.remove(slot.size() - 1);
		}
	}

	/**
	 * A hard reset. Initializes the machine with the passed beans. The machine
	 * starts with one bean at the top.
	 */
	public void reset(Bean[] beans) {
		int t = slots.length - 1;
		pegs = new Bean[t * (t + 1) / 2];
		for (int i = 0; i < slots.length; i ++) {
			slots[i] = new ArrayList<Bean>();
		}
		waitingBeans = new LinkedList(Arrays.asList(beans));
		if (pegs.length > 0) {
			pegs[0] = waitingBeans.poll();
		}
	}

	/**
	 * Repeats the experiment by scooping up all beans in the slots and all beans
	 * in-flight and adding them into the pool of remaining beans. As in the
	 * beginning, the machine starts with one bean at the top.
	 */
	public void repeat() {
		for (int i = 0; i < slots.length; i ++) {
			ArrayList<Bean> slot = slots[i];
			for (int j = 0; j < slot.size(); j ++) {
				waitingBeans.add(slot.remove(0));
			}
		}
		for (int k = 0; k < pegs.length; k ++) {
			if (pegs[k] != null) {
				System.out.println("Found bean.");
				waitingBeans.add(pegs[k]);
				pegs[k] = null;
			}
		}
		pegs[0] = waitingBeans.poll();
	}

	/**
	 * Advances the machine one step. All the in-flight beans fall down one step to
	 * the next peg. A new bean is inserted into the top of the machine if there are
	 * beans remaining.
	 * 
	 * @return whether there has been any status change. If there is no change, that
	 *         means the machine is finished.
	 */
	public boolean advanceStep() {
		boolean status = false;
		for (int i = slots.length - 2; i >= 0; i --) {
			int x = getInFlightBeanXPos(i);
			if (x < 0) {
				continue;
			}
			int index = i * (i + 1) / 2 + x;
			status = true;
			Bean b = pegs[index];
			pegs[index] = null;
			int j = i + 1;
			if (j > slots.length - 2) {
				slots[x + b.fall()].add(b);
				continue;
			}
			int newIndex = j * (j + 1) / 2 + x;
			newIndex += b.fall();
			pegs[newIndex] = b;
		}
		if (slots.length == 1) {
			Bean polled = waitingBeans.poll();
			if (polled == null) {
				return false;
			}
			slots[0].add(polled);
			return true;
		}
		pegs[0] = waitingBeans.poll();
		return status;
	}

	public static void showUsage() {
		System.out.println("Usage: java BeanCounterLogic <number of beans> <luck | skill>");
		System.out.println("Example: java BeanCounterLogic 400 luck");
	}

	/**
	 * Auxiliary main method. Runs the machine in text mode with no bells and
	 * whistles. It simply shows the slot bean count at the end. Also, when the
	 * string "test" is passed to args[0], the program enters test mode. In test
	 * mode, the Java Pathfinder model checking tool checks the logic of the machine
	 * for a small number of beans and slots.
	 * 
	 * @param args args[0] is an integer bean count, args[1] is a string which is
	 *             either luck or skill.
	 */
	public static void main(String[] args) {
		boolean luck;
		int beanCount = 0;
		int slotCount = 0;

		if (args.length == 1 && args[0].equals("test")) {
			// TODO: Verify the model checking passes for beanCount values 0-3 and slotCount
			// values 1-5 using the JPF Verify API.

			beanCount = Verify.getInt(0, 3);
			slotCount = Verify.getInt(1, 5);
			// Create the internal logic
			BeanCounterLogic logic = new BeanCounterLogic(slotCount);
			// Create the beans (in luck mode)
			Bean[] beans = new Bean[beanCount];
			for (int i = 0; i < beanCount; i++) {
				beans[i] = new Bean(true, new Random());
			}
			// Initialize the logic with the beans
			logic.reset(beans);

			while (true) {
				if (!logic.advanceStep()) {
					break;
				}

				// Checks invariant property: all positions of in-flight beans have to be
				// legal positions in the logical coordinate system.
				for (int yPos = 0; yPos < slotCount; yPos++) {
					int xPos = logic.getInFlightBeanXPos(yPos);
					assert xPos == BeanCounterLogic.NO_BEAN_IN_YPOS || (xPos >= 0 && xPos <= yPos);
				}

				// TODO: Check invariant property: the sum of remaining, in-flight, and in-slot
				// beans always have to be equal to beanCount
				int count = logic.waitingBeans.size();
				for (int i = 0; i < logic.pegs.length; i ++) {
					if (logic.pegs[i] != null) {
						count++;
					}
				}
				for (int i = 0; i < logic.slots.length; i ++) {
					count += logic.slots[i].size();
				}
				assert count == beanCount;
				
			}
			// TODO: Check invariant property: when the machine finishes,
			// 1. There should be no remaining beans.
			// 2. There should be no beans in-flight.
			// 3. The number of in-slot beans should be equal to beanCount.
			int inSlotCount = 0;
			for (int i = 0; i < logic.slots.length; i ++) {
				inSlotCount += logic.slots[i].size();
			}
			assert inSlotCount == beanCount;
			int remainingCount = 0;
			for (int i = 0; i < logic.pegs.length; i ++) {
				if (logic.pegs[i] != null) {
					remainingCount++;
				}
			}
			remainingCount += logic.waitingBeans.size();
			assert remainingCount == 0;
			return;
		}

		if (args.length != 2) {
			showUsage();
			return;
		}

		try {
			beanCount = Integer.parseInt(args[0]);
		} catch (NumberFormatException ne) {
			showUsage();
			return;
		}
		if (beanCount < 0) {
			showUsage();
			return;
		}

		if (args[1].equals("luck")) {
			luck = true;
		} else if (args[1].equals("skill")) {
			luck = false;
		} else {
			showUsage();
			return;
		}
		
		slotCount = 10;

		// Create the internal logic
		BeanCounterLogic logic = new BeanCounterLogic(slotCount);
		// Create the beans (in luck mode)
		Bean[] beans = new Bean[beanCount];
		for (int i = 0; i < beanCount; i++) {
			beans[i] = new Bean(luck, new Random());
		}
		// Initialize the logic with the beans
		logic.reset(beans);
					
		// Perform the experiment
		while (true) {
			if (!logic.advanceStep()) {
				break;
			}
		}
		// display experimental results
		System.out.println("Slot bean counts:");
		for (int i = 0; i < slotCount; i++) {
			System.out.print(logic.getSlotBeanCount(i) + " ");
		}
		System.out.println("");
	}
}
