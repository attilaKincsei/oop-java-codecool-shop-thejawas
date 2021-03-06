package com.codecool.shop.dao.implementation.Memory;


import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDaoMem implements ProductDao {

    private List<Product> data = new ArrayList<>();
    private static ProductDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductDaoMem() {
    }

    public static ProductDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductDaoMem();
        }
        return instance;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }

    @Override
    public void add(Product product) {
        data.add(product);
    }

    @Override
    public Product find(int id) {
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public List<Product> getAll() {
        return data;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        return data.stream().filter(t -> t.getSupplier().getId() == supplier.getId()).collect(Collectors.toList());
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return data.stream().filter(t -> t.getProductCategory().getId() == (productCategory.getId())).collect(Collectors.toList());
    }

    @Override
    public Product getBy(int id){
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Product> getBy(String keyWord){
        List<Product> products = new ArrayList<>();
        for(Product product : data){
            if(product.getName().toLowerCase().contains(keyWord) || product.getDescription().toLowerCase().contains(keyWord))
                products.add(product);
        }

        return products;
    }



}