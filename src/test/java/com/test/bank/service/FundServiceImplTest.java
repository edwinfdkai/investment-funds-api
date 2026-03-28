package com.test.bank.service;

import com.test.bank.dto.CancelRequest;
import com.test.bank.dto.SubscriptionRequest;
import com.test.bank.model.Fund;
import com.test.bank.repository.FundRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FundServiceImplTest {

    @Mock
    private FundSubscriptionService fundSubscriptionService;

    @Mock
    private FundCancellationService fundCancellationService;

    @Mock
    private FundRepository fundRepository;

    @InjectMocks
    private FundServiceImpl fundService;

    @Test
    void subscribeToFund_delegatesToSubscriptionService() {
        SubscriptionRequest request = new SubscriptionRequest();

        fundService.subscribeToFund(request);

        verify(fundSubscriptionService).subscribe(request);
    }

    @Test
    void cancelFund_delegatesToCancellationService() {
        CancelRequest request = new CancelRequest();

        fundService.cancelFund(request);

        verify(fundCancellationService).cancel(request);
    }

    @Test
    void getFunds_returnsRepositoryResult() {
        Fund fund = new Fund("1", "F", 100L, "C");
        when(fundRepository.findAll()).thenReturn(List.of(fund));

        List<Fund> result = fundService.getFunds();

        assertEquals(1, result.size());
        assertEquals("F", result.get(0).getName());
        verify(fundRepository).findAll();
    }
}
