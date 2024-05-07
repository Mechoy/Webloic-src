package weblogic.auddi.uddi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.datastructure.Language;
import weblogic.auddi.uddi.xml.ParserWrapper;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.Util;
import weblogic.auddi.xml.SchemaException;

public class StandardLanguages {
   private static StandardLanguages s_instance = null;
   private HashMap m_mapByCode = null;
   private HashMap m_mapByName = null;
   private Language[] m_list = null;

   private StandardLanguages() throws UDDIException {
      try {
         String var1 = "/weblogic/auddi/uddi/resources/";
         String var2 = "listISO369Languages.xml";
         String var3 = var1 + var2;
         InputStream var4 = StandardLanguages.class.getResourceAsStream(var3);
         Logger.debug("ins : " + var4);
         String var5 = Util.getStreamContent(var4);
         Document var6 = ParserWrapper.parseRequest(var5, false);
         NodeList var7 = var6.getElementsByTagName("lang");
         this.m_mapByCode = new HashMap();
         this.m_mapByName = new HashMap();
         ArrayList var8 = new ArrayList();
         Logger.debug("nodes : " + var7.getLength());

         for(int var9 = 0; var9 < var7.getLength(); ++var9) {
            Node var10 = var7.item(var9);
            NamedNodeMap var11 = var10.getAttributes();
            String var12 = ((Attr)var11.item(0)).getValue().toLowerCase();
            String var13 = ((Attr)var11.item(1)).getValue();
            String var14 = ((Attr)var11.item(2)).getValue();
            Language var15 = new Language(var12, var13, var14);
            this.m_mapByCode.put(var12, var15);
            this.m_mapByName.put(var13, var15);
            var8.add(var15);
         }

         this.m_list = (Language[])((Language[])var8.toArray(new Language[0]));
      } catch (IOException var16) {
         throw new FatalErrorException(UDDIMessages.get("error.loading.initial.data", var16.getMessage()), var16);
      } catch (SchemaException var17) {
         throw new FatalErrorException(UDDIMessages.get("error.loading.initial.data", var17.getMessage()), var17);
      }
   }

   public Language getByCode(String var1) {
      Language var2 = (Language)this.m_mapByCode.get(var1.toLowerCase());
      if (var2 == null) {
         var2 = (Language)this.m_mapByCode.get(var1.toUpperCase());
      }

      return var2;
   }

   public Language getByName(String var1) {
      Language var2 = (Language)this.m_mapByName.get(var1);
      if (var2 == null) {
         var2 = (Language)this.m_mapByName.get(var1.toLowerCase());
      }

      if (var2 == null) {
         var2 = (Language)this.m_mapByName.get(var1.toUpperCase());
      }

      return var2;
   }

   public Language[] getLangs() {
      return this.m_list;
   }

   public static StandardLanguages getInstance() throws UDDIException {
      if (s_instance == null) {
         Class var0 = StandardLanguages.class;
         synchronized(StandardLanguages.class) {
            if (s_instance == null) {
               s_instance = new StandardLanguages();
            }
         }
      }

      return s_instance;
   }

   public static void main(String[] var0) throws Exception {
      Language[] var1 = getInstance().getLangs();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         System.out.println(var1[var2].getLang() + " " + var1[var2].getName() + " " + var1[var2].getFamily());
      }

   }
}
