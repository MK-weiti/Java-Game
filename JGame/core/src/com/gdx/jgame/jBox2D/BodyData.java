package com.gdx.jgame.jBox2D;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class BodyData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5935905197816672365L;
	
	public BodyType type = BodyType.StaticBody;
	public final Vector2 position = new Vector2();
	public float angle = 0;
	public final Vector2 linearVelocity = new Vector2();
	public float angularVelocity = 0;
	public float linearDamping = 0;
	public float angularDamping = 0;
	public boolean allowSleep = true;
	public boolean awake = true;
	public boolean fixedRotation = false;
	public boolean bullet = false;
	public boolean active = true;
	public float gravityScale = 1;
	
	public BodyData() {} // default
	
	public BodyData(Body body) {
		this.type = body.getType();
		this.position.set(body.getPosition());
		this.angle = body.getAngle();
		this.linearVelocity.set(body.getLinearVelocity());
		this.angularVelocity = body.getAngularVelocity();
		this.linearDamping = body.getLinearDamping();
		this.angularDamping = body.getAngularDamping();
		this.allowSleep = body.isSleepingAllowed();
		this.awake = body.isAwake();
		this.fixedRotation = body.isFixedRotation();
		this.bullet = body.isBullet();
		this.active = body.isActive();
		this.gravityScale = body.getGravityScale();
	}

	public BodyData(BodyDef bodyDef) {
		this.type = bodyDef.type;
		this.position.set(bodyDef.position);
		this.angle = bodyDef.angle;
		this.linearVelocity.set(bodyDef.linearVelocity);
		this.angularVelocity = bodyDef.angularVelocity;
		this.linearDamping = bodyDef.linearDamping;
		this.angularDamping = bodyDef.angularDamping;
		this.allowSleep = bodyDef.allowSleep;
		this.awake = bodyDef.awake;
		this.fixedRotation = bodyDef.fixedRotation;
		this.bullet = bodyDef.bullet;
		this.active = bodyDef.active;
		this.gravityScale = bodyDef.gravityScale;
	}
	
	public BodyData(BodyData bodyDef) {
		this.type = bodyDef.type;
		this.position.set(bodyDef.position);
		this.angle = bodyDef.angle;
		this.linearVelocity.set(bodyDef.linearVelocity);
		this.angularVelocity = bodyDef.angularVelocity;
		this.linearDamping = bodyDef.linearDamping;
		this.angularDamping = bodyDef.angularDamping;
		this.allowSleep = bodyDef.allowSleep;
		this.awake = bodyDef.awake;
		this.fixedRotation = bodyDef.fixedRotation;
		this.bullet = bodyDef.bullet;
		this.active = bodyDef.active;
		this.gravityScale = bodyDef.gravityScale;
	}
	
	public void synchronize(BodyDef bodyDef) {
		this.type = bodyDef.type;
		this.position.set(bodyDef.position);
		this.angle = bodyDef.angle;
		this.linearVelocity.set(bodyDef.linearVelocity);
		this.angularVelocity = bodyDef.angularVelocity;
		this.linearDamping = bodyDef.linearDamping;
		this.angularDamping = bodyDef.angularDamping;
		this.allowSleep = bodyDef.allowSleep;
		this.awake = bodyDef.awake;
		this.fixedRotation = bodyDef.fixedRotation;
		this.bullet = bodyDef.bullet;
		this.active = bodyDef.active;
		this.gravityScale = bodyDef.gravityScale;
	}
	
	public void restore(BodyDef bodyDef) {
		bodyDef.type = type;
		bodyDef.position.set(position);
		bodyDef.angle = angle;
		bodyDef.linearVelocity.set(linearVelocity);
		bodyDef.angularVelocity = angularVelocity;
		bodyDef.linearDamping = linearDamping;
		bodyDef.angularDamping = angularDamping;
		bodyDef.allowSleep = allowSleep;
		bodyDef.awake = awake;
		bodyDef.fixedRotation = fixedRotation;
		bodyDef.bullet = bullet;
		bodyDef.active = active;
		bodyDef.gravityScale = gravityScale;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + (allowSleep ? 1231 : 1237);
		result = prime * result + Float.floatToIntBits(angle);
		result = prime * result + Float.floatToIntBits(angularDamping);
		result = prime * result + Float.floatToIntBits(angularVelocity);
		result = prime * result + (awake ? 1231 : 1237);
		result = prime * result + (bullet ? 1231 : 1237);
		result = prime * result + (fixedRotation ? 1231 : 1237);
		result = prime * result + Float.floatToIntBits(gravityScale);
		result = prime * result + Float.floatToIntBits(linearDamping);
		result = prime * result + ((linearVelocity == null) ? 0 : linearVelocity.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		BodyData other = (BodyData) obj;
		if (active != other.active)
			return false;
		if (allowSleep != other.allowSleep)
			return false;
		if (Float.floatToIntBits(angle) != Float.floatToIntBits(other.angle))
			return false;
		if (Float.floatToIntBits(angularDamping) != Float.floatToIntBits(other.angularDamping))
			return false;
		if (Float.floatToIntBits(angularVelocity) != Float.floatToIntBits(other.angularVelocity))
			return false;
		if (awake != other.awake)
			return false;
		if (bullet != other.bullet)
			return false;
		if (fixedRotation != other.fixedRotation)
			return false;
		if (Float.floatToIntBits(gravityScale) != Float.floatToIntBits(other.gravityScale))
			return false;
		if (Float.floatToIntBits(linearDamping) != Float.floatToIntBits(other.linearDamping))
			return false;
		if (linearVelocity == null) {
			if (other.linearVelocity != null)
				return false;
		} else if (!linearVelocity.equals(other.linearVelocity))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
