package weblogic.wsee.handler;

import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.monitoring.HandlerStats;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public final class HandlerIterator {
   public static final String HANDLER_INDEX = "weblogic.wsee.handler.index";
   private HandlerListImpl handlers;
   private int index = 0;
   private static final int CONSTRUCTED = 0;
   private static final int IN_HANDLE_REQUEST = 1;
   private static final int IN_HANDLE_RESPONSE = 2;
   private int status = 0;
   private boolean suspended = false;
   private boolean closureEnabled = false;
   private static final boolean verbose = Verbose.isVerbose(HandlerIterator.class);
   private static final boolean verboseHistory = Verbose.isVerbose(HandlerIterator.class.getName() + "History");
   private static final String HANDLER_HISTORY = "weblogic.wsee.handler.HandlerHistory";

   public HandlerIterator(HandlerList var1) {
      this.handlers = (HandlerListImpl)var1;
   }

   public HandlerListImpl getHandlers() {
      return this.handlers;
   }

   public void setIndex(int var1) {
      this.index = var1;
   }

   public void suspend() {
      this.suspended = true;
   }

   public void resume(MessageContext var1) {
      this.suspended = false;
      if (this.status == 2) {
         this.handleResponse(var1);
      } else {
         this.handleRequest(var1, this.index);
      }

   }

   public boolean handleRequest(MessageContext var1) {
      return this.handleRequest(var1, 0);
   }

   public boolean handleRequest(MessageContext var1, int var2) {
      this.closureEnabled = false;
      this.status = 1;
      WlMessageContext var3 = WlMessageContext.narrow(var1);
      if (verboseHistory) {
         updateHandlerHistory("...REQUEST...", var3);
      }

      for(this.index = var2; this.index < this.handlers.size(); ++this.index) {
         Handler var4 = this.handlers.get(this.index);
         if (verbose) {
            Verbose.log((Object)("Processing " + var4.getClass().getSimpleName() + "...  "));
         }

         if (verboseHistory) {
            updateHandlerHistory(var4.getClass().getSimpleName(), var3);
         }

         HandlerStats var5 = this.handlers.getStats(this.index);

         try {
            var3.setProperty("weblogic.wsee.handler.index", new Integer(this.index));
            String var6;
            if (!var4.handleRequest(var3)) {
               if (verboseHistory) {
                  var6 = var4.getClass().getSimpleName() + ".handleRequest=false";
                  updateHandlerHistory(var6, var3);
               }

               if (var5 != null) {
                  var5.reportRequestTermination();
               }

               return false;
            }

            if (var3.hasFault()) {
               if (verbose) {
                  Verbose.log((Object)"A fault in the context");
               }

               if (verboseHistory) {
                  var6 = var4.getClass().getSimpleName() + ".hasFault=true";
                  updateHandlerHistory(var6, var3);
               }

               return false;
            }
         } catch (Throwable var8) {
            if (verbose) {
               Verbose.log("Failed to invoke handler", var8);
            }

            if (verboseHistory) {
               String var7 = var4.getClass().getSimpleName() + ".throwable";
               updateHandlerHistory(var7, var3);
            }

            if (var5 != null) {
               var5.reportRequestError(var8);
            }

            var3.setFault(var8);
            return false;
         }

         if (this.suspended) {
            return true;
         }
      }

      --this.index;
      return true;
   }

   public boolean handleClosure(MessageContext var1) {
      this.closureEnabled = true;
      return this.handleResponse(var1);
   }

   public boolean handleAsyncClosure(MessageContext var1) {
      this.closureEnabled = true;
      if (this.handlers.size() == 0) {
         return true;
      } else {
         this.index = this.handlers.size() - 1;
         return this.handleResponse(var1);
      }
   }

   public boolean handleAsyncResponse(MessageContext var1) {
      if (this.handlers.size() == 0) {
         return true;
      } else {
         this.index = this.handlers.size() - 1;
         return this.handleResponse(var1);
      }
   }

   public boolean handleResponse(MessageContext var1) {
      assert this.index >= 0;

      assert this.index < this.handlers.size();

      this.status = 2;
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      if (verboseHistory) {
         updateHandlerHistory("...RESPONSE...", var2);
      }

      while(this.index >= 0 && this.index < this.handlers.size()) {
         Handler var3 = this.handlers.get(this.index);
         HandlerStats var4 = this.handlers.getStats(this.index);
         if (verbose) {
            Verbose.log((Object)("Processing " + var3.getClass().getSimpleName() + "...  "));
         }

         if (this.closureEnabled) {
            if (var3 instanceof WLHandler) {
               this.handleCloser(var3, var1, var4);
            }
         } else if (var2.hasFault()) {
            this.handleFault(var3, var1, var4);
         } else {
            this.handleResponse(var3, var4, var2);
         }

         if (this.suspended) {
            return true;
         }

         --this.index;
      }

      return true;
   }

   private void handleResponse(Handler var1, HandlerStats var2, WlMessageContext var3) {
      String var5;
      try {
         boolean var4 = var1.handleResponse(var3);
         if (verboseHistory) {
            var5 = var1.getClass().getSimpleName() + ".handleResponse" + (var4 ? "" : "=false");
            updateHandlerHistory(var5, var3);
         }

         if (!var4 && !var3.hasFault()) {
            this.closureEnabled = true;
         }
      } catch (Throwable var6) {
         if (verboseHistory) {
            var5 = var1.getClass().getSimpleName() + ".handleResponse=Throwable";
            updateHandlerHistory(var5, var3);
         }

         this.setFaultOnContext(var6, var2, var3);
      }

   }

   private void handleFault(Handler var1, MessageContext var2, HandlerStats var3) {
      try {
         String var4;
         if (!var1.handleFault(var2)) {
            if (verboseHistory) {
               var4 = var1.getClass().getSimpleName() + ".handleFault=false";
               updateHandlerHistory(var4, var2);
            }

            this.closureEnabled = true;
         } else if (verboseHistory) {
            var4 = var1.getClass().getSimpleName() + ".handleFault";
            updateHandlerHistory(var4, var2);
         }
      } catch (Throwable var7) {
         WlMessageContext var5 = WlMessageContext.narrow(var2);
         if (verboseHistory) {
            String var6 = var1.getClass().getSimpleName() + ".handleFault=Throwable";
            updateHandlerHistory(var6, var2);
         }

         if (var5.getFault() == null) {
            this.setFaultOnContext(var7, var3, var5);
         } else {
            this.ignoreFault(var7, var3);
         }
      }

   }

   private void handleCloser(Handler var1, MessageContext var2, HandlerStats var3) {
      try {
         ((WLHandler)var1).handleClosure(var2);
         if (verboseHistory) {
            String var4 = var1.getClass().getSimpleName() + ".handleClosure";
            updateHandlerHistory(var4, var2);
         }
      } catch (Throwable var6) {
         if (verboseHistory) {
            String var5 = var1.getClass().getSimpleName() + ".handleClosure=Throwable";
            updateHandlerHistory(var5, var2);
         }

         this.ignoreFault(var6, var3);
      }

   }

   private void setFaultOnContext(Throwable var1, HandlerStats var2, WlMessageContext var3) {
      if (verbose) {
         Verbose.log("Failed to invoke handle response", var1);
      }

      if (var2 != null) {
         var2.reportResponseError(var1);
      }

      var3.setFault(var1);
   }

   private void ignoreFault(Throwable var1, HandlerStats var2) {
      if (verbose) {
         Verbose.log("Failed to invoke handle response", var1);
      }

      if (var2 != null) {
         var2.reportResponseError(var1);
      }

      assert false : "handleFault had fault:" + var1;

   }

   public static void updateHandlerHistory(String var0, MessageContext var1) {
      StringBuffer var2 = getHandlerHistory(var1);
      if (var2.length() > 0) {
         var2.append(",");
      }

      var2.append(var0);
      var1.setProperty("weblogic.wsee.handler.HandlerHistory", var2.toString());
   }

   public static StringBuffer getHandlerHistory(MessageContext var0) {
      String var1 = (String)var0.getProperty("weblogic.wsee.handler.HandlerHistory");
      if (var1 == null) {
         var0.setProperty("weblogic.wsee.handler.HandlerHistory", "");
         var1 = "";
      }

      StringBuffer var2 = new StringBuffer(var1);
      return var2;
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("handlers", this.handlers);
      var1.end();
   }
}
