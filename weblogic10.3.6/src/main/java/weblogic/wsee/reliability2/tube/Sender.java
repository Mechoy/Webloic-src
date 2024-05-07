package weblogic.wsee.reliability2.tube;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.api.message.Message;
import java.io.Closeable;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Response;
import javax.xml.ws.soap.SOAPFaultException;
import weblogic.wsee.WseeRmLogger;

public class Sender {
   private static final Logger LOGGER = Logger.getLogger(Sender.class.getName());
   @NotNull
   private DispatchFactory _dispatchFactory;

   public Sender(@NotNull DispatchFactory var1) {
      this._dispatchFactory = var1;
   }

   public void send(@NotNull Message var1, @Nullable String var2, @NotNull WSEndpointReference var3, @Nullable WSEndpointReference var4) throws Exception {
      this.send(var1, var2, var3, var4, (Map)null);
   }

   public void send(@NotNull Message var1, @Nullable String var2, @NotNull WSEndpointReference var3, @Nullable WSEndpointReference var4, @NotNull Map<String, Object> var5) throws Exception {
      this.send(var1, var2, var3, var4, var5, false);
   }

   public void send(@NotNull Message var1, @Nullable String var2, @NotNull WSEndpointReference var3, @Nullable WSEndpointReference var4, @NotNull Map<String, Object> var5, boolean var6) throws Exception {
      this.send(var1, var2, var3, var4, var5, var6, false);
   }

   public void send(@NotNull Message var1, @Nullable String var2, @NotNull WSEndpointReference var3, @Nullable WSEndpointReference var4, @NotNull Map<String, Object> var5, boolean var6, boolean var7) throws Exception {
      this.send(var1, var2, var3, var4, var5, var6, var7, (SendFailureCallback)null);
   }

   public void send(@NotNull Message var1, @Nullable String var2, @NotNull WSEndpointReference var3, @Nullable WSEndpointReference var4, @NotNull Map<String, Object> var5, boolean var6, boolean var7, SendFailureCallback var8) throws Exception {
      if (var5 == null) {
         throw new IllegalArgumentException("invokeProps cannot be null");
      } else if (var4 == null) {
         throw new IllegalArgumentException("replyTo cannot be null");
      } else {
         Dispatch var9 = this._dispatchFactory.createDispatch(var3, Message.class);
         if (this._dispatchFactory instanceof WsrmServerDispatchFactory) {
            var1.getHeaders().addOrReplace(Headers.create(var3.getVersion().toTag, var3.getAddress()));
         }

         AddressingVersion var10 = var3.getVersion();
         SOAPVersion var11 = ((WSBinding)var9.getBinding()).getSOAPVersion();

         try {
            setInvokePropertiesOntoDispatch(var5, var9);
            if (var2 == null) {
               var2 = var1.getHeaders().getAction(var10, var11);
            }

            var9.getRequestContext().put("javax.xml.ws.soap.http.soapaction.uri", var2);
            if (!var4.isAnonymous()) {
               Header var12 = var4.createHeader(var4.getVersion().replyToTag);
               var1.getHeaders().addOrReplace(var12);
            }

            var3.addReferenceParameters(var1.getHeaders());
            if (var4.isAnonymous()) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Sender doing SYNC send of message with action: " + var2 + " on dispatch: " + var9);
               }

               Message var19;
               if (var7) {
                  var19 = null;
                  var9.invokeOneWay(var1);
               } else {
                  var19 = (Message)var9.invoke(var1);
               }

               this.handleResponse(var2, var10, var11, var19, var9);
            } else {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Sender doing *async* send of message with action: " + var2 + " on dispatch: " + var9);
               }

               if (var6 && !var7) {
                  ResponseHolder var21 = new ResponseHolder();
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Sender is blocking until we get the result of action '" + var2 + "' from service at address: " + var3.getAddress() + " on dispatch: " + var9);
                  }

                  synchronized(var21) {
                     var9.invokeAsync(var1, new ForceSyncAsyncHandler(var21));

                     while(true) {
                        if (var21.response != null) {
                           break;
                        }

                        try {
                           var21.wait();
                        } catch (Exception var16) {
                           WseeRmLogger.logUnexpectedException(var16.toString(), var16);
                        }
                     }
                  }

                  this.handleResponseMessage(var2, var10, var11, var21.response, var9, true);
               } else if (var7) {
                  var9.invokeOneWay(var1);
               } else {
                  InternalAsyncHandler var20 = new InternalAsyncHandler(var2, var10, var11, (Dispatch)null, var8);
                  var9.invokeAsync(var1, var20);
               }
            }

         } catch (Exception var18) {
            String var13 = "Error sending ";
            String var14 = var1.getHeaders().getMessageID(var10, var11);
            if (var14 != null) {
               var13 = var13 + "msgId '" + var14 + "' ";
            }

            var13 = var13 + "action '" + var2 + "' to '" + var3.getAddress() + "': " + var18.toString();
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.log(Level.FINE, var13, var18);
            }

            throw var18;
         }
      }
   }

   public static void setInvokePropertiesOntoDispatch(Map<String, Object> var0, Dispatch var1) {
      Iterator var2 = var0.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Object var4 = var0.get(var3);

         try {
            if (!"javax.xml.ws.soap.http.soapaction.uri".equals(var3) || var4 != null && ((String)var4).length() >= 1) {
               var1.getRequestContext().put(var3, var4);
            }
         } catch (Exception var6) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.log(Level.FINE, var6.toString(), var6);
            }
         }
      }

   }

   private void handleResponseMessage(String var1, AddressingVersion var2, SOAPVersion var3, Response<Message> var4, Dispatch<Message> var5, boolean var6) throws Exception {
      try {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Processing Response<Message> (in Sender) to action '" + var1 + "'. Will now attempt to extract the actual response.");
         }

         Message var7 = (Message)var4.get();
         this.handleResponse(var1, var2, var3, var7, var5);
      } catch (Exception var8) {
         if (var6) {
            throw var8;
         }

         if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, var8.toString(), var8);
         }

         WseeRmLogger.logUnexpectedException(var8.toString(), var8);
      }

   }

   private void handleResponse(String var1, AddressingVersion var2, SOAPVersion var3, Message var4, Dispatch<Message> var5) throws SOAPException {
      try {
         if (var4 != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Processing response (in Sender) to action '" + var1 + "'");
            }

            if (var4.isFault()) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Processing fault (in Sender) to action '" + var1 + "'");
               }

               SOAPMessage var6 = var4.readAsSOAPMessage();
               SOAPBody var7 = var6.getSOAPBody();
               SOAPFault var8 = var7.getFault();
               throw new SOAPFaultException(var8);
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Sender IGNORING non-fault response after async send of message with action: " + var1);
               LOGGER.fine("Sender IGNORING non-fault response with action: " + var4.getHeaders().getAction(var2, var3));
            }
         }
      } finally {
         this.closeDispatch(var5);
      }

   }

   private void closeDispatch(Dispatch<Message> var1) {
      if (var1 != null) {
         try {
            ((Closeable)var1).close();
         } catch (Exception var3) {
            if (LOGGER.isLoggable(Level.WARNING)) {
               LOGGER.log(Level.WARNING, var3.toString(), var3);
            }

            WseeRmLogger.logUnexpectedException(var3.toString(), var3);
         }
      }

   }

   private class InternalAsyncHandler implements AsyncHandler<Message> {
      private String useAction;
      private AddressingVersion av;
      private SOAPVersion sv;
      private Dispatch<Message> useDispatch;
      private SendFailureCallback _callback;

      public InternalAsyncHandler(String var2, AddressingVersion var3, SOAPVersion var4, Dispatch<Message> var5, SendFailureCallback var6) {
         this.useAction = var2;
         this.av = var3;
         this.sv = var4;
         this.sv = var4;
         this.useDispatch = var5;
         this._callback = var6;
      }

      public void handleResponse(Response<Message> var1) {
         try {
            if (Sender.LOGGER.isLoggable(Level.FINE)) {
               Sender.LOGGER.fine("Sender just received a response indicator for action '" + this.useAction + "' on dispatch: " + this.useDispatch);
            }

            Sender.this.handleResponseMessage(this.useAction, this.av, this.sv, var1, this.useDispatch, true);
         } catch (Exception var3) {
            if (this._callback != null) {
               this._callback.sendFailed(var3);
            }
         }

      }
   }

   private static class ForceSyncAsyncHandler implements AsyncHandler<Message> {
      private ResponseHolder _holder;

      public ForceSyncAsyncHandler(ResponseHolder var1) {
         this._holder = var1;
      }

      public void handleResponse(Response<Message> var1) {
         synchronized(this._holder) {
            this._holder.response = var1;
            this._holder.notify();
         }
      }
   }

   private static class ResponseHolder {
      Response<Message> response;

      private ResponseHolder() {
      }

      // $FF: synthetic method
      ResponseHolder(Object var1) {
         this();
      }
   }

   public interface SendFailureCallback {
      void sendFailed(Throwable var1);
   }
}
