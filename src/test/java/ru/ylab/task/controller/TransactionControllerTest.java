package ru.ylab.task.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import ru.ylab.task.dto.TransactionDto;
import ru.ylab.task.service.WalletService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private WalletService walletService;

    @Mock
    private HttpSession session = new MockHttpSession();

    @InjectMocks
    private TransactionController transactionController;

    @Test
    public void testActivateTransactionWhenAuthenticatedThenSuccess() {
        TransactionDto dto = new TransactionDto();
        when(session.getAttribute("id")).thenReturn(1L);
        ResponseEntity responseEntity = transactionController.activateTransaction(session, dto);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(walletService, times(1)).activateTransaction(dto);
    }

    @Test
    public void testActivateTransactionWhenNotAuthenticatedThenUnauthorized() {
        TransactionDto dto = new TransactionDto();
        when(session.getAttribute("id")).thenReturn(null);
        ResponseEntity responseEntity = transactionController.activateTransaction(session, dto);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verify(walletService, times(0)).activateTransaction(dto);
    }

    @Test
    public void testGetTransactionHistoryWhenAuthenticatedThenSuccess() {
        when(session.getAttribute("id")).thenReturn(1L);
        when(walletService.findTransactionHistory(1L)).thenReturn(Collections.singletonList(new TransactionDto()));

        ResponseEntity responseEntity = transactionController.getTransactionHistory(session);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(walletService, times(1)).findTransactionHistory(1L);
    }

    @Test
    public void testGetTransactionHistoryWhenNotAuthenticatedThenUnauthorized() {
        when(session.getAttribute("id")).thenReturn(null);
        ResponseEntity responseEntity = transactionController.getTransactionHistory(session);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verify(walletService, times(0)).findTransactionHistory(anyLong());
    }
}
