package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class OverviewDoc {
   private OverviewURL overviewURL = null;
   private Vector description = new Vector();

   public void addDescription(String var1) {
      Description var2 = new Description(var1);
      this.description.add(var2);
   }

   public void setOverviewURL(String var1) {
      this.overviewURL = new OverviewURL(var1);
   }

   public void setOverviewURL(OverviewURL var1) {
      this.overviewURL = var1;
   }

   public OverviewURL getOverviewURL() {
      return this.overviewURL;
   }

   public Vector getDescriptionVector() {
      return this.description;
   }

   public void setDescriptionVector(Vector var1) {
      this.description = var1;
   }
}
