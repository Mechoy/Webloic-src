package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.FaultMessage;
import com.bea.staxb.buildtime.internal.bts.BindingLoader;
import com.bea.staxb.buildtime.internal.bts.BindingType;
import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.buildtime.internal.bts.ByNameBean;
import com.bea.staxb.buildtime.internal.bts.JavaTypeName;
import com.bea.staxb.buildtime.internal.bts.QNameProperty;
import com.bea.staxb.buildtime.internal.bts.SimpleDocumentBinding;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.buildtime.internal.tylar.DebugTylarWriter;
import com.bea.staxb.buildtime.internal.tylar.DefaultTylarLoader;
import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.util.jam.JClass;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.XmlException;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;
import javax.mail.internet.MimeMultipart;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.XBeanUtil;

public class TylarBuildtimeBindings implements BuildtimeBindings {
   private static final boolean VERBOSE = Verbose.isVerbose(TylarBuildtimeBindings.class);
   private static final String XMLOBJECT_SCHEMATYPE_FIELD = "type";
   public static final String ATTACHMENT_NAMESPACE = "http://www.bea.com/servers/wls90/wsee/attachment";
   public static final QName JAVAX_AWT_IMAGE = new QName("http://www.bea.com/servers/wls90/wsee/attachment", "image");
   public static final QName JAVAX_MAIL_INTERNET_MIMEMULTIPART = new QName("http://www.bea.com/servers/wls90/wsee/attachment", "mimemultipart");
   public static final QName JAVAX_XML_TRANSFORM_SOURCE = new QName("http://www.bea.com/servers/wls90/wsee/attachment", "textXml");
   public static final QName JAVAX_ACTIVATION_DATAHANDLER = new QName("http://www.bea.com/servers/wls90/wsee/attachment", "datahandler");
   private static final Map specialJavaTypeMaps = createSpecialJavaTypes();
   private static final Map specialSchemaTypeMaps = createSpecialSchemaTypes();
   private Tylar mTylar;
   private Class mBuilderClass = null;
   private ClassLoader mXmlObjectClassLoader = null;
   private Map mWrapperElements = null;

   void setWrapperElements(Map var1) {
      this.mWrapperElements = var1;
   }

   static boolean isSpecialJavaType(String var0) {
      return specialJavaTypeMaps.containsKey(var0);
   }

   static QName getSpecialJavaType(String var0) {
      return (QName)specialJavaTypeMaps.get(var0);
   }

   TylarBuildtimeBindings(Tylar var1, File var2, Class var3, ClassLoader var4) {
      if (var1 == null) {
         throw new IllegalArgumentException("null tylar");
      } else {
         this.mTylar = var1;
         this.mBuilderClass = var3;
         this.mXmlObjectClassLoader = var4;
      }
   }

   public boolean useCheckedExceptionFromWsdlFault() {
      return true;
   }

   public void generate109Descriptor(JavaWsdlMappingBean var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null out");
      } else {
         if (VERBOSE) {
            Verbose.log((Object)("generate109Descriptor for " + var1 + ".  Tylar contents:"));
            this.debug(new PrintWriter(System.out));
         }

         Buildtime109MappingHelper var2 = new Buildtime109MappingHelper(this.mTylar);
         var2.buildMappings(var1);
      }
   }

   public String getClassFromSchemaType(QName var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null name");
      } else {
         String var2 = (String)specialSchemaTypeMaps.get(var1);
         return var2 != null ? var2 : this.getClassFromXmlType(XmlTypeName.forTypeNamed(var1));
      }
   }

   public String getClassFromSchemaElement(QName var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null name");
      } else {
         XmlTypeName var2 = XmlTypeName.forGlobalName('e', var1);
         String var3 = this.getClassFromXmlType(var2);
         BindingTypeName var4 = this.getTypeBindingTypeName(var3);
         if (var4 != null) {
            String var5 = (String)specialSchemaTypeMaps.get(var4.getXmlName().getQName());
            if (var5 != null) {
               return var5;
            }
         }

         return var3;
      }
   }

   public String getExceptionClassFromFaultMessageType(FaultMessage var1) {
      QName var2 = var1.getMessageName();
      QName var3 = var1.getComponentName();
      String var4 = var1.getPartName();
      if (var2 == null) {
         throw new IllegalArgumentException("null messageName");
      } else if (var4 == null) {
         throw new IllegalArgumentException("null partName");
      } else if (var3 == null) {
         throw new IllegalArgumentException("null type");
      } else {
         XmlTypeName var5 = XmlTypeName.forTypeNamed(var3);
         return this.getExceptionClassFromXmlTypeName(var2, var4, var5);
      }
   }

   public String getExceptionClassFromFaultMessageElement(FaultMessage var1) {
      QName var2 = var1.getMessageName();
      QName var3 = var1.getComponentName();
      String var4 = var1.getPartName();
      if (var2 == null) {
         throw new IllegalArgumentException("null messageName");
      } else if (var4 == null) {
         throw new IllegalArgumentException("null partName");
      } else if (var3 == null) {
         throw new IllegalArgumentException("null type");
      } else {
         XmlTypeName var5 = XmlTypeName.forGlobalName('e', var3);
         return this.getExceptionClassFromXmlTypeName(var2, var4, var5);
      }
   }

   private String getExceptionClassFromXmlTypeName(QName var1, String var2, XmlTypeName var3) {
      XmlTypeName var4 = XmlTypeName.forFaultType(var1, var2, var3);
      BindingLoader var5 = this.mTylar.getBindingLoader();
      BindingTypeName var6;
      String var7;
      if ((var6 = var5.lookupPojoFor(var4)) != null) {
         var7 = var6.getJavaName().toString();
      } else {
         var7 = this.getClassFromXmlType(var3);
      }

      if (VERBOSE) {
         Verbose.log((Object)(" for typeName " + var3 + ", exceptionClass name for faultTypeName '" + var4 + " is '" + var7 + "'"));
      }

      return var7;
   }

   public String getWrappedSimpleClassNameFromFaultMessageType(FaultMessage var1) {
      QName var2 = var1.getMessageName();
      QName var3 = var1.getComponentName();
      String var4 = var1.getPartName();
      if (var2 == null) {
         throw new IllegalArgumentException("null messageName");
      } else if (var4 == null) {
         throw new IllegalArgumentException("null partName");
      } else if (var3 == null) {
         throw new IllegalArgumentException("null type");
      } else {
         XmlTypeName var5 = XmlTypeName.forTypeNamed(var3);
         return this.getSingleBuiltinClassNameFromXmlTypeName(var2, var4, var5);
      }
   }

   public String getWrappedSimpleClassNameFromFaultMessageElement(FaultMessage var1) {
      QName var2 = var1.getMessageName();
      QName var3 = var1.getComponentName();
      String var4 = var1.getPartName();
      if (var2 == null) {
         throw new IllegalArgumentException("null messageName");
      } else if (var4 == null) {
         throw new IllegalArgumentException("null partName");
      } else if (var3 == null) {
         throw new IllegalArgumentException("null type");
      } else {
         XmlTypeName var5 = XmlTypeName.forGlobalName('e', var3);
         return this.getSingleBuiltinClassNameFromXmlTypeName(var2, var4, var5);
      }
   }

   private String getSingleBuiltinClassNameFromXmlTypeName(QName var1, String var2, XmlTypeName var3) {
      XmlTypeName var4 = XmlTypeName.forFaultType(var1, var2, var3);
      BindingLoader var5 = this.mTylar.getBindingLoader();
      if (var5.lookupPojoFor(var4) != null) {
         BindingTypeName var6;
         if ((var6 = var5.lookupPojoFor(var3)) != null) {
            return var6.getJavaName().toString();
         } else {
            throw new IllegalArgumentException(" could not get exception class for " + var1);
         }
      } else {
         return null;
      }
   }

   public List getElementNamesCtorOrderFromFaultMessageElement(FaultMessage var1) {
      ArrayList var2 = new ArrayList();
      QName var3 = var1.getMessageName();
      QName var4 = var1.getComponentName();
      String var5 = var1.getPartName();
      if (var3 == null) {
         throw new IllegalArgumentException("null messageName");
      } else if (var5 == null) {
         throw new IllegalArgumentException("messageName " + var3 + ", has a null partName");
      } else if (var4 == null) {
         throw new IllegalArgumentException("messageName " + var3 + ", partName " + var5 + ", has a null xmlElement");
      } else {
         XmlTypeName var6 = XmlTypeName.forGlobalName('e', var4);
         BindingLoader var7 = this.mTylar.getBindingLoader();
         BindingTypeName var8;
         if ((var8 = var7.lookupPojoFor(var6)) != null) {
            BindingType var9 = var7.getBindingType(var8);
            if (VERBOSE) {
               Verbose.log((Object)(" getElementNamesCtorOrderFromFaultMessageElement BindingType for TypeName " + var6 + ", is " + var9));
            }

            if (var9 instanceof SimpleDocumentBinding) {
               XmlTypeName var10 = ((SimpleDocumentBinding)var9).getTypeOfElement();
               JavaTypeName var11 = var8.getJavaName();
               BindingTypeName var12 = BindingTypeName.forPair(var11, var10);
               if (VERBOSE) {
                  Verbose.log((Object)("element's type from SimpleDocumentBinding is " + var10 + " now lookup BindingType for constructed BTN " + var12));
               }

               var9 = var7.getBindingType(var12);
            }

            String var13 = "Wsdl Fault '" + var1.getMessageName() + "'";
            this.getElementNamesCtorOrderFromBindingType(var9, var13, var2);
         }

         return var2;
      }
   }

   public List getElementNamesCtorOrderFromFaultMessageType(FaultMessage var1) {
      ArrayList var2 = new ArrayList();
      QName var3 = var1.getMessageName();
      QName var4 = var1.getComponentName();
      String var5 = var1.getPartName();
      if (var3 == null) {
         throw new IllegalArgumentException("null messageName");
      } else if (var5 == null) {
         throw new IllegalArgumentException("messageName " + var3 + ", has a null partName");
      } else if (var4 == null) {
         throw new IllegalArgumentException("messageName " + var3 + ", partName " + var5 + ", has a null xmlType");
      } else {
         XmlTypeName var6 = XmlTypeName.forGlobalName('t', var4);
         BindingLoader var7 = this.mTylar.getBindingLoader();
         BindingTypeName var8;
         if ((var8 = var7.lookupPojoFor(var6)) != null) {
            BindingType var9 = var7.getBindingType(var8);
            if (VERBOSE) {
               Verbose.log((Object)(" getElementNamesCtorOrderFromFaultMessageElement BindingType for TypeName " + var6 + ", is " + var9));
            }

            String var10 = "Wsdl Fault '" + var1.getMessageName() + "'";
            this.getElementNamesCtorOrderFromBindingType(var9, var10, var2);
         }

         return var2;
      }
   }

   private void getElementNamesCtorOrderFromBindingType(BindingType var1, String var2, List var3) {
      if (var1 != null) {
         if (var1 instanceof ByNameBean) {
            ByNameBean var4 = (ByNameBean)var1;
            Collection var5 = var4.getProperties();
            if (VERBOSE) {
               Verbose.log((Object)("getElementNamesCtorOrderFromBindingType: ByNameBean has " + var5.size() + " properties"));
            }

            if (var5.size() > 0) {
               int var6 = 0;
               Iterator var7 = var5.iterator();

               int var9;
               while(var7.hasNext()) {
                  QNameProperty var8 = (QNameProperty)var7.next();
                  if (VERBOSE) {
                     Verbose.log((Object)(" process next constructor property " + var6 + "  " + var8));
                  }

                  var9 = var8.getCtorParamIndex();
                  if (var9 >= 0) {
                     ++var6;
                  }
               }

               if (var6 > 0) {
                  QNameProperty[] var11 = new QNameProperty[var6];
                  var7 = var5.iterator();

                  while(var7.hasNext()) {
                     QNameProperty var12 = (QNameProperty)var7.next();
                     int var10 = var12.getCtorParamIndex();
                     if (var10 >= var6) {
                        throw new IllegalArgumentException("Error !  " + var2 + " has " + var6 + " constructor parameters, but the BindingType ByNameBean has a QNameProperty '" + var12.getQName() + "' with a CtorParamIndex value of " + var10 + ", which exceeds the maximum allowed value of " + (var6 - 1) + ".  All QNameProperties must be ordered " + "with unique sequential CtorParamIndex values starting from 0.");
                     }

                     if (var10 >= 0) {
                        var11[var10] = var12;
                     }
                  }

                  for(var9 = 0; var9 < var6; ++var9) {
                     if (var11[var9] == null) {
                        throw new IllegalArgumentException("Error !  " + var2 + " has " + var6 + " constructor parameters, but the propertyArray entry for constructor param " + var9 + " is NULL.  Something may be wrong with binding-file.xml");
                     }

                     var3.add(var11[var9].getQName());
                     if (VERBOSE) {
                        Verbose.log((Object)("getElementNamesCtorOrderFromBindingType: added paramQName '" + var11[var9].getQName() + " to list "));
                     }
                  }
               }
            }
         } else if (VERBOSE) {
            Verbose.log((Object)("getElementNamesCtorOrderFromBindingType:   BindingType " + var1 + ", is not a ByNameBean.  Skipping."));
         }

      }
   }

   public List getElementNamesCtorOrderFromException(JClass var1) {
      JavaTypeName var2 = JavaTypeName.forJClass(var1);
      BindingLoader var3 = this.mTylar.getBindingLoader();
      ArrayList var4 = new ArrayList();
      BindingTypeName var5;
      if ((var5 = var3.lookupTypeFor(var2)) != null) {
         BindingType var6 = var3.getBindingType(var5);
         if (VERBOSE) {
            Verbose.log((Object)(" getElementNamesCtorOrderFromException BindingType for JavaTypeName " + var2 + ", is " + var6));
         }

         String var7 = "Java Exception '" + var1.getQualifiedName() + "'";
         this.getElementNamesCtorOrderFromBindingType(var6, var7, var4);
      }

      return var4;
   }

   public QName getSchemaType(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null javaType");
      } else if (var1.equals(TylarJ2SBindingsBuilderImpl.SOAPELEMENT_CLASSNAME)) {
         return SoapAwareJava2Schema.XS_ANYTYPE;
      } else {
         BindingTypeName var2 = this.getTypeBindingTypeName(var1);
         QName var3 = var2 == null ? null : var2.getXmlName().getQName();
         if (var3 == null) {
            var3 = this.getSpecialJavaTypes(var1);
         }

         if (var3 == null && this.mXmlObjectClassLoader != null) {
            var3 = this.getSchemaTypeFromXmlObject(var1);
         }

         if (VERBOSE) {
            Verbose.log((Object)("javaType = " + var1 + " schemaType = " + var3));
         }

         return var3;
      }
   }

   public SchemaDocument[] getSchemas() {
      return this.mTylar.getSchemas();
   }

   public DocumentFragment[] getSchemaDocumentFragments() {
      SchemaDocument[] var1 = this.getSchemas();
      DocumentFragment[] var2 = new DocumentFragment[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Node var4 = var1[var3].getSchema().newDomNode();
         Verbose.log((Object)var4);
         Verbose.log((Object)var4.getClass());
         var2[var3] = (DocumentFragment)var1[var3].getSchema().newDomNode();
      }

      return var2;
   }

   public Class getBuilderClass() {
      return this.mBuilderClass;
   }

   public LinkedHashMap getJavaTypesForWrapperElement(QName var1) {
      if (this.mWrapperElements == null) {
         return null;
      } else {
         LinkedHashMap var2 = null;
         LinkedHashMap var3 = (LinkedHashMap)this.mWrapperElements.get(var1);
         if (var3 != null) {
            var2 = new LinkedHashMap();
            Iterator var4 = var3.entrySet().iterator();

            while(var4.hasNext()) {
               Map.Entry var5 = (Map.Entry)var4.next();
               BindingTypeName var6 = this.getTypeBindingTypeName((String)var5.getValue());
               if (var6 != null) {
                  String var7 = (String)specialSchemaTypeMaps.get(var6.getXmlName().getQName());
                  if (var7 != null) {
                     var2.put(var5.getKey(), var7);
                  } else {
                     var2.put(var5.getKey(), var5.getValue());
                  }
               } else {
                  var2.put(var5.getKey(), var5.getValue());
               }
            }
         }

         return var2;
      }
   }

   public String toDebugString() {
      StringWriter var1 = new StringWriter();
      PrintWriter var2 = new PrintWriter(var1);
      this.debug(var2);
      var2.flush();
      return var1.toString();
   }

   public void debug(PrintWriter var1) {
      try {
         (new DebugTylarWriter(var1)).write(this.mTylar);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   private static Map createSpecialJavaTypes() {
      HashMap var0 = new HashMap();
      var0.put(Image.class.getName(), JAVAX_AWT_IMAGE);
      var0.put(MimeMultipart.class.getName(), JAVAX_MAIL_INTERNET_MIMEMULTIPART);
      var0.put(Source.class.getName(), JAVAX_XML_TRANSFORM_SOURCE);
      var0.put(DataHandler.class.getName(), JAVAX_ACTIVATION_DATAHANDLER);
      return var0;
   }

   private static Map createSpecialSchemaTypes() {
      HashMap var0 = new HashMap();
      var0.put(JAVAX_AWT_IMAGE, Image.class.getName());
      var0.put(JAVAX_MAIL_INTERNET_MIMEMULTIPART, MimeMultipart.class.getName());
      var0.put(JAVAX_XML_TRANSFORM_SOURCE, Source.class.getName());
      var0.put(JAVAX_ACTIVATION_DATAHANDLER, DataHandler.class.getName());
      return var0;
   }

   public QName getSchemaTypeFromXmlObject(String var1) {
      return XBeanUtil.getQNameFromXmlBean(this.mXmlObjectClassLoader, var1);
   }

   private QName getSpecialJavaTypes(String var1) {
      return (QName)specialJavaTypeMaps.get(var1);
   }

   private String getClassFromXmlType(XmlTypeName var1) {
      BindingLoader var2 = this.mTylar.getBindingLoader();
      BindingTypeName var3 = var2.lookupPojoFor(var1);
      if (var3 != null) {
         return var3.getJavaName().toString();
      } else {
         var3 = var2.lookupXmlObjectFor(var1);
         if (var3 != null) {
            return var3.getJavaName().toString();
         } else {
            throw new IllegalArgumentException("unable to find java type for " + var1);
         }
      }
   }

   private BindingTypeName getTypeBindingTypeName(String var1) {
      JavaTypeName var2 = JavaTypeName.forString(var1);
      return this.mTylar.getBindingLoader().lookupTypeFor(var2);
   }

   public static class Factory implements BuildtimeBindings.Factory {
      private static Factory instance;

      private Factory() {
      }

      public BuildtimeBindings loadFromURI(URI var1) throws IOException, XmlException {
         Tylar var2 = DefaultTylarLoader.getInstance().load(var1);
         URL var3 = var1.toURL();
         URLClassLoader var4 = new URLClassLoader(new URL[]{var3});
         return new TylarBuildtimeBindings(var2, (File)null, TylarBuildtimeBindings.class, var4);
      }

      public static Factory getInstance() {
         if (instance == null) {
            instance = new Factory();
         }

         return instance;
      }
   }
}
