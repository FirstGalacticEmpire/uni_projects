package put.io.testing.mocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import put.io.students.fancylibrary.service.FancyService;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpenseManagerTest {

    List<Expense> createList() {
        List<Expense> expenseList = new ArrayList<>();
        for (int x = 0; x < 3; x++) {
            expenseList.add(new Expense(5));
        }
        return expenseList;
    }

    @Test
    void testCalculateTotal() {
        ExpenseRepository expenseRepository = mock(ExpenseRepository.class);
        List<Expense> expenseList = createList();
        when(expenseRepository.getExpenses()).thenReturn(expenseList);
        ExpenseManager expenseManager = new ExpenseManager(expenseRepository, new FancyService());
        assertEquals(15, expenseManager.calculateTotal());

        when(expenseRepository.getExpensesByCategory("Food")).thenReturn(Collections.emptyList());
        when(expenseRepository.getExpensesByCategory("Sport")).thenReturn(Collections.emptyList());
        when(expenseRepository.getExpensesByCategory("Car")).thenReturn(createList());
        when(expenseRepository.getExpensesByCategory("Home")).thenReturn(createList());
        //assertEquals(0, expenseManager.calculateTotalForCategory("Car"));
        assertEquals(0, expenseManager.calculateTotalForCategory("Sport"));
    }

    @Test
    void testCalculateTotalInDollars() throws ConnectException {
        ExpenseRepository expenseRepository = mock(ExpenseRepository.class);
        List<Expense> expenseList = createList();
        when(expenseRepository.getExpenses()).thenReturn(expenseList);
        FancyService fancyService = mock(FancyService.class);
        when(fancyService.convert(15, "PLN", "USD")).thenReturn(3.75);

        when(fancyService.convert(anyDouble(), anyString(), anyString())).thenAnswer(
                (Answer) invocation -> {
                    double number = invocation.getArgument(0);
                    return number / 4;
                });
        ExpenseManager expenseManager = new ExpenseManager(expenseRepository, fancyService);

        //ExpenseManager expenseManager = new ExpenseManager(expenseRepository, new FancyService());

        //3.0 instead of 3.75 because calculateTotalInDollars() casts to (int)
        assertEquals(3.0, expenseManager.calculateTotalInDollars());


    }

}
