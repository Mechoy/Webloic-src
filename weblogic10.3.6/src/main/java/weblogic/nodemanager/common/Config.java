package weblogic.nodemanager.common;

import java.util.Properties;
import weblogic.nodemanager.NodeManagerTextTextFormatter;

public class Config {
   protected Properties props;

   public Config() {
   }

   public Config(Properties var1) {
      this.props = var1;
   }

   public String getProperty(String var1) {
      return this.trim(this.props.getProperty(var1));
   }

   public String getProperty(String var1, String var2) {
      return this.trim(this.props.getProperty(var1, var2));
   }

   public boolean getBooleanProperty(String var1) {
      return "true".equalsIgnoreCase(this.trim(this.props.getProperty(var1)));
   }

   public boolean getBooleanProperty(String var1, boolean var2) {
      String var3 = this.trim(this.getProperty(var1));
      return var3 != null ? "true".equalsIgnoreCase(var3) : var2;
   }

   public int getIntProperty(String var1, int var2) throws ConfigException {
      String var3 = this.trim(this.props.getProperty(var1));

      try {
         return var3 != null ? Integer.parseInt(var3) : var2;
      } catch (NumberFormatException var5) {
         throw new ConfigException(NodeManagerTextTextFormatter.getInstance().getInvalidIntProperty(var1));
      }
   }

   private String trim(String var1) {
      if (var1 != null) {
         var1 = var1.trim();
         if (var1.length() == 0) {
            var1 = null;
         }
      }

      return var1;
   }
}
