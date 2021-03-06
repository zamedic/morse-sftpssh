package com.marcarndt.morse.data;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by arndt on 2017/04/16.
 */
@Entity
public class Command {
  @Id
  private ObjectId objectId;

  String description;
  String command;

  public Command() {
  }

  public Command(String description, String command) {
    this.description = description;
    this.command = command;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }
}
