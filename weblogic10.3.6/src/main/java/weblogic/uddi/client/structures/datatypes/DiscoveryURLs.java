package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class DiscoveryURLs {
   private Vector discoveryURLVector = new Vector();

   public Vector getDiscoveryURLVector() {
      return this.discoveryURLVector;
   }

   public void setDiscoveryURLVector(Vector var1) {
      this.discoveryURLVector = var1;
   }

   public void addDiscoveryURL(DiscoveryURL var1) {
      this.discoveryURLVector.add(var1);
   }
}
