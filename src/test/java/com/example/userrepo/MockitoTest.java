package com.example.userrepo;

import org.junit.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.mockito.Mockito.*;

public class MockitoTest {
    @Test
    public void variousTest() {
        List<String> mockedList = mock(MyList.class);
        verifyNoInteractions(mockedList);

        mockedList.size();
        verify(mockedList).size(); // метод size выполнялся

        verify(mockedList, times(1)).size();

        verify(mockedList, atLeast(1)).size(); // по меньшей мере раз выполнялся
        verify(mockedList, atMost(10)).size(); // выполнялся меньше 10 раз

        // не выполнялись в тесте
        verify(mockedList, never()).get(2);
        verify(mockedList, never()).get(anyInt());
        verify(mockedList, never()).clear();
    }

    static class ListProcessor {
        static void process(List<String> list)
        {
            list.size();
            list.add("hello");
            list.clear();
        }
    }


    @Test
    public  void verifyInOrder()
    {
        List<String> mockedList = mock(MyList.class);
        ListProcessor.process(mockedList);
//        mockedList.size();
//        mockedList.add("hello");
//        mockedList.clear();

        // методы выполнялись именно в этом порядке
        InOrder inOrder = inOrder(mockedList);
        inOrder.verify(mockedList).size();
        inOrder.verify(mockedList).add("hello");
        inOrder.verify(mockedList).clear();
    }



}
