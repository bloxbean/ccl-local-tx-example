package com.bloxbean.cardano.example.localtx;

import com.bloxbean.cardano.client.account.Account;
import com.bloxbean.cardano.client.api.ProtocolParamsSupplier;
import com.bloxbean.cardano.client.api.UtxoSupplier;
import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.cip.cip20.MessageMetadata;
import com.bloxbean.cardano.client.common.model.Networks;
import com.bloxbean.cardano.client.exception.AddressExcepion;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.function.Output;
import com.bloxbean.cardano.client.function.TxBuilder;
import com.bloxbean.cardano.client.function.TxBuilderContext;
import com.bloxbean.cardano.client.function.helper.AuxDataProviders;
import com.bloxbean.cardano.client.function.helper.InputBuilders;
import com.bloxbean.cardano.client.transaction.spec.Transaction;
import com.bloxbean.cardano.yaci.core.common.TxBodyType;
import com.bloxbean.cardano.yaci.core.protocol.localtx.model.TxSubmissionRequest;

import static com.bloxbean.cardano.client.common.ADAConversionUtil.adaToLovelace;
import static com.bloxbean.cardano.client.common.CardanoConstants.LOVELACE;
import static com.bloxbean.cardano.client.function.helper.BalanceTxBuilders.balanceTx;
import static com.bloxbean.cardano.client.function.helper.SignerProviders.signerFrom;

public class TransferTransactionTest {

    //Initialize UtxoSupplier & ProtocolParamsSupplier
    private UtxoSupplier utxoSupplier = new YaciUtxoSupplier();
    private ProtocolParamsSupplier protocolParamsSupplier = new YaciProtocolSupplier();

    public void transfer() throws CborSerializationException, ApiException, AddressExcepion {
        String senderMnemonic = "damp wish ...";
        String receiverAddress = "addr_test1qqwpl7h3g84mhr36wpetk904p7fchx2vst0z696lxk8ujsjyruqwmlsm344gfux3nsj6njyzj3ppvrqtt36cp9xyydzqzumz82";

        Account sender = new Account(Networks.testnet(), senderMnemonic);
        String senderAddress = sender.baseAddress();

        Output output = Output.builder()
                .address(receiverAddress)
                .assetName(LOVELACE)
                .qty(adaToLovelace(1.1))
                .build();

        TxBuilder txBuilder = output.outputBuilder()
                .buildInputs(InputBuilders.createFromSender(senderAddress, senderAddress))
                .andThen(AuxDataProviders.metadataProvider(MessageMetadata.create().add("Cardano Client Lib - Test")))
                .andThen(balanceTx(senderAddress, 1));

        Transaction signedTransaction = TxBuilderContext.init(utxoSupplier, protocolParamsSupplier)
                .buildAndSign(txBuilder, signerFrom(sender));

        System.out.println(">> " + signedTransaction.serializeToHex());

        //Submit Tx using LocalStateQuery mini-protocol
        LocalNodeClientFactory.INSTANCE.getTxSubmissionClient().submitTx(new TxSubmissionRequest(TxBodyType.BABBAGE, signedTransaction.serialize()));
        LocalNodeClientFactory.INSTANCE.shutdown();
    }

    public static void main(String[] args) throws AddressExcepion, CborSerializationException, ApiException, InterruptedException {
        TransferTransactionTest transferTransactionTest = new TransferTransactionTest();
        transferTransactionTest.transfer();
    }
}
