package com.gdx.jgame;

import java.util.*;

import org.apache.commons.io.FilenameUtils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class TextureManager implements Disposable{
	
	private TreeMap<String, Texture> m_map;
	
	
	private void add(String... paths) {		
		for(String path : paths) {
			m_map.put(FilenameUtils.getName(path), new Texture(path));
		}
	}
	
	public void addWithPath(String... paths) {
		for(String path : paths) {
			m_map.put(path, new Texture(path));
		}
	}
	
	public TextureManager(String... paths) {
		m_map = new TreeMap<String, Texture>();
		
		add(paths);
	}

	@Override
	public void dispose() {
		for(Map.Entry<String, Texture> entry : m_map.entrySet()) {
			entry.getValue().dispose();
		}		
	}
	
	public Texture get(String name) {
		Texture tmp = m_map.get(name);
		
		if(tmp != null) return tmp;
		
		throw new IllegalArgumentException("No texture name found: " + name);
	}
	
	public int getSize() {
		return m_map.size();
	}
	
	public void remove(String name) {
		Texture tmp = m_map.remove(name);
		
		if(tmp !=null) tmp.dispose();
		
		throw new IllegalArgumentException("No texture name found: " + name);
	}
	
	
}
