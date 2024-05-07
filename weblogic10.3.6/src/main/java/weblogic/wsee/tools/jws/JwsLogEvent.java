package weblogic.wsee.tools.jws;

import com.bea.util.jam.JElement;
import com.bea.util.jam.JSourcePosition;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import weblogic.wsee.tools.logging.LogEvent;

public class JwsLogEvent extends LogEvent {
   private static ResourceBundle resourceBundle = ResourceBundle.getBundle(JwsLogEvent.class.getPackage().getName() + ".strings", Locale.getDefault());
   private String id = null;
   protected boolean verbose = false;

   public JwsLogEvent(JElement var1, String var2, Object... var3) {
      this.id = var2;
      JSourcePosition var4 = var1.getSourcePosition();
      if (var4 != null) {
         this.setLine(var4.getLine());
         this.setColumn(var4.getColumn());
         this.setSourceURI(var4.getSourceURI());
      }

      this.setText(buildMessage(var2, var3));
   }

   public JwsLogEvent(URI var1, String var2, Object... var3) {
      this.id = var2;
      this.setSourceURI(var1);
      this.setText(buildMessage(var2, var3));
   }

   private static String buildMessage(String var0, Object... var1) {
      String var2 = var0;
      String var3 = resourceBundle.getString(var0);
      if (var3 != null) {
         var2 = MessageFormat.format(var3, var1);
      }

      return var2;
   }

   public String getId() {
      return this.id;
   }
}
