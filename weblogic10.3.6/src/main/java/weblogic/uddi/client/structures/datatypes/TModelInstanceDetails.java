package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class TModelInstanceDetails {
   private Vector tModelInstanceInfo = new Vector();

   public void addTModelInstanceInfo(TModelInstanceInfo var1) {
      this.tModelInstanceInfo.add(var1);
   }

   public void setTModelInstanceInfoVector(Vector var1) {
      this.tModelInstanceInfo = var1;
   }

   public Vector getTModelInstanceInfoVector() {
      return this.tModelInstanceInfo;
   }
}
