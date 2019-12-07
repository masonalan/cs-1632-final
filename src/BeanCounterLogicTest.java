import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

import org.mockito.*;
import static org.mockito.Mockito.never;

import java.util.ArrayList;
import java.util.Random;


public class BeanCounterLogicTest {
    // TODO: implement
    // Be sure to mock your beans!
    BeanCounterLogic testLogic;
    Bean[] testFlightBeans;
    ArrayList<Bean> testBeansRemaining;
    ArrayList<ArrayList<Bean>> testBeanSlots;
    int testSlotHalfPoint;
    int testHighestSlotNumber;
    boolean testSetupSlotRan;

    // Create mock objects
    @Before
    public void setUp() {
        // Default testLogic object to test with
        testLogic = new BeanCounterLogic(10);
    }

    // Save memory afterwards
    @After
    public void tearDown() {
        testLogic = null;
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

    @Test
    public void constructorInFlightCheck() {
        testFlightBeans = new Bean[10];
        assertArrayEquals(testLogic.getBeans_inflight(), testFlightBeans);
    }

    @Test
    public void constructorRemainingCheck() {
        testBeansRemaining = new ArrayList<Bean>();
        assertEquals(testLogic.getRem_beans(), testBeansRemaining);
    }

    @Test
    public void setupSlotCheck() {
        testSetupSlotRan = true;
        assertEquals(testLogic.isSlot_ran(), testSetupSlotRan);
    }

    @Test
    public void constructorSlotsCheck() {
        testBeanSlots = new ArrayList<ArrayList<Bean>>();
        for (int i = 0; i < 10; i++) {
            testBeanSlots.add(new ArrayList<Bean>());
        }
        assertEquals(testLogic.getSlots(), testBeanSlots);
    }

    @Test
    public void constructorHalfPointCheck() {
        testSlotHalfPoint = 5;
        assertEquals(testLogic.getSlot_half(), testSlotHalfPoint);
    }

    @Test
    public void constructorHighestNumberCheck() {
        testHighestSlotNumber = 9;
        assertEquals(testLogic.getSlot_high(), testHighestSlotNumber);
    }

    @Test 
    public void getRemainingBeanCountTestNoneAdded() {
        assertEquals(testLogic.getRemainingBeanCount(), 0);
    }

    @Test 
    public void getRemainingBeanCountTestOneAdded() {
        testLogic.getRem_beans().add(new Bean(true, new Random()));

        assertEquals(testLogic.getRemainingBeanCount(), 1);
    }

    @Test 
    public void getRemainingBeanCountTestTenAdded() {
        int size = 10;

        for (int i = 0; i < size; i++) {
            testLogic.getRem_beans().add(new Bean(true, new Random()));
        }

        assertEquals(testLogic.getRemainingBeanCount(), size);
    }

    @Test
    public void getInFlightBeanXPosTestNonePresent() {
        testLogic.getBeans_inflight()[0] = null;

        assertEquals(testLogic.getInFlightBeanXPos(0), -1);
    }

    @Test
    public void getInFlightBeanXPosTestPresentAtOne() {
        int beanXPos = 1;

        Bean testBean = Mockito.mock(Bean.class);
        Mockito.when(testBean.getXPos()).thenReturn(beanXPos);

        testLogic.getBeans_inflight()[0] = testBean;

        assertEquals(testLogic.getInFlightBeanXPos(0), beanXPos);
    }

    @Test
    public void getInFlightBeanXPosTestPresentAtThree() {
        int beanXPos = 2;

        Bean testBean = Mockito.mock(Bean.class);
        Bean testBeanOther = Mockito.mock(Bean.class);
        Mockito.when(testBeanOther.getXPos()).thenReturn(beanXPos);

        testLogic.getBeans_inflight()[0] = testBean;
        testLogic.getBeans_inflight()[1] = testBeanOther;

        assertEquals(testLogic.getInFlightBeanXPos(1), beanXPos);
    }

    @Test
    public void getSlotBeanCountTestSizeZero() {
        assertEquals(testLogic.getSlotBeanCount(0), 0);
    }

    @Test
    public void getSlotBeanCountTestSizeOne() {
        int index = 0;

        Bean testBean = Mockito.mock(Bean.class);
        testLogic.getSlots().get(index).add(testBean);

        assertEquals(testLogic.getSlotBeanCount(index), 1);
    }

    @Test
    public void getSlotBeanCountTestSizeTwo() {
        int index = 0;

        Bean testBean = Mockito.mock(Bean.class);
        Bean testBeanOther = Mockito.mock(Bean.class);
        testLogic.getSlots().get(index).add(testBean);
        testLogic.getSlots().get(index).add(testBeanOther);


        assertEquals(testLogic.getSlotBeanCount(index), 2);
    }

    @Test
    public void getSlotBeanCountTestSizeOneSecondIndex() {
        int index = 1;

        Bean testBean = Mockito.mock(Bean.class);
        testLogic.getSlots().get(index).add(testBean);

        assertEquals(testLogic.getSlotBeanCount(index), 1);
    }

    @Test
    public void getSlotBeanCountTestSizeTwoSecondIndex() {
        int index = 1;

        Bean testBean = Mockito.mock(Bean.class);
        Bean testBeanOther = Mockito.mock(Bean.class);
        testLogic.getSlots().get(index).add(testBean);
        testLogic.getSlots().get(index).add(testBeanOther);


        assertEquals(testLogic.getSlotBeanCount(index), 2);
    }

    @Test
    public void totalSlotBeanCountTestZero() {
        int totalCount = 0;
        assertEquals(testLogic.total_slots(), totalCount);
    }

    @Test
    public void totalSlotBeanCountTestOne() {
        int totalCount = 1;

        Bean testBean = Mockito.mock(Bean.class);
        testLogic.getSlots().get(0).add(testBean);

        assertEquals(testLogic.total_slots(), totalCount);
    }

    @Test
    public void totalSlotBeanCountTestTwo() {
        int totalCount = 2;

        Bean testBean = Mockito.mock(Bean.class);
        for (int i = 0; i < totalCount; i++) {
            testLogic.getSlots().get(0).add(testBean);
        }

        assertEquals(testLogic.total_slots(), totalCount);
    } 

    @Test
    public void totalSlotBeanCountTestTwoAcrossIndexes() {
        int totalCount = 2;

        int beanAddRounds = 1;

        Bean testBean = Mockito.mock(Bean.class);
        for (int i = 0; i < beanAddRounds; i++) {
            testLogic.getSlots().get(0).add(testBean);
            testLogic.getSlots().get(1).add(testBean);
        }

        assertEquals(testLogic.total_slots(), totalCount);
    }    

    @Test
    public void weightedTotalSlotBeanCountTestZero() {
        int result = 0;
        assertEquals(testLogic.weighted_slotcount(), result, 0.001);
    }

    @Test
    public void weightedTotalSlotBeanCountTestOne() {
        int totalCount = 1;
        int result = 0;

        Bean testBean = Mockito.mock(Bean.class);
        testLogic.getSlots().get(0).add(testBean);

        assertEquals(testLogic.weighted_slotcount(), result, 0.001);
    }

    @Test
    public void weightedTotalSlotBeanCountTestTwo() {
        int totalCount = 2;
        int result = 0;

        Bean testBean = Mockito.mock(Bean.class);
        for (int i = 0; i < totalCount; i++) {
            testLogic.getSlots().get(0).add(testBean);
        }

        assertEquals(testLogic.weighted_slotcount(), result, 0.001);
    } 

    @Test
    public void weightedTotalSlotBeanCountTestTwoAcrossIndexes() {
        int result = 1;

        int beanAddRounds = 1;

        Bean testBean = Mockito.mock(Bean.class);
        for (int i = 0; i < beanAddRounds; i++) {
            testLogic.getSlots().get(0).add(testBean);
            testLogic.getSlots().get(1).add(testBean);
        }

        assertEquals(testLogic.weighted_slotcount(), result, 0.001);
    } 

    @Test
    public void weightedTotalSlotBeanCountTestTwoAcrossIndexesLarger() {
        int result = 8;

        int beanAddRounds = 1;

        Bean testBean = Mockito.mock(Bean.class);
        for (int i = 0; i < beanAddRounds; i++) {
            testLogic.getSlots().get(0).add(testBean);
            testLogic.getSlots().get(1).add(testBean);
            testLogic.getSlots().get(2).add(testBean);
            testLogic.getSlots().get(2).add(testBean);
            testLogic.getSlots().get(3).add(testBean);
        }

        assertEquals(testLogic.weighted_slotcount(), result, 0.001);
    } 

    @Test
    public void getAverageSlotBeanCountTestSlotsPresent() {
        double result = 0.0;
        int slotBeanCount = 10;
        double weightedSlotBeanCount = 20.0;

        BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
        Mockito.when(testBeanLogicMock.total_slots()).thenReturn(slotBeanCount);
        Mockito.when(testBeanLogicMock.weighted_slotcount()).thenReturn(weightedSlotBeanCount);

        result = weightedSlotBeanCount / slotBeanCount;

        assertEquals(testLogic.getAverageSlotBeanCount(testBeanLogicMock), result, 0.001);
    }

    @Test
    public void getAverageSlotBeanCountTestSlotsPresentLarger() {
        double result = 0.0;
        int slotBeanCount = 40;
        double weightedSlotBeanCount = 160.0;

        BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
        Mockito.when(testBeanLogicMock.total_slots()).thenReturn(slotBeanCount);
        Mockito.when(testBeanLogicMock.weighted_slotcount()).thenReturn(weightedSlotBeanCount);

        result = weightedSlotBeanCount / slotBeanCount;

        assertEquals(testLogic.getAverageSlotBeanCount(testBeanLogicMock), result, 0.001);
    }

    @Test
    public void getAverageSlotBeanCountTestSlotsNotPresent() {
        double result = 0.0;
        int slotBeanCount = 0;
        double weightedSlotBeanCount = 0;

        BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
        Mockito.when(testBeanLogicMock.total_slots()).thenReturn(slotBeanCount);
        Mockito.when(testBeanLogicMock.weighted_slotcount()).thenReturn(weightedSlotBeanCount);

        assertEquals(testLogic.getAverageSlotBeanCount(testBeanLogicMock), result, 0.001);
    }

    @Test
    public void clearSlotsTestAllClear() {
        testLogic.clear_slots(0, testLogic.getSlots().size());
        assertEquals(testLogic.getSlots().get(0), new ArrayList<Bean>());
    }

    @Test
    public void clearSlotsTestNotAllClear() {
        Bean testBean = Mockito.mock(Bean.class);
        testLogic.getSlots().get(4).add(testBean);

        testLogic.clear_slots(0, 3);

        assertEquals(testLogic.getSlots().get(4).get(0), testBean);
    }

    @Test
    public void clearSlotsTestNotAllClearCheckClear() {
        Bean testBean = Mockito.mock(Bean.class);
        testLogic.getSlots().get(4).add(testBean);

        testLogic.clear_slots(0, 3);

        assertEquals(testLogic.getSlots().get(3), new ArrayList<Bean>());
    }

    @Test
    public void upperHalfTest() {
        BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);

        Mockito.when(testBeanLogicMock.getSlot_half()).thenReturn(5);

        testLogic.upperHalf(testBeanLogicMock);

        Mockito.verify(testBeanLogicMock).clear_slots(0, 5);
    }

    @Test
    public void lowerHalfTest() {
        BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
        testBeanSlots = new ArrayList<ArrayList<Bean>>();

        int slotSize = 5;
        int halfPoint = 0;

        for (int i = 0; i < slotSize; i++) {
            testBeanSlots.add(new ArrayList<Bean>());
        }

        Mockito.when(testBeanLogicMock.getSlot_half()).thenReturn(halfPoint);
        Mockito.when(testBeanLogicMock.getSlots()).thenReturn(testBeanSlots);

        testLogic.lowerHalf(testBeanLogicMock);

        Mockito.verify(testBeanLogicMock).clear_slots(halfPoint, slotSize);
    }

    @Test
    public void sendBeanTestSizeZero() {
        BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
        testBeanSlots = new ArrayList<ArrayList<Bean>>();

        Mockito.when(testBeanLogicMock.getSlots()).thenReturn(testBeanSlots);

        assertEquals(testLogic.sendBean(testBeanLogicMock), -1);
    }

    @Test
    public void sendBeanTestSizeNotOneNoneRemaining() {
        BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
        testBeanSlots = new ArrayList<ArrayList<Bean>>();
        testBeansRemaining = new ArrayList<Bean>();
        testBeanSlots.add(testBeansRemaining);
        testFlightBeans = new Bean[10];

        Mockito.when(testBeanLogicMock.getSlots()).thenReturn(testBeanSlots);
        Mockito.when(testBeanLogicMock.getRem_beans()).thenReturn(testBeansRemaining);
        Mockito.when(testBeanLogicMock.getBeans_inflight()).thenReturn(testFlightBeans);

        assertEquals(testLogic.sendBean(testBeanLogicMock), 1);
    }

    @Test
    public void sendBeanTestSizeNotOneBeansRemain() {
        BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
        testBeanSlots = new ArrayList<ArrayList<Bean>>();
        testBeansRemaining = new ArrayList<Bean>();
        testBeanSlots.add(testBeansRemaining);
        Bean testBean = new Bean(true, new Random());
        testBeansRemaining.add(testBean);
        testFlightBeans = new Bean[10];

        Mockito.when(testBeanLogicMock.getSlots()).thenReturn(testBeanSlots);
        Mockito.when(testBeanLogicMock.getRem_beans()).thenReturn(testBeansRemaining);
        Mockito.when(testBeanLogicMock.getBeans_inflight()).thenReturn(testFlightBeans);

        assertEquals(testLogic.sendBean(testBeanLogicMock), 0);
        assertEquals(testBeanLogicMock.getBeans_inflight()[0], testBean);
    }

    @Test
    public void resetTestClearNoBean() {
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
        testBeanSlots = new ArrayList<ArrayList<Bean>>();
        testBeanSlots.add(testBeansRemaining);
        testBeansRemaining = new ArrayList<Bean>();
        testBeansRemaining.add(new Bean(true, new Random()));
		testFlightBeans = new Bean[10];

		Mockito.when(testBeanLogicMock.getSlots()).thenReturn(testBeanSlots);
        Mockito.when(testBeanLogicMock.getRem_beans()).thenReturn(testBeansRemaining);
        Mockito.when(testBeanLogicMock.getBeans_inflight()).thenReturn(testFlightBeans);

        testLogic.reset(testFlightBeans, testBeanLogicMock, true);

        assertEquals(testBeansRemaining.size(), 0);
        Mockito.verify(testBeanLogicMock).clear_slots(0, 1);
        Mockito.verify(testBeanLogicMock).sendBean(testBeanLogicMock);
    }

    @Test
    public void resetTestClearOneBean() {
        BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
        testBeanSlots = new ArrayList<ArrayList<Bean>>();
        testBeanSlots.add(testBeansRemaining);
        testBeansRemaining = new ArrayList<Bean>();
        testBeansRemaining.add(new Bean(true, new Random()));
        testFlightBeans = new Bean[10];
        testFlightBeans[0] = new Bean(true, new Random());

        Mockito.when(testBeanLogicMock.getSlots()).thenReturn(testBeanSlots);
        Mockito.when(testBeanLogicMock.getRem_beans()).thenReturn(testBeansRemaining);
        Mockito.when(testBeanLogicMock.getBeans_inflight()).thenReturn(testFlightBeans);

        testLogic.reset(testFlightBeans, testBeanLogicMock, true);

        assertEquals(testBeansRemaining.size(), 1);
        Mockito.verify(testBeanLogicMock).clear_slots(0, 1);
        Mockito.verify(testBeanLogicMock).sendBean(testBeanLogicMock);
    }

    @Test
    public void resetTestNoClear() {
        BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
        testBeanSlots = new ArrayList<ArrayList<Bean>>();
        testBeanSlots.add(testBeansRemaining);
        testBeansRemaining = new ArrayList<Bean>();
        testBeansRemaining.add(new Bean(true, new Random()));
        testFlightBeans = new Bean[10];
        testFlightBeans[0] = new Bean(true, new Random());
        testFlightBeans[1] = new Bean(true, new Random());

        Mockito.when(testBeanLogicMock.getSlots()).thenReturn(testBeanSlots);
        Mockito.when(testBeanLogicMock.getRem_beans()).thenReturn(testBeansRemaining);
        Mockito.when(testBeanLogicMock.getBeans_inflight()).thenReturn(testFlightBeans);

        testLogic.reset(testFlightBeans, testBeanLogicMock, false);

        assertEquals(testBeansRemaining.size(), 3);
        Mockito.verify(testBeanLogicMock).clear_slots(0, 1);
        Mockito.verify(testBeanLogicMock).sendBean(testBeanLogicMock);
    }

    @Test
    public void addSlotBeansToRemainingTestOne() {
    	ArrayList<Bean> testBeanArray = new ArrayList<Bean>();
    	testLogic.getSlots().add(testBeanArray);

    	testLogic.add_rem_slots();

    	assertEquals(testLogic.getRem_beans(), testBeanArray);
    }

    @Test
    public void addSlotBeansToRemainingTestMultiple() {
    	ArrayList<Bean> testBeanArray = new ArrayList<Bean>();
    	ArrayList<Bean> testBeanArrayOther = new ArrayList<Bean>();
    	testLogic.getSlots().add(testBeanArray);
    	testLogic.getSlots().add(testBeanArrayOther);

    	testBeansRemaining = new ArrayList<Bean>();
    	testBeansRemaining.addAll(testBeanArray);
    	testBeansRemaining.addAll(testBeanArrayOther);

    	testLogic.add_rem_slots();

    	assertEquals(testLogic.getRem_beans(), testBeansRemaining);
    }

    @Test
    public void repeatTest() {
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
    	testFlightBeans = new Bean[10];

    	Mockito.when(testBeanLogicMock.getBeans_inflight()).thenReturn(testFlightBeans);

    	testLogic.repeat(testBeanLogicMock);

    	Mockito.verify(testBeanLogicMock).add_rem_slots();
        Mockito.verify(testBeanLogicMock).reset(testFlightBeans, testBeanLogicMock, false);
    }

    @Test
    public void moveBeanTestSlotEqualHighest() {
    	int slot = 3;
    	int highestSlot = 3;
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);

    	testBeansRemaining = new ArrayList<Bean>();
    	testBeanSlots = new ArrayList<ArrayList<Bean>>();
        testBeanSlots.add(testBeansRemaining);

		Mockito.when(testBeanLogicMock.getSlots()).thenReturn(testBeanSlots);
    	Mockito.when(testBeanLogicMock.getSlot_high()).thenReturn(highestSlot);

    	Bean testBean = Mockito.mock(Bean.class);
    	Mockito.when(testBean.getXPos()).thenReturn(0);

    	assertEquals(testLogic.moveBean(slot, testBean, testBeanLogicMock), 0);
    }

    @Test
    public void moveBeanTestSlotNotHighest() {
    	int slot = 3;
    	int highestSlot = 4;
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);

    	testBeansRemaining = new ArrayList<Bean>();
    	testBeanSlots = new ArrayList<ArrayList<Bean>>();
    	testFlightBeans = new Bean[10];
        testBeanSlots.add(testBeansRemaining);

		Mockito.when(testBeanLogicMock.getSlots()).thenReturn(testBeanSlots);
    	Mockito.when(testBeanLogicMock.getSlot_high()).thenReturn(highestSlot);
    	Mockito.when(testBeanLogicMock.getBeans_inflight()).thenReturn(testFlightBeans);

    	Bean testBean = Mockito.mock(Bean.class);
    	Mockito.when(testBean.getXPos()).thenReturn(0);

    	assertEquals(testLogic.moveBean(slot, testBean, testBeanLogicMock), 1);
    	assertEquals(testFlightBeans[4], testBean);

    	Mockito.verify(testBean).fallOnce();
    }

    @Test
    public void setNoneInFlightTestLessThan() {
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
    	int i = 2;
    	int highestSlot = 4;
    	testFlightBeans = new Bean[10];

    	Mockito.when(testBeanLogicMock.getSlot_high()).thenReturn(highestSlot);
    	Mockito.when(testBeanLogicMock.getBeans_inflight()).thenReturn(testFlightBeans);

    	assertEquals(testFlightBeans[3], null);

    	assertEquals(testLogic.setNoneInFlight(i, testBeanLogicMock), 0);
    }

    @Test
    public void setNoneInFlightTestGreaterThan() {
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
    	int i = 5;
    	int highestSlot = 4;
    	testFlightBeans = new Bean[10];

    	Mockito.when(testBeanLogicMock.getSlot_high()).thenReturn(highestSlot);
    	Mockito.when(testBeanLogicMock.getBeans_inflight()).thenReturn(testFlightBeans);

    	assertEquals(testFlightBeans[3], null);

    	assertEquals(testLogic.setNoneInFlight(i, testBeanLogicMock), -1);
    }

    @Test
    public void advanceStepTestStatusChange() {
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
    	int highestSlot = 4;
    	testFlightBeans = new Bean[10];
    	boolean statusChange = true;
    	Bean testBean = new Bean(true, new Random());
    	testFlightBeans[4] = testBean;

    	Mockito.when(testBeanLogicMock.getSlot_high()).thenReturn(highestSlot);
    	Mockito.when(testBeanLogicMock.getBeans_inflight()).thenReturn(testFlightBeans);

    	assertEquals(testLogic.advanceStep(testBeanLogicMock), statusChange);

    	Mockito.verify(testBeanLogicMock).sendBean(testBeanLogicMock);
    	Mockito.verify(testBeanLogicMock).moveBean(4, testBean, testBeanLogicMock);
    }

    @Test
    public void advanceStepTestNoStatusChange() {
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
    	int highestSlot = 4;
    	testFlightBeans = new Bean[10];
    	boolean statusChange = false;
    	Bean testBean = new Bean(true, new Random()); 

    	Mockito.when(testBeanLogicMock.getSlot_high()).thenReturn(highestSlot);
    	Mockito.when(testBeanLogicMock.getBeans_inflight()).thenReturn(testFlightBeans);

    	assertEquals(testLogic.advanceStep(testBeanLogicMock), statusChange);

    	Mockito.verify(testBeanLogicMock).sendBean(testBeanLogicMock);
    	Mockito.verify(testBeanLogicMock, never()).moveBean(4, testBean, testBeanLogicMock);
    	Mockito.verify(testBeanLogicMock).setNoneInFlight(4, testBeanLogicMock);
    }

    @Test
    public void showUsageTest() {
    	ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        String correct = "Usage: java BeanCounterLogic <number of beans> <luck | skill>\nExample: java BeanCounterLogic 400 luck\n";

    	BeanCounterLogic.showUsage();
    	
    	assertEquals(correct, outContent.toString());
    }

    @Test
    public void getInFlightBeansTestNone() {
    	int totalInSlotBeans = 0;
    	int slotCount = 1;
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);

    	Mockito.when(testBeanLogicMock.getInFlightBeanXPos(0)).thenReturn(-1);

    	assertEquals(testLogic.getInFlightBeans(testBeanLogicMock, slotCount), totalInSlotBeans);
    }

    @Test
    public void getInFlightBeansTestOne() {
    	int totalInSlotBeans = 1;
    	int slotCount = 1;
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);

    	Mockito.when(testBeanLogicMock.getInFlightBeanXPos(0)).thenReturn(0);

    	assertEquals(testLogic.getInFlightBeans(testBeanLogicMock, slotCount), totalInSlotBeans);
    }

    @Test
    public void getInFlightBeansTestMultiple() {
    	int totalInSlotBeans = 2;
    	int slotCount = 2;
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);

    	Mockito.when(testBeanLogicMock.getInFlightBeanXPos(0)).thenReturn(0);
    	Mockito.when(testBeanLogicMock.getInFlightBeanXPos(1)).thenReturn(0);

    	assertEquals(testLogic.getInFlightBeans(testBeanLogicMock, slotCount), totalInSlotBeans);
    }

    @Test
    public void getInFlightBeansTestMultipleDifferent() {
    	int totalInSlotBeans = 1;
    	int slotCount = 2;
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);

    	Mockito.when(testBeanLogicMock.getInFlightBeanXPos(0)).thenReturn(0);
    	Mockito.when(testBeanLogicMock.getInFlightBeanXPos(1)).thenReturn(-1);

    	assertEquals(testLogic.getInFlightBeans(testBeanLogicMock, slotCount), totalInSlotBeans);
    }

    @Test
    public void getInSlotsCountTestNone() {
    	int totalInSlotBeans = 0;
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);

    	Mockito.when(testBeanLogicMock.getSlotBeanCount(0)).thenReturn(0);

    	assertEquals(testLogic.getInSlotsCount(testBeanLogicMock, 1), totalInSlotBeans);
    }

    @Test
    public void getInSlotsCountTestOne() {
    	int totalInSlotBeans = 1;
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);

    	Mockito.when(testBeanLogicMock.getSlotBeanCount(0)).thenReturn(1);

    	assertEquals(testLogic.getInSlotsCount(testBeanLogicMock, 1), totalInSlotBeans);
    }

    @Test
    public void getInSlotsCountTestMultiple() {
    	int totalInSlotBeans = 3;
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);

    	Mockito.when(testBeanLogicMock.getSlotBeanCount(0)).thenReturn(1);
    	Mockito.when(testBeanLogicMock.getSlotBeanCount(1)).thenReturn(2);

    	assertEquals(testLogic.getInSlotsCount(testBeanLogicMock, 2), totalInSlotBeans);
    }

    @Test
    public void sumOfBeansTestSmall() {
    	int sumOfBeans = 3;
    	int slotCount = 2;
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);

    	Mockito.when(testBeanLogicMock.getRemainingBeanCount()).thenReturn(1);
    	Mockito.when(testBeanLogicMock.getInFlightBeans(testBeanLogicMock, slotCount)).thenReturn(1);
    	Mockito.when(testBeanLogicMock.getInSlotsCount(testBeanLogicMock, slotCount)).thenReturn(1);

    	assertEquals(testLogic.sumOfBeans(testBeanLogicMock, slotCount), sumOfBeans);
    }

    @Test
    public void sumOfBeansTestLarge() {
    	int sumOfBeans = 10;
    	int slotCount = 2;
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);

    	Mockito.when(testBeanLogicMock.getRemainingBeanCount()).thenReturn(4);
    	Mockito.when(testBeanLogicMock.getInFlightBeans(testBeanLogicMock, slotCount)).thenReturn(4);
    	Mockito.when(testBeanLogicMock.getInSlotsCount(testBeanLogicMock, slotCount)).thenReturn(2);

    	assertEquals(testLogic.sumOfBeans(testBeanLogicMock, slotCount), sumOfBeans);
    }

    @Test
    public void setupBeansNoLuck() {
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
    	Bean[] testBeans = new Bean[10];
    	int beanCount = 4;
    	boolean luck = false;

    	BeanCounterLogic.setupBeans(testBeanLogicMock, testBeans, 4, luck);

    	for(int i = 0; i < beanCount; i++) {
    		assertNotEquals(testBeans[i], null);
    	}
    	Mockito.verify(testBeanLogicMock).reset(testBeans, testBeanLogicMock, true);
    }

    @Test
    public void setupBeansWithLuck() {
    	BeanCounterLogic testBeanLogicMock = Mockito.mock(BeanCounterLogic.class);
    	Bean[] testBeans = new Bean[10];
    	int beanCount = 4;
    	boolean luck = true;

    	BeanCounterLogic.setupBeans(testBeanLogicMock, testBeans, 4, luck);

    	for(int i = 0; i < beanCount; i++) {
    		assertNotEquals(testBeans[i], null);
    	}
    	Mockito.verify(testBeanLogicMock).reset(testBeans, testBeanLogicMock, true);
    }    
}