package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class TModelInstanceInfo extends UDDIBagObject {
   private TModelKey m_tModelKey = null;
   private Descriptions m_descriptions = null;
   private InstanceDetails m_instanceDetails = null;
   private BindingKey m_bindingKey = null;

   public TModelInstanceInfo(TModelInstanceInfo var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         if (var1.m_bindingKey != null) {
            this.m_bindingKey = new BindingKey(var1.m_bindingKey);
         }

         if (var1.m_tModelKey != null) {
            this.m_tModelKey = new TModelKey(var1.m_tModelKey);
         }

         if (var1.m_descriptions != null) {
            this.m_descriptions = new Descriptions(var1.m_descriptions);
         }

         if (var1.m_instanceDetails != null) {
            this.m_instanceDetails = new InstanceDetails(var1.m_instanceDetails);
         }

      }
   }

   public TModelInstanceInfo(TModelKey var1) {
      this.m_tModelKey = var1;
   }

   public TModelInstanceInfo() {
   }

   public void addDescription(Description var1) throws UDDIException {
      if (var1 != null) {
         if (this.m_descriptions == null) {
            this.m_descriptions = new Descriptions();
         }

         this.m_descriptions.add(var1);
      }
   }

   public void setInstanceDetails(InstanceDetails var1) {
      this.m_instanceDetails = var1;
   }

   public void setDescriptions(Descriptions var1) {
      this.m_descriptions = var1;
   }

   public Descriptions getDescriptions() {
      return this.m_descriptions;
   }

   public InstanceDetails getInstanceDetails() {
      return this.m_instanceDetails;
   }

   public void setTModelKey(TModelKey var1) {
      this.m_tModelKey = var1;
   }

   public TModelKey getTModelKey() {
      return this.m_tModelKey;
   }

   public void setBindingKey(BindingKey var1) {
      this.m_bindingKey = var1;
   }

   public BindingKey getBindingKey() {
      return this.m_bindingKey;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof TModelInstanceInfo)) {
         return false;
      } else {
         TModelInstanceInfo var2 = (TModelInstanceInfo)var1;
         boolean var3 = true;
         var3 &= this.m_descriptions == null ? var2.m_descriptions == null : this.m_descriptions.hasEqualContent(var2.m_descriptions);
         var3 &= Util.isEqual((Object)this.m_instanceDetails, (Object)var2.m_instanceDetails);
         var3 &= Util.isEqual((Object)this.m_tModelKey, (Object)var2.m_tModelKey);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<tModelInstanceInfo");
      var1.append(" tModelKey=\"").append(this.m_tModelKey.toString()).append("\"");
      var1.append(">");
      if (this.m_descriptions != null) {
         var1.append(this.m_descriptions.toXML());
      }

      if (this.m_instanceDetails != null) {
         var1.append(this.m_instanceDetails.toXML());
      }

      var1.append("</tModelInstanceInfo>");
      return var1.toString();
   }
}
