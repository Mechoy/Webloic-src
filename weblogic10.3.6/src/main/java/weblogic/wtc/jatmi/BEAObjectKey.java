package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.Tpconvert;
import com.bea.core.jatmi.common.ntrace;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public final class BEAObjectKey {
   private byte[] keyData;
   private int keyLength;
   private int requestId;
   private int msgType;
   private byte responseExpected;
   private int verMinor;
   private int verMajor;
   public static final int BEAOBJKEY_GIOP_HEADER_SIZE = 12;
   public static final int BEAOBJKEY_GIOP_MAX_VERSION = 1;
   public static final int BEAOBJKEY_GIOP_MAJOR_VERSION_1 = 1;
   public static final int BEAOBJKEY_GIOP_MINOR_VERSION_1 = 1;
   public static final int BEAOBJKEY_GIOP_REQUEST_MESSAGE = 0;
   public static final int BEAOBJKEY_GIOP_REPLY_MESSAGE = 1;
   public static final int BEAOBJKEY_GIOP_CANCELREQUEST_MESSAGE = 2;
   public static final int BEAOBJKEY_GIOP_LOCATE_MESSAGE = 3;
   public static final int BEAOBJKEY_GIOP_LOCATEREPLY_MESSAGE = 4;
   public static final int BEAOBJKEY_GIOP_CLOSECONNECTION_MESSAGE = 5;
   public static final int BEAOBJKEY_GIOP_MESSAGEERROR_MESSAGE = 6;
   public static final int BEAOBJKEY_GIOP_SYSTEM_EXCEPTION = 2;
   public static final int BEAOBJKEY_GIOP_LOC_SYSTEM_EXCEPTION = 4;
   public static final int BEAOBJKEY_GIOP_UNKNOWN_OBJECT = 0;
   public static final int BEAOBJKEY_GIOP_COMPLETED_MAYBE = 2;
   public static final int BEAOBJKEY_GIOP_KEYADDR = 0;
   public static final int BEAOBJKEY_GIOP_PROFILEADDR = 1;
   public static final int BEAOBJKEY_GIOP_REFERENCEADDR = 2;
   public static final int BEAOBJKEY_TAG_INTERNET_IOP = 0;
   public static final int BEAOBJKEY_ENCAPKEYHEADERSIZE = 8;
   public static final int BEAOBJKEY_KEY_MAJOR_VERSION = 1;
   public static final int BEAOBJKEY_KEY_MINOR_VERSION = 2;
   public static final int BEAOBJKEY_KEYTYPE_ICEBERG = 8;
   public static final int BEAOBJKEY_TPFW = 1;
   public static final int BEAOBJKEY_INTFREP = 2;
   public static final int BEAOBJKEY_FF = 3;
   public static final int BEAOBJKEY_ROOT = 4;
   public static final int BEAOBJKEY_NTS = 5;
   public static final int BEAOBJKEY_USER = 6;
   public static final int BEAOBJKEY_NAMESERVICE = 7;
   public static final int BEAOBJKEY_TAG_CLNT_ROUTE_INFO = 1111834884;
   public static final int BEAOBJKEY_TAG_OBJKEY_HASH = 1111834886;

   public BEAObjectKey(TypedTGIOP var1) throws TPException, IOException {
      boolean var5 = false;
      boolean var14 = ntrace.isTraceEnabled(4);
      if (var14) {
         ntrace.doTrace("[/BEAObjectKey/BEAObjectKey/0");
      }

      this.requestId = 0;
      int var15 = var1.send_size;
      if (var15 < 12) {
         if (var14) {
            ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/5");
         }

         throw new TPException(9);
      } else {
         int var16 = var15 + 12;
         DataInputStream var17 = new DataInputStream(new ByteArrayInputStream(var1.tgiop));
         byte[] var3 = new byte[4];
         var17.readFully(var3);
         String var2 = new String(var3);
         if (!var2.equals("GIOP")) {
            throw new TPException(9);
         } else {
            if (var14) {
               ntrace.doTrace("/BEAObjectKey/BEAObjectKey/10");
            }

            if ((this.verMajor = var17.readByte()) > 1) {
               if (var14) {
                  ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/15");
               }

               throw new TPException(12);
            } else {
               this.verMinor = var17.readByte();
               if (this.verMinor <= 1 && this.verMajor <= 1) {
                  var5 = true;
               }

               byte var13 = var17.readByte();
               if (var14) {
                  ntrace.doTrace("/BEAObjectKey/BEAObjectKey/17 byteorder = " + var13);
               }

               this.msgType = var17.readByte();
               if (this.msgType != 0 && this.msgType != 3) {
                  if (var14) {
                     ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/20/" + this.msgType);
                  }

                  throw new TPException(12);
               } else {
                  var17.readByte();
                  var17.readByte();
                  var17.readByte();
                  var17.readByte();
                  var16 -= 12;
                  if (var14) {
                     ntrace.doTrace("/BEAObjectKey/BEAObjectKey/25/" + this.msgType);
                  }

                  int var6;
                  int var8;
                  if (var5 && this.msgType == 0) {
                     var16 -= this.alignLong(var17, var15 - var16);
                     int var7 = TGIOPUtil.extractLong(var17, var13);

                     for(var16 -= 4; var7 > 0; --var7) {
                        var17.readInt();
                        var16 -= 4;
                        var16 -= this.alignLong(var17, var15 - var16);
                        var8 = TGIOPUtil.extractLong(var17, var13);
                        var16 -= 4;
                        if (var8 > var16) {
                           if (var14) {
                              ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/27");
                           }

                           throw new TPException(12);
                        }

                        for(var6 = 0; var6 < var8; ++var6) {
                           var17.readByte();
                           --var16;
                        }
                     }
                  }

                  if (var14) {
                     ntrace.doTrace("/BEAObjectKey/BEAObjectKey/30");
                  }

                  var16 -= this.alignLong(var17, var15 - var16);
                  this.requestId = TGIOPUtil.extractLong(var17, var13);
                  var16 -= 4;
                  if (this.msgType == 0) {
                     this.responseExpected = var17.readByte();
                     var17.readByte();
                     var17.readByte();
                     var17.readByte();
                     var16 -= 4;
                  } else {
                     this.responseExpected = 0;
                  }

                  if (var14) {
                     ntrace.doTrace("/BEAObjectKey/BEAObjectKey/31/" + this.requestId + "/" + this.responseExpected);
                  }

                  int var10000;
                  if (var5) {
                     var16 -= this.alignLong(var17, var15 - var16);
                     this.keyLength = TGIOPUtil.extractLong(var17, var13);
                     var16 -= 4;
                     if (this.keyLength > var16 + 4) {
                        if (var14) {
                           ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/35");
                        }

                        throw new TPException(12);
                     }

                     this.keyData = new byte[this.keyLength];

                     for(var6 = 0; var6 < this.keyLength; ++var6) {
                        this.keyData[var6] = var17.readByte();
                     }

                     var10000 = var16 - this.keyLength;
                  } else {
                     short var9 = TGIOPUtil.extractShort(var17, var13);
                     var16 -= 2;
                     switch (var9) {
                        case 0:
                           var16 -= this.alignLong(var17, var15 - var16);
                           this.keyLength = TGIOPUtil.extractLong(var17, var13);
                           var16 -= 4;
                           if (this.keyLength > var16 + 4) {
                              if (var14) {
                                 ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/40");
                              }

                              throw new TPException(12);
                           }

                           this.keyData = new byte[this.keyLength];

                           for(var6 = 0; var6 < this.keyLength; ++var6) {
                              this.keyData[var6] = var17.readByte();
                           }

                           var10000 = var16 - this.keyLength;
                           break;
                        case 2:
                           var16 -= this.alignLong(var17, var15 - var16);
                           int var10 = TGIOPUtil.extractLong(var17, var13);
                           var16 -= 4;
                           var16 -= this.alignLong(var17, var15 - var16);
                           var8 = TGIOPUtil.extractLong(var17, var13);
                           var16 -= 4;
                           if (var8 > var16) {
                              if (var14) {
                                 ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/45");
                              }

                              throw new TPException(12);
                           }

                           for(var6 = 0; var6 < var8; ++var6) {
                              var17.readByte();
                              --var16;
                           }

                           var16 -= this.alignLong(var17, var15 - var16);
                           int var11 = TGIOPUtil.extractLong(var17, var13);
                           var16 -= 4;

                           for(var6 = 1; var6 <= var11 && var6 != var10; ++var6) {
                              var17.readInt();
                              var16 -= 4;
                              var16 -= this.alignLong(var17, var15 - var16);
                              var8 = TGIOPUtil.extractLong(var17, var13);
                              var16 -= 4;
                              if (var8 > var16) {
                                 if (var14) {
                                    ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/50");
                                 }

                                 throw new TPException(12);
                              }
                           }

                           if (var6 != var10) {
                              if (var14) {
                                 ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/55");
                              }

                              throw new TPException(12);
                           }
                        case 1:
                           var16 -= this.alignLong(var17, var15 - var16);
                           int var12 = TGIOPUtil.extractLong(var17, var13);
                           var16 -= 4;
                           if (var12 != 0) {
                              if (var14) {
                                 ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/60");
                              }

                              throw new TPException(9);
                           }

                           var16 -= this.alignLong(var17, var15 - var16);
                           var8 = TGIOPUtil.extractLong(var17, var13);
                           var16 -= 4;
                           if (var8 > var16) {
                              if (var14) {
                                 ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/65");
                              }

                              throw new TPException(12);
                           }

                           var13 = var17.readByte();
                           --var16;
                           var17.readByte();
                           var17.readByte();
                           var16 -= 2;
                           var16 -= this.alignLong(var17, var15 - var16);
                           var8 = TGIOPUtil.extractLong(var17, var13);
                           var16 -= 4;
                           if (var8 > var16) {
                              if (var14) {
                                 ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/70");
                              }

                              throw new TPException(12);
                           }

                           for(var6 = 0; var6 < var8; ++var6) {
                              var17.readByte();
                              --var16;
                           }

                           var17.readShort();
                           var16 -= 2;
                           var16 -= this.alignLong(var17, var15 - var16);
                           this.keyLength = TGIOPUtil.extractLong(var17, var13);
                           var16 -= 4;
                           if (this.keyLength > var16 + 4) {
                              if (var14) {
                                 ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/75");
                              }

                              throw new TPException(12);
                           }

                           this.keyData = new byte[this.keyLength];

                           for(var6 = 0; var6 < this.keyLength; ++var6) {
                              this.keyData[var6] = var17.readByte();
                           }

                           var10000 = var16 - this.keyLength;
                           break;
                        default:
                           if (var14) {
                              ntrace.doTrace("*]/BEAObjectKey/BEAObjectKey/80");
                           }

                           throw new TPException(12);
                     }
                  }

                  if (var14) {
                     ntrace.doTrace("]/BEAObjectKey/BEAObjectKey/90");
                  }

               }
            }
         }
      }
   }

   public BEAObjectKey(byte[] var1) {
      this.keyLength = var1.length;
      this.keyData = new byte[this.keyLength];

      for(int var2 = 0; var2 < this.keyLength; ++var2) {
         this.keyData[var2] = var1[var2];
      }

   }

   public byte[] getBEAObjectKey() {
      return this.keyData;
   }

   public int getBEAObjectKeyLen() {
      return this.keyLength;
   }

   public int getRequestId() {
      return this.requestId;
   }

   public int getMsgType() {
      return this.msgType;
   }

   public int getMajorVersion() {
      return this.verMajor;
   }

   public int getMinorVersion() {
      return this.verMinor;
   }

   public byte getResponseExpected() {
      return this.responseExpected;
   }

   public void getInfo(Objinfo var1) throws TPException, IOException {
      boolean var10 = ntrace.isTraceEnabled(4);
      if (var10) {
         ntrace.doTrace("[/BEAObjectKey/getInfo/0");
      }

      if (this.keyLength < 8) {
         if (var10) {
            ntrace.doTrace("*]/BEAObjectKey/getInfo/10");
         }

         throw new TPException(9);
      } else {
         DataInputStream var11 = new DataInputStream(new ByteArrayInputStream(this.keyData));
         int var6 = this.keyLength + 8;
         int var12 = var6;
         byte var9 = var11.readByte();
         byte[] var3 = new byte[3];
         var11.readFully(var3);
         String var2 = new String(var3);
         if (!var2.equals("BEA")) {
            if (var10) {
               ntrace.doTrace("*]/BEAObjectKey/getInfo/20");
            }

            throw new TPException(9);
         } else {
            if (var10) {
               ntrace.doTrace("/BEAObjectKey/getInfo/30");
            }

            var1.setKeyType(var11.readByte());
            if (var1.getKeyType() != 8) {
               if (var10) {
                  ntrace.doTrace("*]/BEAObjectKey/getInfo/40");
               }

               throw new TPException(9);
            } else {
               byte var8;
               if ((var8 = var11.readByte()) > 1) {
                  if (var10) {
                     ntrace.doTrace("*]/BEAObjectKey/getInfo/45");
                  }

                  throw new TPException(12);
               } else {
                  byte var7 = var11.readByte();
                  var11.readByte();
                  var6 -= 8;
                  if (var10) {
                     ntrace.doTrace("/BEAObjectKey/getInfo/50");
                  }

                  var6 -= this.alignLong(var11, var12 - var6);
                  int var4 = TGIOPUtil.extractLong(var11, var9);
                  var6 -= 4;
                  if (var4 != 0 && var4 <= var6) {
                     var3 = new byte[var4];

                     int var5;
                     for(var5 = 0; var5 < var4; ++var5) {
                        var3[var5] = var11.readByte();
                     }

                     var6 -= var4;
                     var1.setDomainId(new String(var3, 0, var4 - 1));
                     var6 -= this.alignLong(var11, var12 - var6);
                     var4 = TGIOPUtil.extractLong(var11, var9);
                     var6 -= 4;
                     var1.setGroupId(var4);
                     var6 -= this.alignLong(var11, var12 - var6);
                     var4 = TGIOPUtil.extractLong(var11, var9);
                     var6 -= 4;
                     if (var4 != 0 && var4 <= var6) {
                        var3 = new byte[var4];

                        for(var5 = 0; var5 < var4; ++var5) {
                           var3[var5] = var11.readByte();
                        }

                        var6 -= var4;
                        var1.setIntfId(new String(var3, 0, var4 - 1));
                        var6 -= this.alignLong(var11, var12 - var6);
                        var4 = TGIOPUtil.extractLong(var11, var9);
                        var6 -= 4;
                        if (var4 != 0 && var4 <= var6) {
                           var3 = new byte[var4];

                           for(var5 = 0; var5 < var4; ++var5) {
                              var3[var5] = var11.readByte();
                           }

                           var6 -= var4;
                           var1.setObjectId(new ObjectId(var3, var4));
                           if (var7 > 1 && var8 == 1) {
                              if (var10) {
                                 ntrace.doTrace("/BEAObjectKey/getInfo/80");
                              }

                              var6 -= this.alignLong(var11, var12 - var6);
                              int var13 = TGIOPUtil.extractLong(var11, var9);

                              for(var6 -= 4; var13 > 0; --var13) {
                                 var6 -= this.alignLong(var11, var12 - var6);
                                 int var15 = TGIOPUtil.extractLong(var11, var9);
                                 var6 -= 4;
                                 var6 -= this.alignLong(var11, var12 - var6);
                                 int var14 = TGIOPUtil.extractLong(var11, var9);
                                 var6 -= 4;
                                 switch (var15) {
                                    case 1111834884:
                                       if (var10) {
                                          ntrace.doTrace("/BEAObjectKey/getInfo/90");
                                       }

                                       var6 -= this.alignLong(var11, var12 - var6);
                                       var4 = TGIOPUtil.extractLong(var11, var9);
                                       var6 -= 4;
                                       var14 -= 4;
                                       var1.getCltinfo().setVersion(var4);
                                       var6 -= this.alignLong(var11, var12 - var6);
                                       var4 = TGIOPUtil.extractLong(var11, var9);
                                       var6 -= 4;
                                       var14 -= 4;
                                       var3 = new byte[var4];

                                       for(var5 = 0; var5 < var4; ++var5) {
                                          var3[var5] = var11.readByte();
                                       }

                                       var6 -= var4;
                                       var14 -= var4;
                                       String var16 = new String(var3);
                                       if (var10) {
                                          ntrace.doTrace("/BEAObjectKey/getInfo/95 cltid = " + var16);
                                       }

                                       var1.getCltinfo().setId(Tpconvert.getClientId(var16));
                                       var6 -= this.alignLong(var11, var12 - var6);
                                       var4 = TGIOPUtil.extractLong(var11, var9);
                                       var6 -= 4;
                                       var14 -= 4;
                                       var1.getCltinfo().setCntxt(var4);
                                       var6 -= this.alignLong(var11, var12 - var6);
                                       var4 = TGIOPUtil.extractLong(var11, var9);
                                       var6 -= 4;
                                       var14 -= 4;
                                       var1.getCltinfo().setPid(var4);
                                       var6 -= this.alignLong(var11, var12 - var6);
                                       var4 = TGIOPUtil.extractLong(var11, var9);
                                       var6 -= 4;
                                       var14 -= 4;
                                       var1.getCltinfo().setQaddr(var4);
                                       var1.getCltinfo().setDomain(new String(var1.getDomainId()));
                                       var1.setIsACallout(1);
                                       break;
                                    case 1111834886:
                                       if (var10) {
                                          ntrace.doTrace("/BEAObjectKey/getInfo/100");
                                       }

                                       var6 -= this.alignLong(var11, var12 - var6);
                                       var4 = TGIOPUtil.extractLong(var11, var9);
                                       var6 -= 4;
                                       var14 -= 4;
                                       var1.setScaIntfBkt(var4);
                                 }

                                 if (var14 > var6) {
                                    if (var10) {
                                       ntrace.doTrace("]/BEAObjectKey/getInfo/110");
                                    }

                                    return;
                                 }

                                 while(var14 > 0) {
                                    var11.readByte();
                                    --var6;
                                    --var14;
                                 }
                              }
                           }

                           if (var10) {
                              ntrace.doTrace("]/BEAObjectKey/getInfo/120");
                           }

                        } else {
                           if (var10) {
                              ntrace.doTrace("*]/BEAObjectKey/getInfo/70/" + var4 + "/" + var6);
                           }

                           throw new TPException(12);
                        }
                     } else {
                        if (var10) {
                           ntrace.doTrace("*]/BEAObjectKey/getInfo/60");
                        }

                        throw new TPException(12);
                     }
                  } else {
                     if (var10) {
                        ntrace.doTrace("*]/BEAObjectKey/getInfo/55");
                     }

                     throw new TPException(12);
                  }
               }
            }
         }
      }
   }

   public static boolean isValidBEAObjKey(byte[] var0) {
      return var0.length >= 8 && var0[1] == 66 && var0[2] == 69 && var0[3] == 65;
   }

   public static String getDomId(byte[] var0) throws IOException, TPException {
      boolean var5 = ntrace.isTraceEnabled(4);
      if (var5) {
         ntrace.doTrace("[/BEAObjectKey/getDomId/0");
      }

      if (!isValidBEAObjKey(var0)) {
         if (var5) {
            ntrace.doTrace("*]/BEAObjectKey/getDomId/5");
         }

         throw new TPException(4);
      } else {
         byte var2 = var0[0];
         DataInputStream var6 = new DataInputStream(new ByteArrayInputStream(var0));

         int var3;
         for(var3 = 0; var3 < 8; ++var3) {
            var6.readByte();
         }

         if (var5) {
            ntrace.doTrace("/BEAObjectKey/getDomId/10");
         }

         var3 = TGIOPUtil.extractLong(var6, var2);
         if (var3 == 0) {
            return new String("");
         } else {
            byte[] var1 = new byte[var3];

            for(int var4 = 0; var4 < var3; ++var4) {
               var1[var4] = var6.readByte();
            }

            if (var5) {
               ntrace.doTrace("]/BEAObjectKey/getDomId/20");
            }

            return new String(var1, 0, var3 - 1);
         }
      }
   }

   public int alignLong(DataInputStream var1, int var2) throws IOException {
      byte var3 = 4;
      int var4 = var2 & var3 - 1;
      if (var4 != 0) {
         var4 = var3 - var4;

         for(int var5 = 0; var5 < var4; ++var5) {
            var1.readByte();
         }
      }

      return var4;
   }
}
