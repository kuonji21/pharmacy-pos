package com.pharmacy.pos.service.impl;

import com.pharmacy.pos.model.Collection;
import com.pharmacy.pos.repository.CollectionRepository;
import com.pharmacy.pos.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    
    @Autowired
    public CollectionServiceImpl(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }
    
    @Override
    public List<Collection> findAllCollections() {
        return collectionRepository.findAll();
    }
    
    @Override
    public Optional<Collection> findCollectionById(Long id) {
        return collectionRepository.findById(id);
    }
    
    @Override
    public List<Collection> findCollectionsByInvoice(String invoice) {
        return collectionRepository.findByInvoice(invoice);
    }
    
    @Override
    public List<Collection> findCollectionsByDate(String date) {
        return collectionRepository.findByDate(date);
    }
    
    @Override
    public List<Collection> findCollectionsByName(String name) {
        return collectionRepository.findByName(name);
    }
    
    @Override
    public List<Collection> findCollectionsByDateRange(String startDate, String endDate) {
        return collectionRepository.findByDateBetween(startDate, endDate);
    }
    
    @Override
    public List<Collection> findCollectionsWithOutstandingBalance(Integer balance) {
        return collectionRepository.findByBalanceGreaterThan(balance);
    }
    
    @Override
    public String calculateTotalCollectionForDate(String date) {
        return collectionRepository.findTotalCollectionByDate(date);
    }
    
    @Override
    public Collection saveCollection(Collection collection) {
        return collectionRepository.save(collection);
    }
    
    @Override
    public void deleteCollection(Long id) {
        collectionRepository.deleteById(id);
    }
}