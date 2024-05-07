package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class InstanceDetails extends UDDIElement {
   private Descriptions m_descriptions = null;
   private OverviewDoc m_overviewDoc = null;
   private InstanceParms m_instanceParms = null;

   public InstanceDetails() {
   }

   public InstanceDetails(InstanceDetails var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         if (var1.m_descriptions != null) {
            this.m_descriptions = new Descriptions(var1.m_descriptions);
         }

         if (var1.m_overviewDoc != null) {
            this.m_overviewDoc = new OverviewDoc(var1.m_overviewDoc);
         }

         if (var1.m_instanceParms != null) {
            this.m_instanceParms = new InstanceParms(var1.m_instanceParms);
         }

      }
   }

   public void addDescription(Description var1) throws UDDIException {
      if (var1 != null) {
         if (this.m_descriptions == null) {
            this.m_descriptions = new Descriptions();
         }

         this.m_descriptions.add(var1);
      }
   }

   public OverviewDoc getOverviewDoc() {
      return this.m_overviewDoc;
   }

   public InstanceParms getInstanceParms() {
      return this.m_instanceParms;
   }

   public void setDescriptions(Descriptions var1) {
      this.m_descriptions = var1;
   }

   public Descriptions getDescriptions() {
      return this.m_descriptions;
   }

   public void setOverviewDoc(OverviewDoc var1) {
      this.m_overviewDoc = var1;
   }

   public void setInstanceParms(InstanceParms var1) {
      this.m_instanceParms = var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof InstanceDetails)) {
         return false;
      } else {
         InstanceDetails var2 = (InstanceDetails)var1;
         boolean var3 = true;
         var3 &= this.m_descriptions == null ? var2.m_descriptions == null : this.m_descriptions.hasEqualContent(var2.m_descriptions);
         var3 &= Util.isEqual((Object)this.m_instanceParms, (Object)var2.m_instanceParms);
         var3 &= Util.isEqual((Object)this.m_overviewDoc, (Object)var2.m_overviewDoc);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<instanceDetails>");
      if (this.m_descriptions != null) {
         var1.append(this.m_descriptions.toXML());
      }

      if (this.m_overviewDoc != null) {
         var1.append(this.m_overviewDoc.toXML());
      }

      if (this.m_instanceParms != null) {
         var1.append(this.m_instanceParms.toXML());
      }

      var1.append("</instanceDetails>");
      return var1.toString();
   }
}
