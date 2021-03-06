package com.codecool.shop.dao.implementation.postgresql;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.postgresql.query_util.ProductQueryHandler;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ani on 2016.11.13..
 */
public class ProductDaoSql extends ProductQueryHandler implements ProductDao {

    private static ProductDaoSql singletonInstance = null;

    private ProductDaoSql() {
    }

    public static ProductDaoSql getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new ProductDaoSql();
        }
        return singletonInstance;
    }

    @Override
    public void add(Product product) {
        String prePreparedQuery = "INSERT INTO product (id, name, description, default_price, default_currency, product_category_id, supplier_id) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);";

        insertProductWithValidation(
                prePreparedQuery,
                product.getName(),
                product.getDescription(),
                product.getDefaultPrice(),
                String.valueOf(product.getDefaultCurrency()),
                product.getProductCategory().getId(),
                product.getSupplier().getId()
        );
    }

    @Override
    public Product find(int id) {
        String query = "SELECT * FROM product WHERE id = " + id + ";";

        Product product;
        int numberOfProductsReturned = getProducts(query).size();
        if (getProducts(query).isEmpty()) {
            product = null;
        } else {
            product = getProducts(query).get(numberOfProductsReturned - 1);
            if (numberOfProductsReturned > 1) {
                throw new IllegalArgumentException("There are more than one product with the same id");
            }
        }

        return product;
    }


    @Override
    public void remove(int id) {
        String query = "DELETE FROM product WHERE id ='" + id + "';";
        DMLexecute(query);
    }

    @Override
    public List<Product> getAll() {
        String query = "SELECT * FROM product;";

        List<Product> productList;
        if (getProducts(query).isEmpty()) {
            productList = null;
        } else {
            productList = getProducts(query);
        }


        return productList;

    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        String query = "SELECT * FROM product WHERE supplier_id ='" + supplier.getId() + "';";
        return getProducts(query);

    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        String query = "SELECT * FROM product WHERE product_category_id ='" + productCategory.getId() + "';";
        return getProducts(query);
    }

    @Override
    public Product getBy(int id) {
        String query = "SELECT * FROM product WHERE id ='" + id + "';";
        return getProducts(query).get(0);
    }

    @Override
    public List<Product> getBy(String keyWord) {

        List<Product> products = new ArrayList<>();

        try {
            for(Product product : getAll()){
                if(product.getName().toLowerCase().contains(keyWord) || product.getDescription().toLowerCase().contains(keyWord))
                    products.add(product);
            }
        } catch (NullPointerException npe) {
            System.out.println("No product found while dynamic searchin");
        }

        return products;
    }
}
