package weblogic.common;

import weblogic.io.common.IOServicesDef;
import weblogic.io.common.internal.IOServicesImpl;
import weblogic.jdbc.common.JdbcServicesDef;
import weblogic.time.common.TimeServicesDef;
import weblogic.time.t3client.internal.TimeServicesImpl;
import weblogic.utils.NestedError;

final class T3ClientServices implements T3ServicesDef {
   T3Client t3;
   private AdminServicesDef adminSvc;
   private LogServicesDef logSvc;
   private NameServicesDef nameSvc;
   private IOServicesDef ioSvc;
   private TimeServicesDef timeSvc;

   public T3ClientServices(T3Client var1) {
      this.t3 = var1;
   }

   public AdminServicesDef admin() {
      return this.adminSvc != null ? this.adminSvc : (this.adminSvc = new AdminServicesImpl(this.t3));
   }

   public JdbcServicesDef jdbc() {
      return null;
   }

   public LogServicesDef log() {
      return this.logSvc != null ? this.logSvc : (this.logSvc = new LogServicesImpl(this.t3));
   }

   public IOServicesDef io() {
      try {
         return this.ioSvc = new IOServicesImpl(this, this.t3);
      } catch (T3Exception var2) {
         throw new NestedError("Failed to instantiate IOServicesImpl: ", var2);
      }
   }

   public TimeServicesDef time() {
      return this.timeSvc != null ? this.timeSvc : (this.timeSvc = new TimeServicesImpl(this, this.t3));
   }

   public NameServicesDef name() {
      if (this.nameSvc == null) {
         this.nameSvc = new NameServicesImpl(this.t3);
      }

      return this.nameSvc;
   }
}
