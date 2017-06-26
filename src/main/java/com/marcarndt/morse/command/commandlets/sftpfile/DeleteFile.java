package com.marcarndt.morse.command.commandlets.sftpfile;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.command.SftpFileCommand;
import com.marcarndt.morse.command.commandlet.Commandlet;
import com.marcarndt.morse.service.SSHService;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/05/08.
 */
@Stateless
public class DeleteFile implements Commandlet {

  public static String SftpDeleteFileState = "SFTP Delete File";

  @Inject
  SSHService sshService;

  @Override
  public boolean canHandleCommand(Message message, String state) {
    return state.equals(SftpFileCommand.SftpFileCommandState) && message.getText()
        .equals(SftpFileCommand.delete);
  }

  @Override
  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {
    morseBot.sendReplyKeyboardMessage(message, "Select File to delete",
        sshService.getFileDescriptions());
  }

  @Override
  public String getNewState(Message message, String command) {
    return SftpDeleteFileState;
  }

  @Override
  public List<String> getNewStateParams(Message message, String state, List<String> parameters) {
    return null;
  }
}
