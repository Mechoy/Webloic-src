package weblogic.time.t3client.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.time.t3client.ScheduledTrigger;

public final class TimeMsg implements Externalizable {
   private static final long serialVersionUID = 5819526650654284539L;
   public static final byte CMD_TRIGGER_SCHEDULE_T = 1;
   public static final byte CMD_TRIGGER_SCHEDULE_F = 2;
   public static final byte CMD_TRIGGER_CANCEL = 3;
   public static final byte CMD_TRIGGER_SET_DAEMON_T = 4;
   public static final byte CMD_TRIGGER_SET_DAEMON_F = 5;
   public static final byte CMD_CLOCK_PING = 6;
   public static final boolean verbose = false;
   static final String[] cmd_names = new String[]{"UNKNOWN", "Schedule Daemon Trigger", "Schedule Trigger", "Cancel Trigger", "Set Daemon True", "Set Daemon False", "Clock Ping"};
   public static final String PROXYCLASS = "weblogic.time.t3client.internal.TimeProxy";
   protected byte cmd;
   protected ScheduledTrigger sch;
   protected int key;
   protected byte tindex;
   protected long t1 = 0L;
   protected long t2 = 0L;
   protected long t3 = 0L;
   protected long t4 = 0L;

   public byte cmd() {
      return this.cmd;
   }

   public String toString() {
      String var1 = "(unknown)";
      switch (this.cmd) {
         case 1:
         case 2:
            var1 = " sch = " + this.sch.toString();
            break;
         case 3:
            var1 = " key = " + this.key;
         case 4:
         case 5:
         default:
            break;
         case 6:
            var1 = " ts  = \n\tt1=" + (this.t1 != 0L ? (new Date(this.t1)).toString() : "-") + "/" + this.t1 + "\n\tt2=" + (this.t2 != 0L ? (new Date(this.t2)).toString() : "-") + "/" + this.t2 + "\n\tt3=" + (this.t3 != 0L ? (new Date(this.t3)).toString() : "-") + "/" + this.t3 + "\n\tt4=" + (this.t4 != 0L ? (new Date(this.t4)).toString() : "-") + "/" + this.t4;
      }

      return "TimeMsg: " + cmd_names[this.cmd] + var1;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      WLObjectInput var2 = (WLObjectInput)var1;
      this.cmd = var2.readByte();
      switch (this.cmd) {
         case 1:
         case 2:
            this.sch = (ScheduledTrigger)var2.readObjectWL();
            break;
         case 3:
            this.key = var2.readInt();
            break;
         case 4:
            this.key = var2.readInt();
            break;
         case 5:
            this.key = var2.readInt();
            break;
         case 6:
            long var3 = System.currentTimeMillis();
            this.tindex = var2.readByte();
            switch (this.tindex) {
               case 4:
                  this.t4 = var2.readLong();
               case 3:
                  this.t3 = var2.readLong();
               case 2:
                  this.t2 = var2.readLong();
               case 1:
                  this.t1 = var2.readLong();
               default:
                  ++this.tindex;
                  switch (this.tindex) {
                     case 1:
                        this.t1 = var3;
                        break;
                     case 2:
                        this.t2 = var3;
                        break;
                     case 3:
                        this.t3 = var3;
                        break;
                     case 4:
                        this.t4 = var3;
                  }
            }
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      WLObjectOutput var2 = (WLObjectOutput)var1;
      var2.writeByte(this.cmd);
      switch (this.cmd) {
         case 1:
         case 2:
            var2.writeObjectWL(this.sch);
            break;
         case 3:
            var2.writeInt(this.key);
            break;
         case 4:
            var2.writeInt(this.key);
            break;
         case 5:
            var2.writeInt(this.key);
            break;
         case 6:
            long var3 = System.currentTimeMillis();
            ++this.tindex;
            switch (this.tindex) {
               case 1:
                  this.t1 = var3;
                  break;
               case 2:
                  this.t2 = var3;
                  break;
               case 3:
                  this.t3 = var3;
                  break;
               case 4:
                  this.t4 = var3;
            }

            var2.writeByte(this.tindex);
            switch (this.tindex) {
               case 4:
                  var2.writeLong(this.t4);
               case 3:
                  var2.writeLong(this.t3);
               case 2:
                  var2.writeLong(this.t2);
               case 1:
                  var2.writeLong(this.t1);
            }
      }

   }

   public TimeMsg doSchedule(ScheduledTrigger var1, boolean var2) {
      this.cmd = (byte)(var2 ? 1 : 2);
      this.sch = var1;
      return this;
   }

   public TimeMsg doSetDaemon(boolean var1, int var2) {
      this.cmd = (byte)(var1 ? 4 : 5);
      this.key = var2;
      return this;
   }

   public TimeMsg doCancel(int var1) {
      this.cmd = 3;
      this.key = var1;
      return this;
   }

   public TimeMsg doPing() {
      this.cmd = 6;
      return this;
   }
}
