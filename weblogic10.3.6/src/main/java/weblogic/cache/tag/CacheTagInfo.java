package weblogic.cache.tag;

import java.util.Vector;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import weblogic.cache.KeyEnumerator;

public class CacheTagInfo extends TagExtraInfo {
   public VariableInfo[] getVariableInfo(TagData var1) {
      Vector var2 = new Vector();
      String var3 = var1.getAttributeString("flush");
      if (var3 != null) {
         return new VariableInfo[0];
      } else {
         String var4 = var1.getAttributeString("key");
         String var6;
         VariableInfo var7;
         if (var4 != null) {
            KeyEnumerator var5 = new KeyEnumerator(var4);

            while(var5.hasMoreKeys()) {
               var6 = var5.getNextKey();
               var7 = new VariableInfo(var6, "java.lang.Object", true, 0);
               var2.addElement(var7);
            }
         }

         String var9 = var1.getAttributeString("vars");
         if (var9 != null) {
            KeyEnumerator var10 = new KeyEnumerator(var9);

            while(var10.hasMoreKeys()) {
               String var11 = var10.getNextKey();
               VariableInfo var8 = new VariableInfo(var11, "java.lang.Object", true, 2);
               var2.addElement(var8);
            }
         }

         var6 = var1.getAttributeString("name");
         if (var6 != null) {
            var7 = new VariableInfo(var6, "weblogic.cache.CacheValue", true, 1);
            var2.addElement(var7);
         }

         VariableInfo[] var12 = new VariableInfo[var2.size()];
         var2.toArray(var12);
         return var12;
      }
   }

   public boolean isValid(TagData var1) {
      String var2 = var1.getAttributeString("key");
      if (var2 != null) {
         KeyEnumerator var3 = new KeyEnumerator(var2);

         while(var3.hasMoreKeys()) {
            var3.getNextKey();
         }
      }

      String var5 = var1.getAttributeString("vars");
      if (var5 != null) {
         KeyEnumerator var4 = new KeyEnumerator(var5);

         while(var4.hasMoreKeys()) {
            var4.getNextKey();
         }
      }

      String var6 = var1.getAttributeString("scope");
      if (var6 == null) {
         return true;
      } else {
         return var6.equals("parameter") || var6.equals("request") || var6.equals("session") || var6.equals("application") || var6.equals("file") || var6.equals("cluster");
      }
   }
}
