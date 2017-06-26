package com.marcarndt.morse.command;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.service.UserService;
import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.User;
import javax.ejb.Stateless;

/**
 * Created by arndt on 2017/06/26.
 */
@Stateless
public class SSHCommand extends BaseCommand {
  public static String add = "Add Command";
  public static String delete = "Delete Command";

  public static String SSHCommandConfigureStage = "Configure SSH Commands";

  @Override
  public String getRole() {
    return UserService.ADMIN;
  }

  @Override
  protected String performCommand(MorseBot morseBot, User user, Chat chat, String[] strings) {
    morseBot.sendReplyKeyboardMessage(user,chat,"Option",add,delete);
    return SSHCommandConfigureStage;
  }

  @Override
  public String getCommandIdentifier() {
    return "sshConfigureCommands";
  }

  @Override
  public String getDescription() {
    return "Allows the admin to add / delete a predefined list of commands which may be run";
  }
}
