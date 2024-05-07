package weblogic.wsee.jaxws.persistence;

import com.sun.istack.NotNull;
import weblogic.jws.jaxws.WLSWebServiceFeature;

public class ConversationalClientInstanceFeature extends WLSWebServiceFeature {
   private static String ID = "Conversational Client Instance Feature";
   private String _correlationId;

   public ConversationalClientInstanceFeature(@NotNull String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Null correlation ID for DurableClientInstanceFeature");
      } else {
         this._correlationId = var1;
         super.enabled = true;
         this.setTubelineImpact(false);
      }
   }

   public String getID() {
      return ID;
   }

   public String getCorrelationId() {
      return this._correlationId;
   }
}
