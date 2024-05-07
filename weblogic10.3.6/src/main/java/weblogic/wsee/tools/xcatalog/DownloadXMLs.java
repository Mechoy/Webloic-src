package weblogic.wsee.tools.xcatalog;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.tools.ant.BuildException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlConstants;
import weblogic.xml.domimpl.DocumentImpl;

public class DownloadXMLs {
   private static final boolean verbose = Verbose.isVerbose(DownloadXMLs.class);
   private static final String ENC_UTF8 = "UTF-8";
   private static final String NS_WSDL;
   private static final String NS_SCHEMA = "http://www.w3.org/2001/XMLSchema";
   private HashMap<String, SavedURIInfo> savedUris;
   private List<String> savedFiles;
   private HashMap<String, String> localUris;
   private List<FileAnalysis> faList;
   private File xCatalogDir;
   private boolean copyFlag;
   private String encoding;
   private LREntityResolver entityResolver;
   private HashMap<URL, String> xmlMaps;
   private PrintStream out;

   public DownloadXMLs() {
      this(System.out);
   }

   public DownloadXMLs(PrintStream var1) {
      this.savedUris = new HashMap();
      this.savedFiles = new ArrayList();
      this.localUris = new HashMap();
      this.faList = new ArrayList();
      this.xCatalogDir = null;
      this.copyFlag = false;
      this.encoding = "UTF-8";
      this.entityResolver = null;
      this.xmlMaps = new HashMap();
      this.out = var1;
   }

   private boolean isLegalFileName(String var1) {
      if (var1 == null) {
         return false;
      } else if (var1.indexOf("%") < 0 && var1.indexOf("&") < 0) {
         File var2 = new File(this.xCatalogDir, var1);
         if (!var2.exists()) {
            try {
               var2.createNewFile();
               boolean var3 = var2.exists();
               return var3;
            } catch (IOException var7) {
            } finally {
               var2.deleteOnExit();
            }

            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   private String chg2SavedUri(String var1, String var2, boolean var3) {
      if (var1.indexOf("///") > 0) {
         var1 = var1.replaceAll("///", "/");
      }

      if (this.savedUris.containsKey(var1)) {
         return ((SavedURIInfo)this.savedUris.get(var1)).fileName;
      } else {
         String var4 = var1;
         int var5 = var1.lastIndexOf("/");
         if (var5 >= 0) {
            var4 = var1.substring(var5 + 1);
         }

         String var6 = var4;
         int var7 = var4.lastIndexOf(".");
         if (var7 > 0) {
            var4 = var4.substring(0, var7);
         }

         int var8 = var4.indexOf("?");
         String var9;
         String var10;
         if (var8 >= 0) {
            var9 = var4.substring(var8 + 1);
            var10 = var9.toLowerCase(Locale.ENGLISH);
            if (var10.startsWith("wsdl=")) {
               var4 = var4.substring(0, var8) + var9.substring(5);
            } else if (var10.startsWith("xsd=")) {
               var4 = var4.substring(0, var8) + var9.substring(4);
            } else if (var10.equals("wsdl")) {
               var4 = var4.substring(0, var8);
            } else {
               var4 = var9;
            }
         }

         if (!this.isLegalFileName(var4)) {
            var4 = "SYS_DEF";
         }

         var9 = var4;
         if (var3) {
            var10 = ".xsd";
         } else {
            var10 = ".wsdl";
         }

         if (this.localUris.containsKey(var4 + var10)) {
            int var11 = 1;

            while(true) {
               var9 = var4 + "_" + var11;
               if (!this.localUris.containsKey(var9 + var10)) {
                  break;
               }

               ++var11;
            }
         }

         var4 = var9 + var10;
         this.localUris.put(var4, "");
         SavedURIInfo var12 = new SavedURIInfo();
         var12.fileName = var4;
         var12.isSchema = var3;
         var12.originalContent = var2;
         this.savedUris.put(var1, var12);
         if (!var4.equals(var6) && this.out != null) {
            this.out.println("Rename file [ " + var6 + " ] to [ " + var4 + "]");
         }

         if (verbose) {
            Verbose.say("uri=" + var1);
            Verbose.say("fileName=" + var4);
         }

         return var4;
      }
   }

   private void processNodeList(NodeList var1, boolean var2, String var3) throws BuildException {
      if (var1 != null) {
         for(int var4 = 0; var4 < var1.getLength(); ++var4) {
            Element var5 = (Element)var1.item(var4);
            String var6 = null;
            if (var2) {
               if (var5.hasAttribute("schemaLocation")) {
                  var6 = var5.getAttribute("schemaLocation");
               }
            } else if (var5.hasAttribute("location")) {
               var6 = var5.getAttribute("location");
            }

            if (var6 != null) {
               String var7 = this.parseXML(var3, var6, var2);
               if (var7 == null) {
                  throw new BuildException("Can not get the filename for just saved temporary file!");
               }

               if (var2) {
                  var5.setAttribute("schemaLocation", var7);
               } else {
                  var5.setAttribute("location", var7);
               }
            }
         }
      }

   }

   private String getUniSystemId(InputSource var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = var1.getSystemId();
         return XCatalogUtil.uniqueFile(var2);
      }
   }

   private String parseXML(String var1, String var2, boolean var3) throws BuildException {
      return this.parseXML(var1, var2, (String)null, var3);
   }

   private String parseXML(String var1, String var2, String var3, boolean var4) {
      String[] var5 = this.parseXML_inner(var1, var2, var3, var4);
      URL var6 = XCatalogUtil.toURL(var5[0]);
      if (var6 != null) {
         this.xmlMaps.put(var6, var5[1]);
      }

      return var5[1];
   }

   private String[] parseXML_inner(String var1, String var2, String var3, boolean var4) throws BuildException {
      String[] var5 = new String[2];
      InputSource var6 = null;
      String var7 = var2;
      if (this.entityResolver != null) {
         try {
            var6 = this.entityResolver.resolveEntity(var2, var2);
         } catch (SAXException var20) {
            throw new BuildException(var20);
         } catch (IOException var21) {
            throw new BuildException(var21);
         }
      }

      byte var8 = 2;
      if (var6 == null) {
         if (var1 != null && !var2.startsWith("/") && var2.indexOf(":") < 0) {
            var2 = var1 + "/" + var2;
         }

         var6 = new InputSource(var2);
      } else {
         var8 = 1;
         if (this.entityResolver.isLocal()) {
            var8 = 0;
         }
      }

      if (var6 == null) {
         throw new BuildException("Can not read the uri [ " + var2 + " ], please confirm if it exists");
      } else {
         var5[0] = var2;
         String var9 = var6.getSystemId();
         String var10 = this.getUniSystemId(var6);
         if (this.savedUris.containsKey(var10)) {
            var5[1] = var3 != null ? var3 : ((SavedURIInfo)this.savedUris.get(var10)).fileName;
            return var5;
         } else {
            Document var11;
            try {
               var11 = XCatalogUtil.getDocument(var6);
            } catch (XCatalogException var19) {
               throw new BuildException(var19.getMessage(), var19.getCause());
            }

            if (var11 == null) {
               throw new BuildException("Can not parse the uri [ " + var2 + " ] or it referenced uri, please confirm if it exists");
            } else {
               String var12 = XCatalogUtil.doc2String(var11);
               String var13 = this.getSavedURI(var12, var4);
               if (var13 != null) {
                  var5[1] = var3 != null ? var3 : var13;
                  return var5;
               } else {
                  FileAnalysis var14 = new FileAnalysis(var8, var1, var2, var9, var7);
                  this.faList.add(var14);
                  String var15 = XCatalogUtil.getBaseDir(var9);
                  var13 = var3 != null ? var3 : this.chg2SavedUri(var10, var12, var4);
                  NodeList var16 = var11.getElementsByTagNameNS(NS_WSDL, "import");
                  this.processNodeList(var16, false, var15);
                  var16 = var11.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "import");
                  this.processNodeList(var16, true, var15);
                  var16 = var11.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "include");
                  this.processNodeList(var16, true, var15);
                  var16 = var11.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "redefine");
                  this.processNodeList(var16, true, var15);
                  if (this.copyFlag && !this.savedFiles.contains(var13)) {
                     try {
                        File var17 = new File(this.xCatalogDir, var13);
                        this.encoding = var6.getEncoding();
                        if (this.encoding == null) {
                           boolean var18 = var11 instanceof DocumentImpl || var11.getImplementation().hasFeature("Core", "3.0");
                           if (var18) {
                              this.encoding = var11.getXmlEncoding();
                           }
                        }

                        if (this.encoding == null) {
                           this.encoding = "UTF-8";
                        }

                        XCatalogUtil.writeDoc2File(var11, var17, this.encoding);
                        if (this.out != null) {
                           this.out.println("Download file [" + var13 + "] to " + this.xCatalogDir.getPath());
                        }
                     } catch (IOException var22) {
                        throw new BuildException(var22);
                     }

                     this.savedFiles.add(var13);
                  }

                  var5[1] = var13;
                  return var5;
               }
            }
         }
      }
   }

   public String parseXMLs(CatalogOptions var1, File var2, String var3, boolean var4) throws BuildException {
      return this.parseXMLs(var1, var2, var3, (String)null, var4);
   }

   public String parseXMLs(CatalogOptions var1, File var2, String var3, String var4, boolean var5) throws BuildException {
      try {
         this.entityResolver = XCatalogUtil.createEntityResolver(var1);
      } catch (XCatalogException var7) {
         throw new BuildException(var7);
      }

      this.copyFlag = var5;
      if (var5) {
         this.xCatalogDir = var2;
         if (this.out != null) {
            this.out.println("Catalog dir = " + var2.getPath());
         }

         var2.mkdirs();
      }

      String var6 = this.parseXML((String)null, var3, var4, false);
      if (var2 != null) {
         var6 = var2.getPath() + File.separator + var6;
      }

      if (var1 != null) {
         var1.setXmlMaps(this.xmlMaps);
      }

      return var6;
   }

   public List<FileAnalysis> getFileAnalysisList() {
      return this.faList;
   }

   private String getSavedURI(String var1, boolean var2) {
      Iterator var3 = this.savedUris.keySet().iterator();

      SavedURIInfo var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (SavedURIInfo)this.savedUris.get(var3.next());
      } while(var4.isSchema != var2 || !var4.comparesContent(var1));

      return var4.fileName;
   }

   static {
      NS_WSDL = WsdlConstants.wsdlNS;
   }

   private class SavedURIInfo {
      public boolean isSchema;
      public String fileName;
      public String originalContent;

      private SavedURIInfo() {
      }

      public boolean comparesContent(String var1) {
         return var1 != null && this.originalContent != null ? this.originalContent.equals(var1) : false;
      }

      // $FF: synthetic method
      SavedURIInfo(Object var2) {
         this();
      }
   }
}
