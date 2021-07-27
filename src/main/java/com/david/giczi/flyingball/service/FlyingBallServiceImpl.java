package com.david.giczi.flyingball.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.david.giczi.flyingball.config.Config;
import com.david.giczi.flyingball.config.GameParam;
import com.david.giczi.flyingball.domain.Component;
import com.david.giczi.flyingball.utils.Direction;

@Service
public class FlyingBallServiceImpl implements FlyingBallService{
	
	
	private GameParam param;
	
	@Autowired
	public void setParam(GameParam param) {
		this.param = param;
	}

	@Override
	public void configGame() {
		
		Config.BOARD_ROWS = param.getBoard_rows();
		Config.BOARD_COLS = param.getBoard_cols();
		
	}

	@Override
	public List<Component> createBoard() {
		
		List<Component> gameBoard = new ArrayList<>();
		
		for(int x = 0; x < Config.BOARD_ROWS; x++) {
			for(int y = 0; y < Config.BOARD_COLS; y++) {
				
				gameBoard.add(new Component(x, y));
			}
		}
		
		return addPlayersAndBallToGameboard(gameBoard);
	}

	private List<Component> addPlayersAndBallToGameboard(List<Component> gameBoard){
		
		gameBoard
		.stream()
		.filter(c -> c.getViewBoard_x() ==  0  && c.getViewBoard_y() == 0)
		.forEach(c -> c.setBackground("ball"));
		gameBoard
		.stream()
		.filter(c -> c.getViewBoard_x() == Config.BOARD_ROWS / 2 && c.getViewBoard_y() == 0)
		.forEach(c -> c.setBackground("player"));
		gameBoard
		.stream()
		.filter(c -> c.getViewBoard_x() == Config.BOARD_ROWS / 2 && c.getViewBoard_y() == Config.BOARD_ROWS - 2)
		.forEach(c -> c.setBackground("home_player"));
		
		
		return gameBoard;
	}

	
	@Override
	public Component getPlayer(List<Component> gameBoard) {
		
		Component player = new Component(0);
		gameBoard
		.stream()
		.filter(c -> "player".equals(c.getBackground()))
		.forEach(c -> player.setLogicBoardIndex(c.getLogicBoardIndex()));
		
		return player;
	}
	
	@Override
	public Component getBall(List<Component> gameBoard) {
		
		Component ball = new Component(0);
		gameBoard
		.stream()
		.filter(c -> "ball".equals(c.getBackground()))
		.forEach(c -> ball.setLogicBoardIndex(c.getLogicBoardIndex()));
		ball.setActualDirection(Direction.SOUTH_EAST);
	
		return ball;
	}

	@Override
	public Component stepNorth(Component player) {
		
		
		if(player.getViewBoard_x() > 0) {
			player.setViewBoardCoords(player.getViewBoard_x() - 1, player.getViewBoard_y());
		}
		
		return player;
	}

	@Override
	public Component stepSouth(Component player) {
		
		if(player.getViewBoard_x() < Config.BOARD_ROWS - 1) {
			player.setViewBoardCoords(player.getViewBoard_x() + 1, player.getViewBoard_y());
		}
		
		return player;
	}

	@Override
	public boolean canBallFlyNorth(Component ball) {
		
		return ball.getActualDirection() == Direction.NORTH;
	}

	@Override
	public boolean canBallFlyNorthEast(Component ball) {
		
		return ball.getActualDirection() == Direction.NORTH_EAST;
	}

	@Override
	public boolean canBallFlyEast(Component ball) {
		
		return ball.getActualDirection() == Direction.EAST;
	}

	@Override
	public boolean canBallFlySouthEast(Component ball) {
		
		return ball.getActualDirection() == Direction.SOUTH_EAST;
	}

	@Override
	public boolean canBallFlySouth(Component ball) {
		
		return ball.getActualDirection() == Direction.SOUTH;
	}

	@Override
	public boolean canBallFlySouthWest(Component ball) {
		
		return ball.getActualDirection() == Direction.SOUTH_WEST; 
	}

	@Override
	public boolean canBallFlyWest(Component ball) {
		
		return ball.getActualDirection() == Direction.WEST;
	}

	@Override
	public boolean canBallFlyNorthWest(Component ball) {
		
		return ball.getActualDirection() == Direction.NORTH_WEST;
	}

	

	@Override
	public Component bounceBackFromNorthBorder(Component ball) {
		
		if(ball.getViewBoard_x() == 0 && ball.getViewBoard_y() != 0 && ball.getViewBoard_y() != Config.BOARD_COLS - 1) {
			
			if(ball.getActualDirection() == Direction.NORTH_WEST) {
				
				ball.setActualDirection(Direction.SOUTH_WEST);
				ball = flySouthWest(ball);
				
			}
			else if(ball.getActualDirection() == Direction.NORTH_EAST) {
				
				ball.setActualDirection(Direction.SOUTH_EAST);
				ball = flySouthEast(ball);
			}
			
		}
	
		return ball;
	}


	@Override
	public Component bounceBackFromEastBorder(Component ball) {
		
		
		if(ball.getViewBoard_x() != 0 && ball.getViewBoard_x() != Config.BOARD_ROWS - 1 && ball.getViewBoard_y() == Config.BOARD_COLS - 1) {
			
			int randomDirect = (int) (Math.random() * 100);
			
			if(randomDirect >= 0 && randomDirect < 20) {
				
				ball.setActualDirection(Direction.NORTH);
				ball = flyNorth(ball);
				
			}
			else if(randomDirect >= 20 && randomDirect < 40) {
				
				ball.setActualDirection(Direction.NORTH_WEST);
				ball = flyNorthWest(ball);
				
			}
			else if(randomDirect >= 40 && randomDirect < 60) {
				
				ball.setActualDirection(Direction.WEST);
				ball = flyWest(ball);
				
			}
			else if(randomDirect >= 60 && randomDirect < 80) {
				
				ball.setActualDirection(Direction.SOUTH_WEST);
				ball = flySouthWest(ball);
			}
			else {
				
				ball.setActualDirection(Direction.SOUTH);
				ball = flySouth(ball);
				
			}
			
		}
			
		return ball;
	}


	@Override
	public Component bounceBackFromSouthBorder(Component ball) {
		
		if(ball.getViewBoard_x() == Config.BOARD_ROWS - 1 && ball.getViewBoard_y() != 0 && ball.getViewBoard_y() != Config.BOARD_COLS - 1) {
			
			if(ball.getActualDirection() == Direction.SOUTH_WEST) {
				
				ball.setActualDirection(Direction.NORTH_WEST);
				ball = flyNorthWest(ball);
				
			}
			else if(ball.getActualDirection() == Direction.SOUTH_EAST) {
				
				ball.setActualDirection(Direction.NORTH_EAST);
				ball = flyNorthEast(ball);
			}
	
		}
			
		return ball;
		}
	
	@Override
	public Component kickTheBall(Component ball) {
		
		int randomDirect = (int) (Math.random() * 100);
		
		if(ball.getViewBoard_x() == Config.BOARD_ROWS - 1 && ball.getViewBoard_y() == 0) {
			
		ball = bounceBackFromSouthWestCorner(ball);
		
		}
		else if(ball.getViewBoard_x() == 0 && ball.getViewBoard_y() == 0){
			
		ball = bounceBackFromNorthWestCorner(ball);
	
		}
		else if(randomDirect >= 0 && randomDirect < 33) {
			
			ball.setActualDirection(Direction.NORTH_EAST);
			ball = flyNorthEast(ball);
			
		}
		else if(randomDirect >= 33 && randomDirect < 67) {
			
			ball.setActualDirection(Direction.EAST);
			ball = flyEast(ball);
		}
		else {
			
			ball.setActualDirection(Direction.SOUTH_EAST);
			ball = flySouthEast(ball);
			
		}
		
		return ball;
	}
	
	
	@Override
	public Component bounceBackFromNorthEastCorner(Component ball) {
		
		
		if(ball.getViewBoard_x() == 0 && ball.getViewBoard_y() == Config.BOARD_COLS - 1) {
			
			int randomDirect = (int) (Math.random() * 100);
			
			if(randomDirect >= 0 && randomDirect < 50) {
				
				ball.setActualDirection(Direction.WEST);
				ball = flyWest(ball);
			}
			else if(randomDirect >= 50) {
				
				ball.setActualDirection(Direction.SOUTH_WEST);
				ball = flySouthWest(ball);	
		}
	}
		
		return ball;
	}

	@Override
	public Component bounceBackFromNorthWestCorner(Component ball) {
		
		if(ball.getViewBoard_x() == 0 && ball.getViewBoard_y() == 0) {
			
			int randomDirect = (int) (Math.random() * 100);
			
			if(randomDirect >= 0 && randomDirect < 33) {
				
				ball.setActualDirection(Direction.EAST);
				ball = flyEast(ball);
			}
			else if(randomDirect >= 33 && randomDirect < 67){
				
				ball.setActualDirection(Direction.SOUTH_EAST);
				ball = flySouthEast(ball);
			}
			else {
				
				ball.setActualDirection(Direction.SOUTH);
				ball = flySouth(ball);
			}			
		}
		
		
		
		return ball;
	}

	@Override
	public Component bounceBackFromSouthEastCorner(Component ball) {
		
		if(ball.getViewBoard_x() == Config.BOARD_ROWS - 1 && ball.getViewBoard_y() == Config.BOARD_COLS - 1) {
			
			int randomDirect = (int) (Math.random() * 100);
			
			if(randomDirect >= 0 && randomDirect < 50) {
				
				ball.setActualDirection(Direction.WEST);
				ball = flyWest(ball);
			}
			else if(randomDirect >= 50) {
				
				ball.setActualDirection(Direction.NORTH_WEST);
				ball = flyNorthWest(ball);
			}
			
			
		}
				
		return ball;
	}

	@Override
	public Component bounceBackFromSouthWestCorner(Component ball) {		
		
		
		if(ball.getViewBoard_x() == Config.BOARD_ROWS - 1 && ball.getViewBoard_y() == 0) {
		
		int randomDirect = (int) (Math.random() * 100);
		
		if(randomDirect >= 0 && randomDirect < 33) {
			
			ball.setActualDirection(Direction.EAST);
			ball = flyEast(ball);
		}
		else if(randomDirect >= 33 && randomDirect < 67){
			
			ball.setActualDirection(Direction.NORTH_EAST);
			ball = flyNorthEast(ball);
		}
		else {
			
			ball.setActualDirection(Direction.NORTH);
			ball = flyNorth(ball);
		}
	}	
			
		return ball;
	}

	@Override
	public Component flyNorth(Component ball) {
		
		if(ball.getActualDirection() == Direction.NORTH) {
			ball.setViewBoardCoords(ball.getViewBoard_x() - 1, ball.getViewBoard_y());
		}
		
		return ball;
	}

	@Override
	public Component flyNorthEast(Component ball) {
		
		if(ball.getActualDirection() == Direction.NORTH_EAST) {
			ball.setViewBoardCoords(ball.getViewBoard_x() - 1, ball.getViewBoard_y() + 1);
		}
		
		return ball;
	}

	@Override
	public Component flyEast(Component ball) {
		
		if(ball.getActualDirection() == Direction.EAST) {
			ball.setViewBoardCoords(ball.getViewBoard_x(), ball.getViewBoard_y() + 1);
		}
		
		return ball;
	}

	@Override
	public Component flySouthEast(Component ball) {
		
		if(ball.getActualDirection() == Direction.SOUTH_EAST) {
			ball.setViewBoardCoords(ball.getViewBoard_x() + 1, ball.getViewBoard_y() + 1);
		}
		
		return ball;
	}

	@Override
	public Component flySouth(Component ball) {
		
		if(ball.getActualDirection() == Direction.SOUTH) {
			ball.setViewBoardCoords(ball.getViewBoard_x() + 1, ball.getViewBoard_y());
		}
		
		return ball;
	}

	@Override
	public Component flySouthWest(Component ball) {
		
		if(ball.getActualDirection() == Direction.SOUTH_WEST) {
			ball.setViewBoardCoords(ball.getViewBoard_x() + 1, ball.getViewBoard_y() - 1);
		}
		
		return ball;
	}

	@Override
	public Component flyWest(Component ball) {
		
		if(ball.getActualDirection() == Direction.WEST) {
			ball.setViewBoardCoords(ball.getViewBoard_x(), ball.getViewBoard_y() - 1);
		}
		
		return ball;
	}

	@Override
	public Component flyNorthWest(Component ball) {
		
		if(ball.getActualDirection() == Direction.NORTH_WEST) {
			ball.setViewBoardCoords(ball.getViewBoard_x() - 1, ball.getViewBoard_y() - 1);
		}
		
		return ball;
	}

	@Override
	public boolean isKickedTheBall(Component player, Component ball) {			
		
		return player.equals(ball);
	}

	
	@Override
	public boolean isGoal(Component ball) {
			
		Component alterBall = new Component(ball.getLogicBoardIndex());
		alterBall.setActualDirection(ball.getActualDirection());
		
		if(alterBall.getActualDirection() == Direction.NORTH_WEST && flyNorthWest(alterBall).getViewBoard_y() < 0) {
			return true;
			
		}
		else if(alterBall.getActualDirection() == Direction.WEST && flyWest(alterBall).getViewBoard_y() < 0) {
			return true;
		}
		else if(alterBall.getActualDirection() == Direction.SOUTH_WEST && flySouthWest(alterBall).getViewBoard_y() < 0) {
			return true;
		}	
			
			
		return false;
	}
	
	
	
}
