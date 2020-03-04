package com.gdx.jgame.managers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.gdx.interpreter.CommandInterpreter;

public class MapManager{
	private class MapHierarchy {
		
		public class Vertex{
			private TiledMap map;
			private String mapName;
			private Map<String, Vertex> children; // edge names
			private List<Vertex> parents;
			
			public Vertex(TiledMap map, String name) {
				this.map = map;
				this.mapName = name;
				children = new TreeMap<String, Vertex>();
				parents = new LinkedList<Vertex>();
			}

			public Collection<Vertex> getChildrenCollection() {
				return children.values();
			}
			/**
			 * 
			 * @return Set of keys from children.
			 */
			public Set<String> getEdgeSet() {
				return children.keySet();
			}
			
			/**
			 * 
			 * @return Set of names and vertices of the children.
			 */
			public Set<Entry<String, Vertex>> getChildrenSet() {
				return children.entrySet();
			}
			
			public Collection<Vertex> getParentsCollection() {
				return parents;
			}
			
			public TiledMap getMap() {
				return map;
			}
			
			/**
			 * 
			 * @return The name of the map.
			 */
			public String getName() {
				return mapName;
			}
			
			/**
			 * Add the child to the parent (this object).
			 * @param child
			 * @param edgeName
			 * @return True if this vertex already have this child. 
			 * <br>False otherwise.</br>
			 */
			public boolean addChild(Vertex child, String edgeName) {
				if(children.values().contains(child)) return true;
				children.put(edgeName, child);
				child.parents.add(this);
				return false;
			}
		}
		
		// choose the starting point
		private Map<String, Vertex> startingPoints;
		
		public MapHierarchy() {
			startingPoints = new TreeMap<>();
		}
		
		/**
		 * 
		 * @param parent
		 * @param child
		 * @param edgeName - the name of the edge. 
		 * If parent will have only one child then the connectionName 
		 * is not taken into account while choosing next map.
		 * @return True if the child is already the child of this parent.
		 * <br> False if the operation was successful and parent have now this child.</br>
		 */
		public boolean createConnection(Vertex parent, Vertex child, String edgeName) {
			
			for(Vertex it : parent.getChildrenCollection()) {
				if(it == child) return true;
			}
			
			return parent.addChild(child, edgeName);
		}
		
		/**
		 * 
		 * @param optionName
		 * @return The vertex which is labeled by this option name.
		 */
		public Vertex returnStartingPoint(String optionName) {
			return startingPoints.get(optionName);
		}
		
		/**
		 * 
		 * @param optionName
		 * @param parent - vertex that can be the first map. 
		 * It is possible to have this parent as a child of a different parent vertex.
		 * @return True if the Vertex is already a starting point 
		 * or if the option name was used before to choose different starting point.
		 */
		public boolean addStartingPoint(String optionName, Vertex parent) {
			if(startingPoints.containsKey(optionName) || startingPoints.containsValue(parent)) 
				return true;
			
			startingPoints.put(optionName, parent);
			return false;
		}
		
		/**
		 * If there was some error while reading the file then no changes was performed to this object.
		 * @param path - path to file
		 * @return False if the hierarchy was successfully read from file.
		 * True otherwise. 
		 */
		public boolean loadHierarchyFromFile(String path) {
			// TODO
			
			return false;
		}
		
	}
	
	// virtual screen size
	public static final float PIXELS_PER_VIRTUAL_P = 1.5f;
	public static final int V_WIDTH = (int) (Gdx.graphics.getWidth() / PIXELS_PER_VIRTUAL_P);
	public static final int V_HEIGHT = (int) (Gdx.graphics.getHeight() / PIXELS_PER_VIRTUAL_P);
	public static final float SCREEN_RATIO = V_WIDTH / V_HEIGHT;
	public static float PIXELS_PER_METER = 100f;
	
	private TreeMap<String, MapHierarchy.Vertex> m_map;
	private MapHierarchy hierarchy;
	private OrthogonalTiledMapRenderer m_renderer = null;
	private TiledMap m_actualMap = null;
	private MapHierarchy.Vertex actualMap = null;
	private String m_actualMapName;	
	
	MapManager(String... paths) {
		m_map = new TreeMap<String, MapHierarchy.Vertex>();
		hierarchy = new MapHierarchy();
		
		add(paths);
	}
	
	// TODO
	/**
	 * 
	 * @param pathToFileHierarchy - file which contains a hierarchy of the maps
	 * 
	 * <br><br> -- (first map in hierarchy)
	 * <br> (previous map) -> (next map) </br>
	 * 
	 * <br> Names of the maps must be the same in both files. 
	 * The only part of the path that counts is the name of the file and its ending.
	 * </br> <br>
	 * 
	 * @param pathToFileMaps - file that contains all paths to maps.
	 * <br> It only contains paths with no other special signs. </br> <br>
	 * @param option - only to differentiate the other constructor. Any value is good.
	 */
	MapManager(String pathToFileHierarchy, String pathToFileMaps, boolean option) {
		m_map = new TreeMap<String, MapHierarchy.Vertex>();
		hierarchy = new MapHierarchy();
		
		Vector<String> paths;
		String content = null;
		String contentMaps = null;
		CommandInterpreter interpreter = new CommandInterpreter();
		
		try {
			content = new String(Files.readAllBytes(Paths.get(pathToFileHierarchy)));	
			contentMaps = new String(Files.readAllBytes(Paths.get(pathToFileMaps)));	
		}
		catch(Exception e) {
			// TODO
		}
		
		interpreter.setStringsToBlacklist(" ", "\n");
		//interpreter.setConstraintsChar(' ', '\n');
		interpreter.setCommand(contentMaps);
		
		while(interpreter.isEnd()) {
			String path = interpreter.getNext(), name;
			m_map.put(name = FilenameUtils.getName(path), hierarchy.new Vertex(new TmxMapLoader().load(path), name));
		}
		
		
		
		interpreter.setCommand(content);
		
		MapHierarchy.Vertex lastVertex = null;
		while(interpreter.isEnd()) {
			String tmp1 = interpreter.getNext();
			String tmp2;
			if(tmp1.contains("--")) {
				tmp1 = interpreter.getNext();
				tmp2 = interpreter.getNextNoTrace();
				
				if(tmp2.contains("[a-zA-Z]+") == true && tmp2.length() > 1) {
					hierarchy.addStartingPoint(tmp2, lastVertex = m_map.get(tmp1));
					interpreter.getNext();
					continue;
				} 
				else {
					hierarchy.addStartingPoint("NULL", lastVertex = m_map.get(tmp1));
					continue;
				}
			}
			else if(tmp1.contains("->")) {
				if(lastVertex == null) throw new IllegalArgumentException("The file has incorrect data: " + pathToFileHierarchy);
				
				tmp1 = interpreter.getNext();
				tmp2 = interpreter.getNextNoTrace();
				
				MapHierarchy.Vertex newVertex = m_map.get(tmp1);
				
				if(tmp2.contains("[a-zA-Z]+") == true && tmp2.length() > 1) {
					hierarchy.createConnection(lastVertex, newVertex, tmp2);
					interpreter.getNext();
					continue;
				} 
				else {
					hierarchy.createConnection(lastVertex, newVertex, "NULL");
					continue;
				}
			}
			else {
				continue;
			}
		}
			
		add(pathToFileHierarchy);
	}
	
	/**
	 * Load maps from files and sets their names from the path 
	 * using the last name in hierarchy with extension.
	 * <br>a/b/c.txt --> c.txt
	 * <br>a.txt     --> a.txt
	 * <br>a/b/c     --> c
	 * <br>a/b/c/    --> ""
	 * 
	 * <br><br> Throw IllegalArgumentException if the map names are repeating.
	 *
	 * @param paths - paths to files
	 */
	void add(String... paths) {
		for (String path : paths) {
			String name = FilenameUtils.getName(path);
			if(m_map.containsKey(name)) throw new IllegalArgumentException("Map name was already used: " + name + " with path: " + path);
			m_map.put(name, hierarchy.new Vertex(new TmxMapLoader().load(path), name));
		}
	}
	
	/**
	 * Load maps from files and sets their names as full path to files.
	 * @param paths - paths to files
	 */
	void addWithFullPath(String... paths) {
		for (String path : paths) {
			m_map.put(path, hierarchy.new Vertex(new TmxMapLoader().load(path), path));
		}
	}
	
	int getNumberOfMaps() {
		return m_map.size();
	}
	
	/**
	 * Sets map that will be displayed.
	 * Last render of the map was disposed (map can be used again).
	 * <br>
	 * Throw IllegalArgumentException if the map was not found
	 * @param name - name of the map.
	 */
	void setMap(String name) {
		if(m_actualMap != null) m_renderer.dispose();
		
		MapHierarchy.Vertex tmp = m_map.get(name);
		if(tmp == null) throw new IllegalArgumentException("No map with this name was found: " + name);
		
		m_actualMap = tmp.getMap();
		
		m_renderer = new OrthogonalTiledMapRenderer(m_actualMap, 1 / PIXELS_PER_METER);
		m_actualMapName = name;
	}
	
	
	/** 
	 * @return True if the number of children was different from 1. 
	 * Then while choosing next map the option must be specified or child must be added.
	 * <br>False if the operation was successful.
	 */
	boolean chooseNextMap() {
		Collection<MapHierarchy.Vertex> children;
		if((children = m_map.get(m_actualMapName).getChildrenCollection()).size() != 1) return true;
		
		if(m_actualMap != null) m_renderer.dispose();
		
		MapHierarchy.Vertex child = children.iterator().next();
		
		m_actualMap = child.getMap();
		
		m_renderer = new OrthogonalTiledMapRenderer(m_actualMap, 1 / PIXELS_PER_METER);
		m_actualMapName = child.getName();
		
		return false;
	}
	
	/**
	 * Set map via vertex
	 * @param vertex
	 */
	private void setMap(MapHierarchy.Vertex vertex) {
		m_actualMap = vertex.getMap();
		
		m_renderer = new OrthogonalTiledMapRenderer(m_actualMap, 1 / PIXELS_PER_METER);
		m_actualMapName = vertex.getName();
	}
	
	/**
	 * 
	 * @param connectionName
	 * @return True if there were no children with this name.
	 * <br>False if the operation was successful.
	 */
	boolean chooseNextMap(String connectionName) {
		for(Entry<String, MapHierarchy.Vertex> child : actualMap.getChildrenSet()) {
			if(child.getKey().equals(connectionName)) {
				if(m_actualMap != null) m_renderer.dispose();
				
				setMap(child.getValue());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Choose the first map via option name.
	 * @param optionName
	 * @return True if there was no map with this name.
	 * <br>False if the operation was successful.
	 */
	boolean chooseFirstMap(String optionName) {
		MapHierarchy.Vertex vertex = hierarchy.returnStartingPoint(optionName);
		if(vertex == null) return true;
		
		setMap(vertex);
		
		return false;
	}
	
	/**
	 * Remove maps specified by names. It can also remove map that is in use.
	 * @param names
	 */
	void remove(String... names) {
		TiledMap tmp;
		MapHierarchy.Vertex vertex;
		for (int i = 0; i < names.length; ++i) {
			vertex = m_map.get(names[i]);
			if(vertex == null) continue;
			
			tmp = m_map.get(names[i]).getMap();
			
			// check if they point to the same thing 
			if (tmp == m_actualMap) {
				m_actualMap = null;
				m_renderer.dispose();
			}
			
			tmp.dispose();
		}
	}
	
	/**
	 * Create an connection in hierarchy of maps.
	 * <br></br>
	 * It is presented as the directed graph.
	 * @param parent
	 * @param child
	 * @param connectionName - the name of the edge. 
	 * If parent will have only one child then the connectionName 
	 * is not taken into account while choosing next map.
	 * @return True if the creation of the connection was not successful. 
	 * It may be because edge name was used or the connection already existed.
	 * <br>False otherwise.
	 */
	public boolean createConnection(String parent, String child, String connectionName) {
		MapHierarchy.Vertex one, two;
		if((one = m_map.get(parent)) == null || (two = m_map.get(child)) == null) return true;
		
		return hierarchy.createConnection(one, two, connectionName);
	}
	
	/**
	 * Sets OrthogonalTiledMapRenderer to the camera and then render all the layers of the map.
	 * @param camera - camera that is used to display the scene.
	 */
	public void render(OrthographicCamera camera) {
		m_renderer.setView(camera);
		m_renderer.render();
	}
	
	/**
	 * Dispose rendered OrthogonalTiledMapRenderer if can and dispose all maps.
	 */
	void dispose() {
		if(m_renderer != null) m_renderer.dispose();
		
		for (Map.Entry<String, MapHierarchy.Vertex> entry : m_map.entrySet()) {
			entry.getValue().getMap().dispose();
		}
	}
	
	
	/*
	 * 
	 * Getters and setters
	 * 
	 */
	
	public String getActiveMapName() {
		return m_actualMapName;
	}
	
	public TiledMap getActiveMap() {
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
	
	/**
	 * 
	 * @return Map width * tile width.
	 */
	public int getMapPixelWidth() {
		return getMapWidth()*getTileWidth();
	}
	
	/**
	 * 
	 * @return Map height * tile height.
	 */
	public int getMapPixelHeight() {
		return getMapHeight()*getTileHeight();
	}
	
}
