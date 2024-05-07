package weblogic.wsee.wsdl.validation;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Properties;
import weblogic.wsee.util.Verbose;

public class BasicProfile11ErrorMessage {
   private static final boolean verbose = Verbose.isVerbose(BasicProfile11ErrorMessage.class);
   private String message;
   private String id;
   private static final String FILE = "weblogic/wsee/wsdl/validation/BasicProfile-1.1-2004-08-24.txt";
   private static SoftReference properties = new SoftReference((Object)null);
   private String details;

   public BasicProfile11ErrorMessage(String var1, String var2) {
      this.id = var1;
      this.message = var2;
      this.details = this.getMessageFor(var1);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("\n  -- WS-I Basic Profile 1.1 Validation Error --");
      var1.append("\n").append(this.message);
      var1.append("\n").append("Requirement ID: ").append(this.id);
      var1.append("\n").append(this.details);
      var1.append("\n Details here: ").append("http://www.ws-i.org/Profiles/BasicProfile-1.1-2004-08-24.html");
      return var1.toString();
   }

   private Properties getErrorMap() {
      Properties var1 = (Properties)properties.get();
      if (var1 == null) {
         var1 = this.load();
         properties = new SoftReference(var1);
      }

      return var1;
   }

   private String getMessageFor(String var1) {
      Properties var2 = this.getErrorMap();
      String var3 = var2.getProperty(var1);
      if (var3 == null) {
         throw new IllegalArgumentException("Not a valid Basic Profile 1.1 requirement ID: " + var1);
      } else {
         return var3;
      }
   }

   private Properties load() {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      InputStream var2 = var1.getResourceAsStream("weblogic/wsee/wsdl/validation/BasicProfile-1.1-2004-08-24.txt");
      if (var2 == null) {
         throw new AssertionError("Unable to find BasicProfile-1.1-2004-08-24.txt in classpath ");
      } else {
         Properties var3 = new Properties();

         try {
            var3.load(var2);
            return var3;
         } catch (IOException var5) {
            throw new AssertionError("Unable to load from stream: " + var5);
         }
      }
   }
}
