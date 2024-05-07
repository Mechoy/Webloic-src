package weblogic.transaction.internal;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import weblogic.management.DomainDir;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreException;
import weblogic.transaction.TransactionLoggable;
import weblogic.utils.StackTraceUtils;

public final class TransactionLogUpgradeHelper {
   private final ArrayList abbrevTable = new ArrayList();
   private final List fileList = new LinkedList();

   public void attemptUpgrade(PersistentStore var1, String var2, String var3, String var4) throws IOException, PersistentStoreException {
      StoreTransactionLoggerImpl var5 = new StoreTransactionLoggerImpl(var1);
      File var6 = this.findOldTLOG(var2, var3, var4);
      if (var6 != null) {
         List var7 = this.recover();
         var5.store(var7);
         var6.delete();
         this.delete();
      }

   }

   public File findOldTLOG(String var1, String var2, String var3) throws IOException {
      var1 = var1 + var2;
      String var4 = this.addSeparator(var3);
      String var5 = getRelativePrefixRelativeServerDir(var2);
      if (var5.length() > 0 && !var5.endsWith("/") && !var5.endsWith(File.separatorChar + "")) {
         var5 = var5 + File.separatorChar;
      }

      if (TxDebug.JTATLOG.isDebugEnabled()) {
         TxDebug.JTATLOG.debug("Looking for TLOG: DefaultLogPath=\"" + var5 + "\", RootDir=\"" + var4 + "\"");
      }

      if (this.isRelativePath(var1)) {
         try {
            return this.readHeader(var5 + var1 + ".");
         } catch (FileNotFoundException var11) {
            try {
               return this.readHeader(var4 + var5 + var1 + ".");
            } catch (FileNotFoundException var10) {
               try {
                  return this.readHeader(var4 + var1 + ".");
               } catch (FileNotFoundException var9) {
                  try {
                     return this.readHeader(var1);
                  } catch (FileNotFoundException var8) {
                  }
               }
            }
         }
      } else {
         try {
            return this.readHeader(var1 + ".");
         } catch (FileNotFoundException var12) {
            try {
               return this.readHeader(var1);
            } catch (FileNotFoundException var7) {
            }
         }
      }

      return null;
   }

   private File readHeader(String var1) throws IOException {
      String var2 = var1 + "0000.tlog";
      String var3 = var1 + "0000.tmp";
      if (TxDebug.JTATLOG.isDebugEnabled()) {
         TxDebug.JTATLOG.debug("TLOG header: reading, fname=" + var2);
      }

      File var4 = new File(var2);
      if (!var4.exists()) {
         var4 = new File(var3);
         if (!var4.exists()) {
            if (TxDebug.JTATLOG.isDebugEnabled()) {
               TxDebug.JTATLOG.debug("TLOG header: file not found, headerFileName=" + var2);
            }

            throw new FileNotFoundException(var2);
         }

         TXLogger.logTLOGRecoveredBackupHeader(var3);
      }

      byte[] var5 = this.readFile(var4);
      if (var5.length == 0) {
         return var4;
      } else {
         UpgradeLogDataInputImpl var6 = new UpgradeLogDataInputImpl(new LogByteArrayInputStream(var5));
         int var7 = var6.readNonNegativeInt();
         if (TxDebug.JTATLOG.isDebugEnabled()) {
            TxDebug.JTATLOG.debug("TLOG header: read version=" + var7);
         }

         if (var7 != 0) {
            TXLogger.logTLOGUnrecognizedHeaderVersionNumber();
            return var4;
         } else {
            int var8 = var6.readNonNegativeInt();
            if (TxDebug.JTATLOG.isDebugEnabled()) {
               TxDebug.JTATLOG.debug("TLOG header: read nextFileID=" + var8);
            }

            int var9 = var6.readNonNegativeInt();
            if (TxDebug.JTATLOG.isDebugEnabled()) {
               TxDebug.JTATLOG.debug("TLOG header: read file cnt=" + var9);
            }

            int var10;
            for(var10 = 0; var10 < var9; ++var10) {
               int var11 = var6.readNonNegativeInt();
               if (TxDebug.JTATLOG.isDebugEnabled()) {
                  TxDebug.JTATLOG.debug("TLOG header: read active file id[" + var10 + "]=" + var11);
               }

               this.fileList.add(this.makeName(var1, var11));
            }

            var9 = var6.readNonNegativeInt();
            if (TxDebug.JTATLOG.isDebugEnabled()) {
               TxDebug.JTATLOG.debug("TLOG header: read abbreviation cnt=" + var9);
            }

            for(var10 = 0; var10 < var9; ++var10) {
               String var12 = var6.readString();
               if (TxDebug.JTATLOG.isDebugEnabled()) {
                  TxDebug.JTATLOG.debug("TLOG header: read abbreviation[" + var10 + "]=" + var12);
               }

               this.abbrevTable.add(var12);
            }

            return var4;
         }
      }
   }

   List recover() throws IOException {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.fileList.iterator();

      while(var2.hasNext()) {
         this.recoverFile(var1, (String)var2.next());
      }

      return var1;
   }

   void delete() throws IOException {
      Iterator var1 = this.fileList.iterator();

      while(var1.hasNext()) {
         File var2 = new File((String)var1.next());
         var2.delete();
      }

   }

   private void recoverFile(List var1, String var2) throws IOException {
      if (TxDebug.JTATLOG.isDebugEnabled()) {
         TxDebug.JTATLOG.debug("TLOG file: recovering, fname=" + var2);
      }

      File var3 = new File(var2);
      if (!var3.exists()) {
         TXLogger.logTLOGMissing(var2);
      } else {
         byte[] var4 = this.readFile(var3);
         UpgradeLogDataInputImpl var5 = new UpgradeLogDataInputImpl(new LogByteArrayInputStream(var4));

         while(var5.available() > 0) {
            int var6 = 0;
            int var7 = -1;

            try {
               var6 = var5.readNonNegativeInt();
               var7 = var5.getPos();
            } catch (Exception var11) {
               TXLogger.logTLOGFileReadFormatException(100, var2, var11);
            }

            if (var6 <= 0 || var7 < 0) {
               TXLogger.logTLOGFileReadFormatError(200, var2);
               break;
            }

            var5.skip(var6);

            try {
               TransactionLoggable var8 = this.readLogRecord(var4, var7, var6);
               if (var8 != null) {
                  if (var8 instanceof ResourceCheckpoint) {
                     ((ResourceCheckpoint)var8).convertPre810JTSName();
                  }

                  if (var8 instanceof ServerTransactionImpl) {
                     ((ServerTransactionImpl)var8).convertPre810JTSName();
                  }

                  var1.add(var8);
               } else {
                  TXLogger.logTLOGFileReadFormatError(300, var2);
               }
            } catch (Exception var10) {
               TXLogger.logTLOGFileReadFormatException(400, var2, var10);
            }
         }

      }
   }

   private TransactionLoggable readLogRecord(byte[] var1, int var2, int var3) {
      if (var2 + var3 <= var1.length && var3 >= 5) {
         try {
            CRC32 var5 = new CRC32();
            var5.update(var1, var2, var3 - 4);
            long var6 = var5.getValue();
            long var8 = ((long)var1[var2 + var3 - 4] & 255L) << 24;
            var8 += ((long)var1[var2 + var3 - 3] & 255L) << 16;
            var8 += ((long)var1[var2 + var3 - 2] & 255L) << 8;
            var8 += (long)var1[var2 + var3 - 1] & 255L;
            if (var6 != var8) {
               TXLogger.logTLOGRecordChecksumMismatch(1);
               if (TxDebug.JTATLOG.isDebugEnabled()) {
                  TxDebug.JTATLOG.debug("Mismatch!  checksum=" + var6 + ", verify=" + var8);
               }

               return null;
            }
         } catch (Exception var13) {
            TXLogger.logTLOGRecordChecksumException(2, var13);
            return null;
         }

         UpgradeLogDataInputImpl var4;
         try {
            var4 = new UpgradeLogDataInputImpl(new LogByteArrayInputStream(var1, var2));
         } catch (IOException var12) {
            return null;
         }

         String var14 = var4.readAbbrevString();

         TransactionLoggable var15;
         try {
            var15 = (TransactionLoggable)Class.forName(var14).newInstance();
         } catch (Exception var11) {
            TXLogger.logTLOGRecordClassInstantiationException(var14, var11);
            return null;
         }

         try {
            var15.readExternal(var4);
         } catch (Throwable var10) {
            TXLogger.logTLOGReadExternalException(var14, var10);
            return null;
         }

         if (TxDebug.JTATLOG.isDebugEnabled()) {
            TxDebug.JTATLOG.debug("TLOG read log record, obj=" + var15);
         }

         return var15;
      } else {
         TXLogger.logTLOGReadChecksumError();
         return null;
      }
   }

   private byte[] readFile(File var1) {
      byte[] var2 = new byte[0];
      FileInputStream var3 = null;

      try {
         var2 = new byte[(int)var1.length()];
         var3 = new FileInputStream(var1);
         int var5 = 0;

         int var4;
         do {
            var4 = var3.read(var2, var5, var2.length - var5);
            if (var4 > 0) {
               var5 += var4;
            }
         } while(var4 > 0);
      } catch (Exception var14) {
         TXLogger.logTLOGFileReadError(var1.getAbsolutePath(), var14);
      } finally {
         try {
            if (var3 != null) {
               var3.close();
            }
         } catch (Exception var13) {
            if (TxDebug.JTATLOG.isDebugEnabled()) {
               TxDebug.JTATLOG.debug("TLOG header: close exception, fname=" + var1.getAbsolutePath(), var13);
            }
         }

      }

      return var2;
   }

   private String makeName(String var1, int var2) {
      String[] var3 = new String[]{"000", "00", "0", ""};
      String var4 = Integer.toString(var2);
      return var1 + var3[var4.length() - 1] + var4 + ".tlog";
   }

   private String addSeparator(String var1) {
      if (var1 == null) {
         return "";
      } else {
         return var1.length() > 0 && !var1.endsWith("/") && !var1.endsWith(File.separatorChar + "") ? var1 + File.separatorChar : var1;
      }
   }

   private boolean isRelativePath(String var1) {
      if (var1 != null && var1.length() != 0) {
         File var2 = new File(var1);
         if (var2.isAbsolute()) {
            return false;
         } else {
            return !var2.toString().startsWith(File.separatorChar + "");
         }
      } else {
         return true;
      }
   }

   private static String getRelativePrefixRelativeServerDir(String var0) {
      return DomainDir.getRootDir() + File.separator + var0;
   }

   private final class UpgradeLogDataInputImpl extends DataInputStream implements LogDataInput {
      private final LogByteArrayInputStream inStream;

      public UpgradeLogDataInputImpl(LogByteArrayInputStream var2) throws IOException {
         super(var2);
         this.inStream = var2;
      }

      public int getPos() {
         return this.inStream.getPos();
      }

      public void skip(int var1) {
         this.inStream.skip((long)var1);
      }

      private byte readByteIgnore() {
         try {
            return super.readByte();
         } catch (IOException var2) {
            return 0;
         }
      }

      private int readIntIgnore() {
         try {
            return super.readInt();
         } catch (IOException var2) {
            return 0;
         }
      }

      public int readNonNegativeInt() {
         int var1 = 0;

         byte var2;
         do {
            var2 = this.readByteIgnore();
            var1 = var1 << 7 | var2 & 127;
         } while((var2 & 128) != 0);

         return var1;
      }

      public String readString() {
         int var1 = this.readNonNegativeInt();
         if (var1 > this.inStream.available()) {
            throw new RuntimeException("transaction log decoder:  String(" + var1 + ") too big");
         } else {
            StringBuffer var2 = new StringBuffer(var1);

            for(int var3 = 0; var3 < var1; ++var3) {
               var2.append((char)this.readNonNegativeInt());
            }

            return var2.toString();
         }
      }

      public String readAbbrevString() {
         int var1 = this.readNonNegativeInt();
         if (var1 == 0) {
            int var4 = this.readNonNegativeInt();
            return var4 == 0 ? "" : (String)TransactionLogUpgradeHelper.this.abbrevTable.get(var4 - 1);
         } else if (var1 > this.inStream.available()) {
            throw new RuntimeException("transaction log decoder:  String(" + var1 + ") too big");
         } else {
            StringBuffer var2 = new StringBuffer(var1);

            for(int var3 = 0; var3 < var1; ++var3) {
               var2.append((char)this.readNonNegativeInt());
            }

            return var2.toString();
         }
      }

      public byte[] readByteArray() {
         int var1 = this.readNonNegativeInt();
         if (var1 <= 0) {
            return null;
         } else if (var1 > this.inStream.available()) {
            throw new RuntimeException("transaction log decoder:  String(" + var1 + ") too big");
         } else {
            byte[] var2 = new byte[var1];

            for(int var3 = 0; var3 < var1; ++var3) {
               var2[var3] = this.readByteIgnore();
            }

            return var2;
         }
      }

      public Map readProperties() throws IOException {
         HashMap var1 = new HashMap(5);
         int var3 = this.readNonNegativeInt();

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = this.readAbbrevString();
            if (var5 == null || var5.equals("")) {
               throw new InvalidObjectException("transaction log record: missing property name");
            }

            int var6 = this.readNonNegativeInt();
            XidImpl var2;
            switch (var6) {
               case 1:
                  String var7 = this.readString();
                  var1.put(var5, var7);
                  break;
               case 2:
                  String var8 = this.readString();
                  var1.put(var5, new Integer(var8));
                  break;
               case 3:
                  var2 = XidImpl.create(this.readNonNegativeInt(), this.readByteArray(), this.readByteArray());
                  var1.put(var5, var2);
                  break;
               case 4:
                  var2 = XidImpl.create(this.readIntIgnore(), this.readByteArray(), this.readByteArray());
                  var1.put(var5, var2);
                  break;
               case 5:
                  var1.put(var5, new Integer(this.readIntIgnore()));
                  break;
               case 6:
                  var1.put(var5, this.readObject());
                  break;
               default:
                  throw new InvalidObjectException("transaction log record: bad property type " + var6 + " for property:" + var5);
            }
         }

         return var1;
      }

      private Object readObject() throws IOException {
         ObjectInputStream var1 = new ObjectInputStream(this.inStream);

         try {
            return var1.readObject();
         } catch (ClassNotFoundException var3) {
            throw new IOException(StackTraceUtils.throwable2StackTrace(var3));
         }
      }
   }

   private final class LogByteArrayInputStream extends ByteArrayInputStream {
      LogByteArrayInputStream(byte[] var2) {
         super(var2);
      }

      LogByteArrayInputStream(byte[] var2, int var3) {
         super(var2, var3, var2.length - var3);
      }

      int getPos() {
         return this.pos;
      }
   }
}
