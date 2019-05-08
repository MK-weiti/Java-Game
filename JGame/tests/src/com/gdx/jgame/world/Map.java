package com.gdx.jgame.world;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class Map {
	
	public static final String path = "map.tmx";
	
	private static TiledMap m_map;
	private static OrthogonalTiledMapRenderer m_renderer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		m_map = new TmxMapLoader().load(path);
		assertTrue(m_map != null);
		m_renderer = new OrthogonalTiledMapRenderer(m_map);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		m_map.dispose();
		m_renderer.dispose();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		
	}

}
