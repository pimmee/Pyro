package com.pimme.game.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pimme.game.entities.InteractiveObject;

public class CollisionListener implements ContactListener
{
    @Override public void beginContact(final Contact contact) {
	Fixture fixA = contact.getFixtureA();
	Fixture fixB = contact.getFixtureB();
	if (fixA.getUserData() == "pyret" || fixB.getUserData() == "pyret") {
	    Fixture pyret = fixA.getUserData() == "pyret" ? fixA : fixB; //Is fixA pyret? else fixB
	    Fixture object = pyret.equals(fixA) ? fixB : fixA;

	    if (object.getUserData() instanceof InteractiveObject) {
	        ((InteractiveObject) object.getUserData()).onCollision();
	    }
	}
    }

    @Override public void endContact(final Contact contact) {

    }

    @Override public void preSolve(final Contact contact, final Manifold oldManifold) {

    }

    @Override public void postSolve(final Contact contact, final ContactImpulse impulse) {

    }
}
