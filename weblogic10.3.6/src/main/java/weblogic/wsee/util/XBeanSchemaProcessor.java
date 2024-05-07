package weblogic.wsee.util;

import com.bea.staxb.buildtime.BindingCompiler;
import com.bea.xbean.xb.xsdschema.ImportDocument;
import com.bea.xbean.xb.xsdschema.IncludeDocument;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xbean.xb.xsdschema.TopLevelComplexType;
import com.bea.xbean.xb.xsdschema.TopLevelElement;
import com.bea.xbean.xb.xsdschema.TopLevelSimpleType;
import com.bea.xml.SchemaType;
import com.bea.xml.XmlCursor;
import com.bea.xml.XmlObject;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlFactory;
import weblogic.wsee.wsdl.WsdlSchema;
import weblogic.wsee.wsdl.WsdlSchemaImport;
import weblogic.wsee.wsdl.WsdlTypes;
import weblogic.wsee.wsdl.internal.WsdlSchemaImpl;

public class XBeanSchemaProcessor {
   private static final boolean verbose = Verbose.isVerbose(XBeanSchemaProcessor.class);
   private final BindingCompiler compiler;
   private final ClassLoader xmlObjectClassLoader;
   private final Set<String> processedXsdPaths = new HashSet();
   private final Set<SchemaDocument> globalCandidateSet = new HashSet();
   private final Set<SchemaDocument> globalResultSet = new HashSet();
   private final Set<SchemaDocument> globalIncludedSchemaDocuments = new HashSet();

   public XBeanSchemaProcessor(BindingCompiler var1, ClassLoader var2) {
      this.compiler = var1;
      this.xmlObjectClassLoader = var2;
   }

   private Map getNamespaces(SchemaDocument var1) {
      HashMap var2 = new HashMap();
      XmlCursor var3 = var1.getSchema().newCursor();

      try {
         var3.getAllNamespaces(var2);
      } finally {
         var3.dispose();
      }

      return var2;
   }

   private void processAllSchemas(SchemaDocument var1, List<SchemaDocument> var2) {
      Map var3 = this.getNamespaces(var1);
      if (verbose) {
         Verbose.log((Object)(" +++ all Namespaces on primary : " + var3));
      }

      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         SchemaDocument var5 = (SchemaDocument)var4.next();
         if (var5 != var1) {
            String var6 = this.getXsdPathForSchemaDocument(var5);
            this.includeSchema(var5, var3, var6);
         }
      }

   }

   private String getXsdPathForSchemaDocument(SchemaDocument var1) {
      String var2 = var1.documentProperties().getSourceName();

      try {
         URL var3 = new URL(var2);
         var2 = (new File(var3.getPath())).getName();
      } catch (MalformedURLException var4) {
         var2 = (new File(var2)).getName();
      }

      return var2;
   }

   private void includeSchema(SchemaDocument var1, Map var2, String var3) {
      Map var4 = this.buildPrefixMap(var2, var1);
      if (verbose) {
         Verbose.log((Object)(" +++ prefixMap : " + var4));
      }

      if (verbose) {
         Verbose.log((Object)(" compiler.includeSchema for schema at '" + var3 + "'"));
      }

      stripIncludesAndStripSchemaLocFromImports(var1);
      this.compiler.includeSchema(var1, var3, var4);
   }

   private static void stripIncludesAndStripSchemaLocFromImports(SchemaDocument var0) {
      int var1;
      for(var1 = var0.getSchema().sizeOfIncludeArray() - 1; var1 >= 0; --var1) {
         var0.getSchema().removeInclude(var1);
      }

      for(var1 = var0.getSchema().sizeOfImportArray() - 1; var1 >= 0; --var1) {
         ImportDocument.Import var2 = var0.getSchema().getImportArray(var1);
         if (var2.isSetSchemaLocation()) {
            var2.unsetSchemaLocation();
         }
      }

   }

   private Map buildPrefixMap(Map var1, SchemaDocument var2) {
      HashMap var3 = new HashMap();
      Iterator var4 = var1.entrySet().iterator();

      while(var4.hasNext()) {
         Map.Entry var5 = (Map.Entry)var4.next();
         String var6 = (String)var5.getKey();
         String var7 = (String)var5.getValue();
         if (ObjectUtil.equals(var2.getSchema().getTargetNamespace(), var7)) {
            var3.put(var6, var7);
         }
      }

      return var3;
   }

   private static XBeanInfo getXBeanInfo(Class var0) {
      QName var2;
      String var3;
      if (XmlObject.class.isAssignableFrom(var0)) {
         SchemaType var4 = XBeanUtil.getBeaSchemaTypeForBeaXmlBean(var0);
         var4 = XBeanUtil.transformST(var4);
         var2 = var4.isDocumentType() ? var4.getDocumentElementName() : var4.getName();
         var3 = (new File(var4.getSourceName())).getName();
         return new XBeanInfo(var3, var2);
      } else if (org.apache.xmlbeans.XmlObject.class.isAssignableFrom(var0)) {
         org.apache.xmlbeans.SchemaType var1 = XBeanUtil.getApacheSchemaTypeForApacheXmlBean(var0);
         var1 = XBeanUtil.transformST(var1);
         var2 = var1.isDocumentType() ? var1.getDocumentElementName() : var1.getName();
         var3 = (new File(var1.getSourceName())).getName();
         return new XBeanInfo(var3, var2);
      } else {
         throw new IllegalArgumentException(var0.getName() + " must be either a bea xml bean or an apache xml bean");
      }
   }

   private List<SchemaDocument> getSchemaDocumentsFor(String var1) throws WsdlException {
      ArrayList var2 = new ArrayList();

      try {
         WsdlSchema var3 = WsdlSchemaImpl.parse(var1);
         addSchemas(var3, var2);
      } catch (WsdlException var14) {
         WsdlDefinitions var4 = WsdlFactory.getInstance().parse(var1);
         WsdlTypes var5 = var4.getTypes();
         SchemaDocument[] var6 = var5.getSchemaArray();
         String var7 = (new File(var4.getWsdlLocation())).getName();
         this.processedXsdPaths.add(var7);
         SchemaDocument[] var8 = var6;
         int var9 = var6.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            SchemaDocument var11 = var8[var10];
            var2.add(var11);
            String var12 = this.getXsdPathForSchemaDocument(var11);
            String var13 = this.getAbsolutePathForSchemaDocument(var11);
            if (var7.equals(var12)) {
               this.globalCandidateSet.add(var11);
            } else if (!this.processedXsdPaths.contains(var13)) {
               this.processedXsdPaths.add(var13);
               this.globalCandidateSet.add(var11);
            }
         }
      }

      return var2;
   }

   private static void addSchemas(WsdlSchema var0, List<SchemaDocument> var1) {
      var1.add(var0.getSchema());
      Iterator var2 = var0.getImports().iterator();

      while(var2.hasNext()) {
         WsdlSchemaImport var3 = (WsdlSchemaImport)var2.next();
         addSchemas(var3.getSchema(), var1);
      }

   }

   private List<SchemaDocument> getBeaSchemaDocumentsForXmlBean(Class var1) {
      Object var2 = new ArrayList();
      String var3 = XBeanUtil.getXsdPathForXmlBean(this.xmlObjectClassLoader, var1, false);
      if (var3 == null) {
         Verbose.log((Object)(" WARNING !  could not getXsdPath for XmlBean '" + var1.getName() + "'"));
         return (List)var2;
      } else {
         URL var4 = this.xmlObjectClassLoader.getResource(var3);

         try {
            if (var4 != null) {
               var2 = this.getSchemaDocumentsFor(var4.toString());
            } else {
               var2 = this.getSchemaDocumentsFor(var3);
            }
         } catch (WsdlException var10) {
            String var6 = var3;
            var3 = XBeanUtil.getXsdPathForXmlBean(this.xmlObjectClassLoader, var1, true);
            String var7 = " WARNING !  could not get WsdlSchema from xsdPath '" + var6 + "'   try qualified xsdPath path '" + var3 + "'";
            var4 = this.xmlObjectClassLoader.getResource(var3);

            try {
               if (var4 != null) {
                  var2 = this.getSchemaDocumentsFor(var4.toString());
               } else {
                  var2 = this.getSchemaDocumentsFor(var3);
               }
            } catch (WsdlException var9) {
               Verbose.log((Object)(var7 + " WARNING !  could not get WsdlSchema from " + "xsdPath '" + var3 + "'"));
            }
         }

         if (verbose) {
            Verbose.log((Object)(" +++ found schemas are : " + var2));
         }

         if (((List)var2).isEmpty()) {
            Verbose.log((Object)(" WARNING !  could not get SchemaDocument for xmlBean '" + var1.getName() + "'"));
            throw new IllegalArgumentException("null xsd");
         } else {
            return (List)var2;
         }
      }
   }

   private SchemaDocument findRelevantSchemaDocument(List<SchemaDocument> var1, Class var2) {
      SchemaDocument var3 = null;
      if (var1.size() == 1) {
         var3 = (SchemaDocument)var1.get(0);
      } else if (var1.size() > 1) {
         QName var4 = XBeanUtil.getQNameFromXmlBean(this.xmlObjectClassLoader, var2.getName());
         if (var4 != null) {
            if (verbose) {
               Verbose.log((Object)(" +++ QName for '" + var2 + "' is " + var4));
               Verbose.log((Object)(" +++ NamespaceURI from QName : " + var4.getNamespaceURI()));
               Verbose.log((Object)(" +++ localPart of QName : " + var4.getLocalPart()));
            }

            List var5 = findSchemaDocumentsForNamespace(var1, var4.getNamespaceURI());
            if (var5.size() == 1) {
               var3 = (SchemaDocument)var5.get(0);
            } else {
               Iterator var6 = var5.iterator();

               label100:
               while(true) {
                  while(true) {
                     SchemaDocument var7;
                     TopLevelElement[] var16;
                     do {
                        do {
                           if (!var6.hasNext()) {
                              break label100;
                           }

                           var7 = (SchemaDocument)var6.next();
                           SchemaDocument.Schema var8 = var7.getSchema();
                           TopLevelComplexType[] var9 = var8.getComplexTypeArray();
                           if (var9 != null && var9.length != 0) {
                              for(int var10 = 0; var10 < var9.length; ++var10) {
                                 String var11 = var9[var10].getName();
                                 if (verbose) {
                                    Verbose.log((Object)(" +++ ComplexType name : " + var11));
                                 }

                                 if (var11.equals(var4.getLocalPart())) {
                                    var3 = var7;
                                    break;
                                 }
                              }
                           }

                           TopLevelSimpleType[] var14 = var8.getSimpleTypeArray();
                           if (var14 != null && var14.length != 0) {
                              for(int var15 = 0; var15 < var14.length; ++var15) {
                                 String var12 = var14[var15].getName();
                                 if (verbose) {
                                    Verbose.log((Object)(" +++ SimpleType name : " + var12));
                                 }

                                 if (var12.equals(var4.getLocalPart())) {
                                    var3 = var7;
                                    break;
                                 }
                              }
                           }

                           var16 = var8.getElementArray();
                        } while(var16 == null);
                     } while(var16.length == 0);

                     for(int var17 = 0; var17 < var16.length; ++var17) {
                        String var13 = var16[var17].getName();
                        if (verbose) {
                           Verbose.log((Object)(" +++ Element name : " + var13));
                        }

                        if (var13.equals(var4.getLocalPart())) {
                           var3 = var7;
                           break;
                        }
                     }
                  }
               }
            }
         } else {
            Verbose.log((Object)(" WARNING : Could not find QName for xml bean '" + var2.getName()));
         }
      }

      if (var3 == null) {
         Verbose.log((Object)(" WARNING : Could not find SchemaDocument for " + var2.getName()));
      }

      return var3;
   }

   private static List<SchemaDocument> findSchemaDocumentsForNamespace(List<SchemaDocument> var0, String var1) {
      boolean var2 = false;
      if (var1 == null || var1.length() == 0) {
         var2 = true;
      }

      ArrayList var3 = new ArrayList();
      Iterator var4 = var0.iterator();

      while(true) {
         SchemaDocument var5;
         String var7;
         do {
            if (!var4.hasNext()) {
               if (verbose) {
                  Verbose.log((Object)(" +++ found SchemaDocuments for namespace '" + var1 + "' are : " + var3));
               }

               return var3;
            }

            var5 = (SchemaDocument)var4.next();
            SchemaDocument.Schema var6 = var5.getSchema();
            var7 = var6.getTargetNamespace();
            if (verbose) {
               Verbose.log((Object)(" +++ targetnamespace : " + var7));
            }
         } while(!ObjectUtil.equals(var7, var1) && (!var2 || var7 != null));

         var3.add(var5);
      }
   }

   public void includeAllRelevantSchemas(Set var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(true) {
         Class var6;
         XBeanInfo var7;
         do {
            String var4;
            String var5;
            do {
               if (!var3.hasNext()) {
                  if (verbose) {
                     Verbose.log((Object)("\n globalCandidateSet=" + this.globalCandidateSet));
                  }

                  Iterator var12 = var2.iterator();

                  while(var12.hasNext()) {
                     Class var13 = (Class)var12.next();
                     SchemaDocument var14 = this.findRelevantSchemaDocument(Arrays.asList(this.globalCandidateSet.toArray(new SchemaDocument[0])), var13);
                     if (var14 == null) {
                        this.compiler.logWarning(" WARNING: Could not find SchemaDocument for " + var13.getName());
                     } else {
                        this.addRelevantSchemas(var14, var13);
                     }
                  }

                  if (verbose) {
                     Verbose.log((Object)("\n globalResultSet=" + this.globalResultSet));
                  }

                  return;
               }

               var4 = (String)var3.next();
               var5 = XBeanUtil.getSchemaTypeSourceName(this.xmlObjectClassLoader, var4);
            } while(var5 == null);

            if (verbose) {
               Verbose.log((Object)("\n includeSchema  javaClassName='" + var4 + "'"));
            }

            var6 = XBeanUtil.loadXmlBean(this.xmlObjectClassLoader, var4);
            if (var6 == null) {
               throw new IllegalArgumentException(var4);
            }

            var2.add(var6);
            var7 = getXBeanInfo(var6);
         } while(this.processedXsdPaths.contains(var7.getXsdPath()));

         List var8 = this.getBeaSchemaDocumentsForXmlBean(var6);
         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            SchemaDocument var10 = (SchemaDocument)var9.next();
            String var11 = this.getAbsolutePathForSchemaDocument(var10);
            if (!this.processedXsdPaths.contains(var11)) {
               this.globalCandidateSet.add(var10);
               this.processedXsdPaths.add(var11);
            }
         }
      }
   }

   private void insertNamespaces(XmlCursor var1, Map var2) {
      Iterator var3 = var2.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();

         try {
            var1.push();
            var1.toNextToken();
            var1.insertNamespace((String)var4.getKey(), (String)var4.getValue());
            var1.pop();
         } catch (IllegalArgumentException var6) {
         }
      }

   }

   private void addRelevantSchemas(SchemaDocument var1, Class var2) {
      if (var1 != null) {
         if (!this.contains(this.globalResultSet, var1)) {
            HashSet var3 = new HashSet();
            XBeanInfo var4 = getXBeanInfo(var2);
            HashMap var5 = new HashMap();
            var5.put(var4.getQName().getNamespaceURI(), var4.getQName().getPrefix());
            if (verbose) {
               Verbose.log((Object)(" compiler.includeSchema for qName='" + var4.getQName() + "' and schema at '" + var4.getXsdPath() + "'"));
            }

            this.recursiveAddRelevantSchemas(var1, this.getNamespaces(var1), var3, this.globalIncludedSchemaDocuments);
            if (var3.isEmpty()) {
               if (!this.contains(this.globalResultSet, var1)) {
                  this.globalResultSet.add(var1);
                  this.includeSchema(var1, var5, var4.getXsdPath());
               }
            } else {
               Map var6 = this.getNamespaces(var1);
               XmlCursor var7 = var1.getSchema().newCursor();
               String var8 = var1.getSchema().getTargetNamespace();

               try {
                  var7.toEndToken();
                  Iterator var9 = var3.iterator();

                  while(var9.hasNext()) {
                     SchemaDocument var10 = (SchemaDocument)var9.next();
                     if (!ObjectUtil.equals(var8, var10.getSchema().getTargetNamespace()) && var10.getSchema().getTargetNamespace() != null) {
                        Verbose.log((Object)(" WARNING !  Included schema has a target namespace \"" + var10.getSchema().getTargetNamespace() + "\" that does not match the including namespace \"" + var8 + "\" "));
                        throw new IllegalArgumentException("illegal include: included schema in wrong namespace.");
                     }

                     HashMap var11 = new HashMap();
                     XmlCursor var12;
                     if (var10.getSchema().getTargetNamespace() == null) {
                        if (!var6.containsKey("")) {
                           var12 = var1.getSchema().newCursor();
                           var12.toNextToken();
                           var12.insertNamespace("", var8);
                           var12.dispose();
                           var6.put("", var8);
                        } else if (!ObjectUtil.equals((String)var6.get(""), var8)) {
                           var11.put("", var8);
                        }
                     }

                     var1.getSchema().setImportArray(var10.getSchema().getImportArray());

                     for(int var27 = var10.getSchema().sizeOfImportArray() - 1; var27 >= 0; --var27) {
                        var10.getSchema().removeImport(var27);
                     }

                     var12 = var10.getSchema().newCursor();
                     HashMap var13 = new HashMap();

                     try {
                        var12.getAllNamespaces(var13);
                        if (!var13.isEmpty()) {
                           XmlCursor var14 = var1.getSchema().newCursor();
                           var14.toNextToken();
                           Iterator var15 = var13.entrySet().iterator();

                           while(var15.hasNext()) {
                              Map.Entry var16 = (Map.Entry)var15.next();
                              String var17 = (String)var16.getKey();
                              String var18 = (String)var16.getValue();
                              if (!var6.containsKey(var17)) {
                                 var14.insertNamespace(var17, var18);
                                 var6.put(var17, var18);
                              } else if (!ObjectUtil.equals((String)var6.get(var17), var18)) {
                                 var11.put(var17, var18);
                              }
                           }

                           var14.dispose();
                        }

                        if (var11.isEmpty()) {
                           var12.copyXmlContents(var7);
                        } else {
                           if (verbose) {
                              Verbose.log((Object)(" +++ conflictedNamespaces: " + var11));
                           }

                           var12.toChild("http://www.w3.org/2001/XMLSchema", "schema");
                           if (var12.toFirstChild()) {
                              this.insertNamespaces(var12, var11);
                              var12.copyXml(var7);

                              while(var12.toNextSibling()) {
                                 this.insertNamespaces(var12, var11);
                                 var12.copyXml(var7);
                              }
                           }
                        }
                     } finally {
                        var12.dispose();
                     }
                  }
               } finally {
                  var7.dispose();
               }

               this.includeSchema(var1, var5, var4.getXsdPath());
               this.globalResultSet.add(var1);
            }
         }

      }
   }

   private void recursiveAddRelevantSchemas(SchemaDocument var1, Map var2, Set<SchemaDocument> var3, Set<SchemaDocument> var4) {
      IncludeDocument.Include[] var5 = var1.getSchema().getIncludeArray();
      int var6 = var5.length;

      int var7;
      Iterator var9;
      SchemaDocument var10;
      label69:
      for(var7 = 0; var7 < var6; ++var7) {
         IncludeDocument.Include var8 = var5[var7];
         var9 = this.globalCandidateSet.iterator();

         while(true) {
            do {
               do {
                  do {
                     do {
                        do {
                           if (!var9.hasNext()) {
                              continue label69;
                           }

                           var10 = (SchemaDocument)var9.next();
                        } while(this.contains(this.globalResultSet, var10));
                     } while(this.contains(var4, var10));
                  } while(this.contains(var3, var10));
               } while(!ObjectUtil.equals((new File(var8.getSchemaLocation())).getName(), this.getXsdPathForSchemaDocument(var10)));
            } while(!ObjectUtil.equals(var1.getSchema().getTargetNamespace(), var10.getSchema().getTargetNamespace()) && var10.getSchema().getTargetNamespace() != null);

            this.globalResultSet.add(var10);
            var4.add(var10);
            var3.add(var10);
            this.recursiveAddRelevantSchemas(var10, var2, var3, var4);
         }
      }

      ImportDocument.Import[] var11 = var1.getSchema().getImportArray();
      var6 = var11.length;

      for(var7 = 0; var7 < var6; ++var7) {
         ImportDocument.Import var12 = var11[var7];
         var9 = this.globalCandidateSet.iterator();

         while(var9.hasNext()) {
            var10 = (SchemaDocument)var9.next();
            if (!this.contains(this.globalResultSet, var10) && ObjectUtil.equals(var12.getNamespace(), var10.getSchema().getTargetNamespace())) {
               this.globalResultSet.add(var10);
               this.includeSchema(var10, var2, this.getXsdPathForSchemaDocument(var10));
               this.recursiveAddRelevantSchemas(var10, var2, var3, var4);
            }
         }
      }

   }

   private String getAbsolutePathForSchemaDocument(SchemaDocument var1) {
      String var2 = var1.documentProperties().getSourceName();

      try {
         return (new URL(var2)).toExternalForm();
      } catch (MalformedURLException var6) {
         if (!var2.startsWith("/")) {
            var2 = "/" + var2;
         }

         try {
            return (new URL("file:" + var2)).toExternalForm();
         } catch (MalformedURLException var5) {
            return var2;
         }
      }
   }

   private boolean contains(Set<SchemaDocument> var1, SchemaDocument var2) {
      Iterator var3 = var1.iterator();

      do {
         if (!var3.hasNext()) {
            return false;
         }
      } while(!ObjectUtil.equals(((SchemaDocument)var3.next()).toString(), var2.toString()));

      return true;
   }

   private static class XBeanInfo {
      private final String xsdPath;
      private final QName qName;

      XBeanInfo(String var1, QName var2) {
         this.xsdPath = var1;
         this.qName = var2;
      }

      private String getXsdPath() {
         return this.xsdPath;
      }

      private QName getQName() {
         return this.qName;
      }
   }
}
