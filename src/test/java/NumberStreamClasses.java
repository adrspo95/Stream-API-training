import org.junit.Test;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class NumberStreamClasses {

    @Test
    public void sum() {
        double sum = DoubleStream.of(1.5, 2.3, 3.2).sum();

        assertThat(sum, is(equalTo(7.0)));
    }

    @Test
    public void average() {
        OptionalDouble average = DoubleStream.of(2.5, 2.8, 3.7).average();

        assertThat(average.getAsDouble(), is(equalTo(3.0)));
    }

    @Test
    public void boxed() {
        Stream<Long> boxedLongStream = LongStream.of(1L, 50L, 189L).boxed();
    }

    @Test
    public void mapToObj() {
        Stream<Character> mappedToObjectStream = IntStream.of(49, 50, 51).mapToObj(x -> Character.valueOf((char) x));

        assertThat(mappedToObjectStream.collect(toList()), contains('1', '2', '3'));
    }

    @Test
    public void summaryStatistics() {
        DoubleSummaryStatistics summaryStatistics = DoubleStream.of(49.8, 50.1, 51.6).summaryStatistics();

        assertThat(summaryStatistics.getAverage(), is(equalTo(50.5)));
        assertThat(summaryStatistics.getCount(), is(equalTo(3L)));
        assertThat(summaryStatistics.getMax(), is(equalTo(51.6)));
        assertThat(summaryStatistics.getMin(), is(equalTo(49.8)));
        assertThat(summaryStatistics.getSum(), is(equalTo(151.5)));
    }

    @Test
    public void range() {
        List<Integer> rangeIntegers = IntStream.range(0, 100).boxed().collect(toList());

        assertThat(rangeIntegers, hasSize(100));
        assertThat(rangeIntegers.get(0), is(equalTo(0)));
        assertThat(rangeIntegers.get(99), is(equalTo(99)));
    }

    @Test
    public void rangeClosed() {
        List<Long> rangeClosedLongs = LongStream.rangeClosed(1, 100).boxed().collect(toList());

        assertThat(rangeClosedLongs, hasSize(100));
        assertThat(rangeClosedLongs.get(0), is(equalTo(1L)));
        assertThat(rangeClosedLongs.get(99), is(equalTo(100L)));
    }


}
