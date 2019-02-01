package com.battleship.boundary;

import com.battleship.boundary.dto.*;
import com.battleship.control.AuthService;
import com.battleship.control.GameException;
import com.battleship.control.GameService;
import com.battleship.control.GameUtil;
import com.battleship.entity.Game;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path(GameUtil.GAME_PATH)
public class BattleShipResource {

    private static final String GAME_ID_PARAM = "gameId";
    private static final String JOIN_PATH = "/join";
    private final GameService gameService = new GameService();

    @Context
    private UriInfo uriInfo;

    @Context
    private SecurityContext securityContext;

    @POST
    @Path("")
    public Response createGame() {
        Game game = gameService.generateGame();
        String playerToken = gameService.getToken(game.getFirstPlayer().getId());
        String path = uriInfo.getAbsolutePath().toString();
        InvitationDTO invitationDTO = new InvitationDTO(path + "/" + game.getId() + JOIN_PATH);
        return Response
                .ok(invitationDTO)
                .header(AuthService.SET_AUTH_HEADER, playerToken)
                .build();
    }

    @POST
    @Path("/{" + GAME_ID_PARAM + "}" + JOIN_PATH)
    public Response join(@PathParam(GAME_ID_PARAM) String gameId) {
        try {
            Game game = gameService.getGame(gameId);
            GameStatusDTO statusDTO = gameService.join(gameId);
            String playerToken = gameService.getToken(game.getSecondPlayer().get().getId());
            return Response
                    .ok(statusDTO)
                    .header(AuthService.SET_AUTH_HEADER, playerToken)
                    .build();
        } catch (GameException e) {
            return getErrorResponse(e.getCode(), e.getMessage());
        }
    }

    @GET
    @Path("/{" + GAME_ID_PARAM + "}")
    @Secured
    public Response getStatus(@PathParam(GAME_ID_PARAM) String gameId) {
        try {
            GameStatusDTO gameStatusDTO = gameService.getPlayerStatusGame(gameId,
                    securityContext.getUserPrincipal().getName());
            return Response
                    .ok(gameStatusDTO)
                    .build();
        } catch (GameException e) {
            return getErrorResponse(e.getCode(), e.getMessage());
        }
    }

    @PUT
    @Path("/{" + GAME_ID_PARAM + "}")
    @Secured
    public Response shot(@PathParam(GAME_ID_PARAM) String gameId, MoveDTO moveDTO) {
        try {
            ShotStatusDTO shotStatusDTO = gameService.shot(gameId,
                    securityContext.getUserPrincipal().getName(), moveDTO);
            return Response
                    .ok(shotStatusDTO)
                    .build();
        } catch (GameException e) {
            return getErrorResponse(e.getCode(), e.getMessage());
        }
    }

    private Response getErrorResponse(int errorCode, String errorMessage) {
        Response.Status status = Response.Status.BAD_REQUEST;
        if (GameException.INCORRECT_GAME_CODE == errorCode) {
            status = Response.Status.NOT_FOUND;
        }
        return Response.status(status)
                .entity(new ErrorDTO(errorMessage))
                .build();
    }

}
