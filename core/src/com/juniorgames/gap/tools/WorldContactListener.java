package com.juniorgames.gap.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.juniorgames.gap.GapGame;
import com.juniorgames.gap.sprites.*;

public class WorldContactListener implements ContactListener {
    private Fixture fixA;
    private Fixture fixB;
    private short firstBit;
    private short secondBit;
    private GapGame game;

    public WorldContactListener(GapGame game) {
        this.game = game;
    }

    @Override
    public void beginContact(Contact contact) {
        fixA = contact.getFixtureA();
        fixB = contact.getFixtureB();

        //==================HANDLE PLAYER COLLISION WITH BUFF=================================================
        if (fixA.getUserData() != null && fixB.getUserData() != null) {
            if (fixA.getUserData().getClass() == Player.class && fixB.getUserData().getClass() == Buff.class) {
                //((Player) fixA.getUserData()).buff = ((Buff) fixB.getUserData()).type;
                ((Buff) fixB.getUserData()).onHit();
            }
            if (fixB.getUserData().getClass() == Player.class && fixA.getUserData().getClass() == Buff.class) {
                //((Player) fixB.getUserData()).buff = ((Buff) fixA.getUserData()).type;
                ((Buff) fixA.getUserData()).onHit();
            }
        }//if=================================================================================================

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
                game.playSound("die", false);
                ((Player) fixA.getUserData()).setFilterBit(game.DESTROYED_BIT);
            }
            if (fixB.getUserData().getClass() == Player.class && fixA.getUserData().getClass() == Spikes.class) {
                game.playSound("die", false);
                ((Player) fixB.getUserData()).setFilterBit(game.DESTROYED_BIT);
            }
        }//if=================================================================================================

        //==================HANDLE PLAYER COLLISION WITH CRUMBLES=================================================
        if (fixA.getUserData() != null && fixB.getUserData() != null) {
            if (fixA.getUserData().getClass() == Player.class && fixB.getUserData().getClass() == Crumbles.class) {
                if(((Player) fixA.getUserData()).buff == Buff.BuffType.BOMB) {
                    game.playSound("break", false);
                    ((Crumbles) fixB.getUserData()).onHit();
                }
            }
            if (fixB.getUserData().getClass() == Player.class && fixA.getUserData().getClass() == Crumbles.class) {
                if (((Player) fixB.getUserData()).buff == Buff.BuffType.BOMB) {
                    game.playSound("break", false);
                    ((Crumbles) fixA.getUserData()).onHit();
                }
            }
        }//if=================================================================================================

        //==================HANDLE PLAYER COLLISION WITH ENEMIES==============================================
        if (fixA.getUserData() != null && fixB.getUserData() != null) {
            if (fixA.getUserData().getClass() == Player.class && fixB.getUserData().getClass() == Enemy.class) {
                game.playSound("die", false);
                if (((Player) fixA.getUserData()).currentState == GapGame.State.FALLING || ((Player) fixA.getUserData()).buff == Buff.BuffType.SHIELD){
                    ((Enemy) fixB.getUserData()).setFilterBit(game.DESTROYED_BIT);
                }else{
                    ((Player) fixA.getUserData()).setFilterBit(game.DESTROYED_BIT);
                }
            }
            if (fixB.getUserData().getClass() == Player.class && fixA.getUserData().getClass() == Enemy.class) {
                game.playSound("die", false);
                if (((Player) fixB.getUserData()).currentState == GapGame.State.FALLING || ((Player) fixB.getUserData()).buff == Buff.BuffType.SHIELD){
                    ((Enemy) fixA.getUserData()).setFilterBit(game.DESTROYED_BIT);
                }else{
                    ((Player) fixB.getUserData()).setFilterBit(game.DESTROYED_BIT);
                }
            }
        }//if=================================================================================================

        //==================HANDLE PLAYER COLLISION WITH SPIKE_ENEMIES========================================
        if (fixA.getUserData() != null && fixB.getUserData() != null) {
            if (fixA.getUserData().getClass() == Player.class && fixB.getUserData().getClass() == SpikeEnemy.class) {
                game.playSound("die", false);
                if (((Player) fixA.getUserData()).buff == Buff.BuffType.SHIELD){
                    ((SpikeEnemy) fixB.getUserData()).setFilterBit(game.DESTROYED_BIT);
                }else{
                    ((Player) fixA.getUserData()).setFilterBit(game.DESTROYED_BIT);
                }
            }
            if (fixB.getUserData().getClass() == Player.class && fixA.getUserData().getClass() == SpikeEnemy.class) {
                game.playSound("die", false);
                if (((Player) fixB.getUserData()).buff == Buff.BuffType.SHIELD){
                    ((SpikeEnemy) fixA.getUserData()).setFilterBit(game.DESTROYED_BIT);
                }else{
                    ((Player) fixB.getUserData()).setFilterBit(game.DESTROYED_BIT);
                }
            }
        }//if=================================================================================================

        //==================HANDLE PLAYER COLLISION WITH BUMPS=================================================
        if (fixA.getUserData() != null && fixB.getUserData() != null) {
            if (fixA.getUserData().getClass() == Player.class && fixB.getUserData().getClass() == Bump.class) {
                ((Player) fixA.getUserData()).jumpMultiplier = 2;
                ((Player) fixA.getUserData()).jump();
                ((Player) fixA.getUserData()).jumpMultiplier = 1;
                ((Bump) fixB.getUserData()).isHitted = true;
            }
            if (fixB.getUserData().getClass() == Player.class && fixA.getUserData().getClass() == Bump.class) {
                ((Player) fixB.getUserData()).jumpMultiplier = 2;
                ((Player) fixB.getUserData()).jump();
                ((Player) fixB.getUserData()).jumpMultiplier = 1;
                ((Bump) fixA.getUserData()).isHitted = true;
            }
        }//if=================================================================================================

        //==================HANDLE PLAYER COLLISION WITH SWITCH=================================================
        if (fixA.getUserData() != null && fixB.getUserData() != null) {
            if (fixA.getUserData().getClass() == Player.class && fixB.getUserData().getClass() == Switch.class) {
                ((Switch) fixB.getUserData()).isChecked = true;
                ((Switch) fixB.getUserData()).target.isVisible = true;
            }
            if (fixB.getUserData().getClass() == Player.class && fixA.getUserData().getClass() == Switch.class) {
                ((Switch) fixA.getUserData()).isChecked = true;
                ((Switch) fixA.getUserData()).target.isVisible = true;
            }
        }//if=================================================================================================

        //==================HANDLE ENEMY COLLISION WITH CHANGE_DIRECTION_BOX=================================================
        if (fixA.getUserData() != null && fixB.getUserData() != null) {
            if (fixA.getUserData().getClass() == Enemy.class && fixB.getUserData().getClass() == ChangeDirectionBox.class) {
                ((Enemy) fixA.getUserData()).runRight = !((Enemy) fixA.getUserData()).runRight;
            }
            if (fixB.getUserData().getClass() == Enemy.class && fixA.getUserData().getClass() == ChangeDirectionBox.class) {
                ((Enemy) fixB.getUserData()).runRight = !((Enemy) fixB.getUserData()).runRight;
            }
        }//if=================================================================================================

        //==================HANDLE SPIKE_ENEMY COLLISION WITH CHANGE_DIRECTION_BOX=================================================
        if (fixA.getUserData() != null && fixB.getUserData() != null) {
            if (fixA.getUserData().getClass() == SpikeEnemy.class && fixB.getUserData().getClass() == ChangeDirectionBox.class) {
                ((SpikeEnemy) fixA.getUserData()).runRight = !((SpikeEnemy) fixA.getUserData()).runRight;
            }
            if (fixB.getUserData().getClass() == SpikeEnemy.class && fixA.getUserData().getClass() == ChangeDirectionBox.class) {
                ((SpikeEnemy) fixB.getUserData()).runRight = !((SpikeEnemy) fixB.getUserData()).runRight;
            }
        }//if=================================================================================================

    }//begin contact

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        firstBit = contact.getFixtureA().getFilterData().categoryBits;
        secondBit = contact.getFixtureB().getFilterData().categoryBits;

        //prevent collision for change direction and player
        if ((firstBit | secondBit) == (game.PLAYER_BIT | game.CHANGE_DIRECTION_BOX_BIT)) {
            contact.setEnabled(false);
        }//if

        //prevent collision between enemies
        if ((firstBit | secondBit) == (game.ENEMY_BIT | game.ENEMY_BIT)) {
            contact.setEnabled(false);
        }//if

        if ((firstBit | secondBit) == (game.SPIKE_ENEMY_BIT | game.ENEMY_BIT)) {
            contact.setEnabled(false);
        }//if

        //prevent collision between enemies and spikes
        if ((firstBit | secondBit) == (game.SPIKES_BIT | game.ENEMY_BIT)) {
            contact.setEnabled(false);
        }//if

        //prevent collision between spike enemies and spikes
        if ((firstBit | secondBit) == (game.SPIKES_BIT | game.SPIKE_ENEMY_BIT)) {
            contact.setEnabled(false);
        }//if
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
    }
}
