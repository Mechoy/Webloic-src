package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.ArrayNameHelper;
import com.bea.staxb.buildtime.ArrayNamespaceInfo;
import com.bea.staxb.buildtime.Java2Schema;
import com.bea.staxb.buildtime.internal.Java2SchemaWrapperElement;
import com.bea.staxb.buildtime.internal.bts.BindingFile;
import com.bea.staxb.buildtime.internal.bts.BindingType;
import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.buildtime.internal.bts.JavaTypeName;
import com.bea.staxb.buildtime.internal.bts.SimpleBindingType;
import com.bea.staxb.buildtime.internal.bts.SimpleDocumentBinding;
import com.bea.staxb.buildtime.internal.bts.SoapArrayType;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.buildtime.internal.tylar.ExplodedTylar;
import com.bea.util.jam.JClass;
import com.bea.xbean.xb.xsdschema.AnnotationDocument;
import com.bea.xbean.xb.xsdschema.AnyDocument;
import com.bea.xbean.xb.xsdschema.Attribute;
import com.bea.xbean.xb.xsdschema.ComplexContentDocument;
import com.bea.xbean.xb.xsdschema.ComplexRestrictionType;
import com.bea.xbean.xb.xsdschema.DocumentationDocument;
import com.bea.xbean.xb.xsdschema.ExplicitGroup;
import com.bea.xbean.xb.xsdschema.ImportDocument;
import com.bea.xbean.xb.xsdschema.LocalComplexType;
import com.bea.xbean.xb.xsdschema.LocalElement;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xbean.xb.xsdschema.TopLevelComplexType;
import com.bea.xbean.xb.xsdschema.TopLevelElement;
import com.bea.xbean.xb.xsdschema.TopLevelSimpleType;
import com.bea.xbean.xb.xsdschema.SchemaDocument.Factory;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlCursor;
import com.bea.xml.XmlObject;
import com.bea.xml.XmlOptions;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import org.w3c.dom.Element;
import weblogic.wsee.bind.buildtime.BindingException;
import weblogic.wsee.bind.buildtime.J2SBindingsBuilder;
import weblogic.wsee.util.ClassUtil;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.ObjectUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.XBeanSchemaProcessor;
import weblogic.wsee.util.XBeanUtil;

class SoapAwareJava2Schema extends Java2Schema {
   private static final boolean verbose = Verbose.isVerbose(SoapAwareJava2Schema.class);
   private static final String XSD_NS = "http://www.w3.org/2001/XMLSchema";
   private static final JavaTypeName SOAPELEMENT_JTN;
   private static final String SOAPELEMENT_CLASSNAME;
   private static final String UNBOUNDED = "unbounded";
   private static final String ANYTYPE = "anyType";
   private static final String SOAPENC_NS = "http://schemas.xmlsoap.org/soap/encoding/";
   protected static final QName SOAPENC_ARRAY;
   protected static final QName SOAPENC_ARRAYTYPE;
   private static final QName XSD_ANYTYPE;
   protected static final String SOAPARRAY_NAME_PREFIX = "ArrayOf";
   private static final String SOAP_ENC_XSD_LOCATION = "/weblogic/wsee/wsdl/schema/soap-encoding-11.xsd";
   private static final SchemaDocument SOAP_ENC_SCHEMA_DOC;
   private BindingMessageSink messageSink = new BindingMessageSink();
   private boolean jaxRpcByteArrayStyle = false;
   private boolean localElementDefaultRequired = true;
   private boolean localElementDefaultNillable = true;
   private boolean setElementFormDefaultUnqualified = false;
   protected boolean allowNullByMinOccursZeroInWrapperElements = false;
   private boolean mNeedsSoapEncodedSchema = false;
   private List mAllSoapElements = null;
   private List mAllSoapElementsForElementAny = null;
   private List mSoapEncPrimitiveWrapperTypes = null;
   private Map mArray2SetOfSoapArrayElemQNames = null;
   private Map mArrayInfo2SetOfPrimitiveByteArrayElemQNamesInEncodedStyle = null;
   private List mWrapperElements = null;
   private List<Java2SchemaTypeWrapperElement> mTypeWrapperElements = null;
   private boolean mNeedListSchema = false;
   private List<ElementTypePair> mElmList = null;
   private J2SBindingsBuilder mJ2SBindingsBuilder = null;
   private Set<QName> mSpecialTypeQNames = null;
   protected Collection mGeneratedSoapArrayJavaTypeNames = new HashSet();
   private Set mXmlBeansClassNames = null;
   private Set mGeneratedXmlBeansArraySchemaTypeQNames = null;
   private Map mArray2SetOfXmlBeansLitArrayElemQNames = null;
   private List<SchemaDocument> mXBeanIncludedSchemas = new LinkedList();

   public SoapAwareJava2Schema(J2SBindingsBuilder var1) {
      this.mJ2SBindingsBuilder = var1;
      this.setLocalElementDefaultRequired(this.localElementDefaultRequired);
      this.setLocalElementDefaultNillable(this.localElementDefaultNillable);
      this.setMessageSink(this.messageSink);
      if (this.setElementFormDefaultUnqualified) {
         super.setElementFormDefaultQualified(false);
      }

   }

   public void addTypeWrapperElement(Java2SchemaTypeWrapperElement var1) {
      if (this.mTypeWrapperElements == null) {
         this.mTypeWrapperElements = new ArrayList();
      }

      JClass var2 = var1.getServiceClass();
      JClass var3 = var1.getElementJavaType();
      this.handleWrappedType(var2, var3);
      this.mTypeWrapperElements.add(var1);
   }

   public void addWrapperElement(Java2SchemaWrapperElement var1) {
      if (this.mWrapperElements == null) {
         this.mWrapperElements = new ArrayList();
      }

      JClass var2 = var1.getServiceClass();
      JClass[] var3 = var1.getElementJavaTypes();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         JClass var5 = var3[var4];
         this.handleWrappedType(var2, var5);
      }

      this.mWrapperElements.add(var1);
   }

   public void addSpecialTypeQNames(QName var1) {
      if (this.mSpecialTypeQNames == null) {
         this.mSpecialTypeQNames = new HashSet();
      }

      this.mSpecialTypeQNames.add(var1);
   }

   private void genearteSpecialTypeForRpcLitStyle(Map var1) {
      if (this.mSpecialTypeQNames != null) {
         Iterator var2 = this.mSpecialTypeQNames.iterator();

         while(var2.hasNext()) {
            QName var3 = (QName)var2.next();
            String var4 = var3.getNamespaceURI();
            if (!this.isTopLevelTypeExist(var3, var4)) {
               this.addSpecialType(var3, var1);
            }
         }

      }
   }

   private void handleWrappedType(JClass var1, JClass var2) {
      if (var2.isArrayType()) {
         JClass var3 = var2.getArrayComponentType();
         if (XBeanUtil.isXmlBean(var3)) {
            this.addXmlBeanClassName(var3.getQualifiedName());
         } else if (this.isBoundToAnyType(var2.getQualifiedName())) {
            this.bindJavaArrayToLiteralArray(ClassUtil.getTargetNamespace(var1), var1, var2);
         }
      } else if (!TylarBuildtimeBindings.isSpecialJavaType(var2.getQualifiedName()) && !var2.getQualifiedName().equals(SOAPELEMENT_CLASSNAME)) {
         this.addClassToBind(var2);
      }

   }

   public void bindJavaArrayToSoapArray(String var1, JClass var2, JClass var3) {
      this.bindJavaArrayToSoapArray(var1, var2, var3, (QName)null);
   }

   public void bindJavaArrayToSoapArray(String var1, JClass var2, JClass var3, QName var4) {
      validateArrayType(var3);
      Set var5 = this.findOrCreateElementNameSetFor(var1, var2, var3);
      if (var4 != null) {
         var5.add(var4);
      }

      if (!this.typeIs1DPrimitiveByteArray(var3) || !this.jaxRpcByteArrayStyle) {
         this.addClassToBind(var3.getArrayComponentType());
      }
   }

   public void addClassToBind(JClass var1) {
      if (!XBeanUtil.isXmlBean(var1)) {
         super.addClassToBind(var1);
      } else {
         this.addXmlBeanClassName(var1.getQualifiedName());
      }

   }

   public void addSoapElement(QName var1) {
      if (this.mAllSoapElements == null) {
         this.mAllSoapElements = new ArrayList();
      }

      this.mAllSoapElements.add(var1);
      if (!this.isBoundToAnyType(SOAPElement.class.getName())) {
         if (this.mAllSoapElementsForElementAny == null) {
            this.mAllSoapElementsForElementAny = new ArrayList();
         }

         this.mAllSoapElementsForElementAny.add(var1);
      }

   }

   void addSpecialJavaElement(QName var1, JClass var2) {
      if (this.mElmList == null) {
         this.mElmList = new ArrayList();
      }

      this.mElmList.add(new ElementTypePair(var1, var2));
   }

   protected static void validateArrayType(JClass var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("null javatype");
      } else if (!var0.isArrayType()) {
         throw new IllegalArgumentException(var0.getQualifiedName() + " is not an array type");
      }
   }

   protected void addEncodedClassToBind(JClass var1) {
      if (this.mSoapEncPrimitiveWrapperTypes == null || !this.mSoapEncPrimitiveWrapperTypes.contains(var1)) {
         if (JamUtil.isSoapEncJavaPrimitiveWrapperClass(var1)) {
            this.assertResolved(var1);
            if (this.mSoapEncPrimitiveWrapperTypes == null) {
               this.mSoapEncPrimitiveWrapperTypes = new ArrayList();
            }

            this.mSoapEncPrimitiveWrapperTypes.add(var1);
            this.mNeedsSoapEncodedSchema = true;
         } else {
            if (isCollectionType(var1)) {
               this.mSoapSpecificTypes.add(var1);
            }

            this.addClassToBind(var1);
         }
      }
   }

   protected BindingType createJavaCollectionType(BindingTypeName var1) {
      SoapArrayType var2 = createSoapEncCollectionType(var1);
      this.mBindingFile.addBindingType(var2, true, true);
      QName var3 = var1.getXmlName().getQName();
      SchemaDocument.Schema var4 = this.findOrCreateSchema(var3.getNamespaceURI());
      addSoapEncNS(var4);
      TopLevelComplexType var5 = var4.addNewComplexType();
      var5.setName(var3.getLocalPart());
      ComplexRestrictionType var6 = var5.addNewComplexContent().addNewRestriction();
      var6.setBase(SOAPENC_ARRAY);
      return var2;
   }

   private static void addSoapEncNS(SchemaDocument.Schema var0) {
      ImportDocument.Import[] var1 = var0.getImportArray();
      ImportDocument.Import[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ImportDocument.Import var5 = var2[var4];
         if (var5.getNamespace().equals("http://schemas.xmlsoap.org/soap/encoding/")) {
            return;
         }
      }

      ImportDocument.Import var6 = var0.addNewImport();
      var6.setNamespace("http://schemas.xmlsoap.org/soap/encoding/");
   }

   protected void postProcessOutputs(Map var1, BindingFile var2) {
      this.generateEncodePrimitiveByteArrayElements();
      this.generateWrappedXmlBeanSchemaTypes();
      this.generateSoapEncodedWrapperTypes(var2);
      this.generateSpecialElements(var1);
      this.generateSoapElements(var1, var2);
      this.generateSoapArrays(var1, var2);
      this.generateTypeWrapperElements(var1);
      this.generateWrapperElements(var1);
      this.genearteSpecialTypeForRpcLitStyle(var1);
      if (this.mNeedListSchema) {
         SoapArrayType var3 = this.createListSchema(var1);
         var2.addBindingType(var3, true, true);
      }

      this.addSoapEncSchemaDoc();
   }

   private SoapArrayType createListSchema(Map var1) {
      SchemaDocument.Schema var2 = getSchemaFor(TylarJ2SBindingsBuilderImpl.JAVA_LIST_QNAME.getNamespaceURI(), var1);
      ImportDocument.Import var3 = var2.addNewImport();
      var3.setNamespace("http://schemas.xmlsoap.org/soap/encoding/");
      TopLevelComplexType var4 = var2.addNewComplexType();
      var4.setName(TylarJ2SBindingsBuilderImpl.JAVA_LIST_QNAME.getLocalPart());
      ComplexRestrictionType var5 = var4.addNewComplexContent().addNewRestriction();
      var5.setBase(SOAPENC_ARRAY);
      Attribute var6 = var5.addNewAttribute();
      var6.setRef(SOAPENC_ARRAYTYPE);
      ExplicitGroup var7 = var5.addNewSequence();
      LocalElement var8 = var7.addNewElement();
      var8.setName("anyType");
      var8.setType(XSD_ANYTYPE);
      var8.setMinOccurs(BigInteger.ZERO);
      var8.setMaxOccurs("unbounded");
      XmlTypeName var9 = XmlTypeName.forTypeNamed(TylarJ2SBindingsBuilderImpl.JAVA_LIST_QNAME);
      BindingTypeName var10 = BindingTypeName.forPair(JavaTypeName.forString(List.class.getName()), var9);
      SoapArrayType var11 = new SoapArrayType(var10);
      var11.setRanks(1);
      return var11;
   }

   private QName xmlBeanArrayQName(JClass var1, String var2) {
      if (!var1.isArrayType()) {
         return null;
      } else {
         int var3 = var1.getArrayDimensions();
         if (var3 <= 0) {
            return null;
         } else {
            String var4 = var1.getSimpleName();
            var4 = var4.substring(0, var4.indexOf("["));

            for(int var5 = 0; var5 < var3; ++var5) {
               var4 = "ArrayOf" + var4;
            }

            QName var6 = new QName(var2, var4);
            return var6;
         }
      }
   }

   private boolean generatedXmlBeanArraySchemaTypeQName(QName var1) {
      return this.mGeneratedXmlBeansArraySchemaTypeQNames == null ? false : this.mGeneratedXmlBeansArraySchemaTypeQNames.contains(var1);
   }

   private void addXmlBeanArraySchemaTypeQName(QName var1) {
      if (this.mGeneratedXmlBeansArraySchemaTypeQNames == null) {
         this.mGeneratedXmlBeansArraySchemaTypeQNames = new HashSet();
      }

      this.mGeneratedXmlBeansArraySchemaTypeQNames.add(var1);
   }

   private void addXmlBeanClassName(String var1) {
      if (this.mXmlBeansClassNames == null) {
         this.mXmlBeansClassNames = new HashSet();
      }

      this.mXmlBeansClassNames.add(var1);
   }

   private void generateWrappedXmlBeanSchemaTypes() {
      if (this.mXmlBeansClassNames != null) {
         XBeanSchemaProcessor var1 = new XBeanSchemaProcessor(this, this.mJ2SBindingsBuilder.getXmlObjectClassLoader());
         Iterator var2 = this.mXmlBeansClassNames.iterator();
         if (var2.hasNext()) {
            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               String var4 = XBeanUtil.getSchemaTypeSourceName(this.mJ2SBindingsBuilder.getXmlObjectClassLoader(), var3);
               if (var4 == null) {
                  boolean var5 = XBeanUtil.isBuiltInTypeXmlBean(this.mJ2SBindingsBuilder.getXmlObjectClassLoader(), var3);
                  if (!var5) {
                     this.logError(" ERROR !  could not get Schema Name for XmlBean named '" + var3 + "'.   This is completely unexpected and might be caused by missing *.xsd " + "files in the build environment.   The building and running of this Service is likely to FAIL.");
                  }
               }
            }

            try {
               var1.includeAllRelevantSchemas(this.mXmlBeansClassNames);
            } catch (IllegalArgumentException var6) {
               this.logError(" ERROR !  could not add Xml Schema Type for XmlBean named '" + this.mXmlBeansClassNames + "'.   This is completely unexpected and might be caused by missing *.xsb and *.xsd " + "files in the build environment.   The building and running of this Service is likely to FAIL.");
            }
         }

      }
   }

   private ArrayNamespaceInfo[] getArrayNamespaceInfos() {
      ArrayNamespaceInfo[] var1 = new ArrayNamespaceInfo[this.mArray2SetOfSoapArrayElemQNames.keySet().size()];
      this.mArray2SetOfSoapArrayElemQNames.keySet().toArray(var1);
      return var1;
   }

   protected QName[] getElementNamesForSoapArrayClass(ArrayNamespaceInfo var1, String var2) {
      Object var3 = (Set)this.mArray2SetOfSoapArrayElemQNames.get(var1);
      if (var3 == null) {
         this.mArray2SetOfSoapArrayElemQNames.put(var1, var3 = new HashSet());
      }

      String var4 = var1.getNamespace();
      QName var5 = new QName(var4, "ArrayOf" + var2);
      ((Set)var3).add(var5);
      QName[] var6 = new QName[((Set)var3).size()];
      ((Set)var3).toArray(var6);
      return var6;
   }

   private void generateSoapArrays(Map var1, BindingFile var2) {
      if (this.mArray2SetOfSoapArrayElemQNames != null) {
         ArrayNamespaceInfo[] var3 = this.getArrayNamespaceInfos();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            this.generateSoapArray(var1, var3[var4], var2, this.mGeneratedSoapArrayJavaTypeNames);
         }

      }
   }

   protected BindingType generateSoapArray(Map var1, ArrayNamespaceInfo var2, BindingFile var3, Collection var4) {
      JClass var6 = var2.getArrayClass();
      JavaTypeName var7 = JavaTypeName.forJClass(var6);
      JClass var8 = var6.getArrayComponentType();
      int var9 = var6.getArrayDimensions();
      boolean var10 = var8.isPrimitiveType();
      JavaTypeName var11 = JavaTypeName.forJClass(var8);
      BindingTypeName var12 = this.getBindingLoader().lookupTypeFor(var11);
      if (var12 == null) {
         throw new IllegalArgumentException("could not find binding type for array component type: " + var6.getArrayComponentType());
      } else {
         QName var13 = var12.getXmlName().getQName();
         var13 = this.arrayComponentQNameForSoapEncWrapper(var8, var13);
         String var14 = this.arrayComponentNameForJClass(var8, var13.getLocalPart());
         QName[] var15 = this.getElementNamesForSoapArrayClass(var2, var14);
         StringBuffer var16 = new StringBuffer();

         for(int var17 = 0; var17 < var9; ++var17) {
            var16.append("ArrayOf");
         }

         QName var34 = new QName(var2.getNamespace(), var16.toString() + var14);
         Object var5;
         XmlTypeName var35;
         BindingTypeName var37;
         if (!var4.contains(var34)) {
            var4.add(var34);
            if (!this.isTopLevelTypeExist(var34, var34.getNamespaceURI())) {
               SchemaDocument.Schema var18 = getSchemaFor(var34.getNamespaceURI(), var1);
               TopLevelComplexType var19 = var18.addNewComplexType();
               var19.setName(var34.getLocalPart());
               ComplexContentDocument.ComplexContent var20 = var19.addNewComplexContent();
               ComplexRestrictionType var21 = var20.addNewRestriction();
               var21.setBase(SOAPENC_ARRAY);
               ExplicitGroup var22 = var21.addNewSequence();
               LocalElement var23 = var22.addNewElement();
               var23.setName(var14);
               var23.setType(var13);
               var23.setMaxOccurs("unbounded");
               if (var10) {
                  var23.setNillable(false);
               } else {
                  var23.setNillable(true);
               }

               Attribute var24 = var21.addNewAttribute();
               var24.setRef(SOAPENC_ARRAYTYPE);
               this.checkNsForImport(var34.getNamespaceURI(), var13.getNamespaceURI());
               if (var9 > 1) {
                  String var25 = "type";
                  Element var26 = (Element)var23.getDomNode();
                  String var27 = var26.getAttribute(var25);
                  Element var28 = (Element)var24.getDomNode();
                  String var29 = "http://schemas.xmlsoap.org/wsdl/";
                  String var30 = "arrayType";
                  StringBuffer var31 = new StringBuffer();
                  var31.append("[");

                  for(int var32 = 1; var32 < var9; ++var32) {
                     var31.append(",");
                  }

                  var31.append("]");
                  var28.setAttributeNS(var29, var30, var27 + var31.toString());
                  String var44 = var13.getNamespaceURI();
                  if (!var44.equals("http://www.w3.org/2001/XMLSchema")) {
                     String var33 = var27.substring(0, var27.indexOf(":"));
                     var28.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + var33, var44);
                  }
               }
            }

            var35 = XmlTypeName.forTypeNamed(var34);
            var37 = BindingTypeName.forPair(var7, var35);
            SoapArrayType var39 = new SoapArrayType(var37);
            var39.setRanks(var9);
            if (var10) {
               var39.setItemNillable(false);
            } else {
               var39.setItemNillable(true);
            }

            var39.setItemName(var13);
            var39.setItemType(var12);
            var3.addBindingType(var39, true, true);
            var5 = var39;

            for(int var36 = 0; var36 < var15.length; ++var36) {
               XmlTypeName var38 = XmlTypeName.forGlobalName('e', var15[var36]);
               BindingTypeName var40 = BindingTypeName.forPair(var7, var38);
               if (this.getBindingLoader().getBindingType(var40) != null) {
                  if (!this.isTopLevelElementExist(var15[var36], var15[var36].getNamespaceURI())) {
                     SchemaDocument.Schema var41 = getSchemaFor(var15[var36].getNamespaceURI(), var1);
                     TopLevelElement var43 = var41.addNewElement();
                     var43.setName(var15[var36].getLocalPart());
                     var43.setType(var34);
                     this.checkNsForImport(var15[var36].getNamespaceURI(), var34.getNamespaceURI());
                  }

                  SimpleDocumentBinding var42 = new SimpleDocumentBinding(var40);
                  var42.setTypeOfElement(XmlTypeName.forTypeNamed(var34));
                  var3.addBindingType(var42, true, true);
               }
            }
         } else {
            var35 = XmlTypeName.forTypeNamed(var34);
            var37 = BindingTypeName.forPair(var7, var35);
            var5 = this.mBindingFile.getBindingType(var37);
         }

         return (BindingType)var5;
      }
   }

   private QName arrayComponentQNameForSoapEncWrapper(JClass var1, QName var2) {
      return !JamUtil.isSoapEncJavaPrimitiveWrapperClass(var1) ? var2 : new QName("http://www.w3.org/2001/XMLSchema", JamUtil.soapEncodedTypeForJavaPrimitiveWrapper(var1));
   }

   private void generateSoapElements(Map var1, BindingFile var2) {
      QName[] var3;
      int var4;
      QName var5;
      String var6;
      SchemaDocument.Schema var7;
      XmlTypeName var11;
      BindingTypeName var12;
      SimpleDocumentBinding var13;
      if (this.isBoundToAnyType(SOAPElement.class.getName())) {
         if (this.mAllSoapElements == null) {
            return;
         }

         var3 = new QName[this.mAllSoapElements.size()];
         this.mAllSoapElements.toArray(var3);

         for(var4 = 0; var4 < var3.length; ++var4) {
            var5 = var3[var4];
            var6 = var5.getNamespaceURI();
            if (!this.isTopLevelElementExist(var5, var6)) {
               var7 = getSchemaFor(var6, var1);
               TopLevelElement var8 = var7.addNewElement();
               var8.setName(var5.getLocalPart());
               var8.setType(XS_ANYTYPE);
            }

            var11 = XmlTypeName.forGlobalName('e', var5);
            var12 = BindingTypeName.forPair(SOAPELEMENT_JTN, var11);
            var13 = new SimpleDocumentBinding(var12);
            var13.setTypeOfElement(XmlTypeName.forTypeNamed(XS_ANYTYPE));
            var2.addBindingType(var13, false, true);
         }
      } else {
         if (this.mAllSoapElementsForElementAny == null) {
            return;
         }

         var3 = new QName[this.mAllSoapElementsForElementAny.size()];
         this.mAllSoapElementsForElementAny.toArray(var3);

         for(var4 = 0; var4 < var3.length; ++var4) {
            var5 = var3[var4];
            var6 = var5.getNamespaceURI();
            if (!this.isTopLevelTypeExist(var5, var6)) {
               var7 = getSchemaFor(var6, var1);
               TopLevelComplexType var14 = var7.addNewComplexType();
               var14.setName(var5.getLocalPart());
               ExplicitGroup var9 = var14.addNewSequence();
               AnyDocument.Any var10 = var9.addNewAny();
            }

            var11 = XmlTypeName.forGlobalName('e', var5);
            var12 = BindingTypeName.forPair(SOAPELEMENT_JTN, var11);
            var13 = new SimpleDocumentBinding(var12);
            var13.setTypeOfElement(XmlTypeName.forTypeNamed(XS_ANYTYPE));
            var2.addBindingType(var13, false, true);
         }
      }

   }

   private void generateSoapEncodedWrapperTypes(BindingFile var1) {
      if (this.mSoapEncPrimitiveWrapperTypes != null) {
         Iterator var2 = this.mSoapEncPrimitiveWrapperTypes.iterator();

         while(var2.hasNext()) {
            JClass var3 = (JClass)var2.next();
            JavaTypeName var4 = JavaTypeName.forJClass(var3);
            String var5 = JamUtil.soapEncodedTypeForJavaPrimitiveWrapper(var3);
            if (var5 == null) {
               this.logError(" Could not get SOAP ENCODED type for java class '" + var3.getQualifiedName() + "'");
            } else {
               QName var6 = new QName("http://schemas.xmlsoap.org/soap/encoding/", var5);
               XmlTypeName var7 = XmlTypeName.forTypeNamed(var6);
               BindingTypeName var8 = BindingTypeName.forPair(var4, var7);
               SimpleBindingType var9 = new SimpleBindingType(var8);
               QName var10 = new QName("http://www.w3.org/2001/XMLSchema", var5);
               XmlTypeName var11 = XmlTypeName.forTypeNamed(var10);
               var9.setAsIfXmlType(var11);
               var1.addBindingType(var9, true, true);
            }
         }

      }
   }

   private void addSoapEncSchemaDoc() {
      if (this.mNeedsSoapEncodedSchema) {
         if (SOAP_ENC_SCHEMA_DOC != null) {
            if (!this.isSoapEncSchemaDocExist()) {
               this.getTns2Schemadoc().put("soapenc", SOAP_ENC_SCHEMA_DOC);
            }
         } else {
            this.logError("SoapAwarejava2Schema:  Could not load SOAP-ENCODED schema from /weblogic/wsee/wsdl/schema/soap-encoding-11.xsd");
         }

      }
   }

   private void generateSpecialElements(Map var1) {
      if (this.mElmList != null) {
         Iterator var2 = this.mElmList.iterator();

         while(var2.hasNext()) {
            ElementTypePair var3 = (ElementTypePair)var2.next();
            QName var4 = TylarBuildtimeBindings.getSpecialJavaType(var3.clazz.getQualifiedName());
            String var5 = var3.element.getNamespaceURI();
            if (!this.isTopLevelElementExist(var3.element, var5)) {
               SchemaDocument.Schema var6 = getSchemaFor(var5, var1);
               TopLevelElement var7 = var6.addNewElement();
               var7.setName(var3.element.getLocalPart());
               var7.setType(var4);
               this.addSpecialType(var4, var1);
            }
         }

      }
   }

   private void addSpecialType(QName var1, Map var2) {
      SchemaDocument.Schema var3 = getSchemaFor(var1.getNamespaceURI(), var2);
      TopLevelComplexType[] var4 = var3.getComplexTypeArray();
      boolean var5 = false;

      for(int var6 = 0; !var5 && var6 < var4.length; ++var6) {
         TopLevelComplexType var7 = var4[var6];
         if (var7.getName().equals(var1.getLocalPart())) {
            var5 = true;
         }
      }

      if (!var5) {
         TopLevelComplexType var9 = var3.addNewComplexType();
         var9.setName(var1.getLocalPart());
         AnnotationDocument.Annotation var10 = var9.addNewAnnotation();
         DocumentationDocument.Documentation var8 = var10.addNewDocumentation();
         setTextValue(var8, "Internal type created by WebLogic - Do not edit!");
      }

   }

   private static void setTextValue(XmlObject var0, String var1) {
      XmlCursor var2 = var0.newCursor();

      try {
         var2.setTextValue(var1);
      } finally {
         var2.dispose();
      }

   }

   protected void generateTypeWrapperElements(Map var1) {
      if (this.mTypeWrapperElements != null) {
         Iterator var2 = this.mTypeWrapperElements.iterator();

         while(true) {
            while(var2.hasNext()) {
               Java2SchemaTypeWrapperElement var3 = (Java2SchemaTypeWrapperElement)var2.next();
               QName var4 = var3.getWrapperName();
               String var5 = var4.getNamespaceURI();
               SchemaDocument.Schema var6 = this.findOrCreateSchema(var5);
               JClass var7 = var3.getElementJavaType();
               QName var8 = null;
               if (XBeanUtil.isXmlBean(var7)) {
                  if (!XBeanUtil.xmlBeanIsDocumentType(this.mJ2SBindingsBuilder.getXmlObjectClassLoader(), var7.getQualifiedName())) {
                     var8 = XBeanUtil.getQNameFromXmlBean(this.mJ2SBindingsBuilder.getXmlObjectClassLoader(), var7.getQualifiedName());
                     if (var8 == null) {
                        this.logError(" could not load XMLSchema QName for xmlBean type '" + var7.getQualifiedName() + "'");
                     }
                  }
               } else if (TylarBuildtimeBindings.isSpecialJavaType(var7.getQualifiedName())) {
                  var8 = TylarBuildtimeBindings.getSpecialJavaType(var7.getQualifiedName());
                  this.addSpecialType(var8, var1);
               } else if (var7.getQualifiedName().equals(SOAPELEMENT_CLASSNAME)) {
                  if (this.isBoundToAnyType(SOAPELEMENT_CLASSNAME)) {
                     var8 = XS_ANYTYPE;
                  } else {
                     var8 = XmlTypeName.forElementWildCardType().getQName();
                  }
               } else {
                  BindingTypeName var9 = this.getBindingLoader().lookupTypeFor(JavaTypeName.forJClass(var7));
                  if (var9 == null) {
                     this.logError("Could not locate schema type bound to wrapped element type: " + var7.getQualifiedName());
                     continue;
                  }

                  var8 = var9.getXmlName().getQName();
               }

               if (var8 != null && !containsElementNamed(var6, var4.getLocalPart())) {
                  this.checkNsForImport(var5, var8.getNamespaceURI());
                  TopLevelElement var10 = var6.addNewElement();
                  var10.setName(var4.getLocalPart());
                  var10.setType(var8);
               }
            }

            return;
         }
      }
   }

   protected void generateWrapperElements(Map var1) {
      if (this.mWrapperElements != null) {
         Iterator var2 = this.mWrapperElements.iterator();

         while(true) {
            while(true) {
               Java2SchemaWrapperElement var3;
               QName var4;
               String var5;
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  var3 = (Java2SchemaWrapperElement)var2.next();
                  var4 = var3.getWrapperName();
                  var5 = var4.getNamespaceURI();
               } while(this.isTopLevelElementExist(var4, var5));

               SchemaDocument.Schema var6 = this.findOrCreateSchema(var5);
               if (containsElementNamed(var6, var4.getLocalPart())) {
                  this.logError("Unable to create doc wrapped element named '" + var4 + "' because one was already generated");
               } else {
                  TopLevelElement var7 = var6.addNewElement();
                  var7.setName(var4.getLocalPart());
                  LocalComplexType var8 = var7.addNewComplexType();
                  ExplicitGroup var9 = var8.addNewSequence();
                  JClass[] var10 = var3.getElementJavaTypes();
                  String[] var11 = var3.getElementNames();
                  LocalElement var12 = null;

                  for(int var13 = 0; var13 < var11.length; ++var13) {
                     LocalElement var20;
                     if (var10[var13].isArrayType()) {
                        if (XBeanUtil.isArrayOfXmlBean(var10[var13])) {
                           JavaTypeName var25 = JavaTypeName.forJClass(var10[var13]);
                           int var26 = var25.getArrayDepth();
                           String var23 = var10[var13].getArrayComponentType().getQualifiedName();
                           if (var26 == 1 && !this.isBoundToAnyType(var10[var13].getQualifiedName())) {
                              AnyDocument.Any var17 = var9.addNewAny();
                              var17.setMaxOccurs("unbounded");
                              if (var12 != null) {
                                 var12.unsetMinOccurs();
                                 var12.setNillable(true);
                                 var12 = null;
                              }
                           } else {
                              this.generateXmlBeanArrayWrapperElement(var10[var13], var6, var9, var4, var11[var13]);
                              var12 = null;
                           }
                        } else if (var10[var13].getArrayComponentType().getQualifiedName().equals(SOAPELEMENT_CLASSNAME) && !this.isBoundToAnyType(var10[var13].getQualifiedName())) {
                           AnyDocument.Any var24 = var9.addNewAny();
                           var24.setMaxOccurs("unbounded");
                           if (var12 != null) {
                              var12.unsetMinOccurs();
                              var12.setNillable(true);
                              var12 = null;
                           }
                        } else {
                           BindingTypeName var21 = this.getBindingLoader().lookupTypeFor(JavaTypeName.forJClass(var10[var13]));
                           if (var21 == null) {
                              this.logError("Could not locate schema type bound to wrapped element type: " + var10[var13].getQualifiedName());
                           } else {
                              var20 = var9.addNewElement();
                              var20.setName(var11[var13]);
                              var20.setType(var21.getXmlName().getQName());
                              var12 = null;
                              QName var22 = var21.getXmlName().getQName();
                              if (var22 != null) {
                                 this.checkNsForImport(var5, var22.getNamespaceURI());
                              }
                           }
                        }
                     } else {
                        QName var14 = null;
                        AnyDocument.Any var18;
                        if (XBeanUtil.isXmlBean(var10[var13])) {
                           if (!this.isBoundToAnyType(var10[var13].getQualifiedName())) {
                              var18 = var9.addNewAny();
                              if (var12 != null) {
                                 var12.unsetMinOccurs();
                                 var12.setNillable(true);
                                 var12 = null;
                              }
                              continue;
                           }

                           if (XBeanUtil.xmlBeanIsDocumentType(this.mJ2SBindingsBuilder.getXmlObjectClassLoader(), var10[var13].getQualifiedName())) {
                              QName var15 = XBeanUtil.getQNameFromXmlBean(this.mJ2SBindingsBuilder.getXmlObjectClassLoader(), var10[var13].getQualifiedName());
                              if (var15 == null) {
                                 this.logError(" XmlBean '" + var10[var13].getQualifiedName() + "'was generated from  an xml document.  " + "Its top level element is of an anonymous complex type.  Unable to determine the QName of the " + "top level element.  Without this information we cannot " + "specify an xml schema ref attribute pointing to xmlBean's schema element.");
                              }

                              LocalElement var16 = var9.addNewElement();
                              var16.setRef(var15);
                              if (var15 != null) {
                                 this.checkNsForImport(var5, var15.getNamespaceURI());
                              }

                              var12 = null;
                              continue;
                           }

                           var14 = XBeanUtil.getQNameFromXmlBean(this.mJ2SBindingsBuilder.getXmlObjectClassLoader(), var10[var13].getQualifiedName());
                           if (var14 == null) {
                              this.logError(" could not load XMLSchema QName for xmlBean type '" + var10[var13].getQualifiedName() + "'");
                           }
                        } else if (TylarBuildtimeBindings.isSpecialJavaType(var10[var13].getQualifiedName())) {
                           var14 = TylarBuildtimeBindings.getSpecialJavaType(var10[var13].getQualifiedName());
                           this.addSpecialType(var14, var1);
                        } else if (var10[var13].getQualifiedName().equals(SOAPELEMENT_CLASSNAME)) {
                           if (this.isBoundToAnyType(SOAPELEMENT_CLASSNAME)) {
                              var14 = XS_ANYTYPE;
                           } else {
                              var14 = XmlTypeName.forElementWildCardType().getQName();
                           }
                        } else {
                           BindingTypeName var19 = this.getBindingLoader().lookupTypeFor(JavaTypeName.forJClass(var10[var13]));
                           if (var19 == null) {
                              this.logError("Could not locate schema type bound to wrapped element type: " + var10[var13].getQualifiedName());
                              continue;
                           }

                           var14 = var19.getXmlName().getQName();
                        }

                        if (var14 != null) {
                           this.checkNsForImport(var5, var14.getNamespaceURI());
                        }

                        if (var10[var13].getQualifiedName().equals(SOAPELEMENT_CLASSNAME) && !this.isBoundToAnyType(SOAPELEMENT_CLASSNAME)) {
                           var18 = var9.addNewAny();
                           if (var12 != null) {
                              var12.unsetMinOccurs();
                              var12.setNillable(true);
                              var12 = null;
                           }
                        } else if (var10[var13].isArrayType() && !this.isBoundToAnyType(var10[var13].getQualifiedName())) {
                           var18 = var9.addNewAny();
                           var18.setMinOccurs(BigInteger.ZERO);
                           var18.setMaxOccurs("unbounded");
                           if (var12 != null) {
                              var12.unsetMinOccurs();
                              var12.setNillable(true);
                              var12 = null;
                           }
                        } else {
                           var20 = var9.addNewElement();
                           var20.setName(var11[var13]);
                           var20.setType(var14);
                           if (JamUtil.isSoapEncJavaPrimitiveWrapperClass(var10[var13])) {
                              var20.setNillable(true);
                              var20.setMinOccurs(BigInteger.ZERO);
                              var12 = var20;
                           }

                           if (this.allowNullByMinOccursZeroInWrapperElements && !var10[var13].isPrimitiveType()) {
                              var20.setMinOccurs(BigInteger.ZERO);
                              var12 = var20;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void generateXmlBeanArrayWrapperElement(JClass var1, SchemaDocument.Schema var2, ExplicitGroup var3, QName var4, String var5) {
      this.generateXmlBeanArrayType(var1, var2, var4.getNamespaceURI());
      QName var6 = this.xmlBeanArrayQName(var1, var4.getNamespaceURI());
      LocalElement var7 = var3.addNewElement();
      var7.setName(var5);
      var7.setType(var6);
   }

   private void generateXmlBeanArrayType(JClass var1, SchemaDocument.Schema var2, String var3) {
      JavaTypeName var4 = JavaTypeName.forJClass(var1);
      int var5 = var4.getArrayDepth();
      if (var5 > 0) {
         JavaTypeName var6 = var4.getArrayTypeMinus1Dim(var5);
         String var7 = "";
         if (var5 > 1) {
            for(int var8 = 0; var8 < var5 - 1; ++var8) {
               var7 = var7 + "[]";
            }
         }

         String var17 = var6.getClassName() + var7;
         JClass var9 = var1.forName(var17);
         QName var10 = this.xmlBeanArrayQName(var9, var3);
         QName var11 = this.xmlBeanArrayQName(var1, var3);
         if (!this.generatedXmlBeanArraySchemaTypeQName(var10) && var5 > 1) {
            this.generateXmlBeanArrayType(var9, var2, var3);
         }

         if (!this.generatedXmlBeanArraySchemaTypeQName(var11)) {
            if (verbose) {
               Verbose.log((Object)("  generate xmlBeanArrayType '" + var11 + "'"));
            }

            this.addXmlBeanArraySchemaTypeQName(var11);
            TopLevelComplexType var12 = var2.addNewComplexType();
            var12.setName(var11.getLocalPart());
            ExplicitGroup var13 = var12.addNewSequence();
            LocalElement var14 = var13.addNewElement();
            if (var5 == 1) {
               if (XBeanUtil.xmlBeanIsDocumentType(this.mJ2SBindingsBuilder.getXmlObjectClassLoader(), var1.getArrayComponentType().getQualifiedName())) {
                  QName var15 = XBeanUtil.getQNameFromXmlBean(this.mJ2SBindingsBuilder.getXmlObjectClassLoader(), var9.getQualifiedName());
                  var14.setRef(var15);
               } else {
                  JClass var18 = var1.getArrayComponentType();
                  QName var16 = XBeanUtil.getQNameFromXmlBean(this.mJ2SBindingsBuilder.getXmlObjectClassLoader(), var18.getQualifiedName());
                  var14.setName(var9.getSimpleName() + "Elem");
                  var14.setType(var16);
               }
            } else {
               var14.setName(var10.getLocalPart() + "Elem");
               var14.setType(var10);
            }

            var14.setMinOccurs(BigInteger.ZERO);
            var14.setMaxOccurs("unbounded");
            var14.setNillable(true);
         }

      }
   }

   protected static SchemaDocument.Schema getSchemaFor(String var0, Map var1) {
      SchemaDocument var2 = (SchemaDocument)var1.get(var0);
      if (var2 == null) {
         var2 = Factory.newInstance();
         if (var2.getSchema() == null) {
            var2.addNewSchema();
         }

         var2.getSchema().setTargetNamespace(var0);
         var1.put(var0, var2);
      }

      return var2.getSchema();
   }

   protected Set findOrCreateElementNameSetFor(String var1, JClass var2, JClass var3) {
      if (this.typeIs1DPrimitiveByteArray(var3) && this.jaxRpcByteArrayStyle) {
         if (this.mArrayInfo2SetOfPrimitiveByteArrayElemQNamesInEncodedStyle == null) {
            this.mArrayInfo2SetOfPrimitiveByteArrayElemQNamesInEncodedStyle = new HashMap();
         }

         return this.findOrCreateElementNameSetFor(var1, var2, var3, this.mArrayInfo2SetOfPrimitiveByteArrayElemQNamesInEncodedStyle);
      } else {
         if (this.mArray2SetOfSoapArrayElemQNames == null) {
            this.mArray2SetOfSoapArrayElemQNames = new HashMap();
         }

         return this.findOrCreateElementNameSetFor(var1, var2, var3, this.mArray2SetOfSoapArrayElemQNames);
      }
   }

   protected Set findOrCreateElementNameSetFor(String var1, JClass var2, JClass var3, Map var4) {
      ArrayNamespaceInfo var5 = ArrayNameHelper.getArrayNamespaceInfo(var1, var2, var3);
      Object var6 = (Set)var4.get(var5);
      if (var6 == null) {
         var4.put(var5, var6 = new HashSet());
      }

      return (Set)var6;
   }

   protected Set findOrCreateElementNameSetForXBeanLitArray(String var1, JClass var2, JClass var3) {
      if (this.mArray2SetOfXmlBeansLitArrayElemQNames == null) {
         this.mArray2SetOfXmlBeansLitArrayElemQNames = new HashMap();
      }

      ArrayNamespaceInfo var4 = ArrayNameHelper.getArrayNamespaceInfo(var1, var2, var3);
      Object var5 = (Set)this.mArray2SetOfXmlBeansLitArrayElemQNames.get(var4);
      if (var5 == null) {
         this.mArray2SetOfXmlBeansLitArrayElemQNames.put(var4, var5 = new HashSet());
      }

      return (Set)var5;
   }

   public ExplodedTylar bindAsExplodedTylar(File var1) {
      ExplodedTylar var2 = super.bindAsExplodedTylar(var1);
      if (!this.messageSink.getErrorMessages().isEmpty()) {
         throw new BindingException(this.messageSink.getErrorMessages());
      } else {
         return var2;
      }
   }

   private void generateEncodePrimitiveByteArrayElements() {
      if (this.mArrayInfo2SetOfPrimitiveByteArrayElemQNamesInEncodedStyle != null) {
         Map var1 = this.mArrayInfo2SetOfPrimitiveByteArrayElemQNamesInEncodedStyle;
         ArrayNamespaceInfo[] var2 = new ArrayNamespaceInfo[var1.keySet().size()];
         var1.keySet().toArray(var2);
         JavaTypeName var3 = JavaTypeName.forString("byte[]");
         JavaTypeName var4 = JavaTypeName.forString("byte");
         JClass var5 = var2[0].getServiceClass().forName("byte");
         BindingTypeName var6 = this.getBindingLoader().lookupTypeFor(var4);
         if (var6 == null) {
            throw new IllegalArgumentException("could not find binding type for literal array element class type: " + var5);
         } else {
            QName var7 = var6.getXmlName().getQName();

            for(int var8 = 0; var8 < var2.length; ++var8) {
               Object var9 = (Set)var1.get(var2[var8]);
               if (var9 == null) {
                  var1.put(var2[var8], var9 = new HashSet());
               }

               QName[] var10 = new QName[((Set)var9).size()];
               ((Set)var9).toArray(var10);
               this.createLitArraySchemaElementsAndBindings(var3, XS_BASE_64_BINARY, var10);
            }

         }
      }
   }

   public void setJaxRpcByteArrayStyle(boolean var1) {
      this.jaxRpcByteArrayStyle = var1;
   }

   public void includeSchema(SchemaDocument var1, String var2, Map var3) {
      super.includeSchema(var1, var2, var3);
      this.mXBeanIncludedSchemas.add(var1);
   }

   protected boolean isTopLevelElementExist(QName var1, String var2) {
      Iterator var3 = this.mXBeanIncludedSchemas.iterator();

      SchemaDocument var4;
      do {
         if (!var3.hasNext()) {
            var3 = this.mTns2Schemadoc.keySet().iterator();

            String var5;
            do {
               if (!var3.hasNext()) {
                  return false;
               }

               var5 = (String)var3.next();
            } while(!ObjectUtil.equals(var2, var5) || !containsElementNamed(((SchemaDocument)this.mTns2Schemadoc.get(var5)).getSchema(), var1.getLocalPart()));

            this.logWarning("Element named '" + var1 + "' was already imported from XmlBeans Schema");
            return true;
         }

         var4 = (SchemaDocument)var3.next();
      } while(!ObjectUtil.equals(var2, var4.getSchema().getTargetNamespace()) || !containsElementNamed(var4.getSchema(), var1.getLocalPart()));

      this.logWarning("Element named '" + var1 + "' was already imported from XmlBeans Schema");
      return true;
   }

   protected boolean isTopLevelTypeExist(QName var1, String var2) {
      Iterator var3 = this.mXBeanIncludedSchemas.iterator();

      SchemaDocument var4;
      do {
         if (!var3.hasNext()) {
            var3 = this.mTns2Schemadoc.keySet().iterator();

            String var5;
            do {
               if (!var3.hasNext()) {
                  return false;
               }

               var5 = (String)var3.next();
            } while(!ObjectUtil.equals(var2, var5) || !containsTypeNamed(((SchemaDocument)this.mTns2Schemadoc.get(var5)).getSchema(), var1.getLocalPart()));

            this.logWarning("Schema Type named '" + var1 + "' was already imported from XmlBeans Schema");
            return true;
         }

         var4 = (SchemaDocument)var3.next();
      } while(!ObjectUtil.equals(var2, var4.getSchema().getTargetNamespace()) || !containsTypeNamed(var4.getSchema(), var1.getLocalPart()));

      this.logWarning("Schema Type named '" + var1 + "' was already imported from XmlBeans Schema");
      return true;
   }

   protected boolean isSoapEncSchemaDocExist() {
      Iterator var1 = this.mXBeanIncludedSchemas.iterator();

      SchemaDocument var2;
      do {
         if (!var1.hasNext()) {
            var1 = this.mTns2Schemadoc.keySet().iterator();

            String var3;
            do {
               if (!var1.hasNext()) {
                  return false;
               }

               var3 = (String)var1.next();
            } while(!ObjectUtil.equals(var3, SOAP_ENC_SCHEMA_DOC.getSchema().getTargetNamespace()));

            this.logWarning("SOAP_ENC_SCHEMA_DOC was already imported from XmlBeans Schema");
            return true;
         }

         var2 = (SchemaDocument)var1.next();
      } while(!ObjectUtil.equals(SOAP_ENC_SCHEMA_DOC.getSchema().getTargetNamespace(), var2.getSchema().getTargetNamespace()));

      this.logWarning("SOAP_ENC_SCHEMA_DOC was already imported from XmlBeans Schema");
      return true;
   }

   protected static boolean containsTypeNamed(SchemaDocument.Schema var0, String var1) {
      TopLevelComplexType[] var2 = var0.getComplexTypeArray();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (ObjectUtil.equals(var1, var2[var3].getName())) {
            return true;
         }
      }

      TopLevelSimpleType[] var5 = var0.getSimpleTypeArray();

      for(int var4 = 0; var4 < var5.length; ++var4) {
         if (ObjectUtil.equals(var1, var5[var4].getName())) {
            return true;
         }
      }

      return false;
   }

   static {
      SOAPELEMENT_JTN = JavaTypeName.forClassName(TylarJ2SBindingsBuilderImpl.SOAPELEMENT_CLASSNAME);
      SOAPELEMENT_CLASSNAME = SOAPElement.class.getName();
      SOAPENC_ARRAY = new QName("http://schemas.xmlsoap.org/soap/encoding/", "Array");
      SOAPENC_ARRAYTYPE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "arrayType");
      XSD_ANYTYPE = new QName("http://www.w3.org/2001/XMLSchema", "anyType");
      SchemaDocument var0 = null;

      try {
         SchemaTypeLoader var1 = XmlBeans.typeLoaderForClassLoader(SoapAwareJava2Schema.class.getClassLoader());
         InputStream var2 = SoapAwareJava2Schema.class.getResourceAsStream("/weblogic/wsee/wsdl/schema/soap-encoding-11.xsd");
         var0 = (SchemaDocument)var1.parse(var2, SchemaDocument.type, (XmlOptions)null);
      } catch (Throwable var3) {
      }

      SOAP_ENC_SCHEMA_DOC = var0;
   }

   private static class ElementTypePair {
      private QName element = null;
      private JClass clazz = null;

      public ElementTypePair(QName var1, JClass var2) {
         this.element = var1;
         this.clazz = var2;
      }
   }
}
