package com.bloxbean.cardano.example.localtx;

import com.bloxbean.cardano.client.api.ProtocolParamsSupplier;
import com.bloxbean.cardano.client.api.model.ProtocolParams;
import com.bloxbean.cardano.yaci.core.helpers.LocalStateQueryClient;
import com.bloxbean.cardano.yaci.core.model.Era;
import com.bloxbean.cardano.yaci.core.protocol.localstate.queries.CurrentProtocolParamQueryResult;
import com.bloxbean.cardano.yaci.core.protocol.localstate.queries.CurrentProtocolParamsQuery;

import java.math.BigDecimal;

public class YaciProtocolSupplier implements ProtocolParamsSupplier {

    public YaciProtocolSupplier() {

    }

    @Override
    public ProtocolParams getProtocolParams() {
        LocalStateQueryClient localStateQueryClient = LocalNodeClientFactory.INSTANCE.getLocalStateQueryClient();
        CurrentProtocolParamQueryResult currentProtocolParameters =
                (CurrentProtocolParamQueryResult) localStateQueryClient.executeQuery(new CurrentProtocolParamsQuery(Era.Alonzo)).block();

        ProtocolParams protocolParams = new ProtocolParams();
        protocolParams.setMinFeeA(currentProtocolParameters.getProtocolParams().getMinFeeA());
        protocolParams.setMinFeeB(currentProtocolParameters.getProtocolParams().getMinFeeB());
        protocolParams.setMaxBlockSize(currentProtocolParameters.getProtocolParams().getMaxBlockSize());
        protocolParams.setMaxTxSize(currentProtocolParameters.getProtocolParams().getMaxTxSize());
        protocolParams.setMaxBlockHeaderSize(currentProtocolParameters.getProtocolParams().getMaxBlockHeaderSize());
        protocolParams.setKeyDeposit(String.valueOf(currentProtocolParameters.getProtocolParams().getKeyDeposit()));
        protocolParams.setPoolDeposit(String.valueOf(currentProtocolParameters.getProtocolParams().getPoolDeposit()));
        protocolParams.setEMax(currentProtocolParameters.getProtocolParams().getMaxEpoch());
        protocolParams.setNOpt(currentProtocolParameters.getProtocolParams().getNOpt());
        protocolParams.setA0(currentProtocolParameters.getProtocolParams().getPoolPledgeInfluence());
        protocolParams.setRho(currentProtocolParameters.getProtocolParams().getExpansionRate());
        protocolParams.setTau(currentProtocolParameters.getProtocolParams().getTreasuryGrowthRate());
        protocolParams.setDecentralisationParam(currentProtocolParameters.getProtocolParams().getDecentralisationParam()); //Deprecated. Not there
        protocolParams.setExtraEntropy(currentProtocolParameters.getProtocolParams().getExtraEntropy());
        protocolParams.setProtocolMajorVer(currentProtocolParameters.getProtocolParams().getProtocolMajorVer());
        protocolParams.setProtocolMinorVer(currentProtocolParameters.getProtocolParams().getProtocolMinorVer());
        protocolParams.setMinUtxo(String.valueOf(currentProtocolParameters.getProtocolParams().getMinUtxo()));
        protocolParams.setMinPoolCost(String.valueOf(currentProtocolParameters.getProtocolParams().getMinPoolCost()));
//        protocolParams.setNonce(currentProtocolParameters.getProtocolParameters().getNonce()); //TODO

//        Map<String, Long> plutusV1CostModel = currentProtocolParameters.getProtocolParams().getCostModels().get("plutus:v1"); //TODo
//        Map<String, Long> plutusV2CostModel = currentProtocolParameters.getProtocolParameters().getCostModels().get("plutus:v2");
//        Map<String, Map<String, Long>> costModels = new HashMap<>();
//        costModels.put("PlutusV1", plutusV1CostModel);
//        costModels.put("PlutusV2", plutusV2CostModel);
//        protocolParams.setCostModels(costModels);

        protocolParams.setPriceMem(currentProtocolParameters.getProtocolParams().getPriceMem());
        protocolParams.setPriceStep(currentProtocolParameters.getProtocolParams().getPriceStep());
        protocolParams.setMaxTxExMem(String.valueOf(currentProtocolParameters.getProtocolParams().getMaxTxExMem()));
        protocolParams.setMaxTxExSteps(String.valueOf(currentProtocolParameters.getProtocolParams().getMaxTxExSteps()));
        protocolParams.setMaxBlockExMem(String.valueOf(currentProtocolParameters.getProtocolParams().getMaxBlockExMem()));
        protocolParams.setMaxBlockExSteps(String.valueOf(currentProtocolParameters.getProtocolParams().getMaxBlockExSteps()));
        protocolParams.setMaxValSize(String.valueOf(currentProtocolParameters.getProtocolParams().getMaxValSize()));
        protocolParams.setCollateralPercent(BigDecimal.valueOf(currentProtocolParameters.getProtocolParams().getCollateralPercent()));
        protocolParams.setMaxCollateralInputs(currentProtocolParameters.getProtocolParams().getMaxCollateralInputs());
        protocolParams.setCoinsPerUtxoSize(String.valueOf(currentProtocolParameters.getProtocolParams().getAdaPerUtxoByte()));
        return protocolParams;
    }

    public static void main(String[] args) {
        YaciProtocolSupplier yaciProtocolSupplier = new YaciProtocolSupplier();
        System.out.println(yaciProtocolSupplier.getProtocolParams());
        System.out.println(yaciProtocolSupplier.getProtocolParams());
    }
}
