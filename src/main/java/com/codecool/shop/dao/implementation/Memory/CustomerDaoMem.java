package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.model.Customer;

import java.util.*;

public class CustomerDaoMem implements CustomerDao {

    private static CustomerDaoMem instance = null;
    private List<Customer> data = new ArrayList<>();

    private List<String> checkoutData = Arrays.asList("name", "emailAddress", " telephoneNumber", "countryBill", "cityBill", "zipCodeBill", "addressBill", "countryShip", "cityShip", "zipCodeShip", "addressShip"); // TODO: moves to customer
    private Map<String, String> customerDataMap = new HashMap<>();

    private CustomerDaoMem() {
    }

    public static CustomerDaoMem getInstance() {
        if (instance == null) {
            instance = new CustomerDaoMem();
        }
        return instance;
    }


    @Override
    public void add(Customer objectType) {
        data.add(objectType);
    }

    @Override
    public Customer getCurrent() {
        return data.get(data.size() - 1);
    }


    @Override
    public void createCustomerDataMap() {
        for (int i = 0; i < 11; i++) {
            this.customerDataMap.put(checkoutData.get(i), getCurrent().getCustomerInputData().get(i));
        }
    }

    @Override
    public Map<String, String> getCustomerDataMap() {
        return customerDataMap;
    }


    @Override
    public Customer find(int id) {
        // DOES NOTHING
        return null;
    }

    @Override
    public void remove(int id) {
        // DOES NOTHING
    }

    @Override
    public List<Customer> getAll() {
        // DOES NOTHING
        return null;
    }
}
