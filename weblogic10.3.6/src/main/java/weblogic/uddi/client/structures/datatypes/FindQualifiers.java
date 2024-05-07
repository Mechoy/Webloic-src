package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class FindQualifiers {
   private Vector findQualifier = new Vector();

   public void addFindQualifier(String var1) {
      this.findQualifier.add(new FindQualifier(var1));
   }

   public void setFindQualifierVector(Vector var1) {
      this.findQualifier = var1;
   }

   public Vector getFindQualifierVector() {
      return this.findQualifier;
   }
}
