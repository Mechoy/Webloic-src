package weblogic.uddi.client.structures.request;

import java.util.Vector;
import weblogic.uddi.client.structures.datatypes.ServiceKey;

public class DeleteService extends UpdateRequest {
   private Vector serviceKeyVector = new Vector();

   public void addServiceKey(String var1) {
      ServiceKey var2 = new ServiceKey(var1);
      this.serviceKeyVector.add(var2);
   }

   public void setServiceKeyVector(Vector var1) {
      this.serviceKeyVector = var1;
   }

   public Vector getServiceKeyVector() {
      return this.serviceKeyVector;
   }
}
