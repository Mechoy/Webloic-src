package weblogic.ejb.container.persistence;

import java.util.Iterator;
import weblogic.ejb.container.dd.BaseDescriptor;
import weblogic.ejb.container.persistence.spi.CmrField;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;
import weblogic.ejb.container.persistence.spi.RoleSource;
import weblogic.utils.collections.Iterators;

public final class EjbRelationshipRoleImpl extends BaseDescriptor implements EjbRelationshipRole {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   protected String[] description;
   protected String ejbRelationshipRoleName;
   protected String multiplicity;
   protected boolean cascadeDelete;
   protected RoleSource roleSource;
   protected CmrField cmrField;

   public EjbRelationshipRoleImpl() {
      super((String)null);
   }

   public void setDescriptions(String[] var1) {
      this.description = var1;
   }

   public String[] getDescriptions() {
      return this.description;
   }

   public void setName(String var1) {
      this.ejbRelationshipRoleName = var1;
   }

   public String getName() {
      return this.ejbRelationshipRoleName;
   }

   public void setMultiplicity(String var1) {
      this.multiplicity = var1;
   }

   public String getMultiplicity() {
      return this.multiplicity;
   }

   public void setCascadeDelete(boolean var1) {
      this.cascadeDelete = var1;
   }

   public boolean getCascadeDelete() {
      return this.cascadeDelete;
   }

   public void setRoleSource(RoleSource var1) {
      this.roleSource = var1;
   }

   public RoleSource getRoleSource() {
      return this.roleSource;
   }

   public void setCmrField(CmrField var1) {
      this.cmrField = var1;
   }

   public CmrField getCmrField() {
      return this.cmrField;
   }

   public void validateSelf() {
   }

   public Iterator getSubObjectsIterator() {
      return Iterators.EMPTY_ITERATOR;
   }

   public String toString() {
      return "[EjbRelationshipRoleImpl name: " + this.ejbRelationshipRoleName + " " + "description: " + this.description + " " + "multiplicity: " + this.multiplicity + " " + (this.roleSource == null ? "[RoleSource null]" : this.roleSource.toString()) + (this.cmrField == null ? "[CmrField null]" : this.cmrField.toString()) + "]";
   }
}
