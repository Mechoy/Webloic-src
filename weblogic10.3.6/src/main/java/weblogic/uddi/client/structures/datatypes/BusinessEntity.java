package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class BusinessEntity {
   private DiscoveryURLs discoveryURLs = null;
   private Name name = null;
   private Vector description = new Vector();
   private Contacts contacts = null;
   private BusinessServices businessServices = null;
   private IdentifierBag identifierBag = null;
   private CategoryBag categoryBag = null;
   private String businessKey = null;
   private String operator = null;
   private String authorizedName = null;

   public void setDiscoveryURLs(DiscoveryURLs var1) {
      this.discoveryURLs = var1;
   }

   public DiscoveryURLs getDiscoveryURLs() {
      return this.discoveryURLs;
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
      this.description.add(new Description(var1));
   }

   public void setDescriptionVector(Vector var1) {
      this.description = var1;
   }

   public Vector getDescriptionVector() {
      return this.description;
   }

   public void setContacts(Contacts var1) {
      this.contacts = var1;
   }

   public Contacts getContacts() {
      return this.contacts;
   }

   public void setBusinessServices(BusinessServices var1) {
      this.businessServices = var1;
   }

   public BusinessServices getBusinessServices() {
      return this.businessServices;
   }

   public void setIdentifierBag(IdentifierBag var1) {
      this.identifierBag = var1;
   }

   public IdentifierBag getIdentifierBag() {
      return this.identifierBag;
   }

   public void setCategoryBag(CategoryBag var1) {
      this.categoryBag = var1;
   }

   public CategoryBag getCategoryBag() {
      return this.categoryBag;
   }

   public void setBusinessKey(String var1) {
      this.businessKey = var1;
   }

   public String getBusinessKey() {
      return this.businessKey;
   }

   public void setOperator(String var1) {
      this.operator = var1;
   }

   public String getOperator() {
      return this.operator;
   }

   public void setAuthorizedName(String var1) {
      this.authorizedName = var1;
   }

   public String getAuthorizedName() {
      return this.authorizedName;
   }
}
