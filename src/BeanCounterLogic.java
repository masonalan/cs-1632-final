import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import gov.nasa.jpf.vm.Verify;

import java.util.ArrayList;

/**
 * Code by @author Wonsun Ahn
 * 
 * BeanCounterLogic: The bean counter, also known as a quincunx or the Galton
 * box, is a device for statistics experiments named after English scientist Sir
 * Francis Galton. It consists of an upright board with evenly spaced nails (or
 * pegs) in a triangular form. Each bean takes a random path and falls into a
 * slot.
 *
 * Beans are dropped from the opening of the board. Every time a bean hits a
 * nail, it has a 50% chance of falling to the left or to the right. The piles
 * of beans are accumulated in the slots at the bottom of the board.
 * 
 * This class implements the core logic of the machine. The MainPanel uses the
 * state inside BeanCounterLogic to display on the screen.
 * 
 * Note that BeanCounterLogic uses a logical coordinate system to store the
 * positions of in-flight beans.For example, for a 4-slot machine: (0, 0) (1, 0)
 *                      (0, 0)
 *               (1, 0)        (1, 1)
 *        (2, 0)        (2, 1)        (2, 2)
 * [Slot0]       [Slot1]       [Slot2]      [Slot3]
 * 0 1 3 6 10 15
 * 0 1 2 3 4 5 6 7 8 9 
 * 0 1 1 2 2 2 3 3 3 3
 */

public class BeanCounterLogic {
	// TODO: Add member methods and variables as needed
	private Bean[] beans_inflight;
	private ArrayList<Bean> rem_beans;
	private ArrayList<ArrayList<Bean>> slots;
	
	private int slot_half;
	private int slot_high;
	private boolean slot_ran;

	// No bean in that particular Y coordinate
	public static final int NO_BEAN_IN_YPOS = -1;
	

	/**
	 * Constructor - creates the bean counter logic object that implements the core
	 * logic. Our bean counter should start with a single bean at the top.
	 * 
	 * @param slotCount the number of slots in the machine
	 */
	BeanCounterLogic(int slotCount) 
	{
		this.beans_inflight = new Bean[slotCount];
		this.rem_beans = new ArrayList<Bean>();
		this.slots = new ArrayList<ArrayList<Bean>>();
		
		this.setupBeanSlots(this.slots, slotCount);
		
		this.slot_half = this.slots.size()/2;
		
		this.slot_high = this.slots.size() - 1;
	}
	
	private void setupBeanSlots(ArrayList<ArrayList<Bean>> beanSlots, int slotCount) {
	     for (int i = 0; i < slotCount; i++) {
	            beanSlots.add(new ArrayList<Bean>());
	       }
	       this.slot_ran = true;
	}
	
	
	public Bean[] getBeans_inflight() {
		return beans_inflight;
	}

	public ArrayList<Bean> getRem_beans() {
		return rem_beans;
	}

	public ArrayList<ArrayList<Bean>> getSlots() {
		return slots;
	}

	public int getSlot_half() {
		return slot_half;
	}

	public int getSlot_high() {
		return slot_high;
	}

	public boolean isSlot_ran() {
		return slot_ran;
	}

	/**
	 * Returns the number of beans remaining that are waiting to get inserted.
	 * 
	 * @return number of beans remaining
	 */
	public int getRemainingBeanCount() {
		return this.rem_beans.size();
	}

	/**
	 * Returns the x-coordinate for the in-flight bean at the provided y-coordinate.
	 * 
	 * @param yPos the y-coordinate in which to look for the in-flight bean
	 * @return the x-coordinate of the in-flight bean
	 */
	public int getInFlightBeanXPos(int yPos) {
		if(this.beans_inflight[yPos] ==  null)
		{
			return NO_BEAN_IN_YPOS;
		}
		else
		{
			return this.beans_inflight[yPos].getXPos();
		}
	}

	/**
	 * Returns the number of beans in the ith slot.
	 * 
	 * @param i index of slot
	 * @return number of beans in slot
	 */
	public int getSlotBeanCount(int i) {
		return this.slots.get(i).size();
	}
	
	public int total_slots()
	{
		int total = 0;

        for (int i = 0; i < this.slots.size(); i++) {
            total += this.getSlotBeanCount(i);
        }

        return total;
	}
	
	public double weighted_slotcount()
	{
		double total_weight = 0.0;

        for (int i = 0; i < this.slots.size(); i++) {
        	total_weight += this.getSlotBeanCount(i) * i;
        }

        return total_weight;
	}

	/**
	 * Calculates the average slot bean count.
	 * 
	 * @return average of all slot bean counts
	 */
	public double getAverageSlotBeanCount(BeanCounterLogic logic) {
		int total = logic.total_slots();
        double weighted_total = logic.weighted_slotcount();
        
        if (total > 0) {
            return weighted_total / total;
        }

        return 0.0;
	}
	
	/**
	 * For external testing
	 * @return
	 */
	 public double getAverageSlotBeanCount() {
	        return getAverageSlotBeanCount(this);
	    }

	 public void clear_slots(int start, int end) {
	        for (int i = start; i < end; i++) {
	            this.slots.get(i).clear();
	        }
	    }
	 
	/**
	* Removes the individual beans from their slots, on the range [from, to)
	*/
	 public void clear_beans(int from, int to)
	 {
		 ArrayList<Bean> sorted_b = new ArrayList<>(Arrays.asList(beans_inflight));
		 
		 Collections.sort(sorted_b);
		 Bean b;
		 
		 for(int i = from; i < to; i++)
		 {
			 b = sorted_b.get(i);
			 this.slots.get(b.getXPos()).remove(0);
		 }
	 }
	 
	/**
	 * Removes the lower half of all beans currently in slots, keeping only the
	 * upper half.
	 */
	public void upperHalf(BeanCounterLogic logic) {
		// TODO: Implement
		logic.clear_slots(0, logic.getSlot_half());
	}
	
	//Wrapper function 
	public void upperHalf() {
		
		this.clear_beans(0, this.beans_inflight.length/2);;
	}

	
	/**
	 * Removes the upper half of all beans currently in slots, keeping only the
	 * lower half.
	 */
	public void lowerHalf(BeanCounterLogic logic) {
		// TODO: Implement
		logic.clear_slots(logic.getSlot_half(), logic.getSlots().size());
	}
	
	public void lowerHalf()
	{
		this.clear_slots(this.beans_inflight.length/2, this.beans_inflight.length);
	}
	
	 public int sendBean(BeanCounterLogic logic) {
	        if (logic.getSlots().size() == 0) {
	            // Return an error code if there are no slots for beans
	            return -1;
	        } else if (logic.getRem_beans().size() > 0) {
	        	Bean remainingBean = logic.getRem_beans().remove(0);
	            logic.getBeans_inflight()[0] = remainingBean;
	            logic.getBeans_inflight()[0].reset();
	            return 0;
	        } else {
	            // If there is no bean to add, then set the first bean in flight to null
	            logic.getBeans_inflight()[0] = null;
	            return 1;
	        }
	    }
	
	/**
	 * A hard reset. Initializes the machine with the passed beans. The machine
	 * starts with one bean at the top.
	 */
	public void reset(Bean[] beans, BeanCounterLogic logic, boolean clearBeans) 
	{
		//TO DO 
		if (clearBeans) {
        	logic.getRem_beans().clear();
        }

        for (int i = 0; i < beans.length; i++) {
        	if (beans[i] != null) {
        		logic.getRem_beans().add(beans[i]);
        	}
        }

        Arrays.fill(logic.getBeans_inflight(), null);

        logic.clear_slots(0, logic.getSlots().size());

        logic.sendBean(logic);
	}
	
	/**
     * Wrapper for the above method, for external testing access.
     */
    public void reset(Bean[] beans) {
        reset(beans, this, true);
    }

    public void add_rem_slots() {
        // Add beans from every slot to remaining beans
        for (int i = 0; i < this.slots.size(); i++) {
            this.rem_beans.addAll(this.slots.get(i));
        }
    }
    
	/**
	 * Repeats the experiment by scooping up all beans in the slots and all beans
	 * in-flight and adding them into the pool of remaining beans. As in the
	 * beginning, the machine starts with one bean at the top.
	 */
	public void repeat(BeanCounterLogic logic) {
		logic.add_rem_slots();

        boolean cleared = false;

        logic.reset(logic.getBeans_inflight(), logic, cleared);
    }

    /*
     * Wrapper of above method for external testing
     */
    public void repeat() {
        repeat(this);
    }
    
    
    public int moveBean(int slot, Bean bean, BeanCounterLogic logic) {
        // If you are at the highest slot, just add it to the slot
        if (slot == logic.getSlot_high()) {
            logic.getSlots().get(bean.getXPos()).add(bean);
            return 0;
        } else {
            // Bean is not at the highest slot, so it needs to decide where to go
            bean.fallOnce();
            // The bean moves down one row
            logic.getBeans_inflight()[slot + 1] = bean;
            return 1;
        }
    }

    public int setNoneInFlight(int i, BeanCounterLogic logic) {
        if (i < logic.getSlot_high()) {
            logic.getBeans_inflight()[i + 1] = null;
            return 0;
        }

        return -1;
    }

	/**
	 * Advances the machine one step. All the in-flight beans fall down one step to
	 * the next peg. A new bean is inserted into the top of the machine if there are
	 * beans remaining.
	 * 
	 * @return whether there has been any status change. If there is no change, that
	 *         means the machine is finished.
	 */
	public boolean advanceStep(BeanCounterLogic logic) {
		boolean statusChange = false;

        // Move from the bottom row up so the GUI appears correctly
        for (int i = logic.getSlot_high(); i >= 0; i--) {
            // Get each bean in flight
            Bean currentBean = logic.getBeans_inflight()[i];

            // If there is a bean at this row
            if (currentBean != null) {
                logic.moveBean(i, currentBean, logic);
                statusChange = true;
            } else {
                // Set this row as currently empty if not at the final row
                logic.setNoneInFlight(i, logic);
            }
        }

        // Insert bean on top
        logic.sendBean(logic);

        return statusChange;
	}
	
	public boolean advanceStep()
	{
		return advanceStep(this);
	}

	public static void showUsage() {
		System.out.println("Usage: java BeanCounterLogic <number of beans> <luck | skill>");
		System.out.println("Example: java BeanCounterLogic 400 luck");
	}
	
	
	////Check here
	public int getInFlightBeans(BeanCounterLogic logic, int slotCount) {
        int totalInFlightBeans = 0;

        // For all slots, check how many beans are in that column currently in flight
        for (int i = 0; i < slotCount; i++) {
            if (logic.getInFlightBeanXPos(i) != -1) {
                totalInFlightBeans++;
            }
        }

        return totalInFlightBeans;
    }

    public int getInSlotsCount(BeanCounterLogic logic, int slotCount) {
        int totalInSlotBeans = 0;

        // For all slots, get the bean count in that slot currently
        for (int i = 0; i < slotCount; i++) {
            totalInSlotBeans += logic.getSlotBeanCount(i);
        }

        return totalInSlotBeans;
    }

    public static int sumOfBeans(BeanCounterLogic logic, int slotCount) {
        // Total three types of beans, remaining, in flight, and in slots
        int remaining = logic.getRemainingBeanCount();
        int inFlight = logic.getInFlightBeans(logic, slotCount);
        int inSlots = logic.getInSlotsCount(logic, slotCount);

        return remaining + inFlight + inSlots;
    }

    public static void setupBeans(BeanCounterLogic logic, Bean[] beans, int beanCount, boolean luck) {
        for (int i = 0; i < beanCount; i++) {
            beans[i] = new Bean(luck, new Random());
        }
        // Initialize the logic with the beans
        logic.reset(beans, logic, true); 
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
			int testBeanCount = Verify.getInt(0,3);
			int testSlotCount = Verify.getInt(1,5);
			
			// Create the internal logic
			BeanCounterLogic logic = new BeanCounterLogic(slotCount);
			// Create the beans (in luck mode)
			Bean[] beans = new Bean[beanCount];
			
			setupBeans(logic,beans,testBeanCount,true);
			
			//Continue until you can advance anymore
			while (logic.advanceStep(logic)) {
				

				// Checks invariant property: all positions of in-flight beans have to be
				// legal positions in the logical coordinate system.
				for (int yPos = 0; yPos < slotCount; yPos++) {
					int xPos = logic.getInFlightBeanXPos(yPos);
					assert xPos == BeanCounterLogic.NO_BEAN_IN_YPOS || (xPos >= 0 && xPos <= yPos);
				}

				// TODO: Check invariant property: the sum of remaining, in-flight, and in-slot
				// beans always have to be equal to beanCount
			    
				assert testBeanCount == sumOfBeans(logic,testSlotCount);
				
			}
			// TODO: Check invariant property: when the machine finishes,
			// 1. There should be no remaining beans.
			// 2. There should be no beans in-flight.
			// 3. The number of in-slot beans should be equal to beanCount.
			
			assert logic.getRemainingBeanCount() == 0;
			assert logic.getInFlightBeans(logic, testSlotCount) == 0;
			assert logic.getInSlotsCount(logic, testSlotCount) == testBeanCount;
			
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
