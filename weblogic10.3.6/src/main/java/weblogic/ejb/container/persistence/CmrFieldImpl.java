package weblogic.ejb.container.persistence;

import java.util.Iterator;
import weblogic.ejb.container.dd.BaseDescriptor;
import weblogic.ejb.container.persistence.spi.CmrField;
import weblogic.utils.collections.Iterators;

public final class CmrFieldImpl extends BaseDescriptor implements CmrField {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   protected String[] description;
   protected String cmrFieldName;
   protected String cmrFieldType;

   public CmrFieldImpl() {
      super((String)null);
   }

   public void setDescriptions(String[] var1) {
      this.description = var1;
   }

   public String[] getDescriptions() {
      return this.description;
   }

   public void setCmrFieldName(String var1) {
      this.cmrFieldName = var1;
   }

   public String getName() {
      return this.cmrFieldName;
   }

   public void setCmrFieldType(String var1) {
      this.cmrFieldType = var1;
   }

   public String getType() {
      return this.cmrFieldType;
   }

   public void validateSelf() {
   }

   public Iterator getSubObjectsIterator() {
      return Iterators.EMPTY_ITERATOR;
   }

   public String toString() {
      return "[CmrFieldImpl description: " + this.description + " " + "name: " + this.cmrFieldName + " " + "type: " + this.cmrFieldType + "]";
   }
}
