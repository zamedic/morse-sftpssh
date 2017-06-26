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
public class ConfigureAuthCommand extends BaseCommand {
  public static String addAuth = "Add Auth Details";
  public static String deleteAuth = "Delete Auth Details";

  public static String ConfigAuthState = "Configure ssh auth";



  @Override
  public String getCommandIdentifier() {
    return "sshAuthDetails";
  }

  @Override
  public String getDescription() {
    return "Setup authentication details used by SFTP and SSH";
  }

  @Override
  public String getRole() {
    return UserService.ADMIN;
  }

  @Override
  protected String performCommand(MorseBot morseBot, User user, Chat chat, String[] arguments) {
    morseBot.sendReplyKeyboardMessage(user,chat,"Select function", addAuth,deleteAuth);
    return ConfigAuthState;
  }
}
