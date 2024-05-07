package weblogic.ldap;

import java.util.Vector;
import netscape.ldap.LDAPControl;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPEntryComparator;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;

public class EmbeddedLDAPSearchResults extends LDAPSearchResults {
   private Vector entries = null;
   private boolean searchComplete = false;
   private boolean persistentSearch = false;
   private Vector exceptions;
   private boolean firstResult = false;

   public EmbeddedLDAPSearchResults() {
      this.entries = new Vector();
      this.searchComplete = true;
   }

   void add(EmbeddedLDAPSearchResult var1) {
      this.entries.addElement(var1.getEntry());
   }

   public LDAPControl[] getResponseControls() {
      return null;
   }

   public synchronized void sort(LDAPEntryComparator var1) {
   }

   public LDAPEntry next() throws LDAPException {
      Object var1 = this.nextElement();
      if (var1 instanceof LDAPException) {
         throw (LDAPException)var1;
      } else {
         return var1 instanceof LDAPEntry ? (LDAPEntry)var1 : null;
      }
   }

   public Object nextElement() {
      Object var1;
      if (this.entries.size() > 0) {
         var1 = this.entries.elementAt(0);
         this.entries.removeElementAt(0);
         return var1;
      } else if (this.exceptions != null && this.exceptions.size() > 0) {
         var1 = this.exceptions.elementAt(0);
         this.exceptions.removeElementAt(0);
         return var1;
      } else {
         return null;
      }
   }

   public boolean hasMoreElements() {
      return this.entries.size() > 0 || this.exceptions != null && this.exceptions.size() > 0;
   }

   public int getCount() {
      int var1 = this.entries.size();
      if (this.exceptions != null) {
         var1 += this.exceptions.size();
      }

      return var1;
   }
}
