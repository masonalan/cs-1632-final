import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.Random;

public class BeanTest {
	private Bean b;
	private Random r;
	
	@Before
	public void setUp() {
		r = Mockito.mock(Random.class);
		b = new Bean(false, r);
	}

	@After
	public void tearDown() {
		r = null;
		b = null;
	}

	@Test
	public void testStartX() {
		assertEquals(b.getXPos(), 0);
	}

	@Test
	public void testStartRand() {
		assertEquals(b.getRandom(), r);
	}

	@Test
	public void testStartSkill() {
		Mockito.when(r.nextGaussian()).thenReturn(2.0);

		b = new Bean(true, r);

		assertEquals(b.getSkill(), 7);
	}

	@Test
	public void testStartSkillOver() {
		Mockito.when(r.nextGaussian()).thenReturn(5.0);

		b = new Bean(true, r);

		assertEquals(b.getSkill(), 9);
	}

	@Test
	public void testStartSkillUnder() {
		Mockito.when(r.nextGaussian()).thenReturn(-5.0);

		b = new Bean(true, r);

		assertEquals(b.getSkill(), 0);
	}

	@Test
	public void testStartLuckModeOff() {
		assertEquals(b.getLuckModeOn(), false);
	}

	@Test
	public void testStartLuckModeOn() {
		b = new Bean(true, r);

		assertEquals(b.getLuckModeOn(), true);
	}

	@Test
	public void testStartSpaces() {
		Mockito.when(r.nextGaussian()).thenReturn(2.0);

		b = new Bean(true, r);

		assertEquals(b.getSpacesToMove(), 7);
	}

	@Test
	public void testGetX() {
		Mockito.when(r.nextGaussian()).thenReturn(5.0);

		b = new Bean(false, r);

		int falls = 4;
		for(int i = 0; i < falls; i++) {
			b.fallOnce();
		}
		assertEquals(b.getXPos(), 4);
	}

	@Test
	public void testFallLuck() {
		b = new Bean(true, r);

		b.fallOnce();

		Mockito.verify(r).nextBoolean();
	}

	@Test
	public void testFallNoLuck() {
		Mockito.when(r.nextGaussian()).thenReturn(5.0);

		b = new Bean(false, r);

		b.fallOnce();

		assertEquals(b.getSpacesToMove(), 8);
	}

	@Test
	public void testReset() {
		b = new Bean(false, r);

		Mockito.when(r.nextGaussian()).thenReturn(3.0);

		int startX = b.getXPos();
		b.fallOnce();
		int endX = b.getXPos();
		b.reset();
		int resetX = b.getXPos();

		assertEquals(startX, resetX);
		assertNotEquals(startX, endX);

		assertEquals(b.getSpacesToMove(), b.getSkill());
	}
}