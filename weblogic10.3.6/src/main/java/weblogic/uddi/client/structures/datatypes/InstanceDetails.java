package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class InstanceDetails {
   private Vector description = new Vector();
   private OverviewDoc overviewDoc = null;
   private InstanceParms instanceParms = null;

   public void addDescription(String var1) {
      this.description.add(new Description(var1));
   }

   public Vector getDescriptionVector() {
      return this.description;
   }

   public void setDescriptionVector(Vector var1) {
      this.description = var1;
   }

   public void setOverviewDoc(OverviewDoc var1) {
      this.overviewDoc = var1;
   }

   public void setInstanceParms(InstanceParms var1) {
      this.instanceParms = var1;
   }

   public InstanceParms getInstanceParms() {
      return this.instanceParms;
   }

   public OverviewDoc getOverviewDoc() {
      return this.overviewDoc;
   }
}
