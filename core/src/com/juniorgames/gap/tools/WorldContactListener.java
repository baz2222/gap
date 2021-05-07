package com.juniorgames.gap.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.sprites.Door;
import com.juniorgames.gap.sprites.Player;
import com.juniorgames.gap.sprites.Spikes;

public class WorldContactListener implements ContactListener {
    private Fixture fixA;
    private Fixture fixB;
    private GapGame game;

    public WorldContactListener(GapGame game) {
        this.game = game;
    }

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

        //==================HANDLE PLAYER COLLISION WITH SPIKES=================================================
        if (fixA.getUserData() != null && fixB.getUserData() != null) {
            if (fixA.getUserData().getClass() == Player.class && fixB.getUserData().getClass() == Spikes.class) {
            ((Player) fixA.getUserData()).setFilterBit(game.DESTROYED_BIT);
            }
            if (fixB.getUserData().getClass() == Player.class && fixA.getUserData().getClass() == Spikes.class) {
                ((Player) fixB.getUserData()).setFilterBit(game.DESTROYED_BIT);
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
