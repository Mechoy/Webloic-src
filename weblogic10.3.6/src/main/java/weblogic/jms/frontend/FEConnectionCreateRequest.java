package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.DispatcherWrapper;

public final class FEConnectionCreateRequest implements Externalizable {
   static final long serialVersionUID = 8136687183971929785L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int XA_MASK = 256;
   private static final int HAS_USER_MASK = 512;
   private static final int HAS_PW_MASK = 1024;
   private DispatcherWrapper wrapper;
   private String userName;
   private String password;
   private boolean createXAConnection;

   public FEConnectionCreateRequest() {
   }

   public FEConnectionCreateRequest(DispatcherWrapper var1, String var2, String var3, boolean var4) {
      this.wrapper = var1;
      this.userName = var2;
      this.password = var3;
      this.createXAConnection = var4;
   }

   public DispatcherWrapper getDispatcherWrapper() {
      return this.wrapper;
   }

   public String getUserName() {
      return this.userName;
   }

   public String getPassword() {
      return this.password;
   }

   public boolean getCreateXAConnection() {
      return this.createXAConnection;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 1;
      if (this.createXAConnection) {
         var2 |= 256;
      }

      if (this.userName != null) {
         var2 |= 512;
      }

      if (this.password != null) {
         var2 |= 1024;
      }

      var1.writeInt(var2);
      this.wrapper.writeExternal(var1);
      if (this.userName != null) {
         var1.writeUTF(this.userName);
      }

      if (this.password != null) {
         var1.writeUTF(this.password);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         this.wrapper = new DispatcherWrapper();
         this.wrapper.readExternal(var1);
         this.createXAConnection = (var2 & 256) != 0;
         if ((var2 & 512) != 0) {
            this.userName = var1.readUTF();
         }

         if ((var2 & 1024) != 0) {
            this.password = var1.readUTF();
         }

      }
   }
}
