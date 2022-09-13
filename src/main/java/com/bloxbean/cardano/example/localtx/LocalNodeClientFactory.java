package com.bloxbean.cardano.example.localtx;

import com.bloxbean.cardano.yaci.core.helpers.LocalStateQueryClient;
import com.bloxbean.cardano.yaci.core.helpers.LocalTxSubmissionClient;
import com.bloxbean.cardano.yaci.core.protocol.localtx.LocalTxSubmissionListener;
import com.bloxbean.cardano.yaci.core.protocol.localtx.messages.MsgAcceptTx;
import com.bloxbean.cardano.yaci.core.protocol.localtx.messages.MsgRejectTx;
import com.bloxbean.cardano.yaci.core.protocol.localtx.model.TxSubmissionRequest;

public enum LocalNodeClientFactory {
    INSTANCE;

    private LocalStateQueryClient localStateQueryClient;
    private LocalTxSubmissionClient txSubmissionClient;

    LocalNodeClientFactory() {
        this.localStateQueryClient = new LocalStateQueryClient(Constant.CARDANO_NODE_SOCKET_FILE, Constant.PROTOCOL_MAGIC_ID);
        this.localStateQueryClient.start(result -> {});

        this.txSubmissionClient = new LocalTxSubmissionClient(Constant.CARDANO_NODE_SOCKET_FILE, Constant.PROTOCOL_MAGIC_ID);
        this.txSubmissionClient.addTxSubmissionListener(new LocalTxSubmissionListener() {
            @Override
            public void txAccepted(TxSubmissionRequest txSubmissionRequest, MsgAcceptTx msgAcceptTx) {
                System.out.println("*********** Transaction Submitted Successfully ***************");
                System.out.println("Tx Hash >> " + txSubmissionRequest.getTxHash());
            }

            @Override
            public void txRejected(TxSubmissionRequest txSubmissionRequest, MsgRejectTx msgRejectTx) {
                System.out.println("########## Transaction submission failed ###########");
            }
        });
        this.txSubmissionClient.start(txResult -> {});
    }

    public LocalStateQueryClient getLocalStateQueryClient() {
        return localStateQueryClient;
    }

    public LocalTxSubmissionClient getTxSubmissionClient() {
        return txSubmissionClient;
    }

    public void shutdown() {
        localStateQueryClient.shutdown();
        txSubmissionClient.shutdown();
    }
}
