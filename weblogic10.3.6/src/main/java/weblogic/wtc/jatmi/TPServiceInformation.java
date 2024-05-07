package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.ntrace;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TPServiceInformation extends TuxedoReply implements Conversation {
   private static final long serialVersionUID = -2256092221608261884L;
   private String service_name;
   private TypedBuffer service_data;
   private int service_flags;
   private String service_data_key;
   private int mySessionIdentifier = -1;
   private int myConversationIdentifier;
   private transient boolean isConversationInitialized = false;
   private transient boolean conversationInitializationStatus;
   private transient boolean isDisconnected = false;
   private transient dsession myDomainSession;
   private transient int mySendSequenceNumber;
   private transient int myRecvSequenceNumber;
   private transient boolean mySendOnly;
   private transient ConversationReply myRplyObj;
   private transient SessionAcallDescriptor myCallDescriptor;
   private transient rdsession myRecieveSession;

   private void writeObject(ObjectOutputStream var1) throws IOException {
      boolean var2 = ntrace.isTraceEnabled(50000);
      if (var2) {
         ntrace.doTrace("[/TPServiceInformation/writeObject/");
      }

      if (null != TypedBufferFactory.getBufferPool() && null != this.service_data) {
         this.service_data_key = this.service_data.toString();
         TypedBufferFactory.getBufferPool().put(this.service_data_key, this.service_data);
         TypedBuffer var3 = this.service_data;
         this.service_data = null;
         var1.defaultWriteObject();
         this.service_data = var3;
         this.service_data_key = null;
         if (var2) {
            ntrace.doTrace("]/TPServiceInformation/writeObject/20/true");
         }

      } else {
         this.service_data_key = null;
         var1.defaultWriteObject();
         if (var2) {
            ntrace.doTrace("]/TPServiceInformation/writeObject/10/false");
         }

      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      boolean var2 = ntrace.isTraceEnabled(50000);
      if (var2) {
         ntrace.doTrace("[/TPServiceInformation/readObject/");
      }

      this.service_data_key = null;
      var1.defaultReadObject();
      if (null != TypedBufferFactory.getBufferPool() && null != this.service_data_key) {
         this.service_data = TypedBufferFactory.getBufferPool().get(this.service_data_key);
         this.service_data_key = null;
         if (var2) {
            ntrace.doTrace("]/TPServiceInformation/readObject/20/true");
         }

      } else {
         if (var2) {
            ntrace.doTrace("]/TPServiceInformation/readObject/10/false");
         }

      }
   }

   public TPServiceInformation() {
   }

   public TPServiceInformation(String var1, TypedBuffer var2, int var3, int var4, int var5) {
      this.service_name = var1;
      this.service_data = var2;
      this.service_flags = var3;
      this.mySessionIdentifier = var4;
      this.myConversationIdentifier = var5;
   }

   public String getServiceName() {
      return this.service_name;
   }

   public TypedBuffer getServiceData() {
      return this.service_data;
   }

   public int getServiceFlags() {
      return this.service_flags;
   }

   private void initializeConversation() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/TPServiceInformation/initializeConversation/");
      }

      if (this.isConversationInitialized) {
         if (var1) {
            ntrace.doTrace("]/TPServiceInformation/initializeConversation/10/false");
         }

      } else {
         this.conversationInitializationStatus = false;
         this.isConversationInitialized = true;
         gwatmi var2;
         if ((var2 = DomainRegistry.getDomainSession(this.mySessionIdentifier)) == null) {
            if (var1) {
               ntrace.doTrace("]/TPServiceInformation/initializeConversation/20/false");
            }

         } else if (!(var2 instanceof dsession)) {
            if (var1) {
               ntrace.doTrace("]/TPServiceInformation/initializeConversation/30/false");
            }

         } else {
            this.myDomainSession = (dsession)var2;
            this.mySendOnly = (this.service_flags & 2048) != 0;
            this.myCallDescriptor = new SessionAcallDescriptor(this.myConversationIdentifier, true);
            if ((this.myRecieveSession = this.myDomainSession.get_rcv_place()) == null) {
               if (var1) {
                  ntrace.doTrace("]/TPServiceInformation/initializeConversation/40/false");
               }

            } else if ((this.myRplyObj = this.myRecieveSession.getConversationReply(this.myConversationIdentifier)) == null) {
               if (var1) {
                  ntrace.doTrace("]/TPServiceInformation/initializeConversation/50/false");
               }

            } else {
               this.mySendSequenceNumber = 1;
               this.myRecvSequenceNumber = 1;
               if (var1) {
                  ntrace.doTrace("]/TPServiceInformation/initializeConversation/60/true");
               }

               this.conversationInitializationStatus = true;
            }
         }
      }
   }

   private void internalDisconnect() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/TPServiceInformation/internalDisconnect/");
      }

      if (this.isDisconnected) {
         if (var1) {
            ntrace.doTrace("]/TPServiceInformation/internalDisconnect/10");
         }

      } else {
         this.myRecieveSession.remove_rplyObj(this.myCallDescriptor);
         this.myDomainSession = null;
         this.myRplyObj = null;
         this.myCallDescriptor = null;
         this.myRecieveSession = null;
         this.isDisconnected = true;
         if (var1) {
            ntrace.doTrace("]/TPServiceInformation/internalDisconnect/20");
         }

      }
   }

   public void tpsend(TypedBuffer var1, int var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(4);
      if (var3) {
         ntrace.doTrace("[/TPServiceInformation/tpsend/" + var1 + "/" + var2);
      }

      if ((this.service_flags & 1024) == 0) {
         if (var3) {
            ntrace.doTrace("*]/TPServiceInformation/tpsend/10/TPEINVAL");
         }

         throw new TPException(4, "ERROR: This service is not conversational");
      } else {
         if (!this.isConversationInitialized) {
            this.initializeConversation();
         }

         if (!this.conversationInitializationStatus) {
            if (var3) {
               ntrace.doTrace("*]/TPServiceInformation/tpsend/20/TPEINVAL");
            }

            throw new TPException(12, "ERROR: Conversation initialization failed");
         } else if (this.isDisconnected) {
            if (var3) {
               ntrace.doTrace("*]/TPServiceInformation/tpsend/25/TPEPROTO");
            }

            throw new TPException(9, "ERROR: Conversation has been disconnected");
         } else if (!this.mySendOnly) {
            if (var3) {
               ntrace.doTrace("*]/TPServiceInformation/tpsend/30/TPEPROTO");
            }

            throw new TPException(9, "ERROR: Attempting to send but direction is receive");
         } else if (this.myDomainSession.getIsTerminated()) {
            this.internalDisconnect();
            if (var3) {
               ntrace.doTrace("*]/TPServiceInformation/tpsend/40/TPESYSTEM");
            }

            throw new TPException(12, "WARN: The domain link was removed underneath the conversation");
         } else if ((var2 & -4130) != 0) {
            if (var3) {
               ntrace.doTrace("*]/TPServiceInformation/tpsend/50/TPEINVAL");
            }

            throw new TPException(4);
         } else {
            var2 &= -34;
            boolean var5 = (var2 & 4096) != 0;
            tfmh var4;
            if (var1 == null) {
               var4 = new tfmh(1);
            } else {
               tcm var6 = new tcm((short)0, new UserTcb(var1));
               var4 = new tfmh(var1.getHintIndex(), var6, 1);
            }

            try {
               this.myDomainSession._tpsend_internal(var4, this.mySendSequenceNumber, this.myConversationIdentifier, false, var5, false);
            } catch (TPException var7) {
               this.internalDisconnect();
               if (var3) {
                  ntrace.doTrace("*]/TPServiceInformation/tpsend/60/" + var7);
               }

               throw var7;
            }

            ++this.mySendSequenceNumber;
            if (var5) {
               this.mySendOnly = false;
            }

            if (var3) {
               ntrace.doTrace("]/TPServiceInformation/tpsend/70/");
            }

         }
      }
   }

   public Reply tprecv(int var1) throws TPException, TPReplyException {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/TPServiceInformation/tprecv/" + var1);
      }

      TuxedoReply var7 = null;
      if ((this.service_flags & 1024) == 0) {
         if (var2) {
            ntrace.doTrace("*]/TPServiceInformation/tprecv/10/TPEINVAL");
         }

         throw new TPException(4, "ERROR: This service is not conversational");
      } else {
         if (!this.isConversationInitialized) {
            this.initializeConversation();
         }

         if (!this.conversationInitializationStatus) {
            if (var2) {
               ntrace.doTrace("*]/TPServiceInformation/tprecv/20/TPESYSTEM");
            }

            throw new TPException(12, "ERROR: Conversation initialization failed");
         } else if (this.isDisconnected) {
            if (var2) {
               ntrace.doTrace("*]/TPServiceInformation/tprecv/25/TPEPROTO");
            }

            throw new TPException(9, "ERROR: Conversation has been disconnected");
         } else if (this.mySendOnly) {
            if (var2) {
               ntrace.doTrace("*]/TPServiceInformation/tprecv/30/TPEPROTO");
            }

            throw new TPException(9, "ERROR: Attempting to receive but direction is send");
         } else if (this.myDomainSession.getIsTerminated()) {
            this.internalDisconnect();
            if (var2) {
               ntrace.doTrace("*]/TPServiceInformation/tprecv/40/TPESYSTEM");
            }

            throw new TPException(12, "WARN: The domain link was removed underneath the conversation");
         } else if ((var1 & -34) != 0) {
            if (var2) {
               ntrace.doTrace("*]/TPServiceInformation/tprecv/50/TPEINVAL");
            }

            throw new TPException(4);
         } else {
            boolean var3 = (var1 & 1) == 0;
            if ((var1 & 32) == 0 && !this.myRecieveSession.addRplyObjTimeout(this.myCallDescriptor, 0)) {
               this.internalDisconnect();
               if (var2) {
                  ntrace.doTrace("*]/TPServiceInformation/tprecv/60/TPESYSTEM");
               }

               throw new TPException(12, "ERROR: Unable to set a timeout for tprecv");
            } else {
               ReqMsg var4;
               if ((var4 = this.myRplyObj.get_reply(var3)) == null) {
                  if (var3) {
                     this.internalDisconnect();
                     if (var2) {
                        ntrace.doTrace("*]/TPServiceInformation/tprecv/70/TPESYSTEM");
                     }

                     throw new TPException(12, "ERROR: Conversation in invalid state");
                  } else {
                     if (var2) {
                        ntrace.doTrace("*]/TPServiceInformation/tprecv/80/TPEBLOCK");
                     }

                     throw new TPException(3);
                  }
               } else {
                  tfmh var5;
                  if ((var5 = var4.getReply()) == null) {
                     this.internalDisconnect();
                     if (var2) {
                        ntrace.doTrace("*]/TPServiceInformation/tprecv/90/TPESYSTEM");
                     }

                     throw new TPException(12, "ERROR: Invalid tprecv message");
                  } else {
                     TdomTcb var8 = (TdomTcb)var5.tdom.body;
                     int var9 = var8.get_diagnostic();
                     int var10 = var8.get_flag();
                     int var11 = var8.getTpurcode();
                     int var12 = var8.get_errdetail();
                     int var13 = var8.get_opcode();
                     int var14 = var8.get_tpevent();
                     int var15 = var8.get_seqnum();
                     if ((var10 & 4096) != 0 && var14 == 0) {
                        var14 = 32;
                     }

                     TypedBuffer var16;
                     if (var5.user == null) {
                        var16 = null;
                     } else {
                        var16 = ((UserTcb)var5.user.body).user_data;
                     }

                     TPReplyException var6;
                     if (var15 != this.myRecvSequenceNumber) {
                        var6 = new TPReplyException(22, 0, var11, var12, 0, 1, new TuxedoReply(var16, var11, this.myCallDescriptor));
                        this.internalDisconnect();
                        if (var2) {
                           ntrace.doTrace("*]/TPServiceInformation/tprecv/100/" + var6 + "/" + var15 + "/" + this.myRecvSequenceNumber);
                        }

                        throw var6;
                     } else {
                        ++this.myRecvSequenceNumber;
                        switch (var13) {
                           case 2:
                              var6 = new TPReplyException(22, 0, var11, var12, 0, 8, new TuxedoReply(var16, var11, this.myCallDescriptor));
                              this.internalDisconnect();
                              if (var2) {
                                 ntrace.doTrace("*]/TPServiceInformation/tprecv/140/" + var6);
                              }

                              throw var6;
                           case 3:
                              if (var9 == 11) {
                                 var6 = new TPReplyException(22, 0, var11, var12, 0, 4, new TuxedoReply(var16, var11, this.myCallDescriptor));
                              } else if (var9 == 10) {
                                 var6 = new TPReplyException(22, 0, var11, var12, 0, 2, new TuxedoReply(var16, var11, this.myCallDescriptor));
                              } else {
                                 var6 = new TPReplyException(22, 0, var11, var12, 0, 1, new TuxedoReply(var16, var11, this.myCallDescriptor));
                              }

                              this.internalDisconnect();
                              if (var2) {
                                 ntrace.doTrace("*]/TPServiceInformation/tprecv/160/" + var6);
                              }

                              throw var6;
                           case 4:
                           default:
                              this.internalDisconnect();
                              if (var2) {
                                 ntrace.doTrace("*]/TPServiceInformation/tprecv/170/TPESYSTEM" + var13);
                              }

                              throw new TPException(12, "ERROR: Got an invalid conversational response");
                           case 5:
                              if (var9 == 22) {
                                 switch (var14) {
                                    case 1:
                                    case 2:
                                    case 4:
                                    case 8:
                                    default:
                                       this.internalDisconnect();
                                       if (var2) {
                                          ntrace.doTrace("*]/TPServiceInformation/tprecv/120/TPESYSTEM");
                                       }

                                       throw new TPException(12, "ERROR: Invalid tpevent detected " + var14);
                                    case 32:
                                       this.mySendOnly = true;
                                       var6 = new TPReplyException(var9, 0, var11, var12, 0, var14, new TuxedoReply(var16, var11, this.myCallDescriptor));
                                       if (var2) {
                                          ntrace.doTrace("*]/TPServiceInformation/tprecv/110/" + var6);
                                       }

                                       throw var6;
                                 }
                              } else {
                                 if (var9 != 0) {
                                    if (var2) {
                                       ntrace.doTrace("*]/TPServiceInformation/tprecv/130/TPESYSTEM/invalid diagnostic:" + var9);
                                    }

                                    throw new TPException(12, "ERROR: Invalid diagnostic:" + var9);
                                 }

                                 var7 = new TuxedoReply(var16, 0, this.myCallDescriptor);
                                 if (var2) {
                                    ntrace.doTrace("]/TPServiceInformation/tprecv/180/" + var7);
                                 }

                                 return var7;
                              }
                           case 6:
                              var6 = new TPReplyException(22, 0, var11, var12, 0, 1, new TuxedoReply(var16, var11, this.myCallDescriptor));
                              this.internalDisconnect();
                              if (var2) {
                                 ntrace.doTrace("*]/TPServiceInformation/tprecv/150/" + var6);
                              }

                              throw var6;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void tpdiscon() throws TPException {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/TPServiceInformation/tpdiscon/");
      }

      tfmh var2 = new tfmh(1);
      if ((this.service_flags & 1024) == 0) {
         if (var1) {
            ntrace.doTrace("]/TPServiceInformation/tpdiscon/10/TPEINVAL");
         }

         throw new TPException(4, "ERROR: This service is not conversational");
      } else {
         if (!this.isConversationInitialized) {
            this.initializeConversation();
         }

         if (!this.conversationInitializationStatus) {
            if (var1) {
               ntrace.doTrace("]/TPServiceInformation/tpdiscon/20/TPESYSTEM");
            }

            throw new TPException(12, "ERROR: Conversation initialization failed");
         } else if (this.isDisconnected) {
            if (var1) {
               ntrace.doTrace("*]/TPServiceInformation/tpdiscon/25/TPEPROTO");
            }

            throw new TPException(9, "ERROR: Conversation has been disconnected");
         } else {
            try {
               this.myDomainSession._tpsend_internal(var2, this.mySendSequenceNumber, this.myConversationIdentifier, true, false, true);
            } catch (TPException var4) {
               if (var1) {
                  ntrace.doTrace("/TPServiceInformation/tpdiscon/internal send failure: " + var4);
               }
            }

            ++this.mySendSequenceNumber;
            this.internalDisconnect();
            if (var1) {
               ntrace.doTrace("]/TPServiceInformation/tpdiscon/30");
            }

         }
      }
   }

   public String toString() {
      return new String(super.toString() + ":" + this.service_name + ":" + this.service_data + ":" + this.service_flags + ":" + this.mySessionIdentifier + ":" + this.myConversationIdentifier);
   }
}
