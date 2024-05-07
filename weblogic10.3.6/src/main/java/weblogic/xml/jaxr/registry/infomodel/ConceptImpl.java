package weblogic.xml.jaxr.registry.infomodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.RegistryObject;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.util.JAXRUtil;

public class ConceptImpl extends RegistryObjectImpl implements Concept {
   private static final long serialVersionUID = -1L;
   private String m_value;
   private ArrayList m_children = new ArrayList();
   private ConceptImpl m_parentConcept;
   private ClassificationSchemeImpl m_classificationScheme;

   public ConceptImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
      var1.getRegistryProxy().setRegistryObjectOwner(this, var1.getCurrentUser());
   }

   public ConceptImpl(Concept var1, RegistryServiceImpl var2) throws JAXRException {
      super((RegistryObject)var1, var2);
      var2.getRegistryProxy().setRegistryObjectOwner(this, var2.getCurrentUser());
      if (var1 != null) {
         this.m_value = var1.getValue();
         Collection var3 = var1.getChildrenConcepts();
         this.addChildConcepts(var3);
      }

   }

   public ConceptImpl(RegistryObject var1, InternationalString var2, String var3, RegistryServiceImpl var4) throws JAXRException {
      super(var4);
      var4.getRegistryProxy().setRegistryObjectOwner(this, var4.getCurrentUser());
      this.setName(var2);
      this.setValue(var3);
      if (var1 instanceof ClassificationScheme) {
         ((ClassificationScheme)var1).addChildConcept(this);
      } else if (var1 instanceof Concept) {
         ((Concept)var1).addChildConcept(this);
      }

   }

   public String getValue() throws JAXRException {
      return this.m_value;
   }

   public void setValue(String var1) throws JAXRException {
      this.m_value = var1;
   }

   public void addChildConcept(Concept var1) throws JAXRException {
      if (var1 != null && !this.m_children.contains(var1)) {
         ((ConceptImpl)var1).setParentConcept(this);
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

   public Concept getParentConcept() throws JAXRException {
      return this.m_parentConcept;
   }

   public ClassificationScheme getClassificationScheme() throws JAXRException {
      return this.m_classificationScheme;
   }

   public String getPath() throws JAXRException {
      return this.m_parentConcept == null ? "/" + this.m_classificationScheme.getKey().getId() + "/" + this.m_value : this.m_parentConcept.getPath() + "/" + this.m_value;
   }

   public RegistryObject getParent() throws JAXRException {
      return (RegistryObject)(this.m_parentConcept != null ? this.m_parentConcept : this.m_classificationScheme);
   }

   public void setClassificationScheme(ClassificationSchemeImpl var1) {
      this.m_classificationScheme = var1;
   }

   private void setParentConcept(ConceptImpl var1) {
      this.m_parentConcept = var1;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_value, this.getName(this.m_children), this.getName(this.m_parentConcept), this.getName(this.m_classificationScheme)};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_value", "m_children.Name", "m_parentConcept.Name", "m_classificationScheme.Name"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
