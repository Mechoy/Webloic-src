package weblogic.wsee.mc.tube;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Response;
import javax.xml.ws.soap.SOAPFaultException;

public class McSender {
   private static final Logger LOGGER = Logger.getLogger(McSender.class.getName());
   @NotNull
   private McDispatchFactory _dispatchFactory;

   public McSender(@NotNull McDispatchFactory var1) {
      this._dispatchFactory = var1;
   }

   public void send(@NotNull Message var1, @Nullable String var2, @NotNull WSEndpointReference var3, @Nullable WSEndpointReference var4, @Nullable WSEndpointReference var5) throws Exception {
      this.send(var1, var2, var3, var4, var5, new HashMap());
   }

   public void send(@NotNull Message var1, @Nullable String var2, @NotNull WSEndpointReference var3, @Nullable WSEndpointReference var4, @Nullable WSEndpointReference var5, @NotNull Map<String, Object> var6) throws Exception {
      this.send(var1, var2, var3, var4, var5, var6, false);
   }

   public void send(@NotNull Message var1, @Nullable String var2, @NotNull WSEndpointReference var3, @Nullable WSEndpointReference var4, @Nullable WSEndpointReference var5, @NotNull Map<String, Object> var6, boolean var7) throws Exception {
      if (var4 == null) {
         var4 = var3.getVersion().anonymousEpr;
      }

      if (var5 == null) {
         var5 = var3.getVersion().anonymousEpr;
      }

      Dispatch var8 = this._dispatchFactory.createDispatch(var3, Message.class);
      var8.getRequestContext().put("javax.xml.ws.soap.http.soapaction.uri", var2);
      var8.getRequestContext().putAll(var6);
      Header var9 = var4.createHeader(var4.getVersion().replyToTag);
      var1.getHeaders().addOrReplace(var9);
      var9 = var5.createHeader(var5.getVersion().faultToTag);
      var1.getHeaders().addOrReplace(var9);
      var3.addReferenceParameters(var1.getHeaders());
      Message var10;
      if (var4.isAnonymous()) {
         var10 = (Message)var8.invoke(var1);
      } else {
         var10 = null;
         Response var11 = var8.invokeAsync(var1);
         if (var7) {
            while(var11.getContext() == null) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("McSender is polling for result of action '" + var2 + "' from service at address: " + var3.getAddress());
               }

               try {
                  Thread.sleep(2000L);
               } catch (Exception var14) {
                  var14.printStackTrace();
               }
            }

            var10 = (Message)var11.get();
         }
      }

      if (var10 != null && var10.isFault()) {
         SOAPMessage var15 = var10.readAsSOAPMessage();
         SOAPBody var12 = var15.getSOAPBody();
         SOAPFault var13 = var12.getFault();
         throw new SOAPFaultException(var13);
      }
   }
}
