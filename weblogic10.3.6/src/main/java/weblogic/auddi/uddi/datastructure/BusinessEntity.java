package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import java.util.Date;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class BusinessEntity extends UDDIListObject implements Serializable {
   private BusinessKey m_businessKey = null;
   private AuthorizedName m_authorizedName = null;
   private String m_operator = null;
   private DiscoveryURLs m_discoveryURLs = null;
   private UniqueNames m_names = null;
   private Descriptions m_descriptions = null;
   private Contacts m_contacts = null;
   private BusinessServices m_businessServices = null;
   private IdentifierBag m_identifierBag = null;
   private CategoryBag m_categoryBag = null;

   public BusinessEntity(BusinessKey var1) {
      this.m_businessKey = var1;
   }

   public BusinessEntity() {
   }

   public BusinessEntity(BusinessEntity var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         if (var1.m_businessKey != null) {
            this.m_businessKey = new BusinessKey(var1.m_businessKey);
         }

         if (var1.m_names != null) {
            this.m_names = new UniqueNames(var1.m_names);
         }

         if (var1.m_discoveryURLs != null) {
            this.m_discoveryURLs = new DiscoveryURLs(var1.m_discoveryURLs);
         }

         if (var1.m_descriptions != null) {
            this.m_descriptions = new Descriptions(var1.m_descriptions);
         }

         if (var1.m_authorizedName != null) {
            this.m_authorizedName = new AuthorizedName(var1.m_authorizedName);
         }

         if (var1.m_contacts != null) {
            this.m_contacts = new Contacts(var1.m_contacts);
         }

         if (var1.m_date != null) {
            this.m_date = new Date(var1.m_date.getTime());
         }

         if (var1.m_categoryBag != null) {
            this.m_categoryBag = new CategoryBag(var1.m_categoryBag);
         }

         if (var1.m_identifierBag != null) {
            this.m_identifierBag = new IdentifierBag(var1.m_identifierBag);
         }

         this.m_operator = var1.m_operator;
      }
   }

   public void setDescriptions(Descriptions var1) {
      this.m_descriptions = var1;
   }

   public void setNames(UniqueNames var1) {
      this.m_names = var1;
   }

   public void setAuthorizedName(String var1) {
      if (this.m_authorizedName == null) {
         this.m_authorizedName = new AuthorizedName(var1);
      } else {
         this.m_authorizedName.setName(var1);
      }

   }

   public void setAuthorizedName(AuthorizedName var1) {
      this.m_authorizedName = var1;
   }

   public void setBusinessKey(BusinessKey var1) {
      this.m_businessKey = var1;
   }

   public BusinessKey getBusinessKey() {
      return this.m_businessKey;
   }

   public Descriptions getDescriptions() {
      return this.m_descriptions;
   }

   public Contacts getContacts() {
      return this.m_contacts;
   }

   public void setOperator(String var1) {
      this.m_operator = var1;
   }

   public void setDiscoveryURLs(DiscoveryURLs var1) {
      this.m_discoveryURLs = var1;
   }

   public DiscoveryURLs getDiscoveryURLs() {
      return this.m_discoveryURLs;
   }

   public void addDiscoveryURL(DiscoveryURL var1) throws UDDIException {
      if (this.m_discoveryURLs == null) {
         this.m_discoveryURLs = new DiscoveryURLs();
      }

      this.m_discoveryURLs.add(var1);
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

   public void addDescription(Description var1) throws UDDIException {
      if (var1 != null) {
         if (this.m_descriptions == null) {
            this.m_descriptions = new Descriptions();
         }

         this.m_descriptions.add(var1);
      }
   }

   public void setContacts(Contacts var1) {
      this.m_contacts = var1;
   }

   public void addContact(Contact var1) throws UDDIException {
      if (this.m_contacts == null) {
         this.m_contacts = new Contacts();
      }

      this.m_contacts.add(var1);
   }

   public void setBusinessServices(BusinessServices var1) {
      this.m_businessServices = var1;
   }

   public void addBusinessService(BusinessService var1) throws UDDIException {
      if (this.m_businessServices == null) {
         this.m_businessServices = new BusinessServices();
      }

      this.m_businessServices.add(var1);
   }

   public BusinessServices getBusinessServices() {
      return this.m_businessServices;
   }

   public void setIdentifierBag(IdentifierBag var1) {
      this.m_identifierBag = var1;
   }

   public void addIdentifier(KeyedReference var1) throws UDDIException {
      if (this.m_identifierBag == null) {
         this.m_identifierBag = new IdentifierBag();
      }

      this.m_identifierBag.add(var1);
   }

   public void setCategoryBag(CategoryBag var1) {
      this.m_categoryBag = var1;
   }

   public CategoryBag getCategoryBag() {
      return this.m_categoryBag;
   }

   public IdentifierBag getIdentifierBag() {
      return this.m_identifierBag;
   }

   public AuthorizedName getAuthorizedName() {
      return this.m_authorizedName;
   }

   public String getOperator() {
      return this.m_operator;
   }

   public void addCategory(KeyedReference var1) throws UDDIException {
      if (this.m_categoryBag == null) {
         this.m_categoryBag = new CategoryBag();
      }

      this.m_categoryBag.add(var1);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BusinessEntity)) {
         return false;
      } else {
         BusinessEntity var2 = (BusinessEntity)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_businessKey, (Object)var2.m_businessKey);
         var3 &= this.m_businessServices != null && this.m_businessServices.size() != 0 ? this.m_businessServices.hasEqualContent(var2.m_businessServices) : var2.m_businessServices == null || var2.m_businessServices.size() == 0;
         var3 &= Util.isEqual((Object)this.m_categoryBag, (Object)var2.m_categoryBag);
         var3 &= this.m_contacts == null ? var2.m_contacts == null : this.m_contacts.hasEqualContent(var2.m_contacts);
         var3 &= this.m_descriptions == null ? var2.m_descriptions == null : this.m_descriptions.hasEqualContent(var2.m_descriptions);
         var3 &= this.m_discoveryURLs == null ? var2.m_discoveryURLs == null : this.m_discoveryURLs.hasEqualContent(var2.m_discoveryURLs);
         var3 &= Util.isEqual((Object)this.m_identifierBag, (Object)var2.m_identifierBag);
         var3 &= this.m_names == null ? var2.m_names == null : this.m_names.hasEqualContent(var2.m_names);
         var3 &= Util.isEqual((Object)this.m_authorizedName, (Object)var2.m_authorizedName);
         var3 &= Util.isEqual((Object)this.m_operator, (Object)var2.getOperator());
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<businessEntity");
      if (this.m_businessKey != null) {
         var1.append(" businessKey=\"").append(this.m_businessKey.toString()).append("\"");
      } else {
         var1.append(" businessKey=\"\"");
      }

      if (this.m_authorizedName != null) {
         var1.append(" authorizedName=\"").append(this.m_authorizedName.toString()).append("\"");
      }

      if (this.m_operator != null) {
         var1.append(" operator=\"").append(this.m_operator).append("\"");
      }

      var1.append(">");
      if (this.m_discoveryURLs != null) {
         var1.append(this.m_discoveryURLs.toXML());
      }

      if (this.m_names != null) {
         var1.append(this.m_names.toXML(""));
      }

      if (this.m_descriptions != null) {
         var1.append(this.m_descriptions.toXML());
      }

      if (this.m_contacts != null) {
         var1.append(this.m_contacts.toXML());
      }

      if (this.m_businessServices != null) {
         var1.append(this.m_businessServices.toXML());
      }

      if (this.m_identifierBag != null) {
         var1.append(this.m_identifierBag.toXML());
      }

      if (this.m_categoryBag != null) {
         var1.append(this.m_categoryBag.toXML());
      }

      var1.append("</businessEntity>");
      return var1.toString();
   }
}
