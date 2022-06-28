package org.example.tasks;

import org.example.domain.ExpressionGenerator;
import org.example.tasks.Task;
import org.example.tasks.TaskProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskProducerTest {

    @Mock
    private ExpressionGenerator expressionGenerator;

    @Test
    void test() throws InterruptedException {
        // given
        TaskProducer taskProducer = new TaskProducer(expressionGenerator, () -> {});
        String expression = "some expression";
        when(expressionGenerator.generateExpression()).thenReturn(expression);

        // when
        String taskName = "some name";
        Task task = taskProducer.produce(taskName);

        // then
        verify(expressionGenerator, times(1)).generateExpression();
        Assertions.assertEquals(expression, task.getExpression());
        Assertions.assertEquals(taskName, task.getName());
    }
}