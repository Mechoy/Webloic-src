package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class TModelInstanceInfo {
   private Vector description = new Vector();
   private InstanceDetails instanceDetails = null;
   private String tModelKey = null;

   public void setTModelKey(String var1) {
      this.tModelKey = var1;
   }

   public String getTModelKey() {
      return this.tModelKey;
   }

   public void addDescription(String var1) {
      this.description.add(new Description(var1));
   }

   public void setDescriptionVector(Vector var1) {
      this.description = var1;
   }

   public Vector getDescriptionVector() {
      return this.description;
   }

   public void setInstanceDetails(InstanceDetails var1) {
      this.instanceDetails = var1;
   }

   public InstanceDetails getInstanceDetails() {
      return this.instanceDetails;
   }
}
