package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyPushMessageListRequest extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private ProxyPushMessageRequest first;
   private ProxyPushMessageRequest last;
   private int pipelineSize;
   private int size;

   public ProxyPushMessageListRequest() {
   }

   public ProxyPushMessageListRequest(int var1) {
      this.pipelineSize = var1;
   }

   public synchronized int size() {
      return this.size;
   }

   public synchronized void add(ProxyPushMessageRequest var1) {
      ++this.size;
      var1.setNext((ProxyPushMessageRequest)null);
      if (this.first == null) {
         this.first = var1;
         this.last = var1;
      } else {
         this.last.setNext(var1);
         this.last = var1;
      }
   }

   public int getMarshalTypeCode() {
      return 51;
   }

   public synchronized void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      this.versionFlags.marshal(var1);
      var1.writeInt(this.pipelineSize);
      var1.writeInt(this.size);

      for(ProxyPushMessageRequest var2 = this.first; var2 != null; var2 = var2.getNext()) {
         var2.marshal(var1);
      }

   }

   public synchronized void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.pipelineSize = var1.readInt();
      int var2 = var1.readInt();

      while(var2-- > 0) {
         ProxyPushMessageRequest var3 = new ProxyPushMessageRequest();
         var3.unmarshal(var1);
         this.add(var3);
      }

   }
}
