package weblogic.wsee.message;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.rpc.handler.MessageContext;

public class FilteredMessageContext implements MessageContext {
   private static final String INTERNAL_PREFIX = "weblogic.wsee.";
   private final MessageContext messageContext;

   public FilteredMessageContext(MessageContext var1) {
      assert var1 != null : "No message context";

      this.messageContext = var1;
   }

   private void checkKey(String var1) {
      if (!this.isValid(var1)) {
         throw new IllegalArgumentException(var1 + " starts with an internal prefix");
      }
   }

   private boolean isValid(String var1) {
      return var1 == null || !var1.startsWith("weblogic.wsee.");
   }

   public void setProperty(String var1, Object var2) {
      this.checkKey(var1);
      this.messageContext.setProperty(var1, var2);
   }

   public Object getProperty(String var1) {
      this.checkKey(var1);
      return this.messageContext.getProperty(var1);
   }

   public void removeProperty(String var1) {
      this.checkKey(var1);
      this.messageContext.removeProperty(var1);
   }

   public boolean containsProperty(String var1) {
      this.checkKey(var1);
      return this.messageContext.containsProperty(var1);
   }

   public Iterator getPropertyNames() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.messageContext.getPropertyNames();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (this.isValid(var3)) {
            var1.add(var3);
         }
      }

      return Collections.unmodifiableSet(var1).iterator();
   }
}
