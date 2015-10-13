package com.jj.web;

import com.jj.ents.Transaction;
import com.jj.service.SumObject;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.Assert.*;

/**
 * Created by iuliana.cosmina on 10/14/15.
 */
public class TransactionControllerTest {

    private Logger logger = LoggerFactory.getLogger(TransactionControllerTest.class);


    /**
     * Rest Services endpoints
     */
    private static final String HELLO_URL = "http://localhost:8080/hello";
    private static final String GET_POST_URL = "http://localhost:8080/transactionservice/transaction/{$transaction_id}";
    private static final String PUT_URL = "http://localhost:8080/transactionservice/transaction/update/{$transaction_id}";
    private static final String GET_BY_TYPE_URL = "http://localhost:8080//transactionservice/types/{$type}";
    private static final String GET_SUM_URL = "http://localhost:8080/transactionservice/sum/{$transaction_id}";

    private RestTemplate restTemplate = null;


    @Before
    public void setUp() {
        restTemplate = new RestTemplate();
    }


    @Test
    public void getHello() {
        String content = restTemplate.getForObject(HELLO_URL, String.class);

        // Initially 16 persons in database, but will go up if you rerun this test multiple times
        assertTrue(content.length() > 0);
    }

    @Test
    public void getById() {
        Transaction transaction = restTemplate.getForObject(GET_POST_URL, Transaction.class, "1");

        assertNotNull(transaction);
        assertTrue(2000D == transaction.getAmount());
        assertEquals("food", transaction.getType());
    }

    /**
     * negative test - search for non existent transaction
     *
     * @throws Exception
     */
    @Test(expected = HttpClientErrorException.class)
    public void tryById() throws Exception {
        restTemplate.getForObject(GET_POST_URL, Transaction.class, "100");
    }


    @Test
    public void getByType() {
        Transaction[] transactions = restTemplate.getForObject(GET_BY_TYPE_URL, Transaction[].class, "food");

        assertNotNull(transactions);
        assertEquals(2, transactions.length);
    }

    /**
     * negative test - search for non existent type
     */
    @Test(expected = HttpClientErrorException.class)
    public void tryByType() {
        restTemplate.getForObject(GET_BY_TYPE_URL, Transaction[].class, "bubu");
    }

    @Test
    public void getSum() {
        SumObject sum = restTemplate.getForObject(GET_SUM_URL, SumObject.class, "1");

        assertTrue(15000D == sum.getSum());
    }

    /**
     * Test PUT
     */
    @Test
    public void editTransaction() {
        Transaction transaction = mockTransaction(111D, "test", null);
        transaction.setId(2L);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<Transaction> trRequest = new HttpEntity<>(transaction, headers);
        ResponseEntity<Transaction> responseEntity = restTemplate.exchange(PUT_URL, HttpMethod.PUT, trRequest, Transaction.class, "2");

        Transaction editedTr = responseEntity.getBody();
        assertNotNull(editedTr);
        assertTrue(111D == editedTr.getAmount());
        assertEquals("test", editedTr.getType());
    }

    //Test POST
    @Test
    public void createTransaction() {
        Transaction transaction = mockTransaction(222D, "test", null);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<Transaction> trRequest = new HttpEntity<>(transaction, headers);
        URI uri = this.restTemplate.postForLocation(GET_POST_URL, trRequest, Transaction.class);
        logger.info(">> Location for new transaction: " + uri);
        // test insertion
        Transaction[] transactions = restTemplate.getForObject(GET_BY_TYPE_URL, Transaction[].class, "test");

        assertNotNull(transactions);
        assertTrue(transactions.length > 0);

    }

    private Transaction mockTransaction(Double amount, String type, Long parentId){
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setParentId(parentId);
        return transaction;
    }


}
