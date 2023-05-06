package com.bloxbean.cardano.example.localtx;

import com.bloxbean.cardano.yaci.core.protocol.localtx.LocalTxSubmissionListener;
import com.bloxbean.cardano.yaci.core.protocol.localtx.messages.MsgAcceptTx;
import com.bloxbean.cardano.yaci.core.protocol.localtx.messages.MsgRejectTx;
import com.bloxbean.cardano.yaci.core.protocol.localtx.model.TxSubmissionRequest;
import com.bloxbean.cardano.yaci.helper.LocalClientProvider;
import com.bloxbean.cardano.yaci.helper.LocalStateQueryClient;
import com.bloxbean.cardano.yaci.helper.LocalTxSubmissionClient;

public enum LocalNodeClientFactory {
    INSTANCE;

    private LocalClientProvider localClientProvider;
    private LocalStateQueryClient localStateQueryClient;
    private LocalTxSubmissionClient txSubmissionClient;

    LocalNodeClientFactory() {
        localClientProvider = new LocalClientProvider(Constant.CARDANO_NODE_SOCKET_FILE, Constant.PROTOCOL_MAGIC_ID);

        localStateQueryClient = localClientProvider.getLocalStateQueryClient();
        txSubmissionClient = localClientProvider.getTxSubmissionClient();

        localClientProvider.addTxSubmissionListener(new LocalTxSubmissionListener() {
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

        localClientProvider.start();
    }

    public LocalStateQueryClient getLocalStateQueryClient() {
        return localStateQueryClient;
    }

    public LocalTxSubmissionClient getTxSubmissionClient() {
        return txSubmissionClient;
    }

    public void shutdown() {
        if (localClientProvider != null)
            localClientProvider.shutdown();
    }
}
