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
public class SftpFileCommand extends BaseCommand {
  public static String add = "Add Command";
  public static String delete = "Delete Command";

  public static String SftpFileCommandState = "File command state";


  @Override
  public String getRole() {
    return UserService.ADMIN;
  }

  @Override
  protected String performCommand(MorseBot morseBot, User user, Chat chat, String[] strings) {
    morseBot.sendReplyKeyboardMessage(user,chat,"Select Option",add,delete);
    return SftpFileCommandState;
  }

  @Override
  public String getCommandIdentifier() {
    return "sftpFileManage";
  }

  @Override
  public String getDescription() {
    return "Adds or removes files from a predefined list of files which may be downloaded";
  }
}
