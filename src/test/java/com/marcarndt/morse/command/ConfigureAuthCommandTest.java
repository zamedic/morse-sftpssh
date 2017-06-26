package com.marcarndt.morse.command;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.service.UserService;
import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by arndt on 2017/06/26.
 */
@RunWith(PowerMockRunner.class)
public class ConfigureAuthCommandTest {

  /**
   * The Morse bot.
   */
  @Mock
  private transient MorseBot morseBot;
  /**
   * The User.
   */
  @Mock
  private transient User user;
  /**
   * The Chat.
   */
  @Mock
  private transient Chat chat;

  /**
   * The Configure auth command.
   */
  @InjectMocks
  private transient ConfigureAuthCommand configureAuthCommand;


  /**
   * Gets command identifier.
   */
  @Test
  public void getCommandIdentifier() {
    Assert.assertEquals("sshAuthDetails", configureAuthCommand.getCommandIdentifier());
  }

  /**
   * Gets description.
   */
  @Test
  public void getDescription() {
    Assert.assertEquals("Setup authentication details used by SFTP and SSH",
        configureAuthCommand.getDescription());
  }

  /**
   * Gets role.
   */
  @Test
  public void getRole() {
    Assert.assertEquals(UserService.ADMIN, configureAuthCommand.getRole());
  }

  /**
   * Perform command.
   */
  @Test
  public void performCommand() {
    String result = configureAuthCommand.performCommand(morseBot,user,chat,null);

    Mockito.verify(morseBot)
        .sendReplyKeyboardMessage(user, chat, "Select function", "Add Auth Details",
            "Delete Auth Details");
    Assert.assertEquals("Configure ssh auth",result);


  }

}