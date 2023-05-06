package com.bloxbean.cardano.example.localtx;

import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.api.UtxoSupplier;
import com.bloxbean.cardano.client.api.common.OrderEnum;
import com.bloxbean.cardano.client.api.model.Utxo;
import com.bloxbean.cardano.yaci.core.protocol.localstate.queries.UtxoByAddressQuery;
import com.bloxbean.cardano.yaci.core.protocol.localstate.queries.UtxoByAddressQueryResult;
import com.bloxbean.cardano.yaci.helper.LocalStateQueryClient;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class YaciUtxoSupplier implements UtxoSupplier {

    @Override
    public List<Utxo> getPage(String address, Integer nrOfItems, Integer page, OrderEnum order) {
        if (page != null)
            page = page + 1;
        else
            page = 1;

        if (page != 1)
            return Collections.EMPTY_LIST;

        LocalStateQueryClient localStateQueryClient = LocalNodeClientFactory.INSTANCE.getLocalStateQueryClient();

        UtxoByAddressQueryResult queryResult = (UtxoByAddressQueryResult) localStateQueryClient
                .executeQuery(new UtxoByAddressQuery(new Address(address))).block(Duration.ofSeconds(10));

        List<Utxo> utxos = queryResult.getUtxoList();
        //Replace . with empty string in unit as CCL doens't expect . in unit
        utxos.forEach(utxo -> {
            utxo.getAmount().forEach(amount -> {
                String unit = amount.getUnit();
                unit = unit.replace(".", "");
                amount.setUnit(unit);
            });
        });

        return utxos;
    }

    public static void main(String[] args) {
        String address = "addr_test1qzx9hu8j4ah3auytk0mwcupd69hpc52t0cw39a65ndrah86djs784u92a3m5w475w3w35tyd6v3qumkze80j8a6h5tuqq5xe8y";
        List<Utxo> utxos = new YaciUtxoSupplier().getPage(address, 100, 0, OrderEnum.asc);

        System.out.println(utxos);
    }
}
