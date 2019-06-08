package com.gdx.jgame.logic;

public interface ArmoryMethods {
	public float getInitialImpulseNormalBullet();

	public void setInitialImpulseNormalBullet(float initialImpulseNormalBullet);

	public float getInitialImpulseBouncingBullet();

	public void setInitialImpulseBouncingBullet(float initialImpulseBouncingBullet);
	
	public int getMaxAmmoNormalBullet();

	public void setMaxAmmoNormalBullet(int maxAmmoNormalBullet);

	public int getMaxAmmoBouncingBullet();

	public void setMaxAmmoBouncingBullet(int maxAmmoBouncingBullet);

	public int getCurrentAmmoNormalBullet();

	public void setCurrentAmmoNormalBullet(int currentAmmoNormalBullet);

	public int getCurrentAmmoBouncingBullet();

	public void setCurrentAmmoBouncingBullet(int currentAmmoBouncingBullet);
	
}
