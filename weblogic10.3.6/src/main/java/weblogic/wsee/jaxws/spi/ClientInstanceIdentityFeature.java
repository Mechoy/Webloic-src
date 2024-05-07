package weblogic.wsee.jaxws.spi;

import javax.xml.ws.WebServiceFeature;

public class ClientInstanceIdentityFeature extends WebServiceFeature {
   private ClientInstanceIdentity _clientInstanceId;

   public ClientInstanceIdentityFeature(ClientInstanceIdentity var1) {
      this.enabled = true;
      if (var1 == null) {
         throw new IllegalArgumentException("Null ClientInstanceIdentity");
      } else {
         this._clientInstanceId = var1;
      }
   }

   public ClientInstanceIdentity getClientInstanceId() {
      return this._clientInstanceId;
   }

   public String getID() {
      return ClientInstanceIdentityFeature.class.getSimpleName();
   }

   public void dispose() {
      this._clientInstanceId = null;
   }
}
