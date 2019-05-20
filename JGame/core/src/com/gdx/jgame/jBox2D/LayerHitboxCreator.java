package com.gdx.jgame.jBox2D;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.managers.MapManager;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class LayerHitboxCreator {
	
	private MapObjects m_objectsInLayer;
	private MapLayer m_layer;
	private BodyType bodyType;
	
	@SuppressWarnings("unused")
	public LayerHitboxCreator(MapLayer layer, World world){
		if(layer == null) throw new IllegalArgumentException("No layer found");
		
		m_layer = layer;
		m_objectsInLayer = layer.getObjects();
		
		String tmpPropertyName;
		
		// check layer properties
		tmpPropertyName = (String) m_layer.getProperties().get(LayerConstants.bodyType);
		
		if(tmpPropertyName.equals(LayerConstants.BodyType.dynamicBody)) bodyType = BodyType.DynamicBody;
		else if(tmpPropertyName.equals(LayerConstants.BodyType.staticBody)) bodyType = BodyType.StaticBody;
		else if(tmpPropertyName.equals(LayerConstants.BodyType.kinematicBody)) bodyType = BodyType.KinematicBody;
		else throw new IllegalArgumentException("Wrong BodyType in layer properties: " + tmpPropertyName);
		
		// check shapes of the objects
		for(RectangleMapObject rectangleObject : m_objectsInLayer.getByType(RectangleMapObject.class)){
			createRectangle(world, rectangleObject);
		}
		
		for(PolygonMapObject polygonObject : m_objectsInLayer.getByType(PolygonMapObject.class)){
			//createPolygon(world, polygonObject);
		}
		
		for(PolylineMapObject polylineObject : m_objectsInLayer.getByType(PolylineMapObject.class)){
			//createPolyline(world, polylineObject);
		}
		
		for(EllipseMapObject ellipseObject : m_objectsInLayer.getByType(EllipseMapObject.class)){
			//createEllipse(world, ellipseObject);		
		}
		
		// for some reason it interprets circle as ellipse
		for(CircleMapObject circleObject : m_objectsInLayer.getByType(CircleMapObject.class)){
			@SuppressWarnings("unused")
			Circle circle = circleObject.getCircle();
			
			/*CircleShape circleShape;
			BodyDef bodyDef;
			FixtureDef fixtureDef = new FixtureDef();
			
			bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.position.set(circle.x / MapManager.PIXELS_PER_METER, circle.y / MapManager.PIXELS_PER_METER);	
			
			circleShape = new CircleShape();
			circleShape.setRadius(circle.radius / MapManager.PIXELS_PER_METER);
			
			fixtureDef.shape = circleShape;
			fixtureDef.density = 0.5f; 
			fixtureDef.friction = 0.4f;
			fixtureDef.restitution = 0.6f;
			fixtureDef.filter.categoryBits = 1;
			
			world.createBody(bodyDef).createFixture(fixtureDef);
			circleShape.dispose();	*/	
		}
	}

	@SuppressWarnings("unused")
	private void createPolyline(World world, PolylineMapObject polylineObject) {
		ChainShape chainShape = createPolyline(polylineObject);
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		
		bodyDef.type = BodyType.DynamicBody;
		fixtureDef.shape = chainShape;
		
		world.createBody(bodyDef).createFixture(fixtureDef);
		chainShape.dispose();
	}

	// TODO
	// at now it creates only circle
	@SuppressWarnings("unused")
	private void createEllipse(World world, EllipseMapObject ellipseObject) {
		Ellipse ellipse = ellipseObject.getEllipse();
		
		CircleShape circleShape;
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();

		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set((ellipse.x + ellipse.width/2)/ MapManager.PIXELS_PER_METER, 
				(ellipse.y + ellipse.height/2) / MapManager.PIXELS_PER_METER);	
		
		circleShape = new CircleShape();
		circleShape.setRadius((ellipse.height / 2) / MapManager.PIXELS_PER_METER);
		
		fixtureDef.shape = circleShape;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		fixtureDef.filter.categoryBits = 1;
		
		world.createBody(bodyDef).createFixture(fixtureDef);
		circleShape.dispose();
	}

	@SuppressWarnings("unused")
	private void createPolygon(World world, PolygonMapObject polygonObject) {
		Polygon polygon = polygonObject.getPolygon();
		PolygonShape shape = new PolygonShape();
		float[] vertices = polygon.getVertices();
		Vector2[] points = new Vector2[vertices.length/2];
		
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		bodyDef.type = bodyType;
		
		for (int i = 0, j = 0; i < vertices.length; i+=2, ++j) {
			points[j] = new Vector2(vertices[i] / MapManager.PIXELS_PER_METER, 
					vertices[i+1] / MapManager.PIXELS_PER_METER);
		}
		
		shape.set(points);
		bodyDef.position.set(polygon.getX() / MapManager.PIXELS_PER_METER, 
				polygon.getY() / MapManager.PIXELS_PER_METER);
		fixtureDef.shape = shape;
		
		MapProperties objectProperties = polygonObject.getProperties();
		/*if(objectProperties.containsKey(LayerConstants.CustomProperties.) &&
				objectProperties.get(LayerConstants.CustomProperties.).equals(true)) {
				
		}*/
		
		world.createBody(bodyDef).createFixture(fixtureDef);
		shape.dispose();
	}

	private void createRectangle(World world, RectangleMapObject rectangleObject) {
		Rectangle rectangle = rectangleObject.getRectangle();
		PolygonShape shape = new PolygonShape();
		Vector2[] points = new Vector2[4];
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		bodyDef.position.set(rectangle.x / MapManager.PIXELS_PER_METER, 
				rectangle.y / MapManager.PIXELS_PER_METER);
		bodyDef.type = bodyType;
		bodyDef.fixedRotation = false;
		
		points[0] = new Vector2(0, 0);
		points[1] = new Vector2(rectangle.getWidth() / MapManager.PIXELS_PER_METER, 0);
		points[2] = new Vector2(rectangle.getWidth() / MapManager.PIXELS_PER_METER, 
				rectangle.getHeight() / MapManager.PIXELS_PER_METER);
		points[3] = new Vector2(0, rectangle.getHeight() / MapManager.PIXELS_PER_METER);
		
		
		shape.set(points);
		fixtureDef.shape = shape;
		fixtureDef.density = 0.1f;
		fixtureDef.friction = 0.2f;
		
		// part with if statements (check object properties)
		MapProperties objectProperties = rectangleObject.getProperties();
		if(objectProperties.containsKey(LayerConstants.CustomProperties.strongBlocked) &&
				objectProperties.get(LayerConstants.CustomProperties.strongBlocked).equals(true)) {
			
			// everything should not be able to get through this object
			// default
			// fixtureDef.filter.maskBits = -1;
			
			fixtureDef.filter.categoryBits = -1;
			fixtureDef.isSensor = false;
			bodyDef.type = BodyType.StaticBody;
		}
		else if(objectProperties.containsKey(LayerConstants.CustomProperties.blocked) &&
				objectProperties.get(LayerConstants.CustomProperties.blocked).equals(true)) {
			
			fixtureDef.filter.maskBits = 1;
		}
		else {
			bodyDef.type = BodyType.DynamicBody;
			fixtureDef.density = 1f;
			fixtureDef.friction = 0.2f;
			fixtureDef.restitution = 0.5f;
		}
		Body body = world.createBody(bodyDef);
		@SuppressWarnings("unused")
		Fixture fixture = body.createFixture(fixtureDef);
		//world.createBody(bodyDef).createFixture(fixtureDef);
		shape.dispose();
	}
	
	private ChainShape createPolyline(PolylineMapObject polyline) {
		float vertices[] = polyline.getPolyline().getTransformedVertices();
	    Vector2 worldVertices[] = new Vector2[vertices.length / 2];

	    for(int i = 0, j = 0; j < worldVertices.length; i+=2, ++j) {
	        worldVertices[j] = new Vector2(vertices[i] / MapManager.PIXELS_PER_METER, 
	        		vertices[i+1] / MapManager.PIXELS_PER_METER);
	    }
	    ChainShape chainShape = new ChainShape();
	    chainShape.createChain(worldVertices);
	    return chainShape;
	}
}
