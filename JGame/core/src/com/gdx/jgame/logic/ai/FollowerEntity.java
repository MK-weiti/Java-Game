package com.gdx.jgame.logic.ai;

import java.io.Serializable;
import java.util.Map;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.IDAdapter;
import com.gdx.jgame.IDAdapterSerializable;
import com.gdx.jgame.SaveReference;
import com.gdx.jgame.gameObjects.MovingObject;

public class FollowerEntity extends IDAdapterSerializable implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6792245238024456874L;
	
	private transient Arrive<Vector2> arrive;
	private transient Face<Vector2> face;
	private transient BlendedSteering<Vector2> blend;
	
	private SaveReference<MovingObject> owner;
	private SaveReference<MovingObject> target;
	
	private float arrive_arrivalTolerance;
	private float arrive_decelerationRadius;
	private float arrive_timeToTarget;
	
	private float face_alignTolerance;
	private float face_decelerationRadius;
	private float face_timeToTarget;
	
	public FollowerEntity(MovingObject behaviorOwner, MovingObject targer) {
		super();
		owner = new SaveReference<MovingObject>();
		target = new SaveReference<MovingObject>();
		
		owner.setOwner(behaviorOwner);
		target.setOwner(targer);
		
		arrive = new Arrive<Vector2>(behaviorOwner, targer);
		face = new Face<Vector2>(behaviorOwner, targer);
		
		blend = new BlendedSteering<Vector2>(behaviorOwner);
		
	}
	
	public Arrive<Vector2> getArrive() {
		return arrive;
	}
	
	public Face<Vector2> getFace(){
		return face;
	}
	
	public void setBehavior() {
		blend.add(arrive, 1f);
		blend.add(face, 1f);
		owner.getOwner().setBehavior(blend);
	}
	
	@Override
	public void save() {
		owner.save();
		target.save();
		
		arrive_arrivalTolerance = arrive.getArrivalTolerance();
		arrive_decelerationRadius = arrive.getDecelerationRadius();
		arrive_timeToTarget = arrive.getTimeToTarget();
		
		face_alignTolerance = face.getAlignTolerance();
		face_decelerationRadius = face.getDecelerationRadius();
		face_timeToTarget = face.getTimeToTarget();
	}
	
	@Override
	public void load(Map<Integer, IDAdapter> restoreOwner) {	
		owner.load(restoreOwner);
		target.load(restoreOwner);
		
		if(owner.isNull() || target.isNull()) return;
		
		arrive = new Arrive<Vector2>(owner.getOwner(), target.getOwner());
		face = new Face<Vector2>(owner.getOwner(), target.getOwner());
		blend = new BlendedSteering<Vector2>(owner.getOwner());
		
		arrive.setArrivalTolerance(arrive_arrivalTolerance);
		arrive.setDecelerationRadius(arrive_decelerationRadius);
		arrive.setTimeToTarget(arrive_timeToTarget);
		
		face.setAlignTolerance(face_alignTolerance);
		face.setDecelerationRadius(face_decelerationRadius);
		face.setTimeToTarget(face_timeToTarget);
		
		blend.add(arrive, 1f);
		blend.add(face, 1f);
		owner.getOwner().setBehavior(blend);
	}
	
}
