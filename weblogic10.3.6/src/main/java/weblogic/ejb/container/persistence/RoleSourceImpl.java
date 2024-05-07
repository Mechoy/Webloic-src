package weblogic.ejb.container.persistence;

import java.util.Iterator;
import weblogic.ejb.container.dd.BaseDescriptor;
import weblogic.ejb.container.persistence.spi.RoleSource;
import weblogic.utils.collections.Iterators;

public final class RoleSourceImpl extends BaseDescriptor implements RoleSource {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   protected String[] description;
   protected String ejbName;

   public RoleSourceImpl() {
      super((String)null);
   }

   public void setDescriptions(String[] var1) {
      this.description = var1;
   }

   public String[] getDescriptions() {
      return this.description;
   }

   public void setEjbName(String var1) {
      this.ejbName = var1;
   }

   public String getEjbName() {
      return this.ejbName;
   }

   public void validateSelf() {
   }

   public Iterator getSubObjectsIterator() {
      return Iterators.EMPTY_ITERATOR;
   }

   public String toString() {
      return "[RoleSourceImpl ejbName: " + this.ejbName + " " + "description: " + this.description + "]";
   }
}
