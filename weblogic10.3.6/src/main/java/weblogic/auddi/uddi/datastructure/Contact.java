package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class Contact extends UDDIListObject {
   private String m_useType = null;
   private Descriptions m_descriptions = null;
   private String m_personName = null;
   private Phones m_phones = null;
   private Emails m_emails = null;
   private Addresses m_addresses = null;

   public Contact() {
   }

   public Contact(Contact var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         this.m_useType = var1.m_useType;
         this.m_personName = var1.m_personName;
         if (var1.m_descriptions != null) {
            this.m_descriptions = new Descriptions(var1.m_descriptions);
         }

         if (var1.m_phones != null) {
            this.m_phones = new Phones(var1.m_phones);
         }

         if (var1.m_emails != null) {
            this.m_emails = new Emails(var1.m_emails);
         }

         if (var1.m_addresses != null) {
            this.m_addresses = new Addresses(var1.m_addresses);
         }

      }
   }

   public Contact(String var1) {
      this.setPersonName(var1);
   }

   public void setDescriptions(Descriptions var1) {
      this.m_descriptions = var1;
   }

   public Descriptions getDescriptions() {
      return this.m_descriptions;
   }

   public String getPersonName() {
      return this.m_personName;
   }

   public void setPersonName(String var1) {
      this.m_personName = this.truncateString(var1, 255);
   }

   public void setAddresses(Addresses var1) {
      this.m_addresses = var1;
   }

   public Addresses getAddresses() {
      return this.m_addresses;
   }

   public String getUseType() {
      return this.m_useType;
   }

   public void setUseType(String var1) {
      this.m_useType = this.truncateString(var1, 255);
   }

   public void setEmails(Emails var1) {
      this.m_emails = var1;
   }

   public Emails getEmails() {
      return this.m_emails;
   }

   public void setPhones(Phones var1) {
      this.m_phones = var1;
   }

   public Phones getPhones() {
      return this.m_phones;
   }

   public void addAddress(Address var1) throws UDDIException {
      if (this.m_addresses == null) {
         this.m_addresses = new Addresses();
      }

      this.m_addresses.add(var1);
   }

   public void addDescription(Description var1) throws UDDIException {
      if (var1 != null) {
         if (this.m_descriptions == null) {
            this.m_descriptions = new Descriptions();
         }

         this.m_descriptions.add(var1);
      }
   }

   public void addPhone(Phone var1) throws UDDIException {
      if (this.m_phones == null) {
         this.m_phones = new Phones();
      }

      this.m_phones.add(var1);
   }

   public void addEmail(Email var1) throws UDDIException {
      if (this.m_emails == null) {
         this.m_emails = new Emails();
      }

      this.m_emails.add(var1);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Contact)) {
         return false;
      } else {
         Contact var2 = (Contact)var1;
         boolean var3 = true;
         var3 &= this.m_addresses == null ? var2.m_addresses == null : this.m_addresses.hasEqualContent(var2.m_addresses);
         var3 &= this.m_descriptions == null ? var2.m_descriptions == null : this.m_descriptions.hasEqualContent(var2.m_descriptions);
         var3 &= this.m_emails == null ? var2.m_emails == null : this.m_emails.hasEqualContent(var2.m_emails);
         var3 &= Util.isEqual((Object)this.m_personName, (Object)var2.m_personName);
         var3 &= this.m_phones == null ? var2.m_phones == null : this.m_phones.hasEqualContent(var2.m_phones);
         var3 &= Util.isEqual((Object)this.m_useType, (Object)var2.m_useType);
         return var3;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.m_useType != null) {
         var1.append("   useType : " + this.m_useType + "\n");
      }

      if (this.m_personName != null) {
         var1.append(" personName : " + this.m_personName + "\n");
      }

      if (this.m_descriptions != null) {
         var1.append(this.m_descriptions.toString());
      }

      if (this.m_addresses != null) {
         var1.append(this.m_addresses.toString());
      }

      if (this.m_phones != null) {
         var1.append(this.m_phones.toString());
      }

      if (this.m_emails != null) {
         var1.append(this.m_emails.toString());
      }

      return var1.toString();
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<contact");
      if (this.m_useType != null) {
         var1.append(" useType=\"").append(this.m_useType).append("\" ");
      }

      var1.append(">");
      if (this.m_descriptions != null) {
         var1.append(this.m_descriptions.toXML());
      }

      if (this.m_personName != null) {
         var1.append("<personName>").append(this.fixStringForXML(this.m_personName)).append("</personName>");
      }

      if (this.m_phones != null) {
         var1.append(this.m_phones.toXML());
      }

      if (this.m_emails != null) {
         var1.append(this.m_emails.toXML());
      }

      if (this.m_addresses != null) {
         var1.append(this.m_addresses.toXML());
      }

      var1.append("</contact>");
      return var1.toString();
   }
}
