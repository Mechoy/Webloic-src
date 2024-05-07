package weblogic.ldap;

import com.octetstring.vde.EntryChanges;
import com.octetstring.vde.EntryChangesListener;
import com.octetstring.vde.syntax.DirectoryString;

public class EntryChangesListenerImpl implements EntryChangesListener {
   EmbeddedLDAPChangeListener listener;
   DirectoryString eclBase;

   public EntryChangesListenerImpl(String var1, EmbeddedLDAPChangeListener var2) {
      this.listener = var2;
      this.eclBase = new DirectoryString(var1);
   }

   public DirectoryString getECLBase() {
      return this.eclBase;
   }

   public void receiveEntryChanges(EntryChanges var1) {
      this.listener.entryChanged(new EmbeddedLDAPChange(var1));
   }
}
