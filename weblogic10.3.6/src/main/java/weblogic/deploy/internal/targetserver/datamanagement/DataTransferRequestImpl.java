package weblogic.deploy.internal.targetserver.datamanagement;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import weblogic.deploy.service.DataTransferRequest;

public abstract class DataTransferRequestImpl implements DataTransferRequest {
   private static final long serialVersionUID = -886329126108801508L;
   private long requestId;
   private List filePaths;
   private String lockPath;

   protected DataTransferRequestImpl() {
   }

   public DataTransferRequestImpl(long var1, List var3, String var4) {
      this.requestId = var1;
      this.filePaths = var3;
      this.lockPath = var4;
   }

   public long getRequestId() {
      return this.requestId;
   }

   public List getFilePaths() {
      return this.filePaths;
   }

   public String getLockPath() {
      return this.lockPath;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeLong(this.requestId);
      var1.writeObject(this.filePaths);
      var1.writeObject(this.lockPath);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.requestId = var1.readLong();
      this.filePaths = (List)var1.readObject();
      this.lockPath = (String)var1.readObject();
   }
}
