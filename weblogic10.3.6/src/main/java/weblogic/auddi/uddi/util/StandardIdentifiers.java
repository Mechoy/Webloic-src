package weblogic.auddi.uddi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.uddi.datastructure.TModel;
import weblogic.auddi.uddi.datastructure.TModelKey;
import weblogic.auddi.uddi.datastructure.TModels;
import weblogic.auddi.uddi.response.TModelDetailResponse;
import weblogic.auddi.uddi.soap.UDDISOAPWrapper;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.Util;

public class StandardIdentifiers extends StandardTModels {
   private static StandardIdentifiers s_instance = null;
   private static TModels s_tmodels = null;

   private StandardIdentifiers() throws UDDIException {
      try {
         String var1 = "stdIDS.xml";
         String var2 = "/weblogic/auddi/uddi/resources/";
         String var3 = var2 + var1;
         InputStream var4 = StandardTModels.class.getResourceAsStream(var3);
         String var5 = Util.getStreamContent(var4);
         Logger.Log(3, (String)var5);
         TModelDetailResponse var6 = (TModelDetailResponse)UDDISOAPWrapper.createResponseFromSOAP(var5);
         s_tmodels = var6.getTModels();
         this.m_items = new HashMap();

         for(TModel var7 = s_tmodels.getFirst(); var7 != null; var7 = s_tmodels.getNext()) {
            TModelKey var8 = var7.getTModelKey();
            if (var8 != null) {
               this.m_items.put(var8.getKey(), var7);
            }
         }

      } catch (IOException var9) {
         throw new FatalErrorException(UDDIMessages.get("error.loading.initial.data", var9.getMessage()), var9);
      }
   }

   public boolean isKeyFound(String var1) {
      for(TModel var2 = s_tmodels.getFirst(); var2 != null; var2 = s_tmodels.getNext()) {
         TModelKey var3 = var2.getTModelKey();
         if (var3.toString().toLowerCase().equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public static StandardIdentifiers getInstance() throws UDDIException {
      if (s_instance == null) {
         Class var0 = StandardIdentifiers.class;
         synchronized(StandardIdentifiers.class) {
            if (s_instance == null) {
               s_instance = new StandardIdentifiers();
            }
         }
      }

      return s_instance;
   }

   public static void main(String[] var0) throws Exception {
      Iterator var1 = getInstance().iterator();

      while(var1.hasNext()) {
         TModel var2 = (TModel)var1.next();
         System.out.println(var2.getTModelKey() + ", " + var2.getName());
      }

   }
}
