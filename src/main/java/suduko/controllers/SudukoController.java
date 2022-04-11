package suduko.controllers;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import suduko.logic.SudukoLogic;

import java.util.Objects;

public class SudukoController {

    private final SudukoLogic sudukoLogic = new SudukoLogic();
    private final Javalin app;

    public SudukoController() {

        app = Javalin.create().start(8080);
        sudukoControllers();
    }

    public void sudukoControllers() {
        app.get("api/getNewBoard", ctx -> {
            ctx.status(200).json(sudukoLogic.createNewBoard(Objects.requireNonNull(ctx.queryParam("level"))));
        });

        app.get("api/getFullBoard", ctx -> {
            ctx.status(200).json(sudukoLogic.getFullSudokuBoard());
        });

        app.post("api/validateInput", ctx -> {
            ctx.status(200).json(sudukoLogic.validateInputCells(ctx.body()));

        });

        app.get("/", ctx -> ctx.result("Hello Heroku"));
    }

}