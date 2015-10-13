package com.jj.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.jj.ents.Transaction;
import com.jj.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by iuliana.cosmina on 10/13/15.
 */
@Service
public class TransactionManager {
    private Logger logger = LoggerFactory.getLogger(TransactionManager.class);


    private Map<Long, Transaction> storageById = new HashMap<Long, Transaction>();
    private Multimap<String, Transaction> storageByType = ArrayListMultimap.create();

    public TransactionManager() {
        logger.info(">> TransactionManager created");
    }

    @Autowired
    IdGenerator generator;

    /**
     * Put a few transactions in so we have data to play with
     */
    @PostConstruct
    protected void init(){
        Transaction t1 = buildTransaction(2000D, "food", null);
        storageById.put(t1.getId(), t1);
        storageByType.put(t1.getType(), t1);

        Transaction t2 = buildTransaction(3000D, "food", null);
        storageById.put(t2.getId(), t2);
        storageByType.put(t2.getType(), t2);

        Transaction t4 = buildTransaction(5000D, "cars", 1L);
        storageById.put(t4.getId(), t4);
        storageByType.put(t4.getType(), t4);

        Transaction t5 = buildTransaction(10000D, "cars", 1L);
        storageById.put(t5.getId(), t5);
        storageByType.put(t5.getType(), t5);

        logger.info(">> TransactionManager initialized");
    }

    public boolean save(Transaction transaction) {
        boolean newTransaction = true;
        if (storageById.containsKey(transaction.getId())) {
            newTransaction = false;
            storageById.put(transaction.getId(), transaction);
            logger.info("Updating exising entry with id:" + transaction.getId());
        } else {
            transaction.setId(generator.getNextId());
            storageById.put(transaction.getId(), transaction);
            storageByType.put(transaction.getType(), transaction);
            logger.info("Creating new entry with id:" + transaction.getId());
        }
        return newTransaction;
    }


    public boolean remove(Transaction transaction) {
        boolean removed1 = storageById.remove(transaction.getId(), transaction);
        boolean removed2 = storageByType.remove(transaction.getId(), transaction);
        return removed1 && removed2;
    }

    public Transaction getById(Long id) {
        return storageById.get(id);
    }

    public Collection<Transaction> getByType(String type) {
        return storageByType.get(type);
    }

    public List<Long> getIdsByType(String type) {
        List<Long> result = new ArrayList<>();
        for(Transaction t : storageByType.get(type)) {
            result.add(t.getId());
        }
        return result;
    }

    public Double sum(Long parentId) {
        double sum = 0D;
        for (Map.Entry<Long, Transaction> entry : storageById.entrySet()) {
            Transaction transaction = entry.getValue();
            if(parentId.equals(transaction.getParentId())){
                sum +=transaction.getAmount();
            }
        }
        return sum;
    }

    private Transaction buildTransaction(Double amount, String type, Long parentId){
        Transaction transaction = new Transaction();
        transaction.setId(generator.getNextId());
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setParentId(parentId);
        return transaction;
    }

}
