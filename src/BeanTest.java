import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import org.mockito.*;

public class BeanTest {

	@Test
	public void testConstructorLuck()
	{
		Bean b = new Bean(true, new Random());
		assertEquals(b.isLuck, true);
	}

	@Test
	public void testConstructorNotLuck()
	{
		Bean b = new Bean(false, new Random());
		assertEquals(b.isLuck, false);
	}

	@Test
	public void testConstructorGeneratesSkill()
	{
		Bean b = new Bean(false, new Random());
		assertTrue(b.right > -1);
	}

	@Test
	public void testFallSkillFallsRight()
	{
		Bean b = new Bean(false, new Random());
		b.right = 1;
		int r = b.fall();
		assertEquals(r, 1);
	}

	@Test
	public void testFallSkillDecreasesRight()
	{
		Bean b = new Bean(false, new Random());
		b.right = 1;
		int r = b.fall();
		assertEquals(b.right, 0);
	}

	@Test
	public void testFallLuckFallsRight()
	{
		Random rand = Mockito.mock(Random.class);
		Mockito.when(rand.nextInt(2)).thenReturn(1);
		Bean b = new Bean(true, rand);
		int r = b.fall();
		assertEquals(r, 1);
	}

	@Test
	public void testFallLuckFallsLeft()
	{
		Random rand = Mockito.mock(Random.class);
		Mockito.when(rand.nextInt(2)).thenReturn(0);
		Bean b = new Bean(true, rand);
		int r = b.fall();
		assertEquals(r, 0);
	}
}