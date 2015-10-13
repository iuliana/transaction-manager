package com.jj.web;

import com.jj.ents.Transaction;
import com.jj.exs.TransactionException;
import com.jj.service.SumObject;
import com.jj.service.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Created by iuliana.cosmina on 10/13/15.
 */
@RestController
@RequestMapping("/transactionservice")
public class TransactionController {

    TransactionManager manager;

    /**
     * Adding setter so this class can be testable
     *
     * @param manager
     */
    @Autowired
    public void setManager(TransactionManager manager) {
        this.manager = manager;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/transaction/{$transaction_id}", method = RequestMethod.POST)
    public void newTransaction(@RequestBody Transaction newTransaction, @Value("#{request.requestURL}")
    StringBuffer originalUrl, HttpServletResponse response) throws TransactionException {
        if (newTransaction.getId() != null) {
            throw new TransactionException("Transaction found with id " + newTransaction.getId() + ". Cannot create!");
        }
        manager.save(newTransaction);
        response.setHeader("Location", getLocationForTransaction(originalUrl, newTransaction.getId()));
    }

    /**
     * Determines URL of transaction resource based on the full URL of the given request,
     * appending the path info with the given childIdentifier using a UriTemplate.
     */
    protected static String getLocationForTransaction(StringBuffer url, Object childIdentifier) {
        UriTemplate template = new UriTemplate(url.toString());
        return template.expand(childIdentifier).toASCIIString();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/transaction/update/{$transaction_id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Transaction updateById(@PathVariable("$transaction_id") Long id, @RequestBody Transaction newTransaction) throws TransactionException {
        Transaction transaction = manager.getById(id);
        if (transaction == null) {
            throw new TransactionException("Transaction not found with id " + id);
        }
        manager.save(newTransaction);
        return newTransaction;

    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/transaction/{$transaction_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Transaction getById(@PathVariable("$transaction_id") Long id) throws TransactionException {
        Transaction transaction = manager.getById(id);
        if (transaction == null) {
            throw new TransactionException("Transaction not found with id " + id);
        }
        return transaction;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/types/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Transaction> getByType(@PathVariable String type) throws TransactionException {
        Collection<Transaction> result = manager.getByType(type);
        if (result == null || result.isEmpty()) {
            throw new TransactionException("No transaction found for type " + type);
        }
        return result;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/sum/{$transaction_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SumObject getSumByParent(@PathVariable("$transaction_id") Long parentId) throws TransactionException {
        return new SumObject(manager.sum(parentId));
    }
}
