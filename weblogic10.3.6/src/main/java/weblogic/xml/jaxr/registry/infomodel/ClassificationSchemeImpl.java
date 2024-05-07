package weblogic.xml.jaxr.registry.infomodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.util.JAXRUtil;

public class ClassificationSchemeImpl extends RegistryEntryImpl implements ClassificationScheme {
   private static final long serialVersionUID = -1L;
   private ArrayList m_children = new ArrayList();

   public ClassificationSchemeImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
      var1.getRegistryProxy().setRegistryObjectOwner(this, var1.getCurrentUser());
   }

   public ClassificationSchemeImpl(ClassificationScheme var1, RegistryServiceImpl var2) throws JAXRException {
      super(var1, var2);
      var2.getRegistryProxy().setRegistryObjectOwner(this, var2.getCurrentUser());
      JAXRUtil.verifyNotNull(var1, ClassificationScheme.class);
      Collection var3 = var1.getChildrenConcepts();
      this.addChildConcepts(var3);
   }

   public ClassificationSchemeImpl(Concept var1, RegistryServiceImpl var2) throws JAXRException {
      super(var2);
      var2.getRegistryProxy().setRegistryObjectOwner(this, var2.getCurrentUser());
      this.setKey(var1.getKey());
      this.setName(var1.getName());
      this.setDescription(var1.getDescription());
      this.addChildConcepts(var1.getChildrenConcepts());
   }

   public void addChildConcept(Concept var1) throws JAXRException {
      if (var1 != null && !this.m_children.contains(var1)) {
         ((ConceptImpl)var1).setClassificationScheme(this);
         this.m_children.add(var1);
      }

   }

   public void addChildConcepts(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, Concept.class);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Concept var3 = (Concept)var2.next();
         this.addChildConcept(var3);
      }

   }

   public void removeChildConcept(Concept var1) throws JAXRException {
      if (var1 != null) {
         this.m_children.remove(var1);
      }

   }

   public void removeChildConcepts(Collection var1) throws JAXRException {
      if (var1 != null) {
         this.m_children.removeAll(var1);
      }

   }

   public int getChildConceptCount() throws JAXRException {
      return this.m_children.size();
   }

   public Collection getChildrenConcepts() throws JAXRException {
      return this.m_children;
   }

   public Collection getDescendantConcepts() throws JAXRException {
      ArrayList var1 = new ArrayList(this.m_children);
      Iterator var2 = this.m_children.iterator();

      while(var2.hasNext()) {
         Concept var3 = (Concept)var2.next();
         if (var3.getChildConceptCount() > 0) {
            var1.addAll(var3.getDescendantConcepts());
         }
      }

      return var1;
   }

   public boolean isExternal() throws JAXRException {
      return this.m_children.size() == 0;
   }

   public int getValueType() throws JAXRException {
      this.checkCapability(1);
      return 0;
   }

   public void setValueType(int var1) throws JAXRException {
      this.checkCapability(1);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_children};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_children"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
