package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class BusinessService {
   private Name name = null;
   private Vector description = new Vector();
   private BindingTemplates bindingTemplates = null;
   private CategoryBag categoryBag = null;
   private String serviceKey = null;
   private String businessKey = null;

   public void setName(Name var1) {
      this.name = var1;
   }

   public void setName(String var1) {
      this.name = new Name(var1);
   }

   public Name getName() {
      return this.name;
   }

   public Vector getDescriptionVector() {
      return this.description;
   }

   public void setDescriptionVector(Vector var1) {
      this.description = var1;
   }

   public void addDescription(String var1) {
      this.description.add(new Description(var1));
   }

   public void setBindingTemplates(BindingTemplates var1) {
      this.bindingTemplates = var1;
   }

   public BindingTemplates getBindingTemplates() {
      return this.bindingTemplates;
   }

   public void setCategoryBag(CategoryBag var1) {
      this.categoryBag = var1;
   }

   public CategoryBag getCategoryBag() {
      return this.categoryBag;
   }

   public void setServiceKey(String var1) {
      this.serviceKey = var1;
   }

   public String getServiceKey() {
      return this.serviceKey;
   }

   public void setBusinessKey(String var1) {
      this.businessKey = var1;
   }

   public String getBusinessKey() {
      return this.businessKey;
   }
}
