package weblogic.wsee.tools.xcatalog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.anttasks.AntUtil;
import weblogic.wsee.tools.anttasks.ClientGenFacadeTask;
import weblogic.wsee.tools.anttasks.DelegatingJavacTask;
import weblogic.wsee.tools.clientgen.jaxws.Options;
import weblogic.wsee.util.Verbose;
import weblogic.xml.domimpl.Saver;
import weblogic.xml.domimpl.SaverOptions;

public class ClientGenXMLs {
   private static final boolean verbose = Verbose.isVerbose(ClientGenXMLs.class);
   private static final String JAXWS_CATALOG_FILE = "jax-ws-catalog.xml";
   private static final String CATALOG_ROOT = "catalog";
   private static final String TAG_PUBLIC = "public";
   private static final String TAG_SYSTEM = "system";
   private static final String ATTR_SYSTEM_ID = "systemId";
   private static final String ATTR_PUBLIC_ID = "publicId";
   private static final String ATTR_URI = "uri";
   private static final String ATTR_PREFER = "prefer";
   private static final String CATALOG_NS = "urn:oasis:names:tc:entity:xmlns:xml:catalog";
   private static final String META_INF_WSDLS = "META-INF/wsdls";

   private static void antLog(Task var0, String var1, int var2) {
      if (var0 != null) {
         var0.log(var1, var2);
      }
   }

   private static void validateURL(String var0) throws BuildException {
      try {
         new URL(var0);
      } catch (MalformedURLException var2) {
         throw new BuildException("Catalog reference ID [ " + var0 + " ] is not a valid URL,\n\t suggest to define it as \"http://....\"", var2);
      }
   }

   public static void doAllCatalogFiles(Task var0, File var1, CatalogInfo var2) {
      copyRelatedFiles(var0, var1, var2);
      genJAXWSCatalogDotXML(var0, var2, var1, (String)null);
   }

   public static CatalogInfo processClient(Task var0, WebServiceType var1, Options var2, String var3, CatalogInfo var4) throws BuildException {
      if (var4 == null) {
         var4 = new CatalogInfo();
      }

      if (var1 == WebServiceType.JAXRPC) {
         return var4;
      } else {
         File var6;
         String var7;
         String var10;
         if (!var4.isEmbeddedInJwsc && var3.startsWith("file:/") && var2.isGenRuntimeCatalog() && !var2.isCopyWsdl()) {
            File var5 = ((ClientGenFacadeTask)var0).getDestDir();
            var6 = new File(var5, "META-INF");

            try {
               var7 = var2.getWsdllocationOverride() != null ? var2.getWsdllocationOverride() : var3.substring(var3.lastIndexOf(47) + 1);
               URL var8 = new URL(var3);
               File var9 = new File(var8.toURI());
               var10 = getRelativePath(var6, var9);
               var4.localCatalogs.put(var7, var10);
               var2.setWsdllocationOverride(var7);
            } catch (MalformedURLException var18) {
               throw new BuildException(var18);
            } catch (URISyntaxException var19) {
               throw new BuildException(var19);
            }
         }

         if (var2 == null) {
            return var4;
         } else {
            if (!var2.isCopyWsdl()) {
               if (var2.getCatalog() == null && var2.getXmlCatalog() == null) {
                  return var4;
               }

               if (!var2.isGenRuntimeCatalog()) {
                  antLog(var0, "Does not generate XML Catalog files for runtime", 2);
                  return var4;
               }
            }

            DownloadXMLs var20 = new DownloadXMLs();
            var6 = null;
            var7 = null;
            File var21 = getDestDir(var0);
            String var22;
            if (var2.isCopyWsdl() && var21 != null) {
               var22 = "META-INF/wsdls";
               if (var2.getWsdllocationOverride() != null) {
                  var10 = var2.getWsdllocationOverride();
                  int var11 = var10.replace('\\', '/').lastIndexOf(47);
                  if (var11 > 0) {
                     var22 = var10.substring(0, var11);
                     var7 = var10.substring(var11 + 1);
                  } else if (var11 == 0) {
                     var7 = var10.substring(1);
                     var22 = null;
                  } else {
                     var7 = var10;
                  }
               }

               var6 = var22 != null ? new File(var21, var22) : var21;
            }

            var22 = var20.parseXMLs(var2, var6, var3, var7, var2.isCopyWsdl());
            if (var2.isCopyWsdl() && var2.getWsdllocationOverride() == null) {
               int var23 = var22.replace('\\', '/').lastIndexOf(47);
               StringBuffer var25 = new StringBuffer();
               var25.append("META-INF/wsdls");
               var25.append("/");
               if (var23 > 0) {
                  var25.append(var22.substring(var23 + 1));
               } else if (var23 == 0) {
                  var25.append(var22.substring(1));
               } else {
                  var25.append(var22);
               }

               var2.setWsdllocationOverride(var25.toString());
            }

            List var24 = var20.getFileAnalysisList();
            HashMap var26 = getLocalCatalogs(var24);
            Iterator var12 = var26.keySet().iterator();
            String var13 = null;

            while(var12.hasNext()) {
               var13 = (String)var12.next();
               if (var4.localCatalogs.containsKey(var13)) {
                  if (!((String)var4.localCatalogs.get(var13)).equals(var26.get(var13))) {
                     throw new BuildException("One XML Catalog Reference ID [" + var13 + "] is referencing multiple URIs!");
                  }
               } else {
                  validateURL(var13);
                  var4.localCatalogs.put(var13, var26.get(var13));
               }
            }

            File var14 = var2.getCatalog();
            if (var14 == null) {
               var4.fileAnalysisList.addAll(var24);
               return var4;
            } else {
               String var15 = var14.getParent();

               FileAnalysis var17;
               for(Iterator var16 = var24.iterator(); var16.hasNext(); var17.cBaseDir = var15) {
                  var17 = (FileAnalysis)var16.next();
               }

               var4.fileAnalysisList.addAll(var24);
               HashMap var27 = getRemoteCatalogs(var4.fileAnalysisList, var2);
               var12 = var27.keySet().iterator();
               var13 = null;

               while(var12.hasNext()) {
                  var13 = (String)var12.next();
                  if (var4.remoteCatalogs.containsKey(var13)) {
                     if (!((String)var4.remoteCatalogs.get(var13)).equals(var27.get(var13))) {
                        throw new BuildException("One XML Catalog Reference ID [" + var13 + "] is referencing multiple URIs!");
                     }
                  } else {
                     validateURL(var13);
                     var4.remoteCatalogs.put(var13, var27.get(var13));
                     add2CopyFiles(var0, var4, (String)var27.get(var13), var15);
                  }
               }

               return var4;
            }
         }
      }
   }

   private static File getDestDir(Task var0) {
      File var1 = null;
      if (var0 instanceof ClientGenFacadeTask) {
         var1 = ((ClientGenFacadeTask)var0).getDestDir();
      } else if (var0 instanceof DelegatingJavacTask) {
         var1 = ((DelegatingJavacTask)var0).getDestdir();
      }

      return var1;
   }

   private static boolean isAbsPath(String var0) {
      return var0 == null || var0.indexOf(":") > 0 || var0.startsWith("/");
   }

   private static void checkUpperRelativePath(Task var0, CatalogInfo var1, String var2) {
      if (var2.startsWith("..")) {
         antLog(var0, "[FATAL Warning] The uri [" + var2 + "] " + "is started with upper(..) relative path, " + "so the Ant (copy) task can not copy it to runtime env!", 1);
         var1.copyUpperRelativePathFlag = true;
      }

   }

   private static void add2CopyFiles(Task var0, CatalogInfo var1, String var2, String var3) {
      if (!isAbsPath(var2)) {
         checkUpperRelativePath(var0, var1, var2);
         FileSet var4 = new FileSet();
         File var5 = new File(var3);
         var4.setDir(var5);
         var4.setIncludes(var2);
         var1.copyFiles.add(var4);
      }
   }

   private static void copyRelatedFiles(Task var0, File var1, CatalogInfo var2) throws BuildException {
      if (var2 != null && var2.copyFiles.size() != 0) {
         Iterator var3 = var2.fileAnalysisList.iterator();

         while(true) {
            FileAnalysis var4;
            do {
               do {
                  do {
                     if (!var3.hasNext()) {
                        antLog(var0, "Copy related XML catalog files to runtime env...", 2);
                        if (var2.copyUpperRelativePathFlag) {
                           throw new BuildException("Some uri(s) are started with upper(..) relative path, jax-ws-catalog.xml and related files can not be copied into runtime env.");
                        }

                        AntUtil.copyFiles(var0.getProject(), var2.copyFiles, var1);
                        return;
                     }

                     var4 = (FileAnalysis)var3.next();
                  } while(var4.Type != 2);

                  if (verbose) {
                     Verbose.say("originalURI=" + var4.originalURI + ":" + var4.idOrURI + ":" + var4.cBaseDir);
                  }
               } while(var4.cBaseDir == null);
            } while(isAbsPath(var4.originalURI));

            FileSet var5 = new FileSet();
            File var6 = new File(var4.cBaseDir);
            var5.setDir(var6);
            File var7 = new File(var4.idOrURI);

            try {
               String var8 = XCatalogUtil.getRelativeFile(var7, var6);
               if (verbose) {
                  Verbose.say("includeFile=" + var8);
               }

               checkUpperRelativePath(var0, var2, var8);
               var5.setIncludes(var8);
            } catch (IOException var9) {
               if (verbose) {
                  var9.printStackTrace();
               }

               throw new BuildException("Bad file path!!!", var9);
            }

            var2.copyFiles.add(var5);
         }
      }
   }

   private static void addReference(Document var0, Element var1, String var2, String var3) {
      Element var4 = var0.createElement("system");
      var4.setAttribute("systemId", var2);
      var4.setAttribute("uri", var3);
      var1.appendChild(var4);
   }

   private static void genJAXWSCatalogDotXML(Task var0, CatalogInfo var1, File var2, String var3) throws BuildException {
      if (var1 != null && var1.catalogSize() != 0) {
         if (!var1.copyUpperRelativePathFlag) {
            antLog(var0, "Generate the jax-ws-catalog.xml file to runtime env...", 2);

            try {
               DocumentBuilder var4 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
               Document var5 = var4.newDocument();
               Element var6 = var5.createElement("catalog");
               var6.setAttribute("xmlns", "urn:oasis:names:tc:entity:xmlns:xml:catalog");
               var6.setAttribute("prefer", "system");
               Iterator var7 = var1.localCatalogs.keySet().iterator();

               String var8;
               while(var7.hasNext()) {
                  var8 = (String)var7.next();
                  addReference(var5, var6, var8, (String)var1.localCatalogs.get(var8));
               }

               var7 = var1.remoteCatalogs.keySet().iterator();

               while(var7.hasNext()) {
                  var8 = (String)var7.next();
                  addReference(var5, var6, var8, (String)var1.remoteCatalogs.get(var8));
               }

               var5.appendChild(var6);
               SaverOptions var9 = SaverOptions.getDefaults();
               var9.setPrettyPrint(true);
               var9.setWriteXmlDeclaration(true);
               if (var3 != null) {
                  var9.setEncoding(var3);
               } else {
                  var9.setEncoding("UTF-8");
               }

               var2.mkdir();
               File var10 = new File(var2, "jax-ws-catalog.xml");
               FileOutputStream var11 = new FileOutputStream(var10);
               Saver.save(var11, var5, var9);
               var11.close();
            } catch (ParserConfigurationException var12) {
               throw new BuildException(var12.getMessage(), var12);
            } catch (FileNotFoundException var13) {
               throw new BuildException(var13.getMessage(), var13);
            } catch (IOException var14) {
               throw new BuildException(var14.getMessage(), var14);
            }
         }
      }
   }

   private static HashMap<String, String> getLocalCatalogs(List<FileAnalysis> var0) throws BuildException {
      HashMap var1 = new HashMap();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         FileAnalysis var3 = (FileAnalysis)var2.next();
         if (var3.Type == 0) {
            var1.put(var3.idOrURI, var3.systemId);
         }
      }

      return var1;
   }

   private static HashMap<String, String> getRemoteCatalogs(List<FileAnalysis> var0, CatalogOptions var1) {
      HashMap var2 = new HashMap();
      File var3 = var1.getCatalog();
      if (var3 == null) {
         return null;
      } else {
         FileInputStream var4 = null;
         boolean var5 = false;

         try {
            var4 = new FileInputStream(var3);
         } catch (FileNotFoundException var14) {
            var5 = true;
         } catch (IOException var15) {
            var5 = true;
         }

         if (var5) {
            if (verbose) {
               Verbose.log((Object)("WARNING: Not found catalog file[" + var3 + "]"));
            }

            return null;
         } else {
            Document var6;
            try {
               var6 = XCatalogUtil.getDocument(new InputSource(var4));
            } catch (XCatalogException var13) {
               throw new BuildException(var13);
            }

            String var9 = null;
            NodeList var10 = var6.getElementsByTagName("public");

            int var11;
            Element var12;
            for(var11 = 0; var11 < var10.getLength(); ++var11) {
               var12 = (Element)var10.item(var11);
               String var7 = var12.getAttribute("publicId");
               var9 = var12.getAttribute("uri");
               if (var7 != null && var9 != null) {
                  var2.put(var7, var9);
               }
            }

            var10 = var6.getElementsByTagName("system");

            for(var11 = 0; var11 < var10.getLength(); ++var11) {
               var12 = (Element)var10.item(var11);
               String var8 = var12.getAttribute("systemId");
               var9 = var12.getAttribute("uri");
               if (var8 != null && var9 != null && inAFList(var0, var8)) {
                  var2.put(var8, var9);
               }
            }

            return var2;
         }
      }
   }

   private static boolean inAFList(List<FileAnalysis> var0, String var1) {
      Iterator var2 = var0.iterator();

      FileAnalysis var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (FileAnalysis)var2.next();
      } while(!var3.idOrURI.equals(var1) || var3.Type != 1);

      return true;
   }

   public static String getRelativePath(File var0, File var1) {
      List var2 = getPathList(var0);
      List var3 = getPathList(var1);
      String var4 = matchPathLists(var2, var3);
      return var4;
   }

   private static List<String> getPathList(File var0) {
      ArrayList var1 = new ArrayList();

      try {
         for(File var2 = var0.getCanonicalFile(); var2 != null; var2 = var2.getParentFile()) {
            var1.add(var2.getName());
         }
      } catch (IOException var4) {
         var4.printStackTrace();
         var1 = null;
      }

      return var1;
   }

   private static String matchPathLists(List<String> var0, List<String> var1) {
      String var4 = "";
      int var2 = var0.size() - 1;

      int var3;
      for(var3 = var1.size() - 1; var2 >= 0 && var3 >= 0 && ((String)var0.get(var2)).equals(var1.get(var3)); --var3) {
         --var2;
      }

      while(var2 >= 0) {
         var4 = var4 + "../";
         --var2;
      }

      while(var3 >= 1) {
         var4 = var4 + (String)var1.get(var3) + "/";
         --var3;
      }

      var4 = var4 + (String)var1.get(var3);
      return var4;
   }
}
