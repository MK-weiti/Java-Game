package com.gdx.jgame.gameObjects.missiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import com.gdx.jgame.IDAdapter;
import com.gdx.jgame.JGame;
import com.gdx.jgame.gameObjects.missiles.def.NormalBulletDef;
import com.gdx.jgame.gameObjects.missiles.def.BouncingBulletDef;

public class SaveMisiles implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -371393036729203637L;
	
	private static long m_numberOfObjects = 0;
	public transient final long ID = m_numberOfObjects;
	
	
	private transient MissilesManager m_missileManager;
	private ArrayList<NormalBulletDef> normalBulletDef;
	private ArrayList<BouncingBulletDef> bouncingBulletDef;
	
	public SaveMisiles(MissilesManager manager){
		++m_numberOfObjects;
		normalBulletDef = new ArrayList<NormalBulletDef>();
		bouncingBulletDef = new ArrayList<BouncingBulletDef>();
		m_missileManager = manager;
	}
	
	
	public void save() {
		for(Missile missile : m_missileManager.getMissiles()) {
			
			if(missile instanceof NormalBullet) {
				NormalBulletDef tmp = new NormalBulletDef(  ((NormalBullet) missile)  );
				normalBulletDef.add(tmp);
			}
			else if(missile instanceof BouncingBullet) {
				BouncingBulletDef tmp = new BouncingBulletDef(  ((BouncingBullet) missile)  );
				bouncingBulletDef.add(tmp);
			}
		}
	}
	
	// get from Map
	public void load(JGame jGame, Map<Integer, IDAdapter> ownerSet) {
		m_missileManager = jGame.getMisslesManager();
		
		for(NormalBulletDef def : normalBulletDef) {
			def.restore(jGame.getBulletsTextures(), jGame.getjBox().getWorld(), ownerSet);
			m_missileManager.add(new NormalBullet(def, def.getOwner()));			
		}
		
		for(BouncingBulletDef def : bouncingBulletDef) {
			def.restore(jGame.getBulletsTextures(), jGame.getjBox().getWorld(), ownerSet);
			m_missileManager.add(new BouncingBullet(def, def.getOwner()));			
		}
	}
	
	public long numberOfObjects() {
		return m_numberOfObjects;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bouncingBulletDef == null) ? 0 : bouncingBulletDef.hashCode());
		result = prime * result + ((normalBulletDef == null) ? 0 : normalBulletDef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaveMisiles other = (SaveMisiles) obj;
		if (bouncingBulletDef == null) {
			if (other.bouncingBulletDef != null)
				return false;
		} else if (!bouncingBulletDef.equals(other.bouncingBulletDef))
			return false;
		if (normalBulletDef == null) {
			if (other.normalBulletDef != null)
				return false;
		} else if (!normalBulletDef.equals(other.normalBulletDef))
			return false;
		return true;
	}
	
	
	
}
