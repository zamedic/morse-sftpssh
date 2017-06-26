package com.marcarndt.morse.command.commandlets.sftpfile;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.command.SftpFileCommand;
import com.marcarndt.morse.command.commandlet.Commandlet;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Created by arndt on 2017/05/05.
 */
@Stateless
public class AddFile implements Commandlet {

  public static String AddSftpFileState = "Add SFTP file";

  @Override
  public boolean canHandleCommand(Message message, String state) {
    return state.equals(SftpFileCommand.SftpFileCommandState) && message.getText()
        .equals(SftpFileCommand.add);
  }

  @Override
  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {
    morseBot.sendReplyMessage(message, "Enter a friendly name for the file");
  }

  @Override
  public String getNewState(Message message, String command) {
    return AddSftpFileState;
  }

  @Override
  public List<String> getNewStateParams(Message message, String state, List<String> parameters) {
    return null;
  }
}
