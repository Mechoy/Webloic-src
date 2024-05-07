package weblogic.auddi.uddi.request.inquiry;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.CategoryBag;
import weblogic.auddi.uddi.datastructure.DiscoveryURL;
import weblogic.auddi.uddi.datastructure.DiscoveryURLs;
import weblogic.auddi.uddi.datastructure.IdentifierBag;
import weblogic.auddi.uddi.datastructure.KeyedReference;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.SearchNames;
import weblogic.auddi.uddi.datastructure.TModelBag;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.request.UDDIRequest;

public class FindBusinessRequest extends UDDIRequest {
   private FindQualifiers findQualifiers = null;
   private SearchNames names = null;
   private IdentifierBag identifierBag = null;
   private CategoryBag categoryBag = null;
   private TModelBag tModelBag = null;
   private DiscoveryURLs discoveryURLs = null;

   public void addFindQualifiers(FindQualifiers var1) {
      this.findQualifiers = var1;
   }

   public void addFindQualifier(FindQualifier var1) throws UDDIException {
      if (this.findQualifiers == null) {
         this.findQualifiers = new FindQualifiers();
      }

      this.findQualifiers.add(var1);
   }

   public void setFindQualifiers(FindQualifiers var1) {
      this.findQualifiers = var1;
   }

   public FindQualifiers getFindQualifiers() {
      return this.findQualifiers;
   }

   public void addName(Name var1) throws UDDIException {
      if (this.names == null) {
         this.names = new SearchNames();
      }

      this.names.add(var1);
   }

   public void setNames(SearchNames var1) {
      this.names = var1;
   }

   public SearchNames getNames() {
      return this.names;
   }

   public void addIdentifier(KeyedReference var1) throws UDDIException {
      if (this.identifierBag == null) {
         this.identifierBag = new IdentifierBag();
      }

      this.identifierBag.add(var1);
   }

   public void setIdentifierBag(IdentifierBag var1) {
      this.identifierBag = var1;
   }

   public IdentifierBag getIdentifierBag() {
      return this.identifierBag;
   }

   public void addCategory(KeyedReference var1) throws UDDIException {
      if (this.categoryBag == null) {
         this.categoryBag = new CategoryBag();
      }

      this.categoryBag.add(var1);
   }

   public void setCategoryBag(CategoryBag var1) {
      this.categoryBag = var1;
   }

   public CategoryBag getCategoryBag() {
      return this.categoryBag;
   }

   public void addTModel(TModelKey var1) throws UDDIException {
      if (this.tModelBag == null) {
         this.tModelBag = new TModelBag();
      }

      this.tModelBag.add(var1);
   }

   public TModelBag getTModelBag() {
      return this.tModelBag;
   }

   public void setTModelBag(TModelBag var1) {
      this.tModelBag = var1;
   }

   public void addDiscoveryURL(DiscoveryURL var1) throws UDDIException {
      if (this.discoveryURLs == null) {
         this.discoveryURLs = new DiscoveryURLs();
      }

      this.discoveryURLs.add(var1);
   }

   public void setDiscoveryURLs(DiscoveryURLs var1) {
      this.discoveryURLs = var1;
   }

   public DiscoveryURLs getDiscoveryURLs() {
      return this.discoveryURLs;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<find_business");
      var1.append(super.toXML() + ">\n");
      if (this.findQualifiers != null) {
         var1.append(this.findQualifiers.toXML());
      }

      if (this.names != null) {
         var1.append(this.names.toXML());
      }

      if (this.identifierBag != null) {
         var1.append(this.identifierBag.toXML());
      }

      if (this.categoryBag != null) {
         var1.append(this.categoryBag.toXML());
      }

      if (this.tModelBag != null) {
         var1.append(this.tModelBag.toXML());
      }

      if (this.discoveryURLs != null) {
         var1.append(this.discoveryURLs.toXML());
      }

      var1.append("</find_business>\n");
      return var1.toString();
   }

   public boolean isEmpty() {
      return this.names == null && this.categoryBag == null && this.identifierBag == null && this.tModelBag == null && this.discoveryURLs == null;
   }
}
