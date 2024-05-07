package weblogic.wsee.sender.api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import weblogic.wsee.Version;

public class Resources implements Serializable {
   private static final long serialVersionUID = 1L;
   private SenderFactory _senderFactory;
   private ConversationOptions _options;

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject("10.3.6");
      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      String var2 = (String)var1.readObject();
      if (!Version.isLaterThanOrEqualTo(var2, "10.3+")) {
         throw new IOException("Unknown/unsupported serialization version: " + var2);
      } else {
         var1.defaultReadObject();
      }
   }

   public Resources() {
   }

   public Resources(SenderFactory var1, ConversationOptions var2) {
      this._senderFactory = var1;
      this._options = var2;
   }

   public Resources(Resources var1) {
      this._senderFactory = var1._senderFactory;
      this._options = var1._options;
   }

   public SenderFactory getSenderFactory() {
      return this._senderFactory;
   }

   public void setSenderFactory(SenderFactory var1) {
      this._senderFactory = var1;
   }

   public ConversationOptions getOptions() {
      return this._options;
   }

   public void setOptions(ConversationOptions var1) {
      this._options = var1;
   }
}
