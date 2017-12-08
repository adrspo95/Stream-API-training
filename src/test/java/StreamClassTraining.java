import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class StreamClassTraining {

    @Test
    public void allMatch() {
        Predicate<Integer> lessThanThousand = integer -> integer < 1000;
        Predicate<Integer> greaterThanFifty = integer -> integer > 50;

        boolean allLessThanThousand = oneToHundredIntegers().allMatch(lessThanThousand);
        boolean allGreaterThanFifty = oneToHundredIntegers().allMatch(greaterThanFifty);

        //allMatch always evaluates to true is applied on empty stream
        boolean allMatchIfEmptyStream = emptyStream().allMatch(greaterThanFifty);

        assertFalse(allGreaterThanFifty);
        assertTrue(allLessThanThousand);
        assertTrue(allMatchIfEmptyStream);
    }

    @Test
    public void anyMatch() {
        Predicate<Integer> equalsHundred = integer -> integer == 100;
        Predicate<Integer> equalsThousand = integer -> integer == 1000;

        boolean anyEqualsHundred = oneToHundredIntegers().anyMatch(equalsHundred);
        boolean anyEqualsThousand = oneToHundredIntegers().anyMatch(equalsThousand);

        //anyMatch always evaluates to true is applied on empty stream
        boolean anyMatchIfEmptyStream = emptyStream().anyMatch(equalsHundred);

        assertFalse(anyEqualsThousand);
        assertTrue(anyEqualsHundred);
        assertFalse(anyMatchIfEmptyStream);
    }

    @Test
    public void noneMatch() {
        Predicate<Integer> equalsHundred = integer -> integer == 100;
        Predicate<Integer> equalsThousand = integer -> integer == 1000;

        boolean noneEqualsHundred = oneToHundredIntegers().noneMatch(equalsHundred);
        boolean noneEqualsThousand = oneToHundredIntegers().noneMatch(equalsThousand);

        //noneMatch always evaluates to true is applied on empty stream
        boolean noneMatchIfEmptyStream = emptyStream().noneMatch(equalsHundred);

        assertTrue(noneEqualsThousand);
        assertFalse(noneEqualsHundred);
        assertTrue(noneMatchIfEmptyStream);
    }

    @Test
    public void count() {
        long elementsQantity = oneToHundredIntegers().count();
        long emptyStreamQuantity = emptyStream().count();

        assertThat(elementsQantity, is(equalTo(100L)));
        assertThat(emptyStreamQuantity, is(equalTo(0L)));
    }

    @Test
    public void findAny() {
        Optional<Integer> anyElement = oneToHundredIntegers().findAny();

        assertTrue(anyElement.isPresent());
    }

    @Test
    public void findFirst() {
        Optional<Integer> firstElement = oneToHundredIntegers().findFirst();
        //ensure stream is ordered

        assertTrue(firstElement.isPresent());
        assertThat(firstElement.get(), is(equalTo(1)));
    }

    @Test
    public void concat() {
        Stream<Integer> goldenStream = IntStream.rangeClosed(1, 200).mapToObj(Integer::valueOf);

        Stream<Integer> concatenatedStream = Stream.concat(
                oneToHundredIntegers(),
                IntStream.rangeClosed(101, 200).mapToObj(Integer::valueOf)
        );

        assertThat(
                concatenatedStream.collect(toList()),
                is(equalTo(goldenStream.collect(toList()))));
    }

    @Test
    public void distinct() {
        Stream<Integer> duplicatedStream = Stream.concat(
                oneToHundredIntegers(),
                oneToHundredIntegers()
        );

        Stream<Integer> distinctedStream = duplicatedStream.distinct();

        assertThat(
                distinctedStream.collect(toList()),
                is(equalTo(oneToHundredIntegers().collect(toList()))));
    }

    @Test
    public void limit() {
        Stream<Integer> oneToFiftyStream = IntStream.rangeClosed(1, 50).mapToObj(Integer::valueOf);

        Stream<Integer> streamLimitedToFiftyElements = oneToHundredIntegers().limit(50);

        assertThat(
                streamLimitedToFiftyElements.collect(toList()),
                is(equalTo(oneToFiftyStream.collect(toList()))));
    }

    @Test
    public void skip() {
        Stream<Integer> fiftyOneToHundredStream = IntStream.rangeClosed(51, 100).mapToObj(Integer::valueOf);

        Stream<Integer> streamWithFiftyElementsSkipped = oneToHundredIntegers().skip(50);
        Stream<Integer> streamWithHundredElementsSkipped = oneToHundredIntegers().skip(100);

        assertThat(
                streamWithFiftyElementsSkipped.collect(toList()),
                is(equalTo(fiftyOneToHundredStream.collect(toList()))));
        assertThat(
                streamWithHundredElementsSkipped.count(),
                is(equalTo(0L)));
    }

    @Test
    public void min() {
        Optional<Integer> minValue = oneToHundredIntegers().min(Integer::compare);

        assertThat(
                minValue.get(),
                is(equalTo(1)));
    }

    @Test(expected = NullPointerException.class)
    public void minWithNull() {
        Stream<Integer> streamWithNullValue = Stream.of(1, 2, 3, null);

        //throws NPE when min value is null
        streamWithNullValue.min(Integer::compare);
    }

    @Test
    public void max() {
        Optional<Integer> maxValue = oneToHundredIntegers().max(Integer::compare);

        assertThat(
                maxValue.get(),
                is(equalTo(100)));
    }

    @Test(expected = NullPointerException.class)
    public void maxWithNull() {
        Stream<Integer> streamWithNullValue = Stream.of(-1, -2, -3, null);

        //throws NPE when max value is null
        streamWithNullValue.max(Integer::compare);
    }

    @Test
    public void filter() {
        Predicate<Integer> divisibleByTwenty = x -> x % 20 == 0;

        Stream<Integer> filteredElements = oneToHundredIntegers().filter(divisibleByTwenty);

        assertThat(
                filteredElements.collect(toList()),
                contains(20, 40, 60, 80, 100));
    }

    @Test
    public void map() {
        Stream<String> stringValues = Stream.of("1", "2", "3");

        Stream<Integer> mappedToInteger = stringValues.map(Integer::parseInt);

        assertThat(
                mappedToInteger.collect(toList()),
                contains(1, 2, 3));
    }

    @Test
    public void mapToDouble() {
        Stream<String> stringValues = Stream.of("1", "2", "3");

        DoubleStream mappedToDoubleStream = stringValues.mapToDouble(Double::parseDouble);

        OptionalDouble average = mappedToDoubleStream.average();

        assertThat(average.getAsDouble(), is(equalTo(2.0)));
    }

    @Test
    public void flatMap() {
        Stream<String> stringValues = Stream.of("flat", "Map");

        Stream<Character> flatMappedStream = stringValues.flatMap(s ->
                s.chars()
                        .boxed()
                        .map(c -> (char) c.intValue()));

        assertThat(flatMappedStream.collect(toList()), contains('f', 'l', 'a', 't', 'M', 'a', 'p'));
    }

    @Test
    public void sorted() {
        Stream<Integer> integerStream = Stream.of(2, 1, -3);

        assertThat(integerStream.spliterator().hasCharacteristics(Spliterator.SORTED), is(false));

        integerStream = Stream.of(2, 1, -3).sorted();

        assertThat(integerStream.collect(toList()), contains(-3, 1, 2));
    }

    @Test
    public void sortedWithComparator() {
        Stream<Integer> integerStream = Stream.of(0, 1, -3).sorted(Comparator.reverseOrder());

        assertThat(integerStream.collect(toList()), contains(1, 0, -3));
    }

    @Test
    public void peek() {
        List<String> list = Arrays.asList("peek", "operation", "test");
        List<String> containerList = new ArrayList<>();

        list
                .stream()
                .peek(containerList::add)
                .forEach(System.out::println);

        assertThat(containerList, contains(list.toArray()));
    }

    @Test
    public void peekWithShortCircuit() {
        List<String> list = Arrays.asList("peek", "operation", "test");
        List<String> containerList = new ArrayList<>();

        list
                .stream()
                .peek(containerList::add)
                .count();

        assertThat(containerList, is(empty()));
    }

    @Test
    public void takeWhile() {
        Stream<Integer> integerStream = Stream.of(2, 6, 12, 3, 4, 2, 8, 4, 6).takeWhile(i -> i % 2 == 0);

        assertThat(integerStream.collect(toList()), contains(2, 6, 12));
    }

    @Test
    public void dropWhile() {
        Stream<Integer> integerStream = Stream.of(2, 6, 12, 3, 4, 2, 8, 4, 6).dropWhile(i -> i % 2 == 0);

        assertThat(integerStream.collect(toList()), contains(3, 4, 2, 8, 4, 6));
    }

    @Test
    public void forEach() {
        List<String> list = Arrays.asList("for", "Each", "test");
        List<String> containerList = new ArrayList<>();

        list
                .stream()
                .forEach(containerList::add);

        assertThat(containerList, containsInAnyOrder(list.toArray()));
    }

    @Test
    public void forEachOrdered() {
        List<Integer> list = Arrays.asList(5, 2, 7, 3);
        List<Integer> containerList = new ArrayList<>();

        list
                .stream()
                .forEachOrdered(containerList::add);

        assertThat(containerList, contains(list.toArray()));
    }

    @Test
    public void reduceTwoArgs() {
        int sum = Stream.of(5, 2, 7, 3).reduce(100, (x, y) -> x + y);

        assertThat(sum, is(equalTo(117)));
    }

    @Test
    public void reduceOneArg() {
        Optional<Integer> sum = Stream.of(5, 2, 7, 3).reduce((x, y) -> x + y);

        assertThat(sum.get(), is(equalTo(17)));
    }

    @Test
    public void reduceThreeArg() {
        Integer sum = IntStream
                .rangeClosed(1, 1000000)
                .mapToObj(Integer::toString)
                .parallel()
                .reduce(0,
                        (x, y) -> x + Integer.parseInt(y),
                        Integer::sum);

        assertThat(sum, is(equalTo(17)));
    }

    @Test
    public void collect() {
        List<Integer> collectedStreamList = Stream
                .of(1, 2, 3)
                .collect(ArrayList::new,
                        ArrayList::add,
                        ArrayList::addAll);

        assertThat(collectedStreamList, contains(1, 2, 3));
    }

    @Test
    public void ofNullable() {
        Stream<Integer> streamOfElement = Stream.ofNullable(1);
        Stream<Integer> streamOfNull = Stream.ofNullable(null);

        assertThat(streamOfElement.collect(toList()), contains(1));
        assertThat(streamOfNull.collect(toList()), is(empty()));
    }

    @Test
    public void iterate() {
        Stream<Integer> stream = Stream
                .iterate(2, x -> x * 2)
                .limit(5);

        assertThat(stream.collect(toList()), contains(2, 4, 8, 16, 32));
    }

    @Test
    public void iterateThreeArguments() {
        Stream<Integer> stream = Stream
                .iterate(2, x -> x <= 32, x -> x * 2);

        assertThat(stream.collect(toList()), contains(2, 4, 8, 16, 32));
    }

    @Test
    public void generate() {
        Stream
                .generate(() -> new Random().nextInt())
                .limit(5);
    }


    private Stream<Integer> oneToHundredIntegers() {
        return IntStream
                .rangeClosed(1, 100)
                .mapToObj(Integer::valueOf)
                .unordered();
    }

    private Stream<Integer> emptyStream() {
        return Stream.empty();
    }
}
