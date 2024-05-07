package weblogic.wsee.sender.api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.xml.datatype.Duration;
import weblogic.wsee.Version;

public class ConversationOptions implements Serializable {
   private static final long serialVersionUID = 1L;
   private Duration _baseRetransmissionInterval;
   private boolean _exponentialBackoffEnabled;
   private Duration _expires;
   private Duration _idleTimeout;
   private boolean _inOrder;

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

   public Duration getBaseRetransmissionInterval() {
      return this._baseRetransmissionInterval;
   }

   public void setBaseRetransmissionInterval(Duration var1) {
      this._baseRetransmissionInterval = var1;
   }

   public boolean isExponentialBackoffEnabled() {
      return this._exponentialBackoffEnabled;
   }

   public void setExponentialBackoffEnabled(boolean var1) {
      this._exponentialBackoffEnabled = var1;
   }

   public Duration getExpires() {
      return this._expires;
   }

   public void setExpires(Duration var1) {
      this._expires = var1;
   }

   public Duration getIdleTimeout() {
      return this._idleTimeout;
   }

   public void setIdleTimeout(Duration var1) {
      this._idleTimeout = var1;
   }

   public boolean isInOrder() {
      return this._inOrder;
   }

   public void setInOrder(boolean var1) {
      this._inOrder = var1;
   }
}
