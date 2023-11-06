package lotto.domain;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lotto.util.StringUtils;
import lotto.util.io.OutputUtils;

public class Lottos {

    private static final int PRICE_PER_TICKET = 1000;
    private final List<Lotto> lottos;
    private final int number;
    private Map<Ranking, Integer> winningStatics;
    private int totalPrize;

    public Lottos(List<Lotto> lottos) {
        this.lottos = lottos;
        this.number = lottos.size();
    }

    public List<Lotto> getLottos() {
        return lottos;
    }

    public int getNumber() {
        return number;
    }

    public Map<Ranking, Integer> getWinningStatics() {
        return winningStatics;
    }

    public int getTotalPrize() {
        return totalPrize;
    }

    public void draw(List<Integer> winningNumbers, int bonusNumber) {
        calculateWinningStatistics(winningNumbers, bonusNumber);
        OutputUtils.printResultAnnouncementMessage();
        calculateWinningStatistics(winningNumbers, bonusNumber);
        calculateTotalPrize();
        printResult();
    }

    public void calculateWinningStatistics(List<Integer> winningNumbers, int bonusNumber) {
        winningStatics = lottos.stream()
                .collect(groupingBy(
                        lotto -> lotto.getRanking(winningNumbers, bonusNumber),
                        summingInt(lotto -> 1)
                ));
        Arrays.stream(Ranking.values()).forEach(ranking -> winningStatics.putIfAbsent(ranking, 0));
    }

    public void calculateTotalPrize() {
        totalPrize = winningStatics.entrySet().stream()
                .map(r -> StringUtils.StringToInt(r.getKey().getPrize()) * r.getValue())
                .reduce(0, Integer::sum);
    }

    private void printResult() {
        winningStatics.entrySet().stream()
                .filter(r -> r.getKey() != Ranking.NONE)
                .sorted((entry1, entry2) ->
                        Integer.compare(entry2.getKey().ordinal(), entry1.getKey().ordinal()))
                .forEach(r -> OutputUtils.printWinningDetail(
                        r.getKey().getDescription(),
                        r.getKey().getPrize(),
                        r.getValue().intValue()
                ));
        OutputUtils.printWinningRateOfReturnMessage(calculateRateOfReturn(number * PRICE_PER_TICKET));
    }

    private float calculateRateOfReturn(int money) {
        float value = (float) totalPrize / (float) money * 100;
        return (float) (Math.round(value * 100.0) / 100.0);
    }
}
