package org.example.tasks;

import org.example.domain.ParseException;
import org.example.domain.Parser;
import org.example.tasks.ComputationReporter;
import org.example.tasks.Task;
import org.example.tasks.TaskConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskConsumerTest {

    @Mock
    private Parser parser;
    @Mock
    private ComputationReporter computationReporter;
    @Captor
    private ArgumentCaptor<String> expressionForParserCaptor;
    @Captor
    private ArgumentCaptor<String> expressionForReporterCaptor;
    @Captor
    private ArgumentCaptor<Double> computedValueCaptor;
    @Captor
    private ArgumentCaptor<String> exceptionMessageCaptor;

    @Test
    void name() throws InterruptedException, ParseException {
        // given
        TaskConsumer taskConsumer = new TaskConsumer(parser, computationReporter, () -> {});
        Double computedValue = 123.4;
        when(parser.parse(anyString())).thenReturn(computedValue);

        // when
        String taskName = "some name";
        String expression = "some expression";
        Task task = new Task(taskName, expression);
        taskConsumer.consume(task);

        // then
        verify(parser, times(1)).parse(expressionForParserCaptor.capture());
        Assertions.assertEquals(expression, expressionForParserCaptor.getValue());
        verify(computationReporter, times(1))
                .reportComputedValue(expressionForReporterCaptor.capture(), computedValueCaptor.capture());
        Assertions.assertEquals(expression, expressionForReporterCaptor.getValue());
        Assertions.assertEquals(computedValue, computedValueCaptor.getValue());
    }

    @Test
    void testParserException() throws ParseException, InterruptedException {
        // given
        TaskConsumer taskConsumer = new TaskConsumer(parser, computationReporter, () -> {});
        String exceptionMessage = "some message";
        when(parser.parse(anyString())).thenThrow(new ParseException(exceptionMessage));

        // when
        String taskName = "some name";
        String expression = "some expression";
        Task task = new Task(taskName, expression);
        taskConsumer.consume(task);

        // then
        verify(computationReporter, times(1))
                .reportError(expressionForReporterCaptor.capture(), exceptionMessageCaptor.capture());
        Assertions.assertEquals(expression, expressionForReporterCaptor.getValue());
        Assertions.assertEquals(exceptionMessage, exceptionMessageCaptor.getValue());
    }
}