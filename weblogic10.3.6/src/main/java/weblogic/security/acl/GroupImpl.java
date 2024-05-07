package weblogic.security.acl;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import weblogic.utils.enumerations.SequencingEnumerator;

/** @deprecated */
public class GroupImpl extends User implements Group {
   private Hashtable individuals = new Hashtable();
   private Vector subgroups = new Vector();

   public GroupImpl(String var1) {
      super(var1);
   }

   public boolean addMember(Principal var1) {
      if (this.getName().equals(var1.toString())) {
         throw new IllegalArgumentException("Group " + this.getName() + " can't be added to itself");
      } else if (this.isMember(var1)) {
         return false;
      } else {
         if (var1 instanceof Group) {
            this.subgroups.addElement(var1);
         } else {
            this.individuals.put(var1, var1);
         }

         return true;
      }
   }

   public boolean removeMember(Principal var1) {
      if (var1 instanceof Group) {
         return this.subgroups.removeElement(var1);
      } else {
         return this.individuals.remove(var1) != null;
      }
   }

   public Enumeration members() {
      Enumeration[] var1 = new Enumeration[]{this.individuals.keys(), this.subgroups.elements()};
      return new SequencingEnumerator(var1);
   }

   public boolean isMember(Principal var1) {
      if (this.individuals.get(var1) != null) {
         return true;
      } else {
         Enumeration var2 = this.subgroups.elements();

         Group var3;
         do {
            if (!var2.hasMoreElements()) {
               return false;
            }

            var3 = (Group)var2.nextElement();
         } while(!var3.equals(var1) && !var3.isMember(var1));

         return true;
      }
   }
}
