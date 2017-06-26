package com.marcarndt.morse.command.commandlets.sftpfile;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.command.commandlet.Commandlet;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.Arrays;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Created by arndt on 2017/05/05.
 */
@Stateless
public class AddFileDetails implements Commandlet {

  public static String AddSftpFileDetailsState = "Add SFTP File Details";

  @Override
  public boolean canHandleCommand(Message message, String state) {
    return state.equals(AddFile.AddSftpFileState);
  }

  @Override
  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {
    morseBot.sendReplyMessage(message, "Enter file name and path");

  }

  @Override
  public String getNewState(Message message, String command) {
    return AddSftpFileDetailsState;
  }

  @Override
  public List<String> getNewStateParams(Message message, String state, List<String> parameters) {
    return Arrays.asList(message.getText());
  }
}
