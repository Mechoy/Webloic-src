package weblogic.ldap;

import com.octetstring.vde.Entry;
import com.octetstring.vde.EntryChanges;
import com.octetstring.vde.syntax.DirectoryString;

public class EmbeddedLDAPChange {
   public static final int CHANGE_ADD = 1;
   public static final int CHANGE_MODIFY = 2;
   public static final int CHANGE_DELETE = 3;
   public static final int CHANGE_RENAME = 4;
   private EntryChanges changes;

   public EmbeddedLDAPChange(EntryChanges var1) {
      this.changes = var1;
   }

   public String getEntryName() {
      DirectoryString var1 = this.changes.getName();
      if (var1 == null) {
         Entry var2 = this.changes.getFullEntry();
         if (var2 != null) {
            var1 = var2.getName();
         }
      }

      return var1 == null ? null : var1.getDirectoryString();
   }

   public int getChangeType() {
      return this.changes.getChangeType();
   }
}
