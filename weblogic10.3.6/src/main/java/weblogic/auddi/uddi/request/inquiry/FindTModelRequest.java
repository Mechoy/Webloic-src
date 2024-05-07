package weblogic.auddi.uddi.request.inquiry;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.CategoryBag;
import weblogic.auddi.uddi.datastructure.IdentifierBag;
import weblogic.auddi.uddi.datastructure.KeyedReference;
import weblogic.auddi.uddi.datastructure.Name;
import weblogic.auddi.uddi.datastructure.Names;
import weblogic.auddi.uddi.request.UDDIRequest;

public class FindTModelRequest extends UDDIRequest {
   private FindQualifiers findQualifiers = null;
   private Names names = null;
   private IdentifierBag identifierBag = null;
   private CategoryBag categoryBag = null;

   public void addFindQualifier(FindQualifier var1) throws UDDIException {
      if (this.findQualifiers == null) {
         this.findQualifiers = new FindQualifiers();
      }

      this.findQualifiers.add(var1);
   }

   public void setFindQualifiers(FindQualifiers var1) {
      this.findQualifiers = var1;
   }

   public void setNames(Names var1) {
      this.names = var1;
   }

   public FindQualifiers getFindQualifiers() {
      return this.findQualifiers;
   }

   public void setIdentifierBag(IdentifierBag var1) {
      this.identifierBag = var1;
   }

   public IdentifierBag getIdentifierBag() {
      return this.identifierBag;
   }

   public Names getNames() {
      return this.names;
   }

   public void setCategoryBag(CategoryBag var1) {
      this.categoryBag = var1;
   }

   public CategoryBag getCategoryBag() {
      return this.categoryBag;
   }

   public void addName(Name var1) throws UDDIException {
      if (this.names == null) {
         this.names = new Names();
      }

      this.names.add(var1);
   }

   public void addCategory(KeyedReference var1) throws UDDIException {
      if (this.categoryBag == null) {
         this.categoryBag = new CategoryBag();
      }

      this.categoryBag.add(var1);
   }

   public void addIdentifier(KeyedReference var1) throws UDDIException {
      if (this.identifierBag == null) {
         this.identifierBag = new IdentifierBag();
      }

      this.identifierBag.add(var1);
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<find_tModel" + super.toXML() + ">\n");
      if (this.findQualifiers != null) {
         var1.append(this.findQualifiers.toXML());
      }

      if (this.names != null) {
         var1.append(this.names.toXML());
      }

      if (this.categoryBag != null) {
         var1.append(this.categoryBag.toXML());
      }

      if (this.identifierBag != null) {
         var1.append(this.identifierBag.toXML());
      }

      var1.append("</find_tModel>\n");
      return var1.toString();
   }
}
