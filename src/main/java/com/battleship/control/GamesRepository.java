package com.battleship.control;

import com.battleship.entity.Game;

import java.util.Optional;


public interface GamesRepository {

    void addGame(Game game);

    void removeGame(String gameId);

    Optional<Game> getGame(String gameId);

    long getAllGameCount();
}
