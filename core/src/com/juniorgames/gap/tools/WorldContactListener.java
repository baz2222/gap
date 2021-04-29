package com.juniorgames.gap.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.juniorgames.gap.sprites.Door;
import com.juniorgames.gap.sprites.Player;

public class WorldContactListener implements ContactListener {
    private Fixture fixA;
    private Fixture fixB;

    @Override
    public void beginContact(Contact contact) {
        fixA = contact.getFixtureA();
        fixB = contact.getFixtureB();

        //==================HANDLE PLAYER COLLISION WITH DOOR=================================================
        if (fixA.getUserData() != null && fixB.getUserData() != null) {
            if (fixA.getUserData().getClass() == Player.class && fixB.getUserData().getClass() == Door.class) {
                ((Door) fixB.getUserData()).onHit();
            }
            if (fixB.getUserData().getClass() == Player.class && fixA.getUserData().getClass() == Door.class) {
                ((Door) fixA.getUserData()).onHit();
            }
        }//if=================================================================================================


    }//begin contact

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
