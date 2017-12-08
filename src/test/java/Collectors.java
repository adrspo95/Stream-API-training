import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Collectors {

    @Test
    public void toListCollector() {
        List<String> collectedStream = Stream.of("to", "List", "collector", "test").collect(toList());

        assertThat(collectedStream, contains("to", "List", "collector", "test"));
    }

    @Test
    public void toSetCollector() {
        Set<Integer> collectedStream = Stream.of(1, 2, 2, 3, 4, 3).collect(toSet());

        assertThat(collectedStream, containsInAnyOrder(1, 2, 3, 4));
    }

    @Test
    public void toMapCollector() {
        Map<String, String> uuidByPersonName = Stream
                .of(
                        person("Foo Foo"),
                        person("Foo Bar"),
                        person("Bar Bar")
                )
                .collect(toMap(Person::getName, Person::getIdentifier));

        assertThat(uuidByPersonName.entrySet(), hasSize(3));
        assertThat(uuidByPersonName.keySet(), containsInAnyOrder("Foo Foo", "Foo Bar", "Bar Bar"));
    }

    @Test
    public void toConcurrentMapCollector() {
        ConcurrentMap<String, String> uuidByPersonName = Stream
                .of(
                        person("Foo Foo"),
                        person("Foo Bar"),
                        person("Bar Bar")
                )
                .collect(toConcurrentMap(Person::getName, Person::getIdentifier));

        assertThat(uuidByPersonName.entrySet(), hasSize(3));
        assertThat(uuidByPersonName.keySet(), containsInAnyOrder("Foo Foo", "Foo Bar", "Bar Bar"));
    }

    @Test(expected = IllegalStateException.class)
    public void toMapCollectorWithDuplicates() {

        //throws exception when duplicated key
        Map<String, String> uuidByPersonName = Stream
                .of(
                        person("Foo Foo"),
                        person("Foo Bar"),
                        person("Bar Bar"),
                        person("Bar Bar")
                )
                .collect(toMap(Person::getName, Person::getIdentifier));
    }

    @Test
    public void toCollectionCollector() {
        SortedSet<Integer> collectedStream = Stream.of(1, 4, 2, 1, 8, 5).collect(toCollection(TreeSet::new));

        assertThat(collectedStream, contains(1, 2, 4, 5, 8));
    }

    @Test
    public void joiningCollector() {
        String joinedStream = Stream.of("joining ", "operation ", "test").collect(joining());

        assertThat(joinedStream, is(equalTo("joining operation test")));
    }

    @Test
    public void joiningWithDelimiterCollector() {
        String joinedStream = Stream.of("one", " two", " three").collect(joining(",", "[", "]"));

        assertThat(joinedStream, is(equalTo("[one, two, three]")));
    }

    @Test
    public void joiningWithDelimiterPrefixAndSuffixCollector() {
        String joinedStream = Stream.of("one", " two", " three").collect(joining(","));

        assertThat(joinedStream, is(equalTo("one, two, three")));
    }

    @Test
    public void averagingIntCollector() {
        double averageWordLength = Stream.of("averaging", "Int", "Collector").collect(averagingInt(String::length));

        assertThat(averageWordLength, is(equalTo(7.0)));
    }

    @Test
    public void collectingAndThenCollector() {
        int wordsQuantity = Stream.of("collecting", "And", "Then").collect(collectingAndThen(toList(), Collection::size));

        assertThat(wordsQuantity, is(equalTo(3)));
    }

    @Test
    public void groupingByCollector() {
        Map<Integer, List<String>> wordsByLength = Stream.of("one", "two", "three", "four", "five")
                .collect(groupingBy(String::length));

        assertThat(wordsByLength.keySet(), containsInAnyOrder(3, 4, 5));
        assertThat(wordsByLength.get(3), containsInAnyOrder("one", "two"));
        assertThat(wordsByLength.get(4), containsInAnyOrder("four", "five"));
        assertThat(wordsByLength.get(5), containsInAnyOrder("three"));
    }

    @Test
    public void groupingByCascadedCollector() {
        Map<Integer, Long> wordsQuantityByLength = Stream.of("one", "two", "three", "four", "five")
                .collect(groupingBy(
                        String::length,
                        counting()));

        assertThat(wordsQuantityByLength.keySet(), containsInAnyOrder(3, 4, 5));
        assertThat(wordsQuantityByLength.get(3), is(equalTo(2L)));
        assertThat(wordsQuantityByLength.get(4), is(equalTo(2L)));
        assertThat(wordsQuantityByLength.get(5), is(equalTo(1L)));
    }

    @Test
    public void partitioningByCollector() {
        Map<Boolean, List<Integer>> numbersByEven = Stream.of(1, 2, 3, 4, 5, 6, 7, 8).collect(partitioningBy(x -> x % 2 == 0));

        assertThat(numbersByEven.get(Boolean.TRUE), containsInAnyOrder(2, 4, 6, 8));
        assertThat(numbersByEven.get(Boolean.FALSE), containsInAnyOrder(1, 3, 5, 7));
    }

    @Test
    public void partitioningByReducingCollector() {
        Map<Boolean, Long> numbersQuantityByEven = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).collect(partitioningBy(x -> x % 2 == 0, counting()));

        assertThat(numbersQuantityByEven.get(Boolean.TRUE), is(equalTo(4L)));
        assertThat(numbersQuantityByEven.get(Boolean.FALSE), is(equalTo(5L)));
    }

    @Test
    public void reducingCollector() {
        Optional<Integer> sum = Stream.of(7, 8, 9).collect(reducing(Integer::sum));

        assertThat(sum.get(), is(equalTo(24)));
    }

    @Test
    public void summingCollector() {
        Double doubledSum = Stream.of(7.5, 8.2, 9.3).collect(summingDouble(x -> x * 2.0));

        assertThat(doubledSum, is(equalTo(50.0)));
    }


    private Person person(String name) {
        return new Person(name);
    }
}
