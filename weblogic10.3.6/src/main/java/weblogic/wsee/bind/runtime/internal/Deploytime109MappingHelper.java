package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.Java2Schema;
import com.bea.staxb.buildtime.internal.bts.BindingFile;
import com.bea.staxb.buildtime.internal.bts.BindingLoader;
import com.bea.staxb.buildtime.internal.bts.BindingType;
import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.buildtime.internal.bts.BuiltinBindingLoader;
import com.bea.staxb.buildtime.internal.bts.ByNameBean;
import com.bea.staxb.buildtime.internal.bts.CompositeBindingLoader;
import com.bea.staxb.buildtime.internal.bts.GenericXmlProperty;
import com.bea.staxb.buildtime.internal.bts.JavaTypeName;
import com.bea.staxb.buildtime.internal.bts.JaxRpcBuiltinBindingLoader;
import com.bea.staxb.buildtime.internal.bts.JaxrpcEnumType;
import com.bea.staxb.buildtime.internal.bts.ListArrayType;
import com.bea.staxb.buildtime.internal.bts.MethodName;
import com.bea.staxb.buildtime.internal.bts.QNameProperty;
import com.bea.staxb.buildtime.internal.bts.SimpleBindingType;
import com.bea.staxb.buildtime.internal.bts.SimpleContentBean;
import com.bea.staxb.buildtime.internal.bts.SimpleContentProperty;
import com.bea.staxb.buildtime.internal.bts.SimpleDocumentBinding;
import com.bea.staxb.buildtime.internal.bts.SoapArrayType;
import com.bea.staxb.buildtime.internal.bts.WrappedArrayType;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.staxb.buildtime.internal.tylar.TylarImpl;
import com.bea.staxb.buildtime.internal.tylar.TylarWriter;
import com.bea.staxb.runtime.internal.AnyRuntimeBindingType;
import com.bea.xbean.schema.BuiltinSchemaTypeSystem;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.SchemaGlobalElement;
import com.bea.xml.SchemaParticle;
import com.bea.xml.SchemaProperty;
import com.bea.xml.SchemaType;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.SchemaTypeSystem;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
import com.bea.xml.XmlOptions;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.j2ee.descriptor.ConstructorParameterOrderBean;
import weblogic.j2ee.descriptor.ExceptionMappingBean;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.JavaXmlTypeMappingBean;
import weblogic.j2ee.descriptor.PackageMappingBean;
import weblogic.j2ee.descriptor.VariableMappingBean;
import weblogic.utils.StringUtils;
import weblogic.wsee.bind.BaseTypeLoaderFactory;
import weblogic.wsee.bind.internal.FormQualifiedHelper;
import weblogic.wsee.util.ExceptionUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlPart;

class Deploytime109MappingHelper {
   private static final boolean mVerbose = Verbose.isVerbose(Deploytime109MappingHelper.class);
   private static final String SCOPE_ELEMENT = "element";
   private static final String SCOPE_COMPLETXTYPE = "complexType";
   private static final String SCOPE_SIMPLETYPE = "simpleType";
   private static final String JAXRPC_CTWSC_GETTER = "get_value";
   private static final String JAXRPC_CTWSC_SETTER = "set_value";
   private static boolean suppressDeployErrorMessage = Boolean.getBoolean("weblogic.wsee.bind.suppressDeployErrorMessage");
   private static boolean suppressSimpleExceptionErrorMessage = Boolean.getBoolean("weblogic.wsee.bind.suppressSimpleExceptionErrorMessage");
   private static boolean setCompileNoUpaRule = Boolean.getBoolean("weblogic.wsee.bind.setCompileNoUpaRule");
   private boolean isSimpleExceptionErrorPresent = false;
   private JavaWsdlMappingBean m109dd;
   private WsdlDefinitions mWsdl;
   private SchemaDocument[] mSchemas;
   private BindingFile mBindingFile = null;
   private BindingLoader mLoader = null;
   private SchemaTypeSystem mSchemaTypeSystem = null;
   private Map mNs2Package = new HashMap();
   private FormQualifiedHelper mFQH;
   private Map mPackage2Ns = new HashMap();
   private AnonymousTypeFinder mATF = null;
   private boolean treatEnumsAsSimpleTypes;
   private HashMap<Integer, SchemaType> simpleContentTypes = new HashMap();

   Deploytime109MappingHelper(JavaWsdlMappingBean var1, WsdlDefinitions var2, SchemaDocument[] var3, boolean var4) throws IOException, XmlException {
      this.treatEnumsAsSimpleTypes = var4;
      this.m109dd = var1;
      this.mSchemas = var3;
      this.mWsdl = var2;
      this.mFQH = FormQualifiedHelper.getInstance();
      this.debugInputs();
      this.initSchemaTypeSystem();
      this.processPackageMappings();
      this.initBindingFileFrom109dd();
   }

   Tylar buildTylar() throws XmlException, IOException {
      try {
         this.processExceptionMappings();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      this.debugOutputs();
      TylarImpl var1 = new TylarImpl();
      this.writeTylar(var1);
      return var1;
   }

   private void initSchemaTypeSystem() throws IOException, XmlException {
      XmlOptions var1 = new XmlOptions();
      var1.setCompileDownloadUrls();
      var1.setLoadUseDefaultResolver();
      var1.setCompileNoAnnotations();
      var1.setCompileNoPvrRule();
      if (setCompileNoUpaRule) {
         var1.setCompileNoUpaRule();
      }

      SchemaTypeLoader var2 = BaseTypeLoaderFactory.newInstance((Tylar)null);
      if (mVerbose) {
         for(int var3 = 0; var3 < this.mSchemas.length; ++var3) {
            SchemaDocument var4 = this.mSchemas[var3];
            Verbose.log((Object)("schema " + var3 + " " + var4.documentProperties().getSourceName()));
         }
      }

      ArrayList var8 = new ArrayList();
      Collections.addAll(var8, this.mSchemas);
      if (this.mWsdl != null && this.mWsdl.getTypes() != null) {
         SchemaDocument[] var9 = this.mWsdl.getTypes().getSchemaArray();
         int var5 = var9.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            SchemaDocument var7 = var9[var6];
            if (!var8.contains(var7) && var7.getSchema().getTargetNamespace() != null && !var2.isNamespaceDefined(var7.getSchema().getTargetNamespace())) {
               var8.add(var7);
            }
         }
      }

      if (!var8.isEmpty()) {
         Iterator var10 = var8.iterator();

         while(var10.hasNext()) {
            SchemaDocument var11 = (SchemaDocument)var10.next();
            String var12 = fixFileUri(var11.documentProperties().getSourceName());
            if (var12 != null) {
               var11.documentProperties().setSourceName(var12);
            }
         }
      }

      this.mSchemaTypeSystem = XmlBeans.compileXsd((XmlObject[])var8.toArray(new SchemaDocument[0]), var2, var1);
   }

   private void processPackageMappings() {
      if (this.m109dd != null) {
         PackageMappingBean[] var1 = this.m109dd.getPackageMappings();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            PackageMappingBean var3 = var1[var2];
            this.mNs2Package.put(var3.getNamespaceURI(), var3.getPackageType());
            this.mPackage2Ns.put(var3.getPackageType(), var3.getNamespaceURI());
         }

      }
   }

   private void initBindingFileFrom109dd() {
      this.mBindingFile = new BindingFile();
      this.mLoader = CompositeBindingLoader.forPath(new BindingLoader[]{JaxRpcBuiltinBindingLoader.getInstance(), this.mBindingFile});
      if (this.m109dd != null) {
         this.processTypeMappings();
      }

   }

   private void processExceptionMappings() {
      if (this.m109dd != null) {
         ExceptionMappingBean[] var1 = this.m109dd.getExceptionMappings();
         if (var1 != null && var1.length != 0) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
               ByNameBean var3 = this.getTypeBindingForException(var1[var2]);
               if (var3 == null) {
                  if (!this.isSimpleExceptionErrorPresent) {
                     this.logError(var1[var2].getExceptionType() + " is not understood because " + "there is no type mapping for exception class");
                  } else {
                     this.isSimpleExceptionErrorPresent = false;
                  }
               } else {
                  ConstructorParameterOrderBean var4 = var1[var2].getConstructorParameterOrder();
                  if (var4 != null && var4.getElementNames() != null) {
                     String[] var5 = var4.getElementNames();

                     for(int var6 = 0; var6 < var5.length; ++var6) {
                        QName var7 = new QName(var5[var6]);
                        QNameProperty var8 = this.mFQH.getPropertyForElement(var3, var5[var6]);
                        if (var8 == null) {
                           var8 = this.mFQH.getPropertyForAttribute(var3, var5[var6]);
                        }

                        if (var8 != null) {
                           var8.setCtorParamIndex(var6);
                        } else {
                           StringWriter var9 = new StringWriter();
                           var9.write("<exception-mapping> for " + var1[var2].getExceptionType() + " specifies constructor parameter for unbound property '" + var5[var6] + "' (" + var7 + ")\n  Available properties are:\n");
                           Iterator var10 = var3.getProperties().iterator();

                           while(var10.hasNext()) {
                              var9.write("  ");
                              var9.write(((QNameProperty)var10.next()).getQName().toString());
                              var9.write(10);
                           }

                           this.logError(var9.toString());
                        }
                     }
                  }
               }
            }

         }
      }
   }

   private ByNameBean getTypeBindingForException(ExceptionMappingBean var1) {
      QName var2 = var1.getWsdlMessage();
      String var3 = var1.getWsdlMessagePartName();
      WsdlPart var4 = this.getPartNamed(var2, var3);
      if (var4 == null) {
         return null;
      } else {
         String var5 = var1.getExceptionType();
         BindingTypeName var6 = null;
         QName var7;
         if (var4.getType() != null) {
            var7 = var4.getType();
            var6 = BindingTypeName.forPair(JavaTypeName.forString(var5), XmlTypeName.forTypeNamed(var7));
         } else {
            if (var4.getElement() == null) {
               this.logError("WSDL message part '" + var1.getWsdlMessagePartName() + " must specify 'type' or 'attribute'");
               return null;
            }

            var7 = var4.getElement();
            var6 = BindingTypeName.forPair(JavaTypeName.forString(var5), XmlTypeName.forGlobalName('e', var7));
            BindingType var8 = this.mBindingFile.getBindingType(var6);
            if (var8 == null) {
               String var9 = "While processing <exception-mapping> for wsdlMessageName='" + var2 + "', wsdlMessagePartElement='" + var4.getElement() + "'.  Unable to find a BindingType in the binding file " + " for javaTypeName ='" + JavaTypeName.forString(var5) + "', xmlTypeName='" + XmlTypeName.forGlobalName('e', var7) + "'.  The cause of this error is likely because " + "an <exception-mapping> specified for " + var2 + "requires that a <java-xml-type-mapping> exist for java type='" + var5 + "', xmlTypeName='" + XmlTypeName.forGlobalName('e', var7) + "', with a <root-type-qname> of  " + var7;

               try {
                  this.registerElement(var7);
                  var6 = BindingTypeName.forPair(JavaTypeName.forString(var5), XmlTypeName.forGlobalName('e', var7));
                  var8 = this.mBindingFile.getBindingType(var6);
                  if (var8 == null) {
                     if (!suppressSimpleExceptionErrorMessage) {
                        this.logError(var9);
                     } else {
                        this.isSimpleExceptionErrorPresent = true;
                     }

                     return null;
                  }
               } catch (Exception var11) {
                  this.logError(var9);
                  return null;
               }
            }

            if (!(var8 instanceof SimpleDocumentBinding)) {
               this.logError("Internal error: generated a " + var8.getClass() + " for " + var7 + "; expected it to be a " + SimpleDocumentBinding.class);
               return null;
            }

            XmlTypeName var13 = ((SimpleDocumentBinding)var8).getTypeOfElement();
            if (var13 == null) {
               this.logError("Internal error: SimpleDocumentBinding for " + var7 + " has a null TypeOfElement.");
               return null;
            }

            var6 = BindingTypeName.forPair(JavaTypeName.forString(var5), var13);
         }

         BindingType var12 = this.mBindingFile.getBindingType(var6);
         if (var12 == null) {
            this.logError("Internal error: no binding available for " + var6);
            return null;
         } else if (!(var12 instanceof ByNameBean)) {
            this.logError("Internal error: generated a " + var12.getClass() + " for " + var6 + "; expected a " + ByNameBean.class);
            return null;
         } else {
            return (ByNameBean)var12;
         }
      }
   }

   private WsdlPart getPartNamed(QName var1, String var2) {
      if (this.mWsdl == null) {
         return null;
      } else {
         WsdlMessage var3 = (WsdlMessage)this.mWsdl.getMessages().get(var1);
         if (var3 == null) {
            this.logError("<exception-mapping> specified for non-existent WSDL message named '" + var1);
            return null;
         } else if (var2 == null) {
            Iterator var6 = var3.getParts().values().iterator();
            if (!var6.hasNext()) {
               this.logError("<exception-mapping> specified for WSDL message named '" + var1 + "' which has no parts!");
               return null;
            } else {
               WsdlPart var5 = (WsdlPart)var6.next();
               if (var6.hasNext()) {
                  this.logError("<exception-mapping> specified for multi-part WSDL message named '" + var1 + "' must have a " + "<wsdl-message-part-name> element");
                  return null;
               } else {
                  return var5;
               }
            }
         } else {
            WsdlPart var4 = (WsdlPart)var3.getParts().get(var2);
            if (var4 == null) {
               this.logError("<exception-mapping> for multi-part WSDL message named '" + var1 + "' specifies a " + "non-existent part named '" + var2 + "'");
               return null;
            } else {
               return var4;
            }
         }
      }
   }

   private void processTypeMappings() {
      JavaXmlTypeMappingBean[] var1 = this.m109dd.getJavaXmlTypeMappings();
      BindingType[] var2 = new BindingType[var1.length];

      int var3;
      for(var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.createBindingTypeFrom(var3, var1[var3]);
      }

      for(var3 = 0; var3 < var1.length; ++var3) {
         if (var2[var3] != null) {
            BindingTypeName var6;
            if (var2[var3] instanceof WrappedArrayType) {
               WrappedArrayType var14 = (WrappedArrayType)var2[var3];
               if (var14.getName().getJavaName().getArrayDepth() == 0) {
                  BindingTypeName var18 = var14.getItemType();
                  var6 = this.mLoader.lookupPojoFor(var18.getXmlName());
                  if (var6 == null) {
                     throw new IllegalStateException("For ArrayType '" + var14.getName().getJavaName() + "', Array item type " + var14.getItemType() + " is not mapped in jaxrpc mapping file.");
                  }

                  var14.setItemType(var6);
               }
            } else {
               if (var2[var3] instanceof SoapArrayType) {
                  SoapArrayType var4 = (SoapArrayType)var2[var3];
                  String var5 = var4.getItemType().getJavaName().toString();
                  if (!ExceptionUtil.classNameIsSchemaBuiltin(var5)) {
                     if (var4.getName().getJavaName().getArrayDepth() > 0) {
                        var6 = var4.getItemType();
                        BindingTypeName var7 = this.mLoader.lookupPojoFor(var6.getXmlName());
                        if (var7 == null) {
                           throw new IllegalStateException("For ArrayType '" + var4.getName().getJavaName() + "', non-schema builtin Array item type " + var4.getItemType() + " is not mapped in jaxrpc mapping file.");
                        }

                        var4.setItemType(var7);
                     }

                     (new SoapArrayHelper(this.mSchemaTypeSystem, this.mLoader, this.mBindingFile)).checkArrayItem(var4);
                     continue;
                  }
               } else if (var2[var3] instanceof SimpleContentBean) {
                  JavaTypeName var12 = JavaTypeName.forString(var1[var3].getJavaType());
                  SchemaType var15 = (SchemaType)this.simpleContentTypes.get(var3);
                  SimpleContentProperty var17 = this.createJaxRpcSimpleContentPropertyFor(var12, var15);
                  if (var17 == null) {
                     var2[var3] = null;
                     continue;
                  }

                  ((SimpleContentBean)var2[var3]).setSimpleContentProperty(var17);
               }

               VariableMappingBean[] var13 = var1[var3].getVariableMappings();
               if (var13 != null && var13.length != 0) {
                  XmlTypeName var16 = var2[var3].getName().getXmlName();
                  if (var16.isElement()) {
                     var16 = XmlTypeName.forNestedAnonymous('t', var16);
                  }

                  SchemaType var19 = var16.findTypeIn(this.mSchemaTypeSystem);
                  if (var19 == null) {
                     this.logError("could not find schema type for '" + var2[var3].getName().getXmlName());
                  } else {
                     String var20 = var1[var3].getJavaType();

                     Class var8;
                     try {
                        var8 = Thread.currentThread().getContextClassLoader().loadClass(var20);
                     } catch (ClassNotFoundException var11) {
                        throw new IllegalStateException("ClassNotFound " + var11.getMessage());
                     }

                     String var9 = this.findTargetNamespace(var1[var3]);
                     QNameProperty[] var10 = this.createQNameProps(var9, var13, var8, var19);
                     this.addPropertiesToBinding(var2[var3], var10);
                  }
               }
            }
         }
      }

      this.createInheritedProperties(var1, var2);
   }

   private void createInheritedProperties(JavaXmlTypeMappingBean[] var1, BindingType[] var2) {
      ArrayList var3 = new ArrayList();
      HashMap var4 = new HashMap();
      HashMap var5 = new HashMap();

      for(int var6 = 0; var6 < var1.length; ++var6) {
         if (var1[var6].getRootTypeQname() != null && var2[var6] != null) {
            var4.put(var1[var6].getRootTypeQname(), var1[var6]);
            var5.put(var1[var6].getRootTypeQname(), var2[var6]);
         }
      }

      Iterator var8 = var4.keySet().iterator();

      while(var8.hasNext()) {
         QName var7 = (QName)var8.next();
         this.processInheritedProperties((JavaXmlTypeMappingBean)var4.get(var7), var3, var4, var5);
      }

   }

   private void processInheritedProperties(JavaXmlTypeMappingBean var1, List<QName> var2, Map<QName, JavaXmlTypeMappingBean> var3, Map<QName, BindingType> var4) {
      QName var5 = var1.getRootTypeQname();
      if (var5 != null) {
         if (!var2.contains(var5)) {
            BindingType var6 = (BindingType)var4.get(var5);
            XmlTypeName var7 = var6.getName().getXmlName();
            if (var7.isElement()) {
               var7 = XmlTypeName.forNestedAnonymous('t', var7);
            }

            SchemaType var8 = var7.findTypeIn(this.mSchemaTypeSystem);
            if (var8 != null) {
               ArrayList var9 = new ArrayList();
               boolean var10 = false;
               SchemaType var11 = var8;

               while(var11.getBaseType() != null && !var10) {
                  var11 = var11.getBaseType();
                  QName var12 = var11.getName();
                  if (var3.containsKey(var12)) {
                     JavaXmlTypeMappingBean var13 = (JavaXmlTypeMappingBean)var3.get(var12);
                     BindingType var14 = (BindingType)var4.get(var12);
                     this.processInheritedProperties(var13, var2, var3, var4);
                     var9.addAll(this.getPropertiesFromBinding(var14));
                     var10 = true;
                  }
               }

               if (!var9.isEmpty()) {
                  Collection var18 = this.getPropertiesFromBinding(var6);
                  QNameProperty[] var19 = new QNameProperty[var18.size()];
                  var18.toArray(var19);
                  QNameProperty[] var20 = new QNameProperty[var9.size()];
                  var9.toArray(var20);
                  ArrayList var15 = new ArrayList();
                  var15.addAll(var9);

                  for(int var16 = 0; var16 < var20.length; ++var16) {
                     for(int var17 = 0; var17 < var19.length; ++var17) {
                        if (var20[var16] != null && var19[var17] != null && var20[var16].getQName() != null && var19[var17].getQName() != null && var20[var16].getQName().equals(var19[var17].getQName())) {
                           var15.remove(var20[var16]);
                           break;
                        }
                     }
                  }

                  if (!var15.isEmpty()) {
                     this.addPropertiesToBinding(var6, (QNameProperty[])var15.toArray(new QNameProperty[var15.size()]));
                  }
               }
            }

            if (var5 != null) {
               var2.add(var5);
            }

         }
      }
   }

   void addPropertiesToBinding(BindingType var1, QNameProperty[] var2) {
      QNameProperty[] var3;
      int var4;
      int var5;
      QNameProperty var6;
      if (var1 instanceof ByNameBean) {
         var3 = var2;
         var4 = var2.length;

         for(var5 = 0; var5 < var4; ++var5) {
            var6 = var3[var5];
            if (var6 != null) {
               ByNameBean var7 = (ByNameBean)var1;
               if (var6 instanceof GenericXmlProperty) {
                  var7.setAnyElementProperty((GenericXmlProperty)var6);
               } else {
                  var7.addProperty(var6);
               }
            }
         }
      } else if (var1 instanceof SimpleContentBean) {
         var3 = var2;
         var4 = var2.length;

         for(var5 = 0; var5 < var4; ++var5) {
            var6 = var3[var5];
            if (var6 != null) {
               ((SimpleContentBean)var1).addProperty(var6);
            }
         }
      }

   }

   Collection getPropertiesFromBinding(BindingType var1) {
      Collection var2 = null;
      if (var1 instanceof ByNameBean) {
         var2 = ((ByNameBean)var1).getProperties();
      } else if (var1 instanceof SimpleContentBean) {
         var2 = ((SimpleContentBean)var1).getAttributeProperties();
      }

      return (Collection)(var2 == null ? Collections.EMPTY_LIST : var2);
   }

   void registerElement(QName var1) throws IOException, XmlException {
      SchemaGlobalElement var2 = this.mSchemaTypeSystem.findElement(var1);
      if (var2 == null) {
         throw new IllegalArgumentException(var1 + " is not found in schema.");
      } else {
         XmlTypeName var3 = XmlTypeName.forGlobalName('e', var1);
         BindingTypeName var4 = this.mBindingFile.lookupPojoFor(var3);
         if (var4 == null) {
            var4 = this.mBindingFile.lookupXmlObjectFor(var3);
         }

         if (var4 == null) {
            if (mVerbose) {
               Verbose.log((Object)("Register global element: " + var1));
            }

            XmlTypeName var5 = XmlTypeName.forSchemaType(var2.getType());
            BindingTypeName var6 = this.mLoader.lookupPojoFor(var5);
            if (var6 != null) {
               BindingTypeName var7 = BindingTypeName.forPair(var6.getJavaName(), var3);
               SimpleDocumentBinding var8 = new SimpleDocumentBinding(var7);
               var8.setTypeOfElement(var5);
               this.mBindingFile.addBindingType(var8, false, true);
            } else if (var2.getType().isSimpleType()) {
               BindingType var10 = this.registerType(var2.getType());
               BindingTypeName var11 = BindingTypeName.forPair(var10.getName().getJavaName(), var3);
               SimpleDocumentBinding var9 = new SimpleDocumentBinding(var11);
               var9.setTypeOfElement(var5);
               this.mBindingFile.addBindingType(var9, false, true);
            } else {
               this.logError("Ignoring element declaration " + var1 + " because there is no entry for its type in the " + "JAXRPC mapping file.");
            }
         }

      }
   }

   void registerType(QName var1) throws IOException, XmlException {
      SchemaType var2 = BuiltinSchemaTypeSystem.get().findType(var1);
      if (var2 == null) {
         SchemaTypeLoader var3 = BaseTypeLoaderFactory.newInstance((Tylar)null);
         var2 = var3.findType(var1);
      }

      if (var2 == null) {
         var2 = this.mSchemaTypeSystem.findType(var1);
      }

      if (var2 == null) {
         throw new IllegalArgumentException("Schema global type " + var1 + " is not found in schema.");
      } else {
         this.registerType(var2);
      }
   }

   private BindingType registerType(SchemaType var1) {
      XmlTypeName var2 = XmlTypeName.forSchemaType(var1);
      BindingTypeName var3 = JaxRpcBuiltinBindingLoader.getInstance().lookupPojoFor(var2);
      if (var3 != null) {
         return this.mLoader.getBindingType(var3);
      } else {
         var3 = this.mLoader.lookupPojoFor(var2);
         if (var3 != null) {
            return this.mLoader.getBindingType(var3);
         } else if (SoapArrayHelper.isSoapArray(var1)) {
            SoapArrayType var12 = (new SoapArrayHelper(this.mSchemaTypeSystem, this.mLoader, this.mBindingFile)).createSoapArrayType(var1);
            this.mBindingFile.addBindingType(var12, false, true);
            return var12;
         } else if (LiteralArrayHelper.isLiteralArray(var1)) {
            WrappedArrayType var11 = (new LiteralArrayHelper(this.mSchemaTypeSystem, this.mLoader)).createLiteralArrayType(var1);
            this.mBindingFile.addBindingType(var11, false, true);
            return var11;
         } else {
            BindingTypeName var13;
            if (var1.getListItemType() != null) {
               SchemaType var10 = var1.getListItemType();
               var13 = this.mLoader.lookupPojoFor(XmlTypeName.forSchemaType(var10));
               if (var13 == null) {
                  this.logError("Internal error: no builtin binding found for " + var10);
               }

               JavaTypeName var14 = this.getJavaTypeForSimpleType(var10);
               var14 = JavaTypeName.forArray(var14, 1);
               BindingTypeName var7 = BindingTypeName.forPair(var14, XmlTypeName.forSchemaType(var1));
               ListArrayType var8 = new ListArrayType(var7);
               var8.setItemType(var13);
               this.mBindingFile.addBindingType(var8, false, true);
               return var8;
            } else if (var1.isSimpleType()) {
               JavaTypeName var9 = this.getJavaTypeForSimpleType(var1);
               var13 = BindingTypeName.forPair(var9, XmlTypeName.forSchemaType(var1));
               SimpleBindingType var6 = new SimpleBindingType(var13);
               var6.setAsIfXmlType(XmlTypeName.forSchemaType(getBuiltinBaseType(var1)));
               this.mBindingFile.addBindingType(var6, false, true);
               return var6;
            } else {
               SchemaParticle var4 = var1.getContentModel();
               if (var4 == null) {
                  return null;
               } else {
                  SchemaProperty[] var5;
                  switch (var4.getParticleType()) {
                     case 1:
                        var5 = var1.getElementProperties();
                        return this.createBindingType(var1.getName(), var1, var5);
                     case 2:
                     default:
                        this.logError("unknown schema particle type '" + var4.getParticleType() + "' on " + var1.getName());
                        return null;
                     case 3:
                     case 4:
                        var5 = var1.getElementProperties();
                        return this.createBindingType(var1.getName(), var1, var5);
                  }
               }
            }
         }
      }
   }

   private BindingType createBindingType(QName var1, SchemaType var2, SchemaProperty[] var3) {
      String var4 = (String)this.mNs2Package.get(var1.getNamespaceURI());
      if (var4 == null) {
         this.logError("Type " + var1 + " is not understood because package name " + "mapping for " + var1.getNamespaceURI() + " is not found. " + "You can not use this type in operation calls. Operations that " + "don't use this type are still callable.");
         return null;
      } else {
         String var5 = var4 + "." + ucfirst(var2.getName().getLocalPart());
         if (mVerbose) {
            Verbose.log((Object)("Formed java class name " + var5));
         }

         Class var6 = null;

         try {
            var6 = Thread.currentThread().getContextClassLoader().loadClass(var5);
         } catch (ClassNotFoundException var20) {
            this.logError("Class not found " + var5, var20);
            return null;
         }

         XmlTypeName var7 = XmlTypeName.forTypeNamed(var1);
         JavaTypeName var8 = JavaTypeName.forString(var5);
         BindingTypeName var9 = this.mLoader.lookupPojoFor(var7);
         if (var9 == null) {
            var9 = BindingTypeName.forPair(var8, var7);
            if (mVerbose) {
               Verbose.log((Object)("Adding ByNameBean for non-builtin type " + var7));
            }

            ByNameBean var10 = new ByNameBean(var9);
            this.mBindingFile.addBindingType(var10, true, true);
            Map var11 = this.checkProperties(var6, var3);

            for(int var12 = 0; var12 < var3.length; ++var12) {
               SchemaProperty var13 = var3[var12];
               SchemaType var14 = var13.getType();
               QName var15 = var14.getName();
               if (var15 == null) {
                  this.logError("XMLType " + var2.getName() + " local element " + var13.getName() + " doesn't have a type");
               } else {
                  QNameProperty var16 = new QNameProperty();
                  var16.setQName(var15);
                  var16.setNillable(var13.hasNillable() != 0);
                  BindingType var17 = this.registerType(var14);
                  var16.setBindingType(var17);
                  PropertyDescriptor var18 = (PropertyDescriptor)var11.get(var13.getName().getLocalPart());
                  var18.getPropertyType();
                  JavaTypeName var19 = JavaTypeName.forString(var18.getPropertyType().getName());
                  var16.setSetterName(MethodName.create(var18.getWriteMethod().getName(), var19));
                  var16.setGetterName(MethodName.create(var18.getReadMethod().getName()));
                  var10.addProperty(var16);
               }
            }
         }

         return this.mLoader.getBindingType(var9);
      }
   }

   private Map checkProperties(Class var1, SchemaProperty[] var2) {
      BeanInfo var3;
      try {
         var3 = Introspector.getBeanInfo(var1);
      } catch (IntrospectionException var9) {
         this.logError(var1.getName() + " is not a valid java bean.", var9);
         return null;
      }

      HashMap var4 = new HashMap();

      for(int var5 = 0; var5 < var2.length; ++var5) {
         var4.put(var2[var5].getName().getLocalPart(), var2[var5]);
      }

      HashMap var10 = new HashMap();
      PropertyDescriptor[] var6 = var3.getPropertyDescriptors();

      for(int var7 = 0; var7 < var6.length; ++var7) {
         PropertyDescriptor var8 = var6[var7];
         if (!"class".equals(var8.getName())) {
            if (var4.get(var8.getName()) == null) {
               this.logError(var1.getName() + " java bean " + "properties don't match with schema. " + "property " + var8.getName() + " is not found in schema.");
               return null;
            }

            var4.remove(var8.getName());
            var10.put(var8.getName(), var8);
         }
      }

      if (var4.size() != 0) {
         this.logError(var1.getName() + " java bean " + "properties don't match with schema. " + "Scheam type has more properites " + var4.keySet());
      }

      return var10;
   }

   private SchemaType getBaseTypeForComplexTypeWithSimpleContent(SchemaType var1) {
      if (var1.getContentType() == 2) {
         SchemaType var2;
         for(var2 = var1; var2 != null && !var2.isSimpleType(); var2 = var2.getContentBasedOnType()) {
         }

         if (var2 != null && var2.isSimpleType()) {
            if (var2.getListItemType() == null && var2.getBaseEnumType() == null) {
               return getBuiltinBaseType(var2);
            }

            return var2;
         }
      }

      return null;
   }

   private String findTargetNamespace(JavaXmlTypeMappingBean var1) {
      QName var2 = var1.getRootTypeQname();
      if (var2 != null) {
         return var2.getNamespaceURI();
      } else {
         String var3 = var1.getAnonymousTypeQname();
         if (var3 != null) {
            int var4 = var3.lastIndexOf(58);
            return var4 == -1 ? var3 : var3.substring(0, var4);
         } else {
            throw new AssertionError("no root name or anonymous name specified");
         }
      }
   }

   private BindingType createBindingTypeFrom(int var1, JavaXmlTypeMappingBean var2) {
      String var3 = var2.getQnameScope();
      if (var3.equals("element")) {
         return this.bindElementScopedMapping(var2);
      } else {
         QName var4 = var2.getRootTypeQname();
         boolean var5 = var4 == null;
         if (var5 && var2.getAnonymousTypeQname() == null) {
            throw new IllegalStateException("<java-xml-type-mapping>  element has neither a <root-type-qname> nor an <anonymous-type-qname> - exactly one is required.");
         } else {
            SchemaType var6 = null;
            SchemaProperty var7 = null;
            if (var5) {
               if (this.mATF == null) {
                  this.mATF = new AnonymousTypeFinder(this.mSchemaTypeSystem);
               }

               var6 = this.mATF.getTypeNamed(var2.getAnonymousTypeQname());
               if (var6 == null) {
                  var7 = this.mATF.getHiddenArrayElementComponentTypeNamed(var2.getAnonymousTypeQname());
                  if (var7 == null) {
                     this.logWarning("could not identify anonymous schema type named '" + var2.getAnonymousTypeQname() + "', ignoring");
                     return null;
                  }

                  var6 = var7.getContainerType();
               }
            } else {
               var6 = this.mSchemaTypeSystem.findType(var4);
               if (var6 == null) {
                  SchemaGlobalElement var8 = this.mSchemaTypeSystem.findElement(var4);
                  if (var8 != null) {
                     var6 = var8.getType();
                  }
               }

               if (var6 == null) {
                  this.logError("could not find schema type '" + var4);
                  return null;
               }
            }

            XmlTypeName var15 = XmlTypeName.forSchemaType(var6);
            JavaTypeName var9 = JavaTypeName.forString(var2.getJavaType());
            BindingTypeName var17;
            if (Java2Schema.isCollectionType(var9.getClassName())) {
               var17 = BindingTypeName.forPair(var9, var15);
               boolean var23 = SoapArrayHelper.isSoapArray(var6);
               Object var31;
               if (var23) {
                  var31 = Java2Schema.createSoapEncCollectionType(var17);
               } else {
                  var31 = Java2Schema.createCollectionType(var17);
               }

               this.mBindingFile.addBindingType((BindingType)var31, false, true);
               return (BindingType)var31;
            } else {
               BindingTypeName var27;
               if (var9.getArrayDepth() != 0) {
                  if (SoapArrayHelper.isSoapArray(var6)) {
                     List var19 = (new SoapArrayHelper(this.mSchemaTypeSystem, this.mLoader, this.mBindingFile)).createSoapArrayType(var9, var6);
                     Iterator var21 = var19.iterator();

                     while(var21.hasNext()) {
                        this.mBindingFile.addBindingType((SoapArrayType)var21.next(), false, true);
                     }

                     return (SoapArrayType)var19.get(0);
                  }

                  if (LiteralArrayHelper.isLiteralArray(var6)) {
                     WrappedArrayType var18 = (new LiteralArrayHelper(this.mSchemaTypeSystem, this.mLoader)).createLiteralArrayType(var9, var6);
                     this.mBindingFile.addBindingType(var18, false, true);
                     return var18;
                  }

                  if (var7 != null) {
                     return null;
                  }

                  if (var6.getListItemType() != null) {
                     var17 = BindingTypeName.forPair(var9, var15);
                     ListArrayType var20 = new ListArrayType(var17);
                     SchemaType var30 = var6.getListItemType();
                     if (!var30.isPrimitiveType()) {
                        if (var30.getEnumerationValues() == null && !var30.isAnonymousType()) {
                           if (var30.getUnionMemberTypes() != null && var30.getUnionMemberTypes().length > 0) {
                              var30 = var30.getUnionCommonBaseType();
                           }
                        } else {
                           var30 = var30.getBaseType();
                        }
                     }

                     var27 = BindingTypeName.forPair(var9.getArrayItemType(var9.getArrayDepth()), XmlTypeName.forSchemaType(var30));
                     var20.setItemType(var27);
                     this.mBindingFile.addBindingType(var20, false, true);
                     return var20;
                  }

                  if (!var6.isSimpleType()) {
                     throw new IllegalStateException("JavaType " + var2.getJavaType() + " is" + " a array. XmlType " + var6.getName() + " is not.");
                  }
               }

               BindingTypeName var11;
               if (var5) {
                  if (var7 != null) {
                     var11 = BindingTypeName.forPair(var9, var15);
                     WrappedArrayType var29 = new WrappedArrayType(var11);
                     var29.setItemName(var7.getName());
                     var29.setItemNillable(true);
                     var27 = BindingTypeName.forPair(var9.getArrayItemType(var9.getArrayDepth()), XmlTypeName.forSchemaType(var7.getType()));
                     var29.setItemType(var27);
                     this.mBindingFile.addBindingType(var29, false, true);
                     return var29;
                  }

                  XmlTypeName var10 = var15.getOuterComponent();
                  if (this.mBindingFile.lookupPojoFor(var10) == null && this.mBindingFile.lookupXmlObjectFor(var10) == null) {
                     var11 = BindingTypeName.forPair(var9, var10);
                     SimpleDocumentBinding var12 = new SimpleDocumentBinding(var11);
                     var12.setTypeOfElement(var15);
                     this.mBindingFile.addBindingType(var12, false, true);
                  }
               }

               if (BuiltinBindingLoader.getInstance().lookupPojoFor(var15) != null) {
                  return null;
               } else {
                  SchemaType var16 = this.getBaseTypeForComplexTypeWithSimpleContent(var6);
                  var11 = BindingTypeName.forPair(var9, var15);
                  if (var3.equals("complexType")) {
                     if (var16 == null) {
                        ByNameBean var28 = new ByNameBean(var11);
                        this.mBindingFile.addBindingType(var28, true, true);
                        return var28;
                     } else {
                        this.simpleContentTypes.put(var1, var16);
                        SimpleContentBean var26 = new SimpleContentBean(var11);
                        this.mBindingFile.addBindingType(var26, true, true);
                        return var26;
                     }
                  } else if (var3.equals("simpleType")) {
                     SchemaType var13;
                     BindingTypeName var14;
                     if (var6.getEnumerationValues() != null && !this.treatEnumsAsSimpleTypes) {
                        JaxrpcEnumType var25 = new JaxrpcEnumType(var11);
                        var25.setGetValueMethod(JaxrpcEnumType.DEFAULT_GET_VALUE);
                        var25.setFromStringMethod(JaxrpcEnumType.DEFAULT_FROM_STRING);
                        var13 = getBuiltinBaseType(var6);
                        var14 = this.mLoader.lookupPojoFor(XmlTypeName.forSchemaType(var13));
                        if (var14 == null) {
                           this.logError("binding for " + var13 + " not in loader");
                           return null;
                        } else {
                           var25.setBaseType(var14);
                           var25.setFromValueMethod(MethodName.create("fromValue", var14.getJavaName()));
                           this.mBindingFile.addBindingType(var25, true, true);
                           return var25;
                        }
                     } else if (var6.getListItemType() != null) {
                        ListArrayType var24 = new ListArrayType(var11);
                        var13 = var6.getListItemType();
                        var14 = this.mLoader.lookupPojoFor(XmlTypeName.forSchemaType(var13));
                        if (var14 == null) {
                           this.logError("could not find list item biding for schema type " + var13.getName());
                           return null;
                        } else {
                           var24.setItemType(var14);
                           this.mBindingFile.addBindingType(var24, true, true);
                           return var24;
                        }
                     } else {
                        SimpleBindingType var22 = new SimpleBindingType(var11);
                        if (!var6.isSimpleType()) {
                           this.logError(var4 + " is not a simple type but is mapped with simple type scope");
                           return null;
                        } else {
                           var22.setAsIfXmlType(XmlTypeName.forSchemaType(getBuiltinBaseType(var6)));
                           this.mBindingFile.addBindingType(var22, true, true);
                           return var22;
                        }
                     }
                  } else {
                     this.logError("unknown qname-scope '" + var3 + "' on <java-xml-type mapping> " + "for " + var2.getJavaType());
                     return null;
                  }
               }
            }
         }
      }
   }

   private BindingType bindElementScopedMapping(JavaXmlTypeMappingBean var1) {
      QName var2 = var1.getRootTypeQname();
      if (var2 == null) {
         this.logWarning("Ignoring <java-xml-mapping-bean> for " + var1.getJavaType() + "because it is anonymous and has element scope.");
         return null;
      } else {
         XmlTypeName var3 = XmlTypeName.forGlobalName('e', var2);
         JavaTypeName var4 = JavaTypeName.forString(var1.getJavaType());
         BindingTypeName var5 = BindingTypeName.forPair(var4, var3);
         SimpleDocumentBinding var6 = new SimpleDocumentBinding(var5);
         SchemaGlobalElement var7 = this.mSchemaTypeSystem.findElement(var2);
         if (var7 == null) {
            this.logError("could not locate schema element named " + var2);
            return null;
         } else {
            SchemaType var8 = var7.getType();
            if (var8 == null) {
               this.logError("schema element named " + var2 + " has no type");
               return null;
            } else {
               XmlTypeName var9 = XmlTypeName.forSchemaType(var8);
               var6.setTypeOfElement(var9);
               this.mBindingFile.addBindingType(var6, false, true);
               return var6;
            }
         }
      }
   }

   private SimpleContentProperty createJaxRpcSimpleContentPropertyFor(JavaTypeName var1, SchemaType var2) {
      SimpleContentProperty var3 = new SimpleContentProperty();
      BindingType var4 = this.mLoader.getBindingType(this.mLoader.lookupPojoFor(XmlTypeName.forSchemaType(var2)));
      if (var4 == null) {
         this.logError("Could not find built-in binding for " + var2.getName());
         return null;
      } else {
         var3.setSetterName(MethodName.create("set_value", var4.getName().getJavaName()));
         var3.setGetterName(MethodName.create("get_value"));
         var3.setBindingType(var4);
         return var3;
      }
   }

   private QNameProperty[] createQNameProps(String var1, VariableMappingBean[] var2, Class var3, SchemaType var4) {
      if (var1 == null) {
         throw new IllegalArgumentException("null tns");
      } else if (var2 == null) {
         throw new IllegalArgumentException("null vars");
      } else if (var4 == null) {
         throw new IllegalArgumentException("null schemaType");
      } else if (var3 == null) {
         throw new IllegalArgumentException("null javaClass");
      } else {
         QNameProperty[] var5 = new QNameProperty[var2.length];
         BeanInfo var6 = null;

         try {
            var6 = Introspector.getBeanInfo(var3);
         } catch (IntrospectionException var19) {
            this.logError(var3.getName() + " is not a valid java bean.", var19);
         }

         HashMap var7 = new HashMap();
         PropertyDescriptor[] var9;
         if (var6 != null) {
            PropertyDescriptor[] var8 = var6.getPropertyDescriptors();
            var9 = var8;
            int var10 = var8.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               PropertyDescriptor var12 = var9[var11];
               var7.put(ucfirst(var12.getName()), var12);
            }
         }

         for(int var20 = 0; var20 < var2.length; ++var20) {
            if (var2[var20].getXmlWildcard() != null) {
               var5[var20] = new GenericXmlProperty();
            } else {
               var5[var20] = new QNameProperty();
            }

            var9 = null;
            Method var22 = null;
            Method var23 = null;
            Field var24 = getJavaVariablePublicField(var3, var2[var20]);
            Class var21;
            if (var24 != null) {
               var21 = var24.getType();
            } else {
               String var13 = var2[var20].getJavaVariableName();
               PropertyDescriptor var14 = (PropertyDescriptor)var7.get(ucfirst(var13));
               if (var14 != null) {
                  var22 = var14.getReadMethod();
                  var23 = var14.getWriteMethod();
               }

               if (var22 == null) {
                  this.logError("could not find getter for property '" + var2[var20].getJavaVariableName() + "' on " + var3.getName());
                  var5[var20] = null;
                  continue;
               }

               var21 = var22.getReturnType();
               if (var23 == null && !isException(var3)) {
                  this.logError("Could not find a setter for property '" + var2[var20].getJavaVariableName() + "' on " + var3.getName());
                  var5[var20] = null;
                  continue;
               }
            }

            JavaTypeName var25 = JavaTypeName.forClassName(var21.getName());
            if (var24 != null) {
               var5[var20].setFieldName(var24.getName());
            } else {
               var5[var20].setGetterName(MethodName.create(var22.getName()));
               if (var23 != null) {
                  var5[var20].setSetterName(MethodName.create(var23.getName(), var25));
               }
            }

            SchemaProperty var26 = null;
            Object var15 = null;
            String var16 = var2[var20].getXmlElementName();
            if (var16 != null) {
               ArrayList var17 = this.mFQH.getElementProperties(var4, var16);
               if (var17.size() > 1) {
                  Iterator var18 = var17.iterator();

                  while(var18.hasNext() && var26 == null) {
                     var26 = (SchemaProperty)var18.next();
                     var15 = this.getBindingTypeBetween(var25, var26.getType());
                     if (var15 == null) {
                        var26 = null;
                     }
                  }
               } else {
                  var26 = this.mFQH.getElementProperty(var4, var16);
               }

               if (var26 == null) {
                  this.logMissingPropertyError(var4, var16, false);
                  var5[var20] = null;
                  continue;
               }

               var5[var20].setQName(var26.getName());
               var5[var20].setAttribute(false);
            } else {
               var16 = var2[var20].getXmlAttributeName();
               if (var16 == null) {
                  if (var2[var20].getXmlWildcard() != null) {
                     this.fillWildProperty(var5[var20], var4, var25);
                  } else {
                     this.logError("invalid <variable-name> for " + var2[var20].getJavaVariableName() + " - neither element not attribute specified");
                     var5[var20] = null;
                  }
                  continue;
               }

               var26 = this.mFQH.getAttributeProperty(var4, var16);
               if (var26 == null) {
                  this.logMissingPropertyError(var4, var16, true);
                  var5[var20] = null;
                  continue;
               }

               var5[var20].setQName(var26.getName());
               var5[var20].setAttribute(true);
            }

            var5[var20].setNillable(var26.hasNillable() != 0);
            if (var15 == null) {
               var15 = this.getBindingTypeBetween(var25, var26.getType());
            }

            if (var15 != null) {
               var5[var20].setTypeName(((BindingType)var15).getName());
            } else {
               XmlTypeName var27 = XmlTypeName.forSchemaType(var26.getType());
               if (!var21.isArray()) {
                  throw new IllegalStateException("Could not find binding for QNameProperty " + var2[var20].getJavaVariableName() + " : " + BindingTypeName.forPair(var25, var27) + " Bean type :" + var3 + " XmlType " + var4.getName());
               }

               if (SoapArrayHelper.isSoapArray(var26.getType())) {
                  var15 = (new SoapArrayHelper(this.mSchemaTypeSystem, this.mLoader, this.mBindingFile)).createSoapArrayType(var26.getType());
                  this.mBindingFile.addBindingType((BindingType)var15, false, true);
               } else if (LiteralArrayHelper.isLiteralArray(var26.getType())) {
                  var15 = (new LiteralArrayHelper(this.mSchemaTypeSystem, this.mLoader)).createLiteralArrayType(var26.getType());
                  this.mBindingFile.addBindingType((BindingType)var15, false, true);
               }

               if (var26.getMaxOccurs() != null && var26.getMaxOccurs().intValue() <= 1) {
                  if (var15 == null) {
                     throw new IllegalStateException("Could not find binding for QNameProperty " + var2[var20].getJavaVariableName() + " Java type is array. XmlType is not array " + " Bean type :" + var3 + " XmlType " + var4.getName());
                  }
               } else {
                  var5[var20].setMultiple(true);
                  var5[var20].setCollectionClass(var25);
                  JavaTypeName var28 = JavaTypeName.forClassName(var21.getComponentType().getName());
                  BindingTypeName var29 = BindingTypeName.forPair(var28, var27);
                  var15 = this.mLoader.getBindingType(var29);
                  if (var15 == null) {
                     throw new IllegalStateException("Could not find binding for QNameProperty " + var2[var20].getJavaVariableName() + " : " + var29 + " Bean type :" + var3 + " XmlType " + var4.getName());
                  }
               }

               var5[var20].setTypeName(((BindingType)var15).getName());
            }
         }

         return var5;
      }
   }

   private void fillWildProperty(QNameProperty var1, SchemaType var2, JavaTypeName var3) {
      var1.setAttribute(false);
      var1.setQName(new QName("any"));
      var1.setTypeName(AnyRuntimeBindingType.ANY_TYPE_NAME);
      var1.setMultiple(this.countWildcards(var2.getContentModel()) > 1);
      var1.setOptional(var1.isMultiple());
      if (var1.isMultiple()) {
         var1.setCollectionClass(var3);
      }

   }

   private int countWildcards(SchemaParticle var1) {
      if (var1 == null) {
         return 0;
      } else {
         int var2 = 0;
         SchemaParticle[] var3;
         int var4;
         switch (var1.getParticleType()) {
            case 1:
            case 3:
               var3 = var1.getParticleChildren();

               for(var4 = 0; var4 < var3.length; ++var4) {
                  var2 += this.countWildcards(var3[var4]);
               }

               return var2;
            case 2:
               var3 = var1.getParticleChildren();

               for(var4 = 0; var4 < var3.length; ++var4) {
                  int var5 = this.countWildcards(var3[var4]);
                  if (var5 > var2) {
                     var2 = var5;
                  }
               }
            case 4:
            default:
               break;
            case 5:
               var2 = var1.getIntMaxOccurs();
         }

         return var2;
      }
   }

   private BindingType getBindingTypeBetween(JavaTypeName var1, SchemaType var2) {
      XmlTypeName var3 = XmlTypeName.forSchemaType(var2);
      BindingTypeName var4 = BindingTypeName.forPair(var1, var3);
      BindingType var5 = this.mLoader.getBindingType(var4);
      if (var5 != null) {
         return var5;
      } else {
         SchemaType var6 = this.getBaseTypeForComplexTypeWithSimpleContent(var2);
         if (var6 == null) {
            var6 = getBuiltinBaseType(var2);
         }

         if (var6 == null) {
            return null;
         } else {
            var3 = XmlTypeName.forSchemaType(var6);
            var4 = BindingTypeName.forPair(var1, var3);
            var5 = this.mLoader.getBindingType(var4);
            return var5;
         }
      }
   }

   private void logWarning(String var1) {
      System.out.println(var1);
   }

   private void logError(String var1) {
      if (!suppressDeployErrorMessage) {
         System.out.println("<WS data binding error>" + var1);
      }

   }

   private void logError(String var1, Throwable var2) {
      IllegalStateException var3 = new IllegalStateException(var1);
      var3.initCause(var2);
      var3.printStackTrace();
   }

   private void logMissingPropertyError(SchemaType var1, String var2, boolean var3) {
      StringWriter var4 = new StringWriter();
      String var5 = var3 ? "attribute" : "element";
      var4.write("Could not find " + var5 + " property '" + var2 + "' on " + var1.getName() + ".\n  Available " + var5 + "s are:\n");
      SchemaProperty[] var6 = var3 ? var1.getAttributeProperties() : var1.getElementProperties();

      for(int var7 = 0; var7 < var6.length; ++var7) {
         var4.write(var6[var7].getName().toString());
         var4.write(10);
      }

      this.logError(var4.toString());
   }

   private void debugOutputs() {
   }

   private void debugInputs() {
      if (mVerbose) {
         StringBuffer var1 = new StringBuffer();
         var1.append("Processing following schemas:");

         for(int var2 = 0; var2 < this.mSchemas.length; ++var2) {
            var1 = var1.append("\n");
            var1.append(this.mSchemas[var2].getSchema().getTargetNamespace());
         }

         Verbose.log((Object)var1.toString());
      }

   }

   private void writeTylar(TylarWriter var1) throws IOException {
      var1.writeBindingFile(this.mBindingFile);
      var1.writeSchemaTypeSystem(this.mSchemaTypeSystem);
      if (this.mSchemas != null) {
         for(int var2 = 0; var2 < this.mSchemas.length; ++var2) {
            var1.writeSchema(this.mSchemas[var2], "schema-" + var2 + ".xsd", (Map)null);
         }
      }

   }

   private static Field getJavaVariablePublicField(Class var0, VariableMappingBean var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("null class");
      } else if (var1 == null) {
         throw new IllegalArgumentException("null varmap");
      } else {
         if (var1.getDataMember() != null) {
            try {
               Field var2 = var0.getField(var1.getJavaVariableName());
               if (Modifier.isPublic(var2.getModifiers())) {
                  return var2;
               }
            } catch (NoSuchFieldException var3) {
               return null;
            }
         }

         return null;
      }
   }

   private static final String ucfirst(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("null string");
      } else {
         return StringUtils.ucfirst(var0);
      }
   }

   private JavaTypeName getJavaTypeForSimpleType(SchemaType var1) {
      if (!var1.isSimpleType()) {
         this.logError("Internal error: " + var1 + " expected to be a simple type");
      }

      SchemaType var2 = getBuiltinBaseType(var1);
      if (var2 == null) {
         this.logError("Internal error: no builtin base st found for " + var1);
         return null;
      } else {
         BindingTypeName var3 = this.mLoader.lookupPojoFor(XmlTypeName.forSchemaType(var2));
         if (var3 == null) {
            this.logError("Internal error: no binding found for builtin base st " + var2);
            return null;
         } else {
            return var3.getJavaName();
         }
      }
   }

   private static SchemaType getBuiltinBaseType(SchemaType var0) {
      while(!var0.isBuiltinType()) {
         var0 = var0.getBaseType();
      }

      return var0;
   }

   private static boolean isException(Class var0) {
      return Throwable.class.isAssignableFrom(var0);
   }

   private static String fixFileUri(String var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = var0.replaceAll(" ", "%20");
         return var1;
      }
   }
}
