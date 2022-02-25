package put.io.testing.mocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.*;

import put.io.students.fancylibrary.database.IFancyDatabase;

import java.util.Collections;

public class ExpenseRepositoryTest {

    @Test
    void testLoadExpanses() {
        IFancyDatabase mockedDatabase = mock(MyDatabase.class);
        when(mockedDatabase.queryAll()).thenReturn(Collections.emptyList());
        ExpenseRepository expenseRepository = new ExpenseRepository(mockedDatabase);
        expenseRepository.loadExpenses();

        //verifying the sequence (connect, query, close)
        InOrder inOrder = Mockito.inOrder(mockedDatabase);
        inOrder.verify(mockedDatabase, times(1)).connect();
        inOrder.verify(mockedDatabase, times(1)).queryAll();
        inOrder.verify(mockedDatabase, times(1)).close();

        //verifying that the returned list is empty
        assertTrue(expenseRepository.getExpenses().isEmpty());
    }

    @Test
    void testSaveExpanses() {
        IFancyDatabase mockedDatabase = mock(MyDatabase.class);
        when(mockedDatabase.queryAll()).thenReturn(Collections.emptyList());
        ExpenseRepository expenseRepository = new ExpenseRepository(mockedDatabase);
        expenseRepository.loadExpenses();

        //verifying the sequence (connect, query, close)
        InOrder inOrder = Mockito.inOrder(mockedDatabase);
        inOrder.verify(mockedDatabase, times(1)).connect();
        inOrder.verify(mockedDatabase, times(1)).queryAll();
        inOrder.verify(mockedDatabase, times(1)).close();

        //verifying that the returned list is empty
        assertTrue(expenseRepository.getExpenses().isEmpty());

        //Expense expense = new Expense();
        //expenseRepository.addExpense(expense);
        //verify(mockedDatabase, times(1)).persist(new Expense());
        //verify(mockedDatabase, times(1)).persist(expense);
        for (int x = 0; x < 5; x++) {
            expenseRepository.addExpense(new Expense(15));
        }

        expenseRepository.saveExpenses();

        verify(mockedDatabase, times(5)).persist(argThat(Object -> Object.getClass() == Expense.class));


    }
}
