package weblogic.ejb.container.persistence;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.ejb.container.dd.BaseDescriptor;
import weblogic.ejb.container.persistence.spi.EjbEntityRef;
import weblogic.ejb.container.persistence.spi.EjbRelation;
import weblogic.ejb.container.persistence.spi.Relationships;
import weblogic.utils.collections.CombinedIterator;

public final class RelationshipsImpl extends BaseDescriptor implements Relationships {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   protected String[] description;
   protected Map ejbEntityRefs = new HashMap();
   protected Map ejbRelations = new HashMap();

   public RelationshipsImpl() {
      super((String)null);
   }

   public void setDescriptions(String[] var1) {
      this.description = var1;
   }

   public String[] getDescriptions() {
      return this.description;
   }

   public void addEjbEntityRef(EjbEntityRef var1) {
      this.ejbEntityRefs.put(var1.getRemoteEjbName(), var1);
   }

   public EjbEntityRef removeEjbEntityRef(String var1) {
      return (EjbEntityRef)this.ejbEntityRefs.remove(var1);
   }

   public EjbEntityRef getEjbEntityRef(String var1) {
      return (EjbEntityRef)this.ejbEntityRefs.get(var1);
   }

   public Map getAllEjbEntityRefs() {
      return this.ejbEntityRefs;
   }

   public void addEjbRelation(EjbRelation var1) {
      this.ejbRelations.put(var1.getEjbRelationName(), var1);
   }

   public EjbRelation removeEjbRelation(String var1) {
      return (EjbRelation)this.ejbRelations.remove(var1);
   }

   public EjbRelation getEjbRelation(String var1) {
      return (EjbRelation)this.ejbRelations.get(var1);
   }

   public Map getAllEjbRelations() {
      return this.ejbRelations;
   }

   public void validateSelf() {
   }

   public Iterator getSubObjectsIterator() {
      return new CombinedIterator(this.ejbEntityRefs.values().iterator(), this.ejbRelations.values().iterator());
   }
}
