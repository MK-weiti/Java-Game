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
import com.gdx.jgame.JGame;
import com.gdx.jgame.Utils;
import com.gdx.jgame.gameObjects.characters.PlayerDef;
import com.gdx.jgame.managers.ScreenManager;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class AssetExistsExampleTest {
	
	public static ScreenManager manager;
	public static JGame game;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {
		manager = new ScreenManager(true);
		game = manager.testGame();
	}

	@After
	public void tearDown() throws Exception{
	}
	
	
	@Test
	public void checkPalpableObjectDef() {
		Texture texture = game.getCharactersTextures().get("player.png");
		PlayerDef pldef = new PlayerDef(game.getjBox().getWorld(), game.getCharactersTextures(), game.getBulletsTextures(), 
				game.getMisslesManager(), "player.png", 1f, null, Utils.setVerticesToTexture(texture, 1f));
	}
	
	
	@Test
	public void badlogicLogoFileExists() {
		assertTrue("This test will only pass when the badlogic.jpg file coming "
				+ "with a new project setup has not been deleted.", Gdx.files
				.internal("badlogic.jpg").exists());
	}
	
	
	
}
