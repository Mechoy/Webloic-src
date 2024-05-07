package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.FaultMessage;
import com.bea.staxb.buildtime.WrappedOperationInfo;
import com.bea.xbean.schema.StscState;
import com.bea.xbean.xb.xmlconfig.ConfigDocument;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.SchemaType;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlError;
import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
import com.bea.xml.XmlOptions;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.util.Verbose;

public abstract class BindingsBuilderBase {
   private static final boolean VERBOSE = Verbose.isVerbose(BindingsBuilderBase.class);
   protected List<ClassLoader> mClassLoaders = null;
   protected List<QName> paramTypes = new ArrayList();
   protected List<QName> paramElements = new ArrayList();
   protected List<FaultMessage> faultTypes = new ArrayList();
   protected List<FaultMessage> faultElements = new ArrayList();
   protected List<WrappedOperationInfo> wrapperInfos = new ArrayList();
   protected boolean includeGlobalTypes = false;
   protected boolean sortSchemaTypes = false;
   protected ClassLoader mXmlObjectClassLoader = null;
   protected List<SchemaDocument> schemaDocs = new ArrayList();
   protected File codegenDir = null;
   private static final String CONFIG_URI = "http://xml.apache.org/xmlbeans/2004/02/xbean/config";
   private static final String COMPATIBILITY_CONFIG_URI = "http://www.bea.com/2002/09/xbean/config";
   private static final Map MAP_COMPATIBILITY_CONFIG_URIS = new HashMap();

   public void addBaseTypeLibrary(ClassLoader var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("BindingsBuilderBase.addBaseTypeLibrary() received null classloader");
      } else {
         if (this.mClassLoaders == null) {
            this.mClassLoaders = new ArrayList();
         }

         this.mClassLoaders.add(var1);
      }
   }

   public void addParamType(QName var1) {
      this.paramTypes.add(var1);
   }

   public void addParamElement(QName var1) {
      this.paramElements.add(var1);
   }

   public void addFaultType(FaultMessage var1) {
      this.faultTypes.add(var1);
   }

   public void addFaultElement(FaultMessage var1) {
      this.faultElements.add(var1);
   }

   public void addWrapperOperation(WrappedOperationInfo var1) {
      this.wrapperInfos.add(var1);
   }

   public void includeGlobalTypes(boolean var1) {
      this.includeGlobalTypes = var1;
   }

   public void sortSchemaTypes(boolean var1) {
      this.sortSchemaTypes = var1;
   }

   protected ConfigDocument.Config[] getBindingConfig(File[] var1) throws XmlException {
      if (var1 != null && var1.length != 0) {
         SchemaTypeLoader var2 = XmlBeans.typeLoaderForClassLoader(SchemaDocument.class.getClassLoader());
         ArrayList var3 = new ArrayList();
         File[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File var7 = var4[var6];
            ArrayList var8 = new ArrayList();

            try {
               XmlOptions var9 = new XmlOptions();
               var9.put("LOAD_LINE_NUMBERS");
               var9.setErrorListener(var8);
               var9.setLoadSubstituteNamespaces(MAP_COMPATIBILITY_CONFIG_URIS);
               XmlObject var10 = var2.parse(var7, (SchemaType)null, var9);
               if (!(var10 instanceof ConfigDocument)) {
                  StscState.addError(var8, "invalid.document.type", new Object[]{var7, "xsd config"}, var10);
               } else {
                  StscState.addInfo(var8, "Loading config file " + var7);
                  if (var10.validate((new XmlOptions()).setErrorListener(var8))) {
                     var3.add(((ConfigDocument)var10).getConfig());
                  }
               }
            } catch (Exception var11) {
               throw new XmlException(var11);
            }

            Iterator var14 = var8.iterator();

            while(var14.hasNext()) {
               XmlError var12 = (XmlError)var14.next();
               if (var12.getSeverity() == 0) {
                  throw new XmlException(var12);
               }
            }
         }

         ConfigDocument.Config[] var13 = (ConfigDocument.Config[])((ConfigDocument.Config[])var3.toArray(new ConfigDocument.Config[var3.size()]));
         return var13;
      } else {
         return null;
      }
   }

   protected org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config[] getApacheBindingConfig(File[] var1) throws org.apache.xmlbeans.XmlException {
      if (var1 != null && var1.length != 0) {
         org.apache.xmlbeans.SchemaTypeLoader var2 = org.apache.xmlbeans.XmlBeans.typeLoaderForClassLoader(SchemaDocument.class.getClassLoader());
         ArrayList var3 = new ArrayList();
         File[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File var7 = var4[var6];
            ArrayList var8 = new ArrayList();

            try {
               org.apache.xmlbeans.XmlOptions var9 = new org.apache.xmlbeans.XmlOptions();
               var9.put("LOAD_LINE_NUMBERS");
               var9.setErrorListener(var8);
               var9.setLoadSubstituteNamespaces(MAP_COMPATIBILITY_CONFIG_URIS);
               org.apache.xmlbeans.XmlObject var10 = var2.parse(var7, (org.apache.xmlbeans.SchemaType)null, var9);
               if (!(var10 instanceof org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument)) {
                  org.apache.xmlbeans.impl.schema.StscState.addError(var8, "invalid.document.type", new Object[]{var7, "xsd config"}, var10);
               } else {
                  org.apache.xmlbeans.impl.schema.StscState.addInfo(var8, "Loading config file " + var7);
                  if (var10.validate((new org.apache.xmlbeans.XmlOptions()).setErrorListener(var8))) {
                     var3.add(((org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument)var10).getConfig());
                  }
               }
            } catch (Exception var11) {
               throw new org.apache.xmlbeans.XmlException(var11);
            }

            Iterator var14 = var8.iterator();

            while(var14.hasNext()) {
               org.apache.xmlbeans.XmlError var12 = (org.apache.xmlbeans.XmlError)var14.next();
               if (var12.getSeverity() == 0) {
                  throw new org.apache.xmlbeans.XmlException(var12);
               }
            }
         }

         org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config[] var13 = (org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config[])((org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config[])var3.toArray(new org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument.Config[var3.size()]));
         return var13;
      } else {
         return null;
      }
   }

   public void setXmlObjectClassLoader(ClassLoader var1) {
      this.mXmlObjectClassLoader = var1;
   }

   public ClassLoader getXmlObjectClassLoader() {
      return this.mXmlObjectClassLoader;
   }

   public void setCodegenDir(File var1) {
      this.codegenDir = var1;
   }

   public void addSchemaDocument(SchemaDocument var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("XmlBeansBindingsBuilderImpl.addSchemaDocument() received null xsd");
      } else {
         this.schemaDocs.add(var1);
         if (VERBOSE) {
            Verbose.log((Object)("added schema " + var1.getSchema().getTargetNamespace()));
         }

      }
   }

   static {
      MAP_COMPATIBILITY_CONFIG_URIS.put("http://www.bea.com/2002/09/xbean/config", "http://xml.apache.org/xmlbeans/2004/02/xbean/config");
   }
}
