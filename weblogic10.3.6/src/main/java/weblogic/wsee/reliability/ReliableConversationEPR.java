package weblogic.wsee.reliability;

import java.io.Serializable;
import weblogic.wsee.addressing.EndpointReference;

public final class ReliableConversationEPR implements Serializable {
   static final long serialVersionUID = -2630332817341273613L;
   private final String key;
   private final EndpointReference epr;
   private final String seqId;

   public ReliableConversationEPR(String var1, EndpointReference var2, String var3) {
      this.key = var1;
      this.epr = var2;
      this.seqId = var3;
   }

   public String getKey() {
      return this.key;
   }

   public String getSeqId() {
      return this.seqId;
   }

   public EndpointReference getEPR() {
      return this.epr;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Key: ").append(this.key);
      var1.append(" ");
      var1.append("Seq ID: ").append(this.seqId);
      var1.append(" ");
      var1.append("EPR: ").append(this.epr);
      return var1.toString();
   }
}
