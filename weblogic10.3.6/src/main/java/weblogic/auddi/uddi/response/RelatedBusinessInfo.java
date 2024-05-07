package weblogic.auddi.uddi.response;

import java.util.ArrayList;
import java.util.List;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.Description;
import weblogic.auddi.uddi.datastructure.Descriptions;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.Names;
import weblogic.auddi.uddi.datastructure.UDDIListObject;
import weblogic.auddi.util.Util;

public class RelatedBusinessInfo extends UDDIListObject {
   private BusinessKey businessKey = null;
   private Descriptions descriptions = null;
   private List m_sharedRelationships = new ArrayList();
   private Names names = null;

   public RelatedBusinessInfo(BusinessKey var1) {
      this.businessKey = var1;
   }

   public RelatedBusinessInfo() {
   }

   public void addName(Name var1) {
      if (var1 != null) {
         if (this.names == null) {
            this.names = new Names();
         }

         this.names.add(var1);
      }
   }

   public void setBusinessKey(BusinessKey var1) {
      this.businessKey = var1;
   }

   public void setNames(Names var1) throws UDDIException {
      this.names = var1;
   }

   public Names getNames() throws UDDIException {
      return this.names;
   }

   public void addDescription(Description var1) throws UDDIException {
      if (var1 != null) {
         if (this.descriptions == null) {
            this.descriptions = new Descriptions();
         }

         this.descriptions.add(var1);
      }
   }

   public void setDescriptions(Descriptions var1) throws UDDIException {
      this.descriptions = var1;
   }

   public void addSharedRelationships(SharedRelationships var1) throws FatalErrorException {
      if (var1 != null) {
         if (this.m_sharedRelationships.size() >= 2) {
            throw new FatalErrorException(UDDIMessages.get("error.tooManyOptions.sharedRelationships", "2"));
         } else {
            this.m_sharedRelationships.add(var1);
         }
      }
   }

   public BusinessKey getBusinessKey() {
      return this.businessKey;
   }

   public Descriptions getDescriptions() {
      return this.descriptions;
   }

   public List getSharedRelationships() {
      return this.m_sharedRelationships;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof RelatedBusinessInfo)) {
         return false;
      } else {
         RelatedBusinessInfo var2 = (RelatedBusinessInfo)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.businessKey, (Object)var2.businessKey);
         var3 &= Util.isEqual((Object)this.descriptions, (Object)var2.descriptions);
         var3 &= Util.isEqual((Object)this.m_sharedRelationships, (Object)var2.m_sharedRelationships);
         var3 &= Util.isEqual((Object)this.names, (Object)var2.names);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<relatedBusinessInfo>");
      if (this.businessKey != null) {
         var1.append(this.businessKey.toXML());
      }

      if (this.names != null) {
         var1.append(this.names.toXML());
      }

      if (this.descriptions != null) {
         var1.append(this.descriptions.toXML(""));
      }

      if (this.m_sharedRelationships != null) {
         for(int var2 = 0; var2 < this.m_sharedRelationships.size(); ++var2) {
            var1.append(((SharedRelationships)((SharedRelationships)this.m_sharedRelationships.get(var2))).toXML());
         }
      }

      var1.append("</relatedBusinessInfo>");
      return var1.toString();
   }
}
