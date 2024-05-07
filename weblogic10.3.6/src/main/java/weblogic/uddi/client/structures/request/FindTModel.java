package weblogic.uddi.client.structures.request;

import weblogic.uddi.client.structures.datatypes.CategoryBag;
import weblogic.uddi.client.structures.datatypes.IdentifierBag;
import weblogic.uddi.client.structures.datatypes.Name;

public class FindTModel extends FindRequest {
   private Name name = null;
   private IdentifierBag identifierBag = null;
   private CategoryBag categoryBag = null;

   public void setName(Name var1) {
      this.identifierBag = null;
      this.categoryBag = null;
      this.name = var1;
   }

   public Name getName() {
      return this.name;
   }

   public void setIdentifierBag(IdentifierBag var1) {
      this.name = null;
      this.categoryBag = null;
      this.identifierBag = var1;
   }

   public IdentifierBag getIdentifierBag() {
      return this.identifierBag;
   }

   public void setCategoryBag(CategoryBag var1) {
      this.name = null;
      this.identifierBag = null;
      this.categoryBag = var1;
   }

   public CategoryBag getCategoryBag() {
      return this.categoryBag;
   }
}
