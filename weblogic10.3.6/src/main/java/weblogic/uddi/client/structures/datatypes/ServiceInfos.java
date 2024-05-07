package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class ServiceInfos {
   private Vector serviceInfo = new Vector();

   public Vector getServiceInfoVector() {
      return this.serviceInfo;
   }

   public void setServiceInfoVector(Vector var1) {
      this.serviceInfo = var1;
   }
}
