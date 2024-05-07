package weblogic.iiop;

import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.csi.SASServiceContext;
import weblogic.kernel.Kernel;

public final class ServiceContextList {
   private static final int BASE_LIST_SIZE = 4;
   private static final int VI_FORWARD_OFFSET = 9;
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private static final ServiceContext NULL_CONTEXT = new ServiceContext(-1);
   private long omgBitmask = 0L;
   private long beaBitmask = 0L;
   private int beaElems = 0;
   private int omgElems = 0;
   private int foreignElems = 0;
   private int size = 0;
   private ServiceContext[] beaContexts = new ServiceContext[4];
   private ServiceContext[] omgContexts = new ServiceContext[4];
   private ServiceContext[] foreignContexts;

   public void write(IIOPOutputStream var1) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("writing " + this.size + " ServiceContexts");
      }

      var1.write_ulong(this.size);

      int var2;
      for(var2 = 0; var2 < this.omgElems; ++var2) {
         if (this.omgContexts[var2] != NULL_CONTEXT) {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("write(" + this.omgContexts[var2].toString() + ")");
            }

            this.omgContexts[var2].write(var1);
         }
      }

      for(var2 = 0; var2 < this.beaElems; ++var2) {
         if (this.beaContexts[var2] != NULL_CONTEXT) {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("write(" + this.beaContexts[var2].toString() + ")");
            }

            this.beaContexts[var2].write(var1);
         }
      }

      for(var2 = 0; var2 < this.foreignElems; ++var2) {
         if (this.foreignContexts[var2] != NULL_CONTEXT) {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("write(" + this.foreignContexts[var2].toString() + ")");
            }

            this.foreignContexts[var2].write(var1);
         }
      }

   }

   protected final void read(IIOPInputStream var1) {
      int var2 = var1.read_ulong();
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("reading " + var2 + " ServiceContexts");
      }

      for(int var3 = 0; var3 < var2; ++var3) {
         ServiceContext var4 = ServiceContext.readServiceContext(var1);
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("reading " + var4.toString());
         }

         this.addServiceContext(var4);
      }

   }

   public final ServiceContext getServiceContext(int var1) {
      if (this.size == 0) {
         return null;
      } else {
         int var2;
         if (var1 < 64) {
            if (this.omgElems == 0 || (this.omgBitmask & (long)(1 << var1)) == 0L) {
               return null;
            }

            for(var2 = 0; var2 < this.omgElems; ++var2) {
               if (this.omgContexts[var2].context_id == var1) {
                  return this.omgContexts[var2];
               }
            }
         } else if (var1 > 1111834880 && var1 < 1111834944) {
            if (this.beaElems == 0 || (this.beaBitmask & (long)(1 << var1 - 1111834880)) == 0L) {
               return null;
            }

            for(var2 = 0; var2 < this.beaElems; ++var2) {
               if (this.beaContexts[var2].context_id == var1) {
                  return this.beaContexts[var2];
               }
            }
         } else {
            if (this.foreignElems == 0) {
               return null;
            }

            for(var2 = 0; var2 < this.foreignElems; ++var2) {
               if (this.foreignContexts[var2].context_id == var1) {
                  return this.foreignContexts[var2];
               }
            }
         }

         return null;
      }
   }

   public final void removeServiceContext(int var1) {
      int var2;
      if (var1 < 64) {
         if (this.omgElems == 0 || (this.omgBitmask & (long)(1 << var1)) == 0L) {
            return;
         }

         for(var2 = 0; var2 < this.omgElems; ++var2) {
            if (this.omgContexts[var2].context_id == var1) {
               this.omgContexts[var2] = NULL_CONTEXT;
               --this.size;
            }
         }
      } else if (var1 >= 1111834880 && var1 < 1111834944) {
         if (this.beaElems == 0 || (this.beaBitmask & (long)(1 << var1 - 1111834880)) == 0L) {
            return;
         }

         for(var2 = 0; var2 < this.beaElems; ++var2) {
            if (this.beaContexts[var2].context_id == var1) {
               this.beaContexts[var2] = NULL_CONTEXT;
               --this.size;
            }
         }
      } else {
         if (this.foreignElems == 0) {
            return;
         }

         for(var2 = 0; var2 < this.foreignElems; ++var2) {
            if (this.foreignContexts[var2].context_id == var1) {
               this.foreignContexts[var2] = NULL_CONTEXT;
               --this.size;
            }
         }
      }

   }

   public final void addServiceContext(ServiceContext var1) {
      int var2 = var1.context_id;
      ++this.size;
      if (var2 < 64) {
         if (this.omgElems == this.omgContexts.length) {
            this.omgContexts = this.growList(this.omgContexts, this.omgElems);
         }

         this.omgBitmask |= (long)(1 << var2);
         this.omgContexts[this.omgElems++] = var1;
      } else if (var2 > 1111834880 && var2 < 1111834944) {
         if (this.beaElems == this.beaContexts.length) {
            this.beaContexts = this.growList(this.beaContexts, this.beaElems);
         }

         this.beaBitmask |= (long)(1 << var2 - 1111834880);
         this.beaContexts[this.beaElems++] = var1;
      } else {
         if (this.foreignContexts == null) {
            this.foreignContexts = new ServiceContext[4];
         } else if (this.foreignElems == this.foreignContexts.length) {
            this.foreignContexts = this.growList(this.foreignContexts, this.foreignElems);
         }

         this.foreignContexts[this.foreignElems++] = var1;
      }

   }

   public final ServiceContextList generateOutboundContexts() {
      ServiceContextList var1 = new ServiceContextList();
      if ((this.beaBitmask & 512L) != 0L) {
         var1.beaContexts[var1.beaElems++] = this.getServiceContext(1111834889);
         ++var1.size;
      }

      if ((this.omgBitmask & 32768L) != 0L) {
         SASServiceContext var2 = (SASServiceContext)this.getServiceContext(15);
         if (var2.getMsgType() == 0) {
            var1.addServiceContext(var2.getCompleteEstablishContext());
         }
      }

      return var1;
   }

   private final ServiceContext[] growList(ServiceContext[] var1, int var2) {
      ServiceContext[] var3 = new ServiceContext[var1.length * 2];
      System.arraycopy(var1, 0, var3, 0, var2);
      return var3;
   }

   protected static void p(String var0) {
      System.err.println("<ServiceContextList> " + var0);
   }

   public final void reset() {
      this.beaElems = 0;
      this.omgElems = 0;
      this.foreignElems = 0;
      this.size = 0;
      this.omgBitmask = 0L;
      this.beaBitmask = 0L;
   }

   public String toString() {
      return "ServiceContextList: " + this.size + " elements";
   }
}
