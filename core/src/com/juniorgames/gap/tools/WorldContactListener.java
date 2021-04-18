package com.juniorgames.gap.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.juniorgames.gap.sprites.InteractiveTileObject;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "playerSensor" || fixB.getUserData() == "playerSensor"){
            Fixture playerFixture = fixA.getUserData() == "playerSensor" ? fixA : fixB;
            Fixture object = playerFixture == fixA ? fixB : fixA;
            //returns true if colided object extends InteractiveTileObject class
            if (object.getUserData()!=null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject) object.getUserData()).onHit();
            }
        }


    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
