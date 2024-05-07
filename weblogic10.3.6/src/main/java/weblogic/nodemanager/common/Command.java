package weblogic.nodemanager.common;

import java.util.HashMap;
import java.util.Locale;
import weblogic.nodemanager.NodeManagerTextTextFormatter;

public class Command {
   private String name;
   private static final HashMap commands = new HashMap();
   public static final Command VERSION = put("VERSION");
   public static final Command DOMAIN = put("DOMAIN");
   public static final Command SERVER = put("SERVER");
   public static final Command COHERENCESERVER = put("COHERENCESERVER");
   public static final Command USER = put("USER");
   public static final Command PASS = put("PASS");
   public static final Command STAT = put("STAT");
   public static final Command QUIT = put("QUIT");
   public static final Command START = put("START");
   public static final Command STARTP = put("STARTP");
   public static final Command KILL = put("KILL");
   public static final Command GETLOG = put("GETLOG");
   public static final Command GETNMLOG = put("GETNMLOG");
   public static final Command HELLO = put("HELLO");
   public static final Command CHGCRED = put("CHGCRED");
   public static final Command GETSTATES = put("GETSTATES");
   public static final Command EXECSCRIPT = put("EXECSCRIPT");
   public static final Command UPDATEPROPS = put("UPDATEPROPS");

   private Command(String var1) {
      this.name = var1;
   }

   private static Command put(String var0) {
      Command var1 = new Command(var0);
      commands.put(var0.toUpperCase(Locale.ENGLISH), var1);
      return var1;
   }

   public static Command parse(String var0) {
      Command var1 = (Command)commands.get(var0.toUpperCase(Locale.ENGLISH));
      if (var1 != null) {
         return var1;
      } else {
         throw new IllegalArgumentException(NodeManagerTextTextFormatter.getInstance().getInvalidCommand(var0));
      }
   }

   public String getName() {
      return this.name;
   }

   public boolean equals(Object var1) {
      return var1 instanceof Command ? this.name.equals(((Command)var1).name) : false;
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public String toString() {
      return this.name;
   }
}
