package weblogic.wsee.sender.api;

import java.io.Serializable;
import weblogic.wsee.persistence.AbstractStorable;

public abstract class AbstractSendRequest extends AbstractStorable implements SendRequest, Comparable<SendRequest> {
   private static final long serialVersionUID = 1L;

   protected AbstractSendRequest(Serializable var1) {
      super(var1);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      String var2 = (String)this.getObjectId();
      if (var2 == null) {
         var1.append(super.toString());
      } else {
         var1.append("ID: ").append(var2);
      }

      var1.append(", SeqNum: ").append(this.getSequenceNumber());
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof SendRequest)) {
         return false;
      } else {
         SendRequest var2 = (SendRequest)var1;
         return this.getObjectId() == null && var2.getObjectId() == null || this.getObjectId().equals(var2.getObjectId());
      }
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public int compareTo(SendRequest var1) {
      if (this.getSequenceNumber() > var1.getSequenceNumber()) {
         return 1;
      } else {
         return this.getSequenceNumber() < var1.getSequenceNumber() ? -1 : 0;
      }
   }
}
