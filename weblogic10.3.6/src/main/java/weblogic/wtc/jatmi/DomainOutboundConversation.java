package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.ntrace;

public class DomainOutboundConversation implements Conversation {
   private boolean isDisconnected = false;
   private dsession myDomainSession;
   private int mySendSequenceNumber = 1;
   private int myRecvSequenceNumber = 1;
   private int myConversationIdentifier;
   private boolean mySendOnly;
   private ConversationReply myRplyObj;
   private SessionAcallDescriptor myCallDescriptor;
   private boolean myInTransaction;
   private rdsession myRecieveSession;

   public DomainOutboundConversation(dsession var1, ConversationReply var2, int var3, boolean var4, SessionAcallDescriptor var5, boolean var6) {
      this.myDomainSession = var1;
      this.myRplyObj = var2;
      this.myConversationIdentifier = var3;
      this.mySendOnly = var4;
      this.myCallDescriptor = var5;
      this.myInTransaction = var6;
      this.myRecieveSession = var1.get_rcv_place();
   }

   private void internalDisconnect() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/DomainOutboundConversation/internalDisconnect/");
      }

      if (this.isDisconnected) {
         if (var1) {
            ntrace.doTrace("]/DomainOutboundConversation/internalDisconnect/10");
         }

      } else {
         this.myRecieveSession.remove_rplyObj(this.myCallDescriptor);
         this.myDomainSession = null;
         this.myRplyObj = null;
         this.myCallDescriptor = null;
         this.myRecieveSession = null;
         this.isDisconnected = true;
         if (var1) {
            ntrace.doTrace("]/DomainOutboundConversation/internalDisconnect/20");
         }

      }
   }

   public synchronized void tpsend(TypedBuffer var1, int var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(4);
      if (var3) {
         ntrace.doTrace("[/DomainOutboundConversation/tpsend/" + var1 + "/" + var2);
      }

      if (!this.mySendOnly) {
         if (var3) {
            ntrace.doTrace("*]/DomainOutboundConversation/tpsend/10/TPEPROTO");
         }

         throw new TPException(9, "ERROR: Attempting to send but direction is receive");
      } else if (this.myDomainSession.getIsTerminated()) {
         this.internalDisconnect();
         if (var3) {
            ntrace.doTrace("*]/DomainOutboundConversation/tpsend/20/TPESYSTEM");
         }

         throw new TPException(12, "WARN: The domain link was removed underneath the conversation");
      } else if ((var2 & -4130) != 0) {
         if (var3) {
            ntrace.doTrace("*]/DomainOutboundConversation/tpsend/30/TPEINVAL");
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
            this.myDomainSession._tpsend_internal(var4, this.mySendSequenceNumber, this.myConversationIdentifier, true, var5, false);
         } catch (TPException var7) {
            this.internalDisconnect();
            if (var3) {
               ntrace.doTrace("*]/DomainOutboundConversation/tpsend/40/" + var7);
            }

            throw var7;
         }

         ++this.mySendSequenceNumber;
         if (var5) {
            this.mySendOnly = false;
         }

         if (var3) {
            ntrace.doTrace("]/DomainOutboundConversation/tpsend/50/");
         }

      }
   }

   public synchronized Reply tprecv(int var1) throws TPException, TPReplyException {
      boolean var2 = ntrace.isTraceEnabled(4);
      if (var2) {
         ntrace.doTrace("[/DomainOutboundConversation/tprecv/" + var1);
      }

      TuxedoReply var7 = null;
      if (this.mySendOnly) {
         if (var2) {
            ntrace.doTrace("*]/DomainOutboundConversation/tprecv/10/TPEPROTO");
         }

         throw new TPException(9, "ERROR: Attempting to receive but direction is send");
      } else if (this.myDomainSession.getIsTerminated()) {
         this.internalDisconnect();
         if (var2) {
            ntrace.doTrace("*]/DomainOutboundConversation/tprecv/20/TPESYSTEM");
         }

         throw new TPException(12, "WARN: The domain link was removed underneath the conversation");
      } else if ((var1 & -34) != 0) {
         if (var2) {
            ntrace.doTrace("*]/DomainOutboundConversation/tprecv/30/TPEINVAL");
         }

         throw new TPException(4);
      } else {
         boolean var3 = (var1 & 1) == 0;
         if (!this.myInTransaction && (var1 & 32) == 0 && !this.myRecieveSession.addRplyObjTimeout(this.myCallDescriptor, 0)) {
            this.internalDisconnect();
            if (var2) {
               ntrace.doTrace("*]/DomainOutboundConversation/tprecv/40/TPESYSTEM");
            }

            throw new TPException(12, "ERROR: Unable to set a timeout for tprecv");
         } else {
            ReqMsg var4;
            if ((var4 = this.myRplyObj.get_reply(var3)) == null) {
               if (var3) {
                  this.internalDisconnect();
                  if (var2) {
                     ntrace.doTrace("*]/DomainOutboundConversation/tprecv/50/TPESYSTEM");
                  }

                  throw new TPException(12, "ERROR: Conversation in invalid state");
               } else {
                  if (var2) {
                     ntrace.doTrace("*]/DomainOutboundConversation/tprecv/60/TPEBLOCK");
                  }

                  throw new TPException(3);
               }
            } else {
               tfmh var5;
               if ((var5 = var4.getReply()) == null) {
                  this.internalDisconnect();
                  if (var2) {
                     ntrace.doTrace("*]/DomainOutboundConversation/tprecv/70/TPESYSTEM");
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
                        ntrace.doTrace("*]/DomainOutboundConversation/tprecv/80/" + var6 + "/" + var15 + "/" + this.myRecvSequenceNumber);
                     }

                     throw var6;
                  } else {
                     ++this.myRecvSequenceNumber;
                     if (var2) {
                        ntrace.doTrace("*]/DomainOutboundConversation/tprecv/85/" + var9 + "/" + var10 + "/" + var11 + "/" + var12 + "/" + var13 + "/" + var14 + "/" + var15);
                     }

                     switch (var13) {
                        case 2:
                           var6 = new TPReplyException(22, 0, var11, var12, 0, 8, new TuxedoReply(var16, var11, this.myCallDescriptor));
                           this.internalDisconnect();
                           if (var2) {
                              ntrace.doTrace("*]/DomainOutboundConversation/tprecv/120/" + var6);
                           }

                           throw var6;
                        case 3:
                           this.internalDisconnect();
                           if (var9 == 22) {
                              switch (var14) {
                                 case 1:
                                 case 2:
                                 case 4:
                                    var6 = new TPReplyException(22, 0, var11, var12, 0, var14, new TuxedoReply(var16, var11, this.myCallDescriptor));
                                    if (var2) {
                                       ntrace.doTrace("*]/DomainOutboundConversation/tprecv/140/" + var6);
                                    }

                                    throw var6;
                                 case 3:
                                 default:
                                    if (var2) {
                                       ntrace.doTrace("*]/DomainOutboundConversation/tprecv/145/TPESYSTEM");
                                    }

                                    throw new TPException(12, "ERROR: Invalid tpevent detected " + var14);
                              }
                           }

                           if (var2) {
                              ntrace.doTrace("*]/DomainOutboundConversation/tprecv/146/TPESYSTEM");
                           }

                           throw new TPException(12, "ERROR: Unexpected exception:" + var9);
                        case 4:
                        default:
                           this.internalDisconnect();
                           if (var2) {
                              ntrace.doTrace("*]/DomainOutboundConversation/tprecv/150/TPESYSTEM" + var13);
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
                                       ntrace.doTrace("*]/DomainOutboundConversation/tprecv/100/TPESYSTEM");
                                    }

                                    throw new TPException(12, "ERROR: Invalid tpevent detected " + var14);
                                 case 32:
                                    this.mySendOnly = true;
                                    var6 = new TPReplyException(var9, 0, var11, var12, 0, var14, new TuxedoReply(var16, var11, this.myCallDescriptor));
                                    if (var2) {
                                       ntrace.doTrace("*]/DomainOutboundConversation/tprecv/90/" + var6);
                                    }

                                    throw var6;
                              }
                           } else {
                              if (var9 != 0) {
                                 if (var2) {
                                    ntrace.doTrace("*]/DomainOutboundConversation/tprecv/110/TPESYSTEM/invalid diagnostic:" + var9);
                                 }

                                 throw new TPException(12, "ERROR: Invalid diagnostic:" + var9);
                              }

                              var7 = new TuxedoReply(var16, 0, this.myCallDescriptor);
                              if (var2) {
                                 ntrace.doTrace("]/DomainOutboundConversation/tprecv/160/" + var7);
                              }

                              return var7;
                           }
                        case 6:
                           var6 = new TPReplyException(22, 0, var11, var12, 0, 1, new TuxedoReply(var16, var11, this.myCallDescriptor));
                           this.internalDisconnect();
                           if (var2) {
                              ntrace.doTrace("*]/DomainOutboundConversation/tprecv/130/" + var6);
                           }

                           throw var6;
                     }
                  }
               }
            }
         }
      }
   }

   public synchronized void tpdiscon() throws TPException {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/DomainOutboundConversation/tpdiscon/");
      }

      if (this.myDomainSession == null) {
         if (var1) {
            ntrace.doTrace("*]/DomainOutboundConversation/tpdiscon/10/TPEPROTO");
         }

         throw new TPException(9, "ERROR: Attempting tpdiscon in an improper context");
      } else {
         tfmh var2 = new tfmh(1);

         try {
            this.myDomainSession._tpsend_internal(var2, this.mySendSequenceNumber, this.myConversationIdentifier, true, false, true);
         } catch (TPException var4) {
            if (var1) {
               ntrace.doTrace("/DomainOutboundConversation/tpdiscon/internal send failure: " + var4);
            }

            throw var4;
         }

         ++this.mySendSequenceNumber;
         this.internalDisconnect();
         if (var1) {
            ntrace.doTrace("]/DomainOutboundConversation/tpdiscon/20");
         }

      }
   }

   public String toString() {
      return new String(this.isDisconnected + ":" + this.mySendSequenceNumber + ":" + this.myConversationIdentifier + ":" + this.mySendOnly + ":" + this.myCallDescriptor + ":" + this.myInTransaction);
   }
}
