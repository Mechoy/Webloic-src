package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class DiscoveryURLs extends UDDIList implements Serializable {
   public DiscoveryURLs() {
   }

   public DiscoveryURLs(DiscoveryURLs var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         for(DiscoveryURL var2 = var1.getFirst(); var2 != null; var2 = var1.getNext()) {
            this.add(new DiscoveryURL(var2));
         }

      }
   }

   public void add(DiscoveryURL var1) throws UDDIException {
      String var2 = var1.toString();
      DiscoveryURL var3 = (DiscoveryURL)super.getVFirst();

      boolean var4;
      for(var4 = false; var3 != null; var3 = (DiscoveryURL)super.getVNext()) {
         String var5 = var3.toString();
         if (var5.equals(var2)) {
            var4 = true;
         }
      }

      if (!var4) {
         super.add(var1);
      }

   }

   public DiscoveryURL getFirst() {
      return (DiscoveryURL)super.getVFirst();
   }

   public DiscoveryURL getNext() {
      return (DiscoveryURL)super.getVNext();
   }

   public String toXML() {
      return super.toXML("discoveryURLs");
   }
}
