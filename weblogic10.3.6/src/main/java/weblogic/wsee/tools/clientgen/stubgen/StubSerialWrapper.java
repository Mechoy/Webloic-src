package weblogic.wsee.tools.clientgen.stubgen;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;

public class StubSerialWrapper implements Serializable {
   private String className;
   private String methodName;
   private Map properties;

   public StubSerialWrapper(String var1, String var2, Map var3) {
      this.className = var1 + "_Impl";
      this.methodName = "get" + var2;
      this.properties = var3;
      this.dealMimeHeaders(true);
   }

   private ArrayList convertMimeHeadersToSerializableFormat(Object var1) {
      if (var1 != null && !(var1 instanceof Serializable) && var1 instanceof MimeHeaders) {
         MimeHeaders var2 = (MimeHeaders)var1;
         ArrayList var3 = new ArrayList();
         Iterator var4 = var2.getAllHeaders();
         String[] var5 = null;
         MimeHeader var6 = null;

         while(var4.hasNext()) {
            var6 = (MimeHeader)var4.next();
            var5 = new String[]{var6.getName(), var6.getValue()};
            var3.add(var5);
         }

         return var3;
      } else {
         return null;
      }
   }

   private MimeHeaders restoreMimeHeadersFromSerializableFormat(Object var1) {
      if (var1 != null && var1 instanceof ArrayList) {
         ArrayList var2 = (ArrayList)var1;
         MimeHeaders var3 = new MimeHeaders();

         for(int var4 = 0; var4 < var2.size(); ++var4) {
            String[] var5 = (String[])((String[])var2.get(var4));
            var3.addHeader(var5[0], var5[1]);
         }

         return var3;
      } else {
         return null;
      }
   }

   private void dealMimeHeaders(boolean var1) {
      if (this.properties != null) {
         Object var2 = this.properties.get("weblogic.wsee.transport.headers");
         Object var3 = null;
         if (var1) {
            var3 = this.convertMimeHeadersToSerializableFormat(var2);
         } else {
            var3 = this.restoreMimeHeadersFromSerializableFormat(var2);
         }

         if (var3 != null) {
            this.properties.put("weblogic.wsee.transport.headers", var3);
         }

         Map var4 = (Map)this.properties.get("weblogic.wsee.invoke_properties");
         if (var4 != null) {
            var2 = var4.get("weblogic.wsee.transport.headers");
            if (var1) {
               var3 = this.convertMimeHeadersToSerializableFormat(var2);
            } else {
               var3 = this.restoreMimeHeadersFromSerializableFormat(var2);
            }

            if (var3 != null) {
               var4.put("weblogic.wsee.transport.headers", var3);
            }
         }

      }
   }

   private Object readResolve() throws ObjectStreamException {
      try {
         Class var1 = Thread.currentThread().getContextClassLoader().loadClass(this.className);
         Constructor var2 = var1.getConstructor();
         Object var3 = var2.newInstance();
         Method var4 = var1.getMethod(this.methodName);
         weblogic.wsee.jaxrpc.StubImpl var5 = (weblogic.wsee.jaxrpc.StubImpl)var4.invoke(var3);
         this.dealMimeHeaders(false);
         var5._setUserProperties(this.properties);
         return var5;
      } catch (Exception var6) {
         if (var6 instanceof InvocationTargetException) {
            ((InvocationTargetException)var6).getTargetException().printStackTrace();
         } else {
            var6.printStackTrace();
         }

         throw new StreamCorruptedException(var6.toString());
      }
   }
}
