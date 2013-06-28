package com.badrabbit.psw.objects;

import com.badrabbit.psw.managers.Game;

public class SplashView extends Game {
	public void init() {
		addChild(new StrobingTank(100, 100));
		addChild(new StrobingTank(-100, 100));
		addChild(new StrobingTank(100, -100));
		addChild(new StrobingTank(-100, -100));
		addChild(new Button(0, 0));
	}
}
