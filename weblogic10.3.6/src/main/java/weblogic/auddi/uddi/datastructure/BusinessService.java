package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import java.util.Date;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class BusinessService extends UDDIListObject implements Serializable {
   private BusinessKey m_businessKey;
   private ServiceKey m_serviceKey;
   private UniqueNames m_names;
   private Descriptions m_descriptions;
   private BindingTemplates m_bindingTemplates;
   private CategoryBag m_categories;
   private boolean m_isProjection;

   public BusinessService() {
      this.m_businessKey = null;
      this.m_serviceKey = null;
      this.m_names = null;
      this.m_descriptions = null;
      this.m_bindingTemplates = new BindingTemplates();
      this.m_categories = null;
      this.m_isProjection = false;
      this.m_bindingTemplates = new BindingTemplates();
   }

   public BusinessService(BusinessService var1) throws UDDIException {
      this();
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         if (var1.m_businessKey != null) {
            this.m_businessKey = new BusinessKey(var1.m_businessKey);
         }

         if (var1.m_serviceKey != null) {
            this.m_serviceKey = new ServiceKey(var1.m_serviceKey);
         }

         if (var1.m_names != null) {
            this.m_names = new UniqueNames(var1.m_names);
         }

         if (var1.m_descriptions != null) {
            this.m_descriptions = new Descriptions(var1.m_descriptions);
         }

         if (var1.m_categories != null) {
            this.m_categories = new CategoryBag(var1.m_categories);
         }

         if (var1.m_date != null) {
            this.m_date = new Date(var1.m_date.getTime());
         }

         if (var1.m_bindingTemplates != null) {
            this.m_bindingTemplates = new BindingTemplates(var1.m_bindingTemplates);
         }

         this.m_isProjection = var1.m_isProjection;
      }
   }

   public boolean isProjection() {
      return this.m_isProjection;
   }

   public void setIsProjection(boolean var1) {
      this.m_isProjection = var1;
   }

   public void setBusinessKey(BusinessKey var1) {
      this.m_businessKey = var1;
   }

   public BusinessKey getBusinessKey() {
      return this.m_businessKey;
   }

   public void setServiceKey(ServiceKey var1) {
      this.m_serviceKey = var1;
   }

   public ServiceKey getServiceKey() {
      return this.m_serviceKey;
   }

   public void addName(Name var1) throws UDDIException {
      if (this.m_names == null) {
         this.m_names = new UniqueNames();
      }

      this.m_names.add(var1);
   }

   public UniqueNames getNames() {
      return this.m_names;
   }

   public void setNames(UniqueNames var1) {
      this.m_names = var1;
   }

   public void setDescriptions(Descriptions var1) {
      this.m_descriptions = var1;
   }

   public Descriptions getDescriptions() {
      return this.m_descriptions;
   }

   public CategoryBag getCategoryBag() {
      return this.m_categories;
   }

   public void addDescription(Description var1) throws UDDIException {
      if (var1 != null) {
         if (this.m_descriptions == null) {
            this.m_descriptions = new Descriptions();
         }

         this.m_descriptions.add(var1);
      }
   }

   public void addBindingTemplate(BindingTemplate var1) throws UDDIException {
      this.m_bindingTemplates.add(var1);
   }

   public void setBindingTemplates(BindingTemplates var1) {
      this.m_bindingTemplates = var1;
   }

   public BindingTemplates getBindingTemplates() {
      return this.m_bindingTemplates;
   }

   public void addCategory(KeyedReference var1) throws UDDIException {
      if (this.m_categories == null) {
         this.m_categories = new CategoryBag();
      }

      this.m_categories.add(var1);
   }

   public void setCategoryBag(CategoryBag var1) {
      this.m_categories = var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BusinessService)) {
         return false;
      } else {
         BusinessService var2 = (BusinessService)var1;
         boolean var3 = true;
         var3 &= this.m_bindingTemplates == null ? var2.m_bindingTemplates == null : this.m_bindingTemplates.hasEqualContent(var2.m_bindingTemplates);
         var3 &= Util.isEqual((Object)this.m_businessKey, (Object)var2.m_businessKey);
         var3 &= Util.isEqual((Object)this.m_categories, (Object)var2.m_categories);
         var3 &= this.m_descriptions == null ? var2.m_descriptions == null : this.m_descriptions.hasEqualContent(var2.m_descriptions);
         var3 &= this.m_names == null ? var2.m_names == null : this.m_names.hasEqualContent(var2.m_names);
         var3 &= Util.isEqual((Object)this.m_serviceKey, (Object)var2.m_serviceKey);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<businessService");
      if (this.m_serviceKey != null) {
         var1.append(" serviceKey=\"").append(this.m_serviceKey.toString()).append("\"");
      } else {
         var1.append(" serviceKey=\"\"");
      }

      if (this.m_businessKey != null) {
         var1.append(" businessKey=\"").append(this.m_businessKey.toString()).append("\"");
      }

      var1.append(">");
      if (this.m_names != null) {
         var1.append(this.m_names.toXML());
      }

      if (this.m_descriptions != null) {
         var1.append(this.m_descriptions.toXML());
      }

      if (this.m_bindingTemplates != null) {
         var1.append(this.m_bindingTemplates.toXML());
      } else {
         var1.append("<").append("bindingTemplates").append(" />");
      }

      if (this.m_categories != null) {
         var1.append(this.m_categories.toXML());
      }

      var1.append("</businessService>");
      return var1.toString();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("sk:").append(this.m_serviceKey);
      var1.append(", bk:").append(this.m_businessKey);
      if (this.m_bindingTemplates != null) {
         var1.append("\n     bt size:").append(this.m_bindingTemplates.size());

         for(int var2 = 0; var2 < this.m_bindingTemplates.size(); ++var2) {
            BindingTemplate var3 = (BindingTemplate)this.m_bindingTemplates.get(var2);
            var1.append("\n     ").append(var3.toString());
         }
      } else {
         var1.append("\n     bt: null");
      }

      return var1.toString();
   }
}
