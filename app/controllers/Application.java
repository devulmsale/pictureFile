package controllers;

import play.*;
import play.mvc.*;

public class Application extends Controller {

    public static void index() {
        Logger.info("PATH : %ssss --" ,  Play.applicationPath.getAbsolutePath());
        render();
    }

}