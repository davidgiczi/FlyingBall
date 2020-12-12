package com.david.giczi.flyingball.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.david.giczi.flyingball.config.Config;
import com.david.giczi.flyingball.domain.Component;
import com.david.giczi.flyingball.service.FlyingBallService;

@Controller
public class FlyingBallGameController {

	private FlyingBallService service;

	@Autowired
	public void setService(FlyingBallService service) {
		this.service = service;
	}

	@RequestMapping("/FlyingBall/ajaxRequest")
	public void ajaxResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String userRequest = request.getParameter("usereq");
		
		switch (userRequest) {

		case "flyBall":
			flyBall(request, response);
			break;
		case "stepNorth":
			stepNorth(request, response);
			break;
		case "stepSouth":
			stepSouth(request, response);
			break;

		default:
		}

	}

	@RequestMapping("/FlyingBall")
	public String initGame(Model model, HttpServletRequest request) {

		request.getSession().invalidate();
		service.configGame();
		model.addAttribute("board_rows", Config.BOARD_ROWS);
		model.addAttribute("board_cols", Config.BOARD_COLS);
		List<Component> gameBoard = service.createBoard();
		model.addAttribute("board", gameBoard);
		request.getSession().setAttribute("player", service.getPlayer(gameBoard));
		request.getSession().setAttribute("ball", service.getBall(gameBoard));
		
		return "gameboard";
	}

	private void stepNorth(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Component player = (Component) request.getSession().getAttribute("player");
		player = service.stepNorth(player);
		response.getWriter().append("player_" + player.getLogicBoardIndex());
		request.getSession().setAttribute("player", player);
	}

	private void stepSouth(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Component player = (Component) request.getSession().getAttribute("player");
		player = service.stepSouth(player);
		response.getWriter().append("player_" + player.getLogicBoardIndex());
		request.getSession().setAttribute("player", player);
	}

	private void flyBall(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Component ball = (Component) request.getSession().getAttribute("ball");
		Component player = (Component) request.getSession().getAttribute("player");
		
		
		if(service.isKickedTheBall(player, ball)) {
			
			ball = service.kickTheBall(ball);
			
		}
		else if(service.isGoal(ball)) {
			
			response.getWriter().append("Ez becsúszott! Szeretnél új játékot játszani?");
			return;
		}
		else if(service.canBallFlyNorth(ball)) {
		
			ball = service.bounceBackFromNorthWestCorner(ball);
			ball = service.bounceBackFromNorthBorder(ball);
			ball = service.bounceBackFromNorthEastCorner(ball);
			ball = service.flyNorth(ball);
			
		}
		else if(service.canBallFlyNorthEast(ball)) {
			
			ball = service.bounceBackFromNorthBorder(ball);
			ball = service.bounceBackFromNorthEastCorner(ball);
			ball = service.bounceBackFromEastBorder(ball);
			ball = service.flyNorthEast(ball);
			
		}
		else if(service.canBallFlyEast(ball)) {
			
			ball = service.bounceBackFromNorthEastCorner(ball);
			ball = service.bounceBackFromEastBorder(ball);
			ball = service.bounceBackFromSouthEastCorner(ball);
			ball = service.flyEast(ball);
		}
		else if(service.canBallFlySouthEast(ball)) {
		
			ball = service.bounceBackFromEastBorder(ball);
			ball = service.bounceBackFromSouthEastCorner(ball);
			ball = service.bounceBackFromSouthBorder(ball);
			ball = service.flySouthEast(ball);
		
		}
		else if(service.canBallFlySouth(ball)) {
			
			ball = service.bounceBackFromSouthEastCorner(ball);
			ball = service.bounceBackFromSouthBorder(ball);
			ball = service.bounceBackFromSouthWestCorner(ball);
			ball = service.flySouth(ball);

		}
		else if(service.canBallFlySouthWest(ball)) {
			
			ball = service.bounceBackFromSouthBorder(ball);
			ball = service.flySouthWest(ball);

		}
		else if(service.canBallFlyWest(ball)) {
			
			ball = service.flyWest(ball);
			
		}
		else if(service.canBallFlyNorthWest(ball)) {
			
			ball = service.bounceBackFromNorthBorder(ball);
			ball = service.flyNorthWest(ball);
			
		}
		
		
		response.getWriter().append("ball_" + ball.getLogicBoardIndex());
		request.getSession().setAttribute("ball", ball);


	}

}
