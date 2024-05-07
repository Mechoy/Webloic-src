package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class Contacts {
   private Vector contactVector = new Vector();

   public Vector getContactVector() {
      return this.contactVector;
   }

   public void setContactVector(Vector var1) {
      this.contactVector = var1;
   }

   public void addContact(Contact var1) {
      this.contactVector.add(var1);
   }
}
