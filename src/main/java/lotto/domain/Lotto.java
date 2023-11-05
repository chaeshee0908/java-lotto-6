package lotto.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Lotto {

    private static final int COUNT_OF_LOTTO_NUMBERS = 6;
    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    private void validate(List<Integer> numbers) {
        if (numbers.size() != COUNT_OF_LOTTO_NUMBERS) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개이어야 합니다.");
        }

        Set<Integer> uniqueElements = new HashSet<>(numbers);
        if (numbers.size() !=uniqueElements.size()) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 중복된 숫자가 없어야 합니다.");
        }
    }

    public Ranking getRanking(List<Integer> winningNumbers, int bonusNumber) {
        int sameInWinningNumbers = (int) numbers.stream().filter(n -> winningNumbers.contains(n)).count();
        if (sameInWinningNumbers == 6) {
            return Ranking.FIRST;
        }
        if (sameInWinningNumbers == 5 && numbers.contains(bonusNumber)) {
            return Ranking.SECOND;
        }
        if (sameInWinningNumbers == 5) {
            return Ranking.THIRD;
        }
        if (sameInWinningNumbers == 4) {
            return Ranking.FOURTH;
        }
        if (sameInWinningNumbers == 3) {
            return Ranking.FIFTH;
        }
        return Ranking.NONE;
    }

    @Override
    public String toString() {
        return numbers.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
