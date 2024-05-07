package weblogic.wsee.util;

import com.bea.util.jam.JClass;
import com.bea.xbean.schema.SchemaTypeImpl;
import com.bea.xml.SchemaType;
import com.bea.xml.XmlObject;
import java.lang.reflect.Field;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.dom.Util;

public class XBeanUtil {
   private static final boolean verbose = Verbose.isVerbose(XBeanUtil.class);
   private static final String XMLOBJECT_SCHEMATYPE_FIELD = "type";
   private static final String APACHE_XSDPATH_PREFIX = "schemaorg_apache_xmlbeans";
   private static final String BEA_XSDPATH_PREFIX = "schemacom_bea_xml";
   public static final String SOAPFAULTS_CONTAIN_XMLBEANS = "com.bea.SOAPFAULTS_CONTAIN_XMLBEANS";

   private XBeanUtil() {
   }

   public static Element createXMLFragmentFromElement(Element var0) {
      if (var0 == null) {
         return null;
      } else {
         Element var1 = var0.getOwnerDocument().createElement("xml-fragment");
         if (var0.hasChildNodes()) {
            Element var2 = (Element)var0.cloneNode(true);
            Node var3 = var2.getFirstChild();
            if (var3 != null) {
               Node var4 = var3.getNextSibling();
               var1.appendChild(var3);

               while(var4 != null) {
                  var3 = var4;
                  var4 = var4.getNextSibling();
                  var1.appendChild(var3);
               }
            }
         }

         if (verbose) {
            Verbose.log((Object)("created xmlFragment \n" + Util.printNode(var1)));
         }

         return var1;
      }
   }

   public static boolean isXmlBean(JClass var0) {
      JClass var1 = var0.forName("org.apache.xmlbeans.XmlObject");
      if (!var1.isUnresolvedType() && var1.isAssignableFrom(var0)) {
         return true;
      } else {
         var1 = var0.getClassLoader().loadClass(XmlObject.class.getName());
         if (var1.isAssignableFrom(var0)) {
            Verbose.log((Object)("WARNING: Detected use of an XMLBean derived from com.bea.xml.XmlObject: '" + var0.getQualifiedName() + "'.  " + "Use of com.bea.xml XMLBeans is deprecated.  " + "Apache XMLBeans should be used instead."));
            return true;
         } else {
            return false;
         }
      }
   }

   public static boolean isXmlBean(Class var0) {
      try {
         Class var1 = Class.forName("org.apache.xmlbeans.XmlObject");
         if (var1.isAssignableFrom(var0)) {
            return true;
         }
      } catch (ClassNotFoundException var2) {
      }

      return XmlObject.class.isAssignableFrom(var0);
   }

   public static QName getQNameFromXmlBean(ClassLoader var0, String var1) {
      QName var2 = null;
      Class var3 = loadXmlBean(var0, var1);
      if (var3 != null) {
         if (XmlObject.class.isAssignableFrom(var3)) {
            SchemaType var4 = getBeaSchemaTypeForBeaXmlBean(var3);
            if (var4 == null) {
               return null;
            }

            if (var4.isDocumentType()) {
               var2 = var4.getDocumentElementName();
            } else {
               var2 = var4.getName();
            }
         } else if (org.apache.xmlbeans.XmlObject.class.isAssignableFrom(var3)) {
            org.apache.xmlbeans.SchemaType var5 = getApacheSchemaTypeForApacheXmlBean(var3);
            if (var5 == null) {
               return null;
            }

            if (var5.isDocumentType()) {
               var2 = var5.getDocumentElementName();
            } else {
               var2 = var5.getName();
            }
         }
      }

      return var2;
   }

   public static boolean xmlBeanIsDocumentType(ClassLoader var0, String var1) {
      return xmlBeanIsDocumentType(var0, var1, false);
   }

   public static boolean xmlBeanIsDocumentType(ClassLoader var0, String var1, boolean var2) throws RuntimeException {
      Class var3 = loadXmlBean(var0, var1, true);
      return var3 == null ? false : xmlBeanIsDocumentType(var3, var2);
   }

   public static boolean xmlBeanIsDocumentType(Class var0) {
      return xmlBeanIsDocumentType(var0, false);
   }

   public static SchemaType transformST(SchemaType var0) {
      if (var0 == null) {
         return null;
      } else {
         if (var0.toString().startsWith("E=") && var0 instanceof SchemaTypeImpl) {
            ((SchemaTypeImpl)var0).setDocumentType(true);
         }

         return var0;
      }
   }

   public static org.apache.xmlbeans.SchemaType transformST(org.apache.xmlbeans.SchemaType var0) {
      if (var0 == null) {
         return null;
      } else {
         if (var0.toString().startsWith("E=") && var0 instanceof org.apache.xmlbeans.impl.schema.SchemaTypeImpl) {
            ((org.apache.xmlbeans.impl.schema.SchemaTypeImpl)var0).setDocumentType(true);
         }

         return var0;
      }
   }

   public static boolean xmlBeanIsDocumentType(Class var0, boolean var1) throws RuntimeException {
      if (var0 != null) {
         if (XmlObject.class.isAssignableFrom(var0)) {
            SchemaType var3 = getBeaSchemaTypeForBeaXmlBean(var0, var1);
            if (var3 == null) {
               return false;
            }

            return var3.isDocumentType();
         }

         if (org.apache.xmlbeans.XmlObject.class.isAssignableFrom(var0)) {
            org.apache.xmlbeans.SchemaType var2 = getApacheSchemaTypeForApacheXmlBean(var0, var1);
            if (var2 == null) {
               return false;
            }

            return var2.isDocumentType();
         }
      }

      return false;
   }

   public static boolean isArrayOfXmlBean(JClass var0) {
      if (var0 == null) {
         return false;
      } else if (!var0.isArrayType()) {
         return false;
      } else {
         JClass var1 = var0.getArrayComponentType();
         return var1 == null ? false : isXmlBean(var1);
      }
   }

   static Class loadXmlBean(ClassLoader var0, String var1) {
      return loadXmlBean(var0, var1, false);
   }

   static Class loadXmlBean(ClassLoader var0, String var1, boolean var2) throws RuntimeException {
      Class var3 = null;

      try {
         var3 = var0.loadClass(var1);
      } catch (ClassNotFoundException var5) {
         if (var2) {
            throw new RuntimeException(var5.toString(), var5);
         }

         if (verbose) {
            Verbose.log("Unable to load " + var1 + " as an XmlBean", var5);
         }
      }

      return var3;
   }

   public static boolean isXmlBeanSchemaCompiled(ClassLoader var0, String var1) {
      Class var2 = loadXmlBean(var0, var1);
      if (var2 == null) {
         return false;
      } else {
         Object var3 = null;
         if (XmlObject.class.isAssignableFrom(var2)) {
            var3 = getBeaSchemaTypeForBeaXmlBean(var2);
         } else if (org.apache.xmlbeans.XmlObject.class.isAssignableFrom(var2)) {
            var3 = getApacheSchemaTypeForApacheXmlBean(var2);
         }

         return var3 != null;
      }
   }

   public static String getSchemaTypeSourceName(ClassLoader var0, String var1) {
      Class var2 = loadXmlBean(var0, var1);
      return var2 == null ? null : getXsdPathForXmlBean(var0, var2, false);
   }

   public static boolean isBuiltInTypeXmlBean(ClassLoader var0, String var1) {
      Class var2 = loadXmlBean(var0, var1);
      if (var2 == null) {
         Verbose.log((Object)("WARNING !  could NOT load xmlBean '" + var1 + "'"));
         return true;
      } else {
         return isBuiltInTypeXmlBean(var2);
      }
   }

   public static boolean isBuiltInTypeXmlBean(Class var0) {
      if (var0 == null) {
         Verbose.log((Object)"WARNING !  cannot check is Class is BuiltIn Type XmlBean.  Input class is NULL !");
         return true;
      } else {
         boolean var1 = false;
         if (XmlObject.class.isAssignableFrom(var0)) {
            SchemaType var2 = getBeaSchemaTypeForBeaXmlBean(var0);
            var1 = var2.isBuiltinType();
         } else if (org.apache.xmlbeans.XmlObject.class.isAssignableFrom(var0)) {
            org.apache.xmlbeans.SchemaType var3 = getApacheSchemaTypeForApacheXmlBean(var0);
            var1 = var3.isBuiltinType();
         }

         return var1;
      }
   }

   static String getXsdPathForXmlBean(ClassLoader var0, Class var1, boolean var2) {
      String var3 = null;
      if (verbose) {
         Verbose.log((Object)(" +++ getXsdPathForXmlBean('" + var0 + "', '" + var1 + "', '" + var2 + "')..."));
      }

      if (var1 == null) {
         if (verbose) {
            Verbose.log((Object)" Returning null...");
         }

         return var3;
      } else {
         if (XmlObject.class.isAssignableFrom(var1)) {
            if (verbose) {
               Verbose.log((Object)" is bea-xml-bean...");
            }

            SchemaType var4 = getBeaSchemaTypeForBeaXmlBean(var1);
            var3 = var4.getSourceName();
            if (var2 && var3 != null) {
               var3 = "schemacom_bea_xml/src/" + var3;
            }
         } else if (org.apache.xmlbeans.XmlObject.class.isAssignableFrom(var1)) {
            if (verbose) {
               Verbose.log((Object)" is apache-xml-bean...");
            }

            org.apache.xmlbeans.SchemaType var5 = getApacheSchemaTypeForApacheXmlBean(var1);
            if (verbose) {
               Verbose.log((Object)(" +++ SchemaType : " + var5));
            }

            var3 = var5.getSourceName();
            if (verbose) {
               Verbose.log((Object)(" +++ xsdPath from SchemaType : " + var3));
            }

            if (var2 && var3 != null) {
               var3 = "schemaorg_apache_xmlbeans/src/" + var3;
            }
         }

         if (verbose) {
            Verbose.log((Object)(" +++ for xmlBeanClass '" + var1.getName() + "', xsdPath is '" + var3 + "'"));
         }

         return var3;
      }
   }

   static org.apache.xmlbeans.SchemaType getApacheSchemaTypeForApacheXmlBean(Class var0) {
      return getApacheSchemaTypeForApacheXmlBean(var0, false);
   }

   static org.apache.xmlbeans.SchemaType getApacheSchemaTypeForApacheXmlBean(Class var0, boolean var1) throws RuntimeException {
      org.apache.xmlbeans.SchemaType var2 = null;
      if (var0 == null) {
         Verbose.log((Object)"  WARNING !  getApacheSchemaTypeForApacheXmlBean called with NULL xmlBeanClass ! ");
         return var2;
      } else {
         assert org.apache.xmlbeans.XmlObject.class.isAssignableFrom(var0);

         if (org.apache.xmlbeans.XmlObject.class.isAssignableFrom(var0)) {
            Field var3;
            try {
               var3 = var0.getField("type");
            } catch (NoSuchFieldException var5) {
               if (var1) {
                  throw new RuntimeException(var5.toString(), var5);
               }

               var5.printStackTrace();
               return var2;
            }

            if (var3 != null) {
               try {
                  var2 = (org.apache.xmlbeans.SchemaType)var3.get((Object)null);
               } catch (ExceptionInInitializerError var6) {
                  if (var1) {
                     throw new RuntimeException(var6.toString(), var6);
                  }

                  return var2;
               } catch (IllegalAccessException var7) {
                  if (var1) {
                     throw new RuntimeException(var7.toString(), var7);
                  }

                  return var2;
               }
            }
         }

         return var2;
      }
   }

   static SchemaType getBeaSchemaTypeForBeaXmlBean(Class var0) {
      return getBeaSchemaTypeForBeaXmlBean(var0, false);
   }

   static SchemaType getBeaSchemaTypeForBeaXmlBean(Class var0, boolean var1) {
      SchemaType var2 = null;
      if (var0 == null) {
         Verbose.log((Object)"  WARNING !  getBeaSchemaTypeForBeaXmlBean called with NULL xmlBeanClass ! ");
         return var2;
      } else {
         assert XmlObject.class.isAssignableFrom(var0);

         if (XmlObject.class.isAssignableFrom(var0)) {
            Field var3;
            try {
               var3 = var0.getField("type");
            } catch (NoSuchFieldException var5) {
               if (var1) {
                  throw new RuntimeException(var5.toString(), var5);
               }

               var5.printStackTrace();
               return var2;
            }

            if (var3 != null) {
               try {
                  var2 = (SchemaType)var3.get((Object)null);
               } catch (ExceptionInInitializerError var6) {
                  if (var1) {
                     throw new RuntimeException(var6.toString(), var6);
                  }

                  return var2;
               } catch (IllegalAccessException var7) {
                  if (var1) {
                     throw new RuntimeException(var7.toString(), var7);
                  }

                  var7.printStackTrace();
                  return var2;
               }
            }
         }

         return var2;
      }
   }
}
