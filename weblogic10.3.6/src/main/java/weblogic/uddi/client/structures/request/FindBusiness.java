package weblogic.uddi.client.structures.request;

import weblogic.uddi.client.structures.datatypes.CategoryBag;
import weblogic.uddi.client.structures.datatypes.DiscoveryURLs;
import weblogic.uddi.client.structures.datatypes.IdentifierBag;
import weblogic.uddi.client.structures.datatypes.Name;
import weblogic.uddi.client.structures.datatypes.TModelBag;

public class FindBusiness extends FindRequest {
   private Name name = null;
   private IdentifierBag identifierBag = null;
   private CategoryBag categoryBag = null;
   private TModelBag tModelBag = null;
   private DiscoveryURLs discoveryURLs = null;

   public void setName(Name var1) {
      this.identifierBag = null;
      this.categoryBag = null;
      this.tModelBag = null;
      this.discoveryURLs = null;
      this.name = var1;
   }

   public Name getName() {
      return this.name;
   }

   public void setIdentifierBag(IdentifierBag var1) {
      this.name = null;
      this.categoryBag = null;
      this.tModelBag = null;
      this.discoveryURLs = null;
      this.identifierBag = var1;
   }

   public IdentifierBag getIdentifierBag() {
      return this.identifierBag;
   }

   public void setCategoryBag(CategoryBag var1) {
      this.name = null;
      this.identifierBag = null;
      this.tModelBag = null;
      this.discoveryURLs = null;
      this.categoryBag = var1;
   }

   public CategoryBag getCategoryBag() {
      return this.categoryBag;
   }

   public void setTModelBag(TModelBag var1) {
      this.name = null;
      this.identifierBag = null;
      this.categoryBag = null;
      this.discoveryURLs = null;
      this.tModelBag = var1;
   }

   public TModelBag getTModelBag() {
      return this.tModelBag;
   }

   public void setDiscoveryURLs(DiscoveryURLs var1) {
      this.name = null;
      this.identifierBag = null;
      this.categoryBag = null;
      this.tModelBag = null;
      this.discoveryURLs = var1;
   }

   public DiscoveryURLs getDiscoveryURLs() {
      return this.discoveryURLs;
   }
}
