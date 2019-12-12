import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BeanCounterLogicTest {

	BeanCounterLogic b;
	private ByteArrayOutputStream out = new ByteArrayOutputStream();
	private PrintStream oldOut;

	@Before
	public void setup() {
		b = new BeanCounterLogic(4);
	}
	
	@Test
	public void testConstructorNumSlotsMadeReg() {
		assertEquals(b.slots.length, 4);
	}

	@Test
	public void testConstructorBeanAtTop() {
		assertNotNull(b.pegs[0]);
	}

	@Test
	public void testConstructorEmptyQueue() {
		assertEquals(b.waitingBeans.size(), 0);
	}

	@Test
	public void testGetRemainingBeanCount() {
		b.waitingBeans.add(Mockito.mock(Bean.class));
		b.waitingBeans.add(Mockito.mock(Bean.class));
		b.waitingBeans.add(Mockito.mock(Bean.class));
		assertEquals(b.waitingBeans.size(), 3);
	}

	@Test
	public void testGetInFlightBeanXPosStart() {
		assertEquals(b.getInFlightBeanXPos(0), 0);
	}

	@Test
	public void testGetInFlightBeanXPosEnd() {
		b.pegs[5] = Mockito.mock(Bean.class);
		assertEquals(b.getInFlightBeanXPos(2), 2);
	}

	@Test
	public void testGetInFlightBeanXPosMid() {
		b.pegs[2] = Mockito.mock(Bean.class);
		assertEquals(b.getInFlightBeanXPos(1), 1);
	}
	
	@Test
	public void testGetSlotBeanCountEmpty() {
		assertEquals(b.getSlotBeanCount(0), 0);
	}

	@Test
	public void testGetSlotBeanCountNonEmpty() {
		b.slots[0].add(Mockito.mock(Bean.class));
		assertEquals(b.getSlotBeanCount(0), 1);
	}

	@Test
	public void testAverageSlotBeanCount() {
		b.slots[0].add(Mockito.mock(Bean.class));
		b.slots[1].add(Mockito.mock(Bean.class));
		b.slots[1].add(Mockito.mock(Bean.class));
		b.slots[2].add(Mockito.mock(Bean.class));
		b.slots[2].add(Mockito.mock(Bean.class));
		b.slots[2].add(Mockito.mock(Bean.class));
		b.slots[3].add(Mockito.mock(Bean.class));
		b.slots[3].add(Mockito.mock(Bean.class));
		b.slots[3].add(Mockito.mock(Bean.class));
		b.slots[3].add(Mockito.mock(Bean.class));
		assertEquals(b.getAverageSlotBeanCount(), 2.5f, 0);
	}

	@Test
	public void testResetBeanAtTop() {
		Bean[] array = {Mockito.mock(Bean.class),
				Mockito.mock(Bean.class),
				Mockito.mock(Bean.class),
				Mockito.mock(Bean.class),
				Mockito.mock(Bean.class)};
		b.reset(array);
		assertNotNull(b.pegs[0]);
	}

	@Test
	public void testResetPegArrLengthCorrect() {
		Bean[] array = {Mockito.mock(Bean.class),
				Mockito.mock(Bean.class),
				Mockito.mock(Bean.class),
				Mockito.mock(Bean.class),
				Mockito.mock(Bean.class)};
		b.reset(array);
		assertEquals(b.pegs.length, 6);
	}

	@Test
	public void testResetQueueSizeCorrect() {
		Bean[] array = {Mockito.mock(Bean.class),
				Mockito.mock(Bean.class),
				Mockito.mock(Bean.class),
				Mockito.mock(Bean.class),
				Mockito.mock(Bean.class)};
		b.reset(array);
		assertEquals(b.waitingBeans.size(), 4);
	}

	@Test
	public void testAdvanceStepFallLeftStart() {
		Bean top = Mockito.mock(Bean.class);
		Mockito.when(top.fall()).thenReturn(0);
		b.pegs[0] = top;
		b.advanceStep();
		assertNotNull(b.pegs[1]);
		assertNull(b.pegs[0]);
	}

	@Test
	public void testAdvanceStepFallLeftMiddle() {
		Bean top = Mockito.mock(Bean.class);
		Mockito.when(top.fall()).thenReturn(0);
		b.pegs[0] = null;
		b.pegs[2] = top;
		b.advanceStep();
		assertNotNull(b.pegs[4]);
		assertNull(b.pegs[2]);
	}

	@Test
	public void testAdvanceStepFallLeftEnd() {
		Bean top = Mockito.mock(Bean.class);
		Mockito.when(top.fall()).thenReturn(0);
		b.pegs[0] = null;
		b.pegs[4] = top;
		b.advanceStep();
		assertEquals(b.slots[1].size(), 1);
		assertNull(b.pegs[4]);
	}

	@Test
	public void testAdvanceStepFallRightStart() {
		Bean top = Mockito.mock(Bean.class);
		Mockito.when(top.fall()).thenReturn(1);
		b.pegs[0] = top;
		b.advanceStep();
		assertNotNull(b.pegs[2]);
		assertNull(b.pegs[0]);
	}

	@Test
	public void testAdvanceStepFallRightMiddle() {
		Bean top = Mockito.mock(Bean.class);
		Mockito.when(top.fall()).thenReturn(1);
		b.pegs[0] = null;
		b.pegs[2] = top;
		b.advanceStep();
		assertNotNull(b.pegs[5]);
		assertNull(b.pegs[2]);
	}

	@Test
	public void testAdvanceStepFallRightEnd() {
		Bean top = Mockito.mock(Bean.class);
		Mockito.when(top.fall()).thenReturn(1);
		b.pegs[0] = null;
		b.pegs[4] = top;
		b.advanceStep();
		assertEquals(b.slots[2].size(), 1);
		assertNull(b.pegs[4]);
	}

	@Test
	public void testUpperHalfEvenlySplitSlots() {
		Bean bean = Mockito.mock(Bean.class);
		b.slots[1] = new ArrayList<>(Arrays.asList(bean, bean, bean));
		b.slots[3] = new ArrayList<>(Arrays.asList(bean, bean, bean));
		b.upperHalf();
		assertEquals(b.slots[1].size(), 0);
		assertEquals(b.slots[3].size(), 3);
	}

	@Test
	public void testLowerHalfEvenlySplitSlots() {
		Bean bean = Mockito.mock(Bean.class);
		b.slots[1] = new ArrayList<>(Arrays.asList(bean, bean, bean));
		b.slots[3] = new ArrayList<>(Arrays.asList(bean, bean, bean));
		b.lowerHalf();
		assertEquals(b.slots[3].size(), 0);
		assertEquals(b.slots[1].size(), 3);
	}

	/**
	 *  Okay
	 */
	@Before
	public void setUp() {
		oldOut = System.out;
		try {
			System.setOut(new PrintStream(out, false, "UTF-8"));
		} catch (UnsupportedEncodingException error) {
			error.printStackTrace();
		}
	}

	@Test
	public void testMainNotTwoArgs() {
		BeanCounterLogic.main(new String[]{"a"});
		try {
			assertEquals(out.toString("UTF-8"),
                    "Usage: java BeanCounterLogic <number of beans> <luck | skill>"
                            + "\nExample: java BeanCounterLogic 400 luck\n");
		} catch (UnsupportedEncodingException error) {
			error.printStackTrace();
		}
	}

	@Test
	public void testMainLessThanZeroBeans() {
		BeanCounterLogic.main(new String[]{"-1", "luck"});
		try {
			assertEquals(out.toString("UTF-8"),
                    "Usage: java BeanCounterLogic <number of beans> <luck | skill>"
                            + "\nExample: java BeanCounterLogic 400 luck\n");
		} catch (UnsupportedEncodingException error) {
			error.printStackTrace();
		}
	}

	@Test
	public void testMainRegInput() {
		BeanCounterLogic.main(new String[]{"100", "luck"});
		try {
			assertTrue(out.toString("UTF-8").matches(
                    "Slot bean counts:\n\\d+ \\d+ \\d+ \\d+ \\d+ \\d+ \\d+ \\d+ \\d+ \\d+ \n"));
		} catch (UnsupportedEncodingException error) {
			error.printStackTrace();
		}
	}

	@Test
	public void testStupidTestModeWhichIsCoveredByModelChecking() {
		BeanCounterLogic.main(new String[]{"test"});
	}

	@Test
	public void testGetRemBeanCount() {
		assert b.getRemainingBeanCount() == 0;
	}

	@Test
	public void testReset() {
		b.pegs[1] = Mockito.mock(Bean.class);
		b.slots[0].add(Mockito.mock(Bean.class));
		b.repeat();
		assert b.pegs[1] == null;
		assert b.slots[0].isEmpty();
	}

	@After
	public void tearDown() {
		System.setOut(oldOut); 
	}
}
