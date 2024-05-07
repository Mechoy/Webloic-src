package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class TModel {
   private Name name = null;
   private Vector description = new Vector();
   private OverviewDoc overviewDoc = null;
   private IdentifierBag identifierBag = null;
   private CategoryBag categoryBag = null;
   private String tModelKey = null;
   private String operator = null;
   private String authorizedName = null;

   public void setTModelKey(String var1) {
      this.tModelKey = var1;
   }

   public void setOperator(String var1) {
      this.operator = var1;
   }

   public void setAuthorizedName(String var1) {
      this.authorizedName = var1;
   }

   public String getTModelKey() {
      return this.tModelKey;
   }

   public String getOperator() {
      return this.operator;
   }

   public String getAuthorizedName() {
      return this.authorizedName;
   }

   public void setName(String var1) {
      this.name = new Name(var1);
   }

   public void setName(Name var1) {
      this.name = var1;
   }

   public Name getName() {
      return this.name;
   }

   public void addDescription(String var1) {
      Description var2 = new Description(var1);
      this.description.add(var2);
   }

   public Vector getDescriptionVector() {
      return this.description;
   }

   public void setDescriptionVector(Vector var1) {
      this.description = var1;
   }

   public void setOverviewDoc(OverviewDoc var1) {
      this.overviewDoc = var1;
   }

   public OverviewDoc getOverviewDoc() {
      return this.overviewDoc;
   }

   public void setCategoryBag(CategoryBag var1) {
      this.categoryBag = var1;
   }

   public CategoryBag getCategoryBag() {
      return this.categoryBag;
   }

   public IdentifierBag getIdentifierBag() {
      return this.identifierBag;
   }

   public void setIdentifierBag(IdentifierBag var1) {
      this.identifierBag = var1;
   }
}
