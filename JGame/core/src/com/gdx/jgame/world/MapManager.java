package com.gdx.jgame.world;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FilenameUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;

public class MapManager implements Disposable{
	
	// virtual screen size
	public static final float PIXELS_PER_VIRTUAL_P = 1.5f;
	public static final int V_WIDTH = (int) (Gdx.graphics.getWidth() / PIXELS_PER_VIRTUAL_P);
	public static final int V_HEIGHT = (int) (Gdx.graphics.getHeight() / PIXELS_PER_VIRTUAL_P);
	public static final float SCREEN_RATIO = V_WIDTH / V_HEIGHT;
	public static float PIXELS_PER_METER = 100f;
	
	private TreeMap<String, TiledMap> m_map;
	private OrthogonalTiledMapRenderer m_renderer;
	private TiledMap m_actualMap = null;
	private String m_actualMapName;	
	
	public MapManager(String... paths) {
		m_map = new TreeMap<String, TiledMap>();
		
		add(paths);
	}
	
	public void add(String... paths) {
		for (String path : paths) {
			m_map.put(FilenameUtils.getName(path), new TmxMapLoader().load(path));
		}
	}
	
	public void addWithPath(String... paths) {
		for (String path : paths) {
			m_map.put(path, new TmxMapLoader().load(path));
		}
	}
	
	public int getSize() {
		return m_map.size();
	}
	
	public void setMap(String name) {
		if(m_actualMap != null) m_renderer.dispose();
		
		m_actualMap = m_map.get(name);
		
		if(m_actualMap == null) throw new IllegalArgumentException("No map name found: " + name);
		
		m_renderer = new OrthogonalTiledMapRenderer(m_actualMap, 1 / PIXELS_PER_METER);
		m_actualMapName = name;
	}
	
	public void remove(String... names) {
		TiledMap tmp;
		for (int i = 0; i < names.length; ++i) {
			tmp = m_map.get(names[i]);
			
			// check if they point to the same thing 
			if (tmp == m_actualMap) {
				m_actualMap = null;
				m_renderer.dispose();
			}
			tmp.dispose();
		}
	}
	
	public void render(OrthographicCamera camera) {
		m_renderer.setView(camera);
		m_renderer.render();
	}
	
	@Override
	public void dispose() {
		m_renderer.dispose();
		for (Map.Entry<String, TiledMap> entry : m_map.entrySet()) {
			entry.getValue().dispose();
		}
	}
	
	public String getActualMapName() {
		return m_actualMapName;
	}
	
	public TiledMap getActualMap() {
		return m_actualMap;
	}
	
	public MapLayers getLayers() {
		return m_actualMap.getLayers();
	}
	
	public int getMapWidth() {
		return m_actualMap.getProperties().get("width", Integer.class);
	}
	
	public int getMapHeight() {
		return m_actualMap.getProperties().get("height", Integer.class);
	}
	
	public int getTileWidth() {
		return m_actualMap.getProperties().get("tilewidth", Integer.class);
	}
	
	public int getTileHeight() {
		return m_actualMap.getProperties().get("tileheight", Integer.class);
	}
	
	public int getMapPixelWidth() {
		return getMapWidth()*getTileWidth();
	}
	
	public int getMapPixelHeight() {
		return getMapHeight()*getTileHeight();
	}
	
}
