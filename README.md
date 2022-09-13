**Example:**

Create and submit a transaction to Cardano network using **Cardano Client Lib**, **Yaci's LocalStateQuery & Local Tx**
implementation.

Cardano client Lib needs implementation of the following three interfaces to build and submit a transaction

1. **com.bloxbean.cardano.client.api.ProtocolParamsSupplier :**  Provides current protocol parameters. This is required to calculate fee
and min required ada in utxo.

2. **com.bloxbean.cardano.client.api.UtxoSupplier :** Provides utxos at an address. Required to build the transaction

3. **com.bloxbean.cardano.client.api.TransactionProcessor (Optional) :** To submit the transaction to the Cardano network. 
