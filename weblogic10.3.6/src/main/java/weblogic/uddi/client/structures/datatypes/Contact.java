package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class Contact {
   private String useType = null;
   private Vector descriptionVector = new Vector();
   private PersonName personName = null;
   private Vector phoneVector = new Vector();
   private Vector emailVector = new Vector();
   private Vector addressVector = new Vector();

   public String getUseType() {
      return this.useType;
   }

   public void setUseType(String var1) {
      this.useType = new String(var1);
   }

   public Vector getDescriptionVector() {
      return this.descriptionVector;
   }

   public void setDescriptionVector(Vector var1) {
      this.descriptionVector = var1;
   }

   public void addDescription(String var1) {
      Description var2 = new Description(var1);
      this.descriptionVector.add(var2);
   }

   public PersonName getPersonName() {
      return this.personName;
   }

   public void setPersonName(PersonName var1) {
      this.personName = var1;
   }

   public void setPersonName(String var1) {
      this.personName = new PersonName(var1);
   }

   public Vector getPhoneVector() {
      return this.phoneVector;
   }

   public void setPhoneVector(Vector var1) {
      this.phoneVector = var1;
   }

   public void addPhone(String var1) {
      Phone var2 = new Phone(var1);
      this.phoneVector.add(var2);
   }

   public Vector getEmailVector() {
      return this.emailVector;
   }

   public void setEmailVector(Vector var1) {
      this.emailVector = var1;
   }

   public void addEmail(String var1) {
      Email var2 = new Email(var1);
      this.emailVector.add(var2);
   }

   public Vector getAddressVector() {
      return this.addressVector;
   }

   public void setAddressVector(Vector var1) {
      this.addressVector = var1;
   }

   public void addAddress(Address var1) {
      this.addressVector.add(var1);
   }
}
