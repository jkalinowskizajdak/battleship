package com.battleship.control;

import com.battleship.boundary.dto.GameStatusDTO;
import com.battleship.boundary.dto.MoveDTO;
import com.battleship.boundary.dto.ShotStatusDTO;
import com.battleship.entity.*;
import org.apache.log4j.Logger;

import java.util.Optional;

public class GameService {

    private static final Logger logger = Logger.getLogger(GameService.class);
    private final GamesRepository gamesRepository = GamesInMemoryRepository.getInstance();
    private final AuthService authService = AuthService.getInstance();

    public Game generateGame() {
        Player firstPlayer = Game.createFirstPlayer(new DefaultPlayerStrategy());
        Game game = Game.builder()
                .firstPlayer(firstPlayer)
                .build();
        gamesRepository.addGame(game);
        logger.info("New game " + game.getId() + " was created");
        return game;
    }

    public String getToken(String playerId) {
        String token = authService.generateTokenForPlayer(playerId);
        logger.info("Token for player " + playerId + " was created");
        return token;
    }

    public GameStatusDTO join(String gameId) {
        Game game = getGame(gameId);
        Player secondPlayer = Game.createSecondPlayer(new DefaultPlayerStrategy());
        game.addSecondPlayer(secondPlayer);
        logger.info("Player " + secondPlayer.getId() + " joined to game " + gameId);
        return getPlayerStatusGame(gameId, secondPlayer.getId());
    }

    public GameStatusDTO getPlayerStatusGame(String gameId, String playerId) {
        Game game = getGame(gameId);
        Player firstPlayer = game.getFirstPlayer();
        Optional<Player> secondPlayer = game.getSecondPlayer();
        if (firstPlayer.getId().equals(playerId)) {
            int opponentScore = Game.BEGIN_SCORE;
            if (secondPlayer.isPresent()) {
                opponentScore = secondPlayer.get().getPoints();
            }
            return GameUtil.getGameStatusDTO(firstPlayer, opponentScore);
        } else if (secondPlayer.isPresent() && secondPlayer.get().getId().equals(playerId)) {
            return GameUtil.getGameStatusDTO(secondPlayer.get(), firstPlayer.getPoints());
        } else {
            logger.error("Player " + playerId + " is not in game " + gameId);
            throw new GameException(Game.INCORRECT_PLAYER_ID_MESSAGE, GameException.INCORRECT_PLAYER_CODE);
        }
    }

    public ShotStatusDTO shot(String gameId, String playerId, MoveDTO moveDTO) {
        Game game = getGame(gameId);
        validateGameMove(game, playerId, moveDTO);
        if (!game.isStarted()) {
            game.startGame();
        }
        int x = GameUtil.getX(moveDTO);
        int y = GameUtil.getY(moveDTO);
        ShotResult shotResult = game.turn(playerId, x, y);
        logger.info("Game " + gameId + ", player " + playerId +
                " shot on point " + moveDTO.getPosition() + " with status " + shotResult.getShotStatus());
        return GameUtil.getShotStatusDTO(shotResult);
    }

    public Game getGame(String gameId) {
        return gamesRepository.getGame(gameId)
                .orElseThrow(() -> new GameException("Game does not exists!", GameException.INCORRECT_GAME_CODE));
    }

    private void validateGameMove(Game game, String playerId, MoveDTO moveDTO) {
        Player firstPlayer = game.getFirstPlayer();
        Optional<Player> secondPlayer = game.getSecondPlayer();
        if (!firstPlayer.getId().equals(playerId) && (!secondPlayer.isPresent()
                || !secondPlayer.get().getId().equals(playerId))) {
            logger.error("Player " + playerId + " is not in game " + game.getId());
            throw new GameException(Game.INCORRECT_PLAYER_ID_MESSAGE, GameException.INCORRECT_PLAYER_CODE);
        }
        if (!GameUtil.isMoveValid(moveDTO)) {
            logger.error(moveDTO.getPosition() + " is out of board");
            throw new GameException(Game.POINT_VALUE_OUT_OF_BOARD_EXCEPTION_MESSAGE, GameException.OUT_OF_BOARD_CODE);
        }
    }

}
