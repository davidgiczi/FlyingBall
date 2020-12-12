package com.david.giczi.flyingball.service;

import java.util.List;

import com.david.giczi.flyingball.domain.Component;

public interface FlyingBallService {
	
	void configGame();
	List<Component> createBoard();
	Component getPlayer(List<Component> gameBoard);
	Component getBall(List<Component> gameBoard);
	Component stepNorth(Component player);
	Component stepSouth(Component player);	
	Component bounceBackFromNorthBorder(Component ball);
	Component bounceBackFromEastBorder(Component ball);
	Component bounceBackFromSouthBorder(Component ball);
	Component bounceBackFromNorthEastCorner(Component ball);
	Component bounceBackFromNorthWestCorner(Component ball);
	Component bounceBackFromSouthEastCorner(Component ball);
	Component bounceBackFromSouthWestCorner(Component ball);
	Component flyNorth(Component ball);
	Component flyNorthEast(Component ball);
	Component flyEast(Component ball);
	Component flySouthEast(Component ball);
	Component flySouth(Component ball);
	Component flySouthWest(Component ball);
	Component flyWest(Component ball);
	Component flyNorthWest(Component ball);
	Component kickTheBall(Component ball);
	boolean canBallFlyNorth(Component ball);
	boolean canBallFlyNorthEast(Component ball);
	boolean canBallFlyEast(Component ball);
	boolean canBallFlySouthEast(Component ball);
	boolean canBallFlySouth(Component ball);
	boolean canBallFlySouthWest(Component ball);
	boolean canBallFlyWest(Component ball);
	boolean canBallFlyNorthWest(Component ball);
	boolean isKickedTheBall(Component player, Component ball);
	boolean isGoal(Component ball);
	
}
