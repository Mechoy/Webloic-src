package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.FaultMessage;
import com.bea.staxb.buildtime.WildcardUtil;
import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.util.jam.JClass;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;

public abstract class XmlBeansBaseBuildtimeBindings implements BuildtimeBindings {
   protected BuildtimeBindings _emptyBindings = null;
   protected Map _S2JWrapperElements = null;
   protected Tylar _tylar;
   protected SchemaTypeLoader _apache_loader;
   protected com.bea.xml.SchemaTypeLoader _typeLoader;
   protected SchemaDocument[] _documents;

   void setWrapperElements(Map var1) {
      this._S2JWrapperElements = var1;
   }

   void setTylar(Tylar var1) {
      this._tylar = var1;
   }

   public SchemaDocument[] getSchemas() {
      if (this._documents != null) {
         return this._documents;
      } else {
         throw new UnsupportedOperationException("XmlBeansBuildtimeBindings.getSchemas(). No schema docs - getSchemas() unsupported at runtime");
      }
   }

   public boolean useCheckedExceptionFromWsdlFault() {
      return false;
   }

   public String getExceptionClassFromFaultMessageType(FaultMessage var1) {
      return null;
   }

   public String getExceptionClassFromFaultMessageElement(FaultMessage var1) {
      return null;
   }

   public String getWrappedSimpleClassNameFromFaultMessageType(FaultMessage var1) {
      return null;
   }

   public String getWrappedSimpleClassNameFromFaultMessageElement(FaultMessage var1) {
      return null;
   }

   public List getElementNamesCtorOrderFromFaultMessageElement(FaultMessage var1) {
      return null;
   }

   public List getElementNamesCtorOrderFromFaultMessageType(FaultMessage var1) {
      return null;
   }

   public List getElementNamesCtorOrderFromException(JClass var1) {
      return null;
   }

   public abstract String getClassFromSchemaType(QName var1);

   public abstract String getClassFromSchemaElement(QName var1);

   public abstract QName getSchemaType(String var1);

   public abstract Class getBuilderClass();

   public void generate109Descriptor(JavaWsdlMappingBean var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null out");
      } else {
         if (this._tylar != null) {
            Buildtime109MappingHelper var2 = new Buildtime109MappingHelper(this._tylar);
            var2.buildMappings(var1);
         }

      }
   }

   public abstract LinkedHashMap getJavaTypesForWrapperElement(QName var1);

   protected Map.Entry getCurrentParameter(LinkedHashMap var1, int var2) {
      int var3 = 0;
      Map.Entry var4 = null;

      for(Iterator var5 = var1.entrySet().iterator(); var5.hasNext() && var3 <= var2; ++var3) {
         var4 = (Map.Entry)var5.next();
      }

      return var4;
   }

   protected boolean requiresWildcardClass(String var1) {
      return WildcardUtil.WILDCARD_CLASSNAMES.contains(var1) || var1.equals(Object.class.getName());
   }

   protected String getClassName(QName var1) {
      String var2 = null;
      if (var1 != null && var1.equals(SoapAwareJava2Schema.XS_ANYTYPE)) {
         var2 = this.getWildcardClassName();
      } else {
         try {
            var2 = this._emptyBindings.getClassFromSchemaType(var1);
         } catch (Exception var4) {
         }
      }

      return var2;
   }

   abstract String getWildcardClassName();

   public static boolean hasWrapperFormat(SchemaType var0, boolean var1) {
      if (var0.isSimpleType()) {
         return false;
      } else if (var0.getAttributeProperties().length > 0) {
         return false;
      } else if (var0.getContentType() == 1) {
         return true;
      } else if (var0.getContentType() != 3 && var0.getContentType() != 4) {
         return false;
      } else {
         SchemaParticle var2 = var0.getContentModel();
         String var3 = findNameSpace(var0);
         if (var2.getParticleType() == 4) {
            return var2.getMaxOccurs() != null && var2.getMaxOccurs().compareTo(BigInteger.ONE) != 1 ? namespaceEqual(var3, var2.getName().getNamespaceURI()) : false;
         } else {
            SchemaProperty[] var4 = var0.getElementProperties();
            if (var4.length == 0 && var2.countOfParticleChild() == 0 && var2.getParticleType() == 5) {
               return true;
            } else {
               if (var4.length > 1) {
                  for(int var5 = 0; var5 < var4.length; ++var5) {
                     if (isMultiple(var4[var5])) {
                        return false;
                     }
                  }
               }

               if (var2.getParticleType() != 3) {
                  return false;
               } else {
                  SchemaParticle[] var9 = var2.getParticleChildren();
                  if (var1 && var9.length > 1) {
                     return false;
                  } else {
                     HashSet var6 = new HashSet();

                     for(int var7 = 0; var7 < var9.length; ++var7) {
                        if (var9[var7].getParticleType() != 5) {
                           if (var9[var7].getParticleType() != 4) {
                              return false;
                           }

                           QName var8 = var9[var7].getName();
                           if (!namespaceEqual(var3, var8.getNamespaceURI())) {
                              return false;
                           }

                           if (var9[var7].getMaxOccurs() == null || var9[var7].getMaxOccurs().compareTo(BigInteger.ONE) == 1) {
                              return false;
                           }

                           if (var6.contains(var8)) {
                              return false;
                           }

                           var6.add(var8);
                        }
                     }

                     return true;
                  }
               }
            }
         }
      }
   }

   private static boolean namespaceEqual(String var0, String var1) {
      return true;
   }

   private static String findNameSpace(SchemaType var0) {
      if (var0.getName() != null) {
         return var0.getName().getNamespaceURI();
      } else {
         SchemaType var1 = var0.getOuterType();
         return var1 != null && var1.getDocumentElementName() != null ? var1.getDocumentElementName().getNamespaceURI() : null;
      }
   }

   private static boolean isMultiple(SchemaProperty var0) {
      return var0.getMaxOccurs() == null || var0.getMaxOccurs().compareTo(BigInteger.ONE) > 0;
   }
}
