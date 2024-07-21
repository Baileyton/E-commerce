package com.stock.service;

import com.stock.entity.Stock;
import com.stock.repository.StockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private PessimisticLockStockService stockService;

//    @Autowired
//    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    // 각 테스트 코드는 독립적으로 존재
    // 다른 테스트 영향을 주면 X
    // 독립적으로
    @BeforeEach
    public void createStock() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    public void deleteDb() {
        stockRepository.deleteAll();
    }

    @Test
    void 재고감소() {
        // given BeforeEach 100개를 생성
        // when
        stockService.decrease(1L, 1L);

        Stock stock = stockRepository.findById(1L).orElseThrow();
        Assertions.assertThat(stock.getQuantity()).isEqualTo(99);
        // then
    }

    @Test
    void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        Assertions.assertThat(stock.getQuantity()).isEqualTo(0);
    }

}