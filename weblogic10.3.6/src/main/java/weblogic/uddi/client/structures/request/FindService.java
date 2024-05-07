package weblogic.uddi.client.structures.request;

import weblogic.uddi.client.structures.datatypes.CategoryBag;
import weblogic.uddi.client.structures.datatypes.Name;
import weblogic.uddi.client.structures.datatypes.TModelBag;

public class FindService extends FindRequest {
   private String businessKey = null;
   private Name name = null;
   private CategoryBag categoryBag = null;
   private TModelBag tModelBag = null;

   public void setBusinessKey(String var1) {
      this.businessKey = var1;
   }

   public String getBusinessKey() {
      return this.businessKey;
   }

   public void setName(Name var1) {
      this.categoryBag = null;
      this.tModelBag = null;
      this.name = var1;
   }

   public Name getName() {
      return this.name;
   }

   public void setCategoryBag(CategoryBag var1) {
      this.name = null;
      this.tModelBag = null;
      this.categoryBag = var1;
   }

   public CategoryBag getCategoryBag() {
      return this.categoryBag;
   }

   public void setTModelBag(TModelBag var1) {
      this.name = null;
      this.categoryBag = null;
      this.tModelBag = var1;
   }

   public TModelBag getTModelBag() {
      return this.tModelBag;
   }
}
