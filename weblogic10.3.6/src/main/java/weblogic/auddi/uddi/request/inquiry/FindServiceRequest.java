package weblogic.auddi.uddi.request.inquiry;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessKey;
import weblogic.auddi.uddi.datastructure.CategoryBag;
import weblogic.auddi.uddi.datastructure.KeyedReference;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.SearchNames;
import weblogic.auddi.uddi.datastructure.TModelBag;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.request.UDDIRequest;

public class FindServiceRequest extends UDDIRequest {
   private BusinessKey businessKey = null;
   private FindQualifiers findQualifiers = null;
   private SearchNames names = null;
   private CategoryBag categoryBag = null;
   private TModelBag tModelBag = null;

   public void setCategoryBag(CategoryBag var1) {
      this.categoryBag = var1;
   }

   public CategoryBag getCategoryBag() {
      return this.categoryBag;
   }

   public TModelBag getTModelBag() {
      return this.tModelBag;
   }

   public SearchNames getNames() {
      return this.names;
   }

   public BusinessKey getBusinessKey() {
      return this.businessKey;
   }

   public void setTModelBag(TModelBag var1) {
      this.tModelBag = var1;
   }

   public void setBusinessKey(BusinessKey var1) {
      this.businessKey = var1;
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

   public void addCategory(KeyedReference var1) throws UDDIException {
      if (this.categoryBag == null) {
         this.categoryBag = new CategoryBag();
      }

      this.categoryBag.add(var1);
   }

   public void addTModel(TModelKey var1) throws UDDIException {
      if (this.tModelBag == null) {
         this.tModelBag = new TModelBag();
      }

      this.tModelBag.add(var1);
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<find_service");
      if (this.businessKey != null) {
         var1.append(" businessKey=\"" + this.businessKey.toString() + "\"");
      } else {
         var1.append(" businessKey=\"\"");
      }

      var1.append(" " + super.toXML() + ">\n");
      if (this.findQualifiers != null) {
         var1.append(this.findQualifiers.toXML());
      }

      if (this.names != null) {
         var1.append(this.names.toXML());
      }

      if (this.categoryBag != null) {
         var1.append(this.categoryBag.toXML());
      }

      if (this.tModelBag != null) {
         var1.append(this.tModelBag.toXML());
      }

      var1.append("</find_service>\n");
      return var1.toString();
   }
}
