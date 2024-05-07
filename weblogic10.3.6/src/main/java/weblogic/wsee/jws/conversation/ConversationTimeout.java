package weblogic.wsee.jws.conversation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import weblogic.wsee.jws.container.ContainerEvent;

public class ConversationTimeout implements Serializable, ContainerEvent {
   private static final long serialVersionUID = 5214846059672441513L;
   private final ContainerEvent.TYPE type;
   private final String uri;
   private final String conversationId;
   private final long timeoutTime;
   private StoreConfig storeConfig = null;

   public ConversationTimeout(ContainerEvent.TYPE var1, String var2, String var3, StoreConfig var4, long var5) {
      this.type = var1;
      this.uri = var2;
      this.conversationId = var3;
      this.timeoutTime = var5;
      this.storeConfig = var4;
   }

   public String getURI() {
      return this.uri;
   }

   public String getConversationID() {
      return this.conversationId;
   }

   public ContainerEvent.TYPE getEventType() {
      return this.type;
   }

   public long getTime() {
      return this.timeoutTime;
   }

   public StoreConfig getStoreConfig() {
      return this.storeConfig;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("ConversationTimeout\n");
      var1.append(" type          = " + this.type + "\n");
      var1.append(" uri           = " + this.uri + "\n");
      var1.append(" coversationId = " + this.conversationId + "\n");
      var1.append(" storeConfig   = " + this.storeConfig + "\n");
      var1.append(" timeoutTime   = " + this.timeoutTime + "\n");
      return var1.toString();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeUTF("9.0");
      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      String var2 = var1.readUTF();
      if (!var2.equals("9.0")) {
         throw new IOException("Wrong version, expected: 9.0 actual: " + var2);
      } else {
         var1.defaultReadObject();
      }
   }
}
