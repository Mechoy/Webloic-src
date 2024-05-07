package weblogic.wsee.async;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.store.ObjectHandler;
import weblogic.wsee.Version;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.server.EncryptionUtil;
import weblogic.wsee.util.Verbose;

public class AsyncInvokeStateObjectHandler implements ObjectHandler {
   private static final boolean verbose = Verbose.isVerbose(AsyncInvokeStateObjectHandler.class);
   private static HashSet<String> excludedProperties = new HashSet(Arrays.asList("weblogic.wsee.wsrm.sequence.expiration", "weblogic.wsee.wsrm.offer.sequence.expiration", "weblogic.wsee.seq.expires", "weblogic.wsee.handler.jaxrpcHandlerChain"));
   public static final String USER_DEFINED_EXCLUDED_PROPERTIES = "weblogic.wsee.exclude.properties";

   private static void init() {
      String var0 = System.getProperty("weblogic.wsee.exclude.properties");
      if (var0 != null) {
         StringTokenizer var1 = new StringTokenizer(var0, ",");

         while(var1.hasMoreTokens()) {
            String var2 = var1.nextToken().trim();
            excludedProperties.add(var2);
         }

      }
   }

   public void writeObject(ObjectOutput var1, Object var2) throws IOException {
      var1.writeUTF("10.3.6");
      if (var2 instanceof String) {
         var1.writeInt(0);
         var1.writeUTF((String)var2);
      } else if (var2 instanceof AsyncInvokeState) {
         var1.writeInt(1);
         AsyncInvokeState var3 = (AsyncInvokeState)var2;
         AsyncPostCallContextImpl var4 = var3.getAsyncPostCallContext();
         var1.writeLong(var4.getAbsTimeout());
         HashMap var5 = var4.getProperties();
         int var6 = 0;
         Iterator var7 = var5.keySet().iterator();

         String var8;
         Object var9;
         while(var7.hasNext()) {
            var8 = (String)var7.next();
            if (!excludedProperties.contains(var8)) {
               var9 = var5.get(var8);
               ++var6;
               if (var9 != null && !(var9 instanceof Serializable)) {
                  throw new IOException("Properties set in AsyncPreCallContext have to be serializable in order to enable reliable async request/response, violation key: " + var8);
               }
            }
         }

         var1.writeInt(var6);
         var7 = var5.keySet().iterator();

         while(var7.hasNext()) {
            var8 = (String)var7.next();
            if (!excludedProperties.contains(var8)) {
               var9 = var5.get(var8);
               if (verbose) {
                  if (var9 != null) {
                     Verbose.log((Object)("Writing " + var8 + " " + var9.toString()));
                  } else {
                     Verbose.log((Object)("Writing " + var8 + " " + null));
                  }
               }

               var1.writeUTF(var8);
               if (var9 != null) {
                  var1.writeInt(1);
                  var1.writeObject(var9);
               } else {
                  var1.writeInt(0);
               }
            }
         }

         var6 = 0;
         var7 = var3.getMessageContext().getPropertyNames();

         while(var7.hasNext()) {
            var8 = (String)var7.next();
            if (!excludedProperties.contains(var8)) {
               var9 = var3.getMessageContext().getProperty(var8);
               ++var6;
               if (var9 != null && !(var9 instanceof Serializable)) {
                  throw new IOException("Properties set in the MessageContext have to be serializable in order to enable reliable async request/response, violation key: " + var8);
               }
            }
         }

         var1.writeInt(var6);
         var7 = var3.getMessageContext().getPropertyNames();

         while(var7.hasNext()) {
            var8 = (String)var7.next();
            if (!excludedProperties.contains(var8)) {
               var9 = var3.getMessageContext().getProperty(var8);
               if (var8.equals("javax.xml.rpc.security.auth.password")) {
                  var1.writeUTF(var8);

                  assert var9 != null;

                  var1.writeUTF(new String(EncryptionUtil.encrypt(((String)var9).getBytes())));
               } else {
                  if (verbose) {
                     if (var9 != null) {
                        Verbose.log((Object)("Writing " + var8 + " " + var9.toString()));
                     } else {
                        Verbose.log((Object)("Writing " + var8 + " " + null));
                     }
                  }

                  var1.writeUTF(var8);
                  if (var9 != null) {
                     var1.writeInt(1);
                     var1.writeObject(var9);
                  } else {
                     var1.writeInt(0);
                  }
               }
            }
         }

         if (var3.getSubject() != null) {
            var1.writeInt(1);
            var1.writeObject(var3.getSubject());
         } else {
            var1.writeInt(0);
         }

         var1.writeObject(var3.getState());
         var1.writeObject(var3.getMessageId());
      }
   }

   public Object readObject(ObjectInput var1) throws ClassNotFoundException, IOException {
      String var2 = var1.readUTF();
      Version.checkForKnownVersion(var2);
      int var3 = var1.readInt();
      if (var3 == 0) {
         return var1.readUTF();
      } else {
         AsyncInvokeState var4 = new AsyncInvokeState();
         AsyncPostCallContextImpl var5 = (AsyncPostCallContextImpl)AsyncCallContextFactory.getAsyncPostCallContext();
         long var6 = var1.readLong();
         var5.setAbsTimeout(var6);
         int var8 = var1.readInt();
         if (verbose) {
            Verbose.log((Object)("size is " + var8));
         }

         String var10;
         Object var12;
         for(int var9 = 0; var9 < var8; ++var9) {
            var10 = var1.readUTF();
            int var11 = var1.readInt();
            var12 = null;
            if (var11 == 1) {
               var12 = var1.readObject();
            }

            if (verbose) {
               Verbose.log((Object)("Reading key: " + var10));
            }

            var5.setProperty(var10, var12);
         }

         var4.setAsyncPostCallContext(var5);
         var8 = var1.readInt();
         if (verbose) {
            Verbose.log((Object)("size is " + var8));
         }

         SoapMessageContext var14 = new SoapMessageContext();

         for(int var15 = 0; var15 < var8; ++var15) {
            String var17 = var1.readUTF();
            var12 = null;
            if (var17.equals("javax.xml.rpc.security.auth.password")) {
               var12 = new String(EncryptionUtil.decrypt(var1.readUTF().getBytes()));
            } else {
               int var13 = var1.readInt();
               if (var13 == 1) {
                  var12 = var1.readObject();
               }
            }

            if (verbose) {
               Verbose.log((Object)("Reading key: " + var17));
            }

            var14.setProperty(var17, var12);
         }

         var4.setMessageContext(var14);
         var8 = var1.readInt();
         if (var8 > 0) {
            var4.setSubject((AuthenticatedSubject)var1.readObject());
         }

         if (Version.isLaterThanOrEqualTo(var2, "10.0")) {
            AsyncInvokeState.STATE var16 = (AsyncInvokeState.STATE)var1.readObject();
            var4.setState(var16);
         }

         if (Version.isLaterThanOrEqualTo(var2, "10.3.6")) {
            var10 = (String)var1.readObject();
            var4.setMessageId(var10);
         }

         return var4;
      }
   }

   static {
      init();
   }
}
