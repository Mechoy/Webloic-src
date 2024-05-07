package weblogic.xml.jaxr.registry.infomodel;

import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Classification;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.RegistryObject;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.util.ClassificationSchemeHelper;

public class ClassificationImpl extends RegistryObjectImpl implements Classification {
   private static final long serialVersionUID = -1L;
   private ConceptImpl m_concept;
   private ClassificationSchemeImpl m_classificationScheme;
   private String m_value;
   private RegistryObjectImpl m_classifiedObject;

   public ClassificationImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
   }

   public ClassificationImpl(Classification var1, RegistryServiceImpl var2) throws JAXRException {
      super((RegistryObject)var1, var2);
      if (var1 != null) {
         this.m_concept = new ConceptImpl(var1.getConcept(), var2);
         this.m_value = var1.getValue();
      }

   }

   public Concept getConcept() throws JAXRException {
      return this.m_concept;
   }

   public void setConcept(Concept var1) throws JAXRException {
      ClassificationSchemeHelper.validateConcept(var1, this.getRegistryService());
      this.m_concept = (ConceptImpl)var1;
   }

   public ClassificationScheme getClassificationScheme() throws JAXRException {
      return (ClassificationScheme)(this.isExternal() ? this.m_classificationScheme : this.getConcept().getClassificationScheme());
   }

   public void setClassificationScheme(ClassificationScheme var1) throws JAXRException {
      this.m_classificationScheme = (ClassificationSchemeImpl)var1;
   }

   public String getValue() throws JAXRException {
      return this.isExternal() ? this.m_value : this.getConcept().getValue();
   }

   public void setValue(String var1) throws JAXRException {
      this.m_value = var1;
   }

   public RegistryObject getClassifiedObject() throws JAXRException {
      return this.m_classifiedObject;
   }

   public void setClassifiedObject(RegistryObject var1) throws JAXRException {
      this.m_classifiedObject = (RegistryObjectImpl)var1;
   }

   public boolean isExternal() {
      return this.m_concept == null;
   }

   public InternationalString getName() {
      return this.isExternal() ? super.getName() : this.m_concept.getName();
   }

   protected Object[] getDefiningElements() {
      String var1 = this.m_classificationScheme == null ? "null" : this.m_classificationScheme.getName().toString();
      String var2 = this.m_classifiedObject == null ? "null" : this.m_classifiedObject.getName().toString();
      Object[] var3 = new Object[]{this.m_concept, var1, this.m_value, var2};
      Object[] var4 = mergeObjectArrays(super.getDefiningElements(), var3);
      return var4;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_concept", "m_classificationScheme.Name", "m_value", "m_classifiedObject.Name"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
