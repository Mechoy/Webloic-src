package weblogic.auddi.uddi.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.tree.DefaultMutableTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.datastructure.TModel;
import weblogic.auddi.uddi.datastructure.TModelExt;
import weblogic.auddi.uddi.datastructure.TModelHandler;
import weblogic.auddi.uddi.datastructure.TModels;
import weblogic.auddi.uddi.datastructure.UDDIXMLHandlerMaker;
import weblogic.auddi.uddi.xml.ParserWrapper;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.PropertyManager;
import weblogic.auddi.util.Util;
import weblogic.auddi.xml.SchemaException;

public class UserCategories extends StandardTModels {
   private static UserCategories s_instance = null;
   private TModels m_tModelExts = null;

   private UserCategories() throws UDDIException {
      Logger.trace("+UserCategories.CTOR()");
      this.m_tModelExts = new TModels();
      String var1 = PropertyManager.getRuntimeProperty("pluggableTModel.file.list");
      Logger.debug("fileList: " + var1);
      if (var1 != null && !var1.trim().equals("")) {
         StringTokenizer var2 = new StringTokenizer(var1, ",");

         while(var2.hasMoreTokens()) {
            String var3 = var2.nextToken().trim();
            if (!var3.equals("")) {
               this.process(this.validateFile(var3));
            }
         }
      }

      Logger.trace("-UserCategories.CTOR()");
   }

   public static UserCategories getInstance() throws UDDIException {
      if (s_instance == null) {
         Class var0 = UserCategories.class;
         synchronized(UserCategories.class) {
            if (s_instance == null) {
               s_instance = new UserCategories();
            }
         }
      }

      return s_instance;
   }

   public Document validateFile(String var1) throws UDDIException {
      Logger.trace("+UserCategories.validateFile()");

      try {
         if (!var1.equals("")) {
            File var2 = new File(var1);
            String var3 = Util.getFileContent(var2);
            Document var4 = this.validateContent(var3);
            Logger.trace("-UserCategories.validateFile()");
            return var4;
         } else {
            return null;
         }
      } catch (IOException var5) {
         Logger.trace("-EXCEPTION(FatalErrorException) UserCategories.validateFile()");
         throw new FatalErrorException(UDDIMessages.get("error.loading.pluggableTModel", var5.getMessage()));
      }
   }

   public Document validateContent(String var1) throws UDDIException {
      Logger.trace("+UserCategories.validateContent()");

      try {
         if (var1 != null) {
            Document var2 = ParserWrapper.parseRequest(var1, true);
            Logger.trace("-UserCategories.validateFormat()");
            return var2;
         } else {
            return null;
         }
      } catch (SchemaException var5) {
         Exception var3 = (Exception)var5.getNestedException();
         String var4 = var5.getMessage();
         if (var4 == null) {
            if (var3 != null && var3.getMessage() != null) {
               var4 = var3.getMessage();
            } else {
               var4 = " schema vilation - invalid file format";
            }
         }

         Logger.trace("-EXCEPTION(FatalErrorException) UserCategories.validateContent()");
         throw new FatalErrorException(UDDIMessages.get("error.loading.pluggableTModel", var4));
      }
   }

   public void process(Document var1) throws UDDIException {
      Logger.trace("+UserCategories.process()");
      TModel var2 = null;
      TModelExt var3 = null;
      NodeList var4 = null;
      NodeList var5 = var1.getElementsByTagName("tModel");
      if (var5.getLength() == 0) {
         throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "tModel"));
      } else {
         Node var6 = var5.item(0);
         UDDIXMLHandlerMaker var7 = UDDIXMLHandlerMaker.getInstance();
         TModelHandler var8 = (TModelHandler)var7.makeHandler("tModel");
         var2 = (TModel)var8.create(var6);
         if (var2.getTModelKey() == null) {
            throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "tModelKey"));
         } else if (var2.getName() == null) {
            throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "tModel name"));
         } else {
            Logger.debug("create TModelExt: ");
            var3 = new TModelExt(var2);
            var1.getDocumentElement().removeChild(var6);
            Element var9 = var1.getDocumentElement();
            String var10;
            if (var9.getAttributeNode("checked") != null) {
               var10 = var9.getAttributeNode("checked").getNodeValue();
               boolean var11 = Boolean.valueOf(var10);
               var3.setChecked(var11);
            } else {
               var3.setChecked(false);
            }

            Logger.trace("checked tModel: " + var3.isChecked());
            if (var9.getAttributeNode("type") == null) {
               throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "type attribute"));
            } else {
               var10 = var9.getAttributeNode("type").getNodeValue();
               var3.setType(var10);
               var5 = var1.getElementsByTagName("applicability");
               int var16;
               if (var5.getLength() > 0) {
                  var6 = var5.item(0);
                  var4 = var6.getChildNodes();
                  if (var4 != null) {
                     for(var16 = 0; var16 < var4.getLength(); ++var16) {
                        var9 = (Element)var4.item(var16);
                        if (var9.getNodeType() != 8 && var9.getNodeName().equals("scope") && var9.getChildNodes().item(0) != null) {
                           String var17 = var9.getChildNodes().item(0).getNodeValue();
                           Logger.debug("adding applicable scope: " + var17);
                           var3.addApplicableScope(var17);
                        }
                     }
                  }

                  var1.getDocumentElement().removeChild(var6);
               }

               this.m_tModelExts.add(var3);
               if (var3.isChecked()) {
                  var5 = var1.getElementsByTagName("category");
                  var16 = var5.getLength();
                  NodeList var18 = var1.getElementsByTagName("categories");
                  int var12 = var18.getLength();
                  if (var12 == 0) {
                     throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "categories in pluggable tModel"));
                  } else if (var16 == 0) {
                     throw new FatalErrorException(UDDIMessages.get("error.fatalError.missingElement", "category"));
                  } else {
                     var6 = var18.item(0);
                     var1.getDocumentElement().removeChild(var6);
                     var4 = var6.getChildNodes();

                     for(int var13 = 0; var13 < var4.getLength(); ++var13) {
                        var6 = var4.item(var13);
                        var1.getDocumentElement().appendChild(var6);
                     }

                     XMLToTree var19 = new XMLToTree(var1);
                     DefaultMutableTreeNode var14 = var19.getRoot();
                     NodeItem var15 = (NodeItem)var14.getUserObject();
                     var15.setKey(var3.getTModelKey().getKey());
                     var15.setName(var3.getName().getName());
                     this.m_items.put(var3.getTModelKey().getKey().toLowerCase(), var19);
                     Logger.trace("-UserCategories.process()");
                  }
               }
            }
         }
      }
   }

   public TModels getPluggableTModels() {
      return this.m_tModelExts;
   }

   public TModelExt getTModelExt(String var1) {
      Logger.trace("+UserCategories.getTModelExt()");
      Logger.trace("tModels.size(): " + this.m_tModelExts.size());
      if (var1 == null) {
         return null;
      } else {
         for(TModelExt var2 = (TModelExt)this.m_tModelExts.getFirst(); var2 != null; var2 = (TModelExt)this.m_tModelExts.getNext()) {
            if (var2.getTModelKey().getKey().equalsIgnoreCase(var1)) {
               Logger.debug("found: " + var1);
               Logger.trace("-UserCategories.getTModelExt()");
               return var2;
            }
         }

         Logger.debug("not found: " + var1);
         Logger.trace("-UserCategories.getTModelExt()");
         return null;
      }
   }

   public HashMap getCategorizationItems() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.m_items.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         TModelExt var4 = this.getTModelExt(var3);
         if (var4 != null && var4.getType() != null && var4.isChecked() && var4.getType().equalsIgnoreCase("categorization")) {
            var1.put(var3, this.m_items.get(var3));
         }
      }

      return var1;
   }

   public HashMap getIdentifierItems() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.m_items.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         TModelExt var4 = this.getTModelExt(var3);
         if (var4 != null && var4.getType() != null && var4.getType().equalsIgnoreCase("identifier")) {
            var1.put(var3, var4);
         }
      }

      return var1;
   }
}
