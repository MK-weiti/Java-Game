/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.tomgrill.gdxtesting.examples;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.gdx.jgame.gameObjects.characters.Player;
import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class AssetExistsExampleTest {
	
	public static final String path = "map.tmx";
	
	static Player test;
	static Texture tx;
	//static MapManager m_map;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		/*tx = new Texture("badlogic.jpg");
		m_map = new MapManager(path);	
		assertTrue(m_map != null);
		test = new Player( (TiledMapTileLayer) m_map.getLayers().get("background"), 
				tx, 9, 11, 250);*/
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		tx.dispose();
		//m_map.dispose();
	}

	@Before
	public void setUp() throws Exception {
		//test.setPosition(9, 11);
	}

	@After
	public void tearDown() throws Exception{
	}
	
	@Test
	public void testLoadMap() {
		//assertTrue((TiledMapTileLayer) m_map.getLayers().get("background") != null);
	}

	@Test
	public void testPlayerFloatFloat() {
		
		//assertEquals(test.getX(), 9, 0.0001);
		//assertEquals(test.getY(), 11, 0.0001);
	}

	@Test
	public void testSetPosition() {
		
		//assertEquals(test.getX(), 9, 0.0001);
		//assertEquals(test.getY(), 11, 0.0001);
	}

	@Test
	public void testMove() {
		//test.move(1, -1);
		
		//assertEquals(test.getX(), 10, 0.0001);
		//assertEquals(test.getY(), 10, 0.0001);
	}
	
	
	@Test
	public void badlogicLogoFileExists() {
		assertTrue("This test will only pass when the badlogic.jpg file coming "
				+ "with a new project setup has not been deleted.", Gdx.files
				.internal("badlogic.jpg").exists());
	}
	
	
	
}
