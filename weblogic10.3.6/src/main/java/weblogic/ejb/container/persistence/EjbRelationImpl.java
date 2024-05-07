package weblogic.ejb.container.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.ejb.container.dd.BaseDescriptor;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;

public final class EjbRelationImpl extends BaseDescriptor implements EjbRelation {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   protected String[] description;
   protected String ejbRelationName;
   protected String tableName;
   protected Map ejbRelationshipRoles = new HashMap();

   public EjbRelationImpl() {
      super((String)null);
   }

   public void setDescriptions(String[] var1) {
      this.description = var1;
   }

   public String[] getDescriptions() {
      return this.description;
   }

   public void setTableName(String var1) {
      this.tableName = var1;
   }

   public String getTableName() {
      return this.tableName;
   }

   public void setEjbRelationName(String var1) {
      this.ejbRelationName = var1;
   }

   public String getEjbRelationName() {
      return this.ejbRelationName;
   }

   public void addEjbRelationshipRole(EjbRelationshipRole var1) {
      this.ejbRelationshipRoles.put(var1.getName(), var1);
   }

   public EjbRelationshipRole removeEjbRelationshipRole(String var1) {
      return (EjbRelationshipRole)this.ejbRelationshipRoles.remove(var1);
   }

   public EjbRelationshipRole getEjbRelationshipRole(String var1) {
      return (EjbRelationshipRole)this.ejbRelationshipRoles.get(var1);
   }

   public Collection getAllEjbRelationshipRoles() {
      return this.ejbRelationshipRoles.values();
   }

   public void validateSelf() {
   }

   public Iterator getSubObjectsIterator() {
      return this.ejbRelationshipRoles.values().iterator();
   }
}
