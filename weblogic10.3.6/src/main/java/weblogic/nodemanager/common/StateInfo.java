package weblogic.nodemanager.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;
import weblogic.management.runtime.ServerStates;
import weblogic.nodemanager.util.ConcurrentFile;

public class StateInfo implements ServerStates {
   private String state;
   private boolean started;
   private boolean failed;
   private boolean startupAborted;
   private byte[] buf = new byte[256];
   private static final String EOL;
   private static final String SEPARATOR = ":";
   public static final String ABORTED_STARTUP_SUFFIX = "_ON_ABORTED_STARTUP";

   public synchronized void setState(String var1) {
      this.state = var1;
      if (var1 != null && var1.endsWith("_ON_ABORTED_STARTUP")) {
         this.startupAborted = true;
      }

   }

   public synchronized void set(String var1, boolean var2, boolean var3) {
      this.state = var1;
      this.started = var2;
      this.failed = var3;
      if (var1 != null && var1.endsWith("_ON_ABORTED_STARTUP")) {
         this.startupAborted = true;
      }

   }

   public synchronized String getState() {
      return this.state != null && this.state.endsWith("_ON_ABORTED_STARTUP") ? this.state.substring(0, this.state.indexOf("_ON_ABORTED_STARTUP")) : this.state;
   }

   public synchronized void setStarted(boolean var1) {
      this.started = var1;
   }

   public synchronized boolean isStarted() {
      return this.started;
   }

   public synchronized boolean isStartupAborted() {
      return this.startupAborted;
   }

   public synchronized void setFailed(boolean var1) {
      this.failed = var1;
   }

   public synchronized boolean isFailed() {
      return this.failed;
   }

   public synchronized void load(ConcurrentFile var1) throws IOException {
      int var2 = var1.read(ByteBuffer.wrap(this.buf));
      String var3 = (new String(this.buf, 0, var2)).trim();
      String[] var4 = var3.split(":");
      if (var4.length != 3) {
         throw new IOException("Invalid state file format. State file contents: " + var3);
      } else {
         this.state = var4[0].toUpperCase(Locale.ENGLISH);
         this.started = var4[1].toUpperCase(Locale.ENGLISH).equals("Y");
         this.failed = var4[2].toUpperCase(Locale.ENGLISH).equals("Y");
         if (this.state.endsWith("_ON_ABORTED_STARTUP")) {
            this.startupAborted = true;
         }

      }
   }

   public synchronized void save(ConcurrentFile var1) throws IOException {
      StateInfoWriter.writeStateInfo(var1, this.state, this.started, this.failed);
   }

   public synchronized void reset() {
      this.state = null;
      this.started = false;
      this.failed = false;
   }

   public String toString() {
      return "[state=" + this.state + ",started=" + Boolean.toString(this.started) + ",failed=" + Boolean.toString(this.failed) + "]";
   }

   static {
      EOL = StateInfoWriter.EOL;
   }
}
