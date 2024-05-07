package weblogic.jms.dotnet.transport.socketplugin;

class Stats implements Cloneable {
   long recvBytes;
   long recvPackets;
   long sendBytes;
   long sendPackets;
   long startTime = System.currentTimeMillis();
   long curTime;
   long nextTime;
   String text;

   Stats(String var1) {
      this.nextTime = this.startTime + 2000L;
      this.text = "";
      this.text = var1;
   }

   boolean timeToReport() {
      this.curTime = System.currentTimeMillis();
      if (this.curTime < this.nextTime) {
         return false;
      } else {
         this.nextTime += 2000L;
         return true;
      }
   }

   synchronized boolean _incRecv(long var1) {
      ++this.recvPackets;
      this.recvBytes += var1;
      return this.timeToReport();
   }

   synchronized boolean _incSend(long var1) {
      ++this.sendPackets;
      this.sendBytes += var1;
      return this.timeToReport();
   }

   public Object clone() throws CloneNotSupportedException {
      synchronized(this) {
         Stats var2 = (Stats)super.clone();
         var2.curTime = System.currentTimeMillis();
         this.startTime = var2.curTime;
         this.recvBytes = this.recvPackets = this.sendBytes = this.sendPackets = 0L;
         return var2;
      }
   }

   void printMe() {
      try {
         System.out.println(this.clone());
      } catch (CloneNotSupportedException var2) {
         var2.printStackTrace();
      }

   }

   void incRecv(long var1) {
      if (this._incRecv(var1)) {
         this.printMe();
      }

   }

   void incSend(long var1) {
      if (this._incSend(var1)) {
         this.printMe();
      }

   }

   public String toString() {
      long var1 = this.curTime - this.startTime;
      double var3 = (double)var1 / 1000.0;
      return var3 == 0.0 ? "Div0" : "\nStats " + this.text + "\nsecs=" + var3 + " recv/sec=" + (double)this.recvPackets / var3 + "\nsecs=" + var3 + " recvB/sec=" + (double)this.recvBytes / var3 + "\nsecs=" + var3 + " send/sec=" + (double)this.sendPackets / var3 + "\nsecs=" + var3 + " sendB/sec=" + (double)this.sendBytes / var3 + "\n";
   }
}
