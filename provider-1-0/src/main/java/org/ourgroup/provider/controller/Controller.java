package org.ourgroup.provider.controller;

/**
 * Created by friend_RU.
 */

public class Controller {
    //Думаю, что в контроллер модель отправит команду в виде "<command> <params> <userId>",
    //где userId она допишет сама, так как хранит в себе Customer'a/Employee'я
    public static void parseCommand(String command) {
        String[] strings = command.split(" ");
        //выделение из входной строки команды
        Command c = Command.parseCommand(strings[0]);
        c.execute(strings);

    }
}
