package weblogic.messaging.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class SQLExpression implements Externalizable {
   public static final long serialVersionUID = 7574805445195313601L;
   private static final int EXTERNAL_VERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int HAS_SELECTOR_FLAG = 256;
   protected String selector;

   public SQLExpression() {
   }

   public SQLExpression(String var1) {
      this.setSelector(var1);
   }

   public boolean isNull() {
      return this.selector == null;
   }

   public String getSelector() {
      return this.selector;
   }

   public void setSelector(String var1) {
      if (var1 != null) {
         var1 = var1.trim();
         if (var1.length() == 0) {
            var1 = null;
         }
      }

      this.selector = var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (this.selector != null) {
         var2 |= 256;
      }

      var1.writeInt(var2);
      if (this.selector != null) {
         var1.writeUTF(this.selector);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      if ((var2 & 255) != 1) {
         throw new IOException("External version mismatch");
      } else {
         if ((var2 & 256) != 0) {
            this.selector = var1.readUTF();
         }

      }
   }
}
