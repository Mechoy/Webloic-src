package weblogic.auddi.uddi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.Util;
import weblogic.auddi.xml.SchemaException;

public abstract class StandardTModels {
   protected Map m_items = new HashMap();

   protected StandardTModels() {
   }

   public Map getItems() {
      return this.m_items;
   }

   public int getItemCount() {
      return this.m_items.size();
   }

   public Iterator iterator() {
      return this.m_items.values().iterator();
   }

   protected void loadFile(String var1) throws UDDIException {
      Logger.debug("loading standard tmodels from : " + var1);
      InputStream var2 = null;

      try {
         var2 = StandardTModels.class.getResourceAsStream(var1);
         String var3 = Util.getStreamContent(var2);
         XMLToTree var4 = new XMLToTree(var3);
         String var5 = var4.getKey();
         Logger.debug("Will put this TModel in Standard map: " + var5.toLowerCase());
         this.m_items.put(var5.toLowerCase(), var4);
      } catch (IOException var14) {
         throw new FatalErrorException(UDDIMessages.get("error.loading.initial.data", var14.getMessage()), var14);
      } catch (SchemaException var15) {
         throw new FatalErrorException(UDDIMessages.get("error.loading.initial.data", var15.getMessage()), var15);
      } finally {
         try {
            if (var2 != null) {
               var2.close();
            }
         } catch (IOException var13) {
         }

      }

   }

   public XMLToTree getItemByKey(String var1) {
      return var1 == null ? null : (XMLToTree)this.m_items.get(var1.toLowerCase());
   }

   public boolean isKeyFound(String var1) {
      XMLToTree var2 = this.getItemByKey(var1);
      return var2 != null;
   }

   public boolean isKeyValueFound(String var1, String var2) {
      Logger.debug("Will seek to validate " + var2 + " in " + var1);
      XMLToTree var3 = this.getItemByKey(var1);
      if (var3 == null) {
         Logger.debug("XMLToTree was null");
         return false;
      } else {
         return var3.hasValue(var2);
      }
   }
}
