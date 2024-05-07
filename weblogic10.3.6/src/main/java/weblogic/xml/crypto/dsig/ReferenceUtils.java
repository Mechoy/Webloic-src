package weblogic.xml.crypto.dsig;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.NodeSetData;
import weblogic.xml.crypto.api.URIReferenceException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.utils.DataUtils;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.crypto.wss.STRTransform;

public class ReferenceUtils {
   public static Data dereference(Reference var0, XMLCryptoContext var1) throws URIReferenceException {
      final Data var2 = var1.getURIDereferencer().dereference(var0, var1);
      LogUtils.logDsig(new LogUtils.LogMethod() {
         public String log() {
            return "dereferenced data: " + DataUtils.toString(var2);
         }
      });
      return var2;
   }

   public static Data applyTransforms(final Data var0, List var1, XMLCryptoContext var2) throws XMLSignatureException {
      if (var0 instanceof NodeSetData && var1.isEmpty()) {
         addC14NTransform(var1);
      }

      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         WLTransform var4 = (WLTransform)var3.next();
         LogUtils.logDsig("applying transform: " + var4.getAlgorithm());
         var0 = var4.transform(var0, var2);
         LogUtils.logDsig(new LogUtils.LogMethod() {
            public String log() {
               return "transformed data: " + DataUtils.toString(var0);
            }
         });
      }

      return var0;
   }

   public static List getAppliedTransforms(List var0) {
      ArrayList var1 = new ArrayList();
      TransformImpl var2 = null;
      Iterator var3 = var0.iterator();

      while(true) {
         while(var3.hasNext()) {
            TransformImpl var4 = (TransformImpl)var3.next();
            if (var2 instanceof STRTransform && var4 instanceof ExclDOMC14NTransform) {
               LogUtils.logDsig(new LogUtils.LogMethod() {
                  public String log() {
                     return "exc-c14n transformer is removed after STR transformer";
                  }
               });
            } else {
               if (var2 instanceof NodeTransform && var4 instanceof OctetTransform && !(var4 instanceof DOMC14NTransform) && !(var4 instanceof STRTransform)) {
                  addC14NTransform(var1);
               }

               var1.add(var4);
               var2 = var4;
            }
         }

         if (var2 != null && var2 instanceof NodeTransform) {
            addC14NTransform(var1);
         }

         return var1;
      }
   }

   public static void addC14NTransform(List var0) {
      try {
         var0.add(TransformImpl.newTransform("http://www.w3.org/2001/10/xml-exc-c14n#"));
      } catch (NoSuchAlgorithmException var2) {
      } catch (InvalidAlgorithmParameterException var3) {
      }

   }

   public static List getTransforms(List var0) {
      if (var0 == null || ((List)var0).isEmpty()) {
         var0 = new ArrayList();
         addC14NTransform((List)var0);
      }

      return (List)var0;
   }
}
