package com.gdx.jgame.characters;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PlayerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPlayerFloatFloat() {
		Player test = new Player(9, 11);
		
		assertEquals(test.getPosX(), 9, 0.0001);
		assertEquals(test.getPosY(), 11, 0.0001);
	}

	@Test
	public void testSetPosition() {
		Player test = new Player();
		test.setPosition(9, 11);
		
		assertEquals(test.getPosX(), 9, 0.0001);
		assertEquals(test.getPosY(), 11, 0.0001);
	}

	@Test
	public void testMove() {
		Player test = new Player(9, 11);
		test.move(1, -1);
		
		assertEquals(test.getPosX(), 10, 0.0001);
		assertEquals(test.getPosY(), 10, 0.0001);
	}

}