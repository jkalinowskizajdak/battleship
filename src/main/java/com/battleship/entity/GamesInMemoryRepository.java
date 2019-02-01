package com.battleship.entity;

import com.battleship.control.GamesRepository;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Optional;

public class GamesInMemoryRepository implements GamesRepository {

    private static GamesRepository instance;

    public static GamesRepository getInstance() {
        if (instance == null) {
            synchronized (GamesInMemoryRepository.class) {
                if (instance == null) {
                    instance = new GamesInMemoryRepository();
                }
            }
        }
        return instance;
    }

    private static final Cache<String, Game> gamesRepository = CacheBuilder
            .newBuilder()
            .build();

    private GamesInMemoryRepository() {

    }

    @Override
    public void addGame(Game game) {
        gamesRepository.put(game.getId(), game);
    }

    @Override
    public void removeGame(String gameId) {
        gamesRepository.invalidate(gameId);
    }

    @Override
    public Optional<Game> getGame(String gameId) {
        return Optional.ofNullable(gamesRepository.getIfPresent(gameId));
    }

    @Override
    public long getAllGameCount() {
        return gamesRepository.size();
    }

}
